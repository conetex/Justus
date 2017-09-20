package com.conetex.contract.interpreter.functions;

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
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.FunctionNotFound;
import com.conetex.contract.interpreter.functions.exception.MissingSubOperation;
import com.conetex.contract.interpreter.functions.exception.NoAccessToValue;
import com.conetex.contract.interpreter.functions.exception.OperationInterpreterException;
import com.conetex.contract.interpreter.functions.exception.TypeNotDeterminated;
import com.conetex.contract.interpreter.functions.exception.TypesDoNotMatch;
import com.conetex.contract.interpreter.functions.exception.UnexpectedSubOperation;
import com.conetex.contract.interpreter.functions.exception.UnknownAttribute;
import com.conetex.contract.interpreter.functions.exception.UnknownComplexType;
import com.conetex.contract.interpreter.functions.exception.UnknownType;
import com.conetex.contract.interpreter.functions.nesting.Box;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.SetableValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang._SetableValueOLD;
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

    // 2tree
    public static List<Accessible<?>> _createFunctions(CodeNode n, Complex type) throws OperationInterpreterException {
        String name = n.getTag();
        if (type == null) {
            System.err.println("can not recognize type of " + name);
            return null;
        }

        List<Accessible<?>> acc = new LinkedList<Accessible<?>>();

        List<CodeNode> children = n.getChildNodes();
        for (CodeNode c : children) {
            // for(int i = 0; i < children.getLength(); i++){
            // Node c = children.item(i);
            if (c.isBuildInFunction()) {

                System.out.println("createFunctions " + c.getTag() + " - " + c.getName());
                Accessible<?> v = _createFunction(c, type);
                if (v != null) {
                    acc.add(v);
                }
            }

            if (c.isType()) {
                String cname = c.getName();// ReadXMLtools.getAttribute(c,
                                           // Symbol.TYPE_NAME);
                /*
                 * Identifier<?> cid = type.getSubIdentifier(cname); // if(cid == null){
                 * System.err.println( "createFunctions: can not identify " + cname); continue;
                 * } AbstractType<?> ctype = cid.getType(); if( ctype.getClass() ==
                 * Complex.class ){ createFunctions(c, (Complex) ctype); }
                 */
                Complex ctype = Complex.getInstance(type.getName() + "." + cname);
                if (ctype == null) {
                    System.err.println("createFunctions: can not identify " + type.getName() + "." + cname);
                    continue;
                }
                else {
                    _createFunctions(c, ctype);
                }
            }
        }

        return acc;
    }

    // 2tree
    public static Accessible<?> _createFunction(CodeNode n, Complex parentTyp// ,
                                                                             // List<FunctionBuilder>
                                                                             // functionBuilders//,
                                                                             // List<Value<?>>
                                                                             // values,
                                                                             // Map<Complex,
                                                                             // List<FunctionBuilder>>
                                                                             // complexTyps
    ) throws OperationInterpreterException {

        String name = n.getTag();

        if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
                || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
            Accessible<? extends Number> x = // createNumExpression( super.node,
                                             // theClass );
                    // createFunctionAccessible( n, parentTyp, Integer.class );// TODO
                    // mach das zu
                    // number ...
                    _createFunctionAccessibleNum(n, parentTyp);
            return x;
        }
        else if (name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) || name.equals(Symbol.NOT)
                || name.equals(Symbol.SMALLER) || name.equals(Symbol.EQUAL) || name.equals(Symbol.GREATER)
                || name.equals(Symbol.ISNULL)) {
            Accessible<Boolean> x = // createBoolExpression( super.node );
                    // ReadXML.createAccessible( super.node, c, Boolean.class );
                    _createFunctionAccessibleBool(n, parentTyp);
            return x;
        }
        else if (name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY)) {
            Accessible<?> x = _createFunctionAccessibleObj(n, parentTyp);
            return x;
        }
        else if (name.equals(Symbol.FUNCTION) || name.equals(Symbol.RETURN) || name.equals(Symbol.CALL)) {
            Accessible<?> x = // createBoolExpression( super.node );
                    _createFunctionAccessibleObj(n, parentTyp);
            return x;
        }

        return null;

    }

    // 2tree
    public static Accessible<?> _createFunctionAccessibleObj(CodeNode n, Complex parentTyp) throws OperationInterpreterException {

        String name = n.getTag();

        // MATH
        if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
                || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
            return _createFunctionAccessibleNum(n, parentTyp);
        }
        // VARIABLE
        else if (name.equals(Symbol.BINT)) {
            return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
        }
        else if (name.equals(Symbol.INT)) {
            return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
        }
        else if (name.equals(Symbol.LNG)) {
            return AccessibleConstant.<Long>create2(Long.class, n.getValue());
        }
        // CONTROL FUNCTION
        else if (name.equals(Symbol.RETURN)) {
            Accessible<?> e = _createFunctionAccessibleObj(n.getChildElementByIndex(0), parentTyp);
            return Return.create(e);
        }
        else if (name.equals(Symbol.CALL)) {
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);

            String functionName = n.getName();
            Accessible<?> e = Function.getInstance(functionName);
            // Class<?> expectedBaseTypExp = e.getBaseType();
            return Call.create(e, re);
            // return ReadXML.createAccessible( e, parentTyp, expectedBaseTypExp
            // );
        }
        else if (name.equals(Symbol.FUNCTION)) {
            String functionName = n.getName();
            String idTypeName = n.getType();
            Complex complexType = Complex.getInstance(idTypeName);
            if (complexType != null) {
                List<Accessible<?>> steps = _createFunctions(n, complexType);
                Accessible<?>[] theSteps = new Accessible<?>[steps.size()];
                // TODO hier return typ checken und Bool / Obj creator
                // aufrufen...
                Function.createNum(steps.toArray(theSteps), functionName);
                return null; // das ist wichtig. wir wollen functions nur per
                             // call aufrufen!
            }
        }
        // ASSIGNMENT
        else if (name.equals("copy") || name.equals("refer")) {
            // createAssignment(name, n, parentTyp);
            CodeNode c0 = n.getChildElementByIndex(0);
            System.out.println(c0.getValue());
            Attribute<?> id0 = parentTyp.getSubAttribute(c0.getValue());// TODO
                                                                        // geht
                                                                        // nich

            CodeNode c1 = n.getChildElementByIndex(1);
            System.out.println(c1.getValue());
            Attribute<?> id1 = parentTyp.getSubAttribute(c1.getValue());

            String c0DataType = c0.getType();
            if (c0DataType != null) {
                // TODO nicht n�tig das auszulesen! Sollte aber ne Warnung
                // erzeugen, wenns nicht
                // zum typ des referenzierten Feld passt ...
                // Class<Value<Object>> c0ClassX =
                // Primitive.getClass(c0DataType);
                Class<?> c0ClassX = Primitive.getClass(c0DataType);
            }

            // TODO: der teil hier k�nnte ausgelagert werden nach
            // AbstractAssigment

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
                _SetableValueOLD<?> trg = _SetableValueOLD.create(path0, c0Class);

                Primitive<?> pri1 = Primitive.getInstance(c1Class);
                Class<?> baseType1 = pri1.getBaseType();
                Accessible<?> src = _createFunctionAccessibleObj(c1, parentTyp);
                if (src != null && trg != null) {
                    if (name.equals(Symbol.COPY)) {
                        return Copy._createFromClass(trg, src, baseType1);// TODO dahinter liegt ein
                        // Cast! das geht
                        // sch�ner...
                    }
                    if (name.equals(Symbol.REFERENCE)) {
                        return Reference._create(trg, src);
                    }
                }
                return null;
            }
            else {
                // TODO: classes do not match
                return null;
            }
        }
        // REFERENCE
        else if (name.equals(Symbol.REFERENCE)) {
            return createFunctionRef(n, parentTyp, Structure.class);
        }

        return null;
    }

    // 2tree
    public static Accessible<? extends Number> _createFunctionAccessibleNum(CodeNode n, Complex parentTyp) throws OperationInterpreterException {

        String name = n.getTag();

        // MATH
        if (name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES)
                || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)) {
            Accessible<? extends Number> a = _createFunctionAccessibleNum(n.getChildElementByIndex(0), parentTyp);
            Accessible<? extends Number> b = _createFunctionAccessibleNum(n.getChildElementByIndex(1), parentTyp);
            // TODO check 4 other childs! only 2 are alowed
            if (a != null && b != null) {
                Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
                return re;
            }
        }
        // VARIABLE
        else if (name.equals(Symbol.BINT)) {
            return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
        }
        else if (name.equals(Symbol.INT)) {
            return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
        }
        else if (name.equals(Symbol.LNG)) {
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
            // TODO wozu das hier? m�sste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<? extends Number> e = Function.getInstanceNum(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
        // REFERENCE
        else if (name.equals(Symbol.REFERENCE)) {
            return createFunctionRefNum(n, parentTyp);
        }

        return null;
    }

    // 2tree
    public static Accessible<Boolean> _createFunctionAccessibleBool(CodeNode n, Complex parentTyp) throws OperationInterpreterException {

        String name = n.getTag();
        // COMPARISON
        if (name.equals(Symbol.SMALLER) || name.equals(Symbol.GREATER) || name.equals(Symbol.EQUAL)) {
            Accessible<?> a = _createFunctionAccessibleObj(n.getChildElementByIndex(0), parentTyp);
            Accessible<?> b = _createFunctionAccessibleObj(n.getChildElementByIndex(1), parentTyp);
            // TODO check 4 other childs! only 2 are alowed
            if (a != null && b != null) {
                Accessible<Boolean> re = createComparison(a, b, name);
                return re;
            }
        }
        // BOOL
        else if (name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR)) {
            Accessible<Boolean> a = _createFunctionAccessibleBool(n.getChildElementByIndex(0), parentTyp);
            Accessible<Boolean> b = _createFunctionAccessibleBool(n.getChildElementByIndex(1), parentTyp);
            if (a != null && b != null) {
                return Binary.create(a, b, name);
            }
        }
        else if (name.equals(Symbol.NOT)) {
            Accessible<Boolean> sub = _createFunctionAccessibleBool(n.getChildElementByIndex(0), parentTyp);
            if (sub != null) {
                return Not.create(sub);
            }
        }
        else if (name.equals(Symbol.ISNULL)) {
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
            // TODO wozu das hier? m�sste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<Boolean> e = Function.getInstanceBool(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
        // REFERENCE
        else if (name.equals(Symbol.REFERENCE)) {
            return createFunctionRef(n, parentTyp, Boolean.class);
        }

        return null;
    }

    Box<Object, Object> _objAssigment = new Box<Object, Object>("_objAssigment") {
        @Override
        public Accessible<?> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
            String name = n.getTag();
            if (name.equals("copy") || name.equals("refer")) {
                CodeNode c0 = n.getChildElementByIndex(0);
                System.out.println(c0.getValue());
                // TODO geht nich
                Attribute<?> id0 = parentTyp.getSubAttribute(c0.getValue());

                CodeNode c1 = n.getChildElementByIndex(1);
                System.out.println(c1.getValue());
                Attribute<?> id1 = parentTyp.getSubAttribute(c1.getValue());

                String c0DataType = c0.getType();
                if (c0DataType != null) {
                    // TODO nicht n�tig das auszulesen! Sollte aber ne Warnung
                    Class<?> c0ClassX = Primitive.getClass(c0DataType);
                }

                // TODO: der teil hier k�nnte ausgelagert werden nach
                // AbstractAssigment

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
                    _SetableValueOLD<?> trg = _SetableValueOLD.create(path0, c0Class);

                    Primitive<?> pri1 = Primitive.getInstance(c1Class);
                    Class<?> baseType1 = pri1.getBaseType();
                    // Accessible<?> src = createFunctionAccessibleObj(c1,
                    // parentTyp);
                    Accessible<?> src = this.createChild(c1, parentTyp);
                    if (src != null && trg != null) {
                        if (name.equals(Symbol.COPY)) {
                            return Copy._createFromTrg(trg, src);// TODO dahinter liegt
                                                                 // ein
                                                                 // Cast! das geht
                                                                 // sch�ner...
                        }
                        if (name.equals(Symbol.REFER)) {
                            return Reference._create(trg, src);
                        }
                    }
                    return null;
                }
                else {
                    // TODO: classes do not match
                    return null;
                }
            }
            return null;
        }
    };

    public static <R> SetableValue<R> createFunctionSetable(CodeNode n, Complex parentTyp, Class<R> expected) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        checkBaseType(getID(n.getValue(), parentTyp), expected);
        String path = n.getValue();
        SetableValue<R> re = SetableValue.create(path, expected);
        return re;
    }
    
    public static <R> Accessible<R> createFunctionRef(CodeNode n, Complex parentTyp, Class<R> expected) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        checkBaseType(getID(n.getValue(), parentTyp), Structure.class);
        String path = n.getValue();
        AccessibleValue<R> re = AccessibleValue.create(path, expected);
        return re;
    } 


    public static SetableValue<?> createFunctionSetableWhatEver(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        Class<?> baseType = getBaseType(getID(n.getValue(), parentTyp));
        String path = n.getValue();
        SetableValue<?> re = SetableValue.create(path, baseType);
        return re;
    }

    public static Accessible<?> createFunctionRefWhatEver(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        Class<?> baseType = getBaseType(getID(n.getValue(), parentTyp));
        String path = n.getValue();
        AccessibleValue<?> re = AccessibleValue.create(path, baseType);
        return re;
    }    
    


    
    public static SetableValue<? extends Number> createFunctionSetableNumber(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        Attribute<?> id = getID(n.getValue(), parentTyp);
        Class<? extends Number> baseType = getConcretNumClass( getBaseType(id) );
        SetableValue<? extends Number> re = SetableValue.create(n.getValue(), baseType);
        return re;
    }
    
    public static Accessible<? extends Number> createFunctionRefNum(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
        System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        Attribute<?> id = getID(n.getValue(), parentTyp);
        Class<? extends Number> baseType = getConcretNumClass( getBaseType(id) );
        AccessibleValue<? extends Number> re = AccessibleValue.create(n.getValue(), baseType);        
        return re;
    }

    public static void checkBaseTypeIsSubTypeOf(Class<?> baseType, Class<?> expected) throws TypesDoNotMatch, UnknownType {
    	if(! expected.isAssignableFrom(baseType)){
            //System.err.println("ERR: can not reference '" + n.getValue() + "' (" + baseType + ") as subtype of " + expected.toString());
    		throw new TypesDoNotMatch(baseType.toString(), expected.toString());
    	}
    }
    
    public static Class<? extends Number> getConcretNumClass(Class<?> baseType) throws TypesDoNotMatch {
    	if(baseType == Integer.class){
    		return Integer.class;
    	}
    	if(baseType == BigInteger.class){
    		return BigInteger.class;
    	}
    	if(baseType == Long.class){
    		return Long.class;
    	}    	
    	if(baseType == Byte.class){
    		return Byte.class;
    	}
   		throw new TypesDoNotMatch(baseType.toString(), Number.class.toString());    	
    }
    
    public static void checkBaseType(Attribute<?> id, Class<?> expected) throws TypesDoNotMatch, UnknownType {
    	Class<?> baseType = getBaseType(id);
    	if(baseType != expected){
//            System.err.println("ERR: can not reference '" + n.getValue() + "' (" + baseType + ") as " + expected.toString());
    		throw new TypesDoNotMatch(baseType.toString(), expected.toString());
    	}
    }
    
    public static Class<?> getBaseType(Attribute<?> id) throws UnknownType {
        AbstractType<?> t = id.getType();
        Class<? extends Value<?>> clazzChild = t.getClazz();
        Primitive<?> pri = Primitive.getInstance(clazzChild);
        if(pri == null){
        	//System.err.println("ERR: can not get type of '" + n.getValue() + "'");
        	throw new UnknownType(clazzChild.toString());
        }
        Class<?> baseType = pri.getBaseType();
        return baseType;
    }

    public static Attribute<?> getID(String idName, Complex parentTyp) throws UnknownAttribute {

        //System.out.println("get_id from " + n.getTag() + " (" + n.getValue() + ")");
        String typName = parentTyp.getName();
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
        if(id == null){
        	//System.err.println("ERR: can not find '" + idName + "'");
        	throw new UnknownAttribute( idName );
        }
        return id;

    }

    public static Accessible<Boolean> createComparison(Accessible<?> a, Accessible<?> b, String name) {
        Class<?> baseTypA = a.getBaseType();
        Class<?> baseTypB = b.getBaseType();
        if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
            Accessible<? extends Number> theA = a.<Number>as(Number.class);
            Accessible<? extends Number> theB = b.<Number>as(Number.class);
            return ComparisonNumber.create(theA, theB, name);
        }
        if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
            Accessible<? extends Number> theA = (Accessible<? extends Number>) a;
            Accessible<? extends Number> theB = (Accessible<? extends Number>) b;
            return ComparisonNumber.create(theA, theB, name);
        }
        else if (baseTypA == String.class && baseTypB == String.class) {
            Accessible<String> theA = (Accessible<String>) a;
            Accessible<String> theB = (Accessible<String>) b;
            return ComparisonString.create(theA, theB, name);
        }
        return null;
    }



}
