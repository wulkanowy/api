package io.github.wulkanowy.api.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ForceCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return with(request.url().encodedPath()) {
            when {
                contains("Oceny") -> chain.updateCacheHeaders(request, 5, TimeUnit.SECONDS)
                contains("SzkolaINauczyciele") -> chain.updateCacheHeaders(request, 5, TimeUnit.MINUTES)
                else -> chain.proceed(request)
            }
        }
    }

    private fun Interceptor.Chain.updateCacheHeaders(request: Request, maxAge: Int, timeUnit: TimeUnit): Response {
        return proceed(request).newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control",
                CacheControl.Builder()
                    .maxAge(maxAge, timeUnit)
                    .build().toString()
            )
            .build()
    }
}
