package com.conetex.contract.build;

import java.util.List;

import com.conetex.contract.build.exceptionLang.PrototypeWasInitialized;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.AttributeComplex;
import com.conetex.contract.data.AttributePrimitive;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.FunctionAttributes;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;

public class BuildValues {
	public static List<Value<?>> createValues(CodeNode n, Complex type, Structure data) throws PrototypeWasInitialized, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if (type == null) {
			System.err.println("can not recognize type of " + name);
			return null;
		}

		/*
		 * old List<Value<?>> values = new LinkedList<Value<?>>();
		 */

		for (CodeNode c : n.getChildNodes()) {

			if (c.isAttributeInitialized() || c.isFunction() || c.isValue()) {
				System.out.println("createValues " + c.getCommand());
				Value<?> v = createValue(c, type, data);
				if (v != null) {
					/*
					 * old values.add( v );
					 */
				}
			}
		}

		data.fillMissingValues();

		/*
		 * old Value<?>[] theValues = new Value<?>[ values.size() ]; values.toArray(
		 * theValues ); try { data.set(theValues); } catch (Invalid e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * 
		 * return values;
		 */

		// new
		return null;
	}

	public static Value<?> createValue(CodeNode n, Complex parentTyp, Structure parentData) throws PrototypeWasInitialized, UnknownCommandParameter, UnknownCommand {

		// + " (local: " + n.getLocalName() + ")";

		String name = n.getCommand();

		if (name.equals(Symbol.FUNCTION)) {
			name = n.getName();

			FunctionAttributes type = FunctionAttributes.getInstance(parentTyp.getName() + "." + name);
			if (type != null) {
				// Structure re = ((AttributeComplex) id).createValue(parentData);
				Structure re = Structure.create(type, null);
				// new
				createValues(n, (Complex) type, re);
				try {
					// TODO hier passier garnix weil parentData keinen platz für die funktion hat...
					parentData.set(name, re);
				}
				catch (Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TODO der cast ist scheißßß
				((FunctionAttributes) type).setPrototype(re);

				/*
				 * old List<Value<?>> subvalues = createValues(n, (Complex) type, re);
				 * Value<?>[] theValues = new Value<?>[ subvalues.size() ]; subvalues.toArray(
				 * theValues ); try { re.set(theValues); } catch (Invalid e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 */

				return re;
			}

			return null;
		}

		if (n.isAttribute() || n.isAttributeInitialized() || name.equals(CommandSymbols.VIRTUAL_PRIM_VALUE) || name.equals(CommandSymbols.VIRTUAL_COMP_VALUE)) {
			name = n.getName();
		}

		Attribute<?> id = parentTyp.getSubAttribute(name); //

		if (id == null) {
			System.err.println("createValue: can not identify " + name);
			return null;
		}

		AbstractType<?> type = id.getType();
		if (type.getClass() == Complex.class) {
			Structure re = ((AttributeComplex) id).createValue(parentData);

			// new
			createValues(n, (Complex) type, re);
			try {
				parentData.set(name, re);
			}
			catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * old List<Value<?>> subvalues = createValues(n, (Complex) type, re);
			 * Value<?>[] theValues = new Value<?>[ subvalues.size() ]; subvalues.toArray(
			 * theValues ); try { re.set(theValues); } catch (Invalid e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 */

			return re;
		}
		else {
			String valueNode = n.getValue();
			System.out.println("createValue " + name + " " + valueNode);
			if (valueNode != null) {
				Value<?> re = ((AttributePrimitive<?>) id).createValue(valueNode, parentData);
				try {
					parentData.set(name, re);
				}
				catch (Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return re;
			}
		}

		return null;

	}

}
