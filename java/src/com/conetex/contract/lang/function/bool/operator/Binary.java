package com.conetex.contract.lang.function.bool.operator;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Binary extends Accessible<Boolean>{// implements
															// Accessible<Boolean>

	public static Binary create(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB, String operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation.equals(Symbols.comAnd())){
			return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if(aA.booleanValue() && aB.booleanValue()){
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
				@Override
				public String getCommand() {
					return Symbols.comAnd();
				}
			};
		}
		else if(operation.equals(Symbols.comOr())){
			return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if(aA.booleanValue() || aB.booleanValue()){
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
				@Override
				public String getCommand() {
					return Symbols.comOr();
				}
			};
		}
		else if(operation.equals(Symbols.comXOr())){
			return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if(aA.booleanValue() ^ aB.booleanValue()){
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
				@Override
				public String getCommand() {
					return Symbols.comXOr();
				}
			};
		}
		return null;
	}

	private final Accessible<? extends Boolean> a;

	private final Accessible<? extends Boolean> b;

	Binary(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB) {
		super();
		this.a = theA;
		this.b = theB;
	}

	@Override
	public Accessible<?>[] getChildren() {
		return new Accessible<?>[] {this.a, this.b};
	}
	@Override
	public String[] getParameter() {
		return super.getParameterDft();
	}
	
	protected abstract Boolean calc(Boolean aA, Boolean aB);

	@Override
	public Boolean getFrom(Structure thisObject) throws AbstractRuntimeException {
		Boolean aA = this.a.getFrom(thisObject);
		Boolean aB = this.b.getFrom(thisObject);
		if(aA == null || aB == null){
			return null;
		}
		return this.calc(aA, aB);
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}
