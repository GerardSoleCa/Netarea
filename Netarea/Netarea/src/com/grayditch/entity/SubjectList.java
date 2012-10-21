package com.grayditch.entity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.ListIterator;

import android.graphics.Bitmap;


public class SubjectList {

	private static SubjectList INSTANCE = null;
	private String student = "";
	private Bitmap picture = null;
	
	
	
	ArrayList<Subject> subjects = new ArrayList<Subject>();

	// SINGLETON: fem que només hi hagi una instància a aquesta classe en tot el
	// projecte, així sempre es tractarà de la mateixa sessió
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubjectList();
		}
	}

	public static SubjectList getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void addSubjects(ArrayList<Subject> s) {
		subjects = s;
	}

	public ListIterator<Subject> getIterator() {
		ListIterator<Subject> iterator = subjects.listIterator();
		return iterator;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
}
