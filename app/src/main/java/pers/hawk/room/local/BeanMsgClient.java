package pers.hawk.room.local;

/**
 * 发送给客户端的命令
 *
 */
public class BeanMsgClient {

	private String meterName;
	
	private String meterCode;
	
	private String operat;

	public String getMeterCode() {
		return meterCode;
	}

	public void setMeterCode(String meterCode) {
		this.meterCode = meterCode;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getOperat() {
		return operat;
	}

	public void setOperat(String operat) {
		this.operat = operat;
	}
	
	
}
