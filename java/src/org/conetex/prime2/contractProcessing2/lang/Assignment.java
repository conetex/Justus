package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value.ValueException;

public class Assignment<T> {

	public static <T> Assignment<T> createCopy(Reference2Value<T> src, Reference2Value<T> trg){
		if(src == null || trg == null){
			return null;
		}
		return new Assignment<T>(src, trg, true);
	}

	public static <T> Assignment<T> createRef(Reference2Value<T> src, Reference2Value<T> trg){
		if(src == null || trg == null){
			return null;
		}
		return new Assignment<T>(src, trg, true);
	}
	
	private Reference2Value<T> source;
	
	private Reference2Value<T> target;
	
	private boolean copy;
	
	private Assignment(Reference2Value<T> src, Reference2Value<T> trg, boolean doCopy){
		this.source = src;
		this.target = trg;
		this.copy = doCopy;
	}

	public boolean compute() {
		T value = null;
		if(this.copy){
			value = source.getCopy();			
		}
		else{
			value = source.get();
		}

		try {
			target.set( value );
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
			
		return true;
	}	
	
}
