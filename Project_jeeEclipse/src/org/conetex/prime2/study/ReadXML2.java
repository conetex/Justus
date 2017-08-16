package org.conetex.prime2.study;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.conetex.prime2.study.ReadXML.FunctionBuilder;
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
	
	private static List<Complex> createComplexList(Node n){
		List<Complex> re = new LinkedList<Complex>();
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			String name = c.getNodeName();
			if( name.equals(Symbol.COMPLEX) || name.equals(Symbol.FUNCTION) ) {
System.out.println("createComplexList " + name);
				Complex complexType = createComplex(c);
				if(complexType != null){
					re.add(complexType);			
				}
			}
		}
		return re;
	}
	
	public static Complex createComplex(Node n){
				
		List<Identifier<?>> identifiers = new LinkedList<Identifier<?>>();

		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if(c.getNodeType() == Node.ELEMENT_NODE && c.getNodeName() == Symbol.ELEMENT){ 
					createAttributesValues( c, identifiers//, values, complexTyps 
							);					
			}				
		}		
		
		Identifier<?>[] theOrderedIdentifiers = new Identifier<?>[ identifiers.size() ];
		identifiers.toArray( theOrderedIdentifiers );
		
		Complex complexType = null;
		try {
			complexType = Complex.createComplexDataType(theOrderedIdentifiers); // TODO theOrderedIdentifiers müssen elemente enthalten, sonst gibts keinen typ
		} catch (DuplicateIdentifierNameExeption | Identifier.NullIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
				
		return complexType;
		
	}	
	
	public static boolean createAttributesValues(Node n, List<Identifier<?>> dattributes//, List<Value<?>> values, Map<Complex, List<FunctionBuilder>> complexTyps
			){
		
		//String name = n.getNodeName();// + " (local: " + n.getLocalName() + ")";
		String name = getAttribute(n, Symbol.NAME);//c.getNodeName();
//System.out.println("createAttributesValues " + name);

		NamedNodeMap attributes = n.getAttributes();
		//Node dataTypNode = attributes.getNamedItem( Symbol.TYPE );
		
		//String valueNode = null;		
		//if(dataTypNode != null){			
		//	String valueNode = getNodeValue(n);
		//	if(valueNode != null){
				// ... Primitiv	
				//String type = dataTypNode.getNodeValue();
				String type = getAttribute(n, Symbol.TYPE);
		if(type.startsWith(Symbol.SIMPLE_TYPE_NS)){
				//String value = valueNode;
				Identifier<?> attribute = createSimpleAttribute(name, type.substring(Symbol.SIMPLE_TYPE_NS.length())); //
System.out.println("createAttributesValues " + name + " " + type.substring(Symbol.SIMPLE_TYPE_NS.length()) + " / " + type + " ==> " + attribute);				
				if(attribute != null){
					dattributes.add(attribute);
					return true;
				}
		}
		else {
			
		}
		
		
				return false;		
				
		//	}			
		//}
/*		
		// ... Complex
		//Structure s = createState(n, complexTyps);
		List<FunctionBuilder> functionBuilders = new LinkedList<FunctionBuilder>();
		List<Value<?>> subvalues = new LinkedList<Value<?>>();		
		Complex ct = createState2(n, functionBuilders, subvalues, complexTyps);
		
		if(ct != null){
			// TODO keine doppelten complexTyps erlauben ... gib ihnen auch namen...
			//return complexType;
			complexTyps.put(ct, functionBuilders);
			
			Value<?>[] theValues = new Value<?>[ subvalues.size() ];
			subvalues.toArray( theValues );			
			
			//Structure s = ct.construct(theValues);		
			
			//if(s != null){
			    Identifier<Value<?>[]> attribute = createComplexAttribute(name, ct); //
				if(attribute != null){
					Value<Value<?>[]> v = createComplexValue(attribute, ct, theValues);
					if(v != null){
						dattributes.add(attribute);
						values.add(v);						
						return true;
					}
				}
			//}			
			
		}

		return false;			
*/				
	}	
	
	public static Identifier<Value<?>[]> createComplexAttribute(String name, Complex t){
		//PrimitiveDataType<Structure> simpleType = PrimitiveDataType.getInstance( Value.Implementation.Struct.class.getSimpleName() );
		
		Label str = new Label(); 
		try {
			str.set(name);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<Value<?>[]> attribute = null;
		try {
			attribute = t.createIdentifier( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}	
	
	public static <T> Identifier<T> createSimpleAttribute(String name, String type){
		Primitive<T> simpleType = Primitive.<T>getInstance( type );	
		if(simpleType == null) {
			System.err.println("unknown simple Type " + type);
			return null;
		}
		Label str = new Label(); 
		try {
			str.set(name);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<T> attribute = null;
		try {
			attribute = simpleType.createIdentifier( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}

	public static String getNodeValue(Node n){
		String re = getAttribute(n, "value");
		if(re != null){
			return re;				
		}
		NodeList children = n.getChildNodes();
		if(children != null && children.getLength() == 1){
			Node textValueNode = children.item(0);
			short textValueNodeType = textValueNode.getNodeType();
			if(textValueNodeType == Node.TEXT_NODE){		
				return textValueNode.getNodeValue();
			}
		}
		return null;
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
