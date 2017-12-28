package com.conetex.contract.lang.function.assign;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleValue;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public abstract class AbstractAssigment<T> extends Accessible<T> {// Computable{//
																	// extends
																	// ComputablePair<T>{

	private final AccessibleValue<T>	target;

	private final Accessible<T>			source;

	AbstractAssigment(AccessibleValue<T> trg, Accessible<T> src) {
		super();
		this.target = trg;
		this.source = src;
	}

	@Override
	public Accessible<?>[] getChildren() {
		return new Accessible<?>[] { this.target, this.source };
	}

	@Override
	public String[] getParameter() {
		return super.getParameterDft();
	}

	protected abstract boolean doCopy();

	@Override
	public T getFrom(Structure thisObject) throws AbstractRuntimeException {
		T value = null;
		try {

			if (this.doCopy()) {
				value = this.source.copyFrom(thisObject);
			}
			else {
				value = this.source.getFrom(thisObject);
			}

			value = this.target.setTo(thisObject, value);

		}
		catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return value;
	}

	@Override
	public T copyFrom(Structure thisObject) throws AbstractRuntimeException {
		// TODO: das wird ja nur selten gebraucht...
		return getFrom(thisObject);
	}

	@Override
	public Class<T> getRawTypeClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
