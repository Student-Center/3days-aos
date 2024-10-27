package com.weave.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable

@Composable
fun DaysCheckBox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    size: Dp = 14.dp,
    checkedCheckmarkColor: Color = DaysTheme.colors.white,
    unCheckedCheckmarkColor: Color = Color.Transparent,
    checkedBoxColor: Color = DaysTheme.colors.blue300,
    unCheckedBoxColor: Color = Color.Transparent,
    checkedBorderColor: Color = DaysTheme.colors.blue300,
    unCheckedBorderColor: Color = DaysTheme.colors.grey200,
    borderSize: Dp = 1.dp
) {
    val checkmarkColor = if (checked) checkedCheckmarkColor else unCheckedCheckmarkColor
    val boxColor = if (checked) checkedBoxColor else unCheckedBoxColor
    val borderColor = if (checked) checkedBorderColor else unCheckedBorderColor

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(2.dp))
            .background(boxColor)
            .border(
                width = borderSize,
                color = borderColor,
                shape = RoundedCornerShape(2.dp)
            )
            .noRippleClickable {
                onCheckedChange()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            tint = checkmarkColor,
            contentDescription = "CheckBox Icon"
        )
    }
}

@Preview(showBackground = true, group = "DaysCheckBox")
@Composable
private fun DaysCheckBoxStatesPreview() {
    Box(
        modifier = Modifier.size(width = 100.dp, height = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DaysCheckBox(checked = false, onCheckedChange = {})
            DaysCheckBox(checked = true, onCheckedChange = {})
        }
    }
}