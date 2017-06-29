package org.conetex.prime2.contractProcessing2.data.values.exception;

public class ValueTransformException extends Exception {

		private static final long serialVersionUID = 1L;
		public ValueTransformException(String msg, Exception cause) {
			super(msg, cause);
		}
		public ValueTransformException(String msg) {
			super(msg);
		}		
	}
