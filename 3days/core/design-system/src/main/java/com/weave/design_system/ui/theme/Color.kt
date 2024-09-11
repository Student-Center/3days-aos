package com.weave.design_system.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

class DaysColor {

    companion object {

        private fun hexToColor(hex: String): Color {
            return Color(android.graphics.Color.parseColor(hex))
        }

        @Stable
        val Black
            get() = hexToColor("#1F1F1F")

        @Stable
        val White
            get() = hexToColor("#FFFFFF")

        @Stable
        val Grey500
            get() = hexToColor("#454545")

        @Stable
        val Grey400
            get() = hexToColor("#5E5E5E")

        @Stable
        val Grey300
            get() = hexToColor("#848484")

        @Stable
        val Grey200
            get() = hexToColor("#A0A0A0")

        @Stable
        val Grey100
            get() = hexToColor("#CAC7C5")

        @Stable
        val GradientA: List<Color>
            get() = listOf(hexToColor("#FF8BAC"), hexToColor("#98C1FF"))

        @Stable
        val Red300
            get() = hexToColor("#F2597F")

        @Stable
        val Blue300
            get() = hexToColor("#408CFF")

        @Stable
        val DarkGreen
            get() = hexToColor("#5B6654")

        @Stable
        val DarkPink
            get() = hexToColor("#846470")

        @Stable
        val DarkBlue
            get() = hexToColor("#606D8F")

        @Stable
        val LightYellow
            get() = hexToColor("#FEFDED")

        @Stable
        val LightGreen
            get() = hexToColor("#F0F6EB")

        @Stable
        val LightPink
            get() = hexToColor("#F6EBEF")

        @Stable
        val LightBlue
            get() = hexToColor("#EBF1F6")

        @Stable
        val Background
            get() = hexToColor("#F5F1EE")
    }
}
