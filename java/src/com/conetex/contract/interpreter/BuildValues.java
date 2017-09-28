package com.conetex.contract.interpreter;

import java.util.List;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.AttributeComplex;
import com.conetex.contract.data.AttributePrimitive;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.runtime.exceptionValue.Invalid;

public class BuildValues {
	public static List<Value<?>> createValues(CodeNode n, Complex type, Structure data) {
		String name = n.getCommand();
		if (type == null) {
			System.err.println("can not recognize type of " + name);
			return null;
		}

		/*
		 * old List<Value<?>> values = new LinkedList<Value<?>>();
		 */

		for (CodeNode c : n.getChildNodes()) {

			if (c.isValue()) {
				System.out.println("createValues " + c.getCommand());
				Value<?> v = createValue(c, type, data);
				if (v != null) {
					/*
					 * old values.add( v );
					 */
				}
			}
		}

		/*
		 * old Value<?>[] theValues = new Value<?>[ values.size() ];
		 * values.toArray( theValues ); try { data.set(theValues); } catch
		 * (Invalid e) { // TODO Auto-generated catch block e.printStackTrace();
		 * }
		 * 
		 * 
		 * return values;
		 */

		// new
		return null;
	}

	public static Value<?> createValue(CodeNode n, Complex parentTyp, Structure parentData) {

		// + " (local: " + n.getLocalName() + ")";

		String name = n.getCommand();

		if (n.isIdentifier()) {
			name = n.getName();
		}
		else if (name.equals(Symbol.FUNCTION)) {
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
			 * old List<Value<?>> subvalues = createValues(n, (Complex) type,
			 * re); Value<?>[] theValues = new Value<?>[ subvalues.size() ];
			 * subvalues.toArray( theValues ); try { re.set(theValues); } catch
			 * (Invalid e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
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
