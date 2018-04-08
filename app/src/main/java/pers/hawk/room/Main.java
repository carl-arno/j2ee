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

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ch.randelshofer.quaqua.QuaquaManager;

/**
 * Main.
 *
 * @author Werner Randelshofer
 * @version $Id: Main.java 462 2014-03-22 09:23:12Z wrandelshofer $
 */

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Main main=new Main();
		main.doQue(args);
		
		Engine engine = new Engine(args);
		engine.doTree();
		engine.doJFrame(engine);

	}

	private void doQue(String[] args){
		List<?> argList = Arrays.asList(args); // Explicitly turn on font
		// antialiasing.
		try {
			System.setProperty("swing.aatext", "true");
		} catch (AccessControlException e) {
			// can't do anything about this
		}

		// Use screen menu bar, if not switched off explicitly
		try {
			if (System.getProperty("apple.laf.useScreenMenuBar") == null && System.getProperty("com.apple.macos.useScreenMenuBar") == null) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.macos.useScreenMenuBar", "true");
			}
		} catch (AccessControlException e) {
			// can't do anything about this
		}

		// Add Quaqua to the lafs
		// ArrayList<LookAndFeelInfo> infos = new
		// ArrayList<LookAndFeelInfo>(Arrays.asList(UIManager.getInstalledLookAndFeels()));
		ArrayList<LookAndFeelInfo> infos = new ArrayList<LookAndFeelInfo>();
//		infos.add(new LookAndFeelInfo("Windows", QuaquaManager.getLookAndFeelClassName()));
//		infos.add(new LookAndFeelInfo("Mac", QuaquaManager.getLookAndFeelClassName()));
		UIManager.setInstalledLookAndFeels(infos.toArray(new LookAndFeelInfo[infos.size()]));

		// Turn on look and feel decoration when not running on Mac OS X or
		// Darwin.
		//
		// This will still not look pretty, because we haven't got cast shadows
		// for the frame on other operating systems.
		boolean useDefaultLookAndFeelDecoration = !System.getProperty("os.name").toLowerCase().startsWith("mac")
				&& !System.getProperty("os.name").toLowerCase().startsWith("darwin");
		int index = argList.indexOf("-decoration");
		if (index != -1 && index < argList.size() - 1) {
			useDefaultLookAndFeelDecoration = argList.get(index + 1).equals("true");
		}

		if (useDefaultLookAndFeelDecoration) {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		}

		index = argList.indexOf("-laf");
		String lafName;
		lafName = QuaquaManager.getLookAndFeelClassName();
		if (!lafName.equals("default")) {
			if (lafName.equals("system")) {
				lafName = UIManager.getSystemLookAndFeelClassName();
			} else if (lafName.equals("crossplatform")) {
				lafName = UIManager.getCrossPlatformLookAndFeelClassName();
			}
			try {
				LookAndFeel laf = (LookAndFeel) Class.forName(lafName).newInstance();
				UIManager.setLookAndFeel(laf);
			} catch (Exception e) {
				System.err.println("Error setting " + lafName + " in UIManager.");
				e.printStackTrace();
				// can't do anything about this
			}
		}
	}

}
