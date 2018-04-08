package pers.hawk.view.frame;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class TrayListener implements WindowListener {

	private Thread thread;

	private JFrame jFrame;

	private TrayIcon trayIcon;

	private PopupMenu popupMenu;

	public TrayListener(JFrame jFrame, Thread thread) {
		super();
		this.jFrame = jFrame;
		this.thread = thread;
		trayIconInit().addTrayIcon();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		if (SystemTray.isSupported()) {
			jFrame.setVisible(false);
		} else {
			System.exit(0);
		}
	}

	/*
	 * windowIconified(non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
		if (SystemTray.isSupported()) {
			jFrame.setVisible(false);
		} else {
			System.exit(0);
		}
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	/**
	 * 
	 */
	public TrayListener trayIconInit() {

		MenuItem exitMenuItem = null;

		exitMenuItem = new MenuItem("退出程序");
		exitMenuItem.addActionListener(new ExitAction(thread,false));

		MenuItem jMenuItem = null;
		(jMenuItem = new MenuItem("显示界面")).addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jFrame.setVisible(true);
			}
		});

		popupMenu = new PopupMenu();
		popupMenu.add(jMenuItem);

		popupMenu.add(exitMenuItem);

		Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("tray.png"));
		trayIcon = new TrayIcon(image);
		trayIcon.setImageAutoSize(true);
		trayIcon.setPopupMenu(popupMenu);
		trayIcon.setToolTip("TCP服务");

		trayIcon.addActionListener(new ShowAction(jFrame));

		return this;
	}

	public TrayListener addTrayIcon() {
		try {
			SystemTray.getSystemTray().add(this.trayIcon);
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
		return this;
	}

	/**
	 * 
	 */
	public void minimizeToTray() {

	}

}
