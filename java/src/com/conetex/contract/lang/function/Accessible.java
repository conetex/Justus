package com.conetex.contract.lang.function;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Accessible<T> {

	private static Accessible<?>[]	noChildren		= new AccessibleImp<?>[] {};

	private static String[]			noParams		= new String[] {};

	private static List<CodeNode>	noChildNodes	= new LinkedList<>();

	protected Accessible() {
	}

	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract T copyFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract Class<T> getRawTypeClass();

	public abstract String getCommand();

	public abstract Accessible<?>[] getChildren();

	public Accessible<?>[] getChildrenDft() {
		return Accessible.noChildren;
	}

	public String[] getParameterDft() {
		return Accessible.noParams;
	}

	public abstract String[] getParameter();

	public CodeNode createCodeNode(TypeComplex parent) {
		Accessible<?>[] cs = this.getChildren();
		if (cs != null && cs.length > 0) {
			List<CodeNode> NodeChildren = new LinkedList<>();
			for (Accessible<?> a : cs) {
				CodeNode x = a.createCodeNode(parent);
				if (x == null) {
					System.out.println("SHIT");
				}
				NodeChildren.add(x);
			}
			return new CodeNode(parent, this.getCommand(), this.getParameter(), NodeChildren);
		}
		return new CodeNode(parent, this.getCommand(), this.getParameter(), Accessible.noChildNodes);
	}

}