/*
 * @(#)TableTest.java 
 *
 * Copyright (c) 2004 Werner Randelshofer, Switzerland.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.randelshofer.quaqua.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import pers.hawk.room.local.HService;
import pers.hawk.view.frame.TabMenu;

/**
 * TableTest.
 *
 * @author Werner Randelshofer
 * @version $Id: TableTest.java 462 2014-03-22 09:23:12Z wrandelshofer $
 */
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class TableTest extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6512802966139290597L;

	private HService localService;
	
	private Map<String, TabMenu> mapTabMenu;
	

	/** Creates new form. */

	public TableTest() {
	}
	
	public TableTest(Map<String, TabMenu> mapTabMenu) {
		super();
		this.mapTabMenu = mapTabMenu;
		
		initComponents();
		/*
		 * plainTable = new JTable() { public void repaint(long tm, int x, int
		 * y, int w, int h) { super.repaint(tm, x, y, w, h);
		 * 
		 * System.out.println("JTable.repaint("+tm+","+x+","+y+" "+w+" "+h); if
		 * (w == 192) { new Throwable().printStackTrace(); } } };
		 * plainTableScrollPane.setViewportView(plainTable);
		 */
		plainTable.setModel(new MyTableModel());

		TableColumnModel cm = plainTable.getColumnModel();

		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < 17; i++) {
			list.add(i);
		}

		for (int i = 0; i < list.size(); i++) {
			cm.getColumn(0).setPreferredWidth(30);
			cm.getColumn(1).setPreferredWidth(120);
			cm.getColumn(2).setPreferredWidth(40);
			cm.getColumn(3).setPreferredWidth(60);
			// cm.getColumn(4).setPreferredWidth(50);
			// cm.getColumn(4).setCellRenderer(new DefaultCellRenderer(comboBox
			// = new JComboBox(rendererComboModel)));
			// cm.getColumn(4).setCellEditor(new DefaultCellEditor2(comboBox =
			// new JComboBox(editorComboModel)));
			plainTable.putClientProperty("Quaqua.Table.style", "plain");
		}
	}

	// public TableTest() {
	// initComponents();
	// /*
	// * plainTable = new JTable() { public void repaint(long tm, int x, int
	// * y, int w, int h) { super.repaint(tm, x, y, w, h);
	// *
	// * System.out.println("JTable.repaint("+tm+","+x+","+y+" "+w+" "+h); if
	// * (w == 192) { new Throwable().printStackTrace(); } } };
	// * plainTableScrollPane.setViewportView(plainTable);
	// */
	// plainTable.setModel(new MyTableModel());
	//
	// DefaultComboBoxModel rendererComboModel, editorComboModel;
	// JComboBox comboBox;
	//
	// rendererComboModel = new DefaultComboBoxModel(new Object[] { "Pop",
	// "Rock", "R&B" });
	// editorComboModel = new DefaultComboBoxModel(new Object[] { "Pop", "Rock",
	// "R&B" });
	// TableColumnModel cm = plainTable.getColumnModel();
	// cm.getColumn(0).setPreferredWidth(30);
	// cm.getColumn(1).setPreferredWidth(120);
	// cm.getColumn(2).setPreferredWidth(40);
	// cm.getColumn(3).setPreferredWidth(60);
	// cm.getColumn(4).setPreferredWidth(50);
	// cm.getColumn(4).setCellRenderer(new DefaultCellRenderer(comboBox = new
	// JComboBox(rendererComboModel)));
	// cm.getColumn(4).setCellEditor(new DefaultCellEditor2(comboBox = new
	// JComboBox(editorComboModel)));
	// plainTable.putClientProperty("Quaqua.Table.style", "plain");
	//
	// rendererComboModel = new DefaultComboBoxModel(new Object[] { "Pop",
	// "Rock", "R&B" });
	// editorComboModel = new DefaultComboBoxModel(new Object[] { "Pop", "Rock",
	// "R&B" });
	// cm.getColumn(0).setPreferredWidth(30);
	// cm.getColumn(1).setPreferredWidth(120);
	// cm.getColumn(2).setPreferredWidth(40);
	// cm.getColumn(3).setPreferredWidth(60);
	// cm.getColumn(4).setPreferredWidth(50);
	// cm.getColumn(4).setCellRenderer(new DefaultCellRenderer(comboBox = new
	// JComboBox(rendererComboModel)));
	// comboBox.setEditable(true);
	// comboBox = new JComboBox(editorComboModel);
	// comboBox.setEditable(true);
	// cm.getColumn(4).setCellEditor(new DefaultCellEditor2(comboBox));
	//
	// JCheckBox cb = new JCheckBox();
	// cb.setHorizontalAlignment(SwingConstants.CENTER);
	// cb.putClientProperty("JComponent.sizeVariant", "small");
	// rendererComboModel = new DefaultComboBoxModel(new Object[] { "Pop",
	// "Rock", "R&B" });
	// editorComboModel = new DefaultComboBoxModel(new Object[] { "Pop", "Rock",
	// "R&B" });
	// cm.getColumn(0).setPreferredWidth(30);
	// cm.getColumn(1).setPreferredWidth(160);
	// cm.getColumn(2).setPreferredWidth(40);
	// cm.getColumn(3).setPreferredWidth(70);
	// cm.getColumn(4).setPreferredWidth(50);
	// cm.getColumn(4).setCellRenderer(new DefaultCellRenderer(comboBox = new
	// JComboBox(rendererComboModel)));
	// comboBox.setEditable(true);
	// comboBox = new JComboBox(editorComboModel);
	// comboBox.setEditable(true);
	// cm.getColumn(4).setCellEditor(new DefaultCellEditor2(comboBox));
	//
	// }

	/**
	 * Some bogus data to populate the table.
	 */
	private static class MyTableModel extends AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5268706891064797698L;
		private Object[][] data;

		private Class[] columnClasses;
		private String[] columnNames;

		public MyTableModel() {
			columnClasses = new Class[] { String.class, String.class, String.class, String.class, String.class, Integer.class };
			columnNames = new String[] { "序号", "Name", "Time", "Artist", "Genre", "Year" };
			data = new Object[17][columnClasses.length];
			for (int i = 0; i < data.length; i++) {
				data[i][0] = i + 1;
				data[i][1] = (i % 2 == 0) ? "Fooing In The Wind" : "Baring The Sea";
				data[i][2] = (i % 2 == 0) ? "3:51" : "3:42";
				data[i][3] = (i % 2 == 0) ? "Foo Guy" : "Bar Girl";
				data[i][4] = (i % 2 == 0) ? "Pop" : "Rock";
				data[i][5] = (i % 2 == 0) ? new Integer(2007) : new Integer(2008);
			}
		}

		public int getRowCount() {
			return data.length;
		}

		public int getColumnCount() {
			return data[0].length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int row, int column) {
			return data[row][column];
		}

		@Override
		public void setValueAt(Object value, int row, int column) {
			data[row][column] = value;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return column != 2;
		}

		@Override
		public Class getColumnClass(int column) {
			return columnClasses[column];
		}
	}

	
	public static void main(String args[]) {
		try {
			System.setProperty("Quaqua.Table.useJ2SE5MouseHandler", "true");
			UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame f = new JFrame("Quaqua Table Test");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new TableTest());
		((JComponent) f.getContentPane()).setBorder(new EmptyBorder(9, 17, 17, 17));
		f.pack();
		f.setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		okayButton = new javax.swing.JButton();
		plainTableScrollPane = new javax.swing.JScrollPane();
		plainTable = new javax.swing.JTable();
		jSeparator1 = new javax.swing.JSeparator();

		setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 17, 17, 17));
		setPreferredSize(new java.awt.Dimension(400, 300));
		setLayout(new java.awt.GridBagLayout());

		plainTableScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		plainTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		plainTableScrollPane.setViewportView(plainTable);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.ipadx = 300;
		gridBagConstraints.ipady = 100;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(plainTableScrollPane, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
		add(jSeparator1, gridBagConstraints);

		okayButton.setText("保存");
		add(okayButton, new java.awt.GridBagConstraints());

	}

	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JTable plainTable;
	private javax.swing.JScrollPane plainTableScrollPane;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton okayButton;
	// End of variables declaration//GEN-END:variables

}
