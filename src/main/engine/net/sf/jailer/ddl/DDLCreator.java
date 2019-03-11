/*
 * Copyright 2007 - 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jailer.ddl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import net.sf.jailer.ExecutionContext;
import net.sf.jailer.JailerVersion;
import net.sf.jailer.configuration.Configuration;
import net.sf.jailer.configuration.DBMS;
import net.sf.jailer.database.SQLDialect;
import net.sf.jailer.database.Session;
import net.sf.jailer.database.TemporaryTableManager;
import net.sf.jailer.database.WorkingTableScope;
import net.sf.jailer.datamodel.Column;
import net.sf.jailer.datamodel.DataModel;
import net.sf.jailer.datamodel.RowIdSupport;
import net.sf.jailer.util.PrintUtil;
import net.sf.jailer.util.Quoting;
import net.sf.jailer.util.SqlScriptExecutor;
import net.sf.jailer.util.SqlUtil;

/**
 * Creates the DDL for the working-tables.
 * 
 * @author Ralf Wisser
 */
public class DDLCreator {
	
	/**
	 * The execution context.
	 */
	private final ExecutionContext executionContext;
	
	/**
	 * Constructor.
	 * 
	 * @param executionContext the command line arguments
	 */
	public DDLCreator(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}
	
	/**
	 * Creates the DDL for the working-tables.
	 */
	public boolean createDDL(DataSource dataSource, DBMS dbms, WorkingTableScope temporaryTableScope, String workingTableSchema) throws SQLException, FileNotFoundException, IOException {
		Session session = null;
		if (dataSource != null) {
			session = new Session(dataSource, dbms, executionContext.getIsolationLevel());
		}
		try {
			return createDDL(new DataModel(executionContext), session, temporaryTableScope, workingTableSchema);
		} finally {
			if (session != null) {
				try { session.shutDown(); } catch (Exception e) { /* ignore */ }
			}
		}
	}

	/**
	 * Creates the DDL for the working-tables.
	 */
	public void createDDL(Session localSession, WorkingTableScope temporaryTableScope, String workingTableSchema) throws FileNotFoundException, IOException, SQLException {
		// TODO register all current export processes.
		// Fail if a process is still active.
		// Use a heard beat concept to detect dead processes 
		createDDL(new DataModel(executionContext), localSession, temporaryTableScope, workingTableSchema);
	}

	/**
	 * Creates the DDL for the working-tables.
	 */
	public boolean createDDL(DataModel datamodel, Session session, WorkingTableScope temporaryTableScope, String workingTableSchema) throws FileNotFoundException, IOException, SQLException {
		RowIdSupport rowIdSupport = new RowIdSupport(datamodel, targetDBMS(session), executionContext);
		return createDDL(datamodel, session, temporaryTableScope, rowIdSupport, workingTableSchema);
	}

	/**
	 * Creates the DDL for the working-tables.
	 */
	public boolean createDDL(DataModel datamodel, Session session, WorkingTableScope temporaryTableScope, RowIdSupport rowIdSupport, String workingTableSchema) throws FileNotFoundException, IOException, SQLException {
		uPKWasTooLong = false;
		try {
			return createDDL(datamodel, session, temporaryTableScope, 0, rowIdSupport, workingTableSchema);
		} catch (SQLException e) {
			uPKWasTooLong = true;
			try {
				// [bugs:#37] PostreSQL: transactional execution
				session.getConnection().commit();
			} catch (SQLException e1) {
				// ignore
			}
		}
		// reconnect and retry with another index type
		session.reconnect();
		try {
			return createDDL(datamodel, session, temporaryTableScope, 1, rowIdSupport, workingTableSchema);
		} catch (SQLException e) {
			try {
				// [bugs:#37] PostreSQL: transactional execution
				session.getConnection().commit();
			} catch (SQLException e1) {
				// ignore
			}
		}
		// reconnect and retry with another index type
		session.reconnect();
		return createDDL(datamodel, session, temporaryTableScope, 2, rowIdSupport, workingTableSchema);
	}

	public static boolean uPKWasTooLong = false;

