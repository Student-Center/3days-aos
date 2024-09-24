package com.weave.design_system.extension

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.weave.design_system.rule.DaysShadow
import com.weave.design_system.rule.DaysShadowValue

/**
* 컴포저블에 그림자 효과를 추가합니다.
*
* 이 수정자를 사용하면 다양한 사용자 정의 옵션을 사용하여 구성 가능한 객체 뒤에 그림자를 그릴 수 있습니다.
*
* @param shape 그림자의 모양.
* @param color 그림자의 색상.
* @param blur 그림자의 흐림 반경
* @param offsetY Y축을 따라 그림자 오프셋.
* @param offsetX X축을 따라 그림자 오프셋.
* @param spread 그림자 크기를 늘리는 양입니다.
*
* @return 그림자 효과가 적용된 새로운 `Modifier`.
 */

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = this.drawBehind {

    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

/**
 * 정의된 `DaysShadow` 값을 기반으로 그림자를 적용합니다.
 *
 * @param shape 그림자의 모양.
 * @param shadowType 적용할 `DaysShadow` 유형.
 */
fun Modifier.applyShadow(
    shape: Shape,
    shadowType: DaysShadowValue = DaysShadow().default
): Modifier {
    return this.then(
        Modifier.dropShadow(
            shape = shape,
            color = shadowType.shadowColor,
            blur = shadowType.blur,
            offsetX = shadowType.offsetX,
            offsetY = shadowType.offsetY
        )
    )
}

