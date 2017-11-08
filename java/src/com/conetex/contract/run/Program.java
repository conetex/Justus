package com.conetex.contract.run;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.lang.function.Accessible;

class Program{

	public static List<Accessible<?>> assignments = new LinkedList<>();

	public static List<Accessible<Boolean>> boolExpress = new LinkedList<>();

	public static List<Accessible<? extends Number>> mathExpress = new LinkedList<>();

}
