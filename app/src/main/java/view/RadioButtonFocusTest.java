/*
 * @(#)RadioButtonFocusTest.java
 * 
 * Copyright © 2010 Werner Randelshofer, Switzerland.
 * All rights reserved.
 * 
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */
package view;

/**
 * RadioButtonFocusTest.
 *
 * @author Werner Randelshofer
 * @version $Id: RadioButtonFocusTest.java 462 2014-03-22 09:23:12Z wrandelshofer $
 */
public class RadioButtonFocusTest extends javax.swing.JPanel {

    /** Creates new form RadioButtonFocusTest */
    public RadioButtonFocusTest() {
        initComponents();
    }

    private void updateFocus() {
        if (requestFocusChoice.isSelected()) {
            if (option1.isSelected()) {
                field1.requestFocus();
                field1.selectAll();
            } else if (option2.isSelected()) {
                option2.requestFocus();
            } else if (option3.isSelected()) {
                field3.requestFocus();
                field3.selectAll();
            }
        }
    }

    private void updateEnabled() {
        if (enableFieldsChoice.isSelected()) {
            field1.setEnabled(option1.isSelected());
            field3.setEnabled(option3.isSelected());
        } else {
            field1.setEnabled(true);
            field3.setEnabled(true);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        group = new javax.swing.ButtonGroup();
        option1 = new javax.swing.JRadioButton();
        option2 = new javax.swing.JRadioButton();
        option3 = new javax.swing.JRadioButton();
        field1 = new javax.swing.JTextField();
        field3 = new javax.swing.JTextField();
        otherLabel = new javax.swing.JLabel();
        otherField = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        requestFocusChoice = new javax.swing.JCheckBox();
        enableFieldsChoice = new javax.swing.JCheckBox();
        itemListenerChoice = new javax.swing.JCheckBox();

        FormListener formListener = new FormListener();

        group.add(option1);
        option1.setSelected(true);
        option1.setText("1st Option:");
        option1.addItemListener(formListener);
        option1.addActionListener(formListener);

        group.add(option2);
        option2.setText("2nd Option");
        option2.addItemListener(formListener);
        option2.addActionListener(formListener);

        group.add(option3);
        option3.setText("3rd Option:");
        option3.addItemListener(formListener);
        option3.addActionListener(formListener);

        field1.setColumns(8);
        field1.setText("value1");

        field3.setColumns(8);
        field3.setText("value3");

        otherLabel.setText("Text:");

        otherField.setText("other text");

        requestFocusChoice.setText("Request focus on fields");
        requestFocusChoice.addActionListener(formListener);

        enableFieldsChoice.setText("Enable only selected fields");
        enableFieldsChoice.addActionListener(formListener);

        itemListenerChoice.setText("Use ItemListener instead of ActionListener");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemListenerChoice)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(option1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(option2)
                        .addComponent(requestFocusChoice)
                        .addComponent(enableFieldsChoice)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(otherLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(otherField))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(option3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jSeparator1)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(option1)
                    .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(option2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(option3)
                    .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(otherLabel)
                    .addComponent(otherField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(requestFocusChoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enableFieldsChoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemListenerChoice)
                .addContainerGap(29, Short.MAX_VALUE))
        );
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.ActionListener, java.awt.event.ItemListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == option1) {
                RadioButtonFocusTest.this.optionActionPerformed(evt);
            }
            else if (evt.getSource() == option2) {
                RadioButtonFocusTest.this.optionActionPerformed(evt);
            }
            else if (evt.getSource() == option3) {
                RadioButtonFocusTest.this.optionActionPerformed(evt);
            }
            else if (evt.getSource() == requestFocusChoice) {
                RadioButtonFocusTest.this.requestFocusChoicePerformed(evt);
            }
            else if (evt.getSource() == enableFieldsChoice) {
                RadioButtonFocusTest.this.enableFieldsChoicePerformed(evt);
            }
        }

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            if (evt.getSource() == option1) {
                RadioButtonFocusTest.this.optionItemStateChanged(evt);
            }
            else if (evt.getSource() == option2) {
                RadioButtonFocusTest.this.optionItemStateChanged(evt);
            }
            else if (evt.getSource() == option3) {
                RadioButtonFocusTest.this.optionItemStateChanged(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents

    private void requestFocusChoicePerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestFocusChoicePerformed
        //updateFocus();
    }//GEN-LAST:event_requestFocusChoicePerformed

    private void enableFieldsChoicePerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableFieldsChoicePerformed
        updateEnabled();
    }//GEN-LAST:event_enableFieldsChoicePerformed

    private void optionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionActionPerformed
        if (!itemListenerChoice.isSelected()) {
            if (enableFieldsChoice.isSelected()) {
                updateEnabled();
            }
            if (requestFocusChoice.isSelected()) {
                updateFocus();
            }
        }
    }//GEN-LAST:event_optionActionPerformed

    private void optionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_optionItemStateChanged
        if (itemListenerChoice.isSelected()) {
            if (enableFieldsChoice.isSelected()) {
                updateEnabled();
            }
            if (requestFocusChoice.isSelected()) {
                updateFocus();
            }
        }
    }//GEN-LAST:event_optionItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox enableFieldsChoice;
    private javax.swing.JTextField field1;
    private javax.swing.JTextField field3;
    private javax.swing.ButtonGroup group;
    private javax.swing.JCheckBox itemListenerChoice;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton option1;
    private javax.swing.JRadioButton option2;
    private javax.swing.JRadioButton option3;
    private javax.swing.JTextField otherField;
    private javax.swing.JLabel otherLabel;
    private javax.swing.JCheckBox requestFocusChoice;
    // End of variables declaration//GEN-END:variables
}
