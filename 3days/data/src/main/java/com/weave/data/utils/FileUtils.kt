package com.weave.data.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object FileUtils {
    fun File.toRequestBody(): RequestBody {
        return this.asRequestBody("image/png".toMediaType())
    }

    fun File.toMultipartBody(name: String = "file"): MultipartBody.Part {
        val requestBody = this.toRequestBody()
        return MultipartBody.Part.createFormData(name, this.name, requestBody)
    }
}