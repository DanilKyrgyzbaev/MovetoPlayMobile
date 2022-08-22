package com.movetoplay

import android.util.Log
import com.movetoplay.data.model.AuthBody
import com.movetoplay.domain.model.ApplicationLock
import com.movetoplay.domain.model.TypeLock
import com.movetoplay.domain.use_case.RegularExpressionsUseCase
import com.movetoplay.domain.utils.TypesRegularExpressions
import com.movetoplay.presentation.utils.timeFormat
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
    @Test
    fun a1() {
        println(Json.encodeToString(ApplicationLock("VK", "app.vk.hz", TypeLock.ProhibitedUse)))
        println(Json.encodeToString(ApplicationLock("Viber", "com.viber", TypeLock.FreeUse)))
        println(Json.encodeToString(ApplicationLock("Гонки", "com.gongando", TypeLock.TimeUse(55))))
    }
}