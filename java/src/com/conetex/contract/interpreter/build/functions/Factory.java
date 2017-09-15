package com.conetex.contract.interpreter.build.functions;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.interpreter.build.functions.nesting.Box;
import com.conetex.contract.interpreter.build.functions.nesting.EmptyBox;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleConstant;
import com.conetex.contract.lang.AccessibleValue;
import com.conetex.contract.lang.SetableValue;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.assignment.Copy;
import com.conetex.contract.lang.assignment.Reference;
import com.conetex.contract.lang.bool.operator.Binary;
import com.conetex.contract.lang.bool.operator.Not;
import com.conetex.contract.lang.control.function.Call;
import com.conetex.contract.lang.control.function.Function;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.lang.math.ElementaryArithmetic;

public class Factory {

	Box<?, Object> complex = new Box<Object, Object>("complex") {
        @Override
        public Accessible<?> create(SyntaxNode n, Complex type) {
        	
            String name = n.getTag();
            if (type == null) {
                System.err.println("can not recognize type of " + name);
                return null;
            }

            List<SyntaxNode> children = n.getChildNodes();
            for (SyntaxNode c : children) {
                if (c.isType()) {
                    String cname = c.getName();
                    Complex ctype = Complex.getInstance(type.getName() + "." + cname);
                    if (ctype == null) {
                        System.err.println("createFunctions: can not identify " + type.getName() + "." + cname);
                        continue;
                    }
                    else {
                        //createFunctions(c, ctype);
                    	this.createChild(c, ctype);
                    }
                }
            }           
            return null;
        }
	};
	
