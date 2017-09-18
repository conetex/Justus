package com.conetex.contract.interpreter.functions;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.nesting.Box;
import com.conetex.contract.interpreter.functions.nesting.EmptyBox;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.SetableValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.assignment.Creator;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class Factory {

    Box<Structure, Object> complex = new Box<Structure, Object>("complex") {
        @Override
        public Accessible<Structure> create(CodeNode n, Complex type) {

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

    Box<Object, Object> whatEverFunction = new Box<Object, Object>("whatEverFunction") {
        @Override
        public Accessible<? extends Object> create(CodeNode n, Complex type) {
            Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
            Accessible<? extends Object> main = Function.createWhatEver(theSteps, n.getName());
            return main;
        }
    };

    Box<Structure, Object> objFunction = new Box<Structure, Object>("objFunction") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex type) {
            Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
            Accessible<? extends Structure> main = Function.createStructure(theSteps, n.getName());
            return main;
        }
    };

    Box<Number, Object> numberFunction = new Box<Number, Object>("numberFunction") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex type) {
            Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
            Accessible<? extends Number> main = Function.createNum(theSteps, n.getName());
            return main;
        }
    };

    Box<Boolean, Object> boolFunction = new Box<Boolean, Object>("boolFunction") {
        @Override
        public Accessible<? extends Boolean> create(CodeNode n, Complex type) {
            Accessible<?>[] theSteps = Factory.getFunctionSteps(n, type, this);
            Accessible<? extends Boolean> main = Function.createBool(theSteps, n.getName());
            return main;
        }
    };

    Box<Object, Object> whatEverReturn = new Box<Object, Object>("whatEverReturn") {
        @Override
        public Accessible<?> create(CodeNode n, Complex parentTyp) {
            Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };

    Box<Structure, Structure> objReturn = new Box<Structure, Structure>("objReturn") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) {
            Accessible<? extends Structure> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };

    Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };

    Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
        @Override
        public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) {
            Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };

    EmptyBox<Object> whatEverCall = new EmptyBox<Object>("whatEverCall") {
        @Override
        public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) {
            // CONTROL FUNCTION
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
            // TODO wozu das hier? müsste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<? extends Object> e = Function.getInstanceWhatEver(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
    };

    EmptyBox<Structure> objCall = new EmptyBox<Structure>("objCall") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) {
            // CONTROL FUNCTION
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
            // TODO wozu das hier? müsste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<? extends Structure> e = Function.getInstanceStructure(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
    };

    EmptyBox<Number> numberCall = new EmptyBox<Number>("numberCall") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            // CONTROL FUNCTION
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
            // TODO wozu das hier? müsste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<? extends Number> e = Function.getInstanceNum(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
    };

    EmptyBox<Boolean> boolCall = new EmptyBox<Boolean>("boolCall") {
        @Override
        public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) {
            // CONTROL FUNCTION
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
            // TODO wozu das hier? müsste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<Boolean> e = Function.getInstanceBool(functionName);
            // TODO Exception Funktion nicht da...
            return Call.create(e, re);
        }
    };

    EmptyBox<Structure> whatEverRef = new EmptyBox<Structure>("whatEverRef") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) {
            return Functions.createFunctionRefWhatEver(n, parentTyp); // parentTyp);
        }
    };

    EmptyBox<Structure> objRef = new EmptyBox<Structure>("objRef") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) {
            return Functions.createFunctionRefStructure(n, parentTyp); // parentTyp);
        }
    };

    EmptyBox<Number> numberRef = new EmptyBox<Number>("numberRef") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            return Functions.createFunctionRefNum(n, parentTyp);
        }
    };

    EmptyBox<Boolean> boolRef = new EmptyBox<Boolean>("boolRef") {
        @Override
        public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) {
            return Functions.createFunctionRefBool(n, parentTyp);
        }
    };

    Box<Structure, Structure> objAssigment = new Box<Structure, Structure>("objAssigment") {
        @Override
        public Accessible<? extends Structure> create(CodeNode n, Complex parentTyp) {
            SetableValue<Structure> trg = Functions.createFunctionSetableStructure(n.getChildElementByIndex(0),
                    parentTyp);
            Accessible<?> src = this.createChild(n.getChildElementByIndex(1), parentTyp);
            if (src != null && trg != null) {
                Class<?> srcBaseType = src.getBaseType();
                if (srcBaseType == Structure.class) {
                    // TODO hier müsste man den typ complex checken ...
                }
                else {
                    return null;
                }
                return Creator.createFromQualifiedTrg(trg, src, n.getTag());
            }
            return null;
        }
    };

    Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            SetableValue<Number> trg = Functions.createFunctionSetableNumber(n.getChildElementByIndex(0), parentTyp);
            Accessible<?> src = this.createChild(n.getChildElementByIndex(1), parentTyp);
            if (src != null && trg != null) {
                Class<?> trgBaseType = trg.getBaseType();
                Class<?> srcBaseType = src.getBaseType();
                Class<?> baseType;
                if (trgBaseType == srcBaseType) {
                    baseType = trgBaseType;
                }
                else {
                    baseType = ElementaryArithmetic.getBiggest(trgBaseType, srcBaseType);
                    if (baseType == srcBaseType) {
                        // src is bigger than trg
                        return null;
                    }
                }
                return Creator.createFromQualifiedTrg(trg, src, n.getTag());
            }
            return null;
        }
    };

    Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
        @Override
        public Accessible<? extends Boolean> create(CodeNode n, Complex parentTyp) {
            SetableValue<Boolean> trg = Functions.createFunctionSetableBoolean(n.getChildElementByIndex(0), parentTyp);
            Accessible<?> src = this.createChild(n.getChildElementByIndex(1), parentTyp);
            if (src != null && trg != null) {
                Class<?> srcBaseType = src.getBaseType();
                if (srcBaseType != Boolean.class) {
                    return null;
                }
                return Creator.createFromQualifiedTrg(trg, src, n.getTag());
            }
            return null;
        }
    };

    Box<Object, Object> whatEverAssigment = new Box<Object, Object>("whatEverAssigment") {
        @Override
        public Accessible<? extends Object> create(CodeNode n, Complex parentTyp) {
            SetableValue<?> trg = Functions.createFunctionSetableWhatEver(n.getChildElementByIndex(0), parentTyp);
            Accessible<?> src = this.createChild(n.getChildElementByIndex(1), parentTyp);
            if (src != null && trg != null) {
                Class<?> trgBaseType = trg.getBaseType();
                Class<?> srcBaseType = src.getBaseType();
                Class<?> baseType;
                if (trgBaseType == srcBaseType) {
                    baseType = trgBaseType;
                }
                else {
                    baseType = ElementaryArithmetic.getBiggest(trgBaseType, srcBaseType);
                    if (baseType == srcBaseType) {
                        // src is bigger than trg
                        return null;
                    }
                }
                return Creator.createFromUnqualified(trg, src, baseType, n.getTag());
            }
            return null;
        }
    };

    EmptyBox<Structure> objConst = new EmptyBox<Structure>("objConst") {
        @Override
        public Accessible<Structure> create(CodeNode n, Complex parentTyp) {
            return null; // TODO implement
        }
    };

    EmptyBox<Object> whatEverConst = new EmptyBox<Object>("whatEverConst") {
        @Override
        public Accessible<Object> create(CodeNode n, Complex parentTyp) {
            return null; // TODO implement
        }
    };

    EmptyBox<Number> numberConst = new EmptyBox<Number>("numberConst") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            // VARIABLE
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
    };

    EmptyBox<Boolean> boolConst = new EmptyBox<Boolean>("boolConst") {
        @Override
        public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
            // BOOL
            return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
        }
    };

    Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
        @Override
        public Accessible<? extends Number> create(CodeNode n, Complex parentTyp) {
            // MATH
            String name = n.getTag();
            Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            Accessible<? extends Number> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
            // TODO check 4 other childs! only 2 are alowed
            if (a != null && b != null) {
                Accessible<? extends Number> re = ElementaryArithmetic.createNew(a, b, name);
                return re;
            }
            return null;
        }
    };

    Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
        @Override
        public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
            // BOOL
            String name = n.getTag();
            if (name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR)) {
                Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
                Accessible<? extends Boolean> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
                if (a != null && b != null) {
                    return Binary.create(a, b, name);
                }
            }
            else if (name.equals(Symbol.NOT)) {
                // Accessible<Boolean> sub =
                // Functions.createFunctionAccessibleBool(n.getChildElementByIndex(0),
                // parentTyp);
                Accessible<? extends Boolean> sub = this.createChild(n.getChildElementByIndex(0), parentTyp);
                if (sub != null) {
                    return Not.create(sub);
                }
            }
            return null;
        }
    };

    Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion") {
        @Override
        public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
            // COMPARISON
            String name = n.getTag();
            if (name.equals(Symbol.SMALLER) || name.equals(Symbol.GREATER) || name.equals(Symbol.EQUAL)) {
                Accessible<?> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
                Accessible<?> b = this.createChild(n.getChildElementByIndex(1), parentTyp);
                // TODO check 4 other childs! only 2 are alowed
                if (a != null && b != null) {
                    Accessible<Boolean> re = Functions.createComparison(a, b, name);
                    return re;
                }
            }
            return null;
        }
    };

    Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
        @Override
        public Accessible<Boolean> create(CodeNode n, Complex parentTyp) {
            // BOOL
            String name = n.getTag();
            if (name.equals(Symbol.ISNULL)) {
                // TODO

            }
            return null;
        }
    };

    public static Accessible<?> sbuild(CodeNode n, Complex type) {
        Factory x = new Factory();
        return x.build(n, type);
    }

    public Accessible<?> build(CodeNode n, Complex type) {

        objRef.means(Symbol.REFERENCE);
        numberRef.means(Symbol.REFERENCE);
        boolRef.means(Symbol.REFERENCE);
        whatEverRef.means(Symbol.REFERENCE);

        objCall.means(Symbol.CALL);
        numberCall.means(Symbol.CALL);
        boolCall.means(Symbol.CALL);
        whatEverCall.means(Symbol.CALL);

        numberConst.means(new String[] { Symbol.BINT, Symbol.INT, Symbol.LNG });
        boolConst.means(Symbol.BOOL);
        objConst.means("?"); // TODO objConst

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
        whatEverReturn.contains(whatEverCall);
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
        // objReturn.contains(numberExpession); //TODO weg damit !!!

        numberReturn.means(Symbol.RETURN);// TODO contains
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

        whatEverFunction.means(Symbol.FUNCTION);
        whatEverFunction.contains(whatEverAssigment);
        whatEverFunction.contains(whatEverCall);
        whatEverFunction.contains(whatEverReturn);
        whatEverFunction.contains(numberExpession);// TODO eigentlich nur hinter Zuweisung
        whatEverFunction.contains(boolExpression);// TODO eigentlich nur hinter Zuweisung
        whatEverFunction.contains(boolComparsion);// TODO eigentlich nur hinter Zuweisung

        objFunction.means(Symbol.FUNCTION);
        // objFunction.means("contract");// this is the main function
        objFunction.contains(whatEverCall);
        objFunction.contains(numberRef);// TODO eigentlich nur hinter Zuweisung
        objFunction.contains(numberExpession);// TODO eigentlich nur hinter Zuweisung
        objFunction.contains(boolExpression);// TODO eigentlich nur hinter Zuweisung
        objFunction.contains(boolComparsion);// TODO eigentlich nur hinter Zuweisung
        objFunction.contains(whatEverAssigment);
        objFunction.contains(objReturn);

        numberFunction.means(Symbol.FUNCTION);
        numberFunction.contains(whatEverCall);
        numberFunction.contains(whatEverAssigment);
        numberFunction.contains(numberReturn);

        boolFunction.means(Symbol.FUNCTION);
        boolFunction.contains(whatEverCall);
        boolFunction.contains(whatEverAssigment);
        boolFunction.contains(boolReturn);

        complex.means("complexType");
        complex.contains(whatEverFunction);
        complex.contains(complex);

        //
        complex.create(n, type);
        return whatEverFunction.create(n, type);

    }

    public static Accessible<?>[] getFunctionSteps(CodeNode n, Complex type, Box box) {

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

}
