package com.conetex.contract.interpreter.build.functions;

import java.math.BigInteger;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class Factory {

    Builder<Boolean, ?> boolRef = new Builder<Boolean, Object>() {
        @Override
        public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
            return Functions.createFunctionRefBool(n, parentTyp);
        }
    };

    Builder<Boolean, ?> boolCall = new Builder<Boolean, Object>() {
        @Override
        public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
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

    Builder<Boolean, Boolean> boolConst = new Builder<Boolean, Boolean>() {
        @Override
        public Accessible<Boolean> create(SyntaxNode n, Complex parentTyp) {
            // BOOL
            return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
        }
    };

    Builder<Boolean, Boolean> boolNullCheck = new Builder<Boolean, Boolean>() {
        @Override
        public Accessible<Boolean> create(SyntaxNode n, Complex parentTyp) {
            // BOOL
            String name = n.getTag();
            if (name.equals(Symbol.ISNULL)) {
                // TODO

            }
            return null;
        }
    };

    Builder<Boolean, Boolean> boolExpression = new Builder<Boolean, Boolean>() {
        @Override
        public Accessible<Boolean> create(SyntaxNode n, Complex parentTyp) {
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
                Accessible<Boolean> sub = Functions.createFunctionAccessibleBool(n.getChildElementByIndex(0),
                        parentTyp);
                if (sub != null) {
                    return Not.create(sub);
                }
            }
            return null;
        }
    };

    Builder<Boolean, Object> boolComparsion = new Builder<Boolean, Object>() {
        @Override
        public Accessible<Boolean> create(SyntaxNode n, Complex parentTyp) {
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

    Builder<Number, ?> numberRef = new Builder<Number, Object>() {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
            return Functions.createFunctionRefNum(n, parentTyp);
        }
    };

    Builder<Number, ?> numberCall = new Builder<Number, Object>() {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
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

    Builder<Number, ?> numberConst = new Builder<Number, Object>() {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
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

    Builder<Number, Number> numberExpession = new Builder<Number, Number>() {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
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

    public void build() {

        numberRef.means(Symbol.REFERENCE);

        numberCall.means(Symbol.CALL);

        numberConst.means(Symbol.BINT);
        numberConst.means(Symbol.INT);
        numberConst.means(Symbol.LNG);

        numberExpession.means(Symbol.PLUS);
        numberExpession.means(Symbol.MINUS);
        numberExpession.means(Symbol.TIMES);
        numberExpession.means(Symbol.DIVIDED_BY);
        numberExpession.means(Symbol.REMAINS);
        numberExpession.contains(Symbol.BINT, numberConst);
        numberExpession.contains(Symbol.INT, numberConst);
        numberExpession.contains(Symbol.LNG, numberConst);
        numberExpession.contains(Symbol.CALL, numberCall);
        numberExpession.contains(Symbol.REFERENCE, numberRef);

        boolComparsion.means(Symbol.SMALLER);
        boolComparsion.means(Symbol.GREATER);
        boolComparsion.means(Symbol.EQUAL);
        boolComparsion.contains(Symbol.PLUS, numberExpession);
        boolComparsion.contains(Symbol.MINUS, numberExpession);
        boolComparsion.contains(Symbol.TIMES, numberExpession);
        boolComparsion.contains(Symbol.DIVIDED_BY, numberExpession);
        boolComparsion.contains(Symbol.REMAINS, numberExpession);
        boolComparsion.contains(Symbol.BINT, numberConst);
        boolComparsion.contains(Symbol.INT, numberConst);
        boolComparsion.contains(Symbol.LNG, numberConst);
        boolComparsion.contains(Symbol.CALL, numberCall);
        boolComparsion.contains(Symbol.REFERENCE, numberRef);

        boolRef.means(Symbol.REFERENCE);

        boolCall.means(Symbol.CALL);

        boolConst.means(Symbol.BOOL);

        boolNullCheck.means(Symbol.ISNULL);

        boolExpression.means(Symbol.AND);
        boolExpression.means(Symbol.OR);
        boolExpression.means(Symbol.XOR);
        boolExpression.means(Symbol.NOT);

        boolExpression.contains(Symbol.SMALLER, boolComparsion);
        boolExpression.contains(Symbol.GREATER, boolComparsion);
        boolExpression.contains(Symbol.EQUAL, boolComparsion);
        boolExpression.contains(Symbol.REFERENCE, boolRef);
        boolExpression.contains(Symbol.CALL, boolCall);
        boolExpression.contains(Symbol.BOOL, boolConst);
        boolExpression.contains(Symbol.ISNULL, boolNullCheck);
        boolExpression.contains(Symbol.AND, boolExpression);
        boolExpression.contains(Symbol.OR, boolExpression);
        boolExpression.contains(Symbol.XOR, boolExpression);
        boolExpression.contains(Symbol.NOT, boolExpression);

    }

}
