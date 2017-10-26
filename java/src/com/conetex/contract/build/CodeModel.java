package com.conetex.contract.build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.build.BuildFunctions.Assign;
import com.conetex.contract.build.BuildFunctions.Constant;
import com.conetex.contract.build.BuildFunctions.Control;
import com.conetex.contract.build.BuildFunctions.Data;
import com.conetex.contract.build.BuildFunctions.Expression;
import com.conetex.contract.build.BuildFunctions.Fun;
import com.conetex.contract.build.BuildFunctions.FunCall;
import com.conetex.contract.build.BuildFunctions.FunReturn;
import com.conetex.contract.build.BuildFunctions.Reference;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.DublicateOperation;
import com.conetex.contract.build.exceptionLang.OperationMeansNotCalled;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.lang.access.Accessible;

public class CodeModel {

	
	
	
	public static abstract class Box<T, S> extends Egg<T> {

		private Map<String, Egg<? extends S>> childBuilder = new HashMap<>();

		public Box(String theName) {
			super(theName);
		}

		public Box(String theName, int i) {
			
			super(theName);
		}
		
		public final void contains(String theOperationName, Egg<? extends S> b) {
			if (this.childBuilder.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void contains(Egg<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.contains(s, b);
			}
		}

		public final Accessible<? extends S> createChild(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
			String name = n.getCommand();
			Egg<? extends S> s = this.childBuilder.get(name);
			if (s == null) {
				System.err.println("inner Operation '" + name + "' not found in " + this.getName());
				return null;
			}
			return s.createThis(n, parentTyp);
		}

		public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException;

	}


	public static abstract class Egg<T> {

		private static Map<String, List<Egg<?>>> instances = new HashMap<>();

		public static List<Egg<?>> getInstance(String command){
			return Egg.instances.get(command);
		}
		
		private String name;
		
		private String[] parameterNames;

		private Set<String> meaning = new HashSet<>();

		protected Egg(String theName) {
			this.name = theName;
		}

		public final String getName() {
			return this.name;
		}


		
		final Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
			if (!this.meaning.contains(n.getCommand())) {
				System.err.println("Operation " + n.getCommand() + " not found!");
				return null;
			}
			return this.create(n, parentTyp);
		}

		public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException;

		final Set<String> keySet() {
			return this.meaning;
		}

		public final void means(String theOperationName) throws DublicateOperation {
			if (this.meaning.contains(theOperationName)) {
				//System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
				throw new DublicateOperation( "duplicate operation '" + theOperationName + "' in " + this.getName() );
			}
			this.meaning.add(theOperationName);
			List<Egg<?>> instanceList = Egg.getInstance(theOperationName);
			if(instanceList == null){
				instanceList = new LinkedList<>();
				Egg.instances.put(theOperationName, instanceList);
			}
			instanceList.add(this);			
		}

		public final void means(String[] theOperationNames) throws DublicateOperation {
			for (String theOperationName : theOperationNames) {
				this.means(theOperationName);
			}
		}
		
		public final void registerParameters(String[] theParameterNames) {
			if (this.parameterNames != null) {
				System.err.println("duplicate Param call");
			}
			this.parameterNames = theParameterNames;
		}

		public final int getParameterIndex(String p) {
			for (int j = 0; j < this.parameterNames.length; j++) {
				if (this.parameterNames[j] == p) {
					return j;
				}
			}
			return -1;
		}
		
		public final String[] getParameters() {
			return this.parameterNames;
		}
		
	}
	
