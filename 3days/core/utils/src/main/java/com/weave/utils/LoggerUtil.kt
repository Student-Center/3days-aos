package com.weave.utils

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object LoggerUtil {

    init {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(5)
            .tag("DaysLog")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    fun debug(message: String) = Logger.d(message)

    fun info(message: String) = Logger.i(message)

    fun warning(message: String) = Logger.w(message)

    fun error(message: String, exception: Exception? = null) = Logger.e(message, exception)
}