package com.android.appcomponents.util

import android.content.Context
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.toByteString
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

object SSLCertificate {


    fun sendOkHttpPinned(hostname : String,sha256Key : String) : OkHttpClient? {

        var client : OkHttpClient? = null
            try {
                val certificatePinner = CertificatePinner.Builder()
                    .add(hostname, "sha256/${sha256Key}")
                    .build()

                 client = OkHttpClient.Builder()
                    .certificatePinner(certificatePinner)
                    .build()
            } catch (e: Throwable) {
                println(e)
            }
        return client
        }

    fun sendVolleyPinned(resourceId : Int,context : Context) : SSLSocketFactory {

        val sslContext = SSLContext.getInstance("TLS")
        try {
            val cf = CertificateFactory.getInstance("X.509")
            val caStream = BufferedInputStream(context.resources.openRawResource(resourceId))
            val ca = cf.generateCertificate(caStream)
            caStream.close()

            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            val trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)
            trustManagerFactory.init(keyStore)
            sslContext.init(null, trustManagerFactory.trustManagers, null)
        } catch (e: Throwable) {
            println(e)

        }
        return  sslContext.socketFactory
    }

    fun sendManuallyCustomPinned(key256HashCode:String,hostName:String,postNumber:Int):Boolean {
             var status :Boolean = false
            try {
                // Disable trust manager checks - we'll check the certificate manually ourselves later
                val trustManager = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return null
                    }

                    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                })

                val context = SSLContext.getInstance("TLS")
                context.init(null, trustManager, null)

                val socket = context.socketFactory.createSocket(hostName, postNumber) as SSLSocket

                val certs = socket.session.peerCertificates

                if (!certs.any { cert -> doesCertMatchPin(key256HashCode, cert) }) {
                    socket.close() // Close the socket immediately without sending a request
                    throw Error("Unrecognized cert hash.")
                }

                // Send a real request, just to make it clear that we trust the connection:
                val pw = PrintWriter(socket.outputStream)
                pw.println("GET / HTTP/1.1")
                pw.println("Host: $hostName")
                pw.println("")
                pw.flush()

                val br = BufferedReader(InputStreamReader(socket.inputStream))
                val responseLine = br.readLine()
                println("Response was: $responseLine")
                if(responseLine.isNotEmpty() && responseLine.contains("200") ){
                    status = true
                }
                socket.close()

            } catch (e: Throwable) {
                println(e)
            }
        return status
    }
    private fun doesCertMatchPin(pin: String, cert: Certificate): Boolean {
        val certHash = cert.publicKey.encoded.toByteString().sha256()
        return certHash == pin.decodeBase64()
    }

}