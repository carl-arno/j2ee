/*
 * @(#)ToolBarTest.java  
 *
 * Copyright (c) 2004-2013 Werner Randelshofer, Switzerland.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * ToolBarTest.
 *
 * @author  Werner Randelshofer
 * @version $Id: ToolBarTest.java 462 2014-03-22 09:23:12Z wrandelshofer $
 */
public class ToolBarTest extends javax.swing.JPanel {

    private static JToolBar unifiedToolBar;
    private static JToolBar unifiedStatusBar;
    private static Timer timer;

    /** Creates new form. */
    public ToolBarTest() {
        initComponents();
        setOpaque(true);
        // toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);

        folderButton.putClientProperty("JButton.segmentPosition", "middle");
        fileButton.putClientProperty("JButton.segmentPosition", "last");

        if (unifiedToolBar != null) {
            unifiedToolBarBox.setSelected(true);
            toolBarPanel.remove(toolBar);
            toolBar = unifiedToolBar;
        }
        if (unifiedStatusBar != null) {
            unifiedStatusBarBox.setSelected(true);
            remove(statusBar);
            statusBar = unifiedStatusBar;
        }
    }

    private void startTimer() {
        if (timer==null) {
            timer=new Timer(500,new ActionListener(){

                public void actionPerformed(ActionEvent e) {
                    updateStatusLabel();
                }
            });
            timer.setRepeats(true);
            timer.start();
        }
    }

