package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.CodeModel.Box;
import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.FunctionNotFound;
import com.conetex.contract.build.exceptionFunction.MissingSubOperation;
import com.conetex.contract.build.exceptionFunction.NoAccessToValue;
import com.conetex.contract.build.exceptionFunction.TypeNotDeterminated;
import com.conetex.contract.build.exceptionFunction.TypesDoNotMatch;
import com.conetex.contract.build.exceptionFunction.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionFunction.UnknownComplexType;
import com.conetex.contract.build.exceptionFunction.UnknownType;
import com.conetex.contract.lang.function.access.Accessible;
import com.conetex.contract.lang.function.access.AccessibleConstant;
import com.conetex.contract.lang.function.access.AccessibleValue;
import com.conetex.contract.lang.function.access.SetableValue;
import com.conetex.contract.lang.function.assign.AbstractAssigment;
import com.conetex.contract.lang.function.assign.Creator;
import com.conetex.contract.lang.function.bool.expression.Comparsion;
import com.conetex.contract.lang.function.bool.expression.IsNull;
import com.conetex.contract.lang.function.bool.operator.Binary;
import com.conetex.contract.lang.function.bool.operator.Not;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.function.control.FunctionCall;
import com.conetex.contract.lang.function.control.Loop;
import com.conetex.contract.lang.function.control.Return;
import com.conetex.contract.lang.function.control.When;
import com.conetex.contract.lang.function.control.WhenOtherwise;
import com.conetex.contract.lang.function.math.ElementaryArithmetic;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;

public class BuildFunctions{

	static class Expression{

