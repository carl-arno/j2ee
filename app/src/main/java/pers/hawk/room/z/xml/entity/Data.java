package pers.hawk.room.z.xml.entity;

import java.util.Date;

public class Data {

	private EnergyItems energyItems;

	private Meters meters;

	private String time;

	private Date theTime;

	public EnergyItems getEnergyItems() {
		return energyItems;
	}

	public void setEnergyItems(EnergyItems energyItems) {
		this.energyItems = energyItems;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Meters getMeters() {
		return meters;
	}

	public void setMeters(Meters meters) {
		this.meters = meters;
	}

	public Date getTheTime() {
		return theTime;
	}

	public void setTheTime(Date theTime) {
		this.theTime = theTime;
	}

}
