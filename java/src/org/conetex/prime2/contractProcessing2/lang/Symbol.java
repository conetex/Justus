package org.conetex.prime2.contractProcessing2.lang;

public class Symbol {

	// description of data
		public static final String TYPE = "type";
		
		public static final String BOOL = "Boolean";
		
		public static final String BINT = "BigInteger";
		public static final String INT  = "Integer";
		public static final String LNG  = "Long";
		
		public static final String STR = "String";
		
	// assignment of data
		public static final String COPY = "copy";
		public static final String REFERENCE = "ref";
		
	// boolean operators
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String XOR = "xor";
	public static final String NOT = "not";
	
	// boolean expressions for comparison
	public static final String SMALLER = "smaller";
	public static final String EQUAL = "equal";
	public static final String GREATER = "greater";	
		
	// boolean expressions for checks
		public static final String ISNULL = "isNull";	
		
	// math elementary arithmetic
	public static final String PLUS = "plus";               // Addition
	public static final String MINUS = "minus";             // Subtraction 
	public static final String TIMES = "times";             // Multiplication 
	public static final String DIVIDED_BY = "divided_by";   // Division 
	public static final String REMAINS = "remains";         // Remainder 	

	// control function
	public static final String RETURN = "return";

	
	public static final String VALUE = "value";
	

	
	
	
	public static final String COMPLEX = "complexType";
	public static final String SIMPLE_TYPE_NS = "t:";

	public static final String TYPE_NAME = "name";
	
	public static final String FUNCTION = "function";
	public static final String FUNCTION_NAME = "name";
	
	public static final String IDENTIFIER = "element";
	public static final String IDENTIFIER_NAME = "name";
	public static final String IDENTIFIER_TYPE = "type";
	
	public static final String VALUE_TYPE = "type";
	
	
}
