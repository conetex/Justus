package com.conetex.contract.build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.build.CodeModel.Box;
import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.FunctionNotFound;
import com.conetex.contract.build.exceptionLang.MissingSubOperation;
import com.conetex.contract.build.exceptionLang.NoAccessToValue;
import com.conetex.contract.build.exceptionLang.OperationMeansNotCalled;
import com.conetex.contract.build.exceptionLang.TypeNotDeterminated;
import com.conetex.contract.build.exceptionLang.TypesDoNotMatch;
import com.conetex.contract.build.exceptionLang.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;
import com.conetex.contract.build.exceptionLang.UnknownComplexType;
import com.conetex.contract.build.exceptionLang.UnknownType;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.AccessibleConstant;
import com.conetex.contract.lang.access.AccessibleValue;
import com.conetex.contract.lang.access.SetableValue;
import com.conetex.contract.lang.assign.AbstractAssigment;
import com.conetex.contract.lang.assign.Creator;
import com.conetex.contract.lang.bool.expression.Comparsion;
import com.conetex.contract.lang.bool.expression.IsNull;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.Function;
import com.conetex.contract.lang.control.FunctionCall;
import com.conetex.contract.lang.control.If;
import com.conetex.contract.lang.control.IfElse;
import com.conetex.contract.lang.control.Loop;
import com.conetex.contract.lang.control.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class BuildFunctions {



	static class Expression {



		static Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				// MATH
				String name = n.getCommand();
				Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				Accessible<? extends Number> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
				check2PartOperations(n, a, b);
				Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
				return re;
			}
		};

		static Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				// BOOL
				String name = n.getCommand();
				if (name.equals(Symbol.NOT)) {
					Accessible<? extends Boolean> sub = this.createChild(n.getChildElementByIndex(0), parentTyp);
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

		static Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				// COMPARISON
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				Accessible<?> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
				check2PartOperations(n, a, b);
				return Comparsion.createComparison(a, b, n.getCommand());
			}
		};

		static Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				check1PartOperations(n, a);
				return IsNull.create(a);
			}
		};

	}

	static class Reference {

		
		static Egg<Object> whatEverRef = new Egg<Object>("whatEverRef") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefWhatEver(n.getValue(), parentTyp); // parentTyp);
			}
		};

		static Egg<Structure> objRef = new Egg<Structure>("objRef") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(n.getValue(), parentTyp, Structure.class); // parentTyp);
			}
		};

		static Egg<Number> numberRef = new Egg<Number>("numberRef") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefNum(n.getValue(), parentTyp);
			}
		};

		static Egg<Boolean> boolRef = new Egg<Boolean>("boolRef") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(n.getValue(), parentTyp, Boolean.class);
			}
		};
		

	}

	static class Constant {



		static Egg<Structure> objConst = new Egg<Structure>("objConst") {
			@Override
			public Accessible<Structure> create(CodeNode n, Complex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.<Structure>create2(Structure.class, n.getValue());
			}
		};

		static Egg<Object> whatEverConst = new Egg<Object>("whatEverConst") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
					throws TypeNotDeterminated, UnknownType, UnknownCommandParameter, UnknownCommand {
				Accessible<? extends Number> reNu = AccessibleConstant.try2CreateNumConst(n, parentTyp);
				if (reNu != null) {
					return reNu;
				}
				Accessible<Boolean> reBo = AccessibleConstant.try2CreateBoolConst(n, parentTyp);
				if (reBo != null) {
					return reBo;
				}
				Accessible<Structure> reStructure = AccessibleConstant.try2CreateStructureConst(n, parentTyp);
				if (reStructure != null) {
					return reStructure;
				}
				throw new TypeNotDeterminated("const-Type: " + n.getName());
			}
		};

		static Egg<Number> numberConst = new Egg<Number>("numberConst") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
					throws UnknownType, TypeNotDeterminated, UnknownCommandParameter, UnknownCommand {
				Accessible<? extends Number> re = AccessibleConstant.createNumConst(n, parentTyp);
				return re;
			}
		};

		static Egg<Boolean> boolConst = new Egg<Boolean>("boolConst") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
			}
		};

	}

	static class Assign {



		static Box<Structure, Structure> objAssigment = new Box<Structure, Structure>("objAssigment") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<Structure> trg = SetableValue.createFunctionSetable(trgNode.getValue(), parentTyp, Structure.class);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentStructRawType(trgNode.getType(), srcNode, srcRawType);
				return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
			}
		};

		static Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {

			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<? extends Number> trg = SetableValue.createFunctionSetableNumber(trgNode.getValue(), parentTyp);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				Class<?> trgRawType = trg.getRawTypeClass();
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentNumRawType(trgRawType, srcRawType, srcNode);
				return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
			}
		};

		static Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<Boolean> trg = SetableValue.createFunctionSetable(trgNode.getValue(), parentTyp, Boolean.class);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				checkType(src.getRawTypeClass(), Boolean.class);
				return Creator.createFromQualifiedTrg(trg, src, n.getCommand());
			}
		};

		static Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(trgNode.getValue(), parentTyp);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				if (trg.getRawTypeClass() == Structure.class) {
					return createAssigComp(n.getCommand(), srcNode, src, trgNode.getType(), trg);
				}
				else {
					return createAssig(n.getCommand(), srcNode, src, trg);
				}
			}
		};

	}

	static class FunReturn {



		static Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn") {
			@Override
			public Accessible<?> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Structure, Structure> objReturn = new Box<Structure, Structure>("objReturn") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				Accessible<? extends Structure> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

	}

	static class Fun {



		static Box<Object, Object> noReturn = new Box<Object, Object>("voidFunction") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Object.class) {
					Accessible<Object> main = Function.createVoid(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype); // TODO
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

		static Box<Structure, Object> structure = new Box<Structure, Object>("objFunction") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Structure.class) {
					Accessible<? extends Structure> main = Function.createStructure(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		static Box<Number, Object> number = new Box<Number, Object>("numberFunction") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				Accessible<? extends Number> main = Function.createNum(theSteps, n.getName(), returntype);
				return main;
			}
		};

		static Box<Boolean, Object> bool = new Box<Boolean, Object>("boolFunction") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Boolean.class) {
					Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		public static abstract class FunBox extends Box<Object, Object> {

			public FunBox(String name) {
				super(name);
			}

			@Override
			public Function<?> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				return this.createFunction(n, type);
			}

			public abstract Function<?> createFunction(CodeNode n, Complex parentTyp) throws AbstractInterpreterException;

		}

		static FunBox unknown = new FunBox("whatEverFunction") {
			@Override
			public Function<?> createFunction(CodeNode n, Complex type) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(n, type, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == String.class) {
					return null;
				}
				else if (returntype == Boolean.class) {
					Function<? extends Boolean> main = Function.createBool(theSteps, n.getName());
					return main;
				}
				else if (Number.class.isAssignableFrom(returntype)) {
					Function<? extends Number> main = Function.createNum(theSteps, n.getName(), returntype);
					return main;
				}
				else if (returntype == Structure.class) {
					Function<? extends Structure> main = Function.createStructure(theSteps, n.getName());
					return main;
				}
				else if (returntype == Object.class) {
					Function<Object> main = Function.createVoid(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

	}

	static class FunCall {



		static Egg<Object> voidCall = new Egg<Object>("voidCall") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				String functionObj = n.getType(); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(n.getType());
				}
				Function<? extends Object> e = Function.getInstanceVoid(n.getName());
				if (e == null) {
					throw new FunctionNotFound(n.getName());
				}
				return FunctionCall.create(e, re, createAssigs(n, parentTyp));
			}
		};

		static Egg<Object> whatEverCall = new Egg<Object>("whatEverCall") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				String functionObj = n.getType(); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(n.getType());
				}
				Function<? extends Object> e = Function.getInstance(n.getName());
				if (e == null) {
					throw new FunctionNotFound(n.getName());
				}
				return FunctionCall.create(e, re, createAssigs(n, parentTyp));
			}
		};

		static Egg<Structure> objCall = new Egg<Structure>("objCall") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				String functionObj = n.getType(); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(n.getType());
				}
				Function<? extends Structure> e = Function.getInstanceStructure(n.getName());
				if (e == null) {
					throw new FunctionNotFound(n.getName());
				}
				return FunctionCall.create(e, re, createAssigs(n, parentTyp));
			}
		};

		static Egg<Number> numberCall = new Egg<Number>("numberCall") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = n.getType(); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(n.getType());
				}
				Function<? extends Number> e = Function.getInstanceNum(n.getName());
				if (e == null) {
					throw new FunctionNotFound(n.getName());
				}
				return FunctionCall.create(e, re, createAssigs(n, parentTyp));
			}
		};

		static Egg<Boolean> boolCall = new Egg<Boolean>("boolCall") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = n.getType(); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(n.getType());
				}
				Function<Boolean> e = Function.getInstanceBool(n.getName());
				if (e == null) {
					throw new FunctionNotFound(n.getName());
				}
				return FunctionCall.create(e, re, createAssigs(n, parentTyp));
			}
		};

	}

	static class Control {



		static Box<Object, Object> unknownLoop = new Box<Object, Object>("unknownLoop") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex type) throws AbstractInterpreterException {
				// TODO dies ist ne kopie von unknownIf
				int countOfChilds = n.getChildNodesSize();
				if (countOfChilds == 2) {
					Accessible<?> conditionUnTyped = this.createChild(n.getChildElementByIndex(0), type);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(1), type, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					Accessible<? extends Object> main = Loop.create(theStepsIf, condition, returntypeIf);
					return main;
				}
				else {
					System.err.println("argumente passen nicht");
					return null;
				}

			}
		};

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

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					Accessible<? extends Object> main = If.create(theStepsIf, condition, returntypeIf);
					return main;
				}
				else if (countOfChilds == 3) {
					Accessible<?> conditionUnTyped = this.createChild(n.getChildElementByIndex(0), type);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(1), type, this);
					Accessible<?>[] theStepsElse = BuildFunctions.getFunctionSteps(n.getChildElementByIndex(2), type, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);
					Class<?> returntypeElse = Function.getReturnTyp(theStepsElse);

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

	static class Data {



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
					if (c.isType() || c.isFunction()) {
						String cname = c.getName();
						Complex ctype = Complex.getInstance(type.getName() + "." + cname);
						/*
						 * if(ctype == null) { ctype = ComplexFunction.getInstance(type.getName() + "."
						 * + cname); }
						 */
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

	public static Function<?> build(CodeNode n, Complex type) throws AbstractInterpreterException {

		

		Data.complex.create(n, type);
		return Fun.unknown.createFunction(n, type);

	}



	public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box<?, ?> box) throws AbstractInterpreterException {

		String name = n.getCommand();
		if (type == null) {
			System.err.println("can not recognize type of " + name + " " + n.getName());
			return null;
		}
		//System.out.println("createFunction " + name + " " + n.getName());
		List<Accessible<?>> steps = new LinkedList<>();

		List<CodeNode> children = n.getChildNodes();
		for (CodeNode c : children) {
			if (c.isBuildInFunction() && !c.getCommand().equals(Symbol.FUNCTION)) {
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

	static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
		if (a == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 1) {
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
		}
	}

	static void check2PartOperations(CodeNode n, Accessible<?> trg, Accessible<?> src)
			throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
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

	static void checkAssigmentStructRawType(String ComplexTrgTyp, CodeNode srcNode, Class<?> srcRawType)
			throws TypesDoNotMatch, UnknownComplexType, UnknownCommandParameter, UnknownCommand {
		if (srcRawType != Structure.class) {
			throw new TypesDoNotMatch(srcRawType.toString(), Structure.class.toString());
		}

		Complex trgComplexType = Complex.getInstance(ComplexTrgTyp);
		if (trgComplexType == null) {
			throw new UnknownComplexType(ComplexTrgTyp);
		}
		Complex srcComplexType = Complex.getInstance(srcNode.getType());
		if (srcComplexType == null) {
			throw new UnknownComplexType(srcNode.getType() + "(" + srcNode.getCommand() + " - " + srcNode.getName() + ")");
		}
		if (trgComplexType != srcComplexType) {
			throw new TypesDoNotMatch(ComplexTrgTyp, srcNode.getType() + "(" + srcNode.getCommand() + " - " + srcNode.getName() + ")");
		}
	}

	static void checkAssigmentNumRawType(Class<?> trgRawType, Class<?> srcRawType, CodeNode srcNode)
			throws TypesDoNotMatch, UnknownCommandParameter, UnknownCommand {
		Class<?> rawType;
		if (trgRawType == srcRawType) {
			rawType = trgRawType;
		}
		else {
			rawType = ElementaryArithmetic.getBiggest(trgRawType, srcRawType);
			if (rawType == srcRawType) {
				throw new TypesDoNotMatch(trgRawType + " > " + srcRawType + "(" + srcNode.getCommand() + " - " + srcNode.getName() + ")");
			}
		}
	}

	public static void checkType(Class<?> type, Class<?> expected) throws TypesDoNotMatch {
		if (type != expected) {
			throw new TypesDoNotMatch(type.getName() + " is not " + expected.getName());
		}
	}

	public static List<AbstractAssigment<? extends Object>> createAssigs(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {

		// ComplexFunction z = ComplexFunction.getInstance(parentTyp.getName() + "." +
		// n.getName());

		String path2FunctionComplex = n.getType() + "." + n.getName();
		Complex functionComplex = Complex.getInstance(path2FunctionComplex); // z ...

		List<AbstractAssigment<? extends Object>> assig = new LinkedList<>();

		for (CodeNode srcNode : n.getChildNodes()) {
			String path2TrgParam = // n.getName() + "." +
					srcNode.getName();
			SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(path2TrgParam, functionComplex);

			Accessible<?> src = Assign.whatEverAssigment.createChild(srcNode, parentTyp);
			
			if (trg.getRawTypeClass() == Structure.class) {
				Attribute<?> trgAttr = functionComplex.getSubAttribute(srcNode.getName());
				String trgTypeName = trgAttr.getType().getName();				
				assig.add(createAssigComp(Symbol.COPY, srcNode, src, trgTypeName, trg));
			}
			else {
				assig.add(createAssig(Symbol.COPY, srcNode, src, trg));
			}
			
		}

		return assig;
	}

	public static AbstractAssigment<Structure> createAssigComp(String command, CodeNode srcNode, Accessible<?> src, String ComplexTrgNodeTyp,
			SetableValue<?> trg) throws AbstractInterpreterException {
		// CodeNode trgNode = n.getChildElementByIndex(0);
		// SetableValue<?> trg =
		// SetableValue.createFunctionSetableWhatEver(trgNode.getValue(), parentTyp);
		// CodeNode srcNode = n.getChildElementByIndex(1);
		// Accessible<?> src = this.createChild(srcNode, parentTyp);

		Class<?> trgRawType = trg.getRawTypeClass();
		Class<?> srcRawType = src.getRawTypeClass();
		if (trgRawType == Structure.class) {
			checkAssigmentStructRawType(ComplexTrgNodeTyp, srcNode, srcRawType);
			return Creator.createFromUnqualified(trg, src, Structure.class, command);
		}
		else {
			throw new TypeNotDeterminated("target-Type: " + trgRawType + ", source-Type: " + srcRawType);
		}
	}
	
	public static AbstractAssigment<? extends Object> createAssig(String command, CodeNode srcNode, Accessible<?> src,
			SetableValue<?> trg) throws AbstractInterpreterException {
		// CodeNode trgNode = n.getChildElementByIndex(0);
		// SetableValue<?> trg =
		// SetableValue.createFunctionSetableWhatEver(trgNode.getValue(), parentTyp);
		// CodeNode srcNode = n.getChildElementByIndex(1);
		// Accessible<?> src = this.createChild(srcNode, parentTyp);

		Class<?> trgRawType = trg.getRawTypeClass();
		Class<?> srcRawType = src.getRawTypeClass();
		if (trgRawType == Boolean.class) {
			checkType(src.getRawTypeClass(), Boolean.class);
			return Creator.createFromUnqualified(trg, src, Boolean.class, command);
		}
		else if (trgRawType == String.class) {
			checkType(src.getRawTypeClass(), String.class);
			return Creator.createFromUnqualified(trg, src, String.class, command);
		}
		else if (Number.class.isAssignableFrom(trgRawType)) {
			checkAssigmentNumRawType(trgRawType, srcRawType, srcNode);
			return Creator.createFromUnqualified(trg, src, trgRawType, command);
		}
		else {
			throw new TypeNotDeterminated("target-Type: " + trgRawType + ", source-Type: " + srcRawType);
		}
	}

}
