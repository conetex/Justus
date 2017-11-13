package com.conetex.contract.build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.conetex.contract.build.CodeModel.BoxType;
import com.conetex.contract.build.CodeModel.BoxFun;
import com.conetex.contract.build.CodeModel.BoxValue;
import com.conetex.contract.build.CodeModel.BoxTypeImp;
import com.conetex.contract.build.CodeModel.BoxFunImp;
import com.conetex.contract.build.CodeModel.BoxValueImp;
import com.conetex.contract.build.CodeModel.BoxValueTypeFunImp;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;

public class BuildTypes{

	static class Types{

		public static abstract class ComplexImp extends BoxValueTypeFunImp<Structure, Object>{

			ComplexImp(String name) {
				super(name);
			}

			@Override
			public abstract Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

			public abstract Function<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

		}
		
		public static abstract class CompBox extends BoxFunImp<Structure, Object>{

			CompBox(String name) {
				super(name);
			}

			@Override
			public abstract Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

			public abstract Accessible<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException;

		}

		// TODO 1 anmelden wie complex
		static final CompBox _functions_in_complex = new CompBox("complex"){

			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {

				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				return this.functionCreateImpl(thisNode, thisType);
			}

			// this is just to create functions of complex
			public Accessible<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {
				List<CodeNode> children = thisNode.getChildNodes();
				for(CodeNode c : children){
					this.functionCreateChild(c, thisType);
				}
				return null;
			}

		};
		
		// TODO 1 anmelden wie complex
		static final BoxValue<Structure, Object> _values_in_complex = new BoxValueImp<Structure, Object>("complex"){

		};
		
		static final BoxType<Structure, Object> _complex = new BoxTypeImp<Structure, Object>("complex"){

			@Override
			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}

		};
		
		static final ComplexImp complex = new ComplexImp("complex"){

			@Override
			public Value<?> valueCreate(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Accessible<Structure> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {

				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				return this.functionCreateImpl(thisNode, thisType);
			}

			// this is just to create functions of complex
			@Override
			public Function<Structure> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {
				List<CodeNode> children = thisNode.getChildNodes();
				for(CodeNode c : children){
					this.functionCreateChild(c, thisType);
				}
				return null;
			}

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}

	

		};

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
			public Accessible<?> functionCreate(CodeNode thisNode, TypeComplex parentType) throws AbstractInterpreterException {

				TypeComplex thisType = BuildFunctions.getThisNodeType(thisNode, parentType);

				return this.functionCreateImpl(thisNode, thisType);
			}

			// this is just to create functions of complex
			public Function<Structure> _functionCreateImpl_(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {
				List<CodeNode> children = thisNode.getChildNodes();
				for(CodeNode c : children){
					this.functionCreateChild(c, thisType);
				}
				return null;
			}

			public Function<?> functionCreateImpl(CodeNode thisNode, TypeComplex thisType) throws AbstractInterpreterException {
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
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public TypeComplex complexCreate(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
				return BuildTypes.createComplexType(n, parent, unformedComplexTypes, null);
			}
		}
		
		static final BoxValueTypeFunImp<Object, Object> contract = new ContractClass("contract");
		
		static final BoxType<Object, Object> attribute = new BoxTypeImp<Object, Object>("attribute"){

			@Override
			public Attribute<?> attributeCreate(CodeNode c, Map<String, TypeComplex> unformedComplexTypes) throws AbstractInterpreterException {
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
						id = TypePrimitive.createAttribute(idName, idTypeName.substring(Symbols.litSimpleTypeNS().length()));
					}
					else{
						// Complex
						// referringComplexTypeNames.add(idTypeName);
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

	public static List<TypeComplex> createComplexTypes(CodeNode n) throws AbstractInterpreterException {
		TypeComplex.clearInstances();
		Map<String, TypeComplex> unformedComplexTypes = new HashMap<>();
		Set<String> referringComplexTypeNames = new TreeSet<>();
		List<TypeComplex> re = new LinkedList<>();

		Recursive<Run> recursive = new Recursive<>();
		recursive.function = (CodeNode node, TypeComplex parent) -> {
			for(CodeNode c : node.getChildNodes()){
				TypeComplex complexType = Types.complex.complexCreateChild(c, parent, unformedComplexTypes);
				if(complexType != null){
					re.add(complexType);
					recursive.function.run(c, complexType);
				}
			}
		};
		TypeComplex complexTypeRoot = createComplexType(n, null, unformedComplexTypes, referringComplexTypeNames);
		if(complexTypeRoot != null){
			re.add(complexTypeRoot);
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

		boolean error = false;
		for(String typeName : referringComplexTypeNames){
			System.out.println("createComplexList referenced complex Type " + typeName);
			if(TypeComplex.getInstance(typeName) == null){
				error = true;
				System.err.println("createComplexList unkown Complex: " + typeName);
				// TODO throw Exception: wir kennen einen Typen nicht ...
			}
		}
		if(error){
			TypeComplex.clearInstances();
			return null;
		}

		return re;
	}

	public static TypeComplex createComplexType(CodeNode n, TypeComplex parent, Map<String, TypeComplex> unformedComplexTypes, Set<String> _referringComplexTypeNames)
			throws AbstractInterpreterException {
		String typeName = n.getParameter(Symbols.paramName());
		if(typeName == null){
			// TODO Exception
			System.err.println("no typeName for complex");
			return null;
		}
		if(parent != null){
			typeName = parent.getName() + "." + typeName;
		}

		System.out.println("createComplexType " + typeName);
		if(typeName.endsWith("contract4u")){
			System.out.println("createComplexType " + typeName);
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

			if(n.getCommand() == Symbols.comFunction()){
				complexType = TypeComplexOfFunction.createInit(typeName, theOrderedIdentifiers);
			}
			else{
				complexType = TypeComplex.createInit(typeName, theOrderedIdentifiers);
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
			complexType.init(typeName, theOrderedIdentifiers);

			unformedComplexTypes.remove(typeName);
			return complexType;
		}
	}

}
