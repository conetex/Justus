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
import com.conetex.contract.build.BuildTypes.Types;
import com.conetex.contract.build.BuildValues.Values;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateOperation;
import com.conetex.contract.build.exceptionFunction.OperationMeansNotCalled;
import com.conetex.contract.build.exceptionFunction.UnexpectedSubOperation;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.PrimitiveValue;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;

public class CodeModel{

	public static abstract class _Box<T, S> extends _Egg<T>{

		private final Map<String, _Egg<? extends S>> childBuilder = new HashMap<>();

		public _Box(String theName) {
			super(theName);
		}

		public _Box(String theName, int i) {

			super(theName);
		}

		final void contains(String theOperationName, _Egg<? extends S> b) {
			if(this.childBuilder.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		final void contains(_Egg<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		private _Egg<? extends S> getChildBuilder(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			_Egg<? extends S> s = this.childBuilder.get(name);
			if(s == null){
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp) throws AbstractInterpreterException {
			_Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.functionCreateThis(child, parentTyp);
		}

		public final Attribute<?> attributeCreateChild(CodeNode child, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			_Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.attributeCreateThis(child, unformedComplexTypes);
		}

		public final void valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			_Egg<? extends S> cb = this.getChildBuilder(child);
			cb.valueCreateThis(child, parentTyp, parentData);
		}

		public final TypeComplex complexCreateChild(CodeNode child, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			_Egg<? extends S> cb = this.getChildBuilder(child);
			return cb.complexCreateThis(child, parent, unformedComplexTypes);
		}

	}

	public static abstract class _Egg<T> {

		private static final Map<String, List<_Egg<?>>> instances = new HashMap<>();

		public static List<_Egg<?>> getInstance(String command) {
			return _Egg.instances.get(command);
		}

		private final String name;

		private String[] parameterNames;

		private final Set<String> meaning = new HashSet<>();

		_Egg(String theName) {
			this.name = theName;
		}

		final String getName() {
			return this.name;
		}

		private void checkMeaning(CodeNode c) throws AbstractInterpreterException {
			if(!this.meaning.contains(c.getCommand())){
				System.err.println("Operation " + c.getCommand() + " not found!");
				throw new UnknownCommand("Operation " + c.getCommand() + " not found!");
			}
		}

		final Attribute<?> attributeCreateThis(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			this.checkMeaning(c);
			return this.attributeCreate(c, unformedComplexTypes);
		}

		public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.functionCreate(n, parentTyp);
		}

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			return null;
		}

		final Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.valueCreate(n, parentTyp, parentData);
		}

		public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			return null;
		}

		final TypeComplex complexCreateThis(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.complexCreate(n, parent, unformedComplexTypes);
		}

		public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		final Set<String> keySet() {
			return this.meaning;
		}

		final void means(String theOperationName) throws DublicateOperation {
			if(this.meaning.contains(theOperationName)){
				throw new DublicateOperation("duplicate operation '" + theOperationName + "' in " + this.getName());
			}
			this.meaning.add(theOperationName);
			List<_Egg<?>> instanceList = _Egg.getInstance(theOperationName);
			if(instanceList == null){
				instanceList = new LinkedList<>();
				_Egg.instances.put(theOperationName, instanceList);
			}
			instanceList.add(this);
		}

		final void means(String[] theOperationNames) throws DublicateOperation {
			for(String theOperationName : theOperationNames){
				this.means(theOperationName);
			}
		}

		final void registerParameters(String[] theParameterNames) {
			if(this.parameterNames != null){
				System.err.println("duplicate Param call");
			}
			this.parameterNames = theParameterNames;
		}

		public final int getParameterIndex(String p) throws UnknownCommandParameter {
			for(int j = 0; j < this.parameterNames.length; j++){
				if(this.parameterNames[j] == p){
					return j;
				}
			}
			throw new UnknownCommandParameter(p);
		}

		public final String[] getParameterNames() {
			return this.parameterNames;
		}

