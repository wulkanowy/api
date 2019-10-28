package io.github.wulkanowy.api.service

import io.github.wulkanowy.api.Api
import io.github.wulkanowy.api.ApiException
import io.github.wulkanowy.api.BaseTest
import io.github.wulkanowy.api.OkHttpClientBuilderFactory
import io.github.wulkanowy.api.interceptor.ErrorInterceptorTest
import io.github.wulkanowy.api.login.LoginTest
import io.github.wulkanowy.api.notes.NotesResponse
import io.github.wulkanowy.api.notes.NotesTest
import io.github.wulkanowy.api.register.Student
import io.reactivex.observers.TestObserver
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.URL

class ServiceManagerTest : BaseTest() {

    lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
    }

    @After
    fun shutDown() {
        server.shutdown()
    }

    @Test
    fun interceptorTest() {
        val manager = ServiceManager(OkHttpClientBuilderFactory(), HttpLoggingInterceptor.Level.NONE,
                Api.LoginType.STANDARD, "http", "fakelog.localhost:3000", "default", "email", "password",
                "schoolSymbol", 123, 101, null, "", ""
        )
        manager.setInterceptor(Interceptor {
            throw ApiException("Test")
        })

        val notes = manager.getSnpService().getNotes()
        val observer = TestObserver<NotesResponse>()
        notes.subscribe(observer)
        observer.assertTerminated()
        observer.assertNotComplete()
        observer.assertError(ApiException::class.java)
    }

    @Test
    fun interceptorTest_prepend() {
        server.enqueue(MockResponse().setBody(NotesTest::class.java.getResource("UwagiOsiagniecia-filled.html").readText()))
        server.start(3000)
        val manager = ServiceManager(OkHttpClientBuilderFactory(), HttpLoggingInterceptor.Level.NONE,
                Api.LoginType.STANDARD, "http", "fakelog.localhost:3000", "default", "email", "password",
                "schoolSymbol", 123, 101, null, "", ""
        )
        manager.setInterceptor(Interceptor {
            throw IOException("Test")
        })
        manager.setInterceptor(Interceptor {
            throw ApiException("Test")
        }, false, 0)

        val notes = manager.getSnpService().getNotes()
        val observer = TestObserver<NotesResponse>()
        notes.subscribe(observer)
        observer.assertTerminated()
        observer.assertNotComplete()
        observer.assertError(ApiException::class.java)
    }

    @Test
    fun apiNormalizedSymbol_blank() {
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("LoginPage-standard.html").readText().replace("fakelog.cf", "fakelog.localhost:3000")))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-uonet.html").readText()))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-brak-dostepu.html").readText()))
        server.enqueue(MockResponse().setBody(ErrorInterceptorTest::class.java.getResource("Offline.html").readText()))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-brak-dostepu.html").readText()))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-brak-dostepu.html").readText()))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-brak-dostepu.html").readText()))
        server.enqueue(MockResponse().setBody(LoginTest::class.java.getResource("Logowanie-brak-dostepu.html").readText()))
        server.start(3000)

        val api = Api().apply {
            logLevel = HttpLoggingInterceptor.Level.BASIC
            ssl = false
            host = "fakelog.localhost:3000"
            email = "jan@fakelog.cf"
            password = "jan123"
            symbol = ""
        }

        val pupils = api.getStudents()
        val observer = TestObserver<List<Student>>()
        pupils.subscribe(observer)
        observer.assertTerminated()
        observer.assertError(ApiException::class.java)

        server.takeRequest()
        // /Default/Account/LogOn <– default symbol set
        assertEquals("/Default/Account/LogOn", URL(server.takeRequest().requestUrl.toString()).path)
    }
}
