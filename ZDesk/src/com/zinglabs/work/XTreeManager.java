package com.zinglabs.work;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class XTreeManager {
	private static Log LOG = LogFactory.getLog(XTreeManager.class);

	private final static String CRLF = "\n";

	
	public static void addLine(int spaceNumber, StringBuffer strBuffer,
			String value) {
		for (int i = 0; i < spaceNumber; i++) {
			strBuffer.append("");
		}

		strBuffer.append(value);
		strBuffer.append(CRLF);
	}

	
	public static void addLine(StringBuffer strBuffer, String value) {
		
		addLine(0, strBuffer, value);
	}

	
	public static void beginTree(StringBuffer strBuffer, XTreeNode rootNode) {
		addLine(strBuffer, "<SCRIPT>");
		addLine(strBuffer, "var tree = new WebFXTree(\"" + rootNode.getText()
				+ "\"); ");
		//LOG.debug("beginTree character is not display");
		if(!("".equals(rootNode.getIcon()))){
			addLine(strBuffer, "tree.icon = \"" + rootNode.getIcon() + "\";");
		}
		if(!("".equals(rootNode.getOpenIcon()))){
			addLine(strBuffer, "tree.openIcon = \"" + rootNode.getOpenIcon()
				+ "\";");
		}
		if (!("".equals(rootNode.getTarget()))) {
			addLine(strBuffer, "tree.target = \"" + rootNode.getTarget() + "\";");
		}
		
		addLine(strBuffer, "tree.action = \"" + rootNode.getAction() + "\";");
		
	}

	
	public static void endTree(StringBuffer strBuffer) {
		addLine(strBuffer, "document.write(tree);");
		addLine(strBuffer, "</SCRIPT>");
	}

	
	public static void addTreeNode(StringBuffer strBuffer, XTreeNode node) {
		if (LOG.isDebugEnabled()) {
	//		LOG.debug(node);
		}
		addLine(strBuffer, "var item_" + node.getId()
				+ " = new WebFXTreeItem(\"" + node.getText() + "\");");
		
		//LOG.debug("addTreeNode character is not display");
		
		addLine(strBuffer, "item_" + node.getId() + ".entityId = \""
				+ node.getId() + "\";");
		
		
		if(!("".equals(node.getIcon()))){
			addLine(strBuffer, "item_" + node.getId() + ".icon = \""
					+ node.getIcon() + "\";");
		}
		if(!("".equals(node.getOpenIcon()))){
			addLine(strBuffer, "item_" + node.getId() + ".openIcon = \""
					+ node.getOpenIcon() + "\";");
		}
		
		if (!("".equals(node.getTarget()))) {
			addLine(strBuffer, "item_" + node.getId() + ".target = \""
					+ node.getTarget() + "\";");
		}
		
		addLine(strBuffer, "item_" + node.getId() + ".action = \""
				+ node.getAction() + "\";");
		
		
		if (node.getLevel() == 1) {
			addLine(strBuffer, "tree.add(item_" + node.getId() + ");");
		} else {
			addLine(strBuffer, "item_" + node.getParentId() + ".add(item_"
					+ node.getId() + ");");
		}
	}
}