	/**
	 * Creates the DDL for the working-tables.
	 */
	private boolean createDDL(DataModel dataModel, Session session, WorkingTableScope temporaryTableScope, int indexType, RowIdSupport rowIdSupport, String workingTableSchema) throws FileNotFoundException, IOException, SQLException {
		String template = "script" + File.separator + "ddl-template.sql";
		String contraint = pkColumnConstraint(session);
		Map<String, String> typeReplacement = targetDBMS(session).getTypeReplacement();
		String universalPrimaryKey = rowIdSupport.getUniversalPrimaryKey().toSQL(null, contraint, typeReplacement);
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put("upk", universalPrimaryKey);
		arguments.put("upk-hash", "" + ((universalPrimaryKey + targetDBMS(session).getTableProperties()).hashCode()));
		arguments.put("pre", rowIdSupport.getUniversalPrimaryKey().toSQL("PRE_", contraint, typeReplacement));
		arguments.put("from", rowIdSupport.getUniversalPrimaryKey().toSQL("FROM_", contraint, typeReplacement));
		arguments.put("to", rowIdSupport.getUniversalPrimaryKey().toSQL("TO_", contraint, typeReplacement));
		arguments.put("version", "" + JailerVersion.WORKING_TABLE_VERSION);
		arguments.put("constraint", contraint);

		TemporaryTableManager tableManager = null;
		if (temporaryTableScope == WorkingTableScope.SESSION_LOCAL) {
			tableManager = targetDBMS(session).getSessionTemporaryTableManager();
		}
		if (temporaryTableScope == WorkingTableScope.TRANSACTION_LOCAL) {
			tableManager = targetDBMS(session).getTransactionTemporaryTableManager();
		}
		String tableName = SQLDialect.CONFIG_TABLE_;
		arguments.put("config-dml-reference", tableName);
		String schema = workingTableSchema != null? (session == null? workingTableSchema : new Quoting(session).requote(workingTableSchema)) + "." : "";
		arguments.put("schema", schema);
		arguments.put("index-schema", supportsSchemasInIndexDefinitions(session)? schema : "");
		if (tableManager != null) {
			arguments.put("table-suffix", "_T");
			arguments.put("drop-table", tableManager.getDropTablePrefix());
			arguments.put("create-table", tableManager.getCreateTablePrefix());
			arguments.put("create-table-suffix", tableManager.getCreateTableSuffix());
			arguments.put("create-index", tableManager.getCreateIndexPrefix());
			arguments.put("create-index-suffix", tableManager.getCreateIndexSuffix());
			arguments.put("index-table-prefix", tableManager.getIndexTablePrefix());
			arguments.put("schema", schema + tableManager.getDdlTableReferencePrefix());
		} else {
			arguments.put("table-suffix", "");
			arguments.put("drop-table", "DROP TABLE ");
			arguments.put("create-table", "CREATE TABLE ");
			arguments.put("create-table-suffix", targetDBMS(session).getTableProperties());
			arguments.put("create-index", "CREATE INDEX ");
			arguments.put("create-index-suffix", "");
			arguments.put("index-table-prefix", "");
		}

		Map<String, List<String>> listArguments = new HashMap<String, List<String>>();
		if (indexType == 0) {
			// full index
			listArguments.put("column-list", Collections.singletonList(", " + rowIdSupport.getUniversalPrimaryKey().columnList(null)));
			listArguments.put("column-list-from", Collections.singletonList(", " + rowIdSupport.getUniversalPrimaryKey().columnList("FROM_")));
			listArguments.put("column-list-to", Collections.singletonList(", " + rowIdSupport.getUniversalPrimaryKey().columnList("TO_")));
		} else if (indexType == 1) {
			// single column indexes
			List<String> cl = new ArrayList<String>();
			List<String> clFrom = new ArrayList<String>();
			List<String> clTo = new ArrayList<String>();
			for (Column c : rowIdSupport.getUniversalPrimaryKey().getColumns()) {
				cl.add(", " + c.name);
				clFrom.add(", FROM_" + c.name);
				clTo.add(", FROM_" + c.name);
			}
			listArguments.put("column-list", cl);
			listArguments.put("column-list-from", clFrom);
			listArguments.put("column-list-to", clTo);
		} else {
			// minimal index
			listArguments.put("column-list", Collections.singletonList(""));
			listArguments.put("column-list-from", Collections.singletonList(""));
			listArguments.put("column-list-to", Collections.singletonList(""));
		}
		String ddl = new PrintUtil().applyTemplate(template, arguments, listArguments);

		if (session != null) {
			File tmp = Configuration.getInstance().createTempFile();
			PrintWriter pw = new PrintWriter(tmp);
			pw.println(ddl);
			pw.close();
			new SqlScriptExecutor(session, 1).executeScript(tmp.getPath());
			tmp.delete();
		} else {
			System.out.println(ddl);
		}

		return true;
	}

	private boolean supportsSchemasInIndexDefinitions(Session session) {
		Boolean result = targetDBMS(session).getSupportsSchemasInIndexDefinitions();
		if (result == null) {
			if (session == null) {
				return true;
			}
			try {
				result = session.getMetaData().supportsSchemasInDataManipulation();
			} catch (SQLException e) {
				return false;
			}
		}
		return result;
	}

	private String pkColumnConstraint(Session session) {
		String nullableContraint = targetDBMS(session).getNullableContraint();
		if (nullableContraint != null) {
			return " " + nullableContraint;
		}
		return "";
	}

	private DBMS targetDBMS(Session session) {
		if (session == null) {
			if (executionContext.getTargetDBMS() != null) {
				return executionContext.getTargetDBMS();
			}
			return DBMS.forDBMS(null); // default
		}
		return session.dbms;
	}

