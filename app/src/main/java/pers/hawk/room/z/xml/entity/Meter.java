package pers.hawk.room.z.xml.entity;

public class Meter {

	private String id;

	private String name;

	private String function;

	private String errror;

	private double value;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getErrror() {
		return errror;
	}

	public void setErrror(String errror) {
		this.errror = errror;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
