package com.conetex.contract.run;

public class ContractRuntime {

	public static Participant whoAmI() {
		return new Participant("Matthias", "asdfopikepoavxoiowertxyfgdlnsaflgaqzwuesdmviignyhsmqic");
		//return new Participant("Bjoern", "soidnaaoiyxcvkjlksajdfoaisjoyixcvjoyixcjvoasjfoaskdfkz");
	}
	
	public static String getStringAnswer(String question) {
		return "Hallo Bjoern! I want you to do something. Regards Matthias";
	}
	
}
