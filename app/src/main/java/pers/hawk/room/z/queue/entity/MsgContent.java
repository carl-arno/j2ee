package pers.hawk.room.z.queue.entity;

/**
 * 服务发来的消息实体
 *
 */
public class MsgContent {

	private int type;
	
	private int from_type;
	
	private long userId;
	
	private String command;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getFrom_type() {
		return from_type;
	}

	public void setFrom_type(int from_type) {
		this.from_type = from_type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	
	
}
