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
import com.conetex.contract.build.BuildTypes.Types;
import com.conetex.contract.build.BuildValues.Values;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateOperation;
import com.conetex.contract.build.exceptionFunction.OperationMeansNotCalled;
import com.conetex.contract.build.exceptionFunction.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.lang.function.access.Accessible;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.build.BuildFunctions.Expression;
import com.conetex.contract.build.BuildFunctions.Fun;
import com.conetex.contract.build.BuildFunctions.FunCall;
import com.conetex.contract.build.BuildFunctions.FunReturn;
import com.conetex.contract.build.BuildFunctions.Reference;

public class CodeModel{

	public static abstract class Box<T, S> extends Egg<T>{

		private Map<String, Egg<? extends S>> childBuilder = new HashMap<>();

		public Box(String theName) {
			super(theName);
		}

		public Box(String theName, int i) {

			super(theName);
		}

		public final void contains(String theOperationName, Egg<? extends S> b) {
			if(this.childBuilder.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void contains(Egg<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		private final Egg<? extends S> getChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			Egg<? extends S> s = this.childBuilder.get(name);
			if(s == null){
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp) throws AbstractInterpreterException {
			Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.functionCreateThis(child, parentTyp);
		}

		public final Attribute<?> attributeCreateChild(CodeNode child, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.attributeCreateThis(child, unformedComplexTypes);
		}

		public final Value<?> valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.valueCreateThis(child, parentTyp, parentData);
		}

		public final TypeComplex complexCreateChild(CodeNode child, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.complexCreateThis(child, parent, unformedComplexTypes);
		}

	}

	public static abstract class Egg<T> {

		private static Map<String, List<Egg<?>>> instances = new HashMap<>();

		public static List<Egg<?>> getInstance(String command) {
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

		private final void checkMeaning(CodeNode c) throws AbstractInterpreterException {
			if(!this.meaning.contains(c.getCommand())){
				System.err.println("Operation " + c.getCommand() + " not found!");
				throw new UnknownCommand("Operation " + c.getCommand() + " not found!");
			}
		}

		final Attribute<?> attributeCreateThis(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			this.checkMeaning(c);
			return this.attributeCreate(c, unformedComplexTypes);
		}

		public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.functionCreate(n, parentTyp);
		}

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			return null;
		}

		final Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.valueCreate(n, parentTyp, parentData);
		}

		public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			return null;
		}

		final TypeComplex complexCreateThis(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.complexCreate(n, parent, unformedComplexTypes);
		}

		public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		final Set<String> keySet() {
			return this.meaning;
		}

