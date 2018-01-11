package com.conetex.contract.run;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;

public class Participant {

	final String	nick;

	final String	publicKeyBase64;
	
	final PublicKey publicKey;

	public Participant(String theNick, String thePublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.nick = theNick;
		this.publicKeyBase64 = thePublicKey;
		this.publicKey = getPublicKey(thePublicKey);
	}

	public String getNick() {
		return this.nick;
	}

	public String getPublicKey() {
		return this.publicKeyBase64;
	}

	public boolean isEqual(Structure s) {
		if(s != null){
			Value<?> aNickVal = s.getValue(Symbols.TYPE_PARTICIPANT_ATT_NICK);
			if(aNickVal != null && aNickVal.get() != null){
				String aNick = aNickVal.get().toString();
				if (this.nick.equals(aNick)) {
					Value<?> aPubKeyVal = s.getValue(Symbols.TYPE_PARTICIPANT_ATT_PUBKEY);
					if(aPubKeyVal != null && aPubKeyVal.get() != null){
						String aPubKey = aPubKeyVal.get().toString();
						if (this.publicKeyBase64.equals(aPubKey)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static PublicKey getPublicKey(String thePublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] bytes = ContractRuntime.fromBase64( thePublicKey );
		X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(ks);
	}
	

	
}
