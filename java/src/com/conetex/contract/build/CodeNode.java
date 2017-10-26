package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;

public class CodeNode {

	private static String getParameter(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		List<Egg<?>> commands = CodeModel.Egg.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		String error = "";
		for(Egg<?> command : commands) {
			if ( command.getParameters().length == thisObj.parameters.length ) {
				int idx = command.getParameterIndex(p);
				if(idx > -1){
					//error += c + "." + p + ", ";
					return thisObj.parameters[idx];
				}
			}
			else{
				error += ", " + command.getParameters().length + " != " + thisObj.parameters.length;
			}
		}
		throw new UnknownCommandParameter( c + "." + p + " " + error);
		
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
		
		List<Egg<?>> commands = CodeModel.Egg.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		String error = "";
		outerLoop:
		for(Egg<?> command : commands) {
			String[] paramNames = command.getParameters();
			if(paramNames != null && !(p == null || p.length == 0)) {
				if (paramNames.length == p.length) {
					for (int j = 0; j < paramNames.length; j++) {
						if (paramNames[j] != p[j]) {
							error += paramNames[j] + " != " + p[j] + ", ";
							continue outerLoop;
						}
					}
					return;
				}
				error += paramNames.length + " != " + p.length + ", ";
			}
			else {
				return;
			}
		}
		throw new UnknownCommandParameter(error);
		
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
					checkParameter(theName, null);
					return new CodeNode(theName, null, theChildren);
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
