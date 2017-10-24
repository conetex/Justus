package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;

public class CodeNode {

	private static String[] commandNames = {

			/* isType                    */ Symbol.COMPLEX, //
			/* isFunction                */ Symbol.FUNCTION, //
			/* isAttribute               */ Symbol.ATTRIBUTE, //
			/* isAttributeInitialized    */ Symbol.VALUE, //

			/* VIRTUAL_COMP_VALUE        */ CommandSymbols.VIRTUAL_COMP_VALUE, //	
			/* VIRTUAL_PRIM_VALUE        */ CommandSymbols.VIRTUAL_PRIM_VALUE, //	

			/* isBuildInFunction then    */ "then", //
			/* isBuildInFunction else    */ "else", //

			// def of data (const)
			/* isBuildInFunction struct  */ Symbol.STRUCT, //
			/* isBuildInFunction Boolean */ Symbol.BOOL, //
			/* isBuildInFunction BigInt  */ Symbol.BINT, //
			/* isBuildInFunction Integer */ Symbol.INT, //
			/* isBuildInFunction Long    */ Symbol.LNG, //
			/* isBuildInFunction String  */ Symbol.STR, //

			// assignment of data
			/* isBuildInFunction         */ Symbol.COPY, //
			/* isBuildInFunction         */ Symbol.REFER, //

			// addressing of data
			/* isBuildInFunction         */ Symbol.REFERENCE, //

			// boolean operators
			/* isBuildInFunction         */ Symbol.AND, //
			/* isBuildInFunction         */ Symbol.OR, //
			/* isBuildInFunction         */ Symbol.XOR, //
			/* isBuildInFunction         */ Symbol.NOT, //

			// boolean expressions for comparison
			/* isBuildInFunction         */ Symbol.SMALLER, //
			/* isBuildInFunction         */ Symbol.EQUAL, //
			/* isBuildInFunction         */ Symbol.GREATER, //

			// boolean expressions for checks
			/* isBuildInFunction         */ Symbol.ISNULL, //

			// math elementary arithmetic
			/* isBuildInFunction         */ Symbol.PLUS, //
			/* isBuildInFunction         */ Symbol.MINUS, //
			/* isBuildInFunction         */ Symbol.TIMES, //
			/* isBuildInFunction         */ Symbol.DIVIDED_BY, //
			/* isBuildInFunction         */ Symbol.REMAINS, //

			// control function
			/* isBuildInFunction         */ Symbol.IF, //
			/* isBuildInFunction         */ Symbol.LOOP, //

			/* isBuildInFunction         */ Symbol.RETURN, //
			/* isBuildInFunction         */ Symbol.CALL, //

			/* CONTRACT                  */ CommandSymbols.CONTRACT//

	};

	private static String[][] parameterNames = {

			/*- isType                    */ { CommandParameterSymbols.NAME }, //
			/*- isFunction                */ { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE }, //
			/*- isAttribute               */ { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE }, //
			/*- isAttributeInitialized    */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE, CommandParameterSymbols.TYPE }, //

			/*- VIRTUAL_COMP_VALUE        */ { CommandParameterSymbols.NAME }, //	
			/*- VIRTUAL_PRIM_VALUE        */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //	

			/* isBuildInFunction then    */ {}, //
			/* isBuildInFunction else    */ {}, // 

			// def of data (const)
			/* isBuildInFunction struct  */ {}, //
			/*- isBuildInFunction Boolean */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //
			/*- isBuildInFunction BigInt  */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //
			/*- isBuildInFunction Integer */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //
			/*- isBuildInFunction Long    */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //
			/*- isBuildInFunction String  */ { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE }, //

			// assignment of data
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			// addressing of data
			/*- isBuildInFunction  ref    */ { CommandParameterSymbols.VALUE }, //

			// boolean operators
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			// boolean expressions for comparison
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			// boolean expressions for checks
			/* isBuildInFunction         */ {}, //

			// math elementary arithmetic
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			// control function
			/* isBuildInFunction         */ {}, //
			/* isBuildInFunction         */ {}, //

			/* isBuildInFunction         */ {}, //
			/*- isBuildInFunction   CALL  */ { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE }, //

			/*- CONTRACT                  */ { CommandParameterSymbols.NAME } //

	};

	private static String getParameter(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		Egg<?> command = CodeModel.getInstance(c);
		if(command == null){
			throw new UnknownCommand(c);
		}
		int idx = command.getParameterIndex(p);
		if(idx == -1){
			throw new UnknownCommandParameter(c + "." + p);
		}
		if (idx < thisObj.parameters.length) {
			return thisObj.parameters[idx];
		}
		else{
			throw new UnknownCommandParameter(c + "." + p + " not consistent...");
		}
		/*
		for (int i = 0; i < CodeNode.commandNames.length; i++) {
			if (CodeNode.commandNames[i] == c) {
				
				for (int j = 0; j < CodeNode.parameterNames[i].length; j++) {
					if (CodeNode.parameterNames[i][j] == p) {
						if (j < thisObj.parameters.length) {
							return thisObj.parameters[j];
						}
					}
				}
				throw new UnknownCommandParameter(c + "." + p);
			}
		}
		throw new UnknownCommand(c);
		*/
	}

	private String getParameter(String p) throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameter(this.getCommand(), p, this);
	}

	private static void checkParameter(String c, String[] p) throws UnknownCommandParameter, UnknownCommand {
		Egg<?> command = CodeModel.getInstance(c);
		if(command == null){
			throw new UnknownCommand(c);
		}
		String[] commands = command.getParameters();
		if (commands.length == p.length) {
			for (int j = 0; j < commands.length; j++) {
				if (commands[j] != p[j]) {
					throw new UnknownCommandParameter(commands[j] + " != " + p[j]);
				}
			}
		}
		else{
			throw new UnknownCommandParameter(commands.length + " != " + p.length);
		}
		/*
		boolean commandNotFound = true;
		for (int i = 0; i < CodeNode.commandNames.length; i++) {
			if (CodeNode.commandNames[i] == c) {
				commandNotFound = false;
				if (CodeNode.parameterNames[i].length == p.length) {
					for (int j = 0; j < CodeNode.parameterNames[i].length; j++) {
						if (CodeNode.parameterNames[i][j] != p[j]) {
							throw new UnknownCommandParameter(CodeNode.parameterNames[i][j] + " != " + p[j]);
						}
					}
				}
				else {
					throw new UnknownCommandParameter(CodeNode.parameterNames[i].length + " != " + p.length);
				}
			}
		}
		if (commandNotFound) {
			throw new UnknownCommand(c);
		}
		*/
	}

	@SuppressWarnings("unused")
	private static int getParameterCount(String c) throws UnknownCommand {
		for (int i = 0; i < CodeNode.commandNames.length; i++) {
			if (CodeNode.commandNames[i] == c) {
				return CodeNode.parameterNames.length;
			}
		}
		throw new UnknownCommand(c);
	}

	private String command;

	private String[] parameters;

	private List<CodeNode> children;

	public static CodeNode create(String theName, String theNameAttribute, String theValue, String theType) throws UnknownCommandParameter, UnknownCommand {
		return create(theName, theNameAttribute, theValue, theType, new LinkedList<CodeNode>());
	}

	public static CodeNode create(String theNameOrg, String theNameAttributeOrg, String theValue, String theType, List<CodeNode> theChildren)
			throws UnknownCommandParameter, UnknownCommand {

		String theName = theNameOrg;
		String theNameAttribute = theNameAttributeOrg;

		if (theName == null || theName.length() == 0) {
			return null;
		}
		if (!(isType(theName) || isFunction(theName) || isAttribute(theName) || isAttributeInitialized(theName) || isBuildInFunction(theName))) {
			if (theNameAttribute == null) {
				theNameAttribute = theName;
				if (theValue == null) {
					theName = CommandSymbols.VIRTUAL_COMP_VALUE;
				}
				else {
					theName = CommandSymbols.VIRTUAL_PRIM_VALUE;
				}
			}
		}

		if (theNameAttribute == null) {
			if (theValue == null) {
				if (theType == null) {
					checkParameter(theName, new String[] {});
					return new CodeNode(theName, new String[] {}, theChildren);
				}
				else {
					checkParameter(theName, new String[] { CommandParameterSymbols.TYPE });
					return new CodeNode(theName, new String[] { theType }, theChildren);
				}
			}
			else if (theType == null) {
				checkParameter(theName, new String[] { CommandParameterSymbols.VALUE });
				return new CodeNode(theName, new String[] { theValue }, theChildren);
			}
			else {
				checkParameter(theName, new String[] { CommandParameterSymbols.VALUE, CommandParameterSymbols.TYPE });
				return new CodeNode(theName, new String[] { theValue, theType }, theChildren);
			}
		}
		else if (theValue == null) {
			if (theType == null) {
				checkParameter(theName, new String[] { CommandParameterSymbols.NAME });
				return new CodeNode(theName, new String[] { theNameAttribute }, theChildren);
			}
			else {
				checkParameter(theName, new String[] { CommandParameterSymbols.NAME, CommandParameterSymbols.TYPE });
				return new CodeNode(theName, new String[] { theNameAttribute, theType }, theChildren);
			}
		}
		else if (theType == null) {
			checkParameter(theName, new String[] { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE });
			return new CodeNode(theName, new String[] { theNameAttribute, theValue }, theChildren);
		}
		else {
			checkParameter(theName, new String[] { CommandParameterSymbols.NAME, CommandParameterSymbols.VALUE, CommandParameterSymbols.TYPE });
			return new CodeNode(theName, new String[] { theNameAttribute, theValue, theType }, theChildren);
		}
	}

	private CodeNode(String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;

		//this._type = theType;
	}

	public String getCommand() {
		return this.command;
	}

	public String getName() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.NAME);
		// return this.name;
	}

	public String getValue() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.VALUE);
		// return this.value;
	}

	public String getType() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.TYPE);
		//return this._type;
	}

	public boolean isType() {
		return isType(this.command);
	}

	private static boolean isType(String theCommand) {
		if (theCommand.equals(Symbol.COMPLEX)) {
			return true;
		}
		return false;
	}

	public boolean isFunction() {
		return isFunction(this.command);
	}

	private static boolean isFunction(String theCommand) {
		if (theCommand.equals(Symbol.FUNCTION)) {
			return true;
		}
		return false;
	}

	public boolean isAttribute() {
		return isAttribute(this.command);
	}

	private static boolean isAttribute(String theCommand) {
		return theCommand.equals(Symbol.ATTRIBUTE);
	}

	public boolean isAttributeInitialized() {
		return isAttributeInitialized(this.command);
	}

	private static boolean isAttributeInitialized(String theCommand) {
		return theCommand.equals(Symbol.VALUE);
	}

	public boolean isBuildInFunction() {
		return isBuildInFunction(this.command);
	}

	private static boolean isBuildInFunction(String thiscommand) {
		if (thiscommand.equals(Symbol.PLUS) || thiscommand.equals(Symbol.MINUS) || thiscommand.equals(Symbol.TIMES) || thiscommand.equals(Symbol.DIVIDED_BY)
				|| thiscommand.equals(Symbol.REMAINS) || thiscommand.equals(Symbol.SMALLER) || thiscommand.equals(Symbol.GREATER)
				|| thiscommand.equals(Symbol.EQUAL) || thiscommand.equals(Symbol.AND) || thiscommand.equals(Symbol.OR) || thiscommand.equals(Symbol.XOR)
				|| thiscommand.equals(Symbol.NOT) || thiscommand.equals(Symbol.REFERENCE) || thiscommand.equals(Symbol.COPY)
				|| thiscommand.equals(Symbol.FUNCTION) || thiscommand.equals(Symbol.RETURN) || thiscommand.equals(Symbol.CALL) || thiscommand.equals(Symbol.IF)
				|| thiscommand.equals(Symbol.LOOP) || thiscommand.equals("then") || thiscommand.equals("else") || thiscommand.equals(Symbol.INT)) {
			return true;
		}
		return false;
	}

	public boolean isValue() {
		return isValue(this.command);
	}

	private static boolean isValue(String thiscommand) {
		if (isType(thiscommand) ||
		// isFunction(thiscommand) ||
				isAttribute(thiscommand) ||
				// isAttributeInitialized(thiscommand) ||
				isBuildInFunction(thiscommand)) {
			return false;
		}
		else {
			return true;
		}
	}

	public CodeNode getChildElementByIndex(int index) {
		if (this.children == null) {
			return null;
		}
		if (index >= 0 && index < this.children.size()) {
			return this.children.get(index);
		}
		return null;
	}

	public List<CodeNode> getChildNodes() {
		return this.children;
	}

	public int getChildNodesSize() {
		if (this.children == null) {
			return 0;
		}
		return this.children.size();
	}

}
