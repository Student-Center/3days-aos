package com.weave.intro

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.applyShadow
import kotlinx.coroutines.delay

internal const val tweenDuration = 500
internal const val nextDuration = 750L

@Composable
fun IntroScreen(
    navController: NavController
) {
    var currentScreen by remember { mutableIntStateOf(0) }
    val backgroundColors = listOf(
        Color(0xFFDFE7D1),
        Color(0xFFD7D7EA),
        Color(0xFFECDAE3),
        DaysTheme.colors.background,
        DaysTheme.colors.background
    )

    val backgroundColorAnim by animateColorAsState(
        targetValue = backgroundColors[currentScreen],
        animationSpec = tween(durationMillis = tweenDuration),
        label = "Background Color Animation"
    )

    val context = LocalContext.current
    val window = (context as? ComponentActivity)?.window

    LaunchedEffect(Unit) {
        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
        }

        while (currentScreen < 4) {
            delay(nextDuration)
            currentScreen++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColorAnim)
            .systemBarsPadding()
    ) {
        when (currentScreen) {
            0 -> DayOneScreen()
            1 -> DayTwoScreen()
            2 -> DayThreeScreen()
            3 -> DayLayeredScreen()
            4 -> DaySpreadScreen {
                navController.navigate("mobile_auth")
            }
        }
    }
}

@Composable
fun DayImage(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String? = null,
    isApplyShadow: Boolean = true,
    alpha: Float = 1f,
    rotation: Float = 0f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .size(width = 90.dp, height = 103.dp)
            .offset(offsetX, offsetY)
            .rotate(rotation)
            .alpha(alpha)
            .applyShadow(
                shape = RoundedCornerShape(if (isApplyShadow) 0.dp else 30.dp),
                shadowType = DaysTheme.shadow.default.copy(
                    shadowColor = Color(
                        0x05000000
                    )
                )
            )
    )
}

@Composable
fun DayOneScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Day One Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_1),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_1_description),
            alpha = alpha,
            rotation = -5.47f
        )
    }
}

@Composable
fun DayTwoScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Day Two Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_1),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_1_description),
            rotation = -5.47f
        )
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_2),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_2_description),
            alpha = alpha,
            rotation = 4.98f
        )
    }
}

@Composable
fun DayThreeScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Day Three Alpha Animation"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_1),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_1_description),
            rotation = -5.47f
        )
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_2),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_2_description),
            rotation = 4.98f
        )
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_3),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_3_description),
            alpha = alpha,
            rotation = 15.52f
        )
    }
}

@Composable
fun DayLayeredScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    val rotation1 by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -5.47f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Rotation 1"
    )
    val rotation2 by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 4.98f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Rotation 2"
    )
    val rotation3 by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 15.52f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Rotation 3"
    )

    val offset1X by animateDpAsState(
        targetValue = if (startAnimation) (-28).dp else 0.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Offset 1X"
    )
    val offset1Y by animateDpAsState(
        targetValue = if (startAnimation) (-10).dp else 0.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Offset 1Y"
    )

    val offset3X by animateDpAsState(
        targetValue = if (startAnimation) 28.dp else 0.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Offset 3X"
    )
    val offset3Y by animateDpAsState(
        targetValue = if (startAnimation) 10.dp else 0.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Layered Screen Offset 3Y"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_1),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_1_description),
            rotation = rotation1,
            offsetX = offset1X,
            offsetY = offset1Y
        )
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_2),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_2_description),
            rotation = rotation2
        )
        DayImage(
            painter = painterResource(id = R.drawable.ic_day_3),
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_3_description),
            rotation = rotation3,
            offsetX = offset3X,
            offsetY = offset3Y
        )
    }
}


@Composable
fun DaySpreadScreen(
    onClicked: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = tweenDuration), label = "Spread Alpha Animation"
    )

    val offset1X by animateDpAsState(
        targetValue = if (startAnimation) (-56).dp else (-28).dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Spread Screen Offset 1X"
    )
    val offset1Y by animateDpAsState(
        targetValue = if (startAnimation) (-20).dp else (-10).dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Spread Screen Offset 1Y"
    )

    val offset3X by animateDpAsState(
        targetValue = if (startAnimation) 56.dp else 28.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Spread Screen Offset 3X"
    )
    val offset3Y by animateDpAsState(
        targetValue = if (startAnimation) 20.dp else 10.dp,
        animationSpec = tween(durationMillis = tweenDuration), label = "Spread Screen Offset 3Y"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .alpha(alpha), contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = com.weave.design_system.R.string.splash_title),
                style = DaysTheme.typography.semiBold24.toTextStyle(),
                color = DaysTheme.colors.grey500
            )
        }

        Spacer(modifier = Modifier.height(62.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DayImage(
                painter = painterResource(id = R.drawable.ic_day_1),
                contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_1_description),
                offsetX = offset1X,
                offsetY = offset1Y,
            )
            DayImage(
                painter = painterResource(id = R.drawable.ic_day_2),
                contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_2_description),
            )
            DayImage(
                painter = painterResource(id = R.drawable.ic_day_3),
                contentDescription = stringResource(id = com.weave.design_system.R.string.splash_day_3_description),
                offsetX = offset3X,
                offsetY = offset3Y,
            )
        }

        Spacer(modifier = Modifier.height(68.dp))

        Box(
            modifier = Modifier
                .alpha(alpha)
                .padding(horizontal = 50.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { onClicked() },
                shape = RoundedCornerShape(85.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .border(
                        BorderStroke(4.dp, color = DaysTheme.colors.white),
                        shape = RoundedCornerShape(85.dp)
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFF8BAC),
                                Color(0xFF98C1FF)
                            )
                        ),
                        shape = RoundedCornerShape(85.dp)
                    )
                    .applyShadow(
                        RoundedCornerShape(85.dp), DaysTheme.shadow.default.copy(
                            shadowColor = Color(0x4DC58BCC)
                        )
                    )
            ) {
                Text(
                    text = stringResource(id = com.weave.design_system.R.string.splash_button_start),
                    style = DaysTheme.typography.semiBold18.copy(fontSize = 16.dp)
                        .toTextStyle(),
                    textAlign = TextAlign.Center
                )
            }

            FadeInDropImage(
                modifier = Modifier
                    .size(width = (35 * 1.5).dp, height = (25 * 1.5).dp)
                    .rotate(-20.46f),
                painter = painterResource(id = R.drawable.ic_date_ticket)
            )
        }
    }
}

@Composable
fun FadeInDropImage(
    modifier: Modifier = Modifier,
    painter: Painter
) {
    var startAnimation by remember { mutableStateOf(false) }

    val offsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else -300f,
        animationSpec = tween(durationMillis = tweenDuration), label = "FadeInDropImage Offset Y"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = tweenDuration), label = "FadeInDropImage Alpha Y"
    )

    LaunchedEffect(Unit) {
        delay(500)
        startAnimation = true
    }

    Box(
        modifier = Modifier.padding(bottom = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(id = com.weave.design_system.R.string.splash_date_ticket_description),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .alpha(alpha)
                .offset { IntOffset(20, offsetY.dp.roundToPx()) }
                .applyShadow(
                    shape = RectangleShape,
                    shadowType = DaysTheme.shadow.default.copy(
                        shadowColor = Color(0x05000000)
                    )
                )
        )
    }
}
