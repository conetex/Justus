package org.conetex.prime2.study;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullIdentifierException;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML2 {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		FileInputStream is = new FileInputStream( "input01.xml" );

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );

		List<Complex> complexTyps = null;
		
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node c = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(complexTyps == null){
					complexTyps = createComplexList(c);
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}		
		
	}
	
	public static class Recursive<I>{ public I function; }
	
	private static interface Run { public void run(Node node, Complex parent); }
	
	private static List<Complex> createComplexList(Node n){
		Complex.clearInstances();
		Map<String, Complex> unformedComplexTypes = new HashMap<String, Complex>();
		Set<String> referringComplexTypeNames = new TreeSet<String>();
		List<Complex> re = new LinkedList<Complex>();
		
		Recursive<Run> recursive = new Recursive<Run>();
		recursive.function = (Node node, Complex parent) -> 
		{
			NodeList children = node.getChildNodes();
			for(int i = 0; i < children.getLength(); i++){
				Node c = children.item(i);
				String name = c.getNodeName();
				if( name.equals(Symbol.COMPLEX) || name.equals(Symbol.FUNCTION) ) {
					Complex complexType = createComplex(c, parent, unformedComplexTypes, referringComplexTypeNames);
					if(complexType != null){
						re.add(complexType);			
						recursive.function.run(c, complexType);
					}
				}
			}
		};
		recursive.function.run(n, null);
		
		/*
		if(unformedComplexTypes.size() > 0){
			Complex.clearInstances();
			for(String unformedComplex : unformedComplexTypes.keySet()){
				System.err.println("createComplexList unformed Complex: " + unformedComplex);			
			}
			// TODO throw Exception: wir konnten nicht alles kompilieren!!!
			return null;
		}*/
		
for(String createdComplex : Complex.getInstanceNames()){
System.out.println("createComplexList known: " + createdComplex);			
}

		boolean error = false;
		for(String typeName : referringComplexTypeNames){
System.out.println("createComplexList referenced complex Type " + typeName);
			if( Complex.getInstance(typeName) == null ){
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
	
	public static Complex createComplex(Node n, Complex parent, Map<String, Complex> unformedComplexTypes, Set<String> referringComplexTypeNames){
		String typeName = getAttribute(n, Symbol.TYPE_NAME);
		if(typeName == null){
			// TODO Exception
			System.err.println("no typeName for complex");			
			return null;
		}
		if(parent != null){
			typeName = parent.getName() + "." + typeName;
		}
System.out.println("createComplex " + typeName);		
		List<Identifier<?>> identifiers = new LinkedList<Identifier<?>>();
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if(c.getNodeType() == Node.ELEMENT_NODE){ 
				Identifier<?> id = null;
				if( (c.getNodeName() == Symbol.IDENTIFIER) ){ 
					String idTypeName = getAttribute(c, Symbol.IDENTIFIER_TYPE);
					String idName = getAttribute(c, Symbol.IDENTIFIER_NAME);
					if(idTypeName.startsWith(Symbol.SIMPLE_TYPE_NS)){
						// Simple
						id = Primitive.createIdentifier( idName, idTypeName.substring(Symbol.SIMPLE_TYPE_NS.length()) );	
					}
					else{
						// Complex
						referringComplexTypeNames.add(idTypeName);
						id = Complex.createIdentifier( idName, idTypeName, unformedComplexTypes );						
					}
				}
				else if(c.getNodeName() == Symbol.FUNCTION){ 
					// Complex
					String idTypeName = typeName + "." + getAttribute(c, Symbol.FUNCTION_NAME);
					String idName = getAttribute(c, Symbol.FUNCTION_NAME);
					referringComplexTypeNames.add(idTypeName);
					id = Complex.createIdentifier( idName, idTypeName, unformedComplexTypes );				
				}
				if(id != null){
					identifiers.add(id);
				}
				else{
					// TODO Exception
				}
			}				
		}		
		Identifier<?>[] theOrderedIdentifiers = new Identifier<?>[ identifiers.size() ];
		identifiers.toArray( theOrderedIdentifiers );
		
		Complex complexType = unformedComplexTypes.get(typeName);
		if(complexType == null){
			try {
				complexType = Complex.createInit(typeName, theOrderedIdentifiers); // TODO theOrderedIdentifiers müssen elemente enthalten, sonst gibts keinen typ
			} catch (DuplicateIdentifierNameExeption | Identifier.NullIdentifierException e) {
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
		}
		else{
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
	
	
	
	
	
	public static String getAttribute(Node n, String a){
		NamedNodeMap attributes = n.getAttributes();
		Node attributeNode = attributes.getNamedItem( a );			
		if(attributeNode != null){
			return attributeNode.getNodeValue();
		}
		return null;
	}
}
