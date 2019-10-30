package com.conetex.contract.lang.function.runtime;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.BuildFunctions.Assign;
import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.CodeModel;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.CodeModel.EggAbstr;
import com.conetex.contract.build.CodeModel.EggFun;
import com.conetex.contract.build.CodeModel.EggFunImp;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateOperation;
import com.conetex.contract.build.exceptionFunction.OperationMeansNotCalled;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleConstant;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.ContractRuntime;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Request extends Accessible<String> {

	/*
	// lambda:
	public static interface Buildable {
		public void build();
	} 
	public static void register(Buildable f){	}
	Buildable b = () -> Request.build();
	CodeModel.register(b);
	CodeModel.register( () -> Request.build() );
	CodeModel.register( Request::build );
	*/
	
	private static String getCommandStatic() {
		return Symbols.COM_REQUEST;
	}
	
	public static void build() throws DublicateOperation, OperationMeansNotCalled{
		String param = "question";
		EggFun<String> request = new CodeModel.EggFunImp<String>("stringRequest"){
			
			@Override
			public Accessible<String> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				//return AccessibleConstant.create2(String.class, thisNode);
				String question = thisNode.getParameter(param);
				return new Request(question);
			}
			
		};
		request.means(Request.getCommandStatic());
		request.registerParameters(new String[] {param});
		//Expression.numberExpession.functionContains(request);
		BuildFunctions.Assign.whatEverAssigment.functionContains(request);
		
	}
		
	private final String question;
		
	Request(String theQuestion) {
		super();
		this.question = theQuestion;
	}
	
	@Override
	public String getFrom(Structure thisObject) throws AbstractRuntimeException {
		return ContractRuntime.stringAgency.getStringAnswer(this.question);
	}

	@Override
	public String copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return getFrom(thisObject);
	}

	@Override
	public Class<String> getRawTypeClass() {
		return String.class;
	}

	@Override
	public String getCommand() {
		return Request.getCommandStatic();
	}



	@Override
	public Accessible<?>[] getChildren() {
		return super.getChildrenDft();
	}

	@Override
	public String[] getParameter() {
		return new String[] { this.question };
	}

}
