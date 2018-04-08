package pers.hawk.view.frame;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Restart implements Runnable {

	private Logger logger = Logger.getLogger(this.getClass());
	
	public void run() {
		try {
			Runtime.getRuntime().exec("javaw -jar room.jar");
		} catch (IOException e) {
			logger.warn(e.toString());
			e.printStackTrace();
		}
	}

}
