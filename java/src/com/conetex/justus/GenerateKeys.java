package com.conetex.justus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateKeys{
	
	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		this.keyGen.initialize(keylength, random);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) {
		GenerateKeys gk;
		try {
			gk = new GenerateKeys(1024);
			gk.createKeys();
			
			System.out.println( gk.getPublicKey().getFormat() );
			byte[] publicKeyBase = Base64.getEncoder().encode( gk.getPublicKey().getEncoded() );
			gk.writeToFile("KeyPair/publicKey", publicKeyBase);
			
			System.out.println( gk.getPrivateKey().getFormat() );
			byte[] privateKeyBase = Base64.getEncoder().encode( gk.getPrivateKey().getEncoded() );
			gk.writeToFile("KeyPair/privateKey", privateKeyBase);
			
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
