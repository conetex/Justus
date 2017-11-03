package com.conetex.contract.build;

public class Symbols{

	private static final String SIMPLE_TYPE_NS = "t:";

	public static final String litSimpleTypeNS() {
		return SIMPLE_TYPE_NS;
	}

	private static final String TYPE_SEPERATOR = ".";

	public static final String litTypeSeperator() {
		return TYPE_SEPERATOR;
	}

	public static final String[] PARAM_AFFIXES = new String[] { "(", ")", "_", "-" };

	// data command
	private static final String COM_CONTRACT = "contract";

	public static final String comContract() {
		return COM_CONTRACT;
	}

	private static final String COM_COMPLEX = "complexType";

	public static final String comComplex() {
		return COM_COMPLEX;
	}

	private static final String COM_ATTRIBUTE = "attribute";

	public static final String comAttribute() {
		return COM_ATTRIBUTE;
	}

	private static final String COM_VALUE = "value";

	public static final String comValue() {
		return COM_VALUE;
	}

	private static final String COM_VIRTUAL_COMP_VALUE = "VIRTUAL_COMP_VALUE";

	public static final String comVirtualCompValue() {
		return COM_VIRTUAL_COMP_VALUE;
	}

	private static final String COM_VIRTUAL_PRIM_VALUE = "VIRTUAL_PRIM_VALUE";

	public static final String comvirtualPrimValue() {
		return COM_VIRTUAL_PRIM_VALUE;
	}

	// def of data (const)

	public static final String CLASS_BINT = "BigInt";

	public static final String CLASS_LNG = "Lng";

	public static final String CLASS_INT = "Int";

	public static final String CLASS_BOOL = "Bool";

	public static final String CLASS_SIZED_ASCII = "ASCII";

	public static final String CLASS_MAIL_ADDRESS = "MailAddress";

	public static final String CLASS_BASE64 = "Base64";

	private static final String COM_STRUCT = "struct";

	public static final String comStructure() {
		return COM_STRUCT;
	}

	private static final String COM_BOOL = "Boolean";

	public static final String comBool() {
		return COM_BOOL;
	}

	private static final String COM_BINT = "BigInteger";

	public static final String comBigInt() {
		return COM_BINT;
	}

	private static final String COM_INT = "Integer";

	public static final String comInt() {
		return COM_INT;
	}

	private static final String COM_LNG = "Long";

	public static final String comLng() {
		return COM_LNG;
	}

	private static final String COM_STR = "String";

	public static final String comStr() {
		return COM_STR;
	}

	// control commands
	private static final String COM_WHEN = "if";

	public static final String comWhen() {
		return COM_WHEN;
	}

	private static final String COM_THEN = "then";

	public static final String comThen() {
		return COM_THEN;
	}

	private static final String COM_OTHERWISE = "else";

	public static final String comOtherwise() {
		return COM_OTHERWISE;
	}

	private static final String COM_LOOP = "loop";

	public static final String comLoop() {
		return COM_LOOP;
	}

	private static final String COM_FUNCTION = "function";

	public static final String comFunction() {
		return COM_FUNCTION;
	}

	private static final String COM_RETURN = "return";

	public static final String comReturn() {
		return COM_RETURN;
	}

	private static final String COM_CALL = "call";

	public static final String comCall() {
		return COM_CALL;
	}

	// boolean operator commands
	private static final String COM_AND = "and";

	public static final String comAnd() {
		return COM_AND;
	}

	private static final String COM_OR = "or";

	public static final String comOr() {
		return COM_OR;
	}

	private static final String COM_XOR = "xor";

	public static final String comXOr() {
		return COM_XOR;
	}

	private static final String COM_NOT = "not";

	public static final String comNot() {
		return COM_NOT;
	}

	// boolean expression for comparison commands
	private static final String COM_SMALLER = "smaller";

	public static final String comSmaller() {
		return COM_SMALLER;
	}

	private static final String COM_EQUAL = "equal";

	public static final String comEqual() {
		return COM_EQUAL;
	}

	private static final String COM_GREATER = "greater";

	public static final String comGreater() {
		return COM_GREATER;
	}

	// boolean expression for checks commands
	private static final String COM_ISNULL = "isNull";

	public static final String comIsNull() {
		return COM_ISNULL;
	}

	// math elementary arithmetic commands
	private static final String COM_PLUS = "plus"; // Addition

	public static final String comPlus() {
		return COM_PLUS;
	}

	private static final String COM_MINUS = "minus"; // Subtraction

	public static final String comMinus() {
		return COM_MINUS;
	}

	private static final String COM_TIMES = "times"; // Multiplication

	public static final String comTimes() {
		return COM_TIMES;
	}

	private static final String COM_DIVIDED_BY = "divided_by"; // Division

	public static final String comDividedBy() {
		return COM_DIVIDED_BY;
	}

	private static final String COM_REMAINS = "remains"; // Remainder

	public static final String comRemains() {
		return COM_REMAINS;
	}

	// assignment of data commands
	private static final String COM_COPY = "copy";

	public static final String comCopy() {
		return COM_COPY;
	}

	private static final String COM_REFER = "refer";

	public static final String comRefer() {
		return COM_REFER;
	}

	// addressing of data commands
	private static final String COM_REFERENCE = "ref";

	public static final String comReference() {
		return COM_REFERENCE;
	}

	// parameters
	private static final String PARAM_NAME = "name";

	public static final String paramName() {
		return PARAM_NAME;
	}

	private static final String PARAM_VALUE = "value";

	public static final String paramValue() {
		return PARAM_VALUE;
	}

	private static final String PARAM_TYPE = "type";

	public static final String paramType() {
		return PARAM_TYPE;
	}

}
