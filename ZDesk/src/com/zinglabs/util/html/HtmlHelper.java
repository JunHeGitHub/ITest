package com.zinglabs.util.html;

import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

import com.zinglabs.servlet.HtmlTagExtends.EmbedTag;
import com.zinglabs.servlet.HtmlTagExtends.IframeTag;

public class HtmlHelper {
	
	public static Parser createLexerParser(String htmlstr) {
        Lexer mLexer = new Lexer(new Page(htmlstr));
        return new Parser(mLexer,new DefaultParserFeedback(DefaultParserFeedback.QUIET));
    }
	
	public static HtmlPage createHtmlPage(String htmlstr,Parser parser) throws Exception{
		PrototypicalNodeFactory pnf=new PrototypicalNodeFactory ();
		pnf.registerTag(new EmbedTag());
		pnf.registerTag(new IframeTag());
		parser.setNodeFactory(pnf);
		parser.setEncoding("UTF-8");
		HtmlPage page=new HtmlPage(parser); 
		parser.visitAllNodesWith(page);
		return page;
	}
	
}
