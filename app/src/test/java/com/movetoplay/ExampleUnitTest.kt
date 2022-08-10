package com.movetoplay

import android.util.Log
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import com.movetoplay.domain.utils.TypesRegularExpressions
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val useCase = RegularExpressionsUseCase()
        println(useCase("jdwda@wdad.dw", TypesRegularExpressions.Email).toString())
        println(useCase("jdwda@wdad", TypesRegularExpressions.Email).toString())
        println(useCase("jdwda#wdad.dw", TypesRegularExpressions.Email).toString())
    }
}