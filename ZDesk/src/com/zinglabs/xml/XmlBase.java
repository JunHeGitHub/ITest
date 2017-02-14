package com.zinglabs.xml;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.Attribute;
import com.zinglabs.tools.DOMTool;

public class XmlBase extends BaseAbs {
	
	public void initAttrs(Document doc) throws XPathExpressionException {
		this.apperCount=0;
		this.xTimeFormat=null;
		this.attrs=null;
		this.y=null;
		this.colors=null;
		this.yTimeFormat=null;
		this.x=null;
		Node header = DOMTool.getSingleNode(doc, "headers");
		Node att = DOMTool.getSingleNode(doc, "attributes");
		
		NodeList nl = DOMTool.getMultiNodes(header, "header");
		
		this.attrs = new Attribute[nl.getLength()];
		if (this.attrMap == null) {
			this.attrMap = new HashMap<String, Attribute>();
		}
		Node tmp=null;
		for (int i = 0; i < nl.getLength(); i++) {
			String index=DOMTool.getAttributeValue((Element)nl.item(i), "attrIndex");
			XPathExpression expr = xpath.compile("/DataWindow/attributes/attribute[@index='"+index+"']");
			Object result = expr.evaluate(doc, XPathConstants.NODE);
		    tmp = (Node) result;
//		    System.out.println(index+"-------------"+tmp+"--------------"+DOMTool.getAttributeValue((Element)nl.item(i), "attrName"));
			this.attrs[i] = new Attribute(tmp);
			if(this.attrs[i].getAttrValue("attrName")!=null){
				attrMap.put(this.attrs[i].getAttrValue("attrName"),this.attrs[i]);
			}
			if(this.attrs[i].getAttrValue("fieldLen")!=null && this.attrs[i].getAttrValue("fieldLen").length()>0){
				this.apperCount++;
			}
			
		}
	}
	

	public void initAttrsChart(Document doc) throws XPathExpressionException {
		Node y = DOMTool.getSingleNode(doc, "y");
		NodeList nl = DOMTool.getMultiNodes(y, "ele");
//		this.y = new HashMap<String, Attribute>();
		Element tmp=null;
		
		this.y = new String[nl.getLength()];
		this.colors=new String[nl.getLength()];
		this.yTimeFormat=new String[nl.getLength()];
		for (int i = 0; i < nl.getLength(); i++) {
			this.y[i]=DOMTool.getAttributeValue((Element)nl.item(i), "eleName");
			this.colors[i]=DOMTool.getAttributeValue((Element)nl.item(i), "color");
			XPathExpression expr = xpath.compile("/DataWindow/attributes/attribute[@attrName='"+this.y[i]+"']");
			Object result = expr.evaluate(doc, XPathConstants.NODE);
			tmp = (Element) result;
		    this.yTimeFormat[i]=DOMTool.getAttributeValue(tmp, "timeFormat");
		}
		
		Node x = DOMTool.getSingleNode(doc, "x");
		Node xAttr = DOMTool.getSingleNode(x, "ele");
		this.x=DOMTool.getAttributeValue((Element)xAttr, "eleName");
		XPathExpression expr = xpath.compile("/DataWindow/attributes/attribute[@attrName='"+this.x+"']");
		Object result = expr.evaluate(doc, XPathConstants.NODE);
	    tmp = (Element) result;
	    this.xTimeFormat=DOMTool.getAttributeValue(tmp, "timeFormat");
		
	}
	
	protected XPathFactory factoryXpah = XPathFactory.newInstance();
	protected XPath xpath = factoryXpah.newXPath();
	protected Attribute[] attrs;
	protected String[] y;
	protected String[] colors;
	protected String[] yTimeFormat;
	protected String x;
	protected String xTimeFormat;
	protected int apperCount;
	
	protected HashMap<String, Attribute> attrMap;
}
