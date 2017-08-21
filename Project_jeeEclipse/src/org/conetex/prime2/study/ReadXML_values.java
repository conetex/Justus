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
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullIdentifierException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.conetex.prime2.study.ReadXML.FunctionBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML_values {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Invalid {

		FileInputStream is = new FileInputStream( "input01.xml" );

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );

		List<Complex> complexTyps = null;
		List<Value<?>> values = null; 
				
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node r = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(complexTyps == null){
					complexTyps = ReadXML_types.createComplexTypes(r);
System.out.println("process " + r.getNodeName());
					if(complexTyps != null){
						Complex complexTypeRoot = Complex.getInstance(ReadXMLtools.getRootType(r));
						values = ReadXML_values.createValues(r, complexTypeRoot);		
						Value<?>[] theValues = new Value<?>[ values.size() ];
						values.toArray( theValues );
						Value<Value<?>[]> v = complexTypeRoot.createValue();
						v.set(theValues);
System.out.println("success " + r.getNodeName());						
					}
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
	}
	

	
	public static List<Value<?>> createValues(Node n, Complex type){
		String name = n.getNodeName();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}
		
		List<Value<?>> values = new LinkedList<Value<?>>();		
		
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if( ReadXMLtools.isValue(c) ){
System.out.println("createValues " + c.getNodeName());
				Value<?> v = createValue( c, type);
				if(v != null){
					values.add( v );
				}
			}				
		}		
		
		return values;
	}
	
	
	public static Value<?> createValue(Node n, Complex parentTyp){
		
		// + " (local: " + n.getLocalName() + ")";
		
		String name = null;
		
		if(ReadXMLtools.isIdentifier(n)){
			name = ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME);
		}
		else{
			name = n.getNodeName();
		}
		
	    Identifier<?> id = parentTyp.getSubIdentifier(name); //
	    if(id == null){
	    	System.err.println("can not identify " + name);
	    	return null;
	    }
	    
	    AbstractType<?> type = id.getType();
		if( type.getClass() == Complex.class ){
			List<Value<?>> subvalues = createValues(n, (Complex) type);
			Value<?>[] theValues = new Value<?>[ subvalues.size() ];
			subvalues.toArray( theValues );		
			return ( (Identifier<Value<?>[]>)id ).createValue(theValues);
		}
		else{
			String valueNode = ReadXMLtools.getNodeValue(n);
			if(valueNode != null){
				return id.createValue(valueNode);
			}
		}
		
		
		
		return null;			
				
	}
	
	
	



	
	
}