    private void stopTimer() {
        
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void updateStatusLabel() {
       DateFormat tf=new SimpleDateFormat("HH:mm:ss");
        statusLabel.setText("A status bar " + tf.format(new Date()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bottomStyleGroup = new javax.swing.ButtonGroup();
        titleStyleGroup = new javax.swing.ButtonGroup();
        statusBar = new javax.swing.JToolBar();
        statusLabel = new javax.swing.JLabel();
        toolBarPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        folderButton = new javax.swing.JButton();
        fileButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        unifiedToolBarBox = new javax.swing.JCheckBox();
        plainTitleRadio = new javax.swing.JRadioButton();
        unifiedTitleRadio = new javax.swing.JRadioButton();
        gradientTitleRadio = new javax.swing.JRadioButton();
        unifiedStatusBarBox = new javax.swing.JCheckBox();
        plainBottomRadio = new javax.swing.JRadioButton();
        unifiedBottomRadio = new javax.swing.JRadioButton();
        gradientBottomRadio = new javax.swing.JRadioButton();

        setLayout(new java.awt.BorderLayout());

        statusBar.setFloatable(false);
        statusBar.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                statusBarAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                statusBarAncestorRemoved(evt);
            }
        });

        statusLabel.setText("A status bar");
        statusBar.add(statusLabel);

        add(statusBar, java.awt.BorderLayout.SOUTH);

        toolBarPanel.setLayout(new java.awt.BorderLayout());

        toolBar.setName("ToolBar with Title"); // NOI18N

        folderButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        folderButton.setText("Folder");
        folderButton.setFocusable(false);
        folderButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        folderButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(folderButton);

        fileButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
        fileButton.setText("File");
        fileButton.setFocusable(false);
        fileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(fileButton);

        toolBarPanel.add(toolBar, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        unifiedToolBarBox.setText("Title tool bar");
        unifiedToolBarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unifiedToolBarPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(unifiedToolBarBox, gridBagConstraints);

        titleStyleGroup.add(plainTitleRadio);
        plainTitleRadio.setSelected(true);
        plainTitleRadio.setText("'plain' style");
        plainTitleRadio.setActionCommand("plain");
        plainTitleRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                titleStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(plainTitleRadio, gridBagConstraints);

        titleStyleGroup.add(unifiedTitleRadio);
        unifiedTitleRadio.setText("unified 'title' style");
        unifiedTitleRadio.setActionCommand("title");
        unifiedTitleRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                titleStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(unifiedTitleRadio, gridBagConstraints);

        titleStyleGroup.add(gradientTitleRadio);
        gradientTitleRadio.setText("'gradient' style");
        gradientTitleRadio.setActionCommand("gradient");
        gradientTitleRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                titleStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(gradientTitleRadio, gridBagConstraints);

        unifiedStatusBarBox.setText("Bottom tool bar");
        unifiedStatusBarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unifiedStatusBarPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        jPanel2.add(unifiedStatusBarBox, gridBagConstraints);

        bottomStyleGroup.add(plainBottomRadio);
        plainBottomRadio.setSelected(true);
        plainBottomRadio.setText("'plain' style");
        plainBottomRadio.setActionCommand("plain");
        plainBottomRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bottomStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(plainBottomRadio, gridBagConstraints);

        bottomStyleGroup.add(unifiedBottomRadio);
        unifiedBottomRadio.setText("unified 'bottom' style");
        unifiedBottomRadio.setActionCommand("bottom");
        unifiedBottomRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bottomStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(unifiedBottomRadio, gridBagConstraints);

        bottomStyleGroup.add(gradientBottomRadio);
        gradientBottomRadio.setText("'gradient' style");
        gradientBottomRadio.setActionCommand("gradient");
        gradientBottomRadio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bottomStyleChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel2.add(gradientBottomRadio, gridBagConstraints);

        toolBarPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        add(toolBarPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void unifiedToolBarPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unifiedToolBarPerformed
        JRootPane rp = SwingUtilities.getRootPane(this);
        JPanel cp = (JPanel) rp.getContentPane();

        boolean isUnified = unifiedTitleRadio.isSelected();

        if (unifiedToolBarBox.isSelected()) {
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            cp.add(toolBar, BorderLayout.NORTH);
            unifiedToolBar = toolBar;
            toolBar.setFloatable(false);
        } else {
            toolBar.setOrientation(JToolBar.HORIZONTAL);
            toolBarPanel.add(toolBar, BorderLayout.NORTH);
            toolBar.setFloatable(true);
            unifiedToolBar = null;
        }
        cp.revalidate();

        Boolean currentUnified = (Boolean) rp.getClientProperty("apple.awt.brushMetalLook");
        if (currentUnified == null) {
            currentUnified = false;
        }
        if (currentUnified != isUnified) {

            rp.putClientProperty("apple.awt.brushMetalLook",//
                    isUnified);
            Window f = (Window) rp.getParent();
            f.dispose();
            f.setVisible(true);
        }

    }//GEN-LAST:event_unifiedToolBarPerformed

    private void unifiedStatusBarPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unifiedStatusBarPerformed
        JRootPane rp = SwingUtilities.getRootPane(this);
        JPanel cp = (JPanel) rp.getContentPane();
        if (unifiedStatusBarBox.isSelected()) {
            cp.add(statusBar, BorderLayout.SOUTH);
            unifiedStatusBar = statusBar;
        } else {
            add(statusBar, BorderLayout.SOUTH);
            unifiedStatusBar = null;
        }

        cp.revalidate();
        rp.putClientProperty("apple.awt.brushMetalLook",//
                unifiedToolBarBox.isSelected() /*|| unifiedStatusBarBox.isSelected()*/);
        Window f = (Window) rp.getParent();
        f.dispose();
        f.setVisible(true);

    }//GEN-LAST:event_unifiedStatusBarPerformed

    private void titleStyleChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_titleStyleChanged
        toolBar.putClientProperty("Quaqua.ToolBar.style", titleStyleGroup.getSelection().getActionCommand());
        unifiedToolBarPerformed(null);
        toolBar.revalidate();
        toolBar.repaint();
    }//GEN-LAST:event_titleStyleChanged

    private void bottomStyleChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_bottomStyleChanged
        statusBar.putClientProperty("Quaqua.ToolBar.style", bottomStyleGroup.getSelection().getActionCommand());
        statusBar.revalidate();
        statusBar.repaint();
    }//GEN-LAST:event_bottomStyleChanged

    private void statusBarAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_statusBarAncestorAdded
          startTimer();
    }//GEN-LAST:event_statusBarAncestorAdded

    private void statusBarAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_statusBarAncestorRemoved
              stopTimer();
    }//GEN-LAST:event_statusBarAncestorRemoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bottomStyleGroup;
    private javax.swing.JButton fileButton;
    private javax.swing.JButton folderButton;
    private javax.swing.JRadioButton gradientBottomRadio;
    private javax.swing.JRadioButton gradientTitleRadio;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton plainBottomRadio;
    private javax.swing.JRadioButton plainTitleRadio;
    private javax.swing.JToolBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.ButtonGroup titleStyleGroup;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JPanel toolBarPanel;
    private javax.swing.JRadioButton unifiedBottomRadio;
    private javax.swing.JCheckBox unifiedStatusBarBox;
    private javax.swing.JRadioButton unifiedTitleRadio;
    private javax.swing.JCheckBox unifiedToolBarBox;
    // End of variables declaration//GEN-END:variables
}
