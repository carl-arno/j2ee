package pers.hawk.view.frame;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * @author Adler
 *
 */
public class ShowAction implements ActionListener {

	private JFrame jFrame;

	public ShowAction(JFrame jFrame) {
		super();
		this.jFrame = jFrame;
	}

	public void actionPerformed(ActionEvent e) {
		jFrame.setVisible(true);
		jFrame.setExtendedState(Frame.NORMAL);
	}

}
