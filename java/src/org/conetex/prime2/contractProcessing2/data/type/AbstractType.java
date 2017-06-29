package org.conetex.prime2.contractProcessing2.data.type;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.ASCII8;
import org.conetex.prime2.contractProcessing2.data.values.Base64_256;
import org.conetex.prime2.contractProcessing2.data.values.Bool;
import org.conetex.prime2.contractProcessing2.data.values.Int;
import org.conetex.prime2.contractProcessing2.data.values.Label;
import org.conetex.prime2.contractProcessing2.data.values.Lng;
import org.conetex.prime2.contractProcessing2.data.values.MailAddress64;

	public abstract class AbstractType<T> {
		
		
		
		
		public abstract Identifier<T> createAttribute(ASCII8 theName) throws Identifier.NullLabelException, Identifier.EmptyLabelException;
		public abstract Class<? extends Value<T>> getClazz();
		public abstract Value<T> createValue();
		public abstract <U> Identifier<U> getIdentifier(String aName);
		public static <V> Identifier<V> createAttribute(ASCII8 theName, AbstractType<V> thisObj) throws Identifier.NullLabelException, Identifier.EmptyLabelException {
			if(theName == null || theName.get() == null){
				throw new Identifier.NullLabelException();
			}
			if(theName.get().length() < 1){
				throw new Identifier.EmptyLabelException();
			}
			return Identifier.<V>create(theName, thisObj);
		}		
	}
