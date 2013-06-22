package com.grayditch.entity;

public class Marks {
	private Boolean newMark;
	private String type;
	private String percentage;
	private String mark;
	private String statistics;

	

	public String getStatistics() {
		return statistics;
	}

	public void setStatistics(String statistics) {
		this.statistics = statistics;
	}

	public Marks(Boolean newMark, String type, String percentage, String mark,
			String statistics) {
		super();
		this.newMark = newMark;
		this.type = type;
		this.percentage = percentage;
		this.mark = mark;
		this.statistics = statistics;
	}

	public Boolean getNewMark() {
		return newMark;
	}

	public void setNewMark(Boolean newMark) {
		this.newMark = newMark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
