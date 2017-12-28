package com.conetex.contract.build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.build.BuildFunctions.Assign;
import com.conetex.contract.build.BuildFunctions.Constant;
import com.conetex.contract.build.BuildFunctions.Control;
import com.conetex.contract.build.BuildFunctions.Expression;
import com.conetex.contract.build.BuildFunctions.Fun;
import com.conetex.contract.build.BuildFunctions.FunCall;
import com.conetex.contract.build.BuildFunctions.FunReturn;
import com.conetex.contract.build.BuildFunctions.Reference;
import com.conetex.contract.build.BuildTypes.TypeComplexTemp;
import com.conetex.contract.build.BuildTypes.Types;
import com.conetex.contract.build.BuildValues.Values;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateOperation;
import com.conetex.contract.build.exceptionFunction.OperationMeansNotCalled;
import com.conetex.contract.build.exceptionFunction.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class CodeModel {

	public static interface EggAbstr<T> {

		String getName();

		void checkMeaning(CodeNode c) throws AbstractInterpreterException;

		Set<String> keySet();

		void means(String theOperationName) throws DublicateOperation;

		void means(String[] theOperationNames) throws DublicateOperation;

		void registerParameters(String[] theParameterNames);

		public int getParameterIndex(String p) throws UnknownCommandParameter;

		public String[] getParameterNames();

		public int getParameterCount();

	}

	public static abstract class EggAbstrImp<T> implements EggAbstr<T> {

		private static final Map<String, List<EggAbstr<?>>> instances = new HashMap<>();

		public static List<EggAbstr<?>> getInstance(String command) {
			return EggAbstrImp.instances.get(command);
		}

		private final String		name;

		private String[]			parameterNames;

		private final Set<String>	meaning	= new HashSet<>();

		EggAbstrImp(String theName) {
			this.name = theName;
		}

		public final String getName() {
			return this.name;
		}

		public final void checkMeaning(CodeNode c) throws AbstractInterpreterException {
			if (!this.meaning.contains(c.getCommand())) {
				System.err.println("Operation " + c.getCommand() + " not found!");
				throw new UnknownCommand("Operation " + c.getCommand() + " not found!");
			}
		}

		public final Set<String> keySet() {
			return this.meaning;
		}

		public void means(String theOperationName) throws DublicateOperation {
			if (this.meaning.contains(theOperationName)) {
				throw new DublicateOperation("duplicate operation '" + theOperationName + "' in " + this.getName());
			}
			this.meaning.add(theOperationName);
			List<EggAbstr<?>> instanceList = EggAbstrImp.getInstance(theOperationName);
			if (instanceList == null) {
				instanceList = new LinkedList<>();
				EggAbstrImp.instances.put(theOperationName, instanceList);
			}
			instanceList.add(this);
		}

		public final void means(String[] theOperationNames) throws DublicateOperation {
			for (String theOperationName : theOperationNames) {
				this.means(theOperationName);
			}
		}

		public final void registerParameters(String[] theParameterNames) {
			if (this.parameterNames != null) {
				System.err.println("duplicate Param call");
			}
			this.parameterNames = theParameterNames;
		}

		public final int getParameterIndex(String p) throws UnknownCommandParameter {
			if (this.parameterNames == null) {
				System.out.println("Mist NEU");
			}
			for (int j = 0; j < this.parameterNames.length; j++) {
				if (this.parameterNames[j] == p) {
					return j;
				}
			}
			throw new UnknownCommandParameter(p);
		}

		public final String[] getParameterNames() {
			return this.parameterNames;
		}

		public final int getParameterCount() {
			if (this.parameterNames == null) {
				return 0;
			}
			return this.parameterNames.length;
		}

	}

	public static interface EggType<T> extends EggAbstr<T> {

		Attribute<?> typeCreateAttributeThis(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException;

		public Attribute<?> typeCreateAttribute(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException;

		TypeComplex typeCreateComplexThis(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException;

		public TypeComplex typeCreateComplex(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException;

	}

	public static abstract class EggTypeImp<T> extends EggAbstrImp<T> implements EggType<T> {

		EggTypeImp(String theName) {
			super(theName);
		}

		public final Attribute<?> typeCreateAttributeThis(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(c);
			return this.typeCreateAttribute(c, unformedComplexTypes);
		}

		public Attribute<?> typeCreateAttribute(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		public final TypeComplex typeCreateComplexThis(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			super.checkMeaning(n);
			return this.typeCreateComplex(n, parent, unformedComplexTypes);
		}

		public TypeComplex typeCreateComplex(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			return null;
		}

	}

	public static interface BoxType<T, S> extends EggType<T> {

		void typeContains(String theOperationName, EggType<? extends S> b);

		void typeContains(EggType<? extends S> b) throws OperationMeansNotCalled;

		EggType<? extends S> typeGetChildBuilder(CodeNode n) throws AbstractInterpreterException;

		public Attribute<?> typeCreateAttributeChild(CodeNode child, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException;

		public TypeComplex typeCreateComplexChild(CodeNode child, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException;

	}

	public static class BoxTypeImp<T, S> extends EggTypeImp<T> implements BoxType<T, S> {

		private final Map<String, EggType<? extends S>> childBuilder = new HashMap<>();

		public BoxTypeImp(String theName) {
			super(theName);
		}

		public final void typeContains(String theOperationName, EggType<? extends S> b) {
			if (this.childBuilder.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void typeContains(EggType<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.typeContains(s, b);
			}
		}

		public EggType<? extends S> typeGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggType<? extends S> s = this.childBuilder.get(name);
			if (s == null) {
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Attribute<?> typeCreateAttributeChild(CodeNode child, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			EggType<? extends S> cb = this.typeGetChildBuilder(child);
			return cb.typeCreateAttributeThis(child, unformedComplexTypes);
		}

		public final TypeComplex typeCreateComplexChild(CodeNode child, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			EggType<? extends S> cb = this.typeGetChildBuilder(child);
			return cb.typeCreateComplexThis(child, parent, unformedComplexTypes);
		}

	}

	public static interface EggFun<T> extends EggAbstr<T> {

		public Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

	}

	public static abstract class EggFunImp<T> extends EggAbstrImp<T> implements EggFun<T> {

		EggFunImp(String theName) {
			super(theName);
		}

		public final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
			this.checkMeaning(n);
			Accessible<? extends T> re = this.functionCreate(n, parentTyp);
			return re;
		}

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
			return null;
		}

	}

	public static interface BoxFun<T, S> extends EggFun<T> {

		void functionContains(String theOperationName, EggFun<? extends S> b);

		void functionContains(EggFun<? extends S> b) throws OperationMeansNotCalled;

		EggFun<? extends S> functionGetChildBuilder(CodeNode n) throws AbstractInterpreterException;

		public Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

	}

	public static abstract class BoxFunImp<T, S> extends EggFunImp<T> implements BoxFun<T, S> {

		private final Map<String, EggFun<? extends S>> childBuilder = new HashMap<>();

		public BoxFunImp(String theName) {
			super(theName);
		}

		public final void functionContains(String theOperationName, EggFun<? extends S> b) {
			if (this.childBuilder.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		@Override
		public final void functionContains(EggFun<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.functionContains(s, b);
			}
		}

		public EggFun<? extends S> functionGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggFun<? extends S> s = this.childBuilder.get(name);
			if (s == null) {
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
			EggFun<? extends S> cb = this.functionGetChildBuilder(child);
			return cb.functionCreateThis(child, parentTyp);
		}

	}

	public static interface EggValue<T> extends EggAbstr<T> {

		Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

		public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

	}

	public static abstract class EggValueImp<T> extends EggAbstrImp<T> implements EggValue<T> {

		EggValueImp(String theName) {
			super(theName);
		}

		public final Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.valueCreate(n, parentTyp, parentData);
		}

		public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			return null;
		}

	}

	public static interface BoxValue<T, S> extends EggValue<T> {

		void valueContains(String theOperationName, EggValue<? extends S> b);

		void valueContains(EggValue<? extends S> b) throws OperationMeansNotCalled;

		EggValue<? extends S> valueGetChildBuilder(CodeNode n) throws AbstractInterpreterException;

		public Value<?> valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

	}

	public static class BoxValueImp<T, S> extends EggValueImp<T> implements BoxValue<T, S> {

		private final Map<String, EggValue<? extends S>> childBuilder = new HashMap<>();

		public BoxValueImp(String theName) {
			super(theName);
		}

		public final void valueContains(String theOperationName, EggValue<? extends S> b) {
			if (this.childBuilder.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void valueContains(EggValue<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.valueContains(s, b);
			}
		}

		public EggValue<? extends S> valueGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggValue<? extends S> s = this.childBuilder.get(name);
			if (s == null) {
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Value<?> valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			EggValue<? extends S> cb = this.valueGetChildBuilder(child);
			return cb.valueCreateThis(child, parentTyp, parentData);
		}

	}

	public abstract static class BoxValueTypImp<T, S> extends EggAbstrImp<T> implements BoxValue<T, S>, BoxType<T, S> {
		BoxValueImp<T, S>	delegateValue	= new BoxValueImp<T, S>(null);
		BoxTypeImp<T, S>	delegateType	= new BoxTypeImp<T, S>(null);

		BoxValueTypImp(String theName) {
			super(theName);
		}

		@Override
		public void means(String theOperationName) throws DublicateOperation {
			this.delegateValue.means(theOperationName);
			this.delegateType.means(theOperationName);
			super.means(theOperationName);
		}

		@Override
		public Attribute<?> typeCreateAttributeThis(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
			// return this.delegateType.typeCreateAttributeThis(c,
			// unformedComplexTypes);
			super.checkMeaning(c);
			return this.typeCreateAttribute(c, unformedComplexTypes);
		}

		@Override
		public abstract Attribute<?> typeCreateAttribute(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException;

		@Override
		public TypeComplex typeCreateComplexThis(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			// return this.delegateType.typeCreateComplexThis( n, parent,
			// unformedComplexTypes);
			super.checkMeaning(n);
			return this.typeCreateComplex(n, parent, unformedComplexTypes);
		}

		@Override
		public abstract TypeComplex typeCreateComplex(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException;

		@Override
		public void typeContains(String theOperationName, EggType<? extends S> b) {
			this.delegateType.typeContains(theOperationName, b);
		}

		@Override
		public void typeContains(EggType<? extends S> b) throws OperationMeansNotCalled {
			this.delegateType.typeContains(b);
		}

		@Override
		public EggType<? extends S> typeGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			return typeGetChildBuilder(n);
		}

		@Override
		public Attribute<?> typeCreateAttributeChild(CodeNode child, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
			return this.delegateType.typeCreateAttributeChild(child, unformedComplexTypes);
		}

		@Override
		public TypeComplex typeCreateComplexChild(CodeNode child, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			return this.delegateType.typeCreateComplexChild(child, parent, unformedComplexTypes);
		}

		@Override
		public Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			// return this.delegateValue.valueCreateThis(n, parentTyp,
			// parentData);
			super.checkMeaning(n);
			return this.valueCreate(n, parentTyp, parentData);
		}

		@Override
		public abstract Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

		@Override
		public void valueContains(String theOperationName, EggValue<? extends S> b) {
			this.delegateValue.valueContains(theOperationName, b);
		}

		@Override
		public void valueContains(EggValue<? extends S> b) throws OperationMeansNotCalled {
			this.delegateValue.valueContains(b);
		}

		@Override
		public EggValue<? extends S> valueGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			return this.delegateValue.valueGetChildBuilder(n);
		}

		@Override
		public Value<?> valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			return this.delegateValue.valueCreateChild(child, parentTyp, parentData);
		}

	}

	public abstract static class BoxValueTypeFunImp<T, S> extends EggAbstrImp<T> implements BoxValue<T, S>, BoxFun<T, S>, BoxType<T, S> {

		BoxValueTypeFunImp(String theName) {
			super(theName);
		}

		private final Map<String, EggValue<? extends S>> childBuilderValue = new HashMap<>();

		@Override
		public final Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.valueCreate(n, parentTyp, parentData);
		}

		@Override
		public abstract Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

		@Override
		public final void valueContains(String theOperationName, EggValue<? extends S> b) {
			if (this.childBuilderValue.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderValue.put(theOperationName, b);
		}

		@Override
		public final void valueContains(EggValue<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.valueContains(s, b);
			}
		}

		@Override
		public EggValue<? extends S> valueGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggValue<? extends S> s = this.childBuilderValue.get(name);
			if (s == null) {
				EggFun<? extends S> sF = this.childBuilderFun.get(name);
				if (sF == null) {
					EggType<? extends S> sT = this.childBuilderType.get(name);
					if (sT == null) {
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}
			return s;
		}

		@Override
		public final Value<?> valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			EggValue<? extends S> cb = this.valueGetChildBuilder(child);
			if (cb != null) {
				return cb.valueCreateThis(child, parentTyp, parentData);
			}
			return null;
		}

		private final Map<String, EggFun<? extends S>> childBuilderFun = new HashMap<>();

		@Override
		public final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
			this.checkMeaning(n);
			return this.functionCreate(n, parentTyp);
		}

		@Override
		public abstract Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

		@Override
		public final void functionContains(String theOperationName, EggFun<? extends S> b) {
			if (this.childBuilderFun.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderFun.put(theOperationName, b);
		}

		@Override
		public final void functionContains(EggFun<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.functionContains(s, b);
			}
		}

		@Override
		public EggFun<? extends S> functionGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggFun<? extends S> s = this.childBuilderFun.get(name);
			if (s == null) {
				EggValue<? extends S> sV = this.childBuilderValue.get(name);
				if (sV == null) {
					EggType<? extends S> sT = this.childBuilderType.get(name);
					if (sT == null) {
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}
			return s;
		}

		@Override
		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp)
				throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
			EggFun<? extends S> cb = this.functionGetChildBuilder(child);
			if (cb == null) {
				return null;
			}
			return cb.functionCreateThis(child, parentTyp);
		}

		private final Map<String, EggType<? extends S>> childBuilderType = new HashMap<>();

		@Override
		public final void typeContains(String theOperationName, EggType<? extends S> b) {
			if (this.childBuilderType.containsKey(theOperationName)) {
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderType.put(theOperationName, b);
		}

		@Override
		public final void typeContains(EggType<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if (keySet.size() == 0) {
				throw new OperationMeansNotCalled(b.getName());
			}
			for (String s : b.keySet()) {
				this.typeContains(s, b);
			}
		}

		@Override
		public EggType<? extends S> typeGetChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggType<? extends S> s = this.childBuilderType.get(name);
			if (s == null) {
				EggFun<? extends S> sF = this.childBuilderFun.get(name);
				if (sF == null) {
					EggValue<? extends S> sV = this.childBuilderValue.get(name);
					if (sV == null) {
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}
			return s;
		}

		@Override
		public final Attribute<?> typeCreateAttributeChild(CodeNode child, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			EggType<? extends S> cb = this.typeGetChildBuilder(child);
			if (cb == null) {
				return null;
			}
			return cb.typeCreateAttributeThis(child, unformedComplexTypes);
		}

		@Override
		public final TypeComplex typeCreateComplexChild(CodeNode child, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			EggType<? extends S> cb = this.typeGetChildBuilder(child);
			if (cb == null) {
				return null;
			}
			return cb.typeCreateComplexThis(child, parent, unformedComplexTypes);
		}

		@Override
		public final Attribute<?> typeCreateAttributeThis(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(c);
			return this.typeCreateAttribute(c, unformedComplexTypes);
		}

		@Override
		public abstract Attribute<?> typeCreateAttribute(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException;

		@Override
		public final TypeComplex typeCreateComplexThis(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException {
			super.checkMeaning(n);
			return this.typeCreateComplex(n, parent, unformedComplexTypes);
		}

		@Override
		public abstract TypeComplex typeCreateComplex(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
				throws AbstractInterpreterException;

	}

	private static void buildBool() throws AbstractInterpreterException {
		Expression.boolExpression.functionContains(Expression.boolExpression);
		Expression.boolExpression.functionContains(Expression.boolComparsion);
		Expression.boolExpression.functionContains(Expression.boolNullCheck);
		Expression.boolExpression.functionContains(Reference.boolRef);
		Expression.boolExpression.functionContains(Constant.boolConst);
		Expression.boolExpression.functionContains(FunCall.boolCall);

		Assign.boolAssigment.functionContains(Expression.boolExpression);
		Assign.boolAssigment.functionContains(Expression.boolComparsion);
		Assign.boolAssigment.functionContains(Expression.boolNullCheck);
		Assign.boolAssigment.functionContains(Reference.boolRef);
		Assign.boolAssigment.functionContains(Constant.boolConst);
		Assign.boolAssigment.functionContains(FunCall.boolCall);
		Assign.whatEverAssigment.functionContains(Expression.boolExpression);
		Assign.whatEverAssigment.functionContains(Expression.boolComparsion);
		Assign.whatEverAssigment.functionContains(Expression.boolNullCheck);
		// Assign.whatEverAssigment.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		FunReturn.boolReturn.functionContains(Expression.boolExpression);
		FunReturn.boolReturn.functionContains(Expression.boolComparsion);
		FunReturn.boolReturn.functionContains(Expression.boolNullCheck);
		FunReturn.boolReturn.functionContains(Reference.boolRef);
		FunReturn.boolReturn.functionContains(Constant.boolConst);
		FunReturn.boolReturn.functionContains(FunCall.boolCall);
		FunReturn.whatEverReturn.functionContains(Expression.boolExpression);
		FunReturn.whatEverReturn.functionContains(Expression.boolComparsion);
		FunReturn.whatEverReturn.functionContains(Expression.boolNullCheck);
		// FunReturn.whatEverReturn.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		Control.loop.functionContains(Expression.boolExpression);
		Control.loop.functionContains(Expression.boolComparsion);
		Control.loop.functionContains(Expression.boolNullCheck);

		Control.when.functionContains(Expression.boolExpression);
		Control.when.functionContains(Expression.boolComparsion);
		Control.when.functionContains(Expression.boolNullCheck);
		// Control.unknownIf.contains(Reference.boolRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.boolConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.boolCall); // done by
		// Reference.whatEverCall

		Fun.bool.functionContains(FunReturn.boolReturn);
		Fun.noReturn.functionContains(Expression.boolExpression);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.noReturn.functionContains(Expression.boolComparsion);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.structure.functionContains(Expression.boolExpression);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.structure.functionContains(Expression.boolComparsion);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.whatEver.functionContains(Expression.boolExpression);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.whatEver.functionContains(Expression.boolComparsion);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
	}

	private static void buildNumber() throws AbstractInterpreterException {
		Expression.boolComparsion.functionContains(Expression.numberExpession);
		Expression.boolComparsion.functionContains(Reference.numberRef);
		Expression.boolComparsion.functionContains(Constant.numberConst);
		Expression.boolComparsion.functionContains(FunCall.numberCall);
		Expression.numberExpession.functionContains(Expression.numberExpession);
		Expression.numberExpession.functionContains(Reference.numberRef);
		Expression.numberExpession.functionContains(Constant.numberConst);
		Expression.numberExpession.functionContains(FunCall.numberCall);

		Control.loop.functionContains(Expression.numberExpession);
		Control.when.functionContains(Expression.numberExpession);
		// Control.unknownIf.contains(Reference.numberRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.numberConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.numberCall);// done by
		// Reference.whatEverCall

		Assign.numberAssigment.functionContains(Expression.numberExpession);
		Assign.numberAssigment.functionContains(Reference.numberRef);
		Assign.numberAssigment.functionContains(Constant.numberConst);
		Assign.numberAssigment.functionContains(FunCall.numberCall);
		Assign.whatEverAssigment.functionContains(Expression.numberExpession);
		// Assign.whatEverAssigment.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		FunReturn.numberReturn.functionContains(Expression.numberExpession);
		FunReturn.numberReturn.functionContains(Reference.numberRef);
		FunReturn.numberReturn.functionContains(Constant.numberConst);
		FunReturn.numberReturn.functionContains(FunCall.numberCall);
		FunReturn.whatEverReturn.functionContains(Expression.numberExpession);
		// FunReturn.whatEverReturn.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		Fun.number.functionContains(FunReturn.numberReturn);

		Fun.structure.functionContains(Reference.numberRef);// TODO eigentlich
															// nur
		// hinter Zuweisung
		Fun.structure.functionContains(Expression.numberExpession);// TODO
																	// eigentlich
		// nur hinter
		// Zuweisung
		Fun.noReturn.functionContains(Expression.numberExpession);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
		Fun.whatEver.functionContains(Expression.numberExpession);// TODO
																	// eigentlich
																	// nur
		// hinter Zuweisung
	}

	private static void buildStruct() throws AbstractInterpreterException {
		Assign.structureAssigment.functionContains(Reference.structureRef);
		Assign.structureAssigment.functionContains(Constant.objConst);
		Assign.structureAssigment.functionContains(FunCall.structureCall);

		FunReturn.structureReturn.functionContains(Reference.structureRef);
		FunReturn.structureReturn.functionContains(Constant.objConst);
		FunReturn.structureReturn.functionContains(FunCall.structureCall);

		Fun.structure.functionContains(FunReturn.structureReturn);
	}

	private static void buildUnknown() throws AbstractInterpreterException {
		Assign.whatEverAssigment.functionContains(Reference.whatEverRef);
		Assign.whatEverAssigment.functionContains(Constant.whatEverConst);
		Assign.whatEverAssigment.functionContains(FunCall.whatEverCall);

		Control.loop.functionContains(Assign.whatEverAssigment);
		Control.loop.functionContains(FunCall.whatEverCall);
		Control.loop.functionContains(FunReturn.whatEverReturn);

		Control.when.functionContains(Assign.whatEverAssigment);
		Control.when.functionContains(FunCall.whatEverCall);
		Control.when.functionContains(FunReturn.whatEverReturn);

		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.functionContains(Reference.whatEverRef);
		FunReturn.whatEverReturn.functionContains(Constant.whatEverConst);
		FunReturn.whatEverReturn.functionContains(FunCall.whatEverCall);

		/*
		 * TODO das muss so registriert werden und dann muss aber auch
		 * Assign.whatEverParam implementiert werden statt
		 * BuildFunctions.createAssigs(CodeNode n, TypeComplex parentTyp)
		 */
		FunCall.structureCall.functionContains(Assign.whatEverAssigment);
		FunCall.numberCall.functionContains(Assign.whatEverAssigment);
		FunCall.boolCall.functionContains(Assign.whatEverAssigment);
		FunCall.voidCall.functionContains(Assign.whatEverAssigment);
		FunCall.whatEverCall.functionContains(Assign.whatEverAssigment);

		Fun.bool.functionContains(Assign.whatEverAssigment);
		Fun.bool.functionContains(FunCall.whatEverCall);
		Fun.bool.functionContains(Control.loop);
		Fun.bool.functionContains(Control.when);
		Fun.number.functionContains(Assign.whatEverAssigment);
		Fun.number.functionContains(FunCall.whatEverCall);
		Fun.number.functionContains(Control.loop);
		Fun.number.functionContains(Control.when);
		Fun.structure.functionContains(Assign.whatEverAssigment);
		Fun.structure.functionContains(FunCall.whatEverCall);
		Fun.structure.functionContains(Control.loop);
		Fun.structure.functionContains(Control.when);
		Fun.whatEver.functionContains(Assign.whatEverAssigment);
		Fun.whatEver.functionContains(FunCall.whatEverCall);
		Fun.whatEver.functionContains(FunReturn.whatEverReturn);
		Fun.whatEver.functionContains(Control.loop);
		Fun.whatEver.functionContains(Control.when);

		// TODO 1 was sollte das?
		/*
		 * Fun.whatEver.contains(Types.attribute);
		 * Fun.whatEver.contains(Values.value);
		 * Fun.whatEver.contains(Values.valueVirtComp);
		 * Fun.whatEver.contains(Types.complex);
		 * Fun.whatEver.contains(Fun.whatEver);
		 */

		Fun.noReturn.functionContains(Assign.whatEverAssigment);
		Fun.noReturn.functionContains(FunCall.whatEverCall);
		Fun.noReturn.functionContains(Control.loop);
		Fun.noReturn.functionContains(Control.when);

	}

	public static void build() throws AbstractInterpreterException {

		Expression.numberExpession
				.means(new String[] { Symbols.comPlus(), Symbols.comMinus(), Symbols.comTimes(), Symbols.comDividedBy(), Symbols.comRemains() });
		Expression.boolNullCheck.means(Symbols.comIsNull());
		Expression.boolComparsion.means(new String[] { Symbols.comSmaller(), Symbols.comGreater(), Symbols.comEqual() });
		Expression.boolExpression.means(new String[] { Symbols.comAnd(), Symbols.comOr(), Symbols.comXOr(), Symbols.comNot() });

		String[] params4Reference = new String[] { Symbols.paramValue() };
		Reference.structureRef.means(Symbols.comReference());
		Reference.structureRef.registerParameters(params4Reference);
		Reference.numberRef.means(Symbols.comReference());
		Reference.numberRef.registerParameters(params4Reference);
		Reference.boolRef.means(Symbols.comReference());
		Reference.boolRef.registerParameters(params4Reference);
		Reference.whatEverRef.means(Symbols.comReference());
		Reference.whatEverRef.registerParameters(params4Reference);

		String[] params4Const = new String[] { Symbols.paramValue() };// Symbols.paramName(),
		Constant.numberConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng() });
		Constant.numberConst.registerParameters(params4Const);
		Constant.boolConst.means(Symbols.comBool());
		Constant.boolConst.registerParameters(params4Const);
		Constant.objConst.means(Symbols.comStructure());
		Constant.objConst.registerParameters(params4Const);
		Constant.whatEverConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng(), Symbols.comBool(), Symbols.comStructure() });
		Constant.whatEverConst.registerParameters(params4Const);

		Assign.whatEverAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer(), Symbols.comParam() });
		Assign.structureAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
		Assign.numberAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
		Assign.boolAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });

		Control.when.means(Symbols.comWhen());
		Control.loop.means(Symbols.comLoop());

		FunReturn.whatEverReturn.means(Symbols.comReturn());
		FunReturn.structureReturn.means(Symbols.comReturn());
		FunReturn.numberReturn.means(Symbols.comReturn());
		FunReturn.boolReturn.means(Symbols.comReturn());

		String[] params4Function = { Symbols.paramName(), Symbols.paramType() };
		Fun.noReturn.means(Symbols.comFunction());
		Fun.noReturn.registerParameters(params4Function);
		Fun.structure.means(Symbols.comFunction());
		Fun.structure.registerParameters(params4Function);
		Fun.number.means(Symbols.comFunction());
		Fun.number.registerParameters(params4Function);
		Fun.bool.means(Symbols.comFunction());
		Fun.bool.registerParameters(params4Function);
		Fun.whatEver.means(Symbols.comFunction());
		Fun.whatEver.registerParameters(params4Function);

		String[] params4Call = { Symbols.paramName(), Symbols.paramType() };
		FunCall.structureCall.means(Symbols.comCall());
		FunCall.structureCall.registerParameters(params4Call);
		FunCall.numberCall.means(Symbols.comCall());
		FunCall.numberCall.registerParameters(params4Call);
		FunCall.boolCall.means(Symbols.comCall());
		FunCall.boolCall.registerParameters(params4Call);
		FunCall.voidCall.means(Symbols.comCall());
		FunCall.voidCall.registerParameters(params4Call);
		FunCall.whatEverCall.means(Symbols.comCall());
		FunCall.whatEverCall.registerParameters(params4Call);

		Types.complex.means(Symbols.comComplex());
		Types.complex.registerParameters(new String[] { Symbols.paramName() });

		Types.complexTyped.means(Symbols.comComplex());
		Types.complexTyped.registerParameters(new String[] { Symbols.paramName(), Symbols.PARAM_SUPERTYPE });

		/*
		 * Types.functions_in_complex.means(Symbols.comComplex());
		 * Types.functions_in_complex.registerParameters(new String[] {
		 * Symbols.paramName() });
		 * 
		 * Types.values_in_complex.means(Symbols.comComplex());
		 * Types.values_in_complex.registerParameters(new String[] {
		 * Symbols.paramName() });
		 */

		// Attribute
		Types.attribute.means(Symbols.comAttribute()); // isAttribute
														// //Symbol.ATTRIBUTE ,
		Types.attribute.registerParameters(new String[] { Symbols.paramName(), Symbols.paramType() });

		// TODO this box-object is only a dummy ...
		/*
		 * Values.value.means(Symbols.comValue()); // isAttributeInitialized
		 * //Symbol.VALUE
		 * //Values.value.registerParameters(PrimitiveValue.params); TODO sync
		 * this Values.value.registerParameters(new String[] {
		 * Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() });
		 */
		Values.type_with_value.means(Symbols.comValue());
		Values.type_with_value.registerParameters(new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() });

		// TODO this box-object is only a dummy ...
		Values.valueVirtPrim.means(Symbols.comvirtualPrimValue()); // isAttributeInitialized
																	// //Symbol.VALUE
		Values.valueVirtPrim.registerParameters(new String[] { Symbols.paramName(), Symbols.paramValue() });

		// TODO this box-object is only a dummy ...
		Values.valueVirtComp.means(Symbols.comVirtualCompValue()); // isAttributeInitialized
																	// //Symbol.VALUE
		Values.valueVirtComp.registerParameters(new String[] { Symbols.paramName() });

		// TODO this box-object is only a dummy ...
		Values.contract.means(Symbols.comContract()); // isAttributeInitialized
														// //Symbol.VALUE
		Values.contract.registerParameters(new String[] { Symbols.paramName() });

		Types.contract.means(Symbols.comContract()); // isAttributeInitialized
														// //Symbol.VALUE
		Types.contract.registerParameters(new String[] {});

		// TODO this box-object is only a dummy ...
		Control.then.means(Symbols.comThen());
		// TODO this box-object is only a dummy ...
		Control.otherwise.means(Symbols.comOtherwise());

		CodeModel.buildBool();
		CodeModel.buildNumber();
		CodeModel.buildStruct();
		CodeModel.buildUnknown();

		Types.complex.typeContains((BoxType<Structure, Object>) Types.complex);
		Types.complex.functionContains((BoxFun<Structure, Object>) Types.complex);
		Types.complex.typeContains((BoxType<Object, Object>) Fun.whatEver);
		Types.complex.functionContains((BoxFun<Object, Object>) Fun.whatEver);

		Types.complex.typeContains(Types.attribute);
		Types.complex.typeContains(Values.type_with_value);
		Types.complex.valueContains(Values.type_with_value);
		// Types.complex.contains(Values.value);
		Types.complex.valueContains(Values.valueVirtComp);
		Types.complex.valueContains(Values.valueVirtPrim);

		Fun.whatEver.typeContains(Types.attribute);
		Fun.whatEver.typeContains(Values.type_with_value);
		Fun.whatEver.valueContains(Values.type_with_value);// TODO fragwuerdig:
															// das ist dann ja
															// eine constante...
		// Fun.whatEver.contains(Values.value);
		Fun.whatEver.valueContains(Values.valueVirtComp);
		Fun.whatEver.valueContains(Values.valueVirtPrim);
		// TODO das gilt doch auch für alle anderen Functions ...

		Types.complex.functionContains(FunCall.whatEverCall);
		Types.complex.functionContains(Assign.whatEverAssigment);
		Types.complex.functionContains(Control.loop);
		Types.complex.functionContains(Control.when);
		Types.complex.functionContains(FunReturn.whatEverReturn);
		Types.complex.functionContains(Expression.numberExpession);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung
		Types.complex.functionContains(Expression.boolComparsion);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung
		Types.complex.functionContains(Expression.boolExpression);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung

		Types.contract.functionContains(FunCall.whatEverCall);
		Types.contract.functionContains(Assign.whatEverAssigment);
		Types.contract.functionContains(Control.loop);
		Types.contract.functionContains(Control.when);
		Types.contract.functionContains(FunReturn.whatEverReturn);
		Types.contract.functionContains(Expression.numberExpession);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung
		Types.contract.functionContains(Expression.boolComparsion);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung
		Types.contract.functionContains(Expression.boolExpression);// TODO
																	// eigentlich
																	// nur
																	// hinter
																	// zuweisung

		Types.contract.typeContains(Types.attribute);
		// Types.contract.contains(Values.value);
		Types.contract.typeContains(Values.type_with_value);
		Types.contract.valueContains(Values.valueVirtComp);
		Types.contract.typeContains((BoxType<Structure, Object>) Types.complex);
		// Types.contract.contains((BoxFun<Structure, Object>) Types.complex);
		Types.contract.functionContains((BoxFun<Object, Object>) Fun.whatEver);

	}

}