    Box<?, Object> objFunction = new Box<Object, Object>("objFunction") {
        @Override
        public Accessible<?> create(SyntaxNode n, Complex type) {
        	
            String name = n.getTag();
            if (type == null) {
               System.err.println("can not recognize type of " + name + " " + n.getName());
                return null;
            }
System.out.println("createFunction " + name + " " + n.getName());
            List<Accessible<?>> steps = new LinkedList<Accessible<?>>();

            List<SyntaxNode> children = n.getChildNodes();
            for (SyntaxNode c : children) {
                if (c.isBuildInFunction() && ! c.getTag().equals(Symbol.FUNCTION) ) {
System.out.println("createBuild " + c.getTag() + " - " + c.getName());
                    //Accessible<?> v = createFunction(c, type);
                    Accessible<?> v = this.createChild(c, type);
                    if (v != null) {
                    	steps.add(v);
                    }
                }
            }
        	
        	//List<Accessible<?>> steps = Functions.createFunctions(n, parentTyp);
            Accessible<?>[] theSteps = new Accessible<?>[steps.size()];
            Accessible<?> main = Function.createObj(steps.toArray(theSteps), n.getName());            
            return main;
        }
    };	
    
    
    Box<Number, ?> numberFunction = new Box<Number, Object>("numberFunction") {
		@Override
		public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
            return null; //TODO implement
		}
    };
    
    Box<Boolean, ?> boolFunction = new Box<Boolean, Object>("boolFunction") {
		@Override
		public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
            return null; //TODO implement
		}
    };	
    
    Box<Object, Object> objReturn = new Box<Object, Object>("objReturn") {
        @Override
        public Accessible<? extends Object> create(SyntaxNode n, Complex parentTyp) {
        	Accessible<? extends Object> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };      
    
    Box<Number, Number> numberReturn = new Box<Number, Number>("numberReturn") {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
        	Accessible<? extends Number> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };    
    
    Box<Boolean, Boolean> boolReturn = new Box<Boolean, Boolean>("boolReturn") {
        @Override
        public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
        	Accessible<? extends Boolean> a = this.createChild(n.getChildElementByIndex(0), parentTyp);
            return Return.create(a);
        }
    };    
    
    EmptyBox<Object> objCall = new EmptyBox<Object>("objCall") {
        @Override
        public Accessible<? extends Object> create(SyntaxNode n, Complex parentTyp) {
            // CONTROL FUNCTION
            String functionObj = n.getType();//
            AccessibleValue<Structure> re = AccessibleValue.create(functionObj, Structure.class);
            // TODO wozu das hier? müsste das nicht von createValue angelegt
            // worden sein?
            // TODO Exception FunktionsDaten nicht da...
            String functionName = n.getName();
            Accessible<? extends Object> e = Function.getInstanceObject(functionName);
            // TODO Exception Funktion nicht da...
            return e; // TODO implement: Call.create(e, re);
        }
    };
    
    EmptyBox<Number> numberCall = new EmptyBox<Number>("numberCall") {
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
	
    EmptyBox<Boolean> boolCall = new EmptyBox<Boolean>("boolCall") {
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
    
    EmptyBox<Object> objRef = new EmptyBox<Object>("boolCall") {
        @Override
        public Accessible<? extends Object> create(SyntaxNode n, Complex parentTyp) {
            return null; //TODO implement Functions.createFunctionRefObj(n, parentTyp);
        }
    };
    
    EmptyBox<Number> numberRef = new EmptyBox<Number>("numberRef") {
        @Override
        public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
            return Functions.createFunctionRefNum(n, parentTyp);
        }
    };
    
    EmptyBox<Boolean> boolRef = new EmptyBox<Boolean>("boolRef") {
        @Override
        public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
            return Functions.createFunctionRefBool(n, parentTyp);
        }
    };

    Box<Object, Object> objAssigment = new Box<Object, Object>("objAssigment") {
        @Override
        public Accessible<?> create(SyntaxNode n, Complex parentTyp) {
        	String name = n.getTag();
        	if (name.equals("copy") || name.equals("refer")) {
                SyntaxNode c0 = n.getChildElementByIndex(0);
                System.out.println(c0.getValue());
                // TODO geht nich
                Attribute<?> id0 = parentTyp.getSubAttribute(c0.getValue());

                SyntaxNode c1 = n.getChildElementByIndex(1);
                System.out.println(c1.getValue());
                Attribute<?> id1 = parentTyp.getSubAttribute(c1.getValue());

                String c0DataType = c0.getType();
                if (c0DataType != null) {
                    // TODO nicht nötig das auszulesen! Sollte aber ne Warnung
                    Class<?> c0ClassX = Primitive.getClass(c0DataType);
                }

                // TODO: der teil hier könnte ausgelagert werden nach
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
                    SetableValue<?> trg = SetableValue.create(path0, c0Class);

                    Primitive<?> pri1 = Primitive.getInstance(c1Class);
                    Class<?> baseType1 = pri1.getBaseType();
                    //Accessible<?> src = createFunctionAccessibleObj(c1, parentTyp);
                    Accessible<?> src = this.createChild(c1, parentTyp);
                    if (src != null && trg != null) {
                        if (name.equals(Symbol.COPY)) {
                            return Copy.create(trg, src);// TODO dahinter liegt ein
                                                         // Cast! das geht
                                                         // schöner...
                        }
                        if (name.equals(Symbol.REFER)) {
                            return Reference.create(trg, src);
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
    
    Box<Number, Number> numberAssigment = new Box<Number, Number>("numberAssigment") {
		@Override
		public Accessible<? extends Number> create(SyntaxNode n, Complex parentTyp) {
			// TODO Auto-generated method stub
			return null;
		}
    };
    
    Box<Boolean, Boolean> boolAssigment = new Box<Boolean, Boolean>("boolAssigment") {
		@Override
		public Accessible<? extends Boolean> create(SyntaxNode n, Complex parentTyp) {
			// TODO Auto-generated method stub
			return null;
		}
    };
    
    EmptyBox<Object> objConst = new EmptyBox<Object>("objConst") {
        @Override
        public Accessible<Object> create(SyntaxNode n, Complex parentTyp) {
            return null; // TODO implement
        }
    };    
    
    EmptyBox<Number> numberConst = new EmptyBox<Number>("numberConst") {
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
    
    EmptyBox<Boolean> boolConst = new EmptyBox<Boolean>("boolConst") {
        @Override
        public Accessible<Boolean> create(SyntaxNode n, Complex parentTyp) {
            // BOOL
            return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
        }
    };


    Box<Number, Number> numberExpession = new Box<Number, Number>("numberExpession") {
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


    Box<Boolean, Boolean> boolExpression = new Box<Boolean, Boolean>("boolExpression") {
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

    Box<Boolean, Object> boolComparsion = new Box<Boolean, Object>("boolComparsion") {
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








    Box<Boolean, ?> boolNullCheck = new Box<Boolean, Object>("boolNullCheck") {
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
    
    public static Accessible<?> sbuild(SyntaxNode n, Complex type) {
    	Factory x = new Factory();
    	return x.build(n, type);
    }
    
    public Accessible<?> build(SyntaxNode n, Complex type) {

        objRef.means(Symbol.REFERENCE);
        
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
        numberExpession.contains(numberExpession);
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
        boolExpression.contains(boolComparsion);
        boolExpression.contains(Symbol.REFERENCE, boolRef);
        boolExpression.contains(Symbol.CALL, boolCall);
        boolExpression.contains(Symbol.BOOL, boolConst);
        boolExpression.contains(Symbol.ISNULL, boolNullCheck);
        boolExpression.contains(boolExpression);

        objCall.means(Symbol.CALL);
        
        objAssigment.means(Symbol.COPY);
        objAssigment.means(Symbol.REFER);
        objAssigment.contains(objCall);
        objAssigment.contains(objRef);
        objAssigment.contains(objConst);

        numberAssigment.means(Symbol.COPY);
        numberAssigment.means(Symbol.REFER);
        numberAssigment.contains(numberCall);        
        numberAssigment.contains(numberRef);        
        numberAssigment.contains(numberConst);        
        numberAssigment.contains(numberExpession);
        
        boolAssigment.means(Symbol.COPY);
        boolAssigment.means(Symbol.REFER);        
        boolAssigment.contains(boolCall);
        boolAssigment.contains(boolRef);
        boolAssigment.contains(boolConst);        
        boolAssigment.contains(boolExpression);
        boolAssigment.contains(boolComparsion);
        boolAssigment.contains(boolNullCheck);
        
        
        
        objReturn.means(Symbol.RETURN);
        objReturn.contains(numberExpession);    //TODO obj meint structure! also kein num...
        objReturn.contains(objRef);   
        objReturn.contains(objCall);   
        objReturn.contains(objConst);   
        
        numberReturn.means(Symbol.RETURN);//TODO contains
        
        boolReturn.means(Symbol.RETURN);        
        boolReturn.contains(boolExpression);   
        boolReturn.contains(boolComparsion);   
        boolReturn.contains(boolRef);   
        boolReturn.contains(boolCall);   
        boolReturn.contains(boolConst);   
        boolReturn.contains(boolNullCheck);  
        
        objFunction.means(Symbol.FUNCTION);
        //objFunction.means("contract");// this is the main function
        objFunction.contains(numberRef);//TODO eigentlich nur hinter Zuweisung
        objFunction.contains(objCall);//TODO weitere calls eigentlich nur hinter Zuweisung
        objFunction.contains(numberExpession);//TODO eigentlich nur hinter Zuweisung
        objFunction.contains(boolExpression);//TODO eigentlich nur hinter Zuweisung
        objFunction.contains(objReturn);
        objFunction.contains(boolComparsion);//TODO eigentlich nur hinter Zuweisung
        objFunction.contains(objAssigment);//TODO weitere zuweisungen müssen möglich sein ...
        //objFunction.contains(numberAssigment);
        //objFunction.contains(boolAssigment);
        
        
        complex.means("complexType");
        complex.contains(objFunction);
        complex.contains(complex);
        
        //
        complex.create(n, type);
        return objFunction.create(n, type);
        
    }

}