	/**
	 * Checks whether working-tables schema is up-to-date.
	 * @param useRowId 
	 * @param workingTableSchema 
	 * 
	 * @return <code>true</code> if working-tables schema is up-to-date
	 */
	public boolean isUptodate(DataSource dataSource, DBMS dbms, boolean useRowId, String workingTableSchema) {
		try {
			if (dataSource != null) {
				final Session session = new Session(dataSource, dbms, executionContext.getIsolationLevel());
				try {
					return isUptodate(session, useRowId, workingTableSchema);
				} finally {
					session.shutDown();
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * Checks whether working-tables schema is up-to-date.
	 * @param useRowId 
	 * @param workingTableSchema 
	 * 
	 * @return <code>true</code> if working-tables schema is up-to-date
	 */
	public boolean isUptodate(final Session session, boolean useRowId, String workingTableSchema) {
		try {
			boolean wasSilent = session.getSilent();
			try {
				session.setSilent(true);
				final boolean[] uptodate = new boolean[] { false };
				final DataModel datamodel = new DataModel(executionContext);
				final Map<String, String> typeReplacement = targetDBMS(session).getTypeReplacement();
				final RowIdSupport rowIdSupport = new RowIdSupport(datamodel, targetDBMS(session), useRowId);
				
				final String schema = workingTableSchema == null ? "" : new Quoting(session).requote(workingTableSchema) + ".";
				
				Session.ResultSetReader reader = new Session.ResultSetReader() {
					@Override
					public void readCurrentRow(ResultSet resultSet) throws SQLException {
						String contraint = pkColumnConstraint(session);
						String universalPrimaryKey = rowIdSupport.getUniversalPrimaryKey().toSQL(null, contraint, typeReplacement);
						String h = "" + (universalPrimaryKey + targetDBMS(session).getTableProperties()).hashCode();
						uptodate[0] = resultSet.getString(1).equals(h);
					}

					@Override
					public void close() {
					}
				};
				if (!uptodate[0]) {
					session.executeQuery("Select jvalue from " + schema + SQLDialect.CONFIG_TABLE_ + " where jversion='" + JailerVersion.WORKING_TABLE_VERSION + "' and jkey='upk'", reader);
				}
				// look for jailer tables
				for (String table : SqlUtil.JAILER_MH_TABLES) {
					session.executeQuery("Select * from " + schema + table + " Where 1=0", new Session.ResultSetReader() {
						@Override
						public void readCurrentRow(ResultSet resultSet) throws SQLException {
						}
						@Override
						public void close() {
						}
					});
				}
				if (uptodate[0]) {
					String testId = "ID:" + System.currentTimeMillis();
					session.executeUpdate(
							"INSERT INTO " + schema + SQLDialect.CONFIG_TABLE_ + "(jversion, jkey, jvalue) " +
							"VALUES ('" + JailerVersion.WORKING_TABLE_VERSION + "', '" + testId + "', 'ok')");
					session.executeUpdate(
							"DELETE FROM " + schema + SQLDialect.CONFIG_TABLE_ + " " +
							"WHERE jversion='" + JailerVersion.WORKING_TABLE_VERSION + "' and jkey='" + testId + "'");
				}
				return uptodate[0];
			} catch (Exception e) {
				try {
					// [bugs:#37] PostreSQL: transactional execution
					session.getConnection().commit();
				} catch (SQLException e1) {
					// ignore
				}
				return false;
			} finally {
				session.setSilent(wasSilent);
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks whether working-tables schema is present.
	 * 
	 * @return <code>true</code> if working-tables schema is present
	 */
	public boolean isPresent(Session session) {
		try {
			try {
				final boolean[] uptodate = new boolean[] { false };
				session.executeQuery("Select jvalue from " + SQLDialect.CONFIG_TABLE_ + " where jkey='upk'",
					new Session.ResultSetReader() {
						@Override
						public void readCurrentRow(ResultSet resultSet) throws SQLException {
							uptodate[0] = true;
						}
						@Override
						public void close() {
						}
					});
				return uptodate[0];
			} catch (Exception e) {
				try {
					// [bugs:#37] PostreSQL: transactional execution
					session.getConnection().commit();
				} catch (SQLException e1) {
					// ignore
				}
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Checks for conflicts of existing tables and working-tables.
	 * 
	 * @return name of table in conflict or <code>null</code>
	 */
	public String getTableInConflict(DataSource dataSource, DBMS dbms) {
		try {
			if (dataSource != null) {
				Session session = new Session(dataSource, dbms, executionContext.getIsolationLevel());
				session.setSilent(true);
				try {
					final boolean[] uptodate = new boolean[] { false };
					session.executeQuery("Select jvalue from " + SQLDialect.CONFIG_TABLE_
							+ " where jkey='magic' and jvalue='837065098274756382534403654245288'", new Session.ResultSetReader() {
						@Override
						public void readCurrentRow(ResultSet resultSet) throws SQLException {
							uptodate[0] = true;
						}

						@Override
						public void close() {
						}
					});
					if (uptodate[0]) {
						session.shutDown();
						return null;
					}
				} catch (Exception e) {
					// fall through
				}

				// look for jailer tables
				for (String table : SqlUtil.JAILER_TABLES) {
					try {
						session.executeQuery("Select * from " + table + " Where 1=0", new Session.ResultSetReader() {
							@Override
							public void readCurrentRow(ResultSet resultSet) throws SQLException {
							}

							@Override
							public void close() {
							}
						});
						session.shutDown();
						return table;
					} catch (Exception e) {
						// fall through
					}
				}
				session.shutDown();
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
