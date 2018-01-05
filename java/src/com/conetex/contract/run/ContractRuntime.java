package com.conetex.contract.run;

import java.util.Map;
import java.util.TreeMap;

import com.conetex.contract.run.exeption.NoInformantSubcribed;
import com.conetex.contract.util.Pair;

public class ContractRuntime {

	public static interface Informant<T>{
		
		public T getStringAnswer(String question);
		
		public T getStringAnswer(String question, Pair<T, String>[] allowedAnswers);
		
	}

	public static class AgentHandler<T> {
		
		private Informant<T> dftInformant = null;
		
		private Map<String, Informant<T>> registry = new TreeMap<>();
	
		public void subscribe(String theStub, Informant<T> theInformant){
			this.registry.put(theStub, theInformant);
		}
		
		public void subscribe(Informant<T> theInformant){
			dftInformant = theInformant;
		}
		
		public T getStringAnswer(String theStub, String question) throws NoInformantSubcribed {
			Informant<T> i = this.registry.get(theStub);
			if(i != null){
				return i.getStringAnswer(question);
			}
			else{
				return this.getStringAnswer(question);
			}
		}

		public T getStringAnswer(String question) throws NoInformantSubcribed {
			if(this.dftInformant != null){
				return this.dftInformant.getStringAnswer(question);
			}
			throw new NoInformantSubcribed(question);
		}		
		
	}
	
	
	public static AgentHandler<String> stringAgency = new AgentHandler<>();


	



	
	public static Participant whoAmI() {
		return new Participant("Matthias", "asdfopikepoavxoiowertxyfgdlnsaflgaqzwuesdmviignyhsmqic");
		//return new Participant("Bjoern", "soidnaaoiyxcvkjlksajdfoaisjoyixcvjoyixcjvoasjfoaskdfkz");
	}
	
	
	
	
}
