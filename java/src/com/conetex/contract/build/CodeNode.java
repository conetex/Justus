package com.conetex.contract.build;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel.EggAbstr;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.TypeComplex;

public class CodeNode{

	private static String getParameter(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		return thisObj.parameters[ getParameterIdx(c, p, thisObj) ];
	}
	
	private static int getParameterIdx(String c, String p, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		EggAbstr<?> command = getParameters(c, thisObj);
		return command.getParameterIndex(p);
	}
	
	private static EggAbstr<?> getParameters(String c, CodeNode thisObj) throws UnknownCommandParameter, UnknownCommand {
		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		if(thisObj.parameters == null){
			throw new UnknownCommandParameter(c);
		}
		StringBuilder error = new StringBuilder();
		for(EggAbstr<?> command : commands){
			if(command == null){
				error.append(", ").append(c);
				continue;
			}
			if(command.getParameterNames() == null){
				error.append(", 0 != ").append(thisObj.parameters.length);
			}
			else{
				if(command.getParameterNames().length == thisObj.parameters.length){
					return command;
				}
				else{
					error.append(", ").append(command.getParameterNames().length).append(" != ").append(thisObj.parameters.length);
				}
			}
		}
		throw new UnknownCommandParameter(c + "." + error);
	}
	
	public String[] getParameterNames() throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameters(this.getCommand(), this).getParameterNames();
	}
	
	public String getParameter(String p) throws UnknownCommandParameter, UnknownCommand {
		return CodeNode.getParameter(this.getCommand(), p, this);
	}

	public static String getTypSubstr(String typeName, TypeComplex parent){
		if(typeName == null){
			// TODO Exception
			System.err.println("no typeName for complex");
			return null;
		}
		if(parent != null){
			String[] typeNames = TypeComplex.splitRight(typeName);
			if(typeNames[1] != null && typeNames[0] != null){
				if(typeNames[0].equals(parent.getName())){
					typeName = typeNames[1];
				}
				else{
					// TODO Error
					System.err.println("typeName passt nicht zum parent");
					return null;
				}
			}
			return parent.getName() + "." + typeName;
		}
		return typeName;
	}
	
	private static void __checkParameter(String c, String[] p) throws UnknownCommandParameter, UnknownCommand {

		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(c);
		if(commands == null){
			throw new UnknownCommand(c);
		}
		StringBuilder error = new StringBuilder();
		outerLoop: for(EggAbstr<?> command : commands){
			String[] paramNames = command.getParameterNames();
			if(paramNames != null && !(p == null || p.length == 0)){
				if(paramNames.length == p.length){
					for(int j = 0; j < paramNames.length; j++){
						if(paramNames[j] != p[j]){
							error.append(paramNames[j]).append(" != ").append(p[j]).append(", ");
							continue outerLoop;
						}
					}
					return;
				}
				error.append(paramNames.length).append(" != ").append(p.length).append(", ");
			}
			else{
				return;
			}
		}
		throw new UnknownCommandParameter(error.toString());
	}

	private final String command;

	private final String[] parameters;

	public String[] getParameters() {
		return this.parameters;
	}

	private final List<CodeNode> children;

	public CodeNode(String theCommand, String[] theParams, List<CodeNode> theChildren) {
		this.command = theCommand;
		this.parameters = theParams;
		this.children = theChildren;
	}

	public String getCommand() {
		return this.command;
	}

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
		List<CodeNode> clonedChildren = new LinkedList<>();
        for(CodeNode c : this.children){
        	clonedChildren.add( c.cloneNode() );
		}
		return new CodeNode(this.command, this.parameters, clonedChildren);
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
