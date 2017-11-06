package com.conetex.contract.build;

public class Symbols{

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

	
	
	
	
	
	public static final String litSimpleTypeNS() {
		return SIMPLE_TYPE_NS;
	}
	public static final String litTypeSeperator() {
		return TYPE_SEPERATOR;
	}

	// data command
	public static final String comContract() {
		return COM_CONTRACT;
	}

	public static final String comComplex() {
		return COM_COMPLEX;
	}

	public static final String comAttribute() {
		return COM_ATTRIBUTE;
	}

	public static final String comValue() {
		return COM_VALUE;
	}

	public static final String comVirtualCompValue() {
		return COM_VIRTUAL_COMP_VALUE;
	}

	public static final String comvirtualPrimValue() {
		return COM_VIRTUAL_PRIM_VALUE;
	}

	// def of data (const)
	public static final String comStructure() {
		return COM_STRUCT;
	}

	public static final String comBool() {
		return COM_BOOL;
	}

	public static final String comBigInt() {
		return COM_BINT;
	}

	public static final String comInt() {
		return COM_INT;
	}

	public static final String comLng() {
		return COM_LNG;
	}

	public static final String comStr() {
		return COM_STR;
	}

	// control commands
	public static final String comWhen() {
		return COM_WHEN;
	}

	public static final String comThen() {
		return COM_THEN;
	}

	public static final String comOtherwise() {
		return COM_OTHERWISE;
	}

	public static final String comLoop() {
		return COM_LOOP;
	}

	public static final String comFunction() {
		return COM_FUNCTION;
	}

	public static final String comReturn() {
		return COM_RETURN;
	}

	public static final String comCall() {
		return COM_CALL;
	}

	// boolean operator commands
	public static final String comAnd() {
		return COM_AND;
	}

	public static final String comOr() {
		return COM_OR;
	}

	public static final String comXOr() {
		return COM_XOR;
	}

	public static final String comNot() {
		return COM_NOT;
	}

	// boolean expression for comparison commands
	public static final String comSmaller() {
		return COM_SMALLER;
	}

	public static final String comEqual() {
		return COM_EQUAL;
	}

	public static final String comGreater() {
		return COM_GREATER;
	}

	// boolean expression for checks commands
	public static final String comIsNull() {
		return COM_ISNULL;
	}

	// math elementary arithmetic commands
	public static final String comPlus() {
		return COM_PLUS;
	}

	public static final String comMinus() {
		return COM_MINUS;
	}

	public static final String comTimes() {
		return COM_TIMES;
	}

	public static final String comDividedBy() {
		return COM_DIVIDED_BY;
	}

	public static final String comRemains() {
		return COM_REMAINS;
	}

	// assignment of data commands
	public static final String comCopy() {
		return COM_COPY;
	}

	public static final String comRefer() {
		return COM_REFER;
	}

	// addressing of data commands
	public static final String comReference() {
		return COM_REFERENCE;
	}

	// parameters
	public static final String paramName() {
		return PARAM_NAME;
	}

	public static final String paramValue() {
		return PARAM_VALUE;
	}

	public static final String paramType() {
		return PARAM_TYPE;
	}

}
