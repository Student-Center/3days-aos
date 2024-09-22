package com.weave.design_system.component.tooltip

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import kotlinx.coroutines.launch

enum class TooltipDirection {
    Top, Bottom, Left, Right
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaysTooltip(
    tooltipState: TooltipState,
    direction: TooltipDirection,
    tooltipText: AnnotatedString,
    content: @Composable () -> Unit
) {
    TooltipBox(
        state = tooltipState,
        positionProvider = remember {
            CustomTooltipPositionProvider(direction)
        },
        tooltip = {
            TooltipContent(direction = direction, text = tooltipText)
        },
        content = content
    )
}

@Composable
fun TooltipContent(direction: TooltipDirection, text: AnnotatedString) {
    when (direction) {
        TooltipDirection.Top, TooltipDirection.Bottom -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (direction == TooltipDirection.Bottom) {
                    TooltipArrow(direction)
                }
                TooltipTextBox(text)
                if (direction == TooltipDirection.Top) {
                    TooltipArrow(direction)
                }
            }
        }

        TooltipDirection.Left, TooltipDirection.Right -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (direction == TooltipDirection.Right) {
                    TooltipArrow(direction)
                }
                TooltipTextBox(text)
                if (direction == TooltipDirection.Left) {
                    TooltipArrow(direction)
                }
            }
        }
    }
}

@Composable
fun TooltipTextBox(text: AnnotatedString) {
    Box(
        modifier = Modifier
            .width(241.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(DaysTheme.colors.grey400)
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Text(
            modifier = Modifier
                .width(230.dp)
                .align(Alignment.Center),
            text = text,
            style = DaysTheme.typography.regular12.toTextStyle(),
            color = DaysTheme.colors.white,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TooltipArrow(direction: TooltipDirection) {
    Canvas(modifier = Modifier.size(10.dp)) {
        drawTooltipArrow(Color(0xFF5E5E5E), direction)
    }
}

fun DrawScope.drawTooltipArrow(color: Color, direction: TooltipDirection) {
    val path = Path().apply {
        when (direction) {
            TooltipDirection.Top -> {
                moveTo(0f, 0f)
                lineTo(size.width / 2, size.height)
                lineTo(size.width, 0f)
            }

            TooltipDirection.Bottom -> {
                moveTo(0f, size.height)
                lineTo(size.width / 2, 0f)
                lineTo(size.width, size.height)
            }

            TooltipDirection.Left -> {
                moveTo(size.width, size.height / 2)
                lineTo(0f, 0f)
                lineTo(0f, size.height)
            }

            TooltipDirection.Right -> {
                moveTo(0f, size.height / 2)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
            }
        }
        close()
    }
    drawPath(
        path = path,
        color = color
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TooltipPreview() {
    val coroutineScope = rememberCoroutineScope()
    val tooltipState = remember { TooltipState() }

    DaysTooltip(
        tooltipState = tooltipState,
        direction = TooltipDirection.Right,
        tooltipText = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.W600)) {
                append("1일차 시작! ")
            }
            append("큐피드 WEAVY\uD83E\uDDDA와 함께\n매칭된 상대와의 채팅에서 서로를 알아가요.")
        },
        content = {
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .padding(16.dp)
                    .clickable {
                        coroutineScope.launch { tooltipState.show() }
                    }
            ) {
                Text("Hover me")
            }
        }
    )
}
