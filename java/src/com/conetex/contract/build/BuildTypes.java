package com.conetex.contract.build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.conetex.contract.build.BuildFunctions.Data;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.FunctionAttributes;

public class BuildTypes{

	private static class Recursive<I> {

		public Recursive( ) {
		}

		public I function;

	}

	private static interface Run{
		public void run(CodeNode node, Complex parent) throws AbstractInterpreterException;
	}

	public static List<Complex> createComplexTypes(CodeNode n) throws AbstractInterpreterException {
		Complex.clearInstances();
		Map<String, Complex> unformedComplexTypes = new HashMap<>();
		Set<String> referringComplexTypeNames = new TreeSet<>();
		List<Complex> re = new LinkedList<>();

		Recursive<Run> recursive = new Recursive<>();
		recursive.function = (CodeNode node, Complex parent) -> {
			for(CodeNode c : node.getChildNodes()){
				Complex complexType = Data.complex.complexCreateChild(c, parent, unformedComplexTypes);
				if(complexType != null){
					re.add(complexType);
					recursive.function.run(c, complexType);
				}
			}
		};
		Complex complexTypeRoot = createComplexType(n, null, unformedComplexTypes, referringComplexTypeNames);
		if(complexTypeRoot != null){
			re.add(complexTypeRoot);
			recursive.function.run(n, complexTypeRoot);
		}

		for(String createdComplex : Complex.getInstanceNames()){
			System.out.println("createComplexList known: " + createdComplex);
		}

		if(unformedComplexTypes.size() > 0){
			Complex.clearInstances();
			for(String unformedComplex : unformedComplexTypes.keySet()){
				System.err.println("createComplexList unknown Complex: " + unformedComplex);
			}
			// TODO throw Exception: wir konnten nicht alles kompilieren!!!
			return null;
		}

		boolean error = false;
		for(String typeName : referringComplexTypeNames){
			System.out.println("createComplexList referenced complex Type " + typeName);
			if(Complex.getInstance(typeName) == null){
				error = true;
				System.err.println("createComplexList unkown Complex: " + typeName);
				// TODO throw Exception: wir kennen einen Typen nicht ...
			}
		}
		if(error){
			Complex.clearInstances();
			return null;
		}

		return re;
	}

	public static Complex createComplexType(CodeNode n, Complex parent, Map<String, Complex> unformedComplexTypes, Set<String> _referringComplexTypeNames)
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
			Attribute<?> id = Data.complex.attributeCreateChild(c, unformedComplexTypes);
			if(id != null){
				identifiers.add(id);
			}
		}
		Attribute<?>[] theOrderedIdentifiers = new Attribute<?>[identifiers.size()];
		identifiers.toArray(theOrderedIdentifiers);

		// TODO doppelte definitionen abfangen ...
		Complex complexType = unformedComplexTypes.get(typeName);
		if(complexType == null){

			if(n.getCommand() == Symbols.comFunction()){
				complexType = FunctionAttributes.createInit(typeName, theOrderedIdentifiers);
			}
			else{
				complexType = Complex.createInit(typeName, theOrderedIdentifiers);
			}

			// TODO
			// theOrderedIdentifiers
			// müssen
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
