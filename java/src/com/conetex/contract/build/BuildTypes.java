package com.conetex.contract.build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.conetex.contract.build.CodeModel.BoxFunImp;
import com.conetex.contract.build.CodeModel.BoxType;
import com.conetex.contract.build.CodeModel.BoxTypeImp;
import com.conetex.contract.build.CodeModel.BoxValueTypeFunImp;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.AttributeComplex;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.type.TypeComplexTemp;
import com.conetex.contract.lang.type.TypeComplexTyped;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.util.Pair;

public class BuildTypes{

	static class Types{

		public static abstract class ComplexImp extends BoxValueTypeFunImp<Structure, Object>{

			ComplexImp(String name) {
				super(name);
			}

			@Override
			public abstract Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

			public abstract Function<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException;

		}
		
		public static abstract class CompBox extends BoxFunImp<Structure, Object>{

			CompBox(String name) {
				super(name);
			}

			@Override
			public abstract Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

			public abstract Accessible<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

		}
		
		public static class ComplexType extends ComplexImp{

			ComplexType(String name) {
				super(name);
			}

			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);
				return this.functionCreateImpl(thisNode, thisType);
			}			

			@Override
			public Function<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				List<CodeNode> children = thisNode.getChildNodes();
				for(CodeNode c : children){
					this.functionCreateChild(c, thisType);
				}
				return null;
			}

			@Override
			public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}
						

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes);
			}
			
		}
		
		static final ComplexImp complex = new ComplexType("complex");
		
		static final ComplexImp complexTyped = new ComplexType("complexTyped");

		static class ContractClass extends BoxValueTypeFunImp<Object, Object>{
			
			ContractClass(String theName) {
				super(theName);
			}

			@Override
			public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {

				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				return this.functionCreateImpl(thisNode, thisType);
			}
			
			public Function<?> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
				Accessible<?>[] theSteps = BuildFunctions.getFunctionSteps(thisNode, thisType, this);
				if(theSteps == null){
					System.err.println("no steps ");
				}
				Class<?> returntype = Function.getReturnTyp(theSteps);
				if(returntype == String.class){
					return null;
				}
				else if(returntype == Boolean.class){
					return Function.createBool(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				else if(Number.class.isAssignableFrom(returntype)){
					return Function.createNum(theSteps, thisNode.getParameter(Symbols.paramName()), returntype);
				}
				else if(returntype == Structure.class){
					return Function.createStructure(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				else if(returntype == Object.class){
					return Function.createVoid(theSteps, thisNode.getParameter(Symbols.paramName()));
				}
				System.err.println("unknown return type " + returntype);
				return null;
			}
			
			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes);
			}
		}
		
		static final ContractClass contract = new ContractClass("contract");
		
		static final BoxType<Object, Object> attribute = new BoxTypeImp<Object, Object>("attribute"){

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException {
				String idTypeName = null;
				String idName = null;
				Attribute<?> id = null;
				idTypeName = c.getParameter(Symbols.paramType());
				idName = c.getParameter(Symbols.paramName());
				if(idTypeName == null){
					System.err.println("can not get Type of " + c.getCommand() + " " + idName);
				}
				else{
					if(idTypeName.startsWith(Symbols.litSimpleTypeNS())){
						// Simple
						idName = Symbols.getSimpleName(idName);
						id = TypePrimitive.createAttribute(idName, idTypeName.substring(Symbols.litSimpleTypeNS().length()));
					}
					else{
						// Complex
						// referringComplexTypeNames.add(idTypeName);
						idName = Symbols.getSimpleName(idName);
						id = TypeComplex.createAttribute(idName, idTypeName, unformedComplexTypes);
					}
				}
				if(id == null){
					// TODO Exception
					System.err.println("createComplexType can not create Identifier " + idName + " (" + idTypeName + ")");
				}
				return id;
			}

		};

	}
	
	private static class Recursive<I> {

		Recursive() {
		}

		I function;

	}

	private interface Run{
		void run(CodeNode node, TypeComplex parent) throws AbstractInterpreterException;
	}

	public static CodeNode _getComplexRoot(CodeNode n) throws AbstractInterpreterException {
		String contractName = n.getParameter(Symbols.paramName());
		for(CodeNode c : n.getChildNodes()){
			if( c.getCommand() == Symbols.comComplex() && contractName.equals(c.getParameter(Symbols.paramName())) ){
				return c;
			}
		}
		return n;
	}
	public static CodeNode _getValueRoot(CodeNode n) throws AbstractInterpreterException {
		String contractName = n.getParameter(Symbols.paramName());
		for(CodeNode c : n.getChildNodes()){
			if( c.getCommand() == Symbols.comVirtualCompValue() && contractName.equals(c.getParameter(Symbols.paramName())) ){
				return c;
			}
		}
		return n;
	}
	
	public static List< Pair<CodeNode,TypeComplex> > createComplexTypes(CodeNode n) throws AbstractInterpreterException {
		TypeComplex.clearInstances();
		Map<String, TypeComplexTemp> unformedComplexTypes = new HashMap<>();
		//Set<String> referringComplexTypeNames = new TreeSet<>();
		List<Pair<CodeNode,TypeComplex>> re = new LinkedList<>();

		Recursive<Run> recursive = new Recursive<>();
		recursive.function = (CodeNode node, TypeComplex parent) -> {
			for(CodeNode c : node.getChildNodes()){
				TypeComplex complexType = Types.complex.complexCreateChild(c, parent, unformedComplexTypes);
				if(complexType != null){
					re.add(new Pair<>(c, complexType));
					recursive.function.run(c, complexType);
				}
			}
		};
		TypeComplex complexTypeRoot = createComplexType(n, null, unformedComplexTypes);
		if(complexTypeRoot != null){
			re.add(new Pair<>(n, complexTypeRoot));
			recursive.function.run(n, complexTypeRoot);
		}

		for(String createdComplex : TypeComplex.getInstanceNames()){
			System.out.println("createComplexList known: " + createdComplex);
		}

		if(unformedComplexTypes.size() > 0){
			TypeComplex.clearInstances();
			for(String unformedComplex : unformedComplexTypes.keySet()){
				System.err.println("createComplexList unknown Complex: " + unformedComplex);
			}
			// TODO throw Exception: wir konnten nicht alles kompilieren!!!
			return null;
		}

		return re;
	}

	private static TypeComplex getSuperType(CodeNode n) throws UnknownCommandParameter, UnknownCommand{
		if(n.hasParameter(Symbols.PARAM_SUPERTYPE)){
			String superTypeName = n.getParameter(Symbols.PARAM_SUPERTYPE);
			TypeComplex superType = TypeComplex.getInstance(superTypeName);
			if(superType == null){
				System.err.println("superType " + superTypeName + " not found!");
			}
			return superType;
		}
		return null;	
	}
	
	private static class TypeHierarchy{
		CodeNode node;
		Map<String, TypeHierarchy> namedChildren = new TreeMap<>();
	}
	
	private static class TypeHierarchyResult{
		Map< String, TypeHierarchy > namedTypes = new TreeMap<>();
		List< Pair<String, TypeHierarchy> > rootTypes = new LinkedList<>();
	}
	
	public static TypeComplex createComplexTypeNew(CodeNode root) throws AbstractInterpreterException{
		TypeHierarchyResult r = new TypeHierarchyResult();
		createTypeHierarchy(root, null, r);
		
		TypeComplex re = null;
		
		Map<String, TypeComplexTemp> unformedComplexTypes = new TreeMap<>();
		for(Pair<String, TypeHierarchy> p : r.rootTypes){
			re = createComplexTypeNew(root, p.a, p.b, null, unformedComplexTypes);
		}
		
		for(Entry<String, TypeComplexTemp> e : unformedComplexTypes.entrySet()){
			TypeComplex c = TypeComplex.getInstance( e.getKey() );
			if(c == null){
				System.err.println("Typ " + e.getKey() + " wurde nicht erzeugt...");
				// TODO throw Error...
				continue;
			}
			for(AttributeComplex a : e.getValue().listOfUnformedAttributes){
				a.type = c;
			}
		}
		
		return re;
	}
	
	public static void createTypeHierarchy(CodeNode n, String parentName, TypeHierarchyResult r) throws UnknownCommandParameter, UnknownCommand{
		
		if(n == null){
			// TODO Exception
			System.err.println("no node");
			return;
		}		
		
		String typeName = CodeNode.getTypSubstring( n.getParameter(Symbols.paramName()), parentName );	
		
		TypeHierarchy todo = null;
		if(r.namedTypes.containsKey( typeName )){
			todo = r.namedTypes.get( typeName );
		}
		else{
			todo = new TypeHierarchy();
		}
		todo.node = n;
		
		if(n.hasParameter(Symbols.PARAM_SUPERTYPE)){
			String superTypeName = n.getParameter(Symbols.PARAM_SUPERTYPE);
			if(r.namedTypes.containsKey( superTypeName )){
				TypeHierarchy superType = r.namedTypes.get( superTypeName );
				superType.namedChildren.put(typeName, todo);
			}
			else{
				TypeHierarchy superType = new TypeHierarchy();
				r.namedTypes.put(superTypeName, superType);
				superType.namedChildren.put(typeName, todo);
			}			
		}
		else{
			r.rootTypes.add( new Pair<>(typeName, todo) );
		}
		
		for(CodeNode c : n.getChildNodes()){
			if(Symbols.comComplex().equals( n.getCommand() )){
				createTypeHierarchy(c, typeName, r);
			}
		}		
		
	}
	
	public static TypeComplex createComplexTypeNew(CodeNode root, String typeName, TypeHierarchy r, TypeComplex grandParent, Map<String, TypeComplexTemp> unformedComplexTypes) throws AbstractInterpreterException{
		TypeComplex ret = null;
		TypeComplex parent = createComplexTypeNew(typeName, r.node, grandParent, unformedComplexTypes);
		for(Entry<String, TypeHierarchy> e : r.namedChildren.entrySet()){
			String name = e.getKey();
			TypeHierarchy th = e.getValue();			
			ret = createComplexTypeNew(root, name, th, parent, unformedComplexTypes);
		}
		if(r.node == root){
			ret = parent;
		}
		return ret;
	}

	public static TypeComplex createComplexTypeNew(String typeName, CodeNode n, TypeComplex superType, Map<String, TypeComplexTemp> unformedComplexTypes)
			throws AbstractInterpreterException {
		if(n == null){
			// TODO Exception
			System.err.println("no node");
			return null;
		}		
		 
		System.out.println("createComplexType " + typeName);
		if(typeName.equals("contract4u.DutyOfAgent")){
			System.out.println("debug");
		}

		List<Attribute<?>> identifiers = new LinkedList<>();
		// Map<String, Attribute<?>> functions = new HashMap<>();

		for(CodeNode c : n.getChildNodes()){
			Attribute<?> id = Types.complex.attributeCreateChild(c, unformedComplexTypes);
			if(id != null){
				identifiers.add(id);
			}
		}
		Attribute<?>[] theOrderedIdentifiers = new Attribute<?>[identifiers.size()];
		identifiers.toArray(theOrderedIdentifiers);

		// TODO doppelte definitionen abfangen ...
		TypeComplex complexType = null;//unformedComplexTypes.get(typeName);
		//if(complexType == null){

			if(n.getCommand() == TypeComplexOfFunction.staticGetCommand()){
				complexType = TypeComplexOfFunction.createInit(typeName, theOrderedIdentifiers);
			}
			/*
			else if(n.getCommand() == Symbols.comvirtualPrimValue()){  
				complexType = TypeComplexOfFunction.createInit(typeName, theOrderedIdentifiers);
			}
			*/
			else if(n.getCommand() == TypeComplex.staticGetCommand()){  
				complexType = TypeComplex.createInit(typeName, superType, theOrderedIdentifiers);	
			}			
			/*
			else if(n.getCommand() == Symbols.comVirtualCompValue()){  
				complexType = TypeComplex.createInit(typeName, theOrderedIdentifiers);
			}
			*/
			else if(n.getCommand() == Symbols.comContract()){
				complexType = TypeComplex.createInit(typeName,  superType, theOrderedIdentifiers);
				//complexType = Types.contract.complexCreate(n, null, unformedComplexTypes);
			}	
			
			else{
				// TODO error...
				System.err.println("mist unbekannter tag!");
				complexType = TypeComplex.createInit(typeName, superType, theOrderedIdentifiers);
			}

			// TODO
			// theOrderedIdentifiers
			// m�ssen
			// elemente
			// enthalten,
			// sonst
			// gibts
			// keinen
			// typ

			return complexType;
		//}
			//else{
			//complexType = complexType.init(typeName,  superType, theOrderedIdentifiers);

			//unformedComplexTypes.remove(typeName);
			//return complexType;
		//}
	}
	
	public static TypeComplex createComplexType(CodeNode n, TypeComplex parent, Map<String, TypeComplexTemp> unformedComplexTypes)
			throws AbstractInterpreterException {
		if(n == null){
			// TODO Exception
			System.err.println("no node");
			return null;
		}		
		String typeName = CodeNode.getTypSubstr( n.getParameter(Symbols.paramName()), parent );

		System.out.println("createComplexType " + typeName);
		if(typeName.equals("contract4u.DutyOfAgent")){
			System.out.println("debug");
		}

		List<Attribute<?>> identifiers = new LinkedList<>();
		// Map<String, Attribute<?>> functions = new HashMap<>();

		for(CodeNode c : n.getChildNodes()){
			Attribute<?> id = Types.complex.attributeCreateChild(c, unformedComplexTypes);
			if(id != null){
				identifiers.add(id);
			}
		}
		Attribute<?>[] theOrderedIdentifiers = new Attribute<?>[identifiers.size()];
		identifiers.toArray(theOrderedIdentifiers);

		// TODO doppelte definitionen abfangen ...
		TypeComplex complexType = unformedComplexTypes.get(typeName);
		if(complexType == null){

			if(n.getCommand() == TypeComplexOfFunction.staticGetCommand()){
				complexType = TypeComplexOfFunction.createInit(typeName, theOrderedIdentifiers);
			}
			/*
			else if(n.getCommand() == Symbols.comvirtualPrimValue()){  
				complexType = TypeComplexOfFunction.createInit(typeName, theOrderedIdentifiers);
			}
			*/
			else if(n.getCommand() == TypeComplex.staticGetCommand()){  
				complexType = TypeComplex.createInit(typeName, getSuperType(n), theOrderedIdentifiers);	
			}			
			/*
			else if(n.getCommand() == Symbols.comVirtualCompValue()){  
				complexType = TypeComplex.createInit(typeName, theOrderedIdentifiers);
			}
			*/
			else if(n.getCommand() == Symbols.comContract()){
				complexType = TypeComplex.createInit(typeName,  getSuperType(n), theOrderedIdentifiers);
				//complexType = Types.contract.complexCreate(n, null, unformedComplexTypes);
			}	
			
			else{
				// TODO error...
				System.err.println("mist unbekannter tag!");
				complexType = TypeComplex.createInit(typeName, getSuperType(n), theOrderedIdentifiers);
			}

			// TODO
			// theOrderedIdentifiers
			// m�ssen
			// elemente
			// enthalten,
			// sonst
			// gibts
			// keinen
			// typ

			return complexType;
		}
		else{
			complexType = complexType.init(typeName,  getSuperType(n), theOrderedIdentifiers);

			unformedComplexTypes.remove(typeName);
			return complexType;
		}
	}

}
