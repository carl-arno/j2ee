package pers.hawk.room.server;

import java.nio.ByteBuffer;

public class SocketUtil {
	ByteBuffer byteBuffer = ByteBuffer.allocate(30000);
	boolean flag = false;

	int total = 0;
	int num = 0;
	byte[] head = new byte[7];
	byte[] byteThis;

	public void reset() {
		byteBuffer.clear();
		flag = false;
		num = 0;
		total = 0;
	}

}
