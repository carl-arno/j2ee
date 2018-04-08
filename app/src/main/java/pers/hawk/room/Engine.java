/*
 * @(#)Main.java
 * 
 * Copyright (c) 2009-2013 Werner Randelshofer, Switzerland.
 * All rights reserved.
 * 
 * You may not use, copy or modify this file, except in compliance with the
 * license agreement you entered into with Werner Randelshofer.
 * For details see accompanying license terms.
 */
package pers.hawk.room;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position.Bias;
import javax.swing.tree.*;

import org.apache.log4j.PropertyConfigurator;

import pers.hawk.room.util.AppConst;
import pers.hawk.view.frame.ExitAction;
import pers.hawk.view.frame.TrayListener;
import view.TabbedPaneTest;

/**
 * Main.
 *
 * @author Werner Randelshofer
 * @version $Id: Main.java 462 2014-03-22 09:23:12Z wrandelshofer $
 */

public class Engine extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3031002952947592180L;

	private Injection injection;

	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * Creates new form Main
	 */
	public Engine(String[] args) {
		doInit();
		initComponents();
	}

	/**
	 * 
	 */
	public void doInit() {
		PropertyConfigurator.configure("log4j.properties");
		if (null == injection) {
			injection = new Injection();
		}
		if (null == scheduledExecutorService) {
			scheduledExecutorService = Executors.newScheduledThreadPool(5);
		}
		Runtime.getRuntime().addShutdownHook(injection.getThread());
	}

	/**
	 * 
	 */
	public void initComponents() {
		//
		statusBar = new javax.swing.JToolBar();
		statusLabel = new javax.swing.JLabel();
		statusBar.add(statusLabel);

		statusBar.setFloatable(false);
		statusBar.addAncestorListener(new TimeLabel());
		statusBar.add(statusLabel);

		menuBar = new javax.swing.JMenuBar();
		splitPane = new javax.swing.JSplitPane();
		treeScrollPane = new javax.swing.JScrollPane();
		tree = new javax.swing.JTree();
		rightPane = new javax.swing.JPanel();
		viewPane = new javax.swing.JPanel();
		controlPanel = new javax.swing.JPanel();

		// add
		JMenu jMenu = new JMenu("文件");
		// JMenuItem jMenuItem = new JMenuItem("重新启动");
		// jMenuItem.addActionListener(new ExitAction(injection.getThread(),
		// true));
		JMenuItem jMenuItemExit = new JMenuItem("退出");
		jMenuItemExit.addActionListener(new ExitAction(injection.getThread(), false));
		// jMenu.add(jMenuItem);
		jMenu.add(jMenuItemExit);
		menuBar.add(jMenu);
		// add

		setLayout(new java.awt.BorderLayout());

		splitPane.setDividerLocation(200);

		treeScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));

		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		treeScrollPane.setViewportView(tree);

		splitPane.setLeftComponent(treeScrollPane);

		rightPane.setMinimumSize(new java.awt.Dimension(0, 1));
		rightPane.setLayout(new java.awt.BorderLayout());

		viewPane.setLayout(new java.awt.BorderLayout());
		rightPane.add(viewPane, java.awt.BorderLayout.CENTER);

		rightPane.add(controlPanel, java.awt.BorderLayout.SOUTH);
		add(statusBar, java.awt.BorderLayout.SOUTH);

		splitPane.setRightComponent(rightPane);

		add(splitPane, java.awt.BorderLayout.CENTER);
	}

	public void doTree() {

		treeScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		splitPane.setDividerSize(1);
		splitPane.putClientProperty("Quaqua.SplitPane.style", "bar");
		splitPane.setOneTouchExpandable(false);
		tree.putClientProperty("Quaqua.Tree.style", "sideBar");
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		DefaultMutableTreeNode n;

		StringBuffer stringBuffer = null;
		synchronized (injection.mapTabMenu) {
			stringBuffer = injection.mapTabMenu.get(Injection.menuString[0]).getStringBuffer();
			stringBuffer.append("程序已运行.").append(AppConst.line);
			injection.mapTabMenu.notifyAll();
		}

		root.add(n = new DefaultMutableTreeNode("菜单"));
		n.add(new Item("主页", "view.DesktopPaneTest", injection));

		Item msgItem = new Item("事件", "view.TabbedPaneTest", injection);
		// runnable
		n.add(msgItem);
		scheduledExecutorService.execute((TabbedPaneTest) msgItem.getComponent());

		// // msg
		// scheduledExecutorService.execute(new Publisher());
		// scheduledExecutorService.execute(injection.getSendServerSub());
		// // msg

		scheduledExecutorService.execute(injection.getReceiveServer());
		scheduledExecutorService.execute(injection.getPushServer());
		// runnable

//		 n.add(new Item("配置", "view.TableTest", injection));

		DefaultTreeModel tm = new DefaultTreeModel(root);
		tree.setModel(tm);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = tree.getSelectionPath();
				viewPane.removeAll();
				if (path != null && path.getPathCount() > 0 && (path.getLastPathComponent() instanceof Item)) {
					Item item = (Item) path.getLastPathComponent();
					item.injection = injection;
					viewPane.add(item.getComponent());
				}
				viewPane.revalidate();
				viewPane.repaint();
			}
		});

		for (int i = tree.getRowCount(); i >= 0; i--) {
			tree.expandRow(i);
		}

		tree.setSelectionPath(tree.getNextMatch("事件", 0, Bias.Backward));

		// Add look and feels to menu bar
		ButtonGroup group = new ButtonGroup();
		for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			final JRadioButtonMenuItem mi = new JRadioButtonMenuItem(info.getName());
			group.add(mi);
			if (UIManager.getLookAndFeel().getClass().toString().equals(info.getClassName())) {
				mi.setSelected(true);
			}
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setLookAndFeel(info, mi, root);
				}
			});
		}

	}

	public void doJFrame(Engine engine) {

		JFrame jFrame = new JFrame();
		//
		jFrame.addWindowListener(new TrayListener(jFrame, injection.getThread()));
		// jFrame.setTitle(System.getProperty("java.version") + " " +
		// System.getProperty("os.arch"));
		jFrame.setTitle("能耗数据上传与设备控制系统");
		jFrame.setLocationRelativeTo(null);
		//
		jFrame.add(engine);
		jFrame.setJMenuBar(engine.menuBar);
		jFrame.setSize(740, 480);
		jFrame.setVisible(true);

	}

	public javax.swing.JToolBar statusBar;
	public javax.swing.JLabel statusLabel;
	public Timer timer;
	private javax.swing.JPanel controlPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JPanel rightPane;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JTree tree;
	private javax.swing.JScrollPane treeScrollPane;
	private javax.swing.JPanel viewPane;

	private void setLookAndFeel(LookAndFeelInfo info, JRadioButtonMenuItem mi, DefaultMutableTreeNode root) {
	}

	private class Item extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -3784418891697955978L;
		private String label;
		private String clazz;
		private JComponent component;

		private Injection injection;

		public Item(String label, String clazz, Injection injection) {
			super();
			this.label = label;
			this.clazz = clazz;
			this.injection = injection;
		}

		@Override
		public String toString() {
			return label;
		}

		@SuppressWarnings("rawtypes")
		public JComponent getComponent() {
			if (component == null) {
				try {
					Constructor constructor = Class.forName(clazz).getDeclaredConstructor(new Class[] { Map.class });
					constructor.setAccessible(true);
					component = (JComponent) constructor.newInstance(new Object[] { injection.mapTabMenu });
				} catch (Throwable ex) {
					component = new JLabel(ex.toString());
					ex.printStackTrace();
				}
			}
			return component;
		}
	}

	public class TimeLabel implements AncestorListener {

		private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
		}

		public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
			statusBarAncestorAdded(evt);
		}

		public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
			statusBarAncestorRemoved(evt);
		}

		private void updateStatusLabel() {
			statusLabel.setText(AppConst.currTime + dateFormat.format(Calendar.getInstance().getTime()));
		}

		private void statusBarAncestorAdded(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_statusBarAncestorAdded
			startTimer();
		}

		private void statusBarAncestorRemoved(javax.swing.event.AncestorEvent evt) {// GEN-FIRST:event_statusBarAncestorRemoved
			stopTimer();
		}

		private void startTimer() {
			if (timer == null) {
				timer = new Timer(500, new ActionListener() {
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

	}

}
