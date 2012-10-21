package com.grayditch.entity;

import java.util.ArrayList;

public class Subject {
	private String name;
	private ArrayList<Marks> marks;

	public Subject(String name, ArrayList<Marks> marks) {
		super();
		this.name = name;
		this.marks = marks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Marks> getMarks() {
		return marks;
	}

	public void setMarks(ArrayList<Marks> marks) {
		this.marks = marks;
	}

}
