package com.conetex.contract.run;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.conetex.contract.build.Constants;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exeption.NoInformantSubcribed;
import com.conetex.contract.util.Pair;

public class ContractRuntime {

	public static interface Informant<T> {

		public T getStringAnswer(String question);

		public T getStringAnswer(String question, Pair<T, String>[] allowedAnswers);

	}

	public static class AgentHandler<T> {

		private Informant<T>				dftInformant	= null;

		private Map<String, Informant<T>>	registry		= new TreeMap<>();

		public void subscribe(String theStub, Informant<T> theInformant) {
			this.registry.put(theStub, theInformant);
		}

		public void subscribe(Informant<T> theInformant) {
			dftInformant = theInformant;
		}

		public T getStringAnswer(String theStub, String question) throws NoInformantSubcribed {
			Informant<T> i = this.registry.get(theStub);
			if (i != null) {
				return i.getStringAnswer(question);
			}
			else {
				return this.getStringAnswer(question);
			}
		}

		public T getStringAnswer(String question) throws NoInformantSubcribed {
			if (this.dftInformant != null) {
				return this.dftInformant.getStringAnswer(question);
			}
			throw new NoInformantSubcribed(question);
		}

	}

	public static AgentHandler<String> stringAgency = new AgentHandler<>();

	private static ParticipantMe me = null;
	
	public static void initMe(ParticipantMe theParticipantMe) throws AbstractRuntimeException {
		if(me == null){
			me = theParticipantMe;
		}
		else{
			throw new AbstractRuntimeException("ParticipantMe was initialized bevor"){
				private static final long serialVersionUID = 1L;
			};
		}
	}
	
