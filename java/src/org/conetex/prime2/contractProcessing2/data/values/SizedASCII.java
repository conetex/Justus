package org.conetex.prime2.contractProcessing2.data.values;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;

	public abstract class SizedASCII implements Value<String>{
		
		protected String actual;
				
		protected boolean check(String aValue, String allowedChars) throws Invalid{
			if(aValue == null){
				return true;
			}
			
			if(aValue.length() > this.getMaxSize()){
				throw new Invalid("Input is longer than " + this.getMaxSize() + ": '" + aValue + "'");
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
								throw new Invalid("found control char at begin of Input! Don't use control chars!");						
							}
							throw new Invalid("Please do not use control chars! found control char after '" + strBevorCtrlChar + "'");
						}
					}
					throw new Invalid("found control char in Input! Don't use control chars!");
				}
				
				throw new Invalid("Please do not use '" + noASCII + "' in '" + aValue + "'!");
			}
			
			// when code above is correct we should never go here ... 
			throw new Invalid("regex '[" + allowedChars + "]{1,}' is not matched by '" + aValue + "'! Please report! This Issue should be debugged!");
		
		}
		
		@Override
		public void set(String aValue) throws Invalid{
			// ascii without control characters:
			String allowedChars = " !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
			if( this.check(aValue, allowedChars) ){
				this.actual = aValue;				
			}
		}		
		
		@Override
		public void setConverted(String newValue) throws Invalid  {
			this.set(newValue);
		}		
		
		@Override
		public final String get(){
			return this.actual;
		}	
		
		@Override
		public String copy() {
			return this.get();
		}
		
		public abstract int getMaxSize();
		
	}