		static final Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// MATH
				String name = thisNode.getCommand();
				Accessible<? extends Number> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				Accessible<? extends Number> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
				check2PartOperations(thisNode, a, b);
				return ElementaryArithmetic.createNew(a, b, name);
			}
		};

		static final Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// BOOL
				String name = thisNode.getCommand();
				if(name.equals(Symbols.comNot())){
					Accessible<? extends Boolean> sub = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					check1PartOperations(thisNode, sub);
					return Not.create(sub);
				}
				else{
					Accessible<? extends Boolean> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<? extends Boolean> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
					check2PartOperations(thisNode, a, b);
					return Binary.create(a, b, name);
				}
			}
		};

		static final Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// COMPARISON
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				Accessible<?> b = this.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
				check2PartOperations(thisNode, a, b);
				return Comparsion.createComparison(a, b, thisNode.getCommand());
			}
		};

		static final Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				check1PartOperations(thisNode, a);
				return IsNull.create(a);
			}
		};

	}

	static class Reference{

		static final Egg<Object> whatEverRef = new Egg<Object>("whatEverRef"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefWhatEver(thisNode.getParameter(Symbols.paramValue()), parentType); // parentTyp);
			}
		};

		static final Egg<Structure> structureRef = new Egg<Structure>("objRef"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()), parentType, Structure.class); // parentTyp);
			}
		};

		static final Egg<Number> numberRef = new Egg<Number>("numberRef"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefNum(thisNode.getParameter(Symbols.paramValue()), parentType);
			}
		};

		static final Egg<Boolean> boolRef = new Egg<Boolean>("boolRef"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()), parentType, Boolean.class);
			}
		};

	}

	static class Constant{

		static final Egg<Structure> objConst = new Egg<Structure>("objConst"){
			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.create2(Structure.class, thisNode);
			}
		};

		static final Egg<Object> whatEverConst = new Egg<Object>("whatEverConst"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws TypeNotDeterminated, UnknownType, UnknownCommandParameter, UnknownCommand {
				Accessible<? extends Number> reNu = AccessibleConstant.try2CreateNumConst(thisNode, parentType);
				if(reNu != null){
					return reNu;
				}
				Accessible<Boolean> reBo = AccessibleConstant.try2CreateBoolConst(thisNode, parentType);
				if(reBo != null){
					return reBo;
				}
				Accessible<Structure> reStructure = AccessibleConstant.try2CreateStructureConst(thisNode, parentType);
				if(reStructure != null){
					return reStructure;
				}
				throw new TypeNotDeterminated("const-Type: " + thisNode.getParameter(Symbols.paramName()));
			}
		};

		static final Egg<Number> numberConst = new Egg<Number>("numberConst"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws UnknownType, TypeNotDeterminated, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.createNumConst(thisNode, parentType);
			}
		};

		static final Egg<Boolean> boolConst = new Egg<Boolean>("boolConst"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws UnknownType, UnknownCommandParameter, UnknownCommand {
				return AccessibleConstant.create2(Boolean.class, thisNode);
			}
		};

	}

	static class Assign{

		static final Box<Structure, Structure> structureAssigment = new Box<Structure, Structure>("objAssigment"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
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

		static final Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment"){

			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
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

		static final Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<Boolean> trg = SetableValue.createFunctionSetable(trgNode.getParameter(Symbols.paramValue()), parentType, Boolean.class);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				checkType(src.getRawTypeClass(), Boolean.class);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static final Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(trgNode.getParameter(Symbols.paramValue()), parentType);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				if(trg.getRawTypeClass() == Structure.class){
					return createAssigComp(thisNode.getCommand(), srcNode, src, trgNode.getParameter(Symbols.paramType()), trg);
				}
				else{
					return createAssig(thisNode.getCommand(), srcNode, src, trg);
				}
			}
		};

	}

	static class FunReturn{

		static final Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static final Box<Structure, Structure> structureReturn = new Box<Structure, Structure>("objReturn"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<? extends Structure> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static final Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<? extends Number> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static final Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<? extends Boolean> a = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

	}

	static class Fun{

		static final Box<Object, Object> noReturn = new Box<Object, Object>("voidFunction"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == Object.class){
					return Function.createVoid(theSteps, thisNode.getParameter(Symbols.paramName()));
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

		static final Box<Structure, Object> structure = new Box<Structure, Object>("objFunction"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == Structure.class){
					return Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		static final Box<Number, Object> number = new Box<Number, Object>("numberFunction"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				return Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
			}
		};

		static final Box<Boolean, Object> bool = new Box<Boolean, Object>("boolFunction"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == Boolean.class){
					return Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		public static abstract class FunBox extends Box<Object, Object>{

			FunBox(String name) {
				super(name);
			}

			@Override
			public Function<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {

				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				return this.functionCreateImpl(thisNode, thisType);
			}

			protected abstract Function<?> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

		}

		static final FunBox whatEver = new FunBox("whatEverFunction"){
			@Override
			public Function<?> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, thisType, this);
				if(theSteps == null){
					System.err.println("no steps ");
				}
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == String.class){
					return null;
				}
				else if(returntype == Boolean.class){
					return Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				else if(Number.class.isAssignableFrom(returntype)){
					return Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
				}
				else if(returntype == Structure.class){
					return Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				else if(returntype == Object.class){
					return Function.createVoid(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}

			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				String idTypeName = null;
				String idName = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				// referringComplexTypeNames.add(typeName + "." + idTypeName);//
				// TODO BUG !!!
				// Attribute<?> fun =
				TypeComplexOfFunction.createAttributeFun(idName, idTypeName, unformedComplexTypes);
				return null;
			}

			public Value<?> valueCreate(CodeNode thisNode, TypeComplex parentType, Structure parentData) throws AbstractInterpreterException {
				System.out.println("createValues " + thisNode.getCommand());
				return BuildValues.createValue4Function(thisNode, parentType, parentData);
			}

			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}

		};

	}

	static class FunCall{

		static final Egg<Object> voidCall = new Egg<Object>("voidCall"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<?> e = Function.getInstanceVoid(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static final Egg<Object> whatEverCall = new Egg<Object>("whatEverCall"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<?> e = Function.getInstance(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static final Egg<Structure> structureCall = new Egg<Structure>("objCall"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Structure> e = Function.getInstanceStructure(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static final Egg<Number> numberCall = new Egg<Number>("numberCall"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<? extends Number> e = Function.getInstanceNum(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

		static final Egg<Boolean> boolCall = new Egg<Boolean>("boolCall"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// CONTROL FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				Function<Boolean> e = Function.getInstanceBool(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					throw new FunctionNotFound(thisNode.getParameter(Symbols.paramName()));
				}
				return FunctionCall.create(e, re, createAssigs(thisNode, parentType));
			}
		};

	}

	static class Control{

		static final Box<Object, Object> loop = new Box<Object, Object>("unknownLoop"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				// TODO dies ist ne kopie von unknownIf
				int countOfChilds = thisNode.getChildNodesSize();
				if(countOfChilds == 2){
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					return Loop.create(theStepsIf, condition, returntypeIf);
				}
				else{
					System.err.println("argumente passen nicht");
					return null;
				}

			}
		};

		static final Box<Object, Object> when = new Box<Object, Object>("unknownIf"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {
				return createIntern(thisNode, parentType);
			}

			private Accessible<?> createIntern(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {

				int countOfChilds = thisNode.getChildNodesSize();
				if(countOfChilds == 2){
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					return When.create(theStepsIf, condition, returntypeIf);
				}
				else if(countOfChilds == 3){
					Accessible<?> conditionUnTyped = this.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);
					Accessible<?>[] theStepsElse = BuildFunctions.getFunctionSteps(thisNode.getChildElementByIndex(2), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);
					Class<?> returntypeElse = Function.getReturnTyp(theStepsElse);

					if(returntypeIf == Object.class){
						if(returntypeElse == Object.class){
							return WhenOtherwise.create(theStepsIf, theStepsElse, condition, Object.class);
						}
						else{
							return WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeElse);
						}
					}
					else{
						if(returntypeElse == Object.class){
							return WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeIf);
						}
						else{
							if(returntypeIf != returntypeElse){
								if(Number.class.isAssignableFrom(returntypeIf)){
									Class<?> returntype = ElementaryArithmetic.getBiggest(returntypeIf, returntypeElse);
									return WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntype);
								}
								else{
									System.err.println("typen passen nich...");
									return null;
								}
							}
							else{
								return WhenOtherwise.create(theStepsIf, theStepsElse, condition, returntypeIf);
							}
						}
					}
				}
				else{
					System.err.println("argumente passen nicht");
					return null;
				}

			}
		};

		static final Box<Object, Object> then = new Box<Object, Object>("then"){
// TODO implement
		};

		static final Box<Object, Object> otherwise = new Box<Object, Object>("else"){
			// TODO implement
		};

	}



	public static Function<?> build(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {

		//Data.complex.functionCreateImpl(thisNode, thisType); 
		return Fun.whatEver.functionCreateImpl(thisNode, thisType);

	}

	static Accessible<?>[] getFunctionSteps(CodeNode n, TypeComplex type, Box<?, ?> box) throws AbstractInterpreterException {

		String name = n.getCommand();
		if(type == null){
			System.err.println("can not recognize type of " + name + " " + n.getParameter(Symbols.paramName()));
			return null;
		}
		List<Accessible<?>> steps = new LinkedList<>();

		List<CodeNode> children = n.getChildNodes();
		for(CodeNode c : children){
			//if (c.isBuildInFunction() && !c.getCommand().equals(Symbol.FUNCTION)) {
			Accessible<?> v = box.functionCreateChild(c, type);
			if(v != null){
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

	private static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
		if(a == null){
			throw new MissingSubOperation("");
		}
		if(n.getChildNodesSize() > 1){
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getParameter(Symbols.paramName()));
		}
	}

	static void check2PartOperations(CodeNode n, Accessible<?> trg, Accessible<?> src)
			throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
		if(trg == null){
			throw new MissingSubOperation("");
		}
		if(src == null){
			throw new MissingSubOperation("");
		}
		if(n.getChildNodesSize() > 2){
			CodeNode c = n.getChildElementByIndex(2);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getParameter(Symbols.paramName()));
		}
	}

	private static void checkAssigmentStructRawType(String ComplexTrgTyp, CodeNode srcNode, Class<?> srcRawType)
			throws TypesDoNotMatch, UnknownComplexType, UnknownCommandParameter, UnknownCommand {
		if(srcRawType != Structure.class){
			throw new TypesDoNotMatch(srcRawType.toString(), Structure.class.toString());
		}

		TypeComplex trgComplexType = TypeComplex.getInstance(ComplexTrgTyp);
		if(trgComplexType == null){
			throw new UnknownComplexType(ComplexTrgTyp);
		}
		TypeComplex srcComplexType = TypeComplex.getInstance(srcNode.getParameter(Symbols.paramType()));
		if(srcComplexType == null){
			throw new UnknownComplexType(
					srcNode.getParameter(Symbols.paramType()) + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
		}
		if(trgComplexType != srcComplexType){
			throw new TypesDoNotMatch(ComplexTrgTyp,
					srcNode.getParameter(Symbols.paramType()) + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
		}
	}

	private static void checkAssigmentNumRawType(Class<?> trgRawType, Class<?> srcRawType, CodeNode srcNode)
			throws TypesDoNotMatch, UnknownCommandParameter, UnknownCommand {
		Class<?> rawType;
		if(trgRawType == srcRawType){
			rawType = trgRawType;
		}
		else{
			rawType = ElementaryArithmetic.getBiggest(trgRawType, srcRawType);
			if(rawType == srcRawType){
				throw new TypesDoNotMatch(
						trgRawType + " > " + srcRawType + "(" + srcNode.getCommand() + " - " + srcNode.getParameter(Symbols.paramName()) + ")");
			}
		}
	}

	public static void checkType(Class<?> type, Class<?> expected) throws TypesDoNotMatch {
		if(type != expected){
			throw new TypesDoNotMatch(type.getName() + " is not " + expected.getName());
		}
	}

	static List<AbstractAssigment<?>> createAssigs(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
		String path2FunctionComplex = n.getParameter(Symbols.paramType()) + "." + n.getParameter(Symbols.paramName());
		TypeComplex functionComplex = TypeComplex.getInstance(path2FunctionComplex); // z ...

		List<AbstractAssigment<?>> assig = new LinkedList<>();

		for(CodeNode srcNode : n.getChildNodes()){
			String path2TrgParam = // n.getName() + "." +
					srcNode.getParameter(Symbols.paramName());
			SetableValue<?> trg = SetableValue.createFunctionSetableWhatEver(path2TrgParam, functionComplex);

			Accessible<?> src = Assign.whatEverAssigment.functionCreateChild(srcNode, parentTyp);

			if(trg.getRawTypeClass() == Structure.class){
				Attribute<?> trgAttr = functionComplex.getSubAttribute(srcNode.getParameter(Symbols.paramName()));
				String trgTypeName = trgAttr.getType().getName();
				assig.add(createAssigComp(Symbols.comCopy(), srcNode, src, trgTypeName, trg));
			}
			else{
				assig.add(createAssig(Symbols.comCopy(), srcNode, src, trg));
			}

		}

		return assig;
	}

	private static AbstractAssigment<Structure> createAssigComp(String command, CodeNode srcNode, Accessible<?> src, String ComplexTrgNodeTyp,
																SetableValue<?> trg) throws AbstractInterpreterException {

		Class<?> trgRawType = trg.getRawTypeClass();
		Class<?> srcRawType = src.getRawTypeClass();
		if(trgRawType == Structure.class){
			checkAssigmentStructRawType(ComplexTrgNodeTyp, srcNode, srcRawType);
			return Creator.createFromUnqualified(trg, src, Structure.class, command);
		}
		else{
			throw new TypeNotDeterminated("target-Type: " + trgRawType + ", source-Type: " + srcRawType);
		}
	}

	private static AbstractAssigment<?> createAssig(String command, CodeNode srcNode, Accessible<?> src,
													SetableValue<?> trg) throws AbstractInterpreterException {

		Class<?> trgRawType = trg.getRawTypeClass();
		Class<?> srcRawType = src.getRawTypeClass();
		if(trgRawType == Boolean.class){
			checkType(src.getRawTypeClass(), Boolean.class);
			return Creator.createFromUnqualified(trg, src, Boolean.class, command);
		}
		else if(trgRawType == String.class){
			checkType(src.getRawTypeClass(), String.class);
			return Creator.createFromUnqualified(trg, src, String.class, command);
		}
		else if(Number.class.isAssignableFrom(trgRawType)){
			checkAssigmentNumRawType(trgRawType, srcRawType, srcNode);
			return Creator.createFromUnqualified(trg, src, trgRawType, command);
		}
		else{
			throw new TypeNotDeterminated("target-Type: " + trgRawType + ", source-Type: " + srcRawType);
		}
	}

	public static TypeComplex getThisNodeType(CodeNode thisNode, TypeComplex parentTyp) throws AbstractInterpreterException {
		if(parentTyp == null){
			// TODO throw
			System.err.println("createFunctions: can not identify " + thisNode.getParameter(Symbols.paramName()));
			return null;
		}
		TypeComplex type = TypeComplex.getInstance(parentTyp.getName() + "." + thisNode.getParameter(Symbols.paramName()));
		if(type == null){
			// TODO throw
			System.err.println("createFunctions: can not identify " + parentTyp.getName() + "." + thisNode.getParameter(Symbols.paramName()));
			return null;
		}
		return type;
	}

}
