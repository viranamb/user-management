package com.capitalone.migration.service.impl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Added factory class for not doing host name verification during SSL handshake
 *
 * @author ecd456 -
 * @since 1.0
 */
public class NSBSimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    private final HostnameVerifier verifier;

    public NSBSimpleClientHttpRequestFactory(HostnameVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
        }
        super.prepareConnection(connection, httpMethod);
    }

}
