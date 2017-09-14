package org.conetex.prime2.contractProcessing2.interpreter.build;

import java.util.List;

import org.conetex.prime2.contractProcessing2.data.Attribute;
import org.conetex.prime2.contractProcessing2.data.AttributeComplex;
import org.conetex.prime2.contractProcessing2.data.AttributePrimitive;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.interpreter.SyntaxNode;
import org.conetex.prime2.contractProcessing2.lang.Symbol;

public class Values {
	public static List<Value<?>> createValues(SyntaxNode n, Complex type, Structure data){
		String name = n.getTag();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}
		
		/* old
		List<Value<?>> values = new LinkedList<Value<?>>();
		*/		
		
		for(SyntaxNode c : n.getChildNodes()){

			if( c.isValue() ){
System.out.println("createValues " + c.getTag());
				Value<?> v = createValue( c, type, data );
				if(v != null){
					/* old
					values.add( v );
					*/			
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
	
	
	public static Value<?> createValue(SyntaxNode n, Complex parentTyp, Structure parentData){
		
		// + " (local: " + n.getLocalName() + ")";
		
		String name = n.getTag();
		
		if( n.isIdentifier() ){
			name =  n.getName();
		}
		else if ( name.equals(Symbol.FUNCTION) ){
			name =  n.getName();
		}
		
	    Attribute<?> id = parentTyp.getSubAttribute(name); //
	    if(id == null){
	    	System.err.println("createValue: can not identify " + name);
	    	return null;
	    }
	    AbstractType<?> type = id.getType();
		if( type.getClass() == Complex.class ){
			Structure re = ( (AttributeComplex)id ).createValue(parentData);
			
			// new
			createValues(n, (Complex)type, re);
			try {
				parentData.set(name, re);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
			String valueNode = n.getValue();
System.out.println("createValue " + name + " " + valueNode);
			if(valueNode != null){
				Value<?> re = ( (AttributePrimitive<?>)id ) .createValue(valueNode, parentData);
				try {
					parentData.set(name, re);
				} catch (Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return re;
			}
		}
		
		
		
		return null;			
				
	}

}
