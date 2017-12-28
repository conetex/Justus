package com.conetex.contract.run;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.value.implementation.Structure;

public class Participant {

	private final String	nick;

	private final String	publicKey;

	public Participant(String theNick, String thePublicKey) {
		this.nick = theNick;
		this.publicKey = thePublicKey;
	}

	public String getNick() {
		return this.nick;
	}

	public String getPublicKey() {
		return this.publicKey;
	}

	public boolean isEqual(Structure s) {
		String aNick = s.getValue(Symbols.TYPE_PARTICIPANT_ATT_NICK).get().toString();
		if (this.nick.equals(aNick)) {
			String aPubKey = s.getValue(Symbols.TYPE_PARTICIPANT_ATT_PUBKEY).get().toString();
			if (this.publicKey.equals(aPubKey)) {
				return true;
			}
		}
		return false;
	}

}