		public final int getParameterCount() {
			if(this.parameterNames == null){
				return 0;
			}
			return this.parameterNames.length;
		}

	}

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
	
	public static abstract class EggAbstrImp<T> implements EggAbstr<T>{

		private static final Map<String, List<EggAbstr<?>>> instances = new HashMap<>();

		public static List<EggAbstr<?>> getInstance(String command) {
			return EggAbstrImp.instances.get(command);
		}

		private final String name;

		private String[] parameterNames;

		private final Set<String> meaning = new HashSet<>();

		EggAbstrImp(String theName) {
			this.name = theName;
		}

		public final String getName() {
			return this.name;
		}

		public final void checkMeaning(CodeNode c) throws AbstractInterpreterException {
			if(!this.meaning.contains(c.getCommand())){
				System.err.println("Operation " + c.getCommand() + " not found!");
				throw new UnknownCommand("Operation " + c.getCommand() + " not found!");
			}
		}

		public final Set<String> keySet() {
			return this.meaning;
		}

		public final void means(String theOperationName) throws DublicateOperation {
			if(this.meaning.contains(theOperationName)){
				throw new DublicateOperation("duplicate operation '" + theOperationName + "' in " + this.getName());
			}
			this.meaning.add(theOperationName);
			List<EggAbstr<?>> instanceList = EggAbstrImp.getInstance(theOperationName);
			if(instanceList == null){
				instanceList = new LinkedList<>();
				EggAbstrImp.instances.put(theOperationName, instanceList);
			}
			instanceList.add(this);
		}

		public final void means(String[] theOperationNames) throws DublicateOperation {
			for(String theOperationName : theOperationNames){
				this.means(theOperationName);
			}
		}

		public final void registerParameters(String[] theParameterNames) {
			if(this.parameterNames != null){
				System.err.println("duplicate Param call");
			}
			this.parameterNames = theParameterNames;
		}

		public final int getParameterIndex(String p) throws UnknownCommandParameter {
			for(int j = 0; j < this.parameterNames.length; j++){
				if(this.parameterNames[j] == p){
					return j;
				}
			}
			throw new UnknownCommandParameter(p);
		}

		public final String[] getParameterNames() {
			return this.parameterNames;
		}

		public final int getParameterCount() {
			if(this.parameterNames == null){
				return 0;
			}
			return this.parameterNames.length;
		}

	}	
	
	public static interface EggType<T> extends EggAbstr<T>{
		
		Attribute<?> attributeCreateThis(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

		public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

		TypeComplex complexCreateThis(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

		public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;
		
	}
	
	public static abstract class EggTypeImp<T> extends EggAbstrImp<T> implements EggType<T> {

		EggTypeImp(String theName) {
			super(theName);
		}

		public final Attribute<?> attributeCreateThis(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(c);
			return this.attributeCreate(c, unformedComplexTypes);
		}

		public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

		public final TypeComplex complexCreateThis(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(n);
			return this.complexCreate(n, parent, unformedComplexTypes);
		}

		public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			return null;
		}

	}	
	
	public static interface BoxType<T, S> extends EggType<T>{

		void contains(String theOperationName, EggType<? extends S> b);

		void contains(EggType<? extends S> b) throws OperationMeansNotCalled;

		EggType<? extends S> getChildBuilderType(CodeNode n) throws AbstractInterpreterException;

		public Attribute<?> attributeCreateChild(CodeNode child, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

		public TypeComplex complexCreateChild(CodeNode child, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

	}
	
	public static abstract class BoxTypeImp<T, S> extends EggTypeImp<T> implements BoxType<T, S>{

		private final Map<String, EggType<? extends S>> childBuilder = new HashMap<>();

		public BoxTypeImp(String theName) {
			super(theName);
		}

		public final void contains(String theOperationName, EggType<? extends S> b) {
			if(this.childBuilder.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void contains(EggType<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		public EggType<? extends S> getChildBuilderType(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggType<? extends S> s = this.childBuilder.get(name);
			if(s == null){
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Attribute<?> attributeCreateChild(CodeNode child, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			EggType<? extends S> cb = this.getChildBuilderType(child);
			return cb.attributeCreateThis(child, unformedComplexTypes);
		}

		public final TypeComplex complexCreateChild(CodeNode child, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			EggType<? extends S> cb = this.getChildBuilderType(child);
			return cb.complexCreateThis(child, parent, unformedComplexTypes);
		}

	}

	public static interface EggFun<T> extends EggAbstr<T> {

		public Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException;

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException;

	}
	
	public static abstract class EggFunImp<T> extends EggAbstrImp<T> implements EggFun<T> {

		EggFunImp(String theName) {
			super(theName);
		}

		public final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.functionCreate(n, parentTyp);
		}

		public Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			return null;
		}

	}	
	
	public static interface BoxFun<T, S> extends EggFun<T>{

		void contains(String theOperationName, EggFun<? extends S> b);

		void contains(EggFun<? extends S> b) throws OperationMeansNotCalled;

		EggFun<? extends S> getChildBuilderFun(CodeNode n) throws AbstractInterpreterException;

		public Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp) throws AbstractInterpreterException;

	}
	
	public static abstract class BoxFunImp<T, S> extends EggFunImp<T> implements BoxFun<T, S>{

		private final Map<String, EggFun<? extends S>> childBuilder = new HashMap<>();

		public BoxFunImp(String theName) {
			super(theName);
		}

		public final void contains(String theOperationName, EggFun<? extends S> b) {
			if(this.childBuilder.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		@Override
		public final void contains(EggFun<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		public EggFun<? extends S> getChildBuilderFun(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggFun<? extends S> s = this.childBuilder.get(name);
			if(s == null){
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp) throws AbstractInterpreterException {
			EggFun<? extends S> cb = this.getChildBuilderFun(child);
			return cb.functionCreateThis(child, parentTyp);
		}

	}
	
	public static interface EggValue<T> extends EggAbstr<T> {
		
		Value<?> valueCreateThis(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

		public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

	}
	
	public static abstract class EggValueImp<T> extends EggAbstrImp<T> implements EggValue<T>{

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
	
	public static interface BoxValue<T, S> extends EggValue<T>{

		void contains(String theOperationName, EggValue<? extends S> b);

		void contains(EggValue<? extends S> b) throws OperationMeansNotCalled;

		EggValue<? extends S> getChildBuilderValue(CodeNode n) throws AbstractInterpreterException;

		public void valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException;

	}
	
	public static abstract class BoxValueImp<T, S> extends EggValueImp<T> implements BoxValue<T, S>{

		private final Map<String, EggValue<? extends S>> childBuilder = new HashMap<>();

		public BoxValueImp(String theName) {
			super(theName);
		}

		public final void contains(String theOperationName, EggValue<? extends S> b) {
			if(this.childBuilder.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilder.put(theOperationName, b);
		}

		public final void contains(EggValue<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		public EggValue<? extends S> getChildBuilderValue(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggValue<? extends S> s = this.childBuilder.get(name);
			if(s == null){
				throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
			}
			return s;
		}

		public final void valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			EggValue<? extends S> cb = this.getChildBuilderValue(child);
			cb.valueCreateThis(child, parentTyp, parentData);
		}

	}
	
	public abstract static class BoxValueTypeFunImp<T, S> extends EggAbstrImp<T> implements BoxValue<T, S>, BoxFun<T, S>, BoxType<T, S>{
		
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
		public final void contains(String theOperationName, EggValue<? extends S> b) {
			if(this.childBuilderValue.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderValue.put(theOperationName, b);
		}

		@Override
		public final void contains(EggValue<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		@Override
		public EggValue<? extends S> getChildBuilderValue(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggValue<? extends S> s = this.childBuilderValue.get(name);
			if(s == null){
				EggFun<? extends S> sF = this.childBuilderFun.get(name);
				if(sF == null){
					EggType<? extends S> sT = this.childBuilderType.get(name);
					if(sT == null){
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}
			return s;
		}

		@Override
		public final void valueCreateChild(CodeNode child, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
			EggValue<? extends S> cb = this.getChildBuilderValue(child);
			if(cb != null){
				cb.valueCreateThis(child, parentTyp, parentData);
			}
		}

		
		private final Map<String, EggFun<? extends S>> childBuilderFun = new HashMap<>();
		
		@Override
		public final Accessible<? extends T> functionCreateThis(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException {
			this.checkMeaning(n);
			return this.functionCreate(n, parentTyp);
		}		

		@Override
		public abstract Accessible<? extends T> functionCreate(CodeNode n, TypeComplex parentTyp) throws AbstractInterpreterException;

		
		@Override
		public final void contains(String theOperationName, EggFun<? extends S> b) {
			if(this.childBuilderFun.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderFun.put(theOperationName, b);
		}

		@Override
		public final void contains(EggFun<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		@Override
		public EggFun<? extends S> getChildBuilderFun(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggFun<? extends S> s = this.childBuilderFun.get(name);
			if(s == null){
				EggValue<? extends S> sV = this.childBuilderValue.get(name);
				if(sV == null){
					EggType<? extends S> sT = this.childBuilderType.get(name);
					if(sT == null){
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}			
			return s;
		}

		@Override
		public final Accessible<? extends S> functionCreateChild(CodeNode child, TypeComplex parentTyp) throws AbstractInterpreterException {
			EggFun<? extends S> cb = this.getChildBuilderFun(child);
			if(cb == null){
				return null;
			}
			return cb.functionCreateThis(child, parentTyp);
		}

		
		
		private final Map<String, EggType<? extends S>> childBuilderType = new HashMap<>();
		
		@Override
		public final void contains(String theOperationName, EggType<? extends S> b) {
			if(this.childBuilderType.containsKey(theOperationName)){
				System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
			}
			this.childBuilderType.put(theOperationName, b);
		}

		@Override
		public final void contains(EggType<? extends S> b) throws OperationMeansNotCalled {
			Set<String> keySet = b.keySet();
			if(keySet.size() == 0){
				throw new OperationMeansNotCalled(b.getName());
			}
			for(String s : b.keySet()){
				this.contains(s, b);
			}
		}

		@Override
		public EggType<? extends S> getChildBuilderType(CodeNode n) throws AbstractInterpreterException {
			String name = n.getCommand();
			EggType<? extends S> s = this.childBuilderType.get(name);
			if(s == null){
				EggFun<? extends S> sF = this.childBuilderFun.get(name);
				if(sF == null){
					EggValue<? extends S> sV = this.childBuilderValue.get(name);
					if(sV == null){
						throw new UnexpectedSubOperation("inner Operation '" + name + "' not found in " + this.getName());
					}
				}
			}
			return s;
		}

		@Override
		public final Attribute<?> attributeCreateChild(CodeNode child, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			EggType<? extends S> cb = this.getChildBuilderType(child);
			if(cb == null){
				return null;
			}
			return cb.attributeCreateThis(child, unformedComplexTypes);
		}

		@Override
		public final TypeComplex complexCreateChild(CodeNode child, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			EggType<? extends S> cb = this.getChildBuilderType(child);
			if(cb == null){
				return null;
			}
			return cb.complexCreateThis(child, parent, unformedComplexTypes);
		}

		@Override
		public final Attribute<?> attributeCreateThis(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(c);
			return this.attributeCreate(c, unformedComplexTypes);
		}

		@Override
		public abstract Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;

		@Override
		public final TypeComplex complexCreateThis(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
			super.checkMeaning(n);
			return this.complexCreate(n, parent, unformedComplexTypes);
		}

		@Override
		public abstract TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException;
		
	}
	
	private static void buildBool() throws AbstractInterpreterException {
		Expression.boolExpression.contains(Expression.boolExpression);
		Expression.boolExpression.contains(Expression.boolComparsion);
		Expression.boolExpression.contains(Expression.boolNullCheck);
		Expression.boolExpression.contains(Reference.boolRef);
		Expression.boolExpression.contains(Constant.boolConst);
		Expression.boolExpression.contains(FunCall.boolCall);

		Assign.boolAssigment.contains(Expression.boolExpression);
		Assign.boolAssigment.contains(Expression.boolComparsion);
		Assign.boolAssigment.contains(Expression.boolNullCheck);
		Assign.boolAssigment.contains(Reference.boolRef);
		Assign.boolAssigment.contains(Constant.boolConst);
		Assign.boolAssigment.contains(FunCall.boolCall);
		Assign.whatEverAssigment.contains(Expression.boolExpression);
		Assign.whatEverAssigment.contains(Expression.boolComparsion);
		Assign.whatEverAssigment.contains(Expression.boolNullCheck);
		// Assign.whatEverAssigment.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		FunReturn.boolReturn.contains(Expression.boolExpression);
		FunReturn.boolReturn.contains(Expression.boolComparsion);
		FunReturn.boolReturn.contains(Expression.boolNullCheck);
		FunReturn.boolReturn.contains(Reference.boolRef);
		FunReturn.boolReturn.contains(Constant.boolConst);
		FunReturn.boolReturn.contains(FunCall.boolCall);
		FunReturn.whatEverReturn.contains(Expression.boolExpression);
		FunReturn.whatEverReturn.contains(Expression.boolComparsion);
		FunReturn.whatEverReturn.contains(Expression.boolNullCheck);
		// FunReturn.whatEverReturn.contains(Reference.boolRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.boolConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.boolCall); // done by
		// FunCall.whatEverCall

		Control.loop.contains(Expression.boolExpression);
		Control.loop.contains(Expression.boolComparsion);
		Control.loop.contains(Expression.boolNullCheck);

		Control.when.contains(Expression.boolExpression);
		Control.when.contains(Expression.boolComparsion);
		Control.when.contains(Expression.boolNullCheck);
		// Control.unknownIf.contains(Reference.boolRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.boolConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.boolCall); // done by
		// Reference.whatEverCall

		Fun.bool.contains(FunReturn.boolReturn);
		Fun.noReturn.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.noReturn.contains(Expression.boolComparsion);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.structure.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.structure.contains(Expression.boolComparsion);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.whatEver.contains(Expression.boolExpression);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.whatEver.contains(Expression.boolComparsion);// TODO eigentlich nur
															// hinter Zuweisung
	}

	private static void buildNumber() throws AbstractInterpreterException {
		Expression.boolComparsion.contains(Expression.numberExpession);
		Expression.boolComparsion.contains(Reference.numberRef);
		Expression.boolComparsion.contains(Constant.numberConst);
		Expression.boolComparsion.contains(FunCall.numberCall);
		Expression.numberExpession.contains(Expression.numberExpession);
		Expression.numberExpession.contains(Reference.numberRef);
		Expression.numberExpession.contains(Constant.numberConst);
		Expression.numberExpession.contains(FunCall.numberCall);

		Control.loop.contains(Expression.numberExpession);
		Control.when.contains(Expression.numberExpession);
		// Control.unknownIf.contains(Reference.numberRef);// done by
		// Reference.whatEverRef
		// Control.unknownIf.contains(Constant.numberConst);// done by
		// Reference.whatEverConst
		// Control.unknownIf.contains(FunCall.numberCall);// done by
		// Reference.whatEverCall

		Assign.numberAssigment.contains(Expression.numberExpession);
		Assign.numberAssigment.contains(Reference.numberRef);
		Assign.numberAssigment.contains(Constant.numberConst);
		Assign.numberAssigment.contains(FunCall.numberCall);
		Assign.whatEverAssigment.contains(Expression.numberExpession);
		// Assign.whatEverAssigment.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// Assign.whatEverAssigment.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// Assign.whatEverAssigment.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		FunReturn.numberReturn.contains(Expression.numberExpession);
		FunReturn.numberReturn.contains(Reference.numberRef);
		FunReturn.numberReturn.contains(Constant.numberConst);
		FunReturn.numberReturn.contains(FunCall.numberCall);
		FunReturn.whatEverReturn.contains(Expression.numberExpession);
		// FunReturn.whatEverReturn.contains(Reference.numberRef); // done by
		// Reference.whatEverRef
		// FunReturn.whatEverReturn.contains(Constant.numberConst); // done by
		// Constant.whatEverConst
		// FunReturn.whatEverReturn.contains(FunCall.numberCall); // done by
		// FunCall.whatEverCall

		Fun.number.contains(FunReturn.numberReturn);

		Fun.structure.contains(Reference.numberRef);// TODO eigentlich nur
													// hinter Zuweisung
		Fun.structure.contains(Expression.numberExpession);// TODO eigentlich
															// nur hinter
															// Zuweisung
		Fun.noReturn.contains(Expression.numberExpession);// TODO eigentlich nur
															// hinter Zuweisung
		Fun.whatEver.contains(Expression.numberExpession);// TODO eigentlich nur
															// hinter Zuweisung
	}

	private static void buildStruct() throws AbstractInterpreterException {
		Assign.structureAssigment.contains(Reference.structureRef);
		Assign.structureAssigment.contains(Constant.objConst);
		Assign.structureAssigment.contains(FunCall.structureCall);

		FunReturn.structureReturn.contains(Reference.structureRef);
		FunReturn.structureReturn.contains(Constant.objConst);
		FunReturn.structureReturn.contains(FunCall.structureCall);

		Fun.structure.contains(FunReturn.structureReturn);
	}

	private static void buildUnknown() throws AbstractInterpreterException {
		Assign.whatEverAssigment.contains(Reference.whatEverRef);
		Assign.whatEverAssigment.contains(Constant.whatEverConst);
		Assign.whatEverAssigment.contains(FunCall.whatEverCall);

		Control.loop.contains(Assign.whatEverAssigment);
		Control.loop.contains(FunCall.whatEverCall);
		Control.loop.contains(FunReturn.whatEverReturn);

		Control.when.contains(Assign.whatEverAssigment);
		Control.when.contains(FunCall.whatEverCall);
		Control.when.contains(FunReturn.whatEverReturn);

		// whatEverReturn.contains(whatEverCall);
		FunReturn.whatEverReturn.contains(Reference.whatEverRef);
		FunReturn.whatEverReturn.contains(Constant.whatEverConst);
		FunReturn.whatEverReturn.contains(FunCall.whatEverCall);

		Fun.bool.contains(Assign.whatEverAssigment);
		Fun.bool.contains(FunCall.whatEverCall);
		Fun.bool.contains(Control.loop);
		Fun.bool.contains(Control.when);
		Fun.number.contains(Assign.whatEverAssigment);
		Fun.number.contains(FunCall.whatEverCall);
		Fun.number.contains(Control.loop);
		Fun.number.contains(Control.when);
		Fun.structure.contains(Assign.whatEverAssigment);
		Fun.structure.contains(FunCall.whatEverCall);
		Fun.structure.contains(Control.loop);
		Fun.structure.contains(Control.when);
		Fun.whatEver.contains(Assign.whatEverAssigment);
		Fun.whatEver.contains(FunCall.whatEverCall);
		Fun.whatEver.contains(FunReturn.whatEverReturn);
		Fun.whatEver.contains(Control.loop);
		Fun.whatEver.contains(Control.when);

		// TODO 1 was sollte das?
		/*
		Fun.whatEver.contains(Types.attribute);
		Fun.whatEver.contains(Values.value);
		Fun.whatEver.contains(Values.valueVirtComp);
		Fun.whatEver.contains(Types.complex);
		Fun.whatEver.contains(Fun.whatEver);
		*/

		Fun.noReturn.contains(Assign.whatEverAssigment);
		Fun.noReturn.contains(FunCall.whatEverCall);
		Fun.noReturn.contains(Control.loop);
		Fun.noReturn.contains(Control.when);

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

		String[] params4Const = new String[] { Symbols.paramName(), Symbols.paramValue() };
		Constant.numberConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng() });
		Constant.numberConst.registerParameters(params4Const);
		Constant.boolConst.means(Symbols.comBool());
		Constant.boolConst.registerParameters(params4Const);
		Constant.objConst.means(Symbols.comStructure());
		Constant.objConst.registerParameters(params4Const);
		Constant.whatEverConst.means(new String[] { Symbols.comBigInt(), Symbols.comInt(), Symbols.comLng(), Symbols.comBool(), Symbols.comStructure() });
		Constant.whatEverConst.registerParameters(params4Const);

		Assign.whatEverAssigment.means(new String[] { Symbols.comCopy(), Symbols.comRefer() });
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

		/*
		Types.functions_in_complex.means(Symbols.comComplex());
		Types.functions_in_complex.registerParameters(new String[] { Symbols.paramName() });
		
		Types.values_in_complex.means(Symbols.comComplex());
		Types.values_in_complex.registerParameters(new String[] { Symbols.paramName() });
		*/
		
		// Attribute
		Types.attribute.means(Symbols.comAttribute()); // isAttribute //Symbol.ATTRIBUTE               , 
		Types.attribute.registerParameters(new String[] { Symbols.paramName(), Symbols.paramType() });

		// TODO this box-object is only a dummy ...
		Values.value.means(Symbols.comValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.value.registerParameters(PrimitiveValue.params);

		// TODO this box-object is only a dummy ...
		Values.valueVirtPrim.means(Symbols.comvirtualPrimValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.valueVirtPrim.registerParameters(new String[] { Symbols.paramName(), Symbols.paramValue() });

		// TODO this box-object is only a dummy ...
		Values.valueVirtComp.means(Symbols.comVirtualCompValue()); // isAttributeInitialized //Symbol.VALUE 
		Values.valueVirtComp.registerParameters(new String[] { Symbols.paramName() });

		// TODO this box-object is only a dummy ...
		Values.contract.means(Symbols.comContract()); // isAttributeInitialized //Symbol.VALUE 
		Values.contract.registerParameters(new String[] { Symbols.paramName() });

		// TODO this box-object is only a dummy ...
		Control.then.means(Symbols.comThen());
		// TODO this box-object is only a dummy ...
		Control.otherwise.means(Symbols.comOtherwise());

		CodeModel.buildBool();
		CodeModel.buildNumber();
		CodeModel.buildStruct();
		CodeModel.buildUnknown();

		Types.complex.contains((BoxType<Structure, Object>) Types.complex);
		Types.complex.contains(Fun.whatEver);

		Types.complex.contains(Types.attribute);
		Types.complex.contains(Values.value);
		Types.complex.contains(Values.valueVirtComp);
		Types.complex.contains(Values.valueVirtPrim);

		Types.complex.contains(FunCall.whatEverCall);
		Types.complex.contains(Assign.whatEverAssigment);
		Types.complex.contains(Control.loop);
		Types.complex.contains(Control.when);
		Types.complex.contains(FunReturn.whatEverReturn);
		Types.complex.contains(Expression.numberExpession);// TODO eigentlich nur hinter zuweisung
		Types.complex.contains(Expression.boolComparsion);// TODO eigentlich nur hinter zuweisung
		Types.complex.contains(Expression.boolExpression);// TODO eigentlich nur hinter zuweisung


	}

}
