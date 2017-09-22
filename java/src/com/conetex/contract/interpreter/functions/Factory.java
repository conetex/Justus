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
import com.conetex.contract.interpreter.functions.exception.UnknownType;
import com.conetex.contract.interpreter.functions.nesting.Box;
import com.conetex.contract.interpreter.functions.nesting.Egg;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.Setable;
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

	private static class Fun{
		
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
				} else if (returntype == Boolean.class) {
					Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
					return main;
				} else if (Number.class.isAssignableFrom(returntype)) {
					Accessible<? extends Number> main = Function.createNum(theSteps, n.getName());
					return main;
				} else if (returntype == Structure.class) {
					Accessible<? extends Structure> main = Function.createStructure(theSteps, n.getName());
					return main;
				} else if (returntype == Object.class) {
					Accessible<Object> main = Function.createVoid(theSteps, n.getName());
					return main;
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
		};
		
	}
	
	private static class CallBoxes{
		
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
					} else {
						// createFunctions(c, ctype);
						this.createChild(c, ctype);
					}
				}
			}
			return null;
		}
	};



	public static Class<?> getReturnTyp(Accessible<?>[] theSteps) {
		Class<?> pre = null;
		for (Accessible<?> a : theSteps) {
			if (a instanceof Return) {
				Class<?> c = a.getBaseType();
				if (pre != null) {
					if (pre != c) {
						if (Number.class.isAssignableFrom(c)) {
							pre = ElementaryArithmetic.getBiggest(pre, c);
						} else {
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



	Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn") {
		@Override
		public Accessible<?> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
			return Return.create(a);
		}
	};

	Box<Structure, Structure> objReturn = new Box<Structure, Structure>("objReturn") {
		@Override
		public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
			Accessible<? extends Structure> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
			return Return.create(a);
		}
	};

	Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
		@Override
		public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
			return Return.create(a);
		}
	};

	Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
		@Override
		public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
			Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
			return Return.create(a);
		}
	};

	Egg<Object> voidCall = new Egg<Object>("voidCall") {
		@Override
		public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
				throws FunctionNotFound, NoAccessToValue {
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

	Egg<Object> whatEverCall = new Egg<Object>("whatEverCall") {
		@Override
		public Accessible<? extends Object> create(CodeNode n, Complex parentTyp)
				throws FunctionNotFound, NoAccessToValue {
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

	Egg<Structure> objCall = new Egg<Structure>("objCall") {
		@Override
		public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
				throws FunctionNotFound, NoAccessToValue {
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

	Egg<Number> numberCall = new Egg<Number>("numberCall") {
		@Override
		public Accessible<? extends Number> create(CodeNode n, Complex parentTyp)
				throws FunctionNotFound, NoAccessToValue {
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

	Egg<Boolean> boolCall = new Egg<Boolean>("boolCall") {
		@Override
		public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
				throws FunctionNotFound, NoAccessToValue {
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

	Egg<Object> whatEverRef = new Egg<Object>("whatEverRef") {
		@Override
		public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			return Functions.createFunctionRefWhatEver(n, parentTyp); // parentTyp);
		}
	};

	Egg<Structure> objRef = new Egg<Structure>("objRef") {
		@Override
		public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
			return Functions.createFunctionRef(n, parentTyp, Structure.class); // parentTyp);
		}
	};

	Egg<Number> numberRef = new Egg<Number>("numberRef") {
		@Override
		public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			return Functions.createFunctionRefNum(n, parentTyp);
		}
	};

	Egg<Boolean> boolRef = new Egg<Boolean>("boolRef") {
		@Override
		public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
			return Functions.createFunctionRef(n, parentTyp, Boolean.class);
		}
	};

	Box<Structure, Structure> objAssigment = new Box<Structure, Structure>("objAssigment") {
		@Override
		public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
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

	Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {

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

	Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
		@Override
		public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp)
				throws OperationInterpreterException {
			CodeNode trgNode = n.getChildElementByIndex(0);
			SetableValue<Boolean> trg = Functions.createFunctionSetable(trgNode, parentTyp, Boolean.class);
			CodeNode srcNode = n.getChildElementByIndex(1);
			Accessible<?> src = this.createChild(srcNode, parentTyp);
			check2PartOperations(n, trg, src);
			checkType(src.getBaseType(), Boolean.class);
			return Creator.createFromQualifiedTrg(trg, src, n.getTag());
		}
	};

	Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment") {
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
			} else if (trgBaseType == Structure.class) {
				checkAssigmentStructBaseType(trgNode, srcNode, srcBaseType);
				return Creator.createFromUnqualified(trg, src, Structure.class, n.getTag());
			} else if (trgBaseType == String.class) {
				checkType(src.getBaseType(), String.class);
				return Creator.createFromUnqualified(trg, src, String.class, n.getTag());
			} else if (Number.class.isAssignableFrom(trgBaseType)) {
				checkAssigmentNumBaseType(trgBaseType, srcBaseType, trgNode, srcNode);
				return Creator.createFromUnqualified(trg, src, trgBaseType, n.getTag());
			} else {
				throw new TypeNotDeterminated("target-Type: " + trgBaseType + ", source-Type: " + srcBaseType);
			}
		}
	};

	Egg<Structure> objConst = new Egg<Structure>("objConst") {
		@Override
		public Accessible<Structure> create(CodeNode n, Complex parentTyp) {
			Accessible<Structure> reStructure = try2CreateStructureConst(n, parentTyp);
			return reStructure;
		}
	};

	Egg<Object> whatEverConst = new Egg<Object>("whatEverConst") {
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

	Egg<Number> numberConst = new Egg<Number>("numberConst") {
		@Override
		public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
			Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
			return re;
		}
	};

	Egg<Boolean> boolConst = new Egg<Boolean>("boolConst") {
		@Override
		public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
			return AccessibleConstant.<Boolean> create2(Boolean.class, n.getValue());
		}
	};

	Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
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

	Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
		@Override
		public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			// BOOL
			String name = n.getTag();
			if (name.equals(Symbol.NOT)) {
				Accessible<? extends Boolean> sub = this.createChild(n.getChildElementByIndex(0), parentTyp);
				check1PartOperations(n, sub);
				return Not.create(sub);
			} else {
				Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
				Accessible<? extends Boolean> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
				check2PartOperations(n, a, b);
				return Binary.create(a, b, name);
			}
		}
	};

	Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion") {
		@Override
		public Accessible<Boolean> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
			// COMPARISON
			Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
			Accessible<?> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
			check2PartOperations(n, a, b);
			return Functions.createComparison(a, b, n.getTag());
		}
	};

	Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
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

	public static Accessible<?> sbuild(CodeNode n, Complex type) throws OperationInterpreterException {
		Factory x = new Factory();
		return x.build(n, type);
	}

	public Accessible<?> build(CodeNode n, Complex type) throws OperationInterpreterException {

		objRef.means(Symbol.REFERENCE);
		numberRef.means(Symbol.REFERENCE);
		boolRef.means(Symbol.REFERENCE);
		whatEverRef.means(Symbol.REFERENCE);

		objCall.means(Symbol.CALL);
		numberCall.means(Symbol.CALL);
		boolCall.means(Symbol.CALL);
		voidCall.means(Symbol.CALL);
		whatEverCall.means(Symbol.CALL);

		numberConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG });
		boolConst.means(Symbol.BOOL);
		objConst.means(Symbol.STRUCT);
		whatEverConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG, Symbol.BOOL, Symbol.STRUCT });

		numberExpession
				.means(new String[] { Symbol.PLUS, Symbol.MINUS, Symbol.TIMES, Symbol.DIVIDED_BY, Symbol.REMAINS });
		numberExpession.contains(numberExpession);
		numberExpession.contains(numberConst);
		numberExpession.contains(numberCall);
		numberExpession.contains(numberRef);

		boolComparsion.means(new String[] { Symbol.SMALLER, Symbol.GREATER, Symbol.EQUAL });
		boolComparsion.contains(numberExpession);
		boolComparsion.contains(numberConst);
		boolComparsion.contains(numberCall);
		boolComparsion.contains(numberRef);

		boolNullCheck.means(Symbol.ISNULL);

		boolExpression.means(new String[] { Symbol.AND, Symbol.OR, Symbol.XOR, Symbol.NOT });
		boolExpression.contains(boolComparsion);
		boolExpression.contains(boolRef);
		boolExpression.contains(boolCall);
		boolExpression.contains(boolConst);
		boolExpression.contains(boolNullCheck);
		boolExpression.contains(boolExpression);

		whatEverAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		whatEverAssigment.contains(whatEverCall);
		whatEverAssigment.contains(whatEverRef);
		whatEverAssigment.contains(whatEverConst);
		whatEverAssigment.contains(numberExpession);
		whatEverAssigment.contains(boolExpression);
		whatEverAssigment.contains(boolComparsion);
		whatEverAssigment.contains(boolNullCheck);		
		
		objAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		objAssigment.contains(objCall);
		objAssigment.contains(objRef);
		objAssigment.contains(objConst);

		numberAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		numberAssigment.contains(numberCall);
		numberAssigment.contains(numberRef);
		numberAssigment.contains(numberConst);
		numberAssigment.contains(numberExpession);

		boolAssigment.means(new String[] { Symbol.COPY, Symbol.REFER });
		boolAssigment.contains(boolCall);
		boolAssigment.contains(boolRef);
		boolAssigment.contains(boolConst);
		boolAssigment.contains(boolExpression);
		boolAssigment.contains(boolComparsion);
		boolAssigment.contains(boolNullCheck);

		whatEverReturn.means(Symbol.RETURN);
		// whatEverReturn.contains(whatEverCall);
		whatEverReturn.contains(whatEverRef);
		whatEverReturn.contains(whatEverConst);
		whatEverReturn.contains(numberExpession);
		whatEverReturn.contains(boolExpression);
		whatEverReturn.contains(boolComparsion);
		whatEverReturn.contains(boolNullCheck);

		objReturn.means(Symbol.RETURN);
		objReturn.contains(objCall);
		objReturn.contains(objRef);
		objReturn.contains(objConst);

		numberReturn.means(Symbol.RETURN);
		numberReturn.contains(numberCall);
		numberReturn.contains(numberRef);
		numberReturn.contains(numberConst);
		numberReturn.contains(numberExpession);

		boolReturn.means(Symbol.RETURN);
		boolReturn.contains(boolCall);
		boolReturn.contains(boolRef);
		boolReturn.contains(boolConst);
		boolReturn.contains(boolExpression);
		boolReturn.contains(boolComparsion);
		boolReturn.contains(boolNullCheck);

		Fun.noReturn.means(Symbol.FUNCTION);
		Fun.noReturn.contains(whatEverAssigment);
		Fun.noReturn.contains(whatEverCall);
		Fun.noReturn.contains(numberExpession);// TODO eigentlich nur hinter Zuweisung
		Fun.noReturn.contains(boolExpression);// TODO eigentlich nur hinter Zuweisung
		Fun.noReturn.contains(boolComparsion);// TODO eigentlich nur hinter Zuweisung

		Fun.structure.means(Symbol.FUNCTION);
		Fun.structure.contains(whatEverAssigment);
		Fun.structure.contains(whatEverCall);
		Fun.structure.contains(objReturn);
		Fun.structure.contains(numberRef);// TODO eigentlich nur hinter Zuweisung
		Fun.structure.contains(numberExpession);// TODO eigentlich nur hinter	Zuweisung
		Fun.structure.contains(boolComparsion);// TODO eigentlich nur hinter Zuweisung
		Fun.structure.contains(boolExpression);// TODO eigentlich nur hinter Zuweisung

		Fun.number.means(Symbol.FUNCTION);
		Fun.number.contains(whatEverAssigment);
		Fun.number.contains(whatEverCall);
		Fun.number.contains(numberReturn);

		Fun.bool.means(Symbol.FUNCTION);
		Fun.bool.contains(whatEverAssigment);
		Fun.bool.contains(whatEverCall);
		Fun.bool.contains(boolReturn);

		Fun.unknownReturn.means(Symbol.FUNCTION);
		Fun.unknownReturn.contains(whatEverAssigment);
		Fun.unknownReturn.contains(whatEverCall);
		Fun.unknownReturn.contains(whatEverReturn);
		Fun.unknownReturn.contains(numberExpession);// TODO eigentlich nur hinter	Zuweisung
		Fun.unknownReturn.contains(boolComparsion);// TODO eigentlich nur hinter Zuweisung
		Fun.unknownReturn.contains(boolExpression);// TODO eigentlich nur hinter Zuweisung
		
		complex.means("complexType");
		complex.contains(Fun.unknownReturn);
		complex.contains(complex);

		//
		complex.create(n, type);
		return Fun.unknownReturn.create(n, type);

	}

	public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box box)
			throws OperationInterpreterException {

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
			return AccessibleConstant.<BigInteger> create2(BigInteger.class, n.getValue());
		} else if (name.equals(Symbol.INT)) {
			return AccessibleConstant.<Integer> create2(Integer.class, n.getValue());
		} else if (name.equals(Symbol.LNG)) {
			return AccessibleConstant.<Long> create2(Long.class, n.getValue());
		}
		return null;
	}

	private static Accessible<Boolean> try2CreateBoolConst(CodeNode n, Complex parentTyp) {
		String name = n.getTag();
		if (name.equals(Symbol.BOOL)) {
			return AccessibleConstant.<Boolean> create2(Boolean.class, n.getValue());
		}
		return null;
	}

	private static Accessible<Structure> try2CreateStructureConst(CodeNode n, Complex parentTyp) {
		String name = n.getTag();
		if (name.equals(Symbol.STRUCT)) {
			return AccessibleConstant.<Structure> create2(Structure.class, n.getValue());
		}
		return null;
	}

	private static void check1PartOperations(CodeNode n, Accessible<?> a)
			throws MissingSubOperation, UnexpectedSubOperation {
		if (a == null) {
			throw new MissingSubOperation("");
		}
		if (n.getChildNodesSize() > 1) {
			CodeNode c = n.getChildElementByIndex(1);
			throw new UnexpectedSubOperation("Operation not allowed: " + c.getName());
		}
		;
	}

	private static void check2PartOperations(CodeNode n, Accessible<?> trg, Accessible<?> src)
			throws MissingSubOperation, UnexpectedSubOperation {
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

	private static void checkAssigmentStructBaseType(CodeNode trgNode, CodeNode srcNode, Class<?> srcBaseType)
			throws TypesDoNotMatch, UnknownComplexType {
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

	private static void checkAssigmentNumBaseType(Class<?> trgBaseType, Class<?> srcBaseType, CodeNode trgNode,
			CodeNode srcNode) throws TypesDoNotMatch {
		Class<?> baseType;
		if (trgBaseType == srcBaseType) {
			baseType = trgBaseType;
		} else {
			baseType = ElementaryArithmetic.getBiggest(trgBaseType, srcBaseType);
			if (baseType == srcBaseType) {
				throw new TypesDoNotMatch(trgBaseType + "(" + trgNode.getTag() + " - " + trgNode.getName() + ") > "
						+ srcBaseType + "(" + srcNode.getTag() + " - " + srcNode.getName() + ")");
			}
		}
	}

	public static void checkType(Class<?> type, Class<?> expected) throws TypesDoNotMatch {
		if (type != expected) {
			throw new TypesDoNotMatch(
					type.getName() + " is not " + expected.getName());
		}
	}

}