		public final void means(String theOperationName) throws DublicateOperation {
			if(this.meaning.contains(theOperationName)){
				throw new DublicateOperation("duplicate operation '" + theOperationName + "' in " + this.getName());
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
			for(String theOperationName : theOperationNames){
				this.means(theOperationName);
			}
		}

		public final void registerParameters(String[] theParameterNames) {
			if(this.parameterNames != null){
				System.err.println("duplicate Param call");
			}
			this.parameterNames = theParameterNames;
		}

		public final int getParameterIndex(String p) {
			for(int j = 0; j < this.parameterNames.length; j++){
				if(this.parameterNames[j] == p){
					return j;
				}
			}
			return -1;
		}

		public final String[] getParameters() {
			return this.parameterNames;
		}

		public final int getParameterCount() {
			if(this.parameterNames == null){
				return 0;
			}
			return this.parameterNames.length;
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

		Control.loop.contains(Expression.boolExpression);
		Control.loop.contains(Expression.boolComparsion);
		Control.loop.contains(Expression.boolNullCheck);

		Control.when.contains(Expression.boolExpression);
		Control.when.contains(Expression.boolComparsion);
		Control.when.contains(Expression.boolNullCheck);
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
		Fun.whatEver.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.whatEver.contains(Expression.boolComparsion);// TODO eigentlich nur
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

		Control.loop.contains(Expression.numberExpession);
		Control.when.contains(Expression.numberExpession);
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
		Fun.whatEver.contains(Expression.numberExpession);// TODO eigentlich nur
															// hinter Zuweisung
	}

	static void buildStruct() throws AbstractInterpreterException {
		Assign.structureAssigment.contains(Reference.structureRef);
		Assign.structureAssigment.contains(Constant.objConst);
		Assign.structureAssigment.contains(FunCall.structureCall);

		FunReturn.structureReturn.contains(Reference.structureRef);
		FunReturn.structureReturn.contains(Constant.objConst);
		FunReturn.structureReturn.contains(FunCall.structureCall);

		Fun.structure.contains(FunReturn.structureReturn);
	}

	static void buildUnknown() throws AbstractInterpreterException {
		Assign.whatEverAssigment.contains(Reference.whatEverRef);
		Assign.whatEverAssigment.contains(Constant.whatEverConst);
		Assign.whatEverAssigment.contains(FunCall.whatEverCall);

		Control.loop.contains(Assign.whatEverAssigment);
		Control.loop.contains(FunCall.whatEverCall);
		Control.loop.contains(FunReturn.whatEverReturn);

		Control.when.contains(Assign.whatEverAssigment);
		Control.when.contains(FunCall.whatEverCall);
		Control.when.contains(FunReturn.whatEverReturn);

		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.contains(Reference.whatEverRef);
		FunReturn.whatEverReturn.contains(Constant.whatEverConst);
		FunReturn.whatEverReturn.contains(FunCall.whatEverCall);

		Fun.bool.contains(Assign.whatEverAssigment);
		Fun.bool.contains(FunCall.whatEverCall);
		Fun.bool.contains(Control.loop);
		Fun.bool.contains(Control.when);
		Fun.number.contains(Assign.whatEverAssigment);
		Fun.number.contains(FunCall.whatEverCall);
		Fun.number.contains(Control.loop);
		Fun.number.contains(Control.when);
		Fun.structure.contains(Assign.whatEverAssigment);
		Fun.structure.contains(FunCall.whatEverCall);
		Fun.structure.contains(Control.loop);
		Fun.structure.contains(Control.when);
		Fun.whatEver.contains(Assign.whatEverAssigment);
		Fun.whatEver.contains(FunCall.whatEverCall);
		Fun.whatEver.contains(FunReturn.whatEverReturn);
		Fun.whatEver.contains(Control.loop);
		Fun.whatEver.contains(Control.when);

		Fun.whatEver.contains(Types.attribute);
		Fun.whatEver.contains(Values.value);
		Fun.whatEver.contains(Values.valueVirtComp);
		Fun.whatEver.contains(Types.complex);
		Fun.whatEver.contains(Fun.whatEver);

		Fun.noReturn.contains(Assign.whatEverAssigment);
		Fun.noReturn.contains(FunCall.whatEverCall);
		Fun.noReturn.contains(Control.loop);
		Fun.noReturn.contains(Control.when);

	}

	public static void build() throws AbstractInterpreterException {

		Expression.numberExpession
				.means(new String[] { Symbols.comPlus(), Symbols.comMinus(), Symbols.comTimes(), Symbols.comDividedBy(), Symbols.comRemains() });
		Expression.boolNullCheck.means(Symbols.comIsNull());
		Expression.boolComparsion.means(new String[] { Symbols.comSmaller(), Symbols.comGreater(), Symbols.comEqual() });
		Expression.boolExpression.means(new String[] { Symbols.comAnd(), Symbols.comOr(), Symbols.comXOr(), Symbols.comNot() });

		String[] params4Reference = new String[] { Symbols.paramValue() };
		Reference.structureRef.means(Symbols.comReference());
		Reference.structureRef.registerParameters(params4Reference);
		Reference.numberRef.means(Symbols.comReference());
		Reference.numberRef.registerParameters(params4Reference);
		Reference.boolRef.means(Symbols.comReference());
		Reference.boolRef.registerParameters(params4Reference);
		Reference.whatEverRef.means(Symbols.comReference());
		Reference.whatEverRef.registerParameters(params4Reference);

		String[] params4Const = new String[] { Symbols.paramName(), Symbols.paramValue() };
		Constant.numberConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng() });
		Constant.numberConst.registerParameters(params4Const);
		Constant.boolConst.means(Symbols.comBool());
		Constant.boolConst.registerParameters(params4Const);
		Constant.objConst.means(Symbols.comStructure());
		Constant.objConst.registerParameters(params4Const);
		Constant.whatEverConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng(), Symbols.comBool(), Symbols.comStructure() });
		Constant.whatEverConst.registerParameters(params4Const);

		Assign.whatEverAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
		Assign.structureAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
		Assign.numberAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
		Assign.boolAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });

		Control.when.means(Symbols.comWhen());
		Control.loop.means(Symbols.comLoop());

		FunReturn.whatEverReturn.means(Symbols.comReturn());
		FunReturn.structureReturn.means(Symbols.comReturn());
		FunReturn.numberReturn.means(Symbols.comReturn());
		FunReturn.boolReturn.means(Symbols.comReturn());

		String[] params4Function = { Symbols.paramName(), Symbols.paramType() };
		Fun.noReturn.means(Symbols.comFunction());
		Fun.noReturn.registerParameters(params4Function);
		Fun.structure.means(Symbols.comFunction());
		Fun.structure.registerParameters(params4Function);
		Fun.number.means(Symbols.comFunction());
		Fun.number.registerParameters(params4Function);
		Fun.bool.means(Symbols.comFunction());
		Fun.bool.registerParameters(params4Function);
		Fun.whatEver.means(Symbols.comFunction());
		Fun.whatEver.registerParameters(params4Function);

		String[] params4Call = { Symbols.paramName(), Symbols.paramType() };
		FunCall.structureCall.means(Symbols.comCall());
		FunCall.structureCall.registerParameters(params4Call);
		FunCall.numberCall.means(Symbols.comCall());
		FunCall.numberCall.registerParameters(params4Call);
		FunCall.boolCall.means(Symbols.comCall());
		FunCall.boolCall.registerParameters(params4Call);
		FunCall.voidCall.means(Symbols.comCall());
		FunCall.voidCall.registerParameters(params4Call);
		FunCall.whatEverCall.means(Symbols.comCall());
		FunCall.whatEverCall.registerParameters(params4Call);

		Types.complex.means(Symbols.comComplex());
		Types.complex.registerParameters(new String[] { Symbols.paramName() });

		// Attribute
		Types.attribute.means(Symbols.comAttribute()); // isAttribute //Symbol.ATTRIBUTE               , 
		Types.attribute.registerParameters(new String[] { Symbols.paramName(), Symbols.paramType() });

		// TODO this box-object is only a dummy ...
		Values.value.means(Symbols.comValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.value.registerParameters(new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() });

		// TODO this box-object is only a dummy ...
		Values.valueVirtPrim.means(Symbols.comvirtualPrimValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.valueVirtPrim.registerParameters(new String[] { Symbols.paramName(), Symbols.paramValue() });

		// TODO this box-object is only a dummy ...
		Values.valueVirtComp.means(Symbols.comVirtualCompValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.valueVirtComp.registerParameters(new String[] { Symbols.paramName() });

		// TODO this box-object is only a dummy ...
		Values.contract.means(Symbols.comContract()); // isAttributeInitialized //Symbol.VALUE 
		Values.contract.registerParameters(new String[] { Symbols.paramName() });

		// TODO this box-object is only a dummy ...
		Control.then.means(Symbols.comThen());
		// TODO this box-object is only a dummy ...
		Control.otherwise.means(Symbols.comOtherwise());

		CodeModel.buildBool();
		CodeModel.buildNumber();
		CodeModel.buildStruct();
		CodeModel.buildUnknown();

		Types.complex.contains(Types.complex);
		Types.complex.contains(Fun.whatEver);

		Types.complex.contains(Types.attribute);
		Types.complex.contains(Values.value);
		Types.complex.contains(Values.valueVirtComp);

		Types.complex.contains(FunCall.whatEverCall);
		Types.complex.contains(Assign.whatEverAssigment);
		Types.complex.contains(Control.loop);
		Types.complex.contains(Control.when);
		Types.complex.contains(FunReturn.whatEverReturn);
		Types.complex.contains(Expression.numberExpession);// TODO eigentlich nur hinter zuweisung
		Types.complex.contains(Expression.boolComparsion);// TODO eigentlich nur hinter zuweisung
		Types.complex.contains(Expression.boolExpression);// TODO eigentlich nur hinter zuweisung

		Types.complex.contains(Values.valueVirtPrim);

	}

}
