package io.github.wulkanowy.api.lucky_number

import pl.droidsonroids.jspoon.annotation.Selector

class LuckyNumberResponse {

    @Selector(".panel.szczesliweNumery .subDiv:not(.pCont)", regex = "(\\d+)", defValue = "0")
    var luckyNumer: Int = 0

}
