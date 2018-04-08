package pers.hawk.room.z.queue.entity;

import java.util.Date;

/**
 * 策略详细
 */
public class StrategyTask implements java.io.Serializable {
	private static final long serialVersionUID = 4715113491010400450L;
	private long id;
	private long strategyID;
	private String deviceName;
	/**
	 * 设备编码，控制设备时所用
	 */
	private String code;
	private int strategyClass;
	private long devSenId;
	private int status;
	private Date startDate;
	private Date endDate;

	private String startString;

	private String endString;

	private String memo;

	public String getStartString() {
		return startString;
	}

	public void setStartString(String startString) {
		this.startString = startString;
	}

	public String getEndString() {
		return endString;
	}

	public void setEndString(String endString) {
		this.endString = endString;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(long strategyID) {
		this.strategyID = strategyID;
	}

	public int getStrategyClass() {
		return strategyClass;
	}

	public void setStrategyClass(int strategyClass) {
		this.strategyClass = strategyClass;
	}

	public long getDevSenId() {
		return devSenId;
	}

	public void setDevSenId(long devSenId) {
		this.devSenId = devSenId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}