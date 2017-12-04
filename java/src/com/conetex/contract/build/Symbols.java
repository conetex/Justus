package com.conetex.contract.build;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;

public class Symbols{

	public static String[] splitRight(String aName) {
		String[] re = new String[2];
		if(aName == null){
			return re;
		}
		int i = aName.lastIndexOf(Symbols.litTypeSeperator());
		if(i > -1 && i < aName.length()){
			re[0] = aName.substring(0, i);
			if(i + Symbols.litTypeSeperator().length() < aName.length()){
				re[1] = aName.substring(i + Symbols.litTypeSeperator().length());
			}
		}
		return re;
	}
	
	public static String getParentNameNoNull(String aName) throws AbstractInterpreterException {
		String re = getParentName(aName);
		if(re == null){
			throw new AbstractInterpreterException("can not extract parent name from " + aName);						
		}
		return re;
	}
	
	public static String getParentName(String aName) {
		String[] re = splitRight(aName);
		return re[0];
	}

	public static String getSimpleName(String aName) {
		String[] re = splitRight(aName);
		if(re[1] == null){
			return aName;
		}
		return re[1];
	}
	
	public static final String NAME_SEPERATOR = ".";
	
	
	private static final String SIMPLE_TYPE_NS = "t:";
	private static final String TYPE_SEPERATOR = ".";
	public static final String[] PARAM_AFFIXES = new String[] { "(", ")", "_", "-" };

	// data command
	private static final String COM_CONTRACT = "contract";
	private static final String COM_COMPLEX = "complexType";
	private static final String COM_ATTRIBUTE = "attribute";
	private static final String COM_VALUE = "value";
	private static final String COM_VIRTUAL_COMP_VALUE = "VIRTUAL_COMP_VALUE";
	private static final String COM_VIRTUAL_PRIM_VALUE = "VIRTUAL_PRIM_VALUE";

	// def of data (const)
	public static final String CLASS_BINT = "BigInt";
	public static final String CLASS_LNG = "Lng";
	public static final String CLASS_INT = "Int";
	public static final String CLASS_BOOL = "Bool";
	public static final String CLASS_SIZED_ASCII = "ASCII";
	public static final String CLASS_MAIL_ADDRESS = "MailAddress";
	public static final String CLASS_BASE64 = "Base64";

	private static final String COM_STRUCT = "struct";
	private static final String COM_BOOL = "Boolean";
	private static final String COM_BINT = "BigInteger";
	private static final String COM_INT = "Integer";
	private static final String COM_LNG = "Long";
	private static final String COM_STR = "String";

	// control commands
	private static final String COM_WHEN = "if";
	private static final String COM_THEN = "then";
	private static final String COM_OTHERWISE = "else";
	private static final String COM_LOOP = "loop";
	private static final String COM_FUNCTION = "function";
	private static final String COM_RETURN = "return";
	private static final String COM_CALL = "call";

	// boolean operator commands
	private static final String COM_AND = "and";
	private static final String COM_OR = "or";
	private static final String COM_XOR = "xor";
	private static final String COM_NOT = "not";

	// boolean expression for comparison commands
	private static final String COM_SMALLER = "smaller";
	private static final String COM_EQUAL = "equal";
	private static final String COM_GREATER = "greater";

	// boolean expression for checks commands
	private static final String COM_ISNULL = "isNull";
	
	// math elementary arithmetic commands
	private static final String COM_PLUS = "plus"; // Addition
	private static final String COM_MINUS = "minus"; // Subtraction
	private static final String COM_TIMES = "times"; // Multiplication
	private static final String COM_DIVIDED_BY = "divided_by"; // Division
	private static final String COM_REMAINS = "remains"; // Remainder

	// assignment of data commands
	private static final String COM_COPY = "copy";
	private static final String COM_REFER = "refer";

	// addressing of data commands
	private static final String COM_REFERENCE = "ref";

	// parameters
	private static final String PARAM_NAME = "name";
	private static final String PARAM_VALUE = "value";
	private static final String PARAM_TYPE = "type";

	
	
	
	
	
	public static String litSimpleTypeNS() {
		return SIMPLE_TYPE_NS;
	}
	public static String litTypeSeperator() {
		return TYPE_SEPERATOR;
	}

	// data command
	public static String comContract() {
		return COM_CONTRACT;
	}

	public static String comComplex() {
		return COM_COMPLEX;
	}

	public static String comAttribute() {
		return COM_ATTRIBUTE;
	}

	public static String comValue() {
		return COM_VALUE;
	}

	public static String comVirtualCompValue() {
		return COM_VIRTUAL_COMP_VALUE;
	}

	public static String comvirtualPrimValue() {
		return COM_VIRTUAL_PRIM_VALUE;
	}

	// def of data (const)
	public static String comStructure() {
		return COM_STRUCT;
	}

	public static String comBool() {
		return COM_BOOL;
	}

	public static String comBigInt() {
		return COM_BINT;
	}

	public static String comInt() {
		return COM_INT;
	}

	public static String comLng() {
		return COM_LNG;
	}

	public static String comStr() {
		return COM_STR;
	}

	// control commands
	public static String comWhen() {
		return COM_WHEN;
	}

	public static String comThen() {
		return COM_THEN;
	}

	public static String comOtherwise() {
		return COM_OTHERWISE;
	}

	public static String comLoop() {
		return COM_LOOP;
	}

	public static String comFunction() {
		return COM_FUNCTION;
	}

	public static String comReturn() {
		return COM_RETURN;
	}

	public static String comCall() {
		return COM_CALL;
	}

	// boolean operator commands
	public static String comAnd() {
		return COM_AND;
	}

	public static String comOr() {
		return COM_OR;
	}

	public static String comXOr() {
		return COM_XOR;
	}

	public static String comNot() {
		return COM_NOT;
	}

	// boolean expression for comparison commands
	public static String comSmaller() {
		return COM_SMALLER;
	}

	public static String comEqual() {
		return COM_EQUAL;
	}

	public static String comGreater() {
		return COM_GREATER;
	}

	// boolean expression for checks commands
	public static String comIsNull() {
		return COM_ISNULL;
	}

	// math elementary arithmetic commands
	public static String comPlus() {
		return COM_PLUS;
	}

	public static String comMinus() {
		return COM_MINUS;
	}

	public static String comTimes() {
		return COM_TIMES;
	}

	public static String comDividedBy() {
		return COM_DIVIDED_BY;
	}

	public static String comRemains() {
		return COM_REMAINS;
	}

	// assignment of data commands
	public static String comCopy() {
		return COM_COPY;
	}

	public static String comRefer() {
		return COM_REFER;
	}

	// addressing of data commands
	public static String comReference() {
		return COM_REFERENCE;
	}

	// parameters
	public static String paramName() {
		return PARAM_NAME;
	}

	public static String paramValue() {
		return PARAM_VALUE;
	}

	public static String paramType() {
		return PARAM_TYPE;
	}

}
