package pers.hawk.view.frame;

import java.awt.Color;

public class TabMenu implements Comparable<TabMenu> {

	private Integer sort;

	private String code;

	private String name;

	private String colorString;

	private Color color;

	private StringBuffer stringBuffer;

	public TabMenu(Integer sort, String code, String name, Color color, StringBuffer stringBuffer) {
		super();
		this.sort = sort;
		this.code = code;
		this.name = name;
		this.color = color;
		this.stringBuffer = stringBuffer;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StringBuffer getStringBuffer() {
		return stringBuffer;
	}

	public void setStringBuffer(StringBuffer stringBuffer) {
		this.stringBuffer = stringBuffer;
	}

	public int compareTo(TabMenu o) {
		return this.getSort().compareTo(o.getSort());
	}

	public String getColorString() {
		return colorString;
	}

	public void setColorString(String colorString) {
		this.colorString = colorString;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
