package com.grayditch.entity;

public class MarksPerSubject {
	private String name;
	private int numMarks;

	public MarksPerSubject(String name, int numMarks) {
		super();
		this.name = name;
		this.numMarks = numMarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumMarks() {
		return numMarks;
	}

	public void setNumMarks(int numMarks) {
		this.numMarks = numMarks;
	}

}
