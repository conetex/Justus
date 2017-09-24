package com.conetex.contract.interpreter.functions;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.FunctionNotFound;
import com.conetex.contract.interpreter.functions.exception.MissingSubOperation;
import com.conetex.contract.interpreter.functions.exception.NoAccessToValue;
import com.conetex.contract.interpreter.functions.exception.OperationInterpreterException;
import com.conetex.contract.interpreter.functions.exception.TypeNotDeterminated;
import com.conetex.contract.interpreter.functions.exception.TypesDoNotMatch;
import com.conetex.contract.interpreter.functions.exception.UnexpectedSubOperation;
import com.conetex.contract.interpreter.functions.exception.UnknownComplexType;
import com.conetex.contract.interpreter.functions.nesting.Box;
import com.conetex.contract.interpreter.functions.nesting.Egg;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.SetableValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.assignment.Creator;
import com.conetex.contract.lang.bool.expression.IsNull;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class Factory {

	private static class Fun {

		static void means() {
			Fun.noReturn.means(Symbol.FUNCTION);
			Fun.structure.means(Symbol.FUNCTION);
			Fun.number.means(Symbol.FUNCTION);
			Fun.bool.means(Symbol.FUNCTION);
			Fun.unknownReturn.means(Symbol.FUNCTION);			
		}
		
		static Box<Object, Object> noReturn = new Box<Object, Object>("voidFunction") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex type) throws OperationInterpreterException {
				Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
				Class<?> returntype = getReturnTyp(theSteps);
				if (returntype == Object.class) {
					Accessible<Object> main = Function.createVoid(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);// TODO
																		// suche
																		// alle
																		// System.err
																		// und werfe
																		// lieber
																		// exception
				return null;
			}
		};

		static Box<Structure, Object> structure = new Box<Structure, Object>("objFunction") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex type) throws OperationInterpreterException {
				Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
				Class<?> returntype = getReturnTyp(theSteps);
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
			public Accessible<? extends Number> create(CodeNode n, Complex type) throws OperationInterpreterException {
				Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
				Class<?> returntype = getReturnTyp(theSteps);
				if (Number.class.isAssignableFrom(returntype)) {
					Accessible<? extends Number> main = Function.createNum(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		static Box<Boolean, Object> bool = new Box<Boolean, Object>("boolFunction") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex type) throws OperationInterpreterException {
				Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
				Class<?> returntype = getReturnTyp(theSteps);
				if (returntype == Boolean.class) {
					Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};

		static Box<Object, Object> unknownReturn = new Box<Object, Object>("whatEverFunction") {
			@Override
			public Accessible<?> create(CodeNode n, Complex type) throws OperationInterpreterException {
				Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
				Class<?> returntype = getReturnTyp(theSteps);
				if (returntype == String.class) {
					return null;
				}
				else if (returntype == Boolean.class) {
					Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
					return main;
				}
				else if (Number.class.isAssignableFrom(returntype)) {
					Accessible<? extends Number> main = Function.createNum(theSteps, n.getName());
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
		
		static Egg<Object> voidCall = new Egg<Object>("voidCall") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue {
				String functionObj = n.getType();//
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

		static Egg<Object> whatEverCall = new Egg<Object>("whatEverCall") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue {
				String functionObj = n.getType();//
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

		static Egg<Structure> objCall = new Egg<Structure>("objCall") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue {
				String functionObj = n.getType();//
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

		static Egg<Number> numberCall = new Egg<Number>("numberCall") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue {
				// CONTROL FUNCTION
				String functionObj = n.getType();//
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

		static Egg<Boolean> boolCall = new Egg<Boolean>("boolCall") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue {
				// CONTROL FUNCTION
				String functionObj = n.getType();//
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

	public static class FunReturn {
		
		static void means() {
			FunReturn.whatEverReturn.means(Symbol.RETURN);
			FunReturn.objReturn.means(Symbol.RETURN);
			FunReturn.numberReturn.means(Symbol.RETURN);
			FunReturn.boolReturn.means(Symbol.RETURN);
			
		}
		
		static Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn") {
			@Override
			public Accessible<?> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Structure, Structure> objReturn = new Box<Structure, Structure>("objReturn") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				Accessible<? extends Structure> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

		static Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				return Return.create(a);
			}
		};

	}

	public static class Access {
		
		public static void means() {
			Access.objRef.means(Symbol.REFERENCE);
			Access.numberRef.means(Symbol.REFERENCE);
			Access.boolRef.means(Symbol.REFERENCE);
			Access.whatEverRef.means(Symbol.REFERENCE);			
		}
		
		static Egg<Object> whatEverRef = new Egg<Object>("whatEverRef") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				return Functions.createFunctionRefWhatEver(n, parentTyp); // parentTyp);
			}
		};

		static Egg<Structure> objRef = new Egg<Structure>("objRef") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				return Functions.createFunctionRef(n, parentTyp, Structure.class); // parentTyp);
			}
		};

		static Egg<Number> numberRef = new Egg<Number>("numberRef") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				return Functions.createFunctionRefNum(n, parentTyp);
			}
		};

		static Egg<Boolean> boolRef = new Egg<Boolean>("boolRef") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				return Functions.createFunctionRef(n, parentTyp, Boolean.class);
			}
		};
	}

	public static class Assign {
		
		public static void means() {
			Assign.whatEverAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.objAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.numberAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
			Assign.boolAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });			
		}
		
		static Box<Structure, Structure> objAssigment = new Box<Structure, Structure>("objAssigment") {
			@Override
			public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<Structure> trg = Functions.createFunctionSetable(trgNode, parentTyp, Structure.class);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				Class<?> srcBaseType = src.getBaseType();
				checkAssigmentStructBaseType(trgNode, srcNode, srcBaseType);
				return Creator.createFromQualifiedTrg(trg, src, n.getTag());
			}
		};

		static Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {

			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<? extends Number> trg = Functions.createFunctionSetableNumber(trgNode, parentTyp);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				Class<?> trgBaseType = trg.getBaseType();
				Class<?> srcBaseType = src.getBaseType();
				checkAssigmentNumBaseType(trgBaseType, srcBaseType, trgNode, srcNode);
				return Creator.createFromQualifiedTrg(trg, src, n.getTag());
			}
		};

		static Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
			@Override
			public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<Boolean> trg = Functions.createFunctionSetable(trgNode, parentTyp, Boolean.class);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);
				check2PartOperations(n, trg, src);
				checkType(src.getBaseType(), Boolean.class);
				return Creator.createFromQualifiedTrg(trg, src, n.getTag());
			}
		};

		static Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				CodeNode trgNode = n.getChildElementByIndex(0);
				SetableValue<?> trg = Functions.createFunctionSetableWhatEver(trgNode, parentTyp);
				CodeNode srcNode = n.getChildElementByIndex(1);
				Accessible<?> src = this.createChild(srcNode, parentTyp);

				check2PartOperations(n, trg, src);

				Class<?> trgBaseType = trg.getBaseType();
				Class<?> srcBaseType = src.getBaseType();
				if (trgBaseType == Boolean.class) {
					checkType(src.getBaseType(), Boolean.class);
					return Creator.createFromUnqualified(trg, src, Boolean.class, n.getTag());
				}
				else if (trgBaseType == Structure.class) {
					checkAssigmentStructBaseType(trgNode, srcNode, srcBaseType);
					return Creator.createFromUnqualified(trg, src, Structure.class, n.getTag());
				}
				else if (trgBaseType == String.class) {
					checkType(src.getBaseType(), String.class);
					return Creator.createFromUnqualified(trg, src, String.class, n.getTag());
				}
				else if (Number.class.isAssignableFrom(trgBaseType)) {
					checkAssigmentNumBaseType(trgBaseType, srcBaseType, trgNode, srcNode);
					return Creator.createFromUnqualified(trg, src, trgBaseType, n.getTag());
				}
				else {
					throw new TypeNotDeterminated("target-Type: " + trgBaseType + ", source-Type: " + srcBaseType);
				}
			}
		};

	}

	private static Box<Structure, Object> complex = new Box<Structure, Object>("complex") {
		@Override
		public Accessible<Structure> create(CodeNode n, Complex type) throws OperationInterpreterException {

			String name = n.getTag();
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
						continue;
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

	public static class Constant {
		
		public static void means() {
			Constant.numberConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG });
			Constant.boolConst.means(Symbol.BOOL);
			Constant.objConst.means(Symbol.STRUCT);
			Constant.whatEverConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG, Symbol.BOOL, Symbol.STRUCT });
		}
		
		static Egg<Structure> objConst = new Egg<Structure>("objConst") {
			@Override
			public Accessible<Structure> create(CodeNode n, Complex parentTyp) {
				Accessible<Structure> reStructure = try2CreateStructureConst(n, parentTyp);
				return reStructure;
			}
		};

		static Egg<Object> whatEverConst = new Egg<Object>("whatEverConst") {
			@Override
			public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws TypeNotDeterminated {
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

		static Egg<Number> numberConst = new Egg<Number>("numberConst") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
				Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
				return re;
			}
		};

		static Egg<Boolean> boolConst = new Egg<Boolean>("boolConst") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
				return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
			}
		};

	}

	public static class Expression {
		
		public static void means() {
			Expression.numberExpession.means(new String[] { Symbol.PLUS, Symbol.MINUS, Symbol.TIMES, Symbol.DIVIDED_BY, Symbol.REMAINS });
			Expression.boolNullCheck.means(Symbol.ISNULL);
			Expression.boolComparsion.means(new String[] { Symbol.SMALLER, Symbol.GREATER, Symbol.EQUAL });
			Expression.boolExpression.means(new String[] { Symbol.AND, Symbol.OR, Symbol.XOR, Symbol.NOT });
		}
		
		static Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
			@Override
			public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				// MATH
				String name = n.getTag();
				Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				Accessible<? extends Number> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
				check2PartOperations(n, a, b);
				Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
				return re;
			}
		};

		static Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				// BOOL
				String name = n.getTag();
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
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				// COMPARISON
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				Accessible<?> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
				check2PartOperations(n, a, b);
				return Functions.createComparison(a, b, n.getTag());
			}
		};

		static Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
			@Override
			public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
				Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				if (n.getChildNodesSize() > 1) {
					CodeNode c = n.getChildElementByIndex(1);
					throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
				}
				;
				return IsNull.create(a);
			}
		};

	}

	public static Class<?> getReturnTyp(Accessible<?>[] theSteps) {
		Class<?> pre = null;
		for (Accessible<?> a : theSteps) {
			if (a instanceof Return) {
				Class<?> c = a.getBaseType();
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

	public static Accessible<?> sbuild(CodeNode n, Complex type) throws OperationInterpreterException {
		Factory x = new Factory();
		return x.build(n, type);
	}

	public Accessible<?> build(CodeNode n, Complex type) throws OperationInterpreterException {

		Fun.means();
		FunCall.means();
		FunReturn.means();
		Access.means();
		Assign.means();
		Constant.means();
		Expression.means();



		Expression.numberExpession.contains(Expression.numberExpession);
		Expression.numberExpession.contains(Constant.numberConst);
		Expression.numberExpession.contains(FunCall.numberCall);
		Expression.numberExpession.contains(Access.numberRef);

		Expression.boolComparsion.contains(Expression.numberExpession);
		Expression.boolComparsion.contains(Constant.numberConst);
		Expression.boolComparsion.contains(FunCall.numberCall);
		Expression.boolComparsion.contains(Access.numberRef);

		Expression.boolExpression.contains(Expression.boolExpression);
		Expression.boolExpression.contains(Access.boolRef);
		Expression.boolExpression.contains(FunCall.boolCall);
		Expression.boolExpression.contains(Constant.boolConst);
		Expression.boolExpression.contains(Expression.boolNullCheck);
		Expression.boolExpression.contains(Expression.boolComparsion);

		
		
		
		
		
		Assign.whatEverAssigment.contains(FunCall.whatEverCall);
		Assign.whatEverAssigment.contains(Access.whatEverRef);
		Assign.whatEverAssigment.contains(Constant.whatEverConst);
		Assign.whatEverAssigment.contains(Expression.numberExpession);
		Assign.whatEverAssigment.contains(Expression.boolExpression);
		Assign.whatEverAssigment.contains(Expression.boolComparsion);
		Assign.whatEverAssigment.contains(Expression.boolNullCheck);

		Assign.objAssigment.contains(FunCall.objCall);
		Assign.objAssigment.contains(Access.objRef);
		Assign.objAssigment.contains(Constant.objConst);

		Assign.numberAssigment.contains(FunCall.numberCall);
		Assign.numberAssigment.contains(Access.numberRef);
		Assign.numberAssigment.contains(Constant.numberConst);
		Assign.numberAssigment.contains(Expression.numberExpession);

		Assign.boolAssigment.contains(FunCall.boolCall);
		Assign.boolAssigment.contains(Access.boolRef);
		Assign.boolAssigment.contains(Constant.boolConst);
		Assign.boolAssigment.contains(Expression.boolExpression);
		Assign.boolAssigment.contains(Expression.boolComparsion);
		Assign.boolAssigment.contains(Expression.boolNullCheck);

		
		
		
		
		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.contains(Access.whatEverRef);
		FunReturn.whatEverReturn.contains(Constant.whatEverConst);
		FunReturn.whatEverReturn.contains(Expression.numberExpession);
		FunReturn.whatEverReturn.contains(Expression.boolExpression);
		FunReturn.whatEverReturn.contains(Expression.boolComparsion);
		FunReturn.whatEverReturn.contains(Expression.boolNullCheck);

		FunReturn.objReturn.contains(FunCall.objCall);
		FunReturn.objReturn.contains(Access.objRef);
		FunReturn.objReturn.contains(Constant.objConst);

		FunReturn.numberReturn.contains(FunCall.numberCall);
		FunReturn.numberReturn.contains(Access.numberRef);
		FunReturn.numberReturn.contains(Constant.numberConst);
		FunReturn.numberReturn.contains(Expression.numberExpession);

		FunReturn.boolReturn.contains(FunCall.boolCall);
		FunReturn.boolReturn.contains(Access.boolRef);
		FunReturn.boolReturn.contains(Constant.boolConst);
		FunReturn.boolReturn.contains(Expression.boolExpression);
		FunReturn.boolReturn.contains(Expression.boolComparsion);
		FunReturn.boolReturn.contains(Expression.boolNullCheck);

		
		Fun.noReturn.contains(Assign.whatEverAssigment);
		Fun.noReturn.contains(FunCall.whatEverCall);
		Fun.noReturn.contains(Expression.numberExpession);// TODO eigentlich nur hinter Zuweisung
		Fun.noReturn.contains(Expression.boolExpression);// TODO eigentlich nur hinter Zuweisung
		Fun.noReturn.contains(Expression.boolComparsion);// TODO eigentlich nur hinter Zuweisung

		Fun.structure.contains(Assign.whatEverAssigment);
		Fun.structure.contains(FunCall.whatEverCall);
		Fun.structure.contains(FunReturn.objReturn);
		Fun.structure.contains(Access.numberRef);// TODO eigentlich nur hinter Zuweisung
		Fun.structure.contains(Expression.numberExpession);// TODO eigentlich nur hinter Zuweisung
		Fun.structure.contains(Expression.boolComparsion);// TODO eigentlich nur hinter Zuweisung
		Fun.structure.contains(Expression.boolExpression);// TODO eigentlich nur hinter Zuweisung

		Fun.number.contains(Assign.whatEverAssigment);
		Fun.number.contains(FunCall.whatEverCall);
		Fun.number.contains(FunReturn.numberReturn);

		Fun.bool.contains(Assign.whatEverAssigment);
		Fun.bool.contains(FunCall.whatEverCall);
		Fun.bool.contains(FunReturn.boolReturn);

		Fun.unknownReturn.contains(Assign.whatEverAssigment);
		Fun.unknownReturn.contains(FunCall.whatEverCall);
		Fun.unknownReturn.contains(FunReturn.whatEverReturn);
		Fun.unknownReturn.contains(Expression.numberExpession);// TODO eigentlich nur hinter Zuweisung
		Fun.unknownReturn.contains(Expression.boolComparsion);// TODO eigentlich nur hinter Zuweisung
		Fun.unknownReturn.contains(Expression.boolExpression);// TODO eigentlich nur hinter Zuweisung

		complex.means("complexType");
		complex.contains(Fun.unknownReturn);
		complex.contains(complex);

		//
		complex.create(n, type);
		return Fun.unknownReturn.create(n, type);

	}

	public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box box) throws OperationInterpreterException {

		String name = n.getTag();
		if (type == null) {
			System.err.println("can not recognize type of " + name + " " + n.getName());
			return null;
		}
		System.out.println("createFunction " + name + " " + n.getName());
		List<Accessible<?>> steps = new LinkedList<Accessible<?>>();

		List<CodeNode> children = n.getChildNodes();
		for (CodeNode c : children) {
			if (c.isBuildInFunction() && !c.getTag().equals(Symbol.FUNCTION)) {
				System.out.println("createBuild " + c.getTag() + " - " + c.getName());
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

	private static Accessible<? extends Number> try2CreateNumConst(CodeNode n, Complex parentTyp) {
		String name = n.getTag();
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

	private static Accessible<Boolean> try2CreateBoolConst(CodeNode n, Complex parentTyp) {
		String name = n.getTag();
		if (name.equals(Symbol.BOOL)) {
			return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
		}
		return null;
	}

	private static Accessible<Structure> try2CreateStructureConst(CodeNode n, Complex parentTyp) {
		String name = n.getTag();
		if (name.equals(Symbol.STRUCT)) {
			return AccessibleConstant.<Structure>create2(Structure.class, n.getValue());
		}
		return null;
	}

	private static void check1PartOperations(CodeNode n, Accessible<?> a) throws MissingSubOperation, UnexpectedSubOperation {
		if (a == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 1) {
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
		}
		;
	}

	private static void check2PartOperations(CodeNode n, Accessible<?> trg, Accessible<?> src) throws MissingSubOperation, UnexpectedSubOperation {
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
		;
	}

	private static void checkAssigmentStructBaseType(CodeNode trgNode, CodeNode srcNode, Class<?> srcBaseType) throws TypesDoNotMatch, UnknownComplexType {
		if (srcBaseType != Structure.class) {
			throw new TypesDoNotMatch(srcBaseType.toString(), Structure.class.toString());
		}

		Complex trgComplexType = Complex.getInstance(trgNode.getType());
		if (trgComplexType == null) {
			throw new UnknownComplexType(trgNode.getType() + "(" + trgNode.getTag() + " - " + trgNode.getName() + ")");
		}
		Complex srcComplexType = Complex.getInstance(srcNode.getType());
		if (srcComplexType == null) {
			throw new UnknownComplexType(srcNode.getType() + "(" + srcNode.getTag() + " - " + srcNode.getName() + ")");
		}
		if (trgComplexType != srcComplexType) {
			throw new TypesDoNotMatch(trgNode.getType() + "(" + trgNode.getTag() + " - " + trgNode.getName() + ")",
					srcNode.getType() + "(" + srcNode.getTag() + " - " + srcNode.getName() + ")");
		}
	}

	private static void checkAssigmentNumBaseType(Class<?> trgBaseType, Class<?> srcBaseType, CodeNode trgNode, CodeNode srcNode) throws TypesDoNotMatch {
		Class<?> baseType;
		if (trgBaseType == srcBaseType) {
			baseType = trgBaseType;
		}
		else {
			baseType = ElementaryArithmetic.getBiggest(trgBaseType, srcBaseType);
			if (baseType == srcBaseType) {
				throw new TypesDoNotMatch(trgBaseType + "(" + trgNode.getTag() + " - " + trgNode.getName() + ") > " + srcBaseType + "(" + srcNode.getTag()
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
