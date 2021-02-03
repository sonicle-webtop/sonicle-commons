/*
 * Copyright (C) 2017 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2017 Sonicle S.r.l.".
 */
package com.sonicle.commons.http;

import com.sonicle.commons.URIUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author malbinola
 */
public class HttpClientUtils {
	private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(HttpClientUtils.class);
	
	public static void closeQuietly(HttpClient httpClient) {
		org.apache.http.client.utils.HttpClientUtils.closeQuietly(httpClient);
	}
	
	public static HttpClient createBasicHttpClient(URI uri) {
		return createBasicHttpClient(HttpClientBuilder.create(), uri);
	}
	
	public static HttpClientBuilder configureSSLAcceptAll() {
		return configureSSLAcceptAll(HttpClientBuilder.create());
	}
	
	public static HttpClientBuilder configureSSLAcceptAll(HttpClientBuilder builder) {
		try {
			builder.setSSLContext(new SSLContextBuilder()
				.loadTrustMaterial(null, new TrustAllStrategy()).build()
			);
		} catch(KeyStoreException | NoSuchAlgorithmException | KeyManagementException ex) {
			LOGGER.error("Error applying SSLContext", ex);
		}
		builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
		return builder;
	}
	
	public static HttpClient createBasicHttpClient(HttpClientBuilder builder, URI uri) {
		String[] tokens = URIUtils.getUserInfo(uri);
		if (tokens != null) {
			CredentialsProvider basic = new BasicCredentialsProvider();
			basic.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(tokens[0], tokens[1]));
			builder.setDefaultCredentialsProvider(basic);
		}
		return builder.build();
	}
	
	public static boolean exists(HttpClient client, URI uri) throws IOException {
		HttpResponse response = client.execute(new HttpHead(uri));
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;	
	}
	
	public static void writeContent(HttpClient client, URI uri, OutputStream output) throws IOException {
		HttpResponse response = client.execute(new HttpGet(uri));
		final int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			response.getEntity().writeTo(output);
		} else {
			throw new IOException(MessageFormat.format("Server returns {0}: {1}", statusCode, response.getStatusLine().getReasonPhrase()));
		}
	}
	
	public static String getStringContent(HttpClient client, URI uri) throws IOException {
		HttpResponse response = client.execute(new HttpGet(uri));
		final StatusLine statusLine = response.getStatusLine();
		if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
			return EntityUtils.toString(response.getEntity());
		} else {
			throw new ResponseException(statusLine);
		}
	}
	
	//public static HttpEntity getContent(HttpClient client, URI uri) throws IOException {
	public static InputStream getContent(HttpClient client, URI uri) throws IOException {
		HttpResponse response = client.execute(new HttpGet(uri));
		final StatusLine statusLine = response.getStatusLine();
		if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
			return response.getEntity().getContent();
		} else {
			throw new ResponseException(statusLine);
		}
	}
}
