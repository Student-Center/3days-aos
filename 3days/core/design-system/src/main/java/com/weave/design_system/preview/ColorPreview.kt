package com.weave.design_system.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.ui.theme.DaysColor
import com.weave.design_system.ui.theme.DaysTypography

@Composable
fun ColorPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ColorBox("Black", DaysColor.Black)
        ColorBox("White", DaysColor.White)
        ColorBox("Grey500", DaysColor.Grey500)
        ColorBox("Grey400", DaysColor.Grey400)
        ColorBox("Grey300", DaysColor.Grey300)
        ColorBox("Grey200", DaysColor.Grey200)
        ColorBox("Grey100", DaysColor.Grey100)
        GradientPreview("GradientA", DaysColor.GradientA)
        ColorBox("Red300", DaysColor.Red300)
        ColorBox("Blue300", DaysColor.Blue300)
        ColorBox("DarkGreen", DaysColor.DarkGreen)
        ColorBox("DarkPink", DaysColor.DarkPink)
        ColorBox("DarkBlue", DaysColor.DarkBlue)
        ColorBox("LightYellow", DaysColor.LightYellow)
        ColorBox("LightGreen", DaysColor.LightGreen)
        ColorBox("LightPink", DaysColor.LightPink)
        ColorBox("LightBlue", DaysColor.LightBlue)
        ColorBox("Background", DaysColor.Background)
    }
}

@Composable
fun ColorBox(name: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 4.dp)
            .background(color)
            .padding(8.dp)
    ) {
        Text(
            text = name,
            color = if (color == DaysColor.Black) DaysColor.White else DaysColor.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun GradientPreview(name: String, colors: List<Color>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 4.dp)
            .background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(colors)
            )
            .padding(8.dp)
    ) {
        Text(
            text = name,
            color = DaysColor.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ColorPreviewPreview() {
    Surface {
        ColorPreview()
    }
}