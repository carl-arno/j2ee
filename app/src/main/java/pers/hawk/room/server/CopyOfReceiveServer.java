package pers.hawk.room.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import pers.hawk.room.Injection;
import pers.hawk.room.util.ByteStringUtil;
import pers.hawk.room.util.AppConst;
import pers.hawk.view.frame.TabMenu;

/**
 * 接收数据服务
 */
public class CopyOfReceiveServer implements Runnable {

	private Logger logger = Logger.getLogger(this.getClass());
	private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private ReceiveServiceImp receiveService;

	public Map<String, TabMenu> mapTabMenu;

	private Selector selector;

	public CopyOfReceiveServer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		StringBuffer stringBuffer = null;

		try {
			initSocket();
		} catch (IOException e) {
			logger.warn(e.toString());
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 无法启动TCP服务,检查端口是否被占用.10秒后尝试重新启动").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			synchronized (format) {
				try {
					format.wait(10 * 1000);
				} catch (InterruptedException e1) {
					logger.warn(e.toString());
					e1.printStackTrace();
				}
				format.notifyAll();
			}

			run();
		}
	}

	/**
	 * 打开socket,允许客户端连接
	 * 
	 * @throws Exception
	 */
	public void initSocket() throws IOException {

		StringBuffer stringBuffer = null;

		selector = Selector.open();

		List<Integer> portList = new ArrayList<Integer>();
		portList.add(1917);
		portList.add(1918);

		for (int i = 0; i < portList.size(); ++i) {
			ServerSocketChannel serverSocketChannel = null;
			(serverSocketChannel = ServerSocketChannel.open()).configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(portList.get(i)));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		}

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务已启动.").append(" 已监听端口 {");
			stringBuffer.append(org.apache.commons.lang3.StringUtils.join(portList, ",")).append("} ").append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				if ((selectionKey.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					SocketChannel socketChannel = null;
					(socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept()).configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					iterator.remove();
					synchronized (mapTabMenu) {
						stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
						stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 检测到客户端连接 ").append(AppConst.line);
						stringBuffer.append(socketChannel).append(AppConst.line);
						mapTabMenu.notifyAll();
					}
				} else if ((selectionKey.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					ByteBuffer byteBuffer = ByteBuffer.allocate(102400);

					testTCP(socketChannel, byteBuffer, new SocketUtil());

					// readBytesNew(socketChannel, byteBuffer);

					// int bytesTotal = readBytes(socketChannel, byteBuffer);
					//
					// if (bytesTotal > 0) {
					// byte[] bytes = bufferToByte(socketChannel, byteBuffer,
					// bytesTotal);
					// try {
					// doParse(socketChannel, bytes);
					// } catch (RuntimeException e) {
					// logger.warn("解析数据时发生错误,已停止解析.");
					// } catch (Exception e) {
					// logger.warn("解析数据时发生错误,已停止解析.");
					// }
					//
					// } else {
					// // net工具会导致无期限发送
					// synchronized (mapTabMenu) {
					// stringBuffer =
					// mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
					// stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 检测到空数据包,客户端可能已经关闭了与服务器的连接.").append(AppConst.line);
					// stringBuffer.append(socketChannel).append(AppConst.line);
					// logger.warn(stringBuffer.toString());
					// mapTabMenu.notifyAll();
					// }
					// closeClient(socketChannel);
					// }
					iterator.remove();
				}
			}

		}

	}

	/**
	 * 主动关闭与客户端的连接
	 */
	public void closeClient(SocketChannel socketChannel) {
		StringBuffer stringBuffer = null;
		try {
			socketChannel.close();
		} catch (IOException e) {
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 断开连接时发生错误。").append(AppConst.line);
				logger.warn(stringBuffer.toString());
				mapTabMenu.notifyAll();
			}
		}
	}

	/**
	 * 读取字节数据
	 */
	public int readBytes(SocketChannel socketChannel, ByteBuffer byteBuffer) {

		StringBuffer stringBuffer = null;

		int number_of_bytes = 0;
		int bytesTotal = 0;
		try {
			synchronized (receiveService) {
				try {
					receiveService.wait(500);
				} catch (InterruptedException e) {
					logger.warn(e.toString());
					e.printStackTrace();
				}
				while (true) {
					byteBuffer.clear();
					number_of_bytes = socketChannel.read(byteBuffer);
					if (number_of_bytes <= 0) {
						break;
					}
					byteBuffer.flip();
					bytesTotal += number_of_bytes;
				}
			}
		} catch (IOException e) {
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 网络异常,读取数据失败,与客户端的连接断开。").append(AppConst.line);
				stringBuffer.append(socketChannel).append(AppConst.line);
				logger.warn(stringBuffer.toString());
				mapTabMenu.notifyAll();
			}
			logger.warn("\r\nIOException 读取数据失败:" + e.getMessage() + "\r\n");

			// close socket?
			// closeClient(socketChannel);
			// close socket?
		}

		return bytesTotal;
	}

	@SuppressWarnings("unused")
	public int readBytesNew(SocketChannel socketChannel, ByteBuffer byteBuffer) {

		int bufferTotal = 0;
		int bufferThis = 0;

		byte[] bytes = null;
		byte[] tmpByte = null;

		int bodyLength = 0;

		try {

			byteBuffer.clear();

			// step 1
			bufferThis = socketChannel.read(byteBuffer);
			bufferTotal += bufferThis;
			byteBuffer.flip();

			tmpByte = new byte[bufferThis];
			byteBuffer.get(tmpByte, 0, bufferThis);

			if (bufferThis < 7) {
				while (true) {
					synchronized (receiveService) {
						try {
							receiveService.wait(50);
						} catch (InterruptedException e) {
							logger.warn(e.toString());
							e.printStackTrace();
						}
						bufferThis = socketChannel.read(byteBuffer);
						bufferTotal += bufferThis;
						byteBuffer.flip();
						receiveService.notifyAll();
					}
				}
			} else {

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void testTCP(SocketChannel socketChannel, ByteBuffer byteBuffer, SocketUtil utils) throws IOException {

		// 00 00 03 00 00 00 02 01 02

		utils.bufferThis = socketChannel.read(byteBuffer);
		utils.bufferTotal += utils.bufferThis;
		byteBuffer.flip();

		utils.tmpByte = new byte[utils.bufferThis];
		byteBuffer.get(utils.tmpByte, 0, utils.bufferThis);

		utils.bytes = ArrayUtils.addAll(utils.bytes, utils.tmpByte);

		if (utils.bufferTotal < 7) {
			synchronized (receiveService) {
				try {
					receiveService.wait(50);
				} catch (InterruptedException e) {
					logger.warn(e.toString());
					e.printStackTrace();
				}
				receiveService.notifyAll();
			}
			testTCP(socketChannel, byteBuffer, utils);
		} else if (utils.bufferTotal < 7 + ByteStringUtil.byteArrayToInt(utils.bytes, 3)) {
			synchronized (receiveService) {
				try {
					receiveService.wait(50);
				} catch (InterruptedException e) {
					logger.warn(e.toString());
					e.printStackTrace();
				}
				receiveService.notifyAll();
			}
			testTCP(socketChannel, byteBuffer, utils);
		}
	}

	private class SocketUtil {
		int bufferTotal = 0;
		int bufferThis = 0;
		byte[] bytes;
		byte[] tmpByte;
	}

	/**
	 * 缓冲字符转字节
	 */
	public byte[] bufferToByte(SocketChannel socketChannel, ByteBuffer byteBuffer, int bytesTotal) {
		byte[] bytes = null;

		bytes = new byte[bytesTotal];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = byteBuffer.get(i);
		}

		String byteString = ByteStringUtil.getByteString(bytes);

		StringBuffer stringBuffer = null;
		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 接收到客户端数据 ").append(bytes.length).append(" 个字节");
			stringBuffer.append(byteString).append(AppConst.line);
			mapTabMenu.notifyAll();
		}

		return bytes;
	}

	/**
	 * 发送客户端消息
	 * 
	 * @param socketChannel
	 * @param byteBuffer
	 */
	public void sendMsg(SocketChannel socketChannel, ByteBuffer byteBuffer) {
		StringBuffer stringBuffer = null;

		try {
			byteBuffer.flip();
			while (byteBuffer.hasRemaining()) {
				socketChannel.write(byteBuffer);
			}
		} catch (IOException e) {
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 网络异常，发送数据到客户端失败，与客户端的连接断开。").append(AppConst.line);
				logger.warn(stringBuffer.toString());
				mapTabMenu.notifyAll();
			}
			logger.warn("\r\nIOException 发送数据到客户端失败:" + e.getMessage() + "\r\n");

			// close socket?
			// closeClient(socketChannel);
			// close socket?
		}
	}

	/**
	 * 分析数据
	 */
	public void doParse(SocketChannel socketChannel, byte[] bytes) {

		// HEAD：消息头，2 个字节，固定为0x1F1F。
		// TYPE：消息类型，1 个字节：
		//  0x1：身份认证，Data 体是明文数据
		//  0x2：心跳信息，Data 体是明文数据
		//  0x3：能耗数据，Data 体是通过AES 加密后的数据
		//  0x4: 配置信息，Data 体是明文数据
		// LENGTH：4 个字节，Integer 整型，指明消息体Data 长度，采用网络
		// 字节顺序（高位字节在前）。
		// DATA：应用层数据包，明文或是经过AES 加密后的数据，原始数据是变
		// 长字符串，xml 格式的消息，UTF-8 编码，格式见附录1。
		// CRC: 校验和，采用CCITT-16。

		StringBuffer stringBuffer = null;
		ByteBuffer respBuffer = null;

		byte[] bodyByte = null;

		try {
			bodyByte = getBodyByte(bytes);
		} catch (Exception e) {
			synchronized (mapTabMenu) {
				stringBuffer = mapTabMenu.get(Injection.menuString[3]).getStringBuffer();
				stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" 解析客户端数据时发生错误,请检查客户端数据是否符合规范.").append(AppConst.line);
				mapTabMenu.notifyAll();
			}
			logger.warn(e.toString());
			// throw new RuntimeException(e);
		}

		if (0x1f == bytes[0] && 0x1f == bytes[1]) {
			switch (bytes[2]) {
			case 0x1:
				respBuffer = stringToByte(receiveService.clientVerify(socketChannel, bodyByte).toString(), bytes);
				sendMsg(socketChannel, respBuffer);
				break;
			case 0x2:
				respBuffer = stringToByte(receiveService.clientKeep(socketChannel, bodyByte).toString(), bytes);
				sendMsg(socketChannel, respBuffer);
				break;
			case 0x3:
				respBuffer = stringToByte(receiveService.clientData(socketChannel, bodyByte).toString(), bytes);
				sendMsg(socketChannel, respBuffer);
				break;
			case 0x5:
				receiveService.clientResult(socketChannel, bodyByte);
				break;
			default:
				receiveService.clientDeprate(socketChannel, bytes);
				break;
			}
		} else {
			receiveService.clientDeprate(socketChannel, bytes);
		}

		synchronized (mapTabMenu) {
			stringBuffer = mapTabMenu.get(Injection.menuString[1]).getStringBuffer();
			stringBuffer.append(format.format(Calendar.getInstance().getTime())).append(" TCP服务响应客戶端结束").append(AppConst.line);
			mapTabMenu.notifyAll();
		}
	}

	/**
	 * 获得 data body部分
	 * 
	 * @param bytes
	 * @return
	 */
	public byte[] getBodyByte(byte[] bytes) {
		byte[] bodys = null;
		// bodys = bytes;

		int offset = 3;
		int start = 7;
		int length = ByteStringUtil.byteArrayToInt(bytes, offset);

		if (length > 1000000) {
			throw new RuntimeException("解析到数据包长度过大");
		}

		bodys = new byte[length];
		for (int i = start; i < length + start; i++) {
			bodys[i - start] = bytes[i];
		}
		return bodys;
	}

	/**
	 * String转ByteBuffer
	 * 
	 * @param string
	 * @param byteHead
	 * @return
	 */
	public ByteBuffer stringToByte(String string, byte[] byteHead) {
		ByteBuffer byteBuffer = null;
		byte[] bytes = null;
		try {
			bytes = string.getBytes("UTF-8");
			byteBuffer = ByteBuffer.allocate(bytes.length + 7);

			for (int i = 0; i < 3; i++) {
				byteBuffer.put(byteHead[i]);
			}
			byte[] byteLength = ByteStringUtil.int2Bytes(bytes.length, 4);
			for (int i = 0; i < byteLength.length; i++) {
				byteBuffer.put(byteLength[i]);
			}

			for (int i = 0; i < bytes.length; i++) {
				byteBuffer.put(bytes[i]);
			}
		} catch (UnsupportedEncodingException e) {
			logger.warn("\r\n: " + e.getMessage() + "\r\n");
			throw new RuntimeException(e);
		}
		return byteBuffer;
	}

	public ReceiveServiceImp getReceiveService() {
		return receiveService;
	}

	public void setReceiveService(ReceiveServiceImp receiveService) {
		this.receiveService = receiveService;
	}

}
