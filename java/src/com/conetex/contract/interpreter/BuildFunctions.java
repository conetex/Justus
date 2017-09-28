package com.conetex.contract.interpreter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.interpreter.exceptionLang.CastException;
import com.conetex.contract.interpreter.exceptionLang.FunctionNotFound;
import com.conetex.contract.interpreter.exceptionLang.MissingSubOperation;
import com.conetex.contract.interpreter.exceptionLang.NoAccessToValue;
import com.conetex.contract.interpreter.exceptionLang.OperationMeansNotCalled;
import com.conetex.contract.interpreter.exceptionLang.TypeNotDeterminated;
import com.conetex.contract.interpreter.exceptionLang.TypesDoNotMatch;
import com.conetex.contract.interpreter.exceptionLang.UnexpectedSubOperation;
import com.conetex.contract.interpreter.exceptionLang.UnknownComplexType;
import com.conetex.contract.interpreter.exceptionLang.UnknownType;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.AccessibleConstant;
import com.conetex.contract.lang.access.AccessibleValue;
import com.conetex.contract.lang.access.SetableValue;
import com.conetex.contract.lang.assign.Creator;
import com.conetex.contract.lang.bool.expression.Comparsion;
import com.conetex.contract.lang.bool.expression.IsNull;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.If;
import com.conetex.contract.lang.control.IfElse;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class BuildFunctions {

	public static abstract class Box<T, S> extends Egg<T> {

		private Map<String, Egg<? extends S>> childBuilder = new HashMap<>();

		public Box(String name) {
			super(name);
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
			System.out.println("createChild " + name + " " + n.getName());
			return s.createThis(n, parentTyp);
		}

		public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException;

	}

	public static abstract class Egg<T> {

		private String		name;

		private Set<String>	meaning	= new HashSet<>();

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

		public final void means(String theOperationName) {
			if (this.meaning.contains(theOperationName)) {
				System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
			}
			this.meaning.add(theOperationName);
		}

		public final void means(String[] theOperationNames) {
			for (String theOperationName : theOperationNames) {
				this.means(theOperationName);
			}
		}

	}

	public static class Expression {

		static void means() {
			Expression.numberExpession.means(new String[] { Symbol.PLUS, Symbol.MINUS, Symbol.TIMES, Symbol.DIVIDED_BY, Symbol.REMAINS });
			Expression.boolNullCheck.means(Symbol.ISNULL);
			Expression.boolComparsion.means(new String[] { Symbol.SMALLER, Symbol.GREATER, Symbol.EQUAL });
			Expression.boolExpression.means(new String[] { Symbol.AND, Symbol.OR, Symbol.XOR, Symbol.NOT });
		}

		static Box<Number, Number>		numberExpession	= new Box<Number, Number>("numberExpession") {
															@Override
															public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
																	throws AbstractInterpreterException {
																// MATH
																String name = n.getCommand();
																Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																Accessible<? extends Number> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
																check2PartOperations(n, a, b);
																Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
																return re;
															}
														};

		static Box<Boolean, Boolean>	boolExpression	= new Box<Boolean, Boolean>("boolExpression") {
															@Override
															public Accessible<Boolean> create(CodeNode n, Complex parentTyp)
																	throws AbstractInterpreterException {
																// BOOL
																String name = n.getCommand();
																if (name.equals(Symbol.NOT)) {
																	Accessible<? extends Boolean> sub = this.createChild(n.getChildElementByIndex(0),
																			parentTyp);
																	check1PartOperations(n, sub);
																	return Not.create(sub);
																}
																else {
																	Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																	Accessible<? extends Boolean> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
																	check2PartOperations(n, a, b);
																	return Binary.create(a, b, name);
																}
															}
														};

		static Box<Boolean, Object>		boolComparsion	= new Box<Boolean, Object>("boolComparsion") {
															@Override
															public Accessible<Boolean> create(CodeNode n, Complex parentTyp)
																	throws AbstractInterpreterException {
																// COMPARISON
																Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																Accessible<?> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
																check2PartOperations(n, a, b);
																return Comparsion.createComparison(a, b, n.getCommand());
															}
														};

		static Box<Boolean, ?>			boolNullCheck	= new Box<Boolean, Object>("boolNullCheck") {
															@Override
															public Accessible<Boolean> create(CodeNode n, Complex parentTyp)
																	throws AbstractInterpreterException {
																Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																check1PartOperations(n, a);
																return IsNull.create(a);
															}
														};

	}

	public static class Reference {

		static void means() {
			Reference.objRef.means(Symbol.REFERENCE);
			Reference.numberRef.means(Symbol.REFERENCE);
			Reference.boolRef.means(Symbol.REFERENCE);
			Reference.whatEverRef.means(Symbol.REFERENCE);
		}

		static Egg<Object>		whatEverRef	= new Egg<Object>("whatEverRef") {
												@Override
												public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
													return AccessibleValue.createFunctionRefWhatEver(n.getValue(), parentTyp);									// parentTyp);
												}
											};

		static Egg<Structure>	objRef		= new Egg<Structure>("objRef") {
												@Override
												public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
														throws AbstractInterpreterException {
													return AccessibleValue.createFunctionRef(n.getValue(), parentTyp, Structure.class);							// parentTyp);
												}
											};

		static Egg<Number>		numberRef	= new Egg<Number>("numberRef") {
												@Override
												public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
													return AccessibleValue.createFunctionRefNum(n.getValue(), parentTyp);
												}
											};

		static Egg<Boolean>		boolRef		= new Egg<Boolean>("boolRef") {
												@Override
												public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
														throws AbstractInterpreterException {
													return AccessibleValue.createFunctionRef(n.getValue(), parentTyp, Boolean.class);
												}
											};
	}

	public static class Constant {

		static void means() {
			Constant.numberConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG });
			Constant.boolConst.means(Symbol.BOOL);
			Constant.objConst.means(Symbol.STRUCT);
			Constant.whatEverConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG, Symbol.BOOL, Symbol.STRUCT });
		}

		static Egg<Structure>	objConst		= new Egg<Structure>("objConst") {
													@Override
													public Accessible<Structure> create(CodeNode n, Complex parentTyp) throws UnknownType {
														return AccessibleConstant.<Structure>create2(Structure.class, n.getValue());
													}
												};

		static Egg<Object>		whatEverConst	= new Egg<Object>("whatEverConst") {
													@Override
													public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws TypeNotDeterminated, UnknownType {
														Accessible<? extends Number> reNu = try2CreateNumConst(n, parentTyp);
														if (reNu != null) {
															return reNu;
														}
														Accessible<Boolean> reBo = try2CreateBoolConst(n, parentTyp);
														if (reBo != null) {
															return reBo;
														}
														Accessible<Structure> reStructure = try2CreateStructureConst(n, parentTyp);
														if (reStructure != null) {
															return reStructure;
														}
														throw new TypeNotDeterminated("const-Type: " + n.getName());
													}
												};

		static Egg<Number>		numberConst		= new Egg<Number>("numberConst") {
													@Override
													public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws UnknownType, TypeNotDeterminated {
														Accessible<? extends Number> re = createNumConst(n, parentTyp);
														return re;
													}
												};

		static Egg<Boolean>		boolConst		= new Egg<Boolean>("boolConst") {
													@Override
													public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws UnknownType {
														return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
													}
												};

	}

	public static class Assign {

		static void means() {
			Assign.whatEverAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.objAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.numberAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.boolAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		}

		static Box<Structure, Structure>	objAssigment		= new Box<Structure, Structure>("objAssigment") {
																	@Override
																	public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
																			throws AbstractInterpreterException {
																		CodeNode trgNode = n.getChildElementByIndex(0);
																		SetableValue<Structure> trg = SetableValue.createFunctionSetable(trgNode.getValue(),
																				parentTyp, Structure.class);
																		CodeNode srcNode = n.getChildElementByIndex(1);
																		Accessible<?> src = this.createChild(srcNode, parentTyp);
																		check2PartOperations(n, trg, src);
																		Class<?> srcRawType = src.getRawTypeClass();
																		checkAssigmentStructRawType(trgNode, srcNode, srcRawType);
																		return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
																	}
																};

		static Box<Number, Number>			numberAssigment		= new Box<Number, Number>("numberAssigment") {

																	@Override
																	public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
																			throws AbstractInterpreterException {
																		CodeNode trgNode = n.getChildElementByIndex(0);
																		SetableValue<? extends Number> trg = SetableValue
																				.createFunctionSetableNumber(trgNode.getValue(), parentTyp);
																		CodeNode srcNode = n.getChildElementByIndex(1);
																		Accessible<?> src = this.createChild(srcNode, parentTyp);
																		check2PartOperations(n, trg, src);
																		Class<?> trgRawType = trg.getRawTypeClass();
																		Class<?> srcRawType = src.getRawTypeClass();
																		checkAssigmentNumRawType(trgRawType, srcRawType, trgNode, srcNode);
																		return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
																	}
																};

		static Box<Boolean, Boolean>		boolAssigment		= new Box<Boolean, Boolean>("boolAssigment") {
																	@Override
																	public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
																			throws AbstractInterpreterException {
																		CodeNode trgNode = n.getChildElementByIndex(0);
																		SetableValue<Boolean> trg = SetableValue.createFunctionSetable(trgNode.getValue(),
																				parentTyp, Boolean.class);
																		CodeNode srcNode = n.getChildElementByIndex(1);
																		Accessible<?> src = this.createChild(srcNode, parentTyp);
																		check2PartOperations(n, trg, src);
																		checkType(src.getRawTypeClass(), Boolean.class);
																		return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
																	}
																};

		static Box<Object, Object>			whatEverAssigment	= new Box<Object, Object>("whatEverAssigment") {
																	@Override
																	public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
																			throws AbstractInterpreterException {
																		CodeNode trgNode = n.getChildElementByIndex(0);
																		SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(trgNode.getValue(),
																				parentTyp);
																		CodeNode srcNode = n.getChildElementByIndex(1);
																		Accessible<?> src = this.createChild(srcNode, parentTyp);

																		check2PartOperations(n, trg, src);

																		Class<?> trgRawType = trg.getRawTypeClass();
																		Class<?> srcRawType = src.getRawTypeClass();
																		if (trgRawType == Boolean.class) {
																			checkType(src.getRawTypeClass(), Boolean.class);
																			return Creator.createFromUnqualified(trg, src, Boolean.class, n.getCommand());
																		}
																		else if (trgRawType == Structure.class) {
																			checkAssigmentStructRawType(trgNode, srcNode, srcRawType);
																			return Creator.createFromUnqualified(trg, src, Structure.class, n.getCommand());
																		}
																		else if (trgRawType == String.class) {
																			checkType(src.getRawTypeClass(), String.class);
																			return Creator.createFromUnqualified(trg, src, String.class, n.getCommand());
																		}
																		else if (Number.class.isAssignableFrom(trgRawType)) {
																			checkAssigmentNumRawType(trgRawType, srcRawType, trgNode, srcNode);
																			return Creator.createFromUnqualified(trg, src, trgRawType, n.getCommand());
																		}
																		else {
																			throw new TypeNotDeterminated(
																					"target-Type: " + trgRawType + ", source-Type: " + srcRawType);
																		}
																	}
																};

	}

	public static class FunReturn {

		static void means() {
			FunReturn.whatEverReturn.means(Symbol.RETURN);
			FunReturn.objReturn.means(Symbol.RETURN);
			FunReturn.numberReturn.means(Symbol.RETURN);
			FunReturn.boolReturn.means(Symbol.RETURN);

		}

		static Box<Object, Object>			whatEverReturn	= new Box<Object, Object>("whatEverReturn") {
																@Override
																public Accessible<?> create(CodeNode n, Complex parentTyp)
																		throws AbstractInterpreterException {
																	Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																	return Return.create(a);
																}
															};

		static Box<Structure, Structure>	objReturn		= new Box<Structure, Structure>("objReturn") {
																@Override
																public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
																		throws AbstractInterpreterException {
																	Accessible<? extends Structure> a = this.createChild(n.getChildElementByIndex(0),
																			parentTyp);
																	return Return.create(a);
																}
															};

		static Box<Number, Number>			numberReturn	= new Box<Number, Number>("numberReturn") {
																@Override
																public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
																		throws AbstractInterpreterException {
																	Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																	return Return.create(a);
																}
															};

		static Box<Boolean, Boolean>		boolReturn		= new Box<Boolean, Boolean>("boolReturn") {
																@Override
																public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
																		throws AbstractInterpreterException {
																	Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
																	return Return.create(a);
																}
															};

	}

	private static class Fun {

		static void means() {
			Fun.noReturn.means(Symbol.FUNCTION);
			Fun.structure.means(Symbol.FUNCTION);
			Fun.number.means(Symbol.FUNCTION);
			Fun.bool.means(Symbol.FUNCTION);
			Fun.unknown.means(Symbol.FUNCTION);
		}

		static Box<Object, Object>		noReturn	= new Box<Object, Object>("voidFunction") {
														@Override
														public Accessible<? extends Object> create(CodeNode n, Complex type)
																throws AbstractInterpreterException {
															Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
															Class<?> returntype = getReturnTyp(theSteps);
															if (returntype == Object.class) {
																Accessible<Object> main = Function.createVoid(theSteps, n.getName());
																return main;
															}
															System.err.println("unknown return type " + returntype);										// TODO
																																							// suche
																																							// alle
																																							// System.err
																																							// und
																																							// werfe
																																							// lieber
																																							// exception
															return null;
														}
													};

		static Box<Structure, Object>	structure	= new Box<Structure, Object>("objFunction") {
														@Override
														public Accessible<? extends Structure> create(CodeNode n, Complex type)
																throws AbstractInterpreterException {
															Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
															Class<?> returntype = getReturnTyp(theSteps);
															if (returntype == Structure.class) {
																Accessible<? extends Structure> main = Function.createStructure(theSteps, n.getName());
																return main;
															}
															System.err.println("unknown return type " + returntype);
															return null;
														}
													};

		static Box<Number, Object>		number		= new Box<Number, Object>("numberFunction") {
														@Override
														public Accessible<? extends Number> create(CodeNode n, Complex type)
																throws AbstractInterpreterException {
															Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
															Class<?> returntype = getReturnTyp(theSteps);
															Accessible<? extends Number> main = Function.createNum(theSteps, n.getName(), returntype);
															return main;
														}
													};

		static Box<Boolean, Object>		bool		= new Box<Boolean, Object>("boolFunction") {
														@Override
														public Accessible<? extends Boolean> create(CodeNode n, Complex type)
																throws AbstractInterpreterException {
															Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
															Class<?> returntype = getReturnTyp(theSteps);
															if (returntype == Boolean.class) {
																Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
																return main;
															}
															System.err.println("unknown return type " + returntype);
															return null;
														}
													};

		static Box<Object, Object>		unknown		= new Box<Object, Object>("whatEverFunction") {
														@Override
														public Accessible<?> create(CodeNode n, Complex type) throws AbstractInterpreterException {
															Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
															Class<?> returntype = getReturnTyp(theSteps);
															if (returntype == String.class) {
																return null;
															}
															else if (returntype == Boolean.class) {
																Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
																return main;
															}
															else if (Number.class.isAssignableFrom(returntype)) {
																Accessible<? extends Number> main = Function.createNum(theSteps, n.getName(), returntype);
																return main;
															}
															else if (returntype == Structure.class) {
																Accessible<? extends Structure> main = Function.createStructure(theSteps, n.getName());
																return main;
															}
															else if (returntype == Object.class) {
																Accessible<Object> main = Function.createVoid(theSteps, n.getName());
																return main;
															}
															System.err.println("unknown return type " + returntype);
															return null;
														}
													};

	}

	private static class FunCall {

		static void means() {
			FunCall.objCall.means(Symbol.CALL);
			FunCall.numberCall.means(Symbol.CALL);
			FunCall.boolCall.means(Symbol.CALL);
			FunCall.voidCall.means(Symbol.CALL);
			FunCall.whatEverCall.means(Symbol.CALL);
		}

		static Egg<Object>		voidCall		= new Egg<Object>("voidCall") {
													@Override
													public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
															throws FunctionNotFound, NoAccessToValue {
														String functionObj = n.getType();														//
														AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
														if (re == null) {
															throw new NoAccessToValue(n.getType());
														}
														Accessible<? extends Object> e = Function.getInstanceVoid(n.getName());
														if (e == null) {
															throw new FunctionNotFound(n.getName());
														}
														return Call.create(e, re);
													}
												};

		static Egg<Object>		whatEverCall	= new Egg<Object>("whatEverCall") {
													@Override
													public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
															throws FunctionNotFound, NoAccessToValue {
														String functionObj = n.getType();														//
														AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
														if (re == null) {
															throw new NoAccessToValue(n.getType());
														}
														Accessible<? extends Object> e = Function.getInstance(n.getName());
														if (e == null) {
															throw new FunctionNotFound(n.getName());
														}
														return Call.create(e, re);
													}
												};

		static Egg<Structure>	objCall			= new Egg<Structure>("objCall") {
													@Override
													public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
															throws FunctionNotFound, NoAccessToValue {
														String functionObj = n.getType();														//
														AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
														if (re == null) {
															throw new NoAccessToValue(n.getType());
														}
														Accessible<? extends Structure> e = Function.getInstanceStructure(n.getName());
														if (e == null) {
															throw new FunctionNotFound(n.getName());
														}
														return Call.create(e, re);
													}
												};

		static Egg<Number>		numberCall		= new Egg<Number>("numberCall") {
													@Override
													public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
															throws FunctionNotFound, NoAccessToValue {
														// CONTROL FUNCTION
														String functionObj = n.getType();														//
														AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
														if (re == null) {
															throw new NoAccessToValue(n.getType());
														}
														Accessible<? extends Number> e = Function.getInstanceNum(n.getName());
														if (e == null) {
															throw new FunctionNotFound(n.getName());
														}
														return Call.create(e, re);
													}
												};

		static Egg<Boolean>		boolCall		= new Egg<Boolean>("boolCall") {
													@Override
													public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
															throws FunctionNotFound, NoAccessToValue {
														// CONTROL FUNCTION
														String functionObj = n.getType();														//
														AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
														if (re == null) {
															throw new NoAccessToValue(n.getType());
														}
														Accessible<Boolean> e = Function.getInstanceBool(n.getName());
														if (e == null) {
															throw new FunctionNotFound(n.getName());
														}
														return Call.create(e, re);
													}
												};

	}

	private static class Control {

		static void means() {
			Control.unknownIf.means(Symbol.IF);
		}

		static Box<Object, Object> unknownIf = new Box<Object, Object>("unknownIf") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				return createIntern(n, type);
			}

			private Accessible<? extends Object> createIntern(CodeNode n, Complex type) throws AbstractInterpreterException {

				int countOfChilds = n.getChildNodesSize();
				if (countOfChilds == 2) {
					Accessible<?> conditionUnTyped = this.createChild(n.getChildElementByIndex(0), type);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(1), type, this);

					Class<?> returntypeIf = getReturnTyp(theStepsIf);

					Accessible<? extends Object> main = If.create(theStepsIf, condition, returntypeIf);
					return main;
				}
				else if (countOfChilds == 3) {
					Accessible<?> conditionUnTyped = this.createChild(n.getChildElementByIndex(0), type);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(1), type, this);
					Accessible<?>[] theStepsElse = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(2), type, this);

					Class<?> returntypeIf = getReturnTyp(theStepsIf);
					Class<?> returntypeElse = getReturnTyp(theStepsElse);

					if (returntypeIf == Object.class) {
						if (returntypeElse == Object.class) {
							Accessible<Object> main = IfElse.create(theStepsIf, theStepsElse, condition, Object.class);
							return main;
						}
						else {
							Accessible<? extends Object> main = IfElse.create(theStepsIf, theStepsElse, condition, returntypeElse);
							return main;
						}
					}
					else {
						if (returntypeElse == Object.class) {
							Accessible<? extends Object> main = IfElse.create(theStepsIf, theStepsElse, condition, returntypeIf);
							return main;
						}
						else {
							if (returntypeIf != returntypeElse) {
								if (Number.class.isAssignableFrom(returntypeIf)) {
									Class<?> returntype = ElementaryArithmetic.getBiggest(returntypeIf, returntypeElse);
									Accessible<? extends Object> main = IfElse.create(theStepsIf, theStepsElse, condition, returntype);
									return main;
								}
								else {
									System.err.println("typen passen nich...");
									return null;
								}
							}
							else {
								Accessible<? extends Object> main = IfElse.create(theStepsIf, theStepsElse, condition, returntypeIf);
								return main;
							}
						}
					}
				}
				else {
					System.err.println("argumente passen nicht");
					return null;
				}

			}
		};

	}

	public static class Data {

		static void means() {
			Data.complex.means("complexType");
		}

		static Box<Structure, Object> complex = new Box<Structure, Object>("complex") {
			@Override
			public Accessible<Structure> create(CodeNode n, Complex type) throws AbstractInterpreterException {

				String name = n.getCommand();
				if (type == null) {
					System.err.println("can not recognize type of " + name);
					return null;
				}

				List<CodeNode> children = n.getChildNodes();
				for (CodeNode c : children) {
					if (c.isType()) {
						String cname = c.getName();
						Complex ctype = Complex.getInstance(type.getName() + "." + cname);
						if (ctype == null) {
							System.err.println("createFunctions: can not identify " + type.getName() + "." + cname);
						}
						else {
							// createFunctions(c, ctype);
							this.createChild(c, ctype);
						}
					}
				}
				return null;
			}
		};

	}

	static Class<?> getReturnTyp(Accessible<?>[] theSteps) {
		Class<?> pre = null;
		for (Accessible<?> a : theSteps) {
			if (a instanceof Return) {
				Class<?> c = a.getRawTypeClass();
				if (pre != null) {
					if (pre != c) {
						if (Number.class.isAssignableFrom(c)) {
							pre = ElementaryArithmetic.getBiggest(pre, c);
						}
						else {
							System.err.println("typen passen nich...");
						}
					} // else ok
				}
				pre = c;
			}
		}
		if (pre == null) {
			return Object.class;
		}
		return pre;
	}

	public static <X> List<Return<X>> getReturns(Accessible<?>[] theSteps, Class<X> rawType) throws CastException {
		List<Return<X>> returns = new LinkedList<Return<X>>();
		for (Accessible<?> a : theSteps) {
			if (a instanceof Return) {
				Class<?> c = a.getRawTypeClass();
				if (rawType.isAssignableFrom(c)) {
					Return<X> re = Cast.toTypedReturn(a, rawType);
					returns.add(re);
				}
				else {
					System.err.println("typen passen nich...");
				}
			}
		}
		return returns;
	}

	public static Accessible<?> build(CodeNode n, Complex type) throws AbstractInterpreterException {

		Expression.means();
		Reference.means();
		Constant.means();
		Assign.means();
		Control.means();
		FunReturn.means();
		Fun.means();
		FunCall.means();
		Data.means();

		BuildFunctions.buildBool(n, type);
		BuildFunctions.buildNumber(n, type);
		BuildFunctions.buildStruct(n, type);
		BuildFunctions.buildUnknown(n, type);

		Data.complex.contains(Fun.unknown);
		Data.complex.contains(Data.complex);

		Data.complex.create(n, type);
		return Fun.unknown.create(n, type);

	}

	private static void buildBool(CodeNode n, Complex type) throws AbstractInterpreterException {
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

	private static void buildNumber(CodeNode n, Complex type) throws AbstractInterpreterException {
		Expression.boolComparsion.contains(Expression.numberExpession);
		Expression.boolComparsion.contains(Reference.numberRef);
		Expression.boolComparsion.contains(Constant.numberConst);
		Expression.boolComparsion.contains(FunCall.numberCall);
		Expression.numberExpession.contains(Expression.numberExpession);
		Expression.numberExpession.contains(Reference.numberRef);
		Expression.numberExpession.contains(Constant.numberConst);
		Expression.numberExpession.contains(FunCall.numberCall);

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

	private static void buildStruct(CodeNode n, Complex type) throws AbstractInterpreterException {
		Assign.objAssigment.contains(Reference.objRef);
		Assign.objAssigment.contains(Constant.objConst);
		Assign.objAssigment.contains(FunCall.objCall);

		FunReturn.objReturn.contains(Reference.objRef);
		FunReturn.objReturn.contains(Constant.objConst);
		FunReturn.objReturn.contains(FunCall.objCall);

		Fun.structure.contains(FunReturn.objReturn);
	}

	private static void buildUnknown(CodeNode n, Complex type) throws AbstractInterpreterException {
		Assign.whatEverAssigment.contains(Reference.whatEverRef);
		Assign.whatEverAssigment.contains(Constant.whatEverConst);
		Assign.whatEverAssigment.contains(FunCall.whatEverCall);

		Control.unknownIf.contains(Assign.whatEverAssigment);
		Control.unknownIf.contains(FunCall.whatEverCall);
		Control.unknownIf.contains(FunReturn.whatEverReturn);

		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.contains(Reference.whatEverRef);
		FunReturn.whatEverReturn.contains(Constant.whatEverConst);
		FunReturn.whatEverReturn.contains(FunCall.whatEverCall);

		Fun.bool.contains(Assign.whatEverAssigment);
		Fun.bool.contains(FunCall.whatEverCall);
		Fun.bool.contains(Control.unknownIf);
		Fun.number.contains(Assign.whatEverAssigment);
		Fun.number.contains(FunCall.whatEverCall);
		Fun.number.contains(Control.unknownIf);
		Fun.structure.contains(Assign.whatEverAssigment);
		Fun.structure.contains(FunCall.whatEverCall);
		Fun.structure.contains(Control.unknownIf);
		Fun.unknown.contains(Assign.whatEverAssigment);
		Fun.unknown.contains(FunCall.whatEverCall);
		Fun.unknown.contains(FunReturn.whatEverReturn);
		Fun.unknown.contains(Control.unknownIf);
		Fun.noReturn.contains(Assign.whatEverAssigment);
		Fun.noReturn.contains(FunCall.whatEverCall);
		Fun.noReturn.contains(Control.unknownIf);

	}

	public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box<?, ?> box) throws AbstractInterpreterException {

		String name = n.getCommand();
		if (type == null) {
			System.err.println("can not recognize type of " + name + " " + n.getName());
			return null;
		}
		System.out.println("createFunction " + name + " " + n.getName());
		List<Accessible<?>> steps = new LinkedList<>();

		List<CodeNode> children = n.getChildNodes();
		for (CodeNode c : children) {
			if (c.isBuildInFunction() && !c.getCommand().equals(Symbol.FUNCTION)) {
				System.out.println("createBuild " + c.getCommand() + " - " + c.getName());
				// Accessible<?> v = createFunction(c, type);
				Accessible<?> v = box.createChild(c, type);
				if (v != null) {
					steps.add(v);
				}
			}
		}

		// List<Accessible<?>> steps = Functions.createFunctions(n, parentTyp);
		Accessible<?>[] theSteps = new Accessible<?>[steps.size()];
		return steps.toArray(theSteps);
	}
	
	static Accessible<? extends Number> createNumConst(CodeNode n, Complex parentTyp) throws UnknownType, TypeNotDeterminated {
		Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
		if(re == null){
			throw new TypeNotDeterminated("number const-Type: " + n.getName());			
		}
		return re;
	}
	
	static Accessible<? extends Number> try2CreateNumConst(CodeNode n, Complex parentTyp) throws UnknownType {
		String name = n.getCommand();
		if (name.equals(Symbol.BINT)) {
			return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
		}
		else if (name.equals(Symbol.INT)) {
			return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
		}
		else if (name.equals(Symbol.LNG)) {
			return AccessibleConstant.<Long>create2(Long.class, n.getValue());
		}
		return null;
	}

	static Accessible<Boolean> try2CreateBoolConst(CodeNode n, Complex parentTyp) throws UnknownType {
		String name = n.getCommand();
		if (name.equals(Symbol.BOOL)) {
			return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
		}
		return null;
	}

	static Accessible<Structure> try2CreateStructureConst(CodeNode n, Complex parentTyp) throws UnknownType {
		String name = n.getCommand();
		if (name.equals(Symbol.STRUCT)) {
			return AccessibleConstant.<Structure>create2(Structure.class, n.getValue());
		}
		return null;
	}

	static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation {
		if (a == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 1) {
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
		}
	}

	static void check2PartOperations(CodeNode n, Accessible<?> trg, Accessible<?> src) throws MissingSubOperation, UnexpectedSubOperation {
		if (trg == null) {
			throw new MissingSubOperation("");
		}
		if (src == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 2) {
			CodeNode c = n.getChildElementByIndex(2);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
		}
	}

	static void checkAssigmentStructRawType(CodeNode trgNode, CodeNode srcNode, Class<?> srcRawType) throws TypesDoNotMatch, UnknownComplexType {
		if (srcRawType != Structure.class) {
			throw new TypesDoNotMatch(srcRawType.toString(), Structure.class.toString());
		}

		Complex trgComplexType = Complex.getInstance(trgNode.getType());
		if (trgComplexType == null) {
			throw new UnknownComplexType(trgNode.getType() + "(" + trgNode.getCommand() + " - " + trgNode.getName() + ")");
		}
		Complex srcComplexType = Complex.getInstance(srcNode.getType());
		if (srcComplexType == null) {
			throw new UnknownComplexType(srcNode.getType() + "(" + srcNode.getCommand() + " - " + srcNode.getName() + ")");
		}
		if (trgComplexType != srcComplexType) {
			throw new TypesDoNotMatch(trgNode.getType() + "(" + trgNode.getCommand() + " - " + trgNode.getName() + ")",
					srcNode.getType() + "(" + srcNode.getCommand() + " - " + srcNode.getName() + ")");
		}
	}

	static void checkAssigmentNumRawType(Class<?> trgRawType, Class<?> srcRawType, CodeNode trgNode, CodeNode srcNode) throws TypesDoNotMatch {
		Class<?> rawType;
		if (trgRawType == srcRawType) {
			rawType = trgRawType;
		}
		else {
			rawType = ElementaryArithmetic.getBiggest(trgRawType, srcRawType);
			if (rawType == srcRawType) {
				throw new TypesDoNotMatch(trgRawType + "(" + trgNode.getCommand() + " - " + trgNode.getName() + ") > " + srcRawType + "(" + srcNode.getCommand()
						+ " - " + srcNode.getName() + ")");
			}
		}
	}

	public static void checkType(Class<?> type, Class<?> expected) throws TypesDoNotMatch {
		if (type != expected) {
			throw new TypesDoNotMatch(type.getName() + " is not " + expected.getName());
		}
	}

}
