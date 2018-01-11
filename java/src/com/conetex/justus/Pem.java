package com.conetex.justus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Pem{

	public static void main(String[] args) throws IOException {
		
		File f = new File("KeyPair/publicKey");	
		FileInputStream fis = new FileInputStream(f);
		Long size = f.length();
		
		byte[] k = new byte[size.intValue()];
        fis.read(k);
		fis.close();
		
		byte[] bytes = Base64.getDecoder().decode( k );
		
		try{
			PublicKey publicKey = 
				    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
		}
		catch(InvalidKeySpecException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NoSuchAlgorithmException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
