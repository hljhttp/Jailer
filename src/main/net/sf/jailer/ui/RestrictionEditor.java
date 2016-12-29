/*
 * Copyright 2007 - 2017 the original author or authors.
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
package net.sf.jailer.ui;

/**
 * Editor for a restricted association.
 *
 * @author Ralf Wisser
 */
public class RestrictionEditor extends javax.swing.JPanel {
    
	/** Creates new form RestrictionEditor */
    public RestrictionEditor() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        joinCondition2 = new javax.swing.JTextField();
        description = new javax.swing.JLabel();
        ignore = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        restrictedDependencyWarning = new javax.swing.JLabel();
        apply = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        aName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        source = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        destination = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        type = new javax.swing.JLabel();
        cardinality = new javax.swing.JLabel();
        description1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        joinCondition = new javax.swing.JLabel();
        columnsA = new javax.swing.JLabel();
        columnsB = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        restriction = new javax.swing.JTextField();
        openRestrictionConditionEditor = new javax.swing.JLabel();

        joinCondition2.setEditable(false);
        joinCondition2.setText("jTextField1");
        joinCondition2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        joinCondition2.setCaretPosition(1);
        joinCondition2.setFocusable(false);
        joinCondition2.setRequestFocusEnabled(false);

        setLayout(new java.awt.GridBagLayout());

        description.setText(" Table A ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(description, gridBagConstraints);

        ignore.setText("disabled");
        ignore.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        ignore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ignoreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 0, 0);
        add(ignore, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());

        restrictedDependencyWarning.setForeground(new java.awt.Color(255, 0, 51));
        restrictedDependencyWarning.setText("Restricted Dependency! ");
        jPanel1.add(restrictedDependencyWarning, java.awt.BorderLayout.WEST);

        apply.setText(" apply ");
        jPanel1.add(apply, java.awt.BorderLayout.EAST);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jPanel1, gridBagConstraints);

        jLabel1.setText(" on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jLabel1, gridBagConstraints);

        jLabel4.setText(" Table B ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jLabel4, gridBagConstraints);

        jLabel3.setText(" Name  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jLabel3, gridBagConstraints);

        aName.setFont(new java.awt.Font("Dialog", 0, 12));
        aName.setText("jLabel9");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(aName, gridBagConstraints);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        source.setFont(new java.awt.Font("Dialog", 0, 12));
        source.setText("jLabel3");
        jPanel2.add(source);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        destination.setFont(new java.awt.Font("Dialog", 0, 12));
        destination.setText("jLabel3");
        jPanel3.add(destination);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        type.setFont(new java.awt.Font("Dialog", 0, 12));
        type.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(type, gridBagConstraints);

        cardinality.setFont(new java.awt.Font("Dialog", 0, 12));
        cardinality.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(cardinality, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(jPanel4, gridBagConstraints);

        description1.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        add(description1, gridBagConstraints);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        joinCondition.setFont(new java.awt.Font("Dialog", 0, 12));
        joinCondition.setText("jLabel5");
        jPanel5.add(joinCondition);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel5, gridBagConstraints);

        columnsA.setText("V");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 8);
        add(columnsA, gridBagConstraints);

        columnsB.setText("V");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 8);
        add(columnsB, gridBagConstraints);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        jLabel2.setText(" Restricted by ");
        jLabel2.setToolTipText(getConditionToolTip());
        jPanel6.add(jLabel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(jPanel6, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        restriction.setText("jTextField1");
        restriction.setToolTipText(getConditionToolTip());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(restriction, gridBagConstraints);

        openRestrictionConditionEditor.setText("jLabel5");
        openRestrictionConditionEditor.setToolTipText("open editor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel7.add(openRestrictionConditionEditor, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 0, 0);
        add(jPanel7, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void ignoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ignoreActionPerformed
    }//GEN-LAST:event_ignoreActionPerformed

    private String getConditionToolTip() {
        return "use 'A' as alias for source-table, " +
               "use 'B' as alias for destination table, " +
               "(upper case, no space between A/B and dot!)";
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel aName;
    public javax.swing.JButton apply;
    public javax.swing.JLabel cardinality;
    javax.swing.JLabel columnsA;
    javax.swing.JLabel columnsB;
    public javax.swing.JLabel description;
    public javax.swing.JLabel description1;
    public javax.swing.JLabel destination;
    public javax.swing.JCheckBox ignore;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    javax.swing.JLabel joinCondition;
    javax.swing.JTextField joinCondition2;
    javax.swing.JLabel openRestrictionConditionEditor;
    public javax.swing.JLabel restrictedDependencyWarning;
    public javax.swing.JTextField restriction;
    public javax.swing.JLabel source;
    public javax.swing.JLabel type;
    // End of variables declaration//GEN-END:variables
    
    private static final long serialVersionUID = -6735468124049608700L;
}
