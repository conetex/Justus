package com.conetex.justus.study;

public class Test {

	public static void main(String[] args) {

		Integer i = 10;
		Long l = 10l;
		Double d = 10.0;

		Number n = i;

		System.out.println(i.equals(d));
		if (i == n) {
			System.out.println("true");
		}

	}

}
