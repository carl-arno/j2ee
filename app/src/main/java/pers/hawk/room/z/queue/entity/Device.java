package pers.hawk.room.z.queue.entity;

/**
 * 需要操作的设备
 */
public class Device {

	private String devSenId;

	private String deviceCode;

	private String deviceType;

	private String orderType;

	private Action action;

	public String getDevSenId() {
		return devSenId;
	}

	public void setDevSenId(String devSenId) {
		this.devSenId = devSenId;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	
	
}
