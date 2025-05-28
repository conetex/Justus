package com.conetex.contract.build;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexFunction;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.ContractRuntime;
import com.conetex.contract.run.Main;
import com.conetex.contract.run.Participant;
import com.conetex.contract.run.Writer;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;
import com.conetex.contract.util.Pair;

public class Build {

	private static Map<String, Function<?>> createMainFunctions(List<Pair<CodeNode, TypeComplex>> complexTypes)
			throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
		Map<String, Function<?>> mainFunctions = new TreeMap<>();
		for (Pair<CodeNode, TypeComplex> t : complexTypes) {
			if (t == null || t.b == null) {
				System.err.println("was geht denn hier?");
				continue;
			}

			TypeComplex superType = t.b.getSuperType();
			if (superType != null && Symbols.TYPE_DUTY.equals(superType.getName())) {//Todo besser sowas wie is subtypeOf machen...
				Function<?> f = BuildFunctions.build(t.a, t.b);
				mainFunctions.put(t.b.getName(), f);
				// f.getFrom(rootStructure);
			}
		}
		return mainFunctions;
	}

	private static List<Structure> getAllDuties(Structure rootStructure) {
		List<Structure> duties = new LinkedList<>();
		for (Value<?> va : rootStructure.getValues()) {
			if (va == null) {
				System.err.println("warum ist das value null ?");
				continue;
			}
			Structure s = va.asStructure();
			if (s != null) {
				TypeComplex superType = s.getComplex().getSuperType();
				if (superType != null && Symbols.TYPE_DUTY.equals(superType.getName())) {//Todo besser sowas wie is subtypeOf machen...
					duties.add(s);
				}
			}
		}
		return duties;
	}

	private static List<Structure> getMyDuties(List<Structure> allDuties) throws ValueCastException {
		Participant me = ContractRuntime.whoAmI();
		List<Structure> myDuties = new LinkedList<>();
		for (Structure s : allDuties) {
			if (me.isEqual(s.getStructure(Symbols.TYPE_DUTY_ATT_PARTICIPANT))) {
				myDuties.add(s);
			}
		}
		return myDuties;
	}

	public static Main createMain(CodeNode syntaxTreeRoot) throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException, ValueCastException {
		TypePrimitive.init();
		CodeNode.init(syntaxTreeRoot);
		// List<TypeComplex> complexTyps = BuildTypes.createComplexTypes(code);
		CodeNode complexRoot = CodeNode.getComplexRoot();
		
		TypeComplex.clearInstances();
		TypeComplexFunction.clearInstances();
		List<Pair<CodeNode, TypeComplex>> complexTypes = BuildTypes.createComplexTypes(complexRoot);

		System.out.println("Builder " + syntaxTreeRoot.getCommand());
		if (complexTypes != null) {

			Function.clearInstances();
			Map<String, Function<?>> mainFunctions = createMainFunctions(complexTypes);

			CodeNode valueRoot = CodeNode.getValueRoot();
			TypeComplex complexTypeRoot = TypeComplex.getInstance(valueRoot.getParameter(Symbols.paramName()));
			Structure rootStructure = complexTypeRoot.createValue(null);

			if (rootStructure != null) {
				BuildValues.createValues(valueRoot, complexTypeRoot, rootStructure);
				rootStructure.fillMissingValues();
				TypeComplexFunction.fillMissingPrototypeValues();
				
				// TODO 1: wozu dient diese mainFunction genau?
				Function<?> mainFunction = BuildFunctions.build(complexRoot, complexTypeRoot);
				
				//ContractRuntime.validateSignatures(rootStructure);
				
				List<Structure> allDuties = getAllDuties(rootStructure);
				List<Structure> myDuties = getMyDuties(allDuties);

				if (mainFunction != null) {
					return new Main() {
						@Override
						public void run(Writer w)
								throws AbstractRuntimeException, UnknownCommandParameter, UnknownCommand, NullLabelException, EmptyLabelException {
							// mainFunction.getFromRoot(rootStructure);
							for (Structure d : myDuties) {
								Function<?> f = mainFunctions.get(d.getComplex().getName());
								if (f == null) {
									System.err.println("can not find my main function");
									return;
								}
								f.getFrom(d);
							}
							ContractRuntime.whoAmI().sign(rootStructure);

							if (w != null) {
								CodeNode cnTyps = complexTypeRoot.createCodeNode(null);
								w.write(cnTyps);

								// CodeNode cnFuns =
								// complexTypeRoot.createCodeNode(null);
								// w.write(cnTyps);
								/*
								 * for(TypeComplex tc :
								 * TypeComplex.getInstances()){ if(
								 * tc.getCommand().equals(
								 * TypeComplex.staticGetCommand() ) ){ CodeNode
								 * cnTyps = tc.createCodeNode();
								 * w.write(cnTyps); } // else // functions are
								 * done by tc.createCodeNode() internal }
								 */
								/*
								 * for(TypeComplexOfFunction tcf :
								 * TypeComplexOfFunction.getInstances()){
								 * CodeNode cnTyps = tcf.persist();
								 * w.write(cnTyps); }
								 */
								Attribute<?> r = complexTypeRoot.createComplexAttribute(complexTypeRoot.getName());
								CodeNode cnVal = rootStructure.createCodeNode(null, r);
								w.write(cnVal);
								/*
								 * List<CodeNode> cnVals =
								 * rootStructure.createCodeNodes(); for(CodeNode
								 * cnVal : cnVals){ w.write(cnVal); }
								 */
							}
							else {
								// TODO exception ...
							}
						}

						@Override
						public TypeComplex getRootTyp() {
							return complexTypeRoot;
						}

						@Override
						public CodeNode getRootCodeNode() {
							return syntaxTreeRoot;
						}

						@Override
						public Structure getRootStructure() {
							return rootStructure;
						}
					};
				}
			}
		}
		return null;
	}

}