package com.conetex.contract.build;

import java.util.Map;

import com.conetex.contract.build.BuildTypes.Types;
import com.conetex.contract.build.CodeModel.BoxValue;
import com.conetex.contract.build.CodeModel.BoxType;
import com.conetex.contract.build.CodeModel.BoxValueImp;
import com.conetex.contract.build.CodeModel.BoxTypeImp;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.AttributeComplex;
import com.conetex.contract.lang.type.AttributePrimitive;
import com.conetex.contract.lang.type.Type;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;

class BuildValues{

	static class Values{
		
		static final BoxType<Object, Object> type_with_value = new BoxTypeImp<Object, Object>("value"){

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
		
		static final BoxValue<Object, Object> value = new BoxValueImp<Object, Object>("value"){

			public Value<?> valueCreate(CodeNode thisNode, TypeComplex parentType, Structure parentData) throws AbstractInterpreterException {
				System.out.println("createValues " + thisNode.getCommand());
				return BuildValues.createValue(thisNode, parentType, parentData);
			}

		};
		
		static final BoxValue<Object, Object> valueVirtComp = new BoxValueImp<Object, Object>("VIRTUAL_COMP_VALUE"){
			public Value<?> valueCreate(CodeNode thisNode, TypeComplex parentType, Structure parentData) throws AbstractInterpreterException {
				System.out.println("createValues " + thisNode.getCommand());
				return BuildValues.createValue(thisNode, parentType, parentData);
			}
		};

		static final BoxValue<Object, Object> valueVirtPrim = new BoxValueImp<Object, Object>("VIRTUAL_PRIM_VALUE"){
			public Value<?> valueCreate(CodeNode thisNode, TypeComplex parentType, Structure parentData) throws AbstractInterpreterException {
				System.out.println("createValues " + thisNode.getCommand());
				return BuildValues.createValue(thisNode, parentType, parentData);
			}
		};
		
		static final BoxValue<Object, Object> contract = new BoxValueImp<Object, Object>("contract"){
			// TODO implement
		};
		
	}
		
	public static void createValues(CodeNode n, TypeComplex type, Structure data) throws AbstractInterpreterException {
		String name = n.getCommand();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return;
		}

		for(CodeNode c : n.getChildNodes()){
			Types.complex.valueCreateChild(c, type, data);
		}

		data.fillMissingValues();
	}

	static Value<?> createValue(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {
		String name = n.getParameter(Symbols.paramName());

		Attribute<?> id = parentTyp.getSubAttribute(name); //

		if(id == null){
			System.err.println("createValue: can not identify " + name);
			return null;
		}

		Type<?> type = id.getType();
		if(type.getClass() == TypeComplex.class){
			// TODO: ok wir sind uns sicher, dass id den typ Attribute<Structur> hat. Trotzdem, warum funktioniert der cast ohne warnung?
			// Siehe auch den else-zweig...
			Structure re = ((AttributeComplex) id).createValue(parentData, n);
			
			// new
			createValues(n, (TypeComplex) type, re);
			parentData.set(name, re);
			return re;
		}
		else{
			String valueNode = n.getParameter(Symbols.paramValue());
			System.out.println("createValue " + name + " " + valueNode);
			if(valueNode != null){
				Value<?> re = ((AttributePrimitive<?>) id).createValue(n, valueNode, parentData);
				parentData.set(name, re);
				return re;
			}
		}

		return null;
	}

	public static Value<?> createValue4Function(CodeNode n, TypeComplex parentTyp, Structure parentData) throws AbstractInterpreterException {

		String name = n.getParameter(Symbols.paramName());

		TypeComplexOfFunction type = TypeComplexOfFunction.getInstance(parentTyp.getName() + "." + name);
		if(type != null){
			Structure re = Structure.create(type, null);
			// new
			createValues(n, type, re);
			// TODO hier passier garnix weil parentData keinen platz fuer die funktion hat...
			parentData.set(name, re);
			
			type.setPrototype(re);

			return re;
		}

		return null;
	}

}
