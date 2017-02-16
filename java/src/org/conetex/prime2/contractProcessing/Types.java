package org.conetex.prime2.contractProcessing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conetex.prime2.contractProcessing.State;
import org.conetex.prime2.contractProcessing.State.Value;
import org.conetex.prime2.contractProcessing.State.ValueException;

public class Types {
	
	public static class Complex2 {

		private State value;
		
		public void set(State aValue){
			this.value = aValue;			
		}
		
		public final State get(){
			return this.value;
		}
		
	}
	
	public static class Complex implements Value<State>{

		private State value;
		
		public Complex(State aValue){
			this.value = aValue;			
		}
		
		@Override
		public void set(State aValue){
			this.value = aValue;			
		}
		
		@Override
		public final State get(){
			return this.value;
		}
		
	}
	
	public static class Int implements Value<Integer>{

		private Integer value;
		
		@Override
		public void set(Integer aValue){
			this.value = aValue;			
		}
		
		@Override
		public final Integer get(){
			return this.value;
		}
		
	}

	public static class Lng implements Value<Long>{

		private Long value;
		
		@Override
		public void set(Long aValue){
			this.value = aValue;			
		}
		
		@Override
		public final Long get(){
			return this.value;
		}
		
	}

	public static class Bool implements Value<Boolean>{
		
		private Boolean value;
		
		@Override
		public void set(Boolean aValue){
			this.value = aValue;
		}
		
		@Override
		public final Boolean get(){
			return this.value;
		}		
		
	}
	
	public static abstract class SizedASCII implements Value<String>{
		
		private String value;
				
		protected boolean check(String aValue, String allowedChars) throws ValueException{
			if(aValue == null){
				return true;
			}
			
			if(aValue.length() > this.getMaxSize()){
				throw new ValueException("Input is longer than " + this.getMaxSize() + ": '" + aValue + "'");
			}

			// HINT: Take care adding allowed control chars. You have to adjust throwing Exceptions.
			//       The throwing Exceptions searches for control chars, to provide detailed Information to the user. 
			// ascii:
			//String chars = "\\p{ASCII}";
			
			// ascii without control characters but with \t (Tabulator):
			//String chars = "\\t !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"

			// ascii with control characters:
			//String chars = "\\s !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~";
			
			// ascii without control characters:
			//String allowedChars = " !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
			
			if( aValue.matches("[" + allowedChars + "]{1,}") ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
				return true;
			}
			
			Pattern noASCIIpattern = Pattern.compile("[^" + allowedChars + "]{1}");
			Matcher m = noASCIIpattern.matcher(aValue);
			String noASCII = "";
			if (m.find()) {
				noASCII = m.group(0);
				
				Pattern ctrlCharPattern = Pattern.compile("[\\s]{1}"); // check for control chars (\s)
				Matcher ctrlCharMatcher = ctrlCharPattern.matcher(noASCII);					
				if (ctrlCharMatcher.find()) {
					// noASCII is a control char
					ctrlCharPattern = Pattern.compile("([[^\\s][ ]]{0,})[\\s]{1}"); // search for control chars (\s), except space (blank)
					ctrlCharMatcher = ctrlCharPattern.matcher(aValue);					
					if (ctrlCharMatcher.find()) {
						// Locate the control char
						int groupCount = ctrlCharMatcher.groupCount();
						if(groupCount > 0){
							String strBevorCtrlChar = ctrlCharMatcher.group(1);
							if(strBevorCtrlChar == null || strBevorCtrlChar.length() == 0){								
								throw new ValueException("found control char at begin of Input! Don't use control chars!");						
							}
							throw new ValueException("Please do not use control chars! found control char after '" + strBevorCtrlChar + "'");
						}
					}
					throw new ValueException("found control char in Input! Don't use control chars!");
				}
				
				throw new ValueException("Please do not use '" + noASCII + "' in '" + aValue + "'!");
			}
			
			// when code above is correct we should never go here ... 
			throw new ValueException("regex '[" + allowedChars + "]{1,}' is not matched by '" + aValue + "'! Please report! This Issue should be debugged!");
		
		}
		
		@Override
		public void set(String aValue) throws ValueException{
			// ascii without control characters:
			String allowedChars = " !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
			if( this.check(aValue, allowedChars) ){
				this.value = aValue;				
			}
		}		
		
		@Override
		public final String get(){
			return this.value;
		}	
		
		public abstract int getMaxSize();
		
	}
	
	public static class ASCII8 extends SizedASCII{
		public int getMaxSize(){ return 8; }
	}	
	
	public static class ASCII12 extends SizedASCII{
		public int getMaxSize(){ return 12; }			
	}
	
	public static class ASCII16 extends SizedASCII{
		public int getMaxSize(){ return 16; }			
	}	
	
	public static class ASCII32 extends SizedASCII{
		public int getMaxSize(){ return 32;	}			
	}	
	
	public static class ASCII64 extends SizedASCII{
		public int getMaxSize(){ return 64;	}			
	}	
	
	public static class ASCII128 extends SizedASCII{
		public int getMaxSize(){ return 128; }			
	}	
	
	public static class ASCII256 extends SizedASCII{
		public int getMaxSize(){ return 256; }			
	}	
			
	public static abstract class Base64 extends SizedASCII{
		@Override	
		public void set(String aValue) throws ValueException{
			String allowedChars = "A-Za-z0-9+/=";
			if( super.check(aValue, allowedChars) ){
				if(! ( aValue.matches("[A-Za-z0-9+/]{1,}[=]{0,}") )  ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
					super.value = aValue;
				}				
				else{
					throw new ValueException("no valid Base64! '=' is only allowed at the end!");
				}
			}
		}
	}	

	public static class Base64_256 extends Base64{
		// How to calculate the memory for Base64-encoded data? See https://de.wikipedia.org/wiki/Base64 
		// 4 * ( ceil (256 / 3) ) = 344
		public int getMaxSize(){ return 344; }
	}
	
	public static class Base64_128 extends Base64{
		// 4 * ( ceil (128 / 3) ) = 172
		public int getMaxSize(){ return 172; }
	}
	
	public static class Base64_64 extends Base64{
		// 4 * ( ceil (64 / 3) ) = 88
		public int getMaxSize(){ return 88; }
	}	
	
	
	
	public static abstract class MailAddress extends SizedASCII{
		
		// See http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address
		public abstract int getMaxSize(); // longest email-address is 254
		
		@Override
		public void set(String aValue) throws ValueException{
			if(aValue == null){
				super.value = null;
			}
			
			aValue = aValue.trim();
			if(aValue.length() > this.getMaxSize()){
				throw new ValueException("'" + aValue + "' is longer than " + this.getMaxSize());
			}
			
			if( aValue.matches("\\A[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z") ){
				super.value = aValue;
			}
			else{
				throw new ValueException("'" + aValue + "' is no valid mail-Address");
			}
		}		
	}
	
	public static class MailAddress64 extends MailAddress{
		public int getMaxSize(){ return 64; }
	}
	
	public static class MailAddress128 extends MailAddress{
		public int getMaxSize(){ return 128; }
	}	
	
	public static class MailAddress254 extends MailAddress{
		public int getMaxSize(){ return 254; } // longest email-address is 254
	}
	
}
