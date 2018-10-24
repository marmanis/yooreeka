/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package org.yooreeka.util.parsing.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.ElementRemover;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.yooreeka.util.internet.crawling.model.Outlink;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.ProcessedDocument;

/**
 * Parser for HTML documents.
 */
public class HTMLDocumentParser implements DocumentParser {

	ProcessedDocument htmlDoc;

	public HTMLDocumentParser() {
		// NOTHING YET
	}

	public HTMLDocumentParser(Reader reader) throws HTMLDocumentParserException {
		HTMLDocumentParser p = new HTMLDocumentParser();
		htmlDoc = p.parse(reader);
	}

	/*
	 * Builds absolute URL. For relative URLs will use source document URL and
	 * base URL.
	 */
	private String buildUrl(String href, String baseUrl, String documentUrl) {

		String url = null;

		String protocol = extractProtocol(href);

		if (protocol != null) {
			url = href;
		} else if (baseUrl != null) {
			url = baseUrl + href;
		} else if (href.startsWith("/")) {
			try {
				URL docUrl = new URL(documentUrl);
				if (docUrl.getPort() == -1) {
					url = docUrl.getProtocol() + "://" + docUrl.getHost()
							+ href;
				} else {
					url = docUrl.getProtocol() + "://" + docUrl.getHost() + ":"
							+ docUrl.getPort() + href;
				}
			} catch (MalformedURLException e) {
				url = null;
			}
		} else {
			url = extractParent(documentUrl) + href;
		}

		return url;
	}

	private String cleanText(String text) {
		if (text == null) {
			return null;
		}
		String t = text.replaceAll("[ \t]+", " ");
		t = t.replaceAll("[ \t][\r\n]", "\n");
		t = t.replaceAll("[\r\n]+", "\n");
		return t;
	}

	private List<Outlink> extractLinks(Node node, String docUrl, String baseUrl) {
		if (isNoFollowForDocument(node)) {
			return new ArrayList<Outlink>();
		}

		org.w3c.dom.Document doc = getDocumentNode(node);
		DocumentTraversal traversableDoc = (DocumentTraversal) doc;
		NodeFilter linkFilter = getLinkNodeFilter();
		NodeIterator iterator = traversableDoc.createNodeIterator(node,
				NodeFilter.SHOW_ELEMENT, linkFilter, true);
		Node currentNode = null;

		List<Outlink> outlinks = new ArrayList<Outlink>();

		while ((currentNode = iterator.nextNode()) != null) {
			String href = currentNode.getAttributes().getNamedItem("href")
					.getNodeValue();
			boolean nofollow = isNoFollowPresent(currentNode);
			if (nofollow == false) {
				if ("BASE".equalsIgnoreCase(node.getNodeName())) {
					// ignore this link
				} else {
					String url = buildUrl(href, baseUrl, docUrl);
					if (url != null) {
						String anchorText = getAnchorText(currentNode);
						Outlink link = new Outlink(url, anchorText);
						outlinks.add(link);
					}
				}
			}
		}

		return outlinks;
	}

	private String extractParent(String url) {
		String parent = url;
		int i = url.lastIndexOf("/");
		if (i > -1) {
			parent = url.substring(0, i + "/".length());
		}
		return parent;
	}

	/*
	 * Extracts url protocol if present. Handles two cases:
	 * 
	 * 1. "<protocol>://<host>" 2. "mailto:<email address>"
	 */
	private String extractProtocol(String url) {
		String protocol = null;
		if (url.startsWith("mailto:")) {
			protocol = "mailto";
		} else {
			int i = url.indexOf("://");
			if (i > -1) {
				protocol = url.substring(0, i);
			}
		}
		return protocol;
	}

	private String getAnchorText(Node currentNode) {
		String text = getText(currentNode);
		String cleanText = null;
		if (text != null) {
			cleanText = text.replaceAll("[\r\n\t]", " ").trim();
		}
		return cleanText;
	}

