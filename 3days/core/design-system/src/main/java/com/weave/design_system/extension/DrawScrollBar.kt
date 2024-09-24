package com.weave.design_system.extension

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * LazyColumn을 위한 사용자 정의 스크롤바를 그리기 위한 확장 함수입니다.
 *
 * @param state LazyListState로, lazy list의 상태를 포함합니다.
 * @param barColor 스크롤바의 색상입니다.
 * @param barWidth 스크롤바의 너비입니다.
 * @param barBottomPadding 스크롤바의 하단 패딩입니다.
 */
fun Modifier.drawScrollbar(
    state: LazyListState,
    barColor: Color = Color.Gray,
    barWidth: Dp = 4.dp,
    barBottomPadding: Dp = 0.dp
) = drawBehind {
    val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
    val lastVisibleElementIndex = state.layoutInfo.visibleItemsInfo.lastOrNull()?.index

    val needScrollbar = state.layoutInfo.totalItemsCount > (lastVisibleElementIndex ?: 0)

    if (needScrollbar) {
        val elementHeight = (this.size.height - barBottomPadding.toPx()) / state.layoutInfo.totalItemsCount
        val scrollbarOffsetY = firstVisibleElementIndex!! * elementHeight
        val scrollbarHeight = (lastVisibleElementIndex!! - firstVisibleElementIndex + 1) * elementHeight

        drawRoundRect(
            color = barColor,
            topLeft = Offset(this.size.width - barWidth.toPx(), scrollbarOffsetY),
            size = Size(barWidth.toPx(), scrollbarHeight),
            cornerRadius = CornerRadius(barWidth.toPx() / 2)
        )
    }
}