	static void buildBool() throws AbstractInterpreterException {
		Expression.boolExpression.contains(Expression.boolExpression);
		Expression.boolExpression.contains(Expression.boolComparsion);
		Expression.boolExpression.contains(Expression.boolNullCheck);
		Expression.boolExpression.contains(Reference.boolRef);
		Expression.boolExpression.contains(Constant.boolConst);
		Expression.boolExpression.contains(FunCall.boolCall);

		Assign.boolAssigment.contains(Expression.boolExpression);
		Assign.boolAssigment.contains(Expression.boolComparsion);
		Assign.boolAssigment.contains(Expression.boolNullCheck);
		Assign.boolAssigment.contains(Reference.boolRef);
		Assign.boolAssigment.contains(Constant.boolConst);
		Assign.boolAssigment.contains(FunCall.boolCall);
		Assign.whatEverAssigment.contains(Expression.boolExpression);
		Assign.whatEverAssigment.contains(Expression.boolComparsion);
		Assign.whatEverAssigment.contains(Expression.boolNullCheck);
		// Assign.whatEverAssigment.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		FunReturn.boolReturn.contains(Expression.boolExpression);
		FunReturn.boolReturn.contains(Expression.boolComparsion);
		FunReturn.boolReturn.contains(Expression.boolNullCheck);
		FunReturn.boolReturn.contains(Reference.boolRef);
		FunReturn.boolReturn.contains(Constant.boolConst);
		FunReturn.boolReturn.contains(FunCall.boolCall);
		FunReturn.whatEverReturn.contains(Expression.boolExpression);
		FunReturn.whatEverReturn.contains(Expression.boolComparsion);
		FunReturn.whatEverReturn.contains(Expression.boolNullCheck);
		// FunReturn.whatEverReturn.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		Control.unknownLoop.contains(Expression.boolExpression);
		Control.unknownLoop.contains(Expression.boolComparsion);
		Control.unknownLoop.contains(Expression.boolNullCheck);

		Control.unknownIf.contains(Expression.boolExpression);
		Control.unknownIf.contains(Expression.boolComparsion);
		Control.unknownIf.contains(Expression.boolNullCheck);
		// Control.unknownIf.contains(Reference.boolRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.boolConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.boolCall); // done by
		// Reference.whatEverCall

		Fun.bool.contains(FunReturn.boolReturn);
		Fun.noReturn.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.noReturn.contains(Expression.boolComparsion);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.structure.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.structure.contains(Expression.boolComparsion);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.unknown.contains(Expression.boolExpression);// TODO eigentlich nur
														// hinter Zuweisung
		Fun.unknown.contains(Expression.boolComparsion);// TODO eigentlich nur
														// hinter Zuweisung
	}

	static void buildNumber() throws AbstractInterpreterException {
		Expression.boolComparsion.contains(Expression.numberExpession);
		Expression.boolComparsion.contains(Reference.numberRef);
		Expression.boolComparsion.contains(Constant.numberConst);
		Expression.boolComparsion.contains(FunCall.numberCall);
		Expression.numberExpession.contains(Expression.numberExpession);
		Expression.numberExpession.contains(Reference.numberRef);
		Expression.numberExpession.contains(Constant.numberConst);
		Expression.numberExpession.contains(FunCall.numberCall);

		Control.unknownLoop.contains(Expression.numberExpession);
		Control.unknownIf.contains(Expression.numberExpession);
		// Control.unknownIf.contains(Reference.numberRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.numberConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.numberCall);// done by
		// Reference.whatEverCall

		Assign.numberAssigment.contains(Expression.numberExpession);
		Assign.numberAssigment.contains(Reference.numberRef);
		Assign.numberAssigment.contains(Constant.numberConst);
		Assign.numberAssigment.contains(FunCall.numberCall);
		Assign.whatEverAssigment.contains(Expression.numberExpession);
		// Assign.whatEverAssigment.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		FunReturn.numberReturn.contains(Expression.numberExpession);
		FunReturn.numberReturn.contains(Reference.numberRef);
		FunReturn.numberReturn.contains(Constant.numberConst);
		FunReturn.numberReturn.contains(FunCall.numberCall);
		FunReturn.whatEverReturn.contains(Expression.numberExpession);
		// FunReturn.whatEverReturn.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		Fun.number.contains(FunReturn.numberReturn);

		Fun.structure.contains(Reference.numberRef);// TODO eigentlich nur
													// hinter Zuweisung
		Fun.structure.contains(Expression.numberExpession);// TODO eigentlich
															// nur hinter
															// Zuweisung
		Fun.noReturn.contains(Expression.numberExpession);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.unknown.contains(Expression.numberExpession);// TODO eigentlich nur
															// hinter Zuweisung
	}

