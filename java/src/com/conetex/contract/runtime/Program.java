package com.conetex.contract.runtime;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.lang.Accessible;

public class Program {

	public static List<Accessible<?>> assignments = new LinkedList<Accessible<?>>();

	public static List<Accessible<Boolean>> boolExpress = new LinkedList<Accessible<Boolean>>();

	public static List<Accessible<? extends Number>> mathExpress = new LinkedList<Accessible<? extends Number>>();

}