	public static ParticipantMe whoAmI() {

		try {
			/*		
			return new ParticipantMe("Matthias",
					"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCG4fKRuj6SX6lgaKtQ9pUV/2yVwFB4mq0l7DaeZkzlF/tH99vvMOD9Q0SVa0DuClcNQS8DXtKy3MU+0ax4UG8Yh8YL3C+zv7Cb3kGWsEP/dDWka+6mHhhf7ofZ6a1HZdUvj8qW4H3SDP83YP0j3QnjqCA+t4fjs8qbDu3x4r1ZkQIDAQAB",
					"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIbh8pG6PpJfqWBoq1D2lRX/bJXAUHiarSXsNp5mTOUX+0f32+8w4P1DRJVrQO4KVw1BLwNe0rLcxT7RrHhQbxiHxgvcL7O/sJveQZawQ/90NaRr7qYeGF/uh9nprUdl1S+PypbgfdIM/zdg/SPdCeOoID63h+OzypsO7fHivVmRAgMBAAECgYBDFyQmpDL3b6m6EJYWIXCqjnAeVJgyRQ2W7VWFmHDrCvCsLXcyFGf00X7Nq5mSTYZbS27tCD9ZRELAKl7VQXzgQpy6zEGZ5yOQ1ii97mY692hLC9HyrtQSDjbjeTPMs6sstqs4W1l4vlPdx0QhRuZ0nWanN4ib9B70YvcwxRgpeQJBAOuz5Y+vJOYD9z+nJBL6VsP4kxG3f/5zW7SktZyMcD8g/u52yT3HtEuNZiUMAQ8ZkO/Z7klPte3N8Ue2GjPETrMCQQCSf3Sml2jhxHRK+Db3yYfJQ8leYrL1Iw3aGgnVy85oJ1zUV7C/NE0ozd9IVcdidh1huNNHoUZhNP+VVnDZ1BirAkEAh8C7LQBhLiGGnCC2BEAvDPv0KLYZgAINBYQAHcQ9Of7VFZ9Q1MZar1EnTZsWWQ4OjjZkqdDBJdABcZ4OhVZBRQJAPDfOAGh+gUcasnJjTel2OmH11Slm/GLjX8KSRmKXPrLncpV1HrNmAB7X6EjyQ2Pf4fpyRqBhaLW7VWyJyNBpLwJBAL1wqLMRY8tLzAcgnPVTqX0nAwKNtjRIUgq9KijWfxCWtoCS1qKDEBYCSMAkf8Z8jqhlHFnhzmnGUJ4axtUX5fE=");
*/

			return new ParticipantMe("Bjoern" ,
			 "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTj/pKWLFZvugkf0TQps6wxju4K7oI/iwW5LxyBeAE74JgIwvocBcDnQAWUzhSaktEO5fWgEtH9wr3GvbMhNOqDX3RAX2u+b9nzL+ltqCRBojJqywwVj4HiSku9t7Sc6MkisUdE5jQvSzneml3dxUEXWjIEcwb29NpCpZpQ1Pa1QIDAQAB",
			 "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJOP+kpYsVm+6CR/RNCmzrDGO7grugj+LBbkvHIF4ATvgmAjC+hwFwOdABZTOFJqS0Q7l9aAS0f3Cvca9syE06oNfdEBfa75v2fMv6W2oJEGiMmrLDBWPgeJKS723tJzoySKxR0TmNC9LOd6aXd3FQRdaMgRzBvb02kKlmlDU9rVAgMBAAECgYBRrbxpMrbYames7BV3OR3nk5Ky2uFa4PYepfX5V28szDZqaHvK6WCTy2+k6+OdGuEWn7XoMZN5/jC/ntlFU7Ck8/pNvGxJSFdSneLJsYUOcTvqd9pGpST6L/3vGpmn0x73yu+YwD9eelcn9TR49V9zo60KsxSbit7eHLQUPxLigQJBANgWZ2Jgh6h1e1kuk8V+QRvEOX6p/ict5AOUvDJ/Y6jN0RYQov8s5Vj1E9apze5AY5ryGpliEvF/B0vO7B4BNTkCQQCu0WWQ0PQ/pFhqRQ4SkJlvVpbArBjW0yvB7jhGW4Ur8j06G5/SXfvdVUCY5/Tj62adtLTAmSD/XudocUYUMc59AkAcLX+5wmCuRPOaw4odfMM8va/Jpp0vuro7BVAtPZNfVcdoGy9GGzKlEQBPQ8FIyjiy4dM6ISHbP/dRmWAM4ZdxAkB121fk3Op96XduFaYaIABBTpgNxzhrxCww70BjNpo+eW6LuUnzUkdV/X6yV1cDPZWN5uEhI1tarwnFoPBE9xslAkAHKxay7yNdsW/YMfMonT2SChpmqtEfGO84pG8sVOoQrD0z1HsQjEoMoEeOclIaVc3TaQb8JZvnJJHS70H6Ewc1");

		}
		catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static String validateSignatures(Structure rootStructure) {
		if(rootStructure == null) {
			return "nothing to check";
		}
		List<Structure> sigs = getAllSignatures(rootStructure);

		boolean checked = false;
		
		for (Structure s : sigs) {

			Value<?> ss = s.getValue(Symbols.TYPE_SIGNATURE_ATT_SIGNING);
			if (ss == null || ss.get() == null) {
				continue;
			}
			if (ss.get().toString() == null || ss.get().toString().length() == 0) {
				continue;
			}			
			byte[] sss = fromBase64(ss.get().toString());

			Value<?> p = s.getValue(Symbols.TYPE_SIGNATURE_ATT_PARTICIPANT);
			if (p == null) {
				continue;
			}
			Structure ps = p.asStructure();
			if (ps == null) {
				continue;
			}
			Value<?> k = ps.getValue(Symbols.TYPE_PARTICIPANT_ATT_PUBKEY);
			if (k == null || k.get() == null) {
				continue;
			}
			String thePublicKey = k.get().toString();
			if (thePublicKey == null || thePublicKey.length() == 0) {
				continue;
			}
			try {
				PublicKey publicKey = Participant.getPublicKey(thePublicKey);
				Signature signature = Signature.getInstance(Constants.SEC_HASH_4_SIG); // TODO
																						// welcher
																						// provider?
																						// "BC"?
				signature.initVerify(publicKey);
				update(signature, rootStructure);
				boolean result = signature.verify(sss);
				checked = true;
				System.err.println(" valid signature --> " + result);
				if(! result) {
					return "signing invalid (document was changed after last sign off)";
				}
			}
			catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(checked) {
			return "everything ok!";
		}
		else {
			return "nothing signed";			
		}
	}

	public static void update(Signature rsa, Structure s) throws SignatureException {
		TypeComplex t = s.getComplex();
		for (Attribute<?> a : t.getSubAttributes()) {

			// Name
			String as = a.getLabel().get();
			rsa.update(as.getBytes());

			// Typ
			String ts = a.getType().getName();
			rsa.update(ts.getBytes());

			// Value
			Value<?> v = s.getValue(as);
			if (v != null) {
				Structure cs = v.asStructure();
				if (cs == null) {

					// Primitiv
					Object o = v.get();
					if (o != null) {
						String os = o.toString();
						if (os.length() > 0) {
							rsa.update(os.getBytes());
						}
					}

				}
				else {

					// Complex
					TypeComplex superType = cs.getComplex();// .getSuperType();
					if (!Symbols.TYPE_SIGNATURE.equals(superType.getName())) {// Todo
																				// besser
																				// sowas
																				// wie
																				// is
																				// subtypeOf
																				// machen...
						update(rsa, cs);
					}

				}
			}

		}
	}

	private static List<Structure> getAllSignatures(Structure rootStructure) {
		List<Structure> duties = new LinkedList<>();
		for (Value<?> va : rootStructure.getValues()) {
			if (va == null) {
				System.err.println("warum ist das value null ?");
				continue;
			}
			Structure s = va.asStructure();
			if (s != null) {
				TypeComplex superType = s.getComplex();// .getSuperType();
				if (superType != null && Symbols.TYPE_SIGNATURE.equals(superType.getName())) {// Todo
																								// besser
																								// sowas
																								// wie
																								// is
																								// subtypeOf
																								// machen...
					duties.add(s);
				}
			}
		}
		return duties;
	}

	public static String toBase64(byte[] s) {
		return Base64.getEncoder().encodeToString(s);// TODO alle sollten dies
														// hier nutzen ...
	}

	public static byte[] fromBase64(String s) {
		return Base64.getDecoder().decode(s.getBytes());
	}

}
