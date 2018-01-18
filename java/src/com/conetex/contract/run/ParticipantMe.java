package com.conetex.contract.run;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.conetex.contract.build.Constants;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class ParticipantMe extends Participant{

	private final String privateKeyBase64;
	
	private final PrivateKey privateKey;
	
	public ParticipantMe(String theNick, String theMail, String thePublicKey, String thePrivateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		super(theNick, theMail, thePublicKey);
		this.privateKeyBase64 = thePrivateKey;
		byte[] bytes = Base64.getDecoder().decode( thePrivateKey.getBytes() );
		//this.privateKey = KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(bytes));
		
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(bytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		this.privateKey = kf.generatePrivate(ks);
		
	}

	public void sign(Structure s) throws ValueCastException, Inconvertible, Invalid{
		Participant me = ContractRuntime.whoAmI();
		TypeComplex c = s.getComplex();
		for(Attribute<?> a : c.getSubAttributes()){
			if(Symbols.TYPE_SIGNATURE.equals(a.getType().getName())) {//Todo besser sowas wie is subtypeOf machen...
				System.err.println( a.getLabel().get() + " " + a.getType().getName() ); 
				Structure sig = s.getStructure( a.getLabel().get() );
				if(sig == null){					
					System.out.println("can not sign");
					return;
					/*
					sig = ( a.createValue(s, null) ).asStructure();
					if(sig == null){
						System.out.println("can not sign");
						return;
					}
					Attribute<?> as = sig.getComplex().getSubAttribute(Symbols.TYPE_SIGNATURE_ATT_PARTICIPANT);
					Value<?> partVal = as.createValue(sig, null);
					partVal.asStructure().fillMissingValues();
					Value<?> partValPubKey = partVal.asStructure().getValue("publicKey");
					partValPubKey.setConverted(super.publicKey);
					Value<?> partValNick = partVal.asStructure().getValue("nick");
					partValNick.setConverted(super.nick);
					sig.set(Symbols.TYPE_SIGNATURE_ATT_PARTICIPANT, partVal );
					s.set(a.getLabel().get() , sig);
					*/
				}
				if (me.isEqual(sig.getStructure(Symbols.TYPE_SIGNATURE_ATT_PARTICIPANT))) {
					System.err.println( "--> match " + a.getLabel().get() + " " + a.getType().getName() ); 
					
					Signature rsa;
					try {
						rsa = Signature.getInstance(Constants.SEC_HASH_4_SIG);// TODO welcher Provider? , "BC" ?
						rsa.initSign(this.privateKey);
						ContractRuntime.update(rsa, s);
					    byte[] signature;
						signature = rsa.sign();
						Value<?> partSig = sig.getValue("signing");
						partSig.setConverted( Base64.getEncoder().encodeToString(signature) );
						
					}
					catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (SignatureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	

	
}
