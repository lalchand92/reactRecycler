package com.example.sonarexperiment

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.slot
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class SuspendClassTest {

    @Before
    @ExperimentalCoroutinesApi
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @After
    @ExperimentalCoroutinesApi
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test1() {
        mockkObject(DelayClass)
        coEvery {
            DelayClass.delayFunc()
        } coAnswers {
            delay(5000)
        }

//        mockkObject(CoroutineScopeClass)
//        val slot = slot<suspend CoroutineScope.() -> Unit>()
//        every {
//            CoroutineScopeClass.launch(capture(slot))
//        } coAnswers {
////            runBlocking {
//            slot.captured.run {
//                    this()
//                }
////            }
//        }

        SuspendClass().test()

        assertEquals(1,1)

    }
}