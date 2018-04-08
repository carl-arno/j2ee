package pers.hawk.room.z.xml.entity;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonRootName("root")
@JacksonXmlRootElement(localName="root")
public class Root1 extends Root {

	private IdValidate id_validate;

	public IdValidate getId_validate() {
		return id_validate;
	}

	public void setId_validate(IdValidate id_validate) {
		this.id_validate = id_validate;
	}

}
