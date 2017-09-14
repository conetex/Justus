package com.conetex.contract.interpreter.build;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Attribute.DuplicateIdentifierNameExeption;
import com.conetex.contract.data.Attribute.NullIdentifierException;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.type.Complex.ComplexWasInitializedExeption;
import com.conetex.contract.data.type.Complex.DublicateComplexException;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.lang.Symbol;

public class Types {
	private static class Recursive<I> {
		public I function;
	}

	private static interface Run {
		public void run(SyntaxNode node, Complex parent);
	}

	public static List<Complex> createComplexTypes(SyntaxNode n) {
		Complex.clearInstances();
		Map<String, Complex> unformedComplexTypes = new HashMap<String, Complex>();
		Set<String> referringComplexTypeNames = new TreeSet<String>();
		List<Complex> re = new LinkedList<Complex>();

		Recursive<Run> recursive = new Recursive<Run>();
		recursive.function = (SyntaxNode node, Complex parent) -> {
			for (SyntaxNode c : node.getChildNodes()) {
				if (c.isType()) {
					Complex complexType = createComplexType(c, parent, unformedComplexTypes, referringComplexTypeNames);
					if (complexType != null) {
						re.add(complexType);
						recursive.function.run(c, complexType);
					}
				}
			}
		};
		Complex complexTypeRoot = createComplexType(n, null, unformedComplexTypes, referringComplexTypeNames);
		if (complexTypeRoot != null) {
			re.add(complexTypeRoot);
			recursive.function.run(n, complexTypeRoot);// root == null
		}
		// recursive.function.run(n, null);

		for (String createdComplex : Complex.getInstanceNames()) {
			System.out.println("createComplexList known: " + createdComplex);
		}

		if (unformedComplexTypes.size() > 0) {
			Complex.clearInstances();
			for (String unformedComplex : unformedComplexTypes.keySet()) {
				System.err.println("createComplexList unknown Complex: " + unformedComplex);
			}
			// TODO throw Exception: wir konnten nicht alles kompilieren!!!
			return null;
		}

		boolean error = false;
		for (String typeName : referringComplexTypeNames) {
			System.out.println("createComplexList referenced complex Type " + typeName);
			if (Complex.getInstance(typeName) == null) {
				error = true;
				System.err.println("createComplexList unkown Complex: " + typeName);
				// TODO throw Exception: wir kennen einen Typen nicht ...
			}
		}
		if (error) {
			Complex.clearInstances();
			return null;
		}

		return re;
	}

	private static Complex createComplexType(SyntaxNode n, Complex parent, Map<String, Complex> unformedComplexTypes,
			Set<String> referringComplexTypeNames) {
		String typeName = n.getName();
		if (typeName == null) {
			// TODO Exception
			System.err.println("no typeName for complex");
			return null;
		}
		if (parent != null) {
			typeName = parent.getName() + "." + typeName;
		}

		System.out.println("createComplexType " + typeName);
		if (typeName.endsWith("contract4u")) {
			System.out.println("createComplexType " + typeName);
		}
		List<Attribute<?>> identifiers = new LinkedList<Attribute<?>>();
		for (SyntaxNode c : n.getChildNodes()) {
			String idTypeName = null;
			String idName = null;
			Attribute<?> id = null;
			if (c.isIdentifier()) {
				idTypeName = c.getType();
				idName = c.getName();
				if (idTypeName == null) {
					System.err.println("can not get Type of " + c.getTag() + " " + idName);
				} else {
					if (idTypeName.startsWith(Symbol.SIMPLE_TYPE_NS)) {
						// Simple
						id = Primitive.createAttribute(idName, idTypeName.substring(Symbol.SIMPLE_TYPE_NS.length()));
					} else {
						// Complex
						// referringComplexTypeNames.add(idTypeName);
						id = Complex.createAttribute(idName, idTypeName, unformedComplexTypes);
					}
				}
			} else if (c.getTag() == Symbol.FUNCTION) {
				// Complex
				idTypeName = c.getType();
				idName = c.getName();
				// referringComplexTypeNames.add(typeName + "." + idTypeName);// TODO BUG !!!
				id = Complex.createAttribute(idName, idTypeName, unformedComplexTypes);
			} else {
				continue;
			}
			if (id != null) {
				identifiers.add(id);
			} else {
				// TODO Exception
				System.err.println("createComplexType can not create Identifier " + idName + " (" + idTypeName + ")");
			}
		}
		Attribute<?>[] theOrderedIdentifiers = new Attribute<?>[identifiers.size()];
		identifiers.toArray(theOrderedIdentifiers);

		// TODO doppelte definitionen abfangen ...
		Complex complexType = unformedComplexTypes.get(typeName);
		if (complexType == null) {
			try {
				complexType = Complex.createInit(typeName, theOrderedIdentifiers); // TODO theOrderedIdentifiers müssen
																					// elemente enthalten, sonst gibts
																					// keinen typ
			} catch (DuplicateIdentifierNameExeption | Attribute.NullIdentifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (DublicateComplexException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				e.printStackTrace();
				return null;
			}
			return complexType;
		} else {
			try {
				complexType.init(typeName, theOrderedIdentifiers);
			} catch (DuplicateIdentifierNameExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (NullIdentifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ComplexWasInitializedExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (DublicateComplexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			unformedComplexTypes.remove(typeName);
			return complexType;
		}
	}

}
