package pers.hawk.room.z.queue.entity;

import java.util.List;

/**
 * 策略
 */
public class Strategy {

	private long id;

	private String name;

	private String type;

	private int scycle;

	private String beginTime;

	private String endTime;

	private List<StrategyTask> strategyTaskList;

	private long userId;

	private long brancId;

	public List<StrategyTask> getStrategyTaskList() {
		return strategyTaskList;
	}

	public void setStrategyTaskList(List<StrategyTask> strategyTaskList) {
		this.strategyTaskList = strategyTaskList;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getScycle() {
		return scycle;
	}

	public void setScycle(int scycle) {
		this.scycle = scycle;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getBrancId() {
		return brancId;
	}

	public void setBrancId(long brancId) {
		this.brancId = brancId;
	}

}
