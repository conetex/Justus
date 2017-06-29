package org.conetex.prime2.study_contractProcessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.values.Label;
import org.conetex.prime2.contractProcessing2.data.values.Base64_256;
import org.conetex.prime2.contractProcessing2.data.values.Int;
import org.conetex.prime2.contractProcessing2.data.values.MailAddress;
import org.conetex.prime2.contractProcessing2.data.values.MailAddress64;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

// TODO: Names 4 ComplexDataTyp. This is 4 strong typing
// TODO: parsing Schemas / dtd to define the types
// TODO: Array-Typs ...
// TODO: length of SimpleDat encoded in Typ-Name ...


public class Data {

	
	
	public static void mainMail(String[] args) throws ValueException {
		Primitive<String> simpleType = Primitive.getInstance(MailAddress64.class);
		//simpleType = new PrimitiveDataType<  String   > ( MailAddress64.class , new Value.ValueFactory<String>(){	public MailAddress64 createValueImp() { return new MailAddress64();  } } );
		
		Label str = new Label();
		str.set("einName");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createIdentifier( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		Complex complexType = null;
		try {
			complexType = Complex.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value<String> value = state.getValue("einName", MailAddress.class);
		try {
			value.set("matthias.franke@conetex.com");
			value.set("2@3.1");
			//value.set("2@3");
			//value.set("matthias.franke.conetex.com");
			value.set("2@3.de");
			//value.set("2ü@3.de");
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(attribute);
	}	
	
	public static void main64(String[] args) throws ValueException{
		Primitive<String> simpleType = Primitive.getInstance(Base64_256.class);
		//simpleType = new PrimitiveDataType<   String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		Label str = new Label();
		str.set("einStr");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createIdentifier( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		Complex complexType = null;
		try {
			complexType = Complex.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainInt(String[] args) throws ValueException{
		
		Primitive<Integer> simpleType = Primitive.getInstance(Int.class);
		Label str = new Label();
		str.set("ein Name");
		Identifier<Integer> attribute = null;
		try {
			attribute = simpleType.createIdentifier( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		Complex complexType = null;
		try {
			complexType = Complex.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value<Integer> value = state.getValue("ein Name", Int.class);
		try {
			value.set(3);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(attribute);
		
	}

	
	

	

	

	
	
		

	

	

}
