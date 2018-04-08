/*
 * @(#)TabbedPaneTest.java  1.0  12 February 2005
 *
 * Copyright (c) 2004 Werner Randelshofer, Switzerland.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package view;

import ch.randelshofer.quaqua.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;

import pers.hawk.room.Injection;
import pers.hawk.room.util.AppConst;
import pers.hawk.view.frame.TabMenu;

/**
 * TabbedPaneTest.
 *
 * @author Werner Randelshofer
 * @version 1.0 12 February 2005 Created.
 */
@SuppressWarnings("unused")
public class TabbedPaneTest extends javax.swing.JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1546098458205279576L;

	private Logger logger = Logger.getLogger(this.getClass());
	private DateFormat format;

	public Map<String, TabMenu> mapTabMenu;

	private List<TabMenu> tabMenuList;

	private javax.swing.JTabbedPane tabbedPane1;

	private javax.swing.JPanel jPanel2;

	public TabbedPaneTest(Map<String, TabMenu> mapTabMenu) {
		super();
		this.mapTabMenu = mapTabMenu;
		initTabbedPaneTest();
	}

	public void run() {
		showMsg();
	}

	/**
	 * 界面显示信息
	 */
	public void showMsg() {

		Component[] components = null;

		JTextPane jTextPane;
		Document document = null;

		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		Color color = null;

		// for (int a = 1; a < tabbedPane1.getComponentCount();
		// a++){
		// Component[] components = ((JPanel)
		// tabbedPane1.getComponent(a)).getComponents();
		// }

		while (true) {
			try {
				synchronized (mapTabMenu) {
					mapTabMenu.wait();

					components = ((JPanel) tabbedPane1.getComponent(1)).getComponents();
					jTextPane = (JTextPane) ((JScrollPane) components[0]).getViewport().getView();
					document = jTextPane.getDocument();

					for (int a = 0; a < tabMenuList.size(); a++) {
						if (tabMenuList.get(a).getStringBuffer().length() <= 0) {
							continue;
						}
						logger.info(tabMenuList.get(a).getStringBuffer().toString());

						color = tabMenuList.get(a).getColor();
						StyleConstants.setForeground(attributeSet, color);
						StyleConstants.setBold(attributeSet, true);

						if (document.getLength() > 47000) {
							document.remove(0, document.getLength());
						}
						document.insertString(document.getLength(), tabMenuList.get(a).getStringBuffer().toString(), attributeSet);
						document.insertString(document.getLength(), AppConst.line, attributeSet);

						jTextPane.setCaretPosition(document.getLength());

					}
					tabMenuList.get(0).getStringBuffer().setLength(0);

					for (int a = 1; a < tabMenuList.size(); a++) {
						if (tabMenuList.get(a).getStringBuffer().length() <= 0) {
							continue;
						}
						//logger.info(tabMenuList.get(a).getStringBuffer().toString());

						components = ((JPanel) tabbedPane1.getComponent(a + 1)).getComponents();
//						document = ((JTextPane) ((JScrollPane) components[0]).getViewport().getView()).getDocument();
						
						jTextPane = (JTextPane) ((JScrollPane) components[0]).getViewport().getView();
						document = jTextPane.getDocument();
						
						if (document.getLength() > 47000) {
							document.remove(0, document.getLength());
						}
						document.insertString(document.getLength(), tabMenuList.get(a).getStringBuffer().toString(), attributeSet);
						document.insertString(document.getLength(), AppConst.line, attributeSet);

						jTextPane.setCaretPosition(document.getLength());
						
						tabMenuList.get(a).getStringBuffer().setLength(0);
					}
					mapTabMenu.notifyAll();
				}
			} catch (Exception e) {
				StringBuffer stringBuffer = null;
				synchronized (mapTabMenu) {
					stringBuffer = mapTabMenu.get(Injection.menuString[2]).getStringBuffer();
					stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 发生未知错误.").append(AppConst.line);
					mapTabMenu.notifyAll();
				}
				logger.warn(stringBuffer.toString());
				e.printStackTrace();
			}
		}

	}

	/**
	 * 初始化
	 */
	public void doInit() {
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public void initTabbedPaneTest() {
		initComponents();

		//
		doInit();
		//

		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		tabMenuList = new ArrayList<TabMenu>();
		Collection<TabMenu> collection = mapTabMenu.values();
		for (Iterator<TabMenu> iterator = collection.iterator(); iterator.hasNext();) {
			tabMenuList.add(iterator.next());
		}
		Collections.sort(tabMenuList);

		// test
		for (TabMenu tabMenu : tabMenuList) {
			JPanel p = new JPanel();
			p.setLayout(new GridLayout());
			JTextPane jTextPane = new JTextPane();
			JScrollPane jScrollPane = new JScrollPane(jTextPane);
			jScrollPane.setName(tabMenu.getName());
			p.add(jScrollPane);
			tabbedPane1.add(p, tabMenu.getName());
		}
		// test

		// Try to get a better layout with J2SE6
		try {
			int BASELINE_LEADING = GridBagConstraints.class.getDeclaredField("BASELINE_LEADING").getInt(null);
			GridBagLayout layout = (GridBagLayout) getLayout();
			for (Component c : getComponents()) {
				GridBagConstraints gbc = layout.getConstraints(c);
				if (gbc.anchor == GridBagConstraints.WEST) {
					gbc.anchor = BASELINE_LEADING;
					layout.setConstraints(c, gbc);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/** Creates new form. */
	public TabbedPaneTest() {

		initTabbedPaneTest();

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

		jPanel2 = new javax.swing.JPanel();

		setLayout(new java.awt.BorderLayout());

		jPanel2.setLayout(new java.awt.GridBagLayout());

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

		tabbedPane1 = new javax.swing.JTabbedPane();

		setBorder(javax.swing.BorderFactory.createEmptyBorder(11, 13, 11, 13));
		setLayout(new java.awt.GridBagLayout());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(tabbedPane1, gridBagConstraints);

	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(QuaquaManager.getLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JFrame f = new JFrame("TabbedPaneTest: " + UIManager.getLookAndFeel().getName());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new TabbedPaneTest());
		f.pack();
		f.setVisible(true);
	}

}
