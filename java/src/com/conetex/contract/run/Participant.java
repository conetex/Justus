package com.conetex.contract.run;

public class Participant {

	private final String nick;
	
	private final String publicKey;
	
	public Participant(String theNick, String thePublicKey){
		this.nick = theNick;
		this.publicKey = thePublicKey;
	}

	public String getNick() {
		return nick;
	}

	public String getPublicKey() {
		return publicKey;
	}
	
}
