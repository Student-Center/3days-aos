package com.weave.design_system.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.foundation.DaysColorScheme
import com.weave.design_system.foundation.pressedColorFor

@Preview(showBackground = true)
@Composable
fun ColorPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        with(DaysColorScheme()){
            ColorBox("Black", black)
            ColorBox("White", white)
            ColorBox("Grey500", grey500)
            ColorBox("Grey400", grey400)
            ColorBox("Grey300", grey300)
            ColorBox("Grey200", grey200)
            ColorBox("Grey100", grey100)
            GradientColorBox("GradientA", gradientA)
            ColorBox("Green500", green500)
            ColorBox("Pink500", pink500)
            ColorBox("Blue500", blue500)
            ColorBox("Red300", red300)
            ColorBox("Blue300", blue300)
            ColorBox("Yellow100", yellow100)
            ColorBox("Green100", green100)
            ColorBox("Pink100", pink100)
            ColorBox("Yellow50", yellow50)
            ColorBox("Green50", green50)
            ColorBox("Pink50", pink50)
            ColorBox("Blue50", blue50)
            ColorBox("Background Default", bgDefault)
            ColorBox("Background Green", bgSplashGreen)
            ColorBox("Background Brown", bgSplashBrown)
            ColorBox("Background Pink", bgSplashPink)
            ColorBox("Background Purple", bgSplashPurple)
        }
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
            color = if (color == DaysTheme.colors.black) DaysTheme.colors.white else DaysTheme.colors.black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun GradientColorBox(name: String, colors: List<Color>) {
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
            color = DaysTheme.colors.black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PressedButtonPreview() {
    val buttonColor = DaysTheme.colors.grey100
    val pressedColor = pressedColorFor(buttonColor)

    var isPressed by remember { mutableStateOf(false) }

    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            if (isPressed) pressedColor else buttonColor
        ),
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
    ) {
        Text("Press Me", color = Color.White)
    }
}