	private String getBaseUrl(Node node) {
		if (node == null) {
			return null;
		}
		org.w3c.dom.Document doc = getDocumentNode(node);
		NodeList nodeList = doc.getElementsByTagName("base");
		Node baseNode = nodeList.item(0);
		if (baseNode != null) {
			NamedNodeMap attrs = baseNode.getAttributes();
			if (attrs != null) {
				Node href = attrs.getNamedItem("href");
				if (href != null) {
					return href.getNodeValue();
				}
			}
		}
		return null;
	}

	@Override
	public DataEntry getDataEntry(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	private org.w3c.dom.Document getDocumentNode(Node node) {
		if (node == null) {
			return null;
		}

		if (Node.DOCUMENT_NODE == node.getNodeType()) {
			return (org.w3c.dom.Document) node;
		} else {
			return node.getOwnerDocument();
		}
	}

	public ProcessedDocument getHtmlDoc() {
		return htmlDoc;
	}

	private NodeFilter getLinkNodeFilter() {
		CompositeFilter linkFilter = new CompositeFilter();
		// For now doing the simplest thing possible - only consider <A>
		// elements
		linkFilter.addAcceptFilter(new ElementNodeFilter("a", "href"));
		/*
		 * Other elements to consider:
		 * 
		 * linkFilter.addAcceptFilter(new LinkNodeFilter("frame", "src"));
		 * linkFilter.addAcceptFilter(new LinkNodeFilter("link", "href"));
		 */
		return linkFilter;
	}

	private String getRobotsMeta(Node node) {
		if (node == null) {
			return null;
		}
		org.w3c.dom.Document doc = getDocumentNode(node);
		NodeList nodeList = doc.getElementsByTagName("meta");
		if (nodeList != null) {
			for (int i = 0, n = nodeList.getLength(); i < n; i++) {
				Node currentNode = nodeList.item(i);
				NamedNodeMap attrs = currentNode.getAttributes();
				if (attrs != null) {
					Node contentNode = attrs.getNamedItem("content");
					Node nameNode = attrs.getNamedItem("name");
					if (nameNode != null && contentNode != null) {
						if ("ROBOTS".equalsIgnoreCase(nameNode.getNodeValue())) {
							if (contentNode != null) {
								return contentNode.getNodeValue();
							}
						}
					}
				}
			}
		}
		return null;
	}

	private String getText(Node node) {
		if (node == null) {
			return "";
		}

		org.w3c.dom.Document doc = getDocumentNode(node);
		org.w3c.dom.traversal.DocumentTraversal traversable = (DocumentTraversal) doc;
		int whatToShow = NodeFilter.SHOW_TEXT;
		NodeIterator nodeIterator = traversable.createNodeIterator(node,
				whatToShow, null, true);

		StringBuffer text = new StringBuffer();
		Node currentNode = null;
		while ((currentNode = nodeIterator.nextNode()) != null) {
			text.append(currentNode.getNodeValue());
		}
		return text.toString();
	}

	private String getTitle(Node node) {
		if (node == null) {
			return "";
		}

		String cleanTitle = null;
		org.w3c.dom.Document doc = getDocumentNode(node);
		NodeList nodeList = doc.getElementsByTagName("title");
		Node matchedNode = nodeList.item(0);
		if (matchedNode != null) {
			String title = matchedNode.getTextContent();
			if (title != null) {
				cleanTitle = title.replaceAll("[\r\n\t]", " ").trim();
			}
		}

		return cleanTitle;
	}

	private boolean isNoFollowForDocument(Node node) {
		boolean noFollow = false;

		// Check <META name="robots" content="..."/>
		String robotsMeta = getRobotsMeta(node);
		if (robotsMeta != null
				&& robotsMeta.toLowerCase().indexOf("nofollow") > -1) {
			noFollow = true;
		}

		return noFollow;
	}

	/*
	 * Checks for presense of rel="nofollow" attribute.
	 */
	private boolean isNoFollowPresent(Node currentNode) {
		Node relAttr = currentNode.getAttributes().getNamedItem("rel");
		boolean nofollow = false;
		if (relAttr != null) {
			String relAttrValue = relAttr.getNodeValue();
			if ("nofollow".equalsIgnoreCase(relAttrValue)) {
				nofollow = true;
			}
		}
		return nofollow;
	}

	public ProcessedDocument parse(AbstractDocument doc)
			throws HTMLDocumentParserException {
		//P.println("Entering HTMLDocumentParser.parse(FetchedDocument doc) ...");
		ProcessedDocument htmlDoc = new ProcessedDocument();
		htmlDoc.setDocumentType(ProcessedDocument.TYPE_HTML);
		htmlDoc.setDocumentId(doc.getDocumentId());
		htmlDoc.setDocumentURL(doc.getDocumentURL());
		String documentCharset = doc.getContentCharset();

		//P.println("Converting the content bytes into a string ...");

		InputStream contentBytes = new ByteArrayInputStream(doc.getDocumentContent());
		try {
			/*
			 * Up to this point document content was treated as byte array. Here
			 * we convert byte array into character based stream. Processed
			 * document will be stored using UTF-8 encoding.
			 */
			InputStreamReader characterStream = new InputStreamReader(contentBytes, documentCharset);
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(characterStream);
			parseHTML(htmlDoc, inputSource);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new HTMLDocumentParserException("Document parsing error: ", e);
		}
		return htmlDoc;
	}

	public ProcessedDocument parse(Reader reader)
			throws HTMLDocumentParserException {
		//P.println("Entering HTMLDocumentParser.parse(Reader reader) ...");
		ProcessedDocument processedDocument = new ProcessedDocument();
		processedDocument.setDocumentType(ProcessedDocument.TYPE_HTML);
		processedDocument.setDocumentId(null);
		processedDocument.setDocumentURL(null);
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(reader);
		parseHTML(processedDocument, inputSource);
		return processedDocument;
	}

	private void parseHTML(ProcessedDocument htmlDoc, InputSource inputSource)
			throws HTMLDocumentParserException {
		// NekoHTML parser
		DOMParser parser = new DOMParser();

		// Create filter to remove elements that we don't care about.
		ElementRemover remover = new ElementRemover();
		// keep only a subset of elements (text and links)
		remover.acceptElement("html", null);
		remover.acceptElement("meta", new String[] { "name", "content" });
		remover.acceptElement("title", null);
		remover.acceptElement("body", null);
		remover.acceptElement("base", new String[] { "href" });
		remover.acceptElement("b", null);
		remover.acceptElement("i", null);
		remover.acceptElement("u", null);
		remover.acceptElement("p", null);
		remover.acceptElement("br", null);
		remover.acceptElement("a", new String[] { "href", "rel" });
		// completely remove these elements
		remover.removeElement("script");
		remover.removeElement("style");

		StringWriter sw = new StringWriter();
		XMLDocumentFilter writer = new HTMLWriter(sw, "UTF-8");

		XMLDocumentFilter[] filters = { remover, writer };
		try {
			parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new HTMLDocumentParserException("Property is not supported", e);
		}

		try {
			parser.parse(inputSource);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new HTMLDocumentParserException("Parsing error: ", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HTMLDocumentParserException("Parsing error: ", e);
		}

		// cleaned up html.
		String cleanHTML = cleanText(sw.toString());
		htmlDoc.setContent(cleanHTML);

		// just the text
		Node node = parser.getDocument();
		String text = cleanText(getText(node));
		htmlDoc.setText(text);

		// content of <title/>
		String title = getTitle(node);
		htmlDoc.setDocumentTitle(title);

		if (htmlDoc.getDocumentURL() != null) {
			String baseUrl = getBaseUrl(node);

			// links to other pages
			List<Outlink> outlinks = extractLinks(node,	htmlDoc.getDocumentURL(), baseUrl);
			htmlDoc.setOutlinks(outlinks);
		}
	}

	public void setHtmlDoc(ProcessedDocument htmlDoc) {
		this.htmlDoc = htmlDoc;
	}
}
