package org.conetex.prime2.contractProcessing2.lang;

public abstract class Subject {

	protected Subject parent;
	
	protected Subject(Subject theParent){
		this.parent = theParent;
	}
	
}
