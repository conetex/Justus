package com.conetex.contract.runtime;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.SetableValue;

public class Heap {

	public static Heap create(Structure theRoot) {
		if (theRoot != null) {
			return new Heap(theRoot);
		}
		return null;
	}

	private final Structure root;

	private Heap(Structure theRoot) {
		this.root = theRoot;
	}

	public SetableValue createRef(String referenceString) {
		return null;
	}

}
