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
package org.yooreeka.util.internet.crawling.transport.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.ContextBuilder;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.StandardAuthScheme;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.config.TlsConfig;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.client5.http.impl.auth.CredentialsProviderBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.StatusLine;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.util.P;
import org.yooreeka.util.internet.crawling.db.FetchedDocsDB;
import org.yooreeka.util.internet.crawling.model.FetchedDocument;
import org.yooreeka.util.internet.crawling.transport.common.Transport;
import org.yooreeka.util.internet.crawling.transport.common.TransportException;
import org.yooreeka.util.internet.crawling.util.DocumentIdUtils;

public class HTTPTransport implements Transport {

	public static final int MINIMUM_BUFFER_SIZE=1024;
	
	private FetchedDocsDB db;
	
	/*
	 * Maximum document length that transport will attempt to download
	 * without issuing a warning ...
	 */
	public static final int MAX_DOCUMENT_LENGTH = 8 * 1024 * 1024; // 2Mb

	CloseableHttpClient httpclient = null;
	
	final PoolingHttpClientConnectionManager connManager;
	final CookieStore cookieStore;
	final CredentialsProvider credentialsProvider;
	final RequestConfig defaultRequestConfig;
	HttpContext localContext = null;

	public HTTPTransport() {
		
		P.println("Initializing HTTPTransport ...");

		// Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
//        final HttpMessageParserFactory<ClassicHttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
//
//            @Override
//            public HttpMessageParser<ClassicHttpResponse> create(final Http1Config h1Config) {
//                final LineParser lineParser = new BasicLineParser() {
//
//                    @Override
//                    public Header parseHeader(final CharArrayBuffer buffer) {
//                        try {
//                            return super.parseHeader(buffer);
//                        } catch (final ParseException ex) {
//                            return new BasicHeader(buffer.toString(), null);
//                        }
//                    }
//
//                };
//                return new DefaultHttpResponseParser(lineParser, DefaultClassicHttpResponseFactory.INSTANCE, h1Config);
//            }
//
//        };
        
//        final HttpMessageWriterFactory<ClassicHttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Create HTTP/1.1 protocol configuration
//        final Http1Config h1Config = Http1Config.custom()
//                .setMaxHeaderCount(200)
//                .setMaxLineLength(2000)
//                .build();

        // Create connection configuration
//        final CharCodingConfig connectionConfig = CharCodingConfig.custom()
//                .setMalformedInputAction(CodingErrorAction.IGNORE)
//                .setUnmappableInputAction(CodingErrorAction.IGNORE)
//                .setCharset(StandardCharsets.UTF_8)
//                .build();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
//        final HttpConnectionFactory<ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
//                h1Config, connectionConfig, requestWriterFactory, responseParserFactory);

        // Client HTTP connection objects when fully initialized can be bound to
        // an arbitrary network socket. The process of network socket initialization,
        // its connection to a remote address and binding to a local one is controlled
        // by a connection socket factory.

        // SSL context for secure connections can be created either based on
        // system or application specific properties.
        final SSLContext sslContext = SSLContexts.createSystemDefault();

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("https", new SSLConnectionSocketFactory(sslContext))
            .build();

        // Use custom DNS resolver to override the system DNS resolution.
        final DnsResolver dnsResolver = new SystemDefaultDnsResolver() {

            @Override
            public InetAddress[] resolve(final String host) throws UnknownHostException {
                if (host.equalsIgnoreCase("myhost")) {
                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
                } else {
                    return super.resolve(host);
                }
            }

        };

	
        // Create a connection manager with custom configuration.
        connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, PoolReusePolicy.LIFO, TimeValue.ofMinutes(5),
                null, dnsResolver, null);
        
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(SocketConfig.custom()
                .setTcpNoDelay(true)
                .build());
        // Validate connections after 10 sec of inactivity
        connManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(30))
                .setSocketTimeout(Timeout.ofSeconds(30))
                .setValidateAfterInactivity(TimeValue.ofSeconds(10))
                .setTimeToLive(TimeValue.ofHours(1))
                .build());

        // Use TLS v1.3 only
        connManager.setDefaultTlsConfig(TlsConfig.custom()
                .setHandshakeTimeout(Timeout.ofSeconds(30))
                .setSupportedProtocols(TLS.V_1_3)
                .build());

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(100);
        connManager.setDefaultMaxPerRoute(10);
        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
        
        
        // Use custom cookie store if necessary.
        cookieStore = new BasicCookieStore();
        
        // Use custom credentials provider if necessary.
        credentialsProvider = CredentialsProviderBuilder.create().build();
        
        // Create global request configuration
        defaultRequestConfig = RequestConfig.custom()
            .setCookieSpec(StandardCookieSpec.STRICT)
            .setExpectContinueEnabled(true)
            .setTargetPreferredAuthSchemes(Arrays.asList(StandardAuthScheme.NTLM, StandardAuthScheme.DIGEST))
            .setProxyPreferredAuthSchemes(Collections.singletonList(StandardAuthScheme.BASIC))
            .build();

        
        
	}

	public void clear() {
		httpclient = null;
		// initialState = null;
	}

	private FetchedDocument createDocument(String targetURL, HttpEntity entity, String groupId, int docSequenceInGroup)
			throws IOException, HTTPTransportException {
		
		FetchedDocument doc = new FetchedDocument();
		String documentId = DocumentIdUtils.getDocumentId(groupId, docSequenceInGroup);
		
		BufferedInputStream bufferedInput = null;
		byte[] buffer = new byte[MINIMUM_BUFFER_SIZE];

		int contentLength = (int) entity.getContentLength();
		if (contentLength > MAX_DOCUMENT_LENGTH)
			P.println("WARNING: Retrieved document larger than "
					+ MAX_DOCUMENT_LENGTH + " [bytes]");

		// If the size is smaller than the minimum then extend it
//		if (contentLength < MINIMUM_BUFFER_SIZE)
//			contentLength = MINIMUM_BUFFER_SIZE;
		
		ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_DOCUMENT_LENGTH);

		// Construct the BufferedInputStream object
		bufferedInput = new BufferedInputStream(entity.getContent());

		// Keep reading while there is content
		// when the end of the stream has been reached, -1 is returned
		while (bufferedInput.read(buffer) != -1) {

			// Process the chunk of bytes read
			byteBuffer.put(buffer);
		}

		/* IOException will be thrown for documents that exceed max length */
		byte[] data = byteBuffer.array();

		/*
		 * Check if server sent content in compressed form and uncompress the
		 * content if necessary.
		 */
		// Header contentEncodingHeader = entity.getContentEncoding();
		String contentEncodingHeader = entity.getContentEncoding();
		if (contentEncodingHeader != null) {
			data = HTTPUtils.decodeContent(contentEncodingHeader, data);
		}

		/* 'Content-Type' HTTP header value */
		String contentTypeHeaderValue = null;
		String header = entity.getContentType();
		if (header != null) {
			contentTypeHeaderValue = header;
		}

		/*
		 * Determine MIME type of the document.
		 * 
		 * It is easy if we have Content-Type http header. In cases when this
		 * header is missing or for protocols that don't pass metadata about the
		 * documents (ftp://, file://) we would have to resort to url and/or
		 * content analysis to determine MIME type.
		 */
		String DEFAULT_CONTENT_TYPE = "text/html";
		String contentType = HTTPUtils.getContentType(contentTypeHeaderValue,
				targetURL, data);
		if (contentType == null) {
			contentType = DEFAULT_CONTENT_TYPE;
		}

		/*
		 * Determine Character encoding used in the document. In some cases it
		 * may be specified in the http header, in html file itself or we have
		 * to perform content analysis to choose the encoding.
		 */
		String DEFAULT_CONTENT_CHARSET = "UTF-8";
		String contentCharset = HTTPUtils.getCharset(contentTypeHeaderValue,
				contentType, data);
		if (contentCharset == null) {
			contentCharset = DEFAULT_CONTENT_CHARSET;
		}

		doc.setDocumentId(documentId);
		doc.setContentType(contentType);
		doc.setDocumentURL(targetURL);
		doc.setContentCharset(contentCharset);
		doc.setDocumentContent(data);
		doc.setDocumentMetadata(new HashMap<String, String>());
		return doc;
	}

	public FetchedDocument fetch(String documentUrl, String groupId, int docSequenceInGroup) throws TransportException {

		FetchedDocument doc = null;
		
		try (final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setProxy(new HttpHost("myproxy", 8080))
                .setDefaultRequestConfig(defaultRequestConfig)
                .build()) {
            final HttpGet httpget = new HttpGet(documentUrl);
            
    		P.println("executing request " + httpget.getRequestUri());

            // Request configuration can be overridden at the request level.
            // They will take precedence over the one set at the client level.
            final RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
                    .setConnectionRequestTimeout(Timeout.ofSeconds(5))
                    .build();
            httpget.setConfig(requestConfig);

            // Execution context can be customized locally.
            // Contextual attributes set the local context level will take
            // precedence over those set at the client level.
            final HttpClientContext context = ContextBuilder.create()
                    .useCookieStore(cookieStore)
                    .useCredentialsProvider(credentialsProvider)
                    .build();

            try {
				System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    		ClassicHttpResponse response = httpclient.execute(httpget, context, r -> {
                P.hline();
    			P.println(httpget + "->" + new StatusLine(r));
                EntityUtils.consume(r.getEntity());
                return r;
            });		
		
		if (response != null) {
			HttpEntity entity = response.getEntity();
	
			P.hline();
			P.println((new StatusLine(response)).toString()); 
			
			if (entity != null) {
				P.println("Response content length: "
						+ entity.getContentLength());
			}
		
			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				P.println("Local cookie: " + cookies.get(i));
			}
	
			try {
				doc = createDocument(documentUrl, entity, groupId, docSequenceInGroup);
			} catch (IOException e) {
				throw new TransportException("Failed to fetch url: '" + documentUrl	+ "': ", e);
			} 
			
			db.saveDocument(doc);
		}	
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return doc;
	}

	public boolean pauseRequired() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#fixDud()
	 */
	@Override
	public void fixDud(InputStream in) {
		String empty = YooreekaConfigurator.getProperty("yooreeka.crawl.dudfile");
		
		try {
			in = new BufferedInputStream(new FileInputStream(new File(empty)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.yooreeka.util.internet.crawling.transport.common.Transport#setFetchedDocsDB(org.yooreeka.util.internet.crawling.db.FetchedDocsDB)
	 */
	@Override
	public void setFetchedDocsDB(FetchedDocsDB db) {
		this.db = db;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
