package com.conetex.justus.study;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.runNew.Main;
import com.conetex.contract.runNew.Writer;

class ReadXML{

	public static class XmlWriter extends Writer{

		Document odoc;
		Element root;
		
		XmlWriter(Document theDoc, Element theRoot){
			this.odoc = theDoc;
			this.root = theRoot;
		}
		
		public Element writeNode(CodeNode n) throws UnknownCommandParameter, UnknownCommand {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			Element e = odoc.createElement(n.getCommand());
			
			String[] parameters = n.getParameters();
			int i = 0;
			for(String p : n.getParameterNames()){
				e.setAttribute(p, parameters[i++]);
				//Attr a = odoc.createAttribute(p);
				//e.appendChild(a);
			}
			
			for(CodeNode c : n.getChildNodes()){
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
			throws ParserConfigurationException, SAXException, IOException, AbstractInterpreterException, AbstractRuntimeException {

		CodeModel.build();// TODO das sollte woanders gemacht werden, denn hier ist alles xml-driven...

		String fileExtension = ".xml";
		String inFile = "input02";
		Main main = null;
		try(FileInputStream is = new FileInputStream(inFile + fileExtension)){

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(is);

			// List<Complex> complexTyps_ = null;
			// List<Value<?>> values = null;
			// List<Accessible<?>> functions = null;

			NodeList children = document.getChildNodes();
			for(int i = 0; i < children.getLength(); i++){
				Node r = children.item(i);
				short typOfNode = children.item(i).getNodeType();
				if(typOfNode == Node.ELEMENT_NODE){
					if(main == null){
						CodeNode r2 = createSyntaxNode(r);
						main = Build.create(r2);
					}
					else{
						System.err.println("more than one root element! can not proceed!");
					}
				}
			}

			is.close();
		}
		
		try(FileOutputStream os = new FileOutputStream(inFile + "_out" + fileExtension)){
			StreamResult res = new StreamResult(os);
			DocumentBuilderFactory odocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder odocumentBuilder = odocumentBuilderFactory.newDocumentBuilder();
			Document odoc = odocumentBuilder.newDocument();
			Element root = odoc.createElement("contractOut");
			odoc.appendChild(root);
			
			Writer w = new XmlWriter(odoc, root);
	
			if(main != null){
				main.run(w);
				Transformer t;
				try {
					t = TransformerFactory.newInstance().newTransformer();
					t.setOutputProperty(OutputKeys.INDENT, "yes");
					t.transform(new DOMSource(odoc), res);
				}
				catch (TransformerFactoryConfigurationError | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static CodeNode createSyntaxNode(Node n) {

		short typOfNode = n.getNodeType();
		if(typOfNode != Node.ELEMENT_NODE){
			return null;
		}

		List<CodeNode> children = new LinkedList<>();
		NodeList xmlChildren = n.getChildNodes();
		for(int i = 0; i < xmlChildren.getLength(); i++){
			Node c = xmlChildren.item(i);
			CodeNode child = createSyntaxNode(c);
			if(child != null){
				children.add(child);
			}
		}

		String commandStr = n.getNodeName();
		StringBuilder errors = new StringBuilder(commandStr + " missing params: ");
		NamedNodeMap attributes = n.getAttributes();
		List<EggAbstr<?>> commands = CodeModel.EggAbstrImp.getInstance(commandStr);
		if(commands == null){
			String theValue = ReadXMLtools.getNodeValue(n);
			if(theValue == null){
				return new CodeNode(Symbols.comVirtualCompValue(), new String[] { commandStr }, children);
			}
			else{
				return new CodeNode(Symbols.comvirtualPrimValue(), new String[] { commandStr, theValue }, children);
			}
		}

		// TODO Sortierung ist nicht getestet...
		commands.sort(
				(o1, o2) -> {
                    if(o1.getParameterCount() < o2.getParameterCount()){
                        return -1;
                    }
                    else{
                        if(o1.getParameterCount() == o2.getParameterCount()){
                            return 0;
                        }
                        else{
                            return 1;
                        }
                    }
                });
		List<String> attributeList = new LinkedList<>();
		outerLoop: for(EggAbstr<?> command : commands){
			String[] paramNames = command.getParameterNames();
			if(paramNames != null){
				for(String paramName : paramNames){
					Node a = attributes.getNamedItem(paramName);
					if(a == null){
						if(paramName == Symbols.comValue()){
							String value = ReadXMLtools.getNodeContent(n);
							if(value == null){
								errors.append(paramName).append(", ");
								attributeList.clear();
								continue outerLoop;
							}
							else{
								attributeList.add(value);
							}
						}
						else{
							errors.append(paramName).append(", ");
							attributeList.clear();
							continue outerLoop;
						}
					}
					else{
						attributeList.add(a.getNodeValue());
					}
				}
				String[] theParams = new String[attributeList.size()];
				attributeList.toArray(theParams);
				return new CodeNode(commandStr, theParams, children);
			}
			else{
				return new CodeNode(commandStr, new String[0], children);
			}
		}
		System.err.println(errors);
		return null;

	}

}
