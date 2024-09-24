package com.weave.design_system.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class KoreanPhoneNumberVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val cleanText = text.text.replace(Regex("\\D"), "")

        val formattedText = when {
            cleanText.length < 4 -> "${cleanText.take(3)}-"
            cleanText.length < 7 -> "${cleanText.take(3)}-${cleanText.drop(3)}"
            cleanText.length < 11 -> "${cleanText.take(3)}-${cleanText.substring(3, 7)}-${cleanText.drop(7)}"
            else -> "${cleanText.take(3)}-${cleanText.substring(3, 7)}-${cleanText.drop(7).take(4)}"
        }

        val originalLength = text.text.length
        val transformedLength = formattedText.length

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset < cleanText.length) {
                    formattedText.substring(0, offset.coerceAtMost(formattedText.length)).count { it != '-' }
                } else {
                    transformedLength
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                var originalOffset = 0
                for (i in 0 until offset) {
                    if (i < formattedText.length) {
                        if (formattedText[i] != '-') {
                            originalOffset++
                        }
                    }
                }
                return originalOffset.coerceAtMost(originalLength)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}
