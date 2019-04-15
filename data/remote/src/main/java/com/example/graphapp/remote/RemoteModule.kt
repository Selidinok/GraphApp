package com.example.graphapp.remote

import android.content.Context
import com.example.graphapp.remote.adapter.GraphDeserializer
import com.example.model.remote.GraphResponse
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import org.koin.experimental.builder.single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import okhttp3.CipherSuite
import okhttp3.TlsVersion
import okhttp3.ConnectionSpec



val remoteModule = module {
    val debug = BuildConfig.DEBUG
    val url = BuildConfig.BASE_URL

    single { getClient(debug, get()) }
    single<GraphDeserializer>()
    single { createWebService<GraphApi>(get(), url, createGsonConverterFactory(get())) }
    single<GraphRemoteSource>()
}

private fun getClient(debug: Boolean, context: Context): OkHttpClient {
    val client = OkHttpClient.Builder().apply {
        if (debug) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }
        addInterceptor(getVersionInterceptor())
        connectTimeout(60L, TimeUnit.SECONDS)
        readTimeout(60L, TimeUnit.SECONDS)

        val keyStore = getKeystore(context)
        val (factory, trustManger) = getSocketFactory(keyStore)
        connectionSpecs(listOf(spec))
        sslSocketFactory(factory, trustManger)
    }
    return client.build()
}

private fun getVersionInterceptor() = Interceptor {
    val url = it.request().url().newBuilder().addQueryParameter("version", "1.1").build()
    val request = it.request().newBuilder().url(url).build()
    it.proceed(request)
}

private inline fun <reified Api> createWebService(
    okHttpClient: OkHttpClient,
    url: String,
    gsonFactory: GsonConverterFactory? = null
): Api {
    val converter = gsonFactory ?: GsonConverterFactory.create()
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(converter)
        .build()
    return retrofit.create(Api::class.java)
}

private fun createGsonConverterFactory(graphDeserializer: GraphDeserializer): GsonConverterFactory {
    val gson = GsonBuilder()
        .registerTypeAdapter(GraphResponse::class.java, graphDeserializer)
        .create()
    return GsonConverterFactory.create(gson)
}

private fun getKeystore(context: Context): KeyStore {
    val ks = KeyStore.getInstance(KeyStore.getDefaultType())
    ks.load(null)

    val inputStream = context.resources.openRawResource(R.raw.test).buffered().use {
        val cf = CertificateFactory.getInstance("X.509")
        while (it.available() > 0) {
            val cert = cf.generateCertificate(it)
            ks.setCertificateEntry("fiddler" + it.available(), cert)
        }
    }
    return ks
}

private fun getSocketFactory(keyStore: KeyStore): Pair<SSLSocketFactory, X509TrustManager> {
    val sslContext = SSLContext.getInstance("SSL")
    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, null)
    sslContext.init(keyManagerFactory.keyManagers,trustManagerFactory.trustManagers, SecureRandom())
    return sslContext.socketFactory to trustManagerFactory.trustManagers[0] as X509TrustManager
}

private val spec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
    .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
    .cipherSuites(
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
        CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
        CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
        CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
    )
    .build()


