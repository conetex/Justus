package com.conetex.contract.lang.assign;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.Setable;

public abstract class AbstractAssigment<T> extends Accessible<T> {// Computable{//
																	// extends
																	// ComputablePair<T>{

	private Setable<T> target;

	private Accessible<T> source;

	protected AbstractAssigment(Setable<T> trg, Accessible<T> src) {
		this.target = trg;
		this.source = src;
	}

	public abstract boolean doCopy();

	@Override
	public T getFrom(Structure thisObject) {
		T value = null;
		try {

			if (this.doCopy()) {
				value = this.source.copyFrom(thisObject);
			} else {
				value = this.source.getFrom(thisObject);
			}

			value = this.target.setTo(thisObject, value);

		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return value;
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		// TODO: das wird ja nur selten gebraucht...
		return getFrom(thisObject);
	}

	@Override
	public Class<T> getRawTypeClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