	static void buildStruct() throws AbstractInterpreterException {
		Assign.objAssigment.contains(Reference.objRef);
		Assign.objAssigment.contains(Constant.objConst);
		Assign.objAssigment.contains(FunCall.objCall);

		FunReturn.objReturn.contains(Reference.objRef);
		FunReturn.objReturn.contains(Constant.objConst);
		FunReturn.objReturn.contains(FunCall.objCall);

		Fun.structure.contains(FunReturn.objReturn);
	}

	static void buildUnknown() throws AbstractInterpreterException {
		Assign.whatEverAssigment.contains(Reference.whatEverRef);
		Assign.whatEverAssigment.contains(Constant.whatEverConst);
		Assign.whatEverAssigment.contains(FunCall.whatEverCall);

		Control.unknownLoop.contains(Assign.whatEverAssigment);
		Control.unknownLoop.contains(FunCall.whatEverCall);
		Control.unknownLoop.contains(FunReturn.whatEverReturn);

		Control.unknownIf.contains(Assign.whatEverAssigment);
		Control.unknownIf.contains(FunCall.whatEverCall);
		Control.unknownIf.contains(FunReturn.whatEverReturn);

		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.contains(Reference.whatEverRef);
		FunReturn.whatEverReturn.contains(Constant.whatEverConst);
		FunReturn.whatEverReturn.contains(FunCall.whatEverCall);

		Fun.bool.contains(Assign.whatEverAssigment);
		Fun.bool.contains(FunCall.whatEverCall);
		Fun.bool.contains(Control.unknownLoop);
		Fun.bool.contains(Control.unknownIf);
		Fun.number.contains(Assign.whatEverAssigment);
		Fun.number.contains(FunCall.whatEverCall);
		Fun.number.contains(Control.unknownLoop);
		Fun.number.contains(Control.unknownIf);
		Fun.structure.contains(Assign.whatEverAssigment);
		Fun.structure.contains(FunCall.whatEverCall);
		Fun.structure.contains(Control.unknownLoop);
		Fun.structure.contains(Control.unknownIf);
		Fun.unknown.contains(Assign.whatEverAssigment);
		Fun.unknown.contains(FunCall.whatEverCall);
		Fun.unknown.contains(FunReturn.whatEverReturn);
		Fun.unknown.contains(Control.unknownLoop);
		Fun.unknown.contains(Control.unknownIf);
		Fun.noReturn.contains(Assign.whatEverAssigment);
		Fun.noReturn.contains(FunCall.whatEverCall);
		Fun.noReturn.contains(Control.unknownLoop);
		Fun.noReturn.contains(Control.unknownIf);

	}
		
