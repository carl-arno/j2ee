package pers.hawk.room.dbserver;

/**
 * 指令队列
 */
public class BeanMsg {

	private long queueId;

	private String code;
	
	private long CMDId;

	private String lastCmd;
	
	private String content;

	public String getLastCmd() {
		return lastCmd;
	}

	public void setLastCmd(String lastCmd) {
		this.lastCmd = lastCmd;
	}

	public long getQueueId() {
		return queueId;
	}

	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}

	public long getCMDId() {
		return CMDId;
	}

	public void setCMDId(long cMDId) {
		CMDId = cMDId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
