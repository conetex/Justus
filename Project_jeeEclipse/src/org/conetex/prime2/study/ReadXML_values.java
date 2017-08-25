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
import org.conetex.prime2.contractProcessing2.data.IdentifierComplex;
import org.conetex.prime2.contractProcessing2.data.IdentifierPrimitive;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.runtime.Program;
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
						
						//new
						Structure re = complexTypeRoot.createValue(null);
						ReadXML_values.createValues(r, complexTypeRoot, re);		
						
						/*
						values = ReadXML_values.createValues(r, complexTypeRoot, null);		
						Value<?>[] theValues = new Value<?>[ values.size() ];
						values.toArray( theValues );
						Value<Value<?>[]> v = complexTypeRoot.createValue(null);
						v.set(theValues);
						*/
System.out.println("success " + r.getNodeName());						
					}
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
	}
	

	
	public static List<Value<?>> createValues(Node n, Complex type, Structure data){
		String name = n.getNodeName();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}
		
		/* old
		List<Value<?>> values = new LinkedList<Value<?>>();
		*/		
		
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if( ReadXMLtools.isValue(c) ){
System.out.println("createValues " + c.getNodeName());
				Value<?> v = createValue( c, type, data );
				if(v != null){
					/* old
					values.add( v );
					*/
					
					// new
					try {
						data.set(c.getNodeName(), v);
					} catch (Invalid e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}				
		}		
		
		/* old
		Value<?>[] theValues = new Value<?>[ values.size() ];
		values.toArray( theValues );	
		try {
			data.set(theValues);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
		return values;
		*/
		
		//new
		return null;
	}
	
	
	public static Value<?> createValue(Node n, Complex parentTyp, Structure parentData){
		
		// + " (local: " + n.getLocalName() + ")";
		
		String name = n.getNodeName();
		
		if( ReadXMLtools.isIdentifier(n) ){
			name = ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME);
		}
		else if ( name.equals(Symbol.FUNCTION) ){
			name = ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME);
		}
		
	    Identifier<?> id = parentTyp.getSubIdentifier(name); //
	    if(id == null){
	    	System.err.println("createValue: can not identify " + name);
	    	return null;
	    }
	    
	    AbstractType<?> type = id.getType();
		if( type.getClass() == Complex.class ){
			Structure re = ( (IdentifierComplex)id ).createValue(parentData);
			
			// new
			createValues(n, (Complex) type, re);
			
			/* old
			List<Value<?>> subvalues = createValues(n, (Complex) type, re);
			Value<?>[] theValues = new Value<?>[ subvalues.size() ];
			subvalues.toArray( theValues );	
			try {
				re.set(theValues);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			return re;
		}
		else{
			String valueNode = ReadXMLtools.getNodeValue(n);
			if(valueNode != null){
				return ( (IdentifierPrimitive<?>)id ) .createValue(valueNode, parentData);
			}
		}
		
		
		
		return null;			
				
	}
	
	
	



	
	
}
