package com.weave.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.extension.noRippleClickable

@Composable
fun DaysGenderSelector(
    modifier: Modifier = Modifier,
    genderState: String,
    onChangedGender: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        listOf("남성" to true, "여성" to false).forEach { (gender, isMan) ->
            DaysGenderToggleButton(
                isChecked = genderState == gender,
                onCheckChanged = onChangedGender,
                isMan = isMan
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun DaysGenderToggleButton(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckChanged: (String) -> Unit,
    isMan: Boolean,
) {
    val colors: Pair<List<Color>, Color>
    val textIcon: Triple<String, Int, Color>

    if (isMan) {
        colors = Pair(
            listOf(Color(0xFFA1BA91), Color(0xFF586A4D)),
            DaysTheme.colors.green50
        )
        textIcon = Triple("남성", R.drawable.ic_man, DaysTheme.colors.green500)
    } else {
        colors = Pair(
            listOf(Color(0xFFD2A4B5), Color(0xFF8B5C6D)),
            DaysTheme.colors.pink50
        )
        textIcon = Triple("여성", R.drawable.ic_woman, DaysTheme.colors.pink500)
    }

    Box(
        modifier = modifier
            .size(130.dp)
            .background(
                brush = Brush.verticalGradient(
                    if (isChecked) colors.first else listOf(
                        colors.second,
                        colors.second
                    )
                ),
                shape = RoundedCornerShape(40.dp)
            )
            .border(
                width = if (isChecked) 8.dp else 4.dp,
                color = Color.White,
                shape = RoundedCornerShape(40.dp)
            )
            .noRippleClickable { onCheckChanged(textIcon.first) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = textIcon.second),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = textIcon.first,
                style = DaysTheme.typography.semiBold20.toTextStyle(),
                color = if (isChecked) DaysTheme.colors.white else textIcon.third,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaysGenderSelectorPreview() {
    var genderState by remember { mutableStateOf("") }

    DaysGenderSelector(
        genderState = genderState,
        onChangedGender = { genderState = it }
    )
}
