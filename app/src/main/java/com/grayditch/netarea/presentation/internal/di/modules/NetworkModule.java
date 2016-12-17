package com.grayditch.netarea.presentation.internal.di.modules;

import android.content.Context;

import com.grayditch.netarea.data.network.CookieStore;
import com.grayditch.netarea.data.network.NetareaClient;
import com.grayditch.netarea.data.network.NetareaPageParser;
import com.grayditch.netarea.data.network.NetworkUtils;
import com.grayditch.netarea.data.network.PreferredCipherSuiteSSLSocketFactory;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

/**
 * Created by gerard on 14/04/16.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    ConnectionSpec providesConnectionSpec() {
        return new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_0)
                .cipherSuites(CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA)
                .build();
    }

    @Provides
    @Singleton
    SSLSocketFactory providesSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            context.init(null, trustManagerFactory.getTrustManagers(), null);
            return new PreferredCipherSuiteSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(ConnectionSpec connectionSpec, SSLSocketFactory sslSocketFactory) {
        return new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .cookieJar(new CookieStore())
                .connectionSpecs(Collections.singletonList(connectionSpec))
                .sslSocketFactory(sslSocketFactory)
                .build();
    }

    @Provides
    @Singleton
    NetareaPageParser provideNetareaPageParser() {
        return new NetareaPageParser();
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }

    @Provides
    @Singleton
    NetareaClient provideNetareaClient(OkHttpClient okHttpClient, NetareaPageParser netareaPageParser, NetworkUtils networkUtils) {
        return new NetareaClient(okHttpClient, netareaPageParser, networkUtils);
    }
}
