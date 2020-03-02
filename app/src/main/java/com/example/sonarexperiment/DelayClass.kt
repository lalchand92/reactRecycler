package com.example.sonarexperiment

import kotlinx.coroutines.delay

object DelayClass{
    suspend fun delayFunc(){
        delay(5000)
    }
}