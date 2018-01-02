package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.BuildTypes.TypeComplexTemp;
import com.conetex.contract.build.BuildTypes.Types;
import com.conetex.contract.build.CodeModel.BoxFun;
import com.conetex.contract.build.CodeModel.BoxFunImp;
import com.conetex.contract.build.CodeModel.BoxValueTypeFunImp;
import com.conetex.contract.build.CodeModel.EggFun;
import com.conetex.contract.build.CodeModel.EggFunImp;
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
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleConstant;
import com.conetex.contract.lang.function.access.AccessibleValue;
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
import com.conetex.contract.lang.type.TypeComplexFunction;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class BuildFunctions{

	static class Expression{

		static final BoxFun<Number, Number> numberExpession = new BoxFunImp<Number, Number>("numberExpession"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode,
					TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				// MATH
				String name = thisNode.getCommand();
				Accessible<? extends Number> a = this
						.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				Accessible<? extends Number> b = this
						.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
				check2PartOperations(thisNode, a, b);
				return ElementaryArithmetic.createNew(a, b, name);
			}
		};

		static final BoxFun<Boolean, Boolean> boolExpression = new BoxFunImp<Boolean, Boolean>("boolExpression"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				// BOOL
				String name = thisNode.getCommand();
				if(name.equals(Symbols.comNot())){
					Accessible<? extends Boolean> sub = this
							.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					check1PartOperations(thisNode, sub);
					return Not.create(sub);
				}
				else{
					Accessible<? extends Boolean> a = this
							.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<? extends Boolean> b = this
							.functionCreateChild(thisNode.getChildElementByIndex(1), parentType);
					check2PartOperations(thisNode, a, b);
					return Binary.create(a, b, name);
				}
			}
		};

		static final BoxFun<Boolean, Object> boolComparsion = new BoxFunImp<Boolean, Object>("boolComparsion"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				// COMPARISON
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0),
						parentType);
				Accessible<?> b = this.functionCreateChild(thisNode.getChildElementByIndex(1),
						parentType);
				check2PartOperations(thisNode, a, b);
				return Comparsion.createComparison(a, b, thisNode.getCommand());
			}
		};

		static final BoxFun<Boolean, ?> boolNullCheck = new BoxFunImp<Boolean, Object>("boolNullCheck"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0),
						parentType);
				check1PartOperations(thisNode, a);
				return IsNull.create(a);
			}
		};

	}

	static class Reference{

		static final EggFun<Object> whatEverRef = new EggFunImp<Object>("whatEverRef"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefWhatEver(thisNode.getParameter(Symbols.paramValue()),
						parentType); // parentTyp);
			}
		};

		static final EggFun<Structure> structureRef = new EggFunImp<Structure>("objRef"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()),
						parentType, Structure.class); // parentTyp);
			}
		};

		static final EggFun<Number> numberRef = new EggFunImp<Number>("numberRef"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRefNum(thisNode.getParameter(Symbols.paramValue()),
						parentType);
			}
		};

		static final EggFun<Boolean> boolRef = new EggFunImp<Boolean>("boolRef"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException {
				return AccessibleValue.createFunctionRef(thisNode.getParameter(Symbols.paramValue()),
						parentType, Boolean.class);
			}
		};

	}

	static class Constant{

		static final EggFun<Structure> objConst = new EggFunImp<Structure>("objConst"){
			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				return AccessibleConstant.create2(Structure.class, thisNode);
			}
		};

		static final EggFun<Object> whatEverConst = new EggFunImp<Object>("whatEverConst"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				Accessible<? extends Number> reNu = AccessibleConstant.try2CreateNumConst(thisNode, parentType);
				if(reNu != null){
					return reNu;
				}
				Accessible<Boolean> reBo = AccessibleConstant.try2CreateBoolConst(thisNode, parentType);
				if(reBo != null){
					return reBo;
				}
				Accessible<String> reStr = AccessibleConstant.try2CreateStrConst(thisNode, parentType);
				if(reStr != null){
					return reStr;
				}				
				Accessible<Structure> reStructure = AccessibleConstant.try2CreateStructureConst(thisNode,
						parentType);
				if(reStructure != null){
					return reStructure;
				}
				throw new TypeNotDeterminated("const-Type: " + thisNode.getParameter(Symbols.paramName()));
			}
		};

		static final EggFun<Number> numberConst = new EggFunImp<Number>("numberConst"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				return AccessibleConstant.createNumConst(thisNode, parentType);
			}
		};

		static final EggFun<String> stringConst = new EggFunImp<String>("stringConst"){
			@Override
			public Accessible<String> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				return AccessibleConstant.create2(String.class, thisNode);
			}
		};

		static final EggFun<Boolean> boolConst = new EggFunImp<Boolean>("boolConst"){
			@Override
			public Accessible<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
				return AccessibleConstant.create2(Boolean.class, thisNode);
			}
		};

	}

	static class Assign{

		static final BoxFun<Structure, Structure> structureAssigment = new BoxFunImp<Structure, Structure>("objAssigment"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				AccessibleValue<Structure> trg = AccessibleValue.createFunctionSetable(
						trgNode.getParameter(Symbols.paramValue()), parentType,
						Structure.class);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentStructRawType(trgNode.getParameter(Symbols.paramType()), srcNode,
						srcRawType);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static final BoxFun<Number, Number> numberAssigment = new BoxFunImp<Number, Number>("numberAssigment"){

			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				AccessibleValue<? extends Number> trg = AccessibleValue
						.createFunctionSetableNumber(trgNode.getParameter(Symbols.paramValue()),
								parentType);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				Class<?> trgRawType = trg.getRawTypeClass();
				Class<?> srcRawType = src.getRawTypeClass();
				checkAssigmentNumRawType(trgRawType, srcRawType, srcNode);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static final BoxFun<Boolean, Boolean> boolAssigment = new BoxFunImp<Boolean, Boolean>("boolAssigment"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				AccessibleValue<Boolean> trg = AccessibleValue.createFunctionSetable(
						trgNode.getParameter(Symbols.paramValue()), parentType, Boolean.class);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				checkType(src.getRawTypeClass(), Boolean.class);
				return Creator.createFromQualifiedTrg(trg, src, thisNode.getCommand());
			}
		};

		static final BoxFun<Object, Object> whatEverAssigment = new BoxFunImp<Object, Object>("whatEverAssigment"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid,
					AbstractTypException {
				CodeNode trgNode = thisNode.getChildElementByIndex(0);
				AccessibleValue<?> trg = AccessibleValue.createFunctionSetableWhatEver(
						trgNode.getParameter(Symbols.paramValue()), parentType);
				CodeNode srcNode = thisNode.getChildElementByIndex(1);
				Accessible<?> src = this.functionCreateChild(srcNode, parentType);
				check2PartOperations(thisNode, trg, src);
				if(trg.getRawTypeClass() == Structure.class){
					return createAssigComp(thisNode.getCommand(), srcNode, src,
							trgNode.getParameter(Symbols.paramType()), trg);
				}
				else{
					return createAssig(thisNode.getCommand(), srcNode, src, trg);
				}
			}
		};

	}

	static class FunReturn{

		static final BoxFun<Object, Object> whatEverReturn = new BoxFunImp<Object, Object>("whatEverReturn"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid,
					AbstractTypException {
				Accessible<?> a = this.functionCreateChild(thisNode.getChildElementByIndex(0),
						parentType);
				return Return.create(a);
			}
		};

		static final BoxFun<Structure, Structure> structureReturn = new BoxFunImp<Structure, Structure>("objReturn"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				Accessible<? extends Structure> a = this
						.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static final BoxFun<Number, Number> numberReturn = new BoxFunImp<Number, Number>("numberReturn"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				Accessible<? extends Number> a = this
						.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

		static final BoxFun<Boolean, Boolean> boolReturn = new BoxFunImp<Boolean, Boolean>("boolReturn"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				Accessible<? extends Boolean> a = this
						.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
				return Return.create(a);
			}
		};

	}

	static class Fun{

		static class NoReturn extends BoxFunImp<Object, Object>{
			NoReturn(String theName) {
				super(theName);
			}

			@Override
			public Function<? extends Object> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
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
		}

		static final NoReturn noReturn = new NoReturn("voidFunction");

		static class StructureReturn extends BoxFunImp<Structure, Object>{
			StructureReturn(String theName) {
				super(theName);
			}

			@Override
			public Function<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == Structure.class){
					return Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		}

		static final StructureReturn structure = new StructureReturn("objFunction");

		static class NumberReturn extends BoxFunImp<Number, Object>{
			NumberReturn(String theName) {
				super(theName);
			}

			@Override
			public Function<? extends Number> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				return Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
			}
		}

		static final NumberReturn number = new NumberReturn("numberFunction");

		static class BoolReturn extends BoxFunImp<Boolean, Object>{
			BoolReturn(String theName) {
				super(theName);
			}

			@Override
			public Function<Boolean> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, parentType, this);
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == Boolean.class){
					return Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		}

		static final BoolReturn bool = new BoolReturn("boolFunction");

		static class FunctionClass extends BoxValueTypeFunImp<Object, Object>{

			FunctionClass(String theName) {
				super(theName);
			}

			@Override
			public Value<?> valueCreate(CodeNode n, TypeComplex parentType, Structure parentData) throws AbstractInterpreterException {
				System.out.println("createValues " + n.getCommand());
				return BuildValues.createValue4Function(n, parentType, parentData);
			}

			@Override
			public Function<? extends Object> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				// return this.functionCreateImpl(thisNode, thisType);
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
				throw new AbstractInterpreterException("unknown return type " + returntype);
			}

			@Override
			public Attribute<?> typeCreateAttribute(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				String idTypeName = null;
				String idName = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				// referringComplexTypeNames.add(typeName + "." + idTypeName);//
				// TODO BUG !!!
				// Attribute<?> fun =
				TypeComplexFunction.createAttributeFun(idName, idTypeName, unformedComplexTypes);
				return null;
			}

			@Override
			public TypeComplex typeCreateComplex(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
					throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes);
			}

		}

		static final FunctionClass whatEver = new FunctionClass("whatEverFunction");

	}

	static class FunCall{

		static CodeNode getFunctionNode(String functionName) throws FunctionNotFound, UnknownCommandParameter, UnknownCommand {
			CodeNode fNode = getFunctionNode(CodeNode.getComplexRoot(), functionName);
			if(fNode == null){
				throw new FunctionNotFound(functionName); // throw new
															// UnknownCommand("function
															// not defined " +
															// functionName);
			}
			return fNode;
		}

		static CodeNode getFunctionNode(CodeNode n, String functionName) throws UnknownCommandParameter, UnknownCommand {
			if(n.getCommand() == Symbols.comFunction()){
				if(functionName.equals(n.getParameter(Symbols.paramName()))){
					System.out.println("Treffer ... " + n.getParameter(Symbols.paramName()));
					return n;
				}
				else{
					System.out.println("kein Treffer ... " + n.getParameter(Symbols.paramName()));
				}
			}
			for(CodeNode c : n.getChildNodes()){
				CodeNode re = getFunctionNode(c, functionName);
				if(re != null){
					return re;
				}
			}
			return null;
		}

		static final BoxFunImp<Object, Object> voidCall = new BoxFunImp<Object, Object>("voidCall"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid,
					AbstractTypException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj,
						Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				String functionName = thisNode.getParameter(Symbols.paramName());
				Function<?> e = Function
						.getInstanceVoid(thisNode.getParameter(Symbols.paramName()));
				if(e == null){
					CodeNode fNode = getFunctionNode(functionName);
					TypeComplex parentTypeOfFunction = TypeComplex
							.getInstanceNoNull(Symbols.getParentNameNoNull(functionName));
					e = Fun.noReturn.functionCreate(fNode, parentTypeOfFunction);
				}
				TypeComplex functionComplex = TypeComplex.getInstance(functionName);
				List<Accessible<?>> assig = new LinkedList<>();
				for(CodeNode srcNode : thisNode.getChildNodes()){
					Accessible<?> src = this.functionCreateChild(srcNode, functionComplex);
					assig.add(src);
				}
				return FunctionCall.create(e, re, assig);
			}
		};

		static final BoxFunImp<Object, Object> whatEverCall = new BoxFunImp<Object, Object>("whatEverCall"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid,
					AbstractTypException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj,
						Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				String functionName = thisNode.getParameter(Symbols.paramName());
				Function<?> e = Function.getInstance(functionName);
				if(e == null){
					CodeNode fNode = getFunctionNode(functionName);
					TypeComplex parentTypeOfFunction = TypeComplex
							.getInstanceNoNull(Symbols.getParentNameNoNull(functionName));
					e = Fun.whatEver.functionCreate(fNode, parentTypeOfFunction);
				}
				TypeComplex functionComplex = TypeComplex.getInstance(functionName);
				List<Accessible<?>> assig = new LinkedList<>();
				for(CodeNode srcNode : thisNode.getChildNodes()){
					Accessible<?> src = this.functionCreateChild(srcNode, functionComplex);
					assig.add(src);
				}
				return FunctionCall.create(e, re, assig);
			}
		};

		static final BoxFunImp<Structure, Object> structureCall = new BoxFunImp<Structure, Object>("objCall"){
			@Override
			public Accessible<? extends Structure> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj,
						Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				String functionName = thisNode.getParameter(Symbols.paramName());
				Function<? extends Structure> e = Function.getInstanceStructure(functionName);
				if(e == null){
					CodeNode fNode = getFunctionNode(functionName);
					TypeComplex parentTypeOfFunction = TypeComplex
							.getInstanceNoNull(Symbols.getParentNameNoNull(functionName));
					e = Fun.structure.functionCreate(fNode, parentTypeOfFunction);
				}
				TypeComplex functionComplex = TypeComplex.getInstance(functionName);
				List<Accessible<?>> assig = new LinkedList<>();
				for(CodeNode srcNode : thisNode.getChildNodes()){
					Accessible<?> src = this.functionCreateChild(srcNode, functionComplex);
					assig.add(src);
				}
				return FunctionCall.create(e, re, assig);
			}
		};

		static final BoxFunImp<Number, Object> numberCall = new BoxFunImp<Number, Object>("numberCall"){
			@Override
			public Accessible<? extends Number> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				// CONTROL
				// FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj,
						Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				String functionName = thisNode.getParameter(Symbols.paramName());
				Function<? extends Number> e = Function.getInstanceNum(functionName);
				if(e == null){
					CodeNode fNode = getFunctionNode(CodeNode.getComplexRoot(), functionName);
					TypeComplex parentTypeOfFunction = TypeComplex
							.getInstanceNoNull(Symbols.getParentNameNoNull(functionName));
					e = Fun.number.functionCreate(fNode, parentTypeOfFunction);
				}
				TypeComplex functionComplex = TypeComplex.getInstance(functionName);
				List<Accessible<?>> assig = new LinkedList<>();
				for(CodeNode srcNode : thisNode.getChildNodes()){
					Accessible<?> src = this.functionCreateChild(srcNode, functionComplex);
					assig.add(src);
				}
				return FunctionCall.create(e, re, assig);
			}
		};

		static final BoxFunImp<Boolean, Object> boolCall = new BoxFunImp<Boolean, Object>("boolCall"){
			@Override
			public Accessible<? extends Boolean> functionCreate(CodeNode thisNode,
					TypeComplex parentType) throws AbstractInterpreterException, Inconvertible,
					Invalid, AbstractTypException {
				// CONTROL
				// FUNCTION
				String functionObj = thisNode.getParameter(Symbols.paramType()); //
				AccessibleValue<Structure> re = AccessibleValue.create(functionObj,
						Structure.class);
				if(re == null){
					throw new NoAccessToValue(thisNode.getParameter(Symbols.paramType()));
				}
				String functionName = thisNode.getParameter(Symbols.paramName());
				Function<Boolean> e = Function.getInstanceBool(functionName);
				if(e == null){
					CodeNode fNode = getFunctionNode(CodeNode.getComplexRoot(), functionName);
					TypeComplex parentTypeOfFunction = TypeComplex
							.getInstanceNoNull(Symbols.getParentNameNoNull(functionName));
					e = Fun.bool.functionCreate(fNode, parentTypeOfFunction);
				}
				TypeComplex functionComplex = TypeComplex.getInstance(functionName);
				List<Accessible<?>> assig = new LinkedList<>();
				for(CodeNode srcNode : thisNode.getChildNodes()){
					Accessible<?> src = this.functionCreateChild(srcNode, functionComplex);
					assig.add(src);
				}
				return FunctionCall.create(e, re, assig);
			}
		};

		static final BoxFun<Object, Object> _whatEverParam = new BoxFunImp<Object, Object>("whatEverParam"){
			// TODO
			// implement
			// ....
		};

	}

	static class Control{

		static final BoxFun<Object, Object> loop = new BoxFunImp<Object, Object>("unknownLoop"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				// TODO dies ist
				// ne kopie von
				// unknownIf
				int countOfChilds = thisNode.getChildNodesSize();
				if(countOfChilds == 2){
					Accessible<?> conditionUnTyped = this
							.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions
							.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					return Loop.create(theStepsIf, condition, returntypeIf);
				}
				else{
					System.err.println("argumente passen nicht");
					return null;
				}

			}
		};

		static final BoxFun<Object, Object> when = new BoxFunImp<Object, Object>("unknownIf"){
			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				return createIntern(thisNode, parentType);
			}

			private Accessible<?> createIntern(CodeNode thisNode, TypeComplex parentType)
					throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {

				int countOfChilds = thisNode.getChildNodesSize();
				if(countOfChilds == 2){
					Accessible<?> conditionUnTyped = this
							.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions
							.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);

					Class<?> returntypeIf = Function.getReturnTyp(theStepsIf);

					return When.create(theStepsIf, condition, returntypeIf);
				}
				else if(countOfChilds == 3){
					Accessible<?> conditionUnTyped = this
							.functionCreateChild(thisNode.getChildElementByIndex(0), parentType);
					Accessible<Boolean> condition = Cast.toTypedAccessible(conditionUnTyped, Boolean.class);

					Accessible<?>[] theStepsIf = BuildFunctions
							.getFunctionSteps(thisNode.getChildElementByIndex(1), parentType, this);
					Accessible<?>[] theStepsElse = BuildFunctions
							.getFunctionSteps(thisNode.getChildElementByIndex(2), parentType, this);

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
									Class<?> returntype = ElementaryArithmetic.getBiggest(returntypeIf,
											returntypeElse);
									return WhenOtherwise.create(theStepsIf, theStepsElse, condition,
											returntype);
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

		static final BoxFun<Object, Object> then = new BoxFunImp<Object, Object>("then"){
			// TODO implement
		};

		static final BoxFun<Object, Object> otherwise = new BoxFunImp<Object, Object>("else"){
			// TODO implement
		};

	}
	
	public static Function<?> build(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {

		// return
		// Types.complex.functionCreateImpl(thisNode, thisType);
		// Types.contract.functionCreateImpl(thisNode, thisType);
		// Function.outAllInstances();

		// return Fun.whatEver.functionCreateImpl(thisNode, thisType);
		return Types.contract.functionCreateImpl(thisNode, thisType);

	}

	static Accessible<?>[] getFunctionSteps(CodeNode n, TypeComplex type, BoxFun<?, ?> box)
			throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {

		String name = n.getCommand();
		if(type == null){
			System.err.println("can not recognize type of " + name + " " + n.getParameter(Symbols.paramName()));
			return null;
		}
		List<Accessible<?>> steps = new LinkedList<>();

		List<CodeNode> children = n.getChildNodes();
		for(CodeNode c : children){
			// if (c.isBuildInFunction() &&
			// !c.getCommand().equals(Symbol.FUNCTION)) {

			Accessible<?> v = box.functionCreateChild(c, type);
			if(v != null){
				steps.add(v);
			}
			// }
			// else{
			// System.out.println("check this ...");
			// box.functionCreateChild(c, type);
			// }
		}

		Accessible<?>[] theSteps = new Accessible<?>[steps.size()];
		return steps.toArray(theSteps);
	}

	static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation, UnknownCommandParameter, UnknownCommand {
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

	static void checkAssigmentStructRawType(String ComplexTrgTyp, CodeNode srcNode, Class<?> srcRawType)
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

	static void checkAssigmentNumRawType(Class<?> trgRawType, Class<?> srcRawType, CodeNode srcNode)
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

	static List<AbstractAssigment<?>> __createAssigs(CodeNode n, TypeComplex parentTyp)
			throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
		// String functionComplexName = n.getParameter(Symbols.paramName());
		// functionComplexName = Symbols.getSimpleName(functionComplexName);
		// String parentComplexPath = parentTyp.getName();
		// String path2FunctionComplex = n.getParameter(Symbols.paramType()) +
		// "." + n.getParameter(Symbols.paramName());
		// String path2FunctionComplex = parentComplexPath + "." +
		// functionComplexName;

		String path2FunctionComplex = n.getParameter(Symbols.paramName());
		TypeComplex functionComplex = TypeComplex.getInstance(path2FunctionComplex);

		List<AbstractAssigment<?>> assig = new LinkedList<>();

		for(CodeNode srcNode : n.getChildNodes()){
			String path2TrgParam = srcNode.getParameter(Symbols.paramName()); // n.getName()
																				// +
																				// "."
																				// +
			AccessibleValue<?> trg = AccessibleValue.createFunctionSetableWhatEver(path2TrgParam, functionComplex);

			Accessible<?> src = Assign.whatEverAssigment.functionCreateChild(srcNode.getChildElementByIndex(1), parentTyp);

			if(trg.getRawTypeClass() == Structure.class){
				Attribute<?> trgAttr = functionComplex.getSubAttribute(srcNode.getParameter(Symbols.paramName()));
				String trgTypeName = trgAttr.getType().getName();
				assig.add(createAssigComp(Symbols.comParam(), srcNode, src, trgTypeName, trg));
			}
			else{
				assig.add(createAssig(Symbols.comParam(), srcNode, src, trg));
			}

		}

		return assig;
	}

	static AbstractAssigment<Structure> createAssigComp(String command, CodeNode srcNode, Accessible<?> src, String ComplexTrgNodeTyp, AccessibleValue<?> trg)
			throws AbstractInterpreterException {

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

	static AbstractAssigment<?> createAssig(String command, CodeNode srcNode, Accessible<?> src, AccessibleValue<?> trg) throws AbstractInterpreterException {

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
			System.err.println("createFunctions: no parent for " + thisNode.getParameter(Symbols.paramName()));
			return null;
		}
		if(thisNode == null){
			// TODO throw
			System.err.println("createFunctions: no node ");
			return null;
		}
		String typeName = CodeNode.getTypSubstr(thisNode.getParameter(Symbols.paramName()), parentTyp);
		TypeComplex type = TypeComplex.getInstance(typeName);
		if(type == null){
			// TODO throw
			// TypeComplex.getInstance(parentTyp.getName() + "." +
			// thisNode.getParameter(Symbols.paramName()));
			System.err.println("createFunctions: can not identify " + typeName);
			return null;
		}
		return type;
	}

}
