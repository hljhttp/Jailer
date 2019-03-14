package net.sf.jailer;
import java.util.HashMap;
import java.util.Map;
//import org.mapdb.*;

import net.sf.jailer.datamodel.DataModel;
import org.dizitart.no2.IndexType;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteCollection;

public class Singleton {
    //private Map<String, DB> db_map = new HashMap<String, DB>();
    private Map<String, Nitrite> db_map = new HashMap<String, Nitrite>();
    private static Singleton singleton = new Singleton( );
    public static Singleton getInstance( ) {
        return singleton;
    }
    private ExecutionContext executionContext;
    public Nitrite CREATE_DB(ExecutionContext executionContext)
    {
        String name = executionContext.getCurrentModelSubfolder();
        if(!db_map.containsKey(name))
        {

            db_map.put(name,Nitrite.builder()
                    .compressed()
                    .filePath(DataModel.getDatamodelFolder(executionContext)+"/database.db")
                    .openOrCreate("user", "password"));
            this.executionContext = executionContext;
        }
        return db_map.get(name);
    }
    public Nitrite DB()
    {
        String name = executionContext.getCurrentModelSubfolder();
        if(db_map.containsKey(name))
        {
            return db_map.get(name);
        }
        return null;
    }
}
