package pers.hawk.room.z.queue.entity;

/**
 * 设备需要进行的操作
 */
public class Action {

	private String name;

	private String orderValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderValue() {
		return orderValue;
	}

	public void setOrderValue(String orderValue) {
		this.orderValue = orderValue;
	}

}
