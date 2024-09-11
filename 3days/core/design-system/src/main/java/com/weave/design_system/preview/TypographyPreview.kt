package com.weave.design_system.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.ui.theme.DaysTypography

@Composable
fun DaysTypographyPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "enMedium16", style = DaysTypography.enMedium16)
        Text(text = "enMedium20", style = DaysTypography.enMedium20)
        Text(text = "medium14", style = DaysTypography.medium14)
        Text(text = "medium14Center", style = DaysTypography.medium14Center)
        Text(text = "medium16Right", style = DaysTypography.medium16Right)
        Text(text = "regular12", style = DaysTypography.regular12)
        Text(text = "regular14", style = DaysTypography.regular14)
        Text(text = "regular15", style = DaysTypography.regular15)
        Text(text = "semiBold14", style = DaysTypography.semiBold14)
        Text(text = "semiBold18", style = DaysTypography.semiBold18)
        Text(text = "semiBold24", style = DaysTypography.semiBold24)
        Text(text = "semiBold28", style = DaysTypography.semiBold28)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDaysTypographyPreview() {
    Surface {
        DaysTypographyPreview()
    }
}