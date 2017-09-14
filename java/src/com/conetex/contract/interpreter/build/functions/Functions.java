package com.conetex.contract.interpreter.build.functions;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Node;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleAbstract;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.SetableValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.assignment.Copy;
import com.conetex.contract.lang.assignment.Reference;
import com.conetex.contract.lang.bool.expression.ComparisonNumber;
import com.conetex.contract.lang.bool.expression.ComparisonString;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class Functions {

	public static abstract class FunctionBuilder {
		protected Node node;

		protected FunctionBuilder(Node n) {
			this.node = n;
		}

		public abstract Accessible<?> build(Complex d);
	}

	public static List<AccessibleAbstract<?>> createFunctions(SyntaxNode n, Complex type) {
		String name = n.getTag();
		if (type == null) {
			System.err.println("can not recognize type of " + name);
			return null;
		}

		List<AccessibleAbstract<?>> acc = new LinkedList<AccessibleAbstract<?>>();

		List<SyntaxNode> children = n.getChildNodes();
		for (SyntaxNode c : children) {
			// for(int i = 0; i < children.getLength(); i++){
			// Node c = children.item(i);
			if (c.isBuildInFunction()) {

				System.out.println("createFunctions " + c.getTag() + " - " + c.getName());
				AccessibleAbstract<?> v = createFunction(c, type);
				if (v != null) {
					acc.add(v);
				}
			}

			if (c.isType()) {
				String cname = c.getName();// ReadXMLtools.getAttribute(c, Symbol.TYPE_NAME);
				/*
				 * Identifier<?> cid = type.getSubIdentifier(cname); // if(cid == null){
				 * System.err.println("createFunctions: can not identify " + cname); continue; }
				 * AbstractType<?> ctype = cid.getType(); if( ctype.getClass() == Complex.class
				 * ){ createFunctions(c, (Complex) ctype); }
				 */
				Complex ctype = Complex.getInstance(type.getName() + "." + cname);
				if (ctype == null) {
					System.err.println("createFunctions: can not identify " + type.getName() + "." + cname);
					continue;
				} else {
					createFunctions(c, ctype);
				}
			}
		}

		return acc;
	}

	public static AccessibleAbstract<?> createFunction(SyntaxNode n, Complex parentTyp// , List<FunctionBuilder>
																				// functionBuilders//, List<Value<?>>
																				// values, Map<Complex,
																				// List<FunctionBuilder>> complexTyps
	) {

		String name = n.getTag();

		if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
				|| name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
			AccessibleAbstract<? extends Number> x = // createNumExpression( super.node, theClass );
					// createFunctionAccessible( n, parentTyp, Integer.class );// TODO mach das zu
					// number ...
					createFunctionAccessibleNum(n, parentTyp);
			return x;
		} else if (name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR)
				|| name.equals(Symbol.NOT) || name.equals(Symbol.SMALLER) || name.equals(Symbol.EQUAL)
				|| name.equals(Symbol.GREATER) || name.equals(Symbol.ISNULL)) {
			AccessibleAbstract<Boolean> x = // createBoolExpression( super.node );
					// ReadXML.createAccessible( super.node, c, Boolean.class );
					createFunctionAccessibleBool(n, parentTyp);
			return x;
		} else if (name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY)) {
			AccessibleAbstract<?> x = createFunctionAccessibleObj(n, parentTyp);
			return x;
		} else if (name.equals(Symbol.FUNCTION) || name.equals(Symbol.RETURN) || name.equals(Symbol.CALL)) {
			AccessibleAbstract<?> x = // createBoolExpression( super.node );
					createFunctionAccessibleObj(n, parentTyp);
			return x;
		}

		return null;

	}

	public static AccessibleAbstract<?> createFunctionAccessibleObj(SyntaxNode n, Complex parentTyp) {

		String name = n.getTag();

		// MATH
		if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
				|| name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
			return createFunctionAccessibleNum(n, parentTyp);
		}
		// VARIABLE
		else if (name.equals(Symbol.BINT)) {
			return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
		} else if (name.equals(Symbol.INT)) {
			return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
		} else if (name.equals(Symbol.LNG)) {
			return AccessibleConstant.<Long>create2(Long.class, n.getValue());
		}
		// CONTROL FUNCTION
		else if (name.equals(Symbol.RETURN)) {
			AccessibleAbstract<?> e = createFunctionAccessibleObj(n.getChildElementByIndex(0), parentTyp);
			return Return.create(e);
		} else if (name.equals(Symbol.CALL)) {
			String functionObj = n.getType();//
			AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);

			String functionName = n.getName();
			AccessibleAbstract<?> e = Function.getInstance(functionName);
			// Class<?> expectedBaseTypExp = e.getBaseType();
			return Call.create(e, re);
			// return ReadXML.createAccessible( e, parentTyp, expectedBaseTypExp );
		} else if (name.equals(Symbol.FUNCTION)) {
			String functionName = n.getName();
			String idTypeName = n.getType();
			Complex complexType = Complex.getInstance(idTypeName);
			if (complexType != null) {
				List<AccessibleAbstract<?>> steps = createFunctions(n, complexType);
				AccessibleAbstract<?>[] theSteps = new AccessibleAbstract<?>[steps.size()];
				// TODO hier return typ checken und Bool / Obj creator aufrufen...
				Function.createNum(steps.toArray(theSteps), functionName);
				return null; // das ist wichtig. wir wollen functions nur per call aufrufen!
			}
		}
		// ASSIGNMENT
		else if (name.equals("copy") || name.equals("refer")) {
			// createAssignment(name, n, parentTyp);
			SyntaxNode c0 = n.getChildElementByIndex(0);
			System.out.println(c0.getValue());
			Attribute<?> id0 = parentTyp.getSubAttribute(c0.getValue());// TODO geht nich

			SyntaxNode c1 = n.getChildElementByIndex(1);
			System.out.println(c1.getValue());
			Attribute<?> id1 = parentTyp.getSubAttribute(c1.getValue());

			String c0DataType = c0.getType();
			if (c0DataType != null) {
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht
				// zum typ des referenzierten Feld passt ...
				// Class<Value<Object>> c0ClassX = Primitive.getClass(c0DataType);
				Class<?> c0ClassX = Primitive.getClass(c0DataType);
			}

			// TODO: der teil hier könnte ausgelagert werden nach AbstractAssigment

			Class<? extends Value<?>> c0Class = null;
			if (id0 != null) {
				AbstractType<?> t = id0.getType();
				c0Class = t.getClazz();
			}
			Class<? extends Value<?>> c1Class = null;
			if (id1 != null) {
				AbstractType<?> t = id1.getType();
				c1Class = t.getClazz();
			}

			// TODO hier reicht eigentlich gleicher base-typ !!!
			if (c0Class != null && c1Class != null && c0Class == c1Class) {
				String path0 = c0.getValue();
				SetableValue<?> trg = SetableValue.create(path0, c0Class);

				Primitive<?> pri1 = Primitive.getInstance(c1Class);
				Class<?> baseType1 = pri1.getBaseType();
				AccessibleAbstract<?> src = createFunctionAccessibleObj(c1, parentTyp);
				if (src != null && trg != null) {
					if (name.equals(Symbol.COPY)) {
						return Copy.create(trg, src);// TODO dahinter liegt ein Cast! das geht schöner...
					}
					if (name.equals(Symbol.REFERENCE)) {
						return Reference.create(trg, src);
					}
				}
				return null;
			} else {
				// TODO: classes do not match
				return null;
			}
		}
		// REFERENCE
		else if (name.equals(Symbol.REFERENCE)) {
			return createFunctionRefObj(n, parentTyp);
		}

		return null;
	}

	public static AccessibleAbstract<? extends Number> createFunctionAccessibleNum(SyntaxNode n, Complex parentTyp) {

		String name = n.getTag();

		// MATH
		if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
				|| name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
			AccessibleAbstract<? extends Number> a = createFunctionAccessibleNum(n.getChildElementByIndex(0), parentTyp);
			AccessibleAbstract<? extends Number> b = createFunctionAccessibleNum(n.getChildElementByIndex(1), parentTyp);
			// TODO check 4 other childs! only 2 are alowed
			if (a != null && b != null) {
				AccessibleAbstract<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
				return re;
			}
		}
		// VARIABLE
		else if (name.equals(Symbol.BINT)) {
			return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
		} else if (name.equals(Symbol.INT)) {
			return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
		} else if (name.equals(Symbol.LNG)) {
			return AccessibleConstant.<Long>create2(Long.class, n.getValue());
		}
		// CONTROL FUNCTION
		/*
		 * else if( name.equals(Symbol.FUNCTION) ) { String functionName = n.getName();
		 * String idTypeName = n.getType(); Complex complexType =
		 * Complex.getInstance(idTypeName); if(complexType != null){ List<Accessible<?>>
		 * steps = createFunctions(n, complexType); Accessible<?>[] theSteps = new
		 * Accessible<?>[ steps.size() ]; FunctionNew.createNum( steps.toArray( theSteps
		 * ), functionName ); return null; // das ist wichtig. wir wollen functions nur
		 * per call aufrufen! } } else if( name.equals(Symbol.RETURN) ) { Accessible<?
		 * extends Number> e = createFunctionAccessibleNum( n.getChildElementByIndex(0),
		 * parentTyp ); return Return.create(e); }
		 */
		else if (name.equals(Symbol.CALL)) {
			String functionObj = n.getType();//
			AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
			// TODO wozu das hier? müsste das nicht von createValue angelegt worden sein?
			// TODO Exception FunktionsDaten nicht da...
			String functionName = n.getName();
			AccessibleAbstract<? extends Number> e = Function.getInstanceNum(functionName);
			// TODO Exception Funktion nicht da...
			return Call.create(e, re);
		}
		// REFERENCE
		else if (name.equals(Symbol.REFERENCE)) {
			return createFunctionRefNum(n, parentTyp);
		}

		return null;
	}

	public static AccessibleAbstract<Boolean> createFunctionAccessibleBool(SyntaxNode n, Complex parentTyp) {

		String name = n.getTag();
		// COMPARISON
		if (name.equals(Symbol.SMALLER) || name.equals(Symbol.GREATER) || name.equals(Symbol.EQUAL)) {
			AccessibleAbstract<?> a = createFunctionAccessibleObj(n.getChildElementByIndex(0), parentTyp);
			AccessibleAbstract<?> b = createFunctionAccessibleObj(n.getChildElementByIndex(1), parentTyp);
			// TODO check 4 other childs! only 2 are alowed
			if (a != null && b != null) {
				AccessibleAbstract<Boolean> re = createComparison(a, b, name);
				return re;
			}
		}
		// BOOL
		else if (name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR)) {
			AccessibleAbstract<Boolean> a = createFunctionAccessibleBool(n.getChildElementByIndex(0), parentTyp);
			AccessibleAbstract<Boolean> b = createFunctionAccessibleBool(n.getChildElementByIndex(1), parentTyp);
			if (a != null && b != null) {
				return Binary.create(a, b, name);
			}
		} else if (name.equals(Symbol.NOT)) {
			AccessibleAbstract<Boolean> sub = createFunctionAccessibleBool(n.getChildElementByIndex(0), parentTyp);
			if (sub != null) {
				return Not.create(sub);
			}
		} else if (name.equals(Symbol.ISNULL)) {
			// TODO

		}
		// VARIABLE
		else if (name.equals(Symbol.BOOL)) {
			return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
		}
		// CONTROL FUNCTION
		else if (name.equals(Symbol.CALL)) {
			String functionObj = n.getType();//
			AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
			// TODO wozu das hier? müsste das nicht von createValue angelegt worden sein?
			// TODO Exception FunktionsDaten nicht da...
			String functionName = n.getName();
			AccessibleAbstract<Boolean> e = Function.getInstanceBool(functionName);
			// TODO Exception Funktion nicht da...
			return Call.create(e, re);
		}
		// REFERENCE
		else if (name.equals(Symbol.REFERENCE)) {
			return createFunctionRefBool(n, parentTyp);
		}

		return null;
	}

	public static AccessibleAbstract<?> createFunctionRefObj(SyntaxNode n, Complex parentTyp) {
		String name = n.getTag();
		System.out.println("get_id from " + name + " (" + n.getValue() + ")");
		Attribute<?> id = getID(n, parentTyp);
		if (id != null) {
			Class<?> baseType = getBaseType(id);
			if (baseType == null) {
				System.err.println("ERR: can not get type of '" + n.getValue() + "'");
				return null;
			} else {
				String path = n.getValue();
				AccessibleValue<?> re = AccessibleValue.create(path, baseType);
				return re;
			}
		} else {
			System.err.println("ERR: can not find '" + n.getValue() + "'");
		}
		return null;
	}

	public static AccessibleAbstract<Boolean> createFunctionRefBool(SyntaxNode n, Complex parentTyp) {
		String name = n.getTag();
		System.out.println("get_id from " + name + " (" + n.getValue() + ")");
		Attribute<?> id = getID(n, parentTyp);
		if (id != null) {
			Class<?> baseType = getBaseType(id);
			if (baseType == null) {
				System.err.println("ERR: can not get type of '" + n.getValue() + "'");
				return null;
			} else if (baseType == Boolean.class) {
				String path = n.getValue();
				AccessibleValue<Boolean> re = AccessibleValue.create(path, Boolean.class);
				return re;
			} else {
				System.err.println("ERR: can not reference '" + n.getValue() + "' (" + baseType + ") as Number");
			}
		} else {
			System.err.println("ERR: can not find '" + n.getValue() + "'");
		}
		return null;
	}

	public static AccessibleAbstract<? extends Number> createFunctionRefNum(SyntaxNode n, Complex parentTyp) {
		String name = n.getTag();
		System.out.println("get_id from " + name + " (" + n.getValue() + ")");
		Attribute<?> id = getID(n, parentTyp);
		if (id != null) {
			Class<?> baseType = getBaseType(id);
			if (baseType == null) {
				System.err.println("ERR: can not get type of '" + n.getValue() + "'");
				return null;
			} else if (baseType == Integer.class) {
				String path = n.getValue();
				AccessibleValue<Integer> re = AccessibleValue.createNum(path, Integer.class);
				return re;
			} else if (baseType == Long.class) {
				String path = n.getValue();
				AccessibleValue<Long> re = AccessibleValue.createNum(path, Long.class);
				return re;
			} else if (baseType == BigInteger.class) {
				String path = n.getValue();
				AccessibleValue<BigInteger> re = AccessibleValue.createNum(path, BigInteger.class);
				return re;
			} else if (baseType == Byte.class) {
				String path = n.getValue();
				AccessibleValue<Byte> re = AccessibleValue.createNum(path, Byte.class);
				return re;
			} else {
				System.err.println("ERR: can not reference '" + n.getValue() + "' (" + baseType + ") as Number");
			}
		} else {
			System.err.println("ERR: can not find '" + n.getValue() + "'");
		}
		return null;
	}

	public static Class<?> getBaseType(Attribute<?> id) {
		AbstractType<?> t = id.getType();
		Class<? extends Value<?>> clazzChild = t.getClazz();
		Primitive<?> pri = Primitive.getInstance(clazzChild);
		Class<?> baseType = pri.getBaseType();
		return baseType;
	}

	public static Attribute<?> getID(SyntaxNode n, Complex parentTyp) {

		String name = n.getTag();

		System.out.println("get_id from " + name + " (" + n.getValue() + ")");
		String typName = parentTyp.getName();
		String idName = n.getValue();
		Complex pTyp = parentTyp;
		Attribute<?> id = pTyp.getSubAttribute(idName);
		while (id == null) {
			String[] names = Complex.splitRight(typName);
			if (names[0] == null) {
				break;
			}
			typName = names[0];
			pTyp = Complex.getInstance(typName);
			if (pTyp == null) {
				break;
			}
			id = pTyp.getSubAttribute(idName);
		}
		return id;

	}

	public static AccessibleAbstract<Boolean> createComparison(AccessibleAbstract<?> a, AccessibleAbstract<?> b, String name) {
		Class<?> baseTypA = a.getBaseType();
		Class<?> baseTypB = b.getBaseType();
		if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
			AccessibleAbstract<? extends Number> theA = a.<Number>as(Number.class);
			AccessibleAbstract<? extends Number> theB = b.<Number>as(Number.class);
			return ComparisonNumber.create(theA, theB, name);
		}
		if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
			AccessibleAbstract<? extends Number> theA = (AccessibleAbstract<? extends Number>) a;
			AccessibleAbstract<? extends Number> theB = (AccessibleAbstract<? extends Number>) b;
			return ComparisonNumber.create(theA, theB, name);
		} else if (baseTypA == String.class && baseTypB == String.class) {
			AccessibleAbstract<String> theA = (AccessibleAbstract<String>) a;
			AccessibleAbstract<String> theB = (AccessibleAbstract<String>) b;
			return ComparisonString.create(theA, theB, name);
		}
		return null;
	}

}
