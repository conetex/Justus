package org.conetex.prime2.contractProcessing2.runtime;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;

public class Heap {
	
	public static Heap create(Structure theRoot){
		if(theRoot != null){
			return new Heap(theRoot);
		}
		return null;
	}
	
	private final Structure root;

	private Heap(Structure theRoot){
		this.root = theRoot;
	}
	
	public AccessibleValue createRef(String referenceString){
		return null;
	}
	
}