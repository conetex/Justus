package com.conetex.contract.build;

import com.conetex.contract.lang.type.Attribute;

public class Constants {

	public static final Attribute<?>[]	noAttributes	= new Attribute<?>[0];

	private static final int			LABEL_SIZE		= 256;

	public static int labelSize() {
		return LABEL_SIZE;
	}

}
