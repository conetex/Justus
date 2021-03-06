package com.conetex.justus.study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.conetex.contract.build.Build;
import com.conetex.contract.build.CodeModel;
import com.conetex.contract.build.CodeModel.EggAbstr;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.ContractRuntime;
import com.conetex.contract.run.Main;
import com.conetex.contract.run.Writer;
import com.conetex.contract.run.ContractRuntime.Informant;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.util.Pair;

public class ReadXML {

	public static class XmlWriter extends Writer {

		Document	odoc;
		Element		root;

		XmlWriter(Document theDoc, Element theRoot) {
			this.odoc = theDoc;
			this.root = theRoot;
		}

		public Element writeNode(CodeNode n) throws UnknownCommandParameter, UnknownCommand {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			if (n == null) {
				System.out.println("was geht");
				return null;
			}
			Element e = null;
			try {
				e = this.odoc.createElement(n.getCommand());
			} catch (DOMException de) {
				System.out.println(de);
				return null;
			}

			String[] parameters = n.getParameters();
			String[] names = n.getParameterNames();
			if (names != null) {
				int i = 0;
				for (String p : names) {
					e.setAttribute(p, parameters[i++]);
					// Attr a = odoc.createAttribute(p);
					// e.appendChild(a);
				}
			}

			for (CodeNode c : n.getChildNodes()) {
				Element ec = this.writeNode(c);
				e.appendChild(ec);
			}

			System.out.println("write " + n.getCommand());
			return e;
		}

		@Override
		public void write(CodeNode n) throws UnknownCommandParameter, UnknownCommand {
			Element e = this.writeNode(n);
			this.root.appendChild(e);
		}

	}

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, AbstractInterpreterException,
			AbstractRuntimeException, AbstractTypException, InvalidKeySpecException, NoSuchAlgorithmException {
		String inFile = "changeProcessV2__out_out_out";
		String fileExtension = ".xml";

		Main main = in(new File(inFile + fileExtension));
		ContractRuntime.stringAgency.subscribe(new Informant<String>() {
			@Override
			public String getStringAnswer(String question) {
				return "default answer A";
			}

			@Override
			public String getStringAnswer(String question, Pair<String, String>[] allowedAnswers) {
				return "default answer B";
			}
		});
		Document doc = run(main);
		out(doc, new File(inFile + "_out" + fileExtension));
	}

	public static Main in(File inFile) throws ParserConfigurationException, SAXException, IOException,
			AbstractInterpreterException, AbstractRuntimeException, AbstractTypException {

		CodeModel.build(); // TODO das sollte woanders gemacht werden, denn hier ist alles xml-driven...

		Main main = null;
		try (FileInputStream is = new FileInputStream(inFile)) {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(is);

			// List<Complex> complexTyps_ = null;
			// List<Value<?>> values = null;
			// List<Accessible<?>> functions = null;

			NodeList children = document.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node r = children.item(i);
				short typOfNode = children.item(i).getNodeType();
				if (typOfNode == Node.ELEMENT_NODE) {
					if (main == null) {
						CodeNode r2 = parse2SyntaxTree(r);

						// createChildren("", r);

						main = Build.createMain(r2);
					} else {
						System.err.println("more than one root element! can not proceed!");
					}
				}
			}

			is.close();
		}

		return main;

	}

	public static Document run(Main main) throws ParserConfigurationException, UnknownCommandParameter, UnknownCommand,
			NullLabelException, EmptyLabelException, AbstractRuntimeException {
		if (main != null) {
			DocumentBuilderFactory odocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder odocumentBuilder = odocumentBuilderFactory.newDocumentBuilder();
			Document doc = odocumentBuilder.newDocument();
			Element root = doc.createElement(Symbols.comContract());
			// root.setAttribute(Symbols.paramName(),
			// main.getRootTyp().getName());
			doc.appendChild(root);
			Writer w = new XmlWriter(doc, root);
			main.run(w);
			return doc;
		}
		return null;
	}

	public static void out(Document doc, File outFile) throws ParserConfigurationException, SAXException, IOException,
			AbstractInterpreterException, AbstractRuntimeException, AbstractTypException {
		if (doc == null) {
			return;
		}
		Document odoc = doc;
		try (FileOutputStream os = new FileOutputStream(outFile)) {
			StreamResult res = new StreamResult(os);
			Transformer t;
			try {
				t = TransformerFactory.newInstance().newTransformer();
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				t.transform(new DOMSource(odoc), res);
			} catch (TransformerFactoryConfigurationError | TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static CodeNode parse2SyntaxTree(Node n) {
		return createSyntaxNode("", n);
	}
	
	private static CodeNode createSyntaxNode(String parentName, Node n) {

		short typOfNode = n.getNodeType();
		if (typOfNode != Node.ELEMENT_NODE) {
			return null;
		}

		String commandStr = n.getNodeName();
		StringBuilder errors = new StringBuilder(commandStr + " missing params: ");
		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(commandStr);

		String thisName = "";

		if (commands == null) {
			String theValue = ReadXMLtools.getNodeValue(n);
			if (theValue == null) {
				return new CodeNode(parentName, Symbols.comVirtualCompValue(), new String[] { commandStr },
						createChildren(thisName, n));
			} else {
				return new CodeNode(parentName, Symbols.comvirtualPrimValue(), new String[] { commandStr, theValue },
						createChildren(thisName, n));
			}
		} else {
			// TODO Sortierung ist nicht getestet...
			commands.sort((o1, o2) -> {
				if (o1.getParameterCount() < o2.getParameterCount()) {
					return 1;
				} else {
					if (o1.getParameterCount() == o2.getParameterCount()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			NamedNodeMap attributes = n.getAttributes();
			String[] theParams = null;
			List<String> attributeList = new LinkedList<>();
			outerLoop: for (EggAbstr<?> command : commands) {
				String[] paramNames = command.getParameterNames();
				if (paramNames != null) {
					for (String paramName : paramNames) {
						Node a = attributes.getNamedItem(paramName);
						if (a == null) {
							if (paramName == Symbols.comValue()) {
								String value = ReadXMLtools.getNodeContent(n);
								if (value == null) {
									errors.append(paramName).append(", ");
									attributeList.clear();
									continue outerLoop;
								} else {
									attributeList.add(value);
								}
							} else {
								errors.append(paramName).append(", ");
								attributeList.clear();
								continue outerLoop;
							}
						} else {
							if (paramName.equals(Symbols.paramName())) {
								if (parentName == null || parentName.equals("")) {
									thisName = a.getNodeValue();
									attributeList.add(thisName);
								} else {
									// nur bei complex / function
									thisName = parentName + Symbols.NAME_SEPERATOR
											+ Symbols.getSimpleName(a.getNodeValue());
									if (thisName.equals("root.person.tuWas")) {
										System.err.println("upps");
									}

									if (commandStr == Symbols.comFunction()) {
										attributeList.add(thisName);
									} else {
										attributeList.add(a.getNodeValue());
									}
								}
							} else {
								attributeList.add(a.getNodeValue());
							}
						}
					}
					theParams = new String[attributeList.size()];
					attributeList.toArray(theParams);
					return new CodeNode(parentName, commandStr, theParams, createChildren(thisName, n));
				} else {
					return new CodeNode(parentName, commandStr, new String[0], createChildren(thisName, n));
				}
			}
		}
		System.err.println(errors);
		return null;
	}

	private static List<CodeNode> createChildren(String thisName, Node n) {
		List<CodeNode> children = new LinkedList<>();
		NodeList xmlChildren = n.getChildNodes();
		for (int i = 0; i < xmlChildren.getLength(); i++) {
			Node c = xmlChildren.item(i);
			CodeNode child = createSyntaxNode(thisName, c);
			if (child != null) {
				children.add(child);
			}
		}
		return children;
	}

}
