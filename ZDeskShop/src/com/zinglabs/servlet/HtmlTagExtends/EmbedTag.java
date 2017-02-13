package com.zinglabs.servlet.HtmlTagExtends;

import org.htmlparser.nodes.TagNode;

public class EmbedTag extends TagNode {

	private static final long serialVersionUID = 2721559952651742203L;
	private static final String[] mIds = { "EMBED" };

	public String[] getIds() {
		return mIds;
	}

	public String getSrc() {
		String ret = super.getAttribute("SRC");
		if (null == ret){
			ret = "";
		}else if (null != super.getPage()) {
			ret = super.getPage().getAbsoluteURL(ret);
		}
		return ret;
	}

	public void setSrc(String url) {
		super.setAttribute("SRC", url);
	}

	public String getName() {
		return super.getAttribute("NAME");
	}

	public String toString() {
		return "EMBED TAG : EMBED " + getName() + " at "
				+ getSrc() + "; begins at : "
				+ super.getStartPosition() + "; ends at : "
				+ super.getEndPosition();
	}
}
