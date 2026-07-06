package org.yooreeka.test.crawling;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.playwright.PlaywrightTransport;

public class PlaywrightTransportTest {

	private PlaywrightTransport transport;

	@Before
	public void setUp() {
		transport = new PlaywrightTransport();
		transport.init();
	}

	@After
	public void tearDown() {
		transport.clear();
	}

	@Test
	public void testFetchWikipedia() throws Exception {
		String url = "https://en.wikipedia.org/wiki/Main_Page";
		FetchedDocument doc = transport.fetch(url, "test-group", 1);
		
		Assert.assertNotNull("Document should not be null", doc);
		Assert.assertEquals("URL should match", url, doc.getDocumentURL());
		
		String html = new String(doc.getDocumentContent(), StandardCharsets.UTF_8);
		Assert.assertTrue("HTML should contain Wikipedia text", html.toLowerCase().contains("wikipedia"));
	}

	@Test
	public void testFetchNpr() throws Exception {
		String url = "https://www.npr.org/";
		FetchedDocument doc = transport.fetch(url, "test-group", 2);
		
		Assert.assertNotNull("Document should not be null", doc);
		Assert.assertEquals("URL should match", url, doc.getDocumentURL());
		
		String html = new String(doc.getDocumentContent(), StandardCharsets.UTF_8);
		Assert.assertTrue("HTML should contain NPR text", html.toLowerCase().contains("npr") || html.toLowerCase().contains("national public radio"));
	}

	@Test
	public void testFetchLocalDeveloperGuide() throws Exception {
		File devGuideFile = new File("C:/Code/BitBucket/Mg/yooreeka/docs/developer_guide.md");
		Assert.assertTrue("Developer guide file must exist at " + devGuideFile.getAbsolutePath(), devGuideFile.exists());
		
		String fileUrl = devGuideFile.toURI().toURL().toString();
		FetchedDocument doc = transport.fetch(fileUrl, "test-group", 3);
		
		Assert.assertNotNull("Document should not be null", doc);
		
		String content = new String(doc.getDocumentContent(), StandardCharsets.UTF_8);
		Assert.assertTrue("Content should contain Yooreeka", content.contains("Yooreeka"));
	}
}
