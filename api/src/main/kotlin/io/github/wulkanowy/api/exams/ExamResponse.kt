package io.github.wulkanowy.api.exams

import com.google.gson.annotations.SerializedName
import pl.droidsonroids.jspoon.annotation.Format
import pl.droidsonroids.jspoon.annotation.Selector
import java.util.Date

class ExamResponse {

    @SerializedName("SprawdzianyGroupedByDayList")
    var weeks: List<ExamDay> = emptyList()

    @Selector(".mainContainer > div:has(h2)")
    var days: List<ExamDay> = emptyList()

    class ExamDay {

        @SerializedName("Data")
        @Format("dd.MM.yyyy")
        @Selector("h2", regex = ".+, (.+)", defValue = "01.01.1970")
        lateinit var date: Date

        @SerializedName("Sprawdziany")
        @Selector("article")
        lateinit var exams: List<Exam>
    }
}
