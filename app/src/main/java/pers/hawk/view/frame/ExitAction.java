package pers.hawk.view.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitAction implements ActionListener {

	private Thread thread;

	private boolean restart;

	public ExitAction(Thread thread, boolean restart) {
		super();
		this.thread = thread;
		this.restart = restart;
	}

	public void actionPerformed(ActionEvent e) {
		if (!restart) {
			Runtime.getRuntime().removeShutdownHook(thread);
		}
		System.exit(0);
	}

}
