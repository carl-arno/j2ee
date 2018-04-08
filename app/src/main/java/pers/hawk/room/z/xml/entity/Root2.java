package pers.hawk.room.z.xml.entity;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonRootName("root")
@JacksonXmlRootElement(localName="root")
public class Root2 extends Root {

	private HeartBeat heart_beat;

	public HeartBeat getHeart_beat() {
		return heart_beat;
	}

	public void setHeart_beat(HeartBeat heart_beat) {
		this.heart_beat = heart_beat;
	}

}
