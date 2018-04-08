/*
 * @(#)TreeTest.java 
 *
 * Copyright (c) 2004 Werner Randelshofer, Switzerland.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */

package view;

import ch.randelshofer.quaqua.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;
/**
 * TreeTest.
 *
 * @author  Werner Randelshofer
 * @version $Id: TreeTest.java 94 2009-06-24 13:54:21Z wrandelshofer $
 */
public class TreeHighlightCellTest extends javax.swing.JPanel {

    private static class MyCellRenderer extends DefaultTreeCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component c = super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
            c.setBackground(isSelected ? Color.RED.darker() : Color.RED);
            return c;
        }
        
    }
    
    /** Creates new form. */
    public TreeHighlightCellTest() {
        initComponents();
        
        tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree3.putClientProperty("Quaqua.Tree.style","striped");

        tree1.setCellRenderer(new MyCellRenderer());
        tree3.setCellRenderer(new MyCellRenderer());
    }
    public static void main(String args[]) {
System.out.println(System.getProperty("java.library.path"));
        try {
            UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame f = new JFrame("Quaqua Tree Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new TreeHighlightCellTest());
        ((JComponent) f.getContentPane()).setBorder(new EmptyBorder(9,17,17,17));
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

        scrollPane1 = new javax.swing.JScrollPane();
        tree1 = new javax.swing.JTree();
        scrollPane3 = new javax.swing.JScrollPane();
        tree3 = new javax.swing.JTree();

        setLayout(new java.awt.GridLayout(2, 2));

        tree1.setEditable(true);
        tree1.setRootVisible(false);
        tree1.setShowsRootHandles(true);
        scrollPane1.setViewportView(tree1);

        add(scrollPane1);

        tree3.setRootVisible(false);
        tree3.setShowsRootHandles(true);
        scrollPane3.setViewportView(tree3);

        add(scrollPane3);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollPane1;
    private javax.swing.JScrollPane scrollPane3;
    private javax.swing.JTree tree1;
    private javax.swing.JTree tree3;
    // End of variables declaration//GEN-END:variables
    
}
