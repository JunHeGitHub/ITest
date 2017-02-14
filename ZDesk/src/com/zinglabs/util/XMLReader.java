package com.zinglabs.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to parse a XML File.
 *
 * @Class Name: XMLUtil
 * @version 1.0
 */

public class XMLReader {
	/**
	 * The cDocument represents the entire HTML or XML document. Conceptually,
	 * it is the root of the document tree, and provides the primary access to
	 * the document's data.
	 */
	private Document cDocument;

	private XMLReader() {
	};

	public XMLReader(String aXMLFileName) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory lDocumentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder lDocumentBuilder = lDocumentBuilderFactory
				.newDocumentBuilder();
		cDocument = lDocumentBuilder.parse(new File(aXMLFileName));
	}

	/**
	 * Search a Node from cDocument example: in Config.XML <Screen id="SEC001">
	 * <Action id="Save"> <ClassName>SomePackage.SomeCommand</ClassName>
	 * </Action> </Screen>
	 *
	 * @parameter aNodeName: a Segment name at XML,just like "Screen" "Action"
	 * @parameter aParaName: a Segment attribute name just like "id"
	 * @parameter aParaValue:a Segment attribute value just like "SEC001","Save"
	 * @parameter aNode: begin search Node,if null,then from root node.
	 * @return if success search a Node then return Node,else return null;
	 *
	 */
	public Node getNode(String aNodeName, String aParaName, String aParaValue,
			Node aNode) {
		NodeList lNodeList = null;
		if (aNode == null) {
			if (cDocument == null)
				return null;
			lNodeList = cDocument.getChildNodes();
		} else {
			lNodeList = aNode.getChildNodes();
		}
		for (int i = 0; i < lNodeList.getLength(); i++) {
			Node lNode = lNodeList.item(i);
			if (lNode instanceof EntityReference || lNode instanceof Element) {
				if (lNode.getNodeName().equalsIgnoreCase(aNodeName)) {
					if ((aParaName == null) || aParaName.equals("")) {
						return lNode;
					} else {
						String lNodeValue = lNode.getAttributes().getNamedItem(
								aParaName).getNodeValue();
						if (lNodeValue.equalsIgnoreCase(aParaValue)) {
							return lNode;
						}
					}
				}
			}
		}
		for (int i = 1; i < lNodeList.getLength(); i++) {
			Node lNode = lNodeList.item(i);
			if (lNode instanceof EntityReference || lNode instanceof Element) {
				Node tmpNode = getNode(aNodeName, aParaName, aParaValue, lNode);
				if (tmpNode != null)
					return tmpNode;
			}
		}
		return null;
	}

	/**
	 * Search a Node from cDocument example: in Config.XML <Screen id="SEC001">
	 * <Action id="Save"> <ClassName>SomePackage.SomeCommand</ClassName>
	 * </Action> </Screen>
	 *
	 * @parameter aNodeName: a Segment name at XML,just like "Screen" "Action"
	 * @parameter aNode: begin search Node,if null,then from root node.
	 * @return if success search a Node then return Node,else return null;
	 * @version 1.0
	 *
	 */
	public Node getNode(String aNodeName, Node aNode) {
		return this.getNode(aNodeName, "", "", aNode);
	}
}
