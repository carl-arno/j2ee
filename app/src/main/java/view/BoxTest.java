/*
 * @(#)BoxTest.java
 *
 * Copyright (c) 2003-2013 Werner Randelshofer, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer.
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * Werner Randelshofer. For details see accompanying license terms.
 */
package view;

import javax.swing.*;
import javax.swing.border.*;

/**
 * Box test.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class BoxTest extends javax.swing.JPanel {
    
    /**
     * Creates new form BoxTest
     */
    public BoxTest() {
        initComponents();
        
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setBorder(new TitledBorder("Ångström"));
        box.add(new JButton("Ångström"));
        add(box);
    }
    
    public static void main(String[] args) {
        
        JFrame f = new JFrame("Box Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new BoxTest());
        f.pack();
        f.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