	public static void build() throws AbstractInterpreterException {

		Expression.numberExpession.means(new String[] { Symbol.PLUS, Symbol.MINUS, Symbol.TIMES, Symbol.DIVIDED_BY, Symbol.REMAINS });
		Expression.boolNullCheck.means(Symbol.ISNULL);
		Expression.boolComparsion.means(new String[] { Symbol.SMALLER, Symbol.GREATER, Symbol.EQUAL });
		Expression.boolExpression.means(new String[] { Symbol.AND, Symbol.OR, Symbol.XOR, Symbol.NOT });
		
		
		String[] params4Reference = new String[]{ CommandParameterSymbols.VALUE };
		Reference.objRef.means(Symbol.REFERENCE);
		Reference.objRef.registerParameters(params4Reference);
		Reference.numberRef.means(Symbol.REFERENCE);
		Reference.numberRef.registerParameters(params4Reference);
		Reference.boolRef.means(Symbol.REFERENCE);
		Reference.boolRef.registerParameters(params4Reference);
		Reference.whatEverRef.means(Symbol.REFERENCE);
		Reference.whatEverRef.registerParameters(params4Reference);
		
		
		
		String[] params4Const = new String[]{ CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE };
		Constant.numberConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG });
		Constant.numberConst.registerParameters(params4Const);
		Constant.boolConst.means(Symbol.BOOL);
		Constant.boolConst.registerParameters(params4Const);
		Constant.objConst.means(Symbol.STRUCT);
		Constant.objConst.registerParameters(params4Const);
		Constant.whatEverConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG, Symbol.BOOL, Symbol.STRUCT });
		Constant.whatEverConst.registerParameters(params4Const);
		
		
		
		Assign.whatEverAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		Assign.objAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		Assign.numberAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		Assign.boolAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		
		
		
		Control.unknownIf.means(Symbol.IF);
		Control.unknownLoop.means(Symbol.LOOP);
		
		
		
		FunReturn.whatEverReturn.means(Symbol.RETURN);
		FunReturn.objReturn.means(Symbol.RETURN);
		FunReturn.numberReturn.means(Symbol.RETURN);
		FunReturn.boolReturn.means(Symbol.RETURN);
		
		
		String[] params4Function = { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE };
		Fun.noReturn.means(Symbol.FUNCTION);
		Fun.noReturn.registerParameters(params4Function);
		Fun.structure.means(Symbol.FUNCTION);
		Fun.structure.registerParameters(params4Function);
		Fun.number.means(Symbol.FUNCTION);
		Fun.number.registerParameters(params4Function);
		Fun.bool.means(Symbol.FUNCTION);
		Fun.bool.registerParameters(params4Function);
		Fun.unknown.means(Symbol.FUNCTION);
		Fun.unknown.registerParameters(params4Function);

		
		String[] params4Call = { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE };
		FunCall.objCall.means(Symbol.CALL);
		FunCall.objCall.registerParameters(params4Call);
		FunCall.numberCall.means(Symbol.CALL);
		FunCall.numberCall.registerParameters(params4Call);
		FunCall.boolCall.means(Symbol.CALL);
		FunCall.boolCall.registerParameters(params4Call);
		FunCall.voidCall.means(Symbol.CALL);
		FunCall.voidCall.registerParameters(params4Call);
		FunCall.whatEverCall.means(Symbol.CALL);
		FunCall.whatEverCall.registerParameters(params4Call);
		
		
		
		Data.complex.means(Symbol.COMPLEX);
		Data.complex.registerParameters(new String[]{ CommandParameterSymbols.NAME });

		
		// Attribute
		Data.attribute.means(Symbol.ATTRIBUTE);	// isAttribute //Symbol.ATTRIBUTE               , 
		Data.attribute.registerParameters(new String[]{ CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE }); 
		
		// TODO this box-object is only a dummy ...
		Data.value.means( Symbol.VALUE );	// isAttributeInitialized //Symbol.VALUE 
		Data.value.registerParameters(new String[]{ CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE, CommandParameterSymbols.TYPE }); 

		Data.valueVirtPrim.means( CommandSymbols.VIRTUAL_PRIM_VALUE );	// isAttributeInitialized //Symbol.VALUE 
		Data.valueVirtPrim.registerParameters(new String[]{ CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }); 
		
		Data.valueVirtComp.means( CommandSymbols.VIRTUAL_COMP_VALUE );	// isAttributeInitialized //Symbol.VALUE 
		Data.valueVirtComp.registerParameters(new String[]{ CommandParameterSymbols.NAME }); 
		
		Data.contract.means( CommandSymbols.CONTRACT );	// isAttributeInitialized //Symbol.VALUE 
		Data.contract.registerParameters(new String[]{ CommandParameterSymbols.NAME }); 		
		/* TODO
		// value
			 VIRTUAL_COMP_VALUE         { CommandParameterSymbols.NAME }, //	CommandSymbols.VIRTUAL_COMP_VALUE
			 VIRTUAL_PRIM_VALUE         { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //	CommandSymbols.VIRTUAL_PRIM_VALUE
 
  		// CONTRACT                   { CommandParameterSymbols.NAME } //CommandSymbols.CONTRACT
		*/
		// TODO this box-object is only a dummy ...
		Control.then.means(Symbol.THEN);
		Control.otherwise.means(Symbol.OTHERWISE);
		
		CodeModel.buildBool();
		CodeModel.buildNumber();
		CodeModel.buildStruct();
		CodeModel.buildUnknown();

		Data.complex.contains(Fun.unknown);
		Data.complex.contains(Data.complex);

	}
	
}
