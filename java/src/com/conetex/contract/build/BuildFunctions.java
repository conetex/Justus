package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.CodeModel.Box;
import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.FunctionNotFound;
import com.conetex.contract.build.exceptionLang.MissingSubOperation;
import com.conetex.contract.build.exceptionLang.NoAccessToValue;
import com.conetex.contract.build.exceptionLang.TypeNotDeterminated;
import com.conetex.contract.build.exceptionLang.TypesDoNotMatch;
import com.conetex.contract.build.exceptionLang.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;
import com.conetex.contract.build.exceptionLang.UnknownComplexType;
import com.conetex.contract.build.exceptionLang.UnknownType;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.FunctionAttributes;
import com.conetex.contract.data.type.Primitive;
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
import com.conetex.contract.lang.control.Loop;
import com.conetex.contract.lang.control.Return;
import com.conetex.contract.lang.control.When;
import com.conetex.contract.lang.control.WhenOtherwise;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class BuildFunctions {

	static class Expression {

		static Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// MATH
				String name = thisNode.getCommand();
				Accessible<? extends Number> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				Accessible<? extends Number> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
				check2PartOperations(thisNode, a, b);
				Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
				return re;
			}
		};

		static Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// BOOL
				String name = thisNode.getCommand();
				if (name.equals(Symbols.comNot())) {
					Accessible<? extends Boolean> sub = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					check1PartOperations(thisNode, sub);
					return Not.create(sub);
				}
				else {
					Accessible<? extends Boolean> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<? extends Boolean> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
					check2PartOperations(thisNode, a, b);
					return Binary.create(a, b, name);
				}
			}
		};

		static Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion") {
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// COMPARISON
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				Accessible<?> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
				check2PartOperations(thisNode, a, b);
				return Comparsion.createComparison(a, b, thisNode.getCommand());
			}
		};

		static Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				check1PartOperations(thisNode, a);
				return IsNull.create(a);
			}
		};

	}

	static class Reference {
		
		static Egg<Object> whatEverRef = new Egg<Object>("whatEverRef") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefWhatEver(thisNode.getParameter(Symbols.paramValue()), parentType); // parentTyp);
			}
		};

		static Egg<Structure> structureRef = new Egg<Structure>("objRef") {
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()), parentType, Structure.class); // parentTyp);
			}
		};

		static Egg<Number> numberRef = new Egg<Number>("numberRef") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefNum(thisNode.getParameter(Symbols.paramValue()), parentType);
			}
		};

		static Egg<Boolean> boolRef = new Egg<Boolean>("boolRef") {
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()), parentType, Boolean.class);
			}
		};
		

	}

	static class Constant {

		static Egg<Structure> objConst = new Egg<Structure>("objConst") {
			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, Complex parentType) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.<Structure>create2(Structure.class, thisNode.getParameter(Symbols.paramValue()));
			}
		};

		static Egg<Object> whatEverConst = new Egg<Object>("whatEverConst") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType)
					throws TypeNotDeterminated, UnknownType, UnknownCommandParameter, UnknownCommand {
				Accessible<? extends Number> reNu = AccessibleConstant.try2CreateNumConst(thisNode, parentType);
				if (reNu != null) {
					return reNu;
				}
				Accessible<Boolean> reBo = AccessibleConstant.try2CreateBoolConst(thisNode, parentType);
				if (reBo != null) {
					return reBo;
				}
				Accessible<Structure> reStructure = AccessibleConstant.try2CreateStructureConst(thisNode, parentType);
				if (reStructure != null) {
					return reStructure;
				}
				throw new TypeNotDeterminated("const-Type: " + thisNode.getParameter(Symbols.paramName()));
			}
		};

		static Egg<Number> numberConst = new Egg<Number>("numberConst") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType)
					throws UnknownType, TypeNotDeterminated, UnknownCommandParameter, UnknownCommand {
				Accessible<? extends Number> re = AccessibleConstant.createNumConst(thisNode, parentType);
				return re;
			}
		};

		static Egg<Boolean> boolConst = new Egg<Boolean>("boolConst") {
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.<Boolean>create2(Boolean.class, thisNode.getParameter(Symbols.paramValue()));
			}
		};

	}

	static class Assign {

		static Box<Structure, Structure> structureAssigment = new Box<Structure, Structure>("objAssigment") {
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<Structure> trg = SetableValue.createFunctionSetable(trgNode.getParameter(Symbols.paramValue()), parentType, Structure.class);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentStructRawType(trgNode.getParameter(Symbols.paramType()), srcNode, srcRawType);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {

			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<? extends Number> trg = SetableValue.createFunctionSetableNumber(trgNode.getParameter(Symbols.paramValue()), parentType);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				Class<?> trgRawType = trg.getRawTypeClass();
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentNumRawType(trgRawType, srcRawType, srcNode);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<Boolean> trg = SetableValue.createFunctionSetable(trgNode.getParameter(Symbols.paramValue()), parentType, Boolean.class);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				checkType(src.getRawTypeClass(), Boolean.class);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(trgNode.getParameter(Symbols.paramValue()), parentType);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				if (trg.getRawTypeClass() == Structure.class) {
					return createAssigComp(thisNode.getCommand(), srcNode, src, trgNode.getParameter(Symbols.paramType()), trg);
				}
				else {
					return createAssig(thisNode.getCommand(), srcNode, src, trg);
				}
			}
		};

	}

	static class FunReturn {

		static Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn") {
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static Box<Structure, Structure> structureReturn = new Box<Structure, Structure>("objReturn") {
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<? extends Structure> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<? extends Number> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<? extends Boolean> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

	}

	static class Fun {

		static Box<Object, Object> noReturn = new Box<Object, Object>("voidFunction") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Object.class) {
					Accessible<Object> main = Function.createVoid(theSteps, thisNode.getParameter(Symbols.paramName()));
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
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Structure.class) {
					Accessible<? extends Structure> main = Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		static Box<Number, Object> number = new Box<Number, Object>("numberFunction") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				Accessible<? extends Number> main = Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
				return main;
			}
		};

		static Box<Boolean, Object> bool = new Box<Boolean, Object>("boolFunction") {
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == Boolean.class) {
					Accessible<? extends Boolean> main = Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
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
			public Function<?> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				
				Complex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);
				
				return this.functionCreateImpl(thisNode, thisType);
			}

			public abstract Function<?> functionCreateImpl(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException;

		}

		static FunBox whatEver = new FunBox("whatEverFunction") {
			@Override
			public Function<?> functionCreateImpl(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, thisType, this);
				if(theSteps == null){
					System.err.println("no steps ");
				}
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if (returntype == String.class) {
					return null;
				}
				else if (returntype == Boolean.class) {
					Function<? extends Boolean> main = Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
					return main;
				}
				else if (Number.class.isAssignableFrom(returntype)) {
					Function<? extends Number> main = Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
					return main;
				}
				else if (returntype == Structure.class) {
					Function<? extends Structure> main = Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
					return main;
				}
				else if (returntype == Object.class) {
					Function<Object> main = Function.createVoid(theSteps, thisNode.getParameter(Symbols.paramName()));
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
			
			public Attribute<?> attributeCreate(CodeNode c, Map<String, Complex> unformedComplexTypes) throws AbstractInterpreterException{
				String idTypeName = null;
				String idName = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				// referringComplexTypeNames.add(typeName + "." + idTypeName);//
				// TODO BUG !!!
				// Attribute<?> fun =
				FunctionAttributes.createAttribute(idName, idTypeName, unformedComplexTypes);
				return null;
			}
			
			public Value<?> valueCreate(CodeNode thisNode, Complex parentType, Structure parentData) throws AbstractInterpreterException{
				System.out.println("createValues " + thisNode.getCommand());
				Value<?> v = BuildValues.createValue4Function(thisNode, parentType, parentData);
				return v;
			}
			
			public Complex complexCreate(CodeNode n, Complex parent, Map<String, Complex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}
			
		};

	}

	static class FunCall {

		static Egg<Object> voidCall = new Egg<Object>("voidCall") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Object> e = Function.getInstanceVoid(thisNode.getParameter(Symbols.paramName()));
				if (e == null) {
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static Egg<Object> whatEverCall = new Egg<Object>("whatEverCall") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Object> e = Function.getInstance(thisNode.getParameter(Symbols.paramName()));
				if (e == null) {
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static Egg<Structure> structureCall = new Egg<Structure>("objCall") {
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Structure> e = Function.getInstanceStructure(thisNode.getParameter(Symbols.paramName()));
				if (e == null) {
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static Egg<Number> numberCall = new Egg<Number>("numberCall") {
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Number> e = Function.getInstanceNum(thisNode.getParameter(Symbols.paramName()));
				if (e == null) {
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static Egg<Boolean> boolCall = new Egg<Boolean>("boolCall") {
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if (re == null) {
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<Boolean> e = Function.getInstanceBool(thisNode.getParameter(Symbols.paramName()));
				if (e == null) {
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

	}

	static class Control {

		static Box<Object, Object> loop = new Box<Object, Object>("unknownLoop") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				// TODO dies ist ne kopie von unknownIf
				int countOfChilds = thisNode.getChildNodesSize();
				if (countOfChilds == 2) {
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

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

		static Box<Object, Object> when = new Box<Object, Object>("unknownIf") {
			@Override
			public Accessible<? extends Object> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				return createIntern(thisNode, parentType);
			}

			private Accessible<? extends Object> createIntern(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {

				int countOfChilds = thisNode.getChildNodesSize();
				if (countOfChilds == 2) {
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					Accessible<? extends Object> main = When.create(theStepsIf, condition, returntypeIf);
					return main;
				}
				else if (countOfChilds == 3) {
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);
					Accessible<?>[] theStepsElse = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(2), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);
					Class<?> returntypeElse = Function.getReturnTyp(theStepsElse);

					if (returntypeIf == Object.class) {
						if (returntypeElse == Object.class) {
							Accessible<Object> main = WhenOtherwise.create(theStepsIf, theStepsElse, condition, Object.class);
							return main;
						}
						else {
							Accessible<? extends Object> main = WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeElse);
							return main;
						}
					}
					else {
						if (returntypeElse == Object.class) {
							Accessible<? extends Object> main = WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeIf);
							return main;
						}
						else {
							if (returntypeIf != returntypeElse) {
								if (Number.class.isAssignableFrom(returntypeIf)) {
									Class<?> returntype = ElementaryArithmetic.getBiggest(returntypeIf, returntypeElse);
									Accessible<? extends Object> main = WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntype);
									return main;
								}
								else {
									System.err.println("typen passen nich...");
									return null;
								}
							}
							else {
								Accessible<? extends Object> main = WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeIf);
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

		static Box<Object, Object> then = new Box<Object, Object>("then") {

		};
		
		static Box<Object, Object> otherwise = new Box<Object, Object>("else") {

		};
		
	}

	static class Data {

		public static abstract class CompBox extends Box<Structure, Object> {

			public CompBox(String name) {
				super(name);
			}

			@Override
			public abstract Accessible<Structure> functionCreate(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException;

			public abstract Accessible<Structure> functionCreateImpl(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException;

		}
		
		static CompBox complex = new CompBox("complex") {
			
			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, Complex parentType) throws AbstractInterpreterException {
				
				Complex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);
				
				return this.functionCreateImpl(thisNode, thisType);
			}
			
			
			public Accessible<Structure> functionCreateImpl(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException {
				List<CodeNode> children = thisNode.getChildNodes();
				for (CodeNode c : children) {
					this.functionCreateChild(c, thisType);
				}
				return null;
			}
			
			public Complex complexCreate(CodeNode n, Complex parent, Map<String, Complex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}
			
		};

		static Box<Object, Object> attribute = new Box<Object, Object>("attribute", 1) {

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, Complex> unformedComplexTypes) throws AbstractInterpreterException{
				String idTypeName = null;
				String idName = null;
				Attribute<?> id = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				if (idTypeName == null) {
					System.err.println("can not get Type of " + c.getCommand() + " " + idName);
				}
				else {
					if (idTypeName.startsWith(Symbols.litSimpleTypeNS())) {
						// Simple
						id = Primitive.createAttribute(idName, idTypeName.substring(Symbols.litSimpleTypeNS().length()));
					}
					else {
						// Complex
						// referringComplexTypeNames.add(idTypeName);
						id = Complex.createAttribute(idName, idTypeName, unformedComplexTypes);
					}
				}
				if (id == null) {
					// TODO Exception
					System.err.println("createComplexType can not create Identifier " + idName + " (" + idTypeName + ")");
				}
				return id;
			}

		};		
		
		static Box<Object, Object> value = new Box<Object, Object>("value", 2) {

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, Complex> unformedComplexTypes) throws AbstractInterpreterException{
				String idTypeName = null;
				String idName = null;
				Attribute<?> id = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				if (idTypeName == null) {
					System.err.println("can not get Type of " + c.getCommand() + " " + idName);
				}
				else {
					if (idTypeName.startsWith(Symbols.litSimpleTypeNS())) {
						// Simple
						id = Primitive.createAttribute(idName, idTypeName.substring(Symbols.litSimpleTypeNS().length()));
					}
					else {
						// Complex
						// referringComplexTypeNames.add(idTypeName);
						id = Complex.createAttribute(idName, idTypeName, unformedComplexTypes);
					}
				}
				if (id == null) {
					// TODO Exception
					System.err.println("createComplexType can not create Identifier " + idName + " (" + idTypeName + ")");
				}
				return id;
			}
			
			public Value<?> valueCreate(CodeNode thisNode, Complex parentType, Structure parentData) throws AbstractInterpreterException{
				System.out.println("createValues " + thisNode.getCommand());
				Value<?> v = BuildValues.createValue(thisNode, parentType, parentData);
				return v;
			}
			
		};
		
		static Box<Object, Object> valueVirtComp = new Box<Object, Object>("VIRTUAL_COMP_VALUE") {
			public Value<?> valueCreate(CodeNode thisNode, Complex parentType, Structure parentData) throws AbstractInterpreterException{
					System.out.println("createValues " + thisNode.getCommand());
					Value<?> v = BuildValues.createValue(thisNode, parentType, parentData);
					return v;
			}
		};
		
		static Box<Object, Object> valueVirtPrim = new Box<Object, Object>("VIRTUAL_PRIM_VALUE") {
			public Value<?> valueCreate(CodeNode thisNode, Complex parentType, Structure parentData) throws AbstractInterpreterException{
				System.out.println("createValues " + thisNode.getCommand());
				Value<?> v = BuildValues.createValue(thisNode, parentType, parentData);
				return v;
			}
		};
		
		static Box<Object, Object> contract = new Box<Object, Object>("contract") {

		};
	}

	public static Function<?> build(CodeNode thisNode, Complex thisType) throws AbstractInterpreterException {

		//Data.complex.functionCreateImpl(thisNode, thisType); 
		return Fun.whatEver.functionCreateImpl(thisNode, thisType); 

	}

	public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box<?, ?> box) throws AbstractInterpreterException {

		String name = n.getCommand();
		if (type == null) {
			System.err.println("can not recognize type of " + name + " " + n.getParameter(Symbols.paramName()));
			return null;
		}
		List<Accessible<?>> steps = new LinkedList<>();

		List<CodeNode> children = n.getChildNodes();
		for (CodeNode c : children) {
			//if (c.isBuildInFunction() && !c.getCommand().equals(Symbol.FUNCTION)) {
				Accessible<?> v = box.functionCreateChild(c, type);
				if (v != null) {
					steps.add(v);
				}
			//}
			//else{
			//System.out.println("check this ...");
			//box.functionCreateChild(c, type);
			//}
		}

		Accessible<?>[] theSteps = new Accessible<?>[steps.size()];
		return steps.toArray(theSteps);
	}

	static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
		if (a == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 1) {
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getParameter(Symbols.paramName()));
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
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getParameter(Symbols.paramName()));
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
		Complex srcComplexType = Complex.getInstance(srcNode.getParameter(Symbols.paramType()));
		if (srcComplexType == null) {
			throw new UnknownComplexType(srcNode.getParameter(Symbols.paramType()) + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
		}
		if (trgComplexType != srcComplexType) {
			throw new TypesDoNotMatch(ComplexTrgTyp, srcNode.getParameter(Symbols.paramType()) + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
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
				throw new TypesDoNotMatch(trgRawType + " > " + srcRawType + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
			}
		}
	}

	public static void checkType(Class<?> type, Class<?> expected) throws TypesDoNotMatch {
		if (type != expected) {
			throw new TypesDoNotMatch(type.getName() + " is not " + expected.getName());
		}
	}

	public static List<AbstractAssigment<? extends Object>> createAssigs(CodeNode n, Complex parentTyp) throws AbstractInterpreterException {
		String path2FunctionComplex = n.getParameter(Symbols.paramType()) + "." + n.getParameter(Symbols.paramName());
		Complex functionComplex = Complex.getInstance(path2FunctionComplex); // z ...

		List<AbstractAssigment<? extends Object>> assig = new LinkedList<>();

		for (CodeNode srcNode : n.getChildNodes()) {
			String path2TrgParam = // n.getName() + "." +
					srcNode.getParameter(Symbols.paramName());
			SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(path2TrgParam, functionComplex);

			Accessible<?> src = Assign.whatEverAssigment.functionCreateChild(srcNode, parentTyp);
			
			if (trg.getRawTypeClass() == Structure.class) {
				Attribute<?> trgAttr = functionComplex.getSubAttribute(srcNode.getParameter(Symbols.paramName()));
				String trgTypeName = trgAttr.getType().getName();				
				assig.add(createAssigComp(Symbols.comCopy(), srcNode, src, trgTypeName, trg));
			}
			else {
				assig.add(createAssig(Symbols.comCopy(), srcNode, src, trg));
			}
			
		}

		return assig;
	}

	public static AbstractAssigment<Structure> createAssigComp(String command, CodeNode srcNode, Accessible<?> src, String ComplexTrgNodeTyp,
			SetableValue<?> trg) throws AbstractInterpreterException {

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

	public static Complex getThisNodeType(CodeNode thisNode, Complex parentTyp) throws AbstractInterpreterException {
		if(parentTyp == null){
			// TODO throw
			System.err.println("createFunctions: can not identify " + thisNode.getParameter(Symbols.paramName()));
			return null;
		}
		Complex type = Complex.getInstance(parentTyp.getName() + "." + thisNode.getParameter(Symbols.paramName()));
		if (type == null) {
			// TODO throw
			System.err.println("createFunctions: can not identify " + parentTyp.getName() + "." + thisNode.getParameter(Symbols.paramName()));
			return null;
		}
		return type;		
	}
	
}
