package com.weave.design_system.component.tooltip

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

class CustomTooltipPositionProvider(
    private val direction: TooltipDirection) : PopupPositionProvider
{
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x: Int
        val y: Int
        when (direction) {
            TooltipDirection.Top -> {
                x = anchorBounds.left + (anchorBounds.width / 2) - (popupContentSize.width / 2)
                y = anchorBounds.top - popupContentSize.height
            }
            TooltipDirection.Bottom -> {
                x = anchorBounds.left + (anchorBounds.width / 2) - (popupContentSize.width / 2)
                y = anchorBounds.bottom
            }
            TooltipDirection.Left -> {
                x = anchorBounds.left - popupContentSize.width
                y = anchorBounds.top + (anchorBounds.height / 2) - (popupContentSize.height / 2)
            }
            TooltipDirection.Right -> {
                x = anchorBounds.right
                y = anchorBounds.top + (anchorBounds.height / 2) - (popupContentSize.height / 2)
            }
        }
        return IntOffset(x, y)
    }
}