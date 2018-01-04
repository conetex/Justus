package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel.EggAbstr;
import com.conetex.contract.build.CodeModel.EggAbstrImp;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.TypeComplex;

public class CodeNode {// TODO ziel sollte sein dass die Klasse package-Scope
						// hat ( class CodeNode{ )

	private static CodeNode	rootComplex;

	private static CodeNode	rootValue;

	public static void init(CodeNode code) throws AbstractInterpreterException {
		if (code == null) {
			throw new AbstractInterpreterException("no root of syntax tree");
		}
		if (code.getCommand() == Symbols.comContract()) {
			if (code.hasParameter(Symbols.paramName())) {
				CodeNode.rootComplex = code;
				CodeNode.rootValue = code;
			}
			else {
				for (CodeNode n : code.children) {
					if (n.getCommand() == Symbols.comComplex()) {
						CodeNode.rootComplex = n;
					}
					if (n.getCommand() == Symbols.comVirtualCompValue()) {
						CodeNode.rootValue = n;
					}
				}
				if (CodeNode.rootComplex == null) {
					throw new AbstractInterpreterException("no rootComplex");
				}
				if (CodeNode.rootValue == null) {
					throw new AbstractInterpreterException("no rootValue");
				}
			}
		}
		else {
			throw new AbstractInterpreterException("no contract");
		}
	}

	public static CodeNode getComplexRoot() {
		return CodeNode.rootComplex;
	}

	public static CodeNode getValueRoot() {
		return CodeNode.rootValue;
	}

	private static String getParameter(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		return thisObj.parameters[getParameterIdx(c, p, thisObj)];
	}

	private static int getParameterIdx(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		EggAbstr<?> command = getParameters(c, thisObj);
		return command.getParameterIndex(p);
	}

	private static EggAbstr<?> getParameters(String c, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(c);
		if (commands == null) {
			throw new UnknownCommand(c);
		}
		if (thisObj.parameters == null) {
			throw new UnknownCommandParameter(c);
		}
		StringBuilder error = new StringBuilder();
		for (EggAbstr<?> command : commands) {
			if (command == null) {
				error.append(", ").append(c);
				continue;
			}
			if (command.getParameterNames() == null) {
				if (thisObj.parameters.length == 0) {
					return command;
				}
				else {
					error.append(", 0 != ").append(thisObj.parameters.length);
				}
			}
			else {
				if (command.getParameterNames().length == thisObj.parameters.length) {
					return command;
				}
				else {
					error.append(", ").append(command.getParameterNames().length).append(" != ").append(thisObj.parameters.length);
				}
			}
		}
		throw new UnknownCommandParameter(c + "." + error);
	}

	public boolean hasParameter(String p) throws UnknownCommandParameter, UnknownCommand {
		if (p == null) {
			throw new UnknownCommandParameter("null");
		}
		String[] names = this.getParameterNames();
		for (String n : names) {
			if (n.equals(p)) {
				return true;
			}
		}
		return false;
	}

	public String[] getParameterNames() throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameters(this.getCommand(), this).getParameterNames();
	}

	public String getParameter(String p) throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameter(this.getCommand(), p, this);
	}

	public static String getTypSubstring(String typeName, String parentName) {
		if (typeName == null) {
			// TODO Exception
			System.err.println("no typeName for complex");
			return null;
		}
		String re = typeName;
		if (parentName != null) {
			String[] typeNames = Symbols.splitRight(typeName);
			if (typeNames[1] != null && typeNames[0] != null) {
				if (typeNames[0].equals(parentName)) {
					re = typeNames[1];
				}
				else {
					// TODO Error
					System.err.println("typeName passt nicht zum parent");
					return null;
				}
			}
			return parentName + "." + re;
		}
		return re;
	}

	public static String getTypSubstr(String typeName, TypeComplex parent) {
		if (parent != null) {
			return getTypSubstring(typeName, parent.getName());
		}
		return getTypSubstring(typeName, null);
	}

	public static CodeNode __create(String command, String theNameAttribute, String theValue, String theType) throws UnknownCommandParameter, UnknownCommand {
		return __create(command, theNameAttribute, theValue, theType, new LinkedList<>());
	}

	private static CodeNode __create(String commandOrg, String theNameAttributeOrg, String theValue, String theType, List<CodeNode> theChildren)
			throws UnknownCommandParameter, UnknownCommand {

		String command = commandOrg;
		String theNameAttribute = theNameAttributeOrg;

		if (command == null || command.length() == 0) {
			return null;
		}

		List<EggAbstr<?>> x = EggAbstrImp.getInstance(commandOrg);
		if (x == null) {
			// if (!(isType(theName) || isFunction(theName) ||
			// isAttribute(theName) || isAttributeInitialized(theName) ||
			// isBuildInFunction(theName))) {
			if (theNameAttribute == null) {
				theNameAttribute = command;
				if (theValue == null) {
					command = Symbols.comVirtualCompValue();
				}
				else {
					command = Symbols.comvirtualPrimValue();
				}
			}
		}

		if (theNameAttribute == null) {
			if (theValue == null) {
				if (theType == null) {
					__checkParameter(command, null);
					return new CodeNode(command, null, theChildren);
				}
				else {
					__checkParameter(command, new String[] { Symbols.paramType() });
					return new CodeNode(command, new String[] { theType }, theChildren);
				}
			}
			else if (theType == null) {
				__checkParameter(command, new String[] { Symbols.paramValue() });
				return new CodeNode(command, new String[] { theValue }, theChildren);
			}
			else {
				__checkParameter(command, new String[] { Symbols.paramValue(), Symbols.paramType() });
				return new CodeNode(command, new String[] { theValue, theType }, theChildren);
			}
		}
		else if (theValue == null) {
			if (theType == null) {
				__checkParameter(command, new String[] { Symbols.paramName() });
				return new CodeNode(command, new String[] { theNameAttribute }, theChildren);
			}
			else {
				__checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramType() });
				return new CodeNode(command, new String[] { theNameAttribute, theType }, theChildren);
			}
		}
		else if (theType == null) {
			__checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramValue() });
			return new CodeNode(command, new String[] { theNameAttribute, theValue }, theChildren);
		}
		else {
			__checkParameter(command, new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() });
			return new CodeNode(command, new String[] { theNameAttribute, theValue, theType }, theChildren);
		}
	}

	private static void __checkParameter(String c, String[] p) throws UnknownCommandParameter, UnknownCommand {

		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(c);
		if (commands == null) {
			throw new UnknownCommand(c);
		}
		StringBuilder error = new StringBuilder();
		outerLoop: for (EggAbstr<?> command : commands) {
			String[] paramNames = command.getParameterNames();
			if (paramNames != null && !(p == null || p.length == 0)) {
				if (paramNames.length == p.length) {
					for (int j = 0; j < paramNames.length; j++) {
						if (paramNames[j] != p[j]) {
							error.append(paramNames[j]).append(" != ").append(p[j]).append(", ");
							continue outerLoop;
						}
					}
					return;
				}
				error.append(paramNames.length).append(" != ").append(p.length).append(", ");
			}
			else {
				return;
			}
		}
		throw new UnknownCommandParameter(error.toString());
	}

	private final String	command;

	private final String[]	parameters;

	public String[] getParameters() {
		return this.parameters;
	}

	private final List<CodeNode> children;

	public CodeNode(TypeComplex parent, String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;
	}

	public CodeNode(String parent, String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;
	}

	private CodeNode(String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;
	}

	public String getCommand() {
		return this.command;
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

	public CodeNode cloneNode() {
		List<CodeNode> clonedChildren = new LinkedList<>();
		for (CodeNode c : this.children) {
			clonedChildren.add(c.cloneNode());
		}
		return new CodeNode(this.command, this.parameters, clonedChildren);
	}

	public void setParameter(String p, Object x) throws UnknownCommandParameter, UnknownCommand {
		if (x == null) {
			this.parameters[getParameterIdx(this.command, p, this)] = null;
		}
		else {
			this.parameters[getParameterIdx(this.command, p, this)] = x.toString();
		}
	}
	
	public String toString(){
		return this.command;
	}

}
