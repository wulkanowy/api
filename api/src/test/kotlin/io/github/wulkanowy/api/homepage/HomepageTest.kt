package io.github.wulkanowy.api.homepage

import io.github.wulkanowy.api.BaseLocalTest
import io.github.wulkanowy.api.home.LuckyNumber
import io.github.wulkanowy.api.repository.HomepageRepository
import io.github.wulkanowy.api.service.HomepageService
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomepageTest : BaseLocalTest() {

    private val repo by lazy {
        HomepageRepository(getService(HomepageService::class.java, "http://fakelog.localhost:3000/", false))
    }

    @Test
    fun getSelfGovernments() {
    }

    @Test
    fun getStudentsTrips() {
    }

    @Test
    fun getLastGrades() {
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("Index.html").readText()))
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("GetLastNotes.json").readText()))
        server.start(3000)

        val res = repo.getLastGrades().blockingGet()
        assertEquals(
            listOf(
                "j. angielski: 1, 6",
                "j. polski: 6, 1",
                "matematyka: 4+, -"
            ), res
        )
    }

    @Test
    fun getFreeDays() {
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("Index.html").readText()))
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("GetFreeDays.json").readText()))
        server.start(3000)

        val res = repo.getFreeDays().blockingGet()
        assertEquals(
            listOf(
                "Czwartek (20.06.2019) - Sobota (31.08.2019) - Ferie letnie",
                "Czwartek (15.08.2019) - Wniebowzięcie Najświętszej Maryi Panny"
            ), res
        )
    }

    @Test
    fun getKidsLuckyNumbers() {
    }

    @Test
    fun getKidsLessonPlan() {
    }

    @Test
    fun getLastHomework() {
    }

    @Test
    fun getLastTests() {
    }

    @Test
    fun getLastStudentLessons() {
    }

    @Test
    fun getLuckyNumber() {
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("Index.html").readText()))
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("GetKidsLuckyNumbers.json").readText()))
        server.start(3000)

        val number = repo.getLuckyNumber().blockingGet()
        assertEquals(18, number)
    }

    @Test
    fun getLuckyNumber_empty() {
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("Index.html").readText()))
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("GetEmpty.json").readText()))
        server.start(3000)

        val number = repo.getLuckyNumber().blockingGet()
        assertEquals(null, number)
    }

    @Test
    fun getLuckyNumber_list() {
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("Index.html").readText()))
        server.enqueue(MockResponse().setBody(HomepageTest::class.java.getResource("GetKidsLuckyNumbers.json").readText()))
        server.start(3000)

        val numbers = repo.getKidsLuckyNumbers().blockingGet()
        assertEquals(listOf(
            LuckyNumber("Szczęśliwy numer w dzienniku: 18", "SPL", 18),
            LuckyNumber("Szczęśliwy numer w dzienniku: 42", "T1", 42),
            LuckyNumber("Szczęśliwy numer w dzienniku bo nigdy nie wiadomo: 41", "T1", 41)
        ), numbers)

        server.takeRequest()
        with(server.takeRequest().body.readUtf8()) {
            assertTrue(startsWith("permissions=YRQQQJH"))
            assertTrue(endsWith("XzKdrhz9Ke9dkHzx"))
        }
    }
}
