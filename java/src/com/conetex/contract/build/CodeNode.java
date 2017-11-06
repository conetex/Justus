package com.conetex.contract.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;

public class CodeNode{

	private static String getParameter(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		return thisObj.parameters[ getParameterIdx(c, p, thisObj) ];
	}

	private static int getParameterIdx(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		List<Egg<?>> commands = CodeModel.Egg.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		if(thisObj.parameters == null){
			throw new UnknownCommandParameter(c + "." + p);
		}
		String error = "";
		for(Egg<?> command : commands){
			if(command == null){
				error += ", " + c + " - " + p;
			}
			if(command.getParameters() == null){
				error += ", 0 != " + thisObj.parameters.length;
			}
			else{
				if(command.getParameters().length == thisObj.parameters.length){
					return command.getParameterIndex(p);
				}
				else{
					error += ", " + command.getParameters().length + " != " + thisObj.parameters.length;
				}
			}
		}
		throw new UnknownCommandParameter(c + "." + p + " " + error);
	}
	
	public String getParameter(String p) throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameter(this.getCommand(), p, this);
	}

	private static void checkParameter(String c, String[] p) throws UnknownCommandParameter, UnknownCommand {

		List<Egg<?>> commands = CodeModel.Egg.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		String error = "";
		outerLoop: for(Egg<?> command : commands){
			String[] paramNames = command.getParameters();
			if(paramNames != null && !(p == null || p.length == 0)){
				if(paramNames.length == p.length){
					for(int j = 0; j < paramNames.length; j++){
						if(paramNames[j] != p[j]){
							error += paramNames[j] + " != " + p[j] + ", ";
							continue outerLoop;
						}
					}
					return;
				}
				error += paramNames.length + " != " + p.length + ", ";
			}
			else{
				return;
			}
		}
		throw new UnknownCommandParameter(error);
	}

	private String command;

	private String[] parameters;

	private List<CodeNode> children;

	public static CodeNode _create(String command, String theNameAttribute, String theValue, String theType) throws UnknownCommandParameter, UnknownCommand {
		return _create(command, theNameAttribute, theValue, theType, new LinkedList<CodeNode>());
	}

	public static CodeNode _create(String commandOrg, String theNameAttributeOrg, String theValue, String theType, List<CodeNode> theChildren)
			throws UnknownCommandParameter, UnknownCommand {

		String command = commandOrg;
		String theNameAttribute = theNameAttributeOrg;

		if(command == null || command.length() == 0){
			return null;
		}

		List<Egg<?>> x = Egg.getInstance(commandOrg);
		if(x == null){
			//if (!(isType(theName) || isFunction(theName) || isAttribute(theName) || isAttributeInitialized(theName) || isBuildInFunction(theName))) {
			if(theNameAttribute == null){
				theNameAttribute = command;
				if(theValue == null){
					command = Symbols.comVirtualCompValue();
				}
				else{
					command = Symbols.comvirtualPrimValue();
				}
			}
		}

		if(theNameAttribute == null){
			if(theValue == null){
				if(theType == null){
					checkParameter(command, null);
					return new CodeNode(command, null, theChildren);
				}
				else{
					checkParameter(command, new String[] { Symbols.paramType() });
					return new CodeNode(command, new String[] { theType }, theChildren);
				}
			}
			else if(theType == null){
				checkParameter(command, new String[] { Symbols.paramValue() });
				return new CodeNode(command, new String[] { theValue }, theChildren);
			}
			else{
				checkParameter(command, new String[] { Symbols.paramValue(), Symbols.paramType() });
				return new CodeNode(command, new String[] { theValue, theType }, theChildren);
			}
		}
		else if(theValue == null){
			if(theType == null){
				checkParameter(command, new String[] { Symbols.paramName() });
				return new CodeNode(command, new String[] { theNameAttribute }, theChildren);
			}
			else{
				checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramType() });
				return new CodeNode(command, new String[] { theNameAttribute, theType }, theChildren);
			}
		}
		else if(theType == null){
			checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramValue() });
			return new CodeNode(command, new String[] { theNameAttribute, theValue }, theChildren);
		}
		else{
			checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() });
			return new CodeNode(command, new String[] { theNameAttribute, theValue, theType }, theChildren);
		}
	}

	public CodeNode(String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;
	}

	public String getCommand() {
		return this.command;
	}

	/*
	public String _getName() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.NAME);
	}
	
	public String _getValue() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.VALUE);
	}
	
	public String _getType() throws UnknownCommandParameter, UnknownCommand {
		return this.getParameter(CommandParameterSymbols.TYPE);
	}
	
	private static boolean isType(String theCommand) {
		if (theCommand.equals(Symbol.COMPLEX)) {
			return true;
		}
		return false;
	}
	
	private static boolean isFunction(String theCommand) {
		if (theCommand.equals(Symbol.FUNCTION)) {
			return true;
		}
		return false;
	}
	
	private static boolean isAttribute(String theCommand) {
		return theCommand.equals(Symbol.ATTRIBUTE);
	}
	
	private static boolean isAttributeInitialized(String theCommand) {
		return theCommand.equals(Symbol.VALUE);
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
	
	*/

	public CodeNode getChildElementByIndex(int index) {
		if(this.children == null){
			return null;
		}
		if(index >= 0 && index < this.children.size()){
			return this.children.get(index);
		}
		return null;
	}

	public List<CodeNode> getChildNodes() {
		return this.children;
	}

	public int getChildNodesSize() {
		if(this.children == null){
			return 0;
		}
		return this.children.size();
	}
	
	public CodeNode cloneNode(){
		String[] parameters = this.parameters;
		List<CodeNode> children = new LinkedList<>();;
		for(CodeNode c : this.children){
			children.add( c.cloneNode() );
		}
		return new CodeNode(this.command, parameters, children);
	}

	public void setParameter(String p, Object x) throws UnknownCommandParameter, UnknownCommand {
		if(x == null){
			this.parameters[ getParameterIdx(this.command, p, this) ] = null;
		}
		else{
			this.parameters[ getParameterIdx(this.command, p, this) ] = x.toString();			
		}
	}

}
