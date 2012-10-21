package com.grayditch.entity;

import com.grayditch.program.FunctionForMarks;

public class FunctionForMarksSingleton {
	private static FunctionForMarks INSTANCE = null;
	FunctionForMarks function = new FunctionForMarks();

	// SINGLETON: fem que nom�s hi hagi una inst�ncia a aquesta classe en tot el
	// projecte, aix� sempre es tractar� de la mateixa sessi�
	private synchronized static void createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FunctionForMarks();
		}
	}

	public static FunctionForMarks getInstance() {
		if (INSTANCE == null) {
			createInstance();
		}
		return INSTANCE;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
}
