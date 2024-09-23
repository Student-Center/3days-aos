package com.weave.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.extension.noRippleClickable

@Composable
fun DaysJobToggleButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit,
    icon: Painter,
    text: String
) {
    val gradientColors = if (isChecked) listOf(
        Color(0xFFE1DDA5),
        Color(0xFFC6C277)
    ) else listOf(DaysTheme.colors.yellow50, DaysTheme.colors.yellow50)

    Box(
        modifier = modifier
            .size(96.dp)
            .background(
                brush = Brush.verticalGradient(gradientColors),
                shape = RoundedCornerShape(26.dp)
            )
            .border(
                width = if (isChecked) 5.dp else 3.dp,
                color = Color.White,
                shape = RoundedCornerShape(26.dp)
            )
            .noRippleClickable { onToggle(!isChecked) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                style = DaysTheme.typography.semiBold14.toTextStyle(),
                color = if (isChecked) DaysTheme.colors.white else DaysTheme.colors.grey400,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class JobToggleItem(
    val text: String,
    val resourceId: Int
)

@Preview(showBackground = true)
@Composable
fun DaysJobToggleButtonPreview() {
    val toggleItems = listOf(
        JobToggleItem("경영·사무", R.drawable.ic_business),
        JobToggleItem("금융·보험", R.drawable.ic_finance),
        JobToggleItem("연구·공학기술", R.drawable.ic_research),
        JobToggleItem("교육", R.drawable.ic_education),
        JobToggleItem("법률", R.drawable.ic_legal),
        JobToggleItem("경찰·소방·군인", R.drawable.ic_security),
        JobToggleItem("IT·소프트웨어", R.drawable.ic_tech),
        JobToggleItem("디자인·예술", R.drawable.ic_design),
        JobToggleItem("스포츠", R.drawable.ic_sports),
        JobToggleItem("보건·의료", R.drawable.ic_medical),
        JobToggleItem("프리랜서", R.drawable.ic_freelance),
        JobToggleItem("기타", R.drawable.ic_others),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.size(312.dp, 426.dp) // 크기 조정
    ) {
        items(toggleItems) { item ->
            var isChecked by remember { mutableStateOf(false) }

            DaysJobToggleButton(
                isChecked = isChecked,
                onToggle = { isChecked = it },
                icon = painterResource(id = item.resourceId),
                text = item.text
            )
        }
    }
}