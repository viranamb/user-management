package com.capitalone.migration.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public final class SSLRestTemplateFactory {

    private static final Logger LOGGER = LogManager.getLogger(SSLRestTemplateFactory.class);

    private SSLRestTemplateFactory() {
    }

    public static RestTemplate getSSLRestTemplate(String keyStoreFileName, String certificatePassword) throws Exception {

        FileSystemResource resource = new FileSystemResource(keyStoreFileName);
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(resource.getInputStream(), certificatePassword.toCharArray());
            return new RestTemplate(getSSLRequestFactory(keyStore, certificatePassword));
        } catch (UnrecoverableKeyException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            LOGGER.error("Unable to create RestTemplate, Missing or invalid keys" + e);
            throw new Exception("Unable to create RestTemplate, Missing or invalid keys");
        }
    }

    private static ClientHttpRequestFactory getSSLRequestFactory(KeyStore keyStore, String certificatePassword) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, certificatePassword.toCharArray())
                .loadTrustMaterial(keyStore, null).build();

        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        HttpClient httpClient = httpClientBuilder.setSSLSocketFactory(sslSocketFactory).build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

}
