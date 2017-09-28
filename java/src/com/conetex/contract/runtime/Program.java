package com.conetex.contract.runtime;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.lang.access.Accessible;

public class Program {

	public static List<Accessible<?>>					assignments	= new LinkedList<>();

	public static List<Accessible<Boolean>>				boolExpress	= new LinkedList<>();

	public static List<Accessible<? extends Number>>	mathExpress	= new LinkedList<>();

}
