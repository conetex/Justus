package org.conetex.prime2.contractProcessing2.runtime;

import java.util.LinkedList;
import java.util.List;

import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Computable;
import org.conetex.prime2.contractProcessing2.lang.assignment.AbstractAssigment;

public class Program {

	public static List<Computable> steps = new LinkedList<Computable>();

	public static List<Accessible<Boolean>> boolExpress = new LinkedList<Accessible<Boolean>>();

	public static List<Accessible<? extends Number>> mathExpress = new LinkedList<Accessible<? extends Number>>();

	
	
}
