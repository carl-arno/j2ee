package pers.hawk.a;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * COM服务
 */
public class PortServer {

	private Logger logger = Logger.getLogger(this.getClass());
	private DateFormat format;

	private CommPortIdentifier commPortIdentifier;

	private SerialPort serialPort;

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public DateFormat getFormat() {
		return format;
	}

	public void setFormat(DateFormat format) {
		this.format = format;
	}

	public void test() {
		try {
			
			format=new SimpleDateFormat();
			
			commPortIdentifier=	CommPortIdentifier.getPortIdentifier("COM3");
			serialPort = (SerialPort) commPortIdentifier.open("SerialReader", 1000);

			serialPort.addEventListener(new PortListen());
			serialPort.notifyOnDataAvailable(true);
			serialPort.setSerialPortParams(9600, 8, 1, 0);
		} catch (Exception e) {
			logger.warn(e.toString());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PortServer().test();
	}
	
	private class PortListen implements Runnable, SerialPortEventListener{

		public void serialEvent(SerialPortEvent arg0) {
			switch (arg0.getEventType()) {
			case SerialPortEvent.BI: // 10
			case SerialPortEvent.OE: // 7
			case SerialPortEvent.FE: // 9
			case SerialPortEvent.PE: // 8
			case SerialPortEvent.CD: // 6
			case SerialPortEvent.CTS: // 3
			case SerialPortEvent.DSR: // 4
			case SerialPortEvent.RI: // 5
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
				break;
			case SerialPortEvent.DATA_AVAILABLE: // 1
				try {
					byte[] cache = new byte[10240];
					int total = 0;
					InputStream inputStream = serialPort.getInputStream();

					while (inputStream.available() > 0) {
						total += inputStream.read(cache);
						synchronized (format) {
							try {
								format.wait(50);
							} catch (InterruptedException e) {
								logger.warn(e.toString());
								e.printStackTrace();
							}
							format.notifyAll();
						}
						if (inputStream.available() <= 0) {
							synchronized (format) {
								try {
									format.wait(500);
								} catch (InterruptedException e) {
									logger.warn(e.toString());
									e.printStackTrace();
								}
								format.notifyAll();
							}
						}
						// dothings
						StringBuffer stringBuffer = new StringBuffer();
						for (int i = 0; i < total; i++) {
							stringBuffer.append(String.valueOf(cache[i]));
						}
						System.out.println(total);
						System.out.println(stringBuffer.toString());
						// dothings
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		public void run() {
			
		}
	}
	
	
}
