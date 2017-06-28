package org.conetex.prime2.study_contractProcessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Structure;
import org.conetex.prime2.contractProcessing2.data.Type;
import org.conetex.prime2.contractProcessing2.data.Type.ComplexDataType;
import org.conetex.prime2.contractProcessing2.data.Type.PrimitiveDataType;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateAttributeNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullAttributeException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation;
import org.conetex.prime2.contractProcessing2.data.Value.Interface;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueFactory;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.*;

// TODO: Names 4 ComplexDataTyp. This is 4 strong typing
// TODO: parsing Schemas / dtd to define the types
// TODO: Array-Typs ...
// TODO: length of SimpleDat encoded in Typ-Name ...


public class Data {

	
	
	public static void mainMail(String[] args) throws ValueException {
		PrimitiveDataType<String> simpleType = PrimitiveDataType.getInstance(MailAddress64.class);
		//simpleType = new PrimitiveDataType<  String   > ( MailAddress64.class , new Value.ValueFactory<String>(){	public MailAddress64 createValueImp() { return new MailAddress64();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einName");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = Type.ComplexDataType.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<String> value = state.getValue("einName", MailAddress.class);
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
		PrimitiveDataType<String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		//simpleType = new PrimitiveDataType<   String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einStr");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = Type.ComplexDataType.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainInt(String[] args) throws ValueException{
		
		PrimitiveDataType<Integer> simpleType = PrimitiveDataType.getInstance(Int.class);
		ASCII8 str = new ASCII8();
		str.set("ein Name");
		Identifier<Integer> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = Type.ComplexDataType.createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<Integer> value = state.getValue("ein Name", Int.class);
		try {
			value.set(3);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(attribute);
		
	}

	
	

	

	

	
	
		

	

	

}
