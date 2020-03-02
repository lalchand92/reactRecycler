package com.example.sonarexperiment

import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class SuspendClass {
    private  val MAX_THREADS_FOR_APPLICATION = 200
    private val CPU_COUNT = 4;//Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = (CPU_COUNT * 2).coerceAtLeast(6)
    private  val KEEP_ALIVE_MILLI_SECONDS = 100L

    private var executorServiceIO: ExecutorService = ThreadPoolExecutor(CORE_POOL_SIZE,
        MAX_THREADS_FOR_APPLICATION,
        KEEP_ALIVE_MILLI_SECONDS,
        TimeUnit.MILLISECONDS,
        LinkedBlockingDeque<Runnable>())

    /**
     * Context for IO tasks
     */
    val ioContext: CoroutineContext by lazy {
        SupervisorJob() + executorServiceIO.asCoroutineDispatcher()
    }

    /**
     * [CoroutineScope] for executing IO related tasks.
     */
    val taskIO: CoroutineScope by lazy {
        CoroutineScope(ioContext)
    }

    fun test(){
        CoroutineScopeClass.launch {
            println("test suspended: from test:${Thread.currentThread()}")
            suspendedFunc()
        }
    }

    suspend fun suspendedFunc(){
        println("test suspended: from suspendedFunc1:${Thread.currentThread()}")
//        delay(5000)
        DelayClass.delayFunc()
        println("test suspended: from suspendedFunc2:${Thread.currentThread()}")
    }
}

fun main() {
    SuspendClass().test()
}