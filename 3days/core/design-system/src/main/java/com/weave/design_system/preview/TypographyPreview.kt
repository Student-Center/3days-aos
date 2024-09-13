package com.weave.design_system.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme

@Composable
fun DaysTypographyPreview() {
    with(DaysTheme.typography){
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "enMedium16", style = enMedium16.toTextStyle())
            Text(text = "enMedium20", style = enMedium20.toTextStyle())

            Text(text = "medium14", style = medium14.toTextStyle())
            Text(text = "medium16", style = medium16.toTextStyle())
            Text(text = "medium18", style = medium18.toTextStyle())

            Text(text = "regular12", style = regular12.toTextStyle())
            Text(text = "regular14", style = regular14.toTextStyle())
            Text(text = "regular15", style = regular15.toTextStyle())

            Text(text = "semiBold14", style = semiBold14.toTextStyle())
            Text(text = "semiBold18", style = semiBold18.toTextStyle())
            Text(text = "semiBold24", style = semiBold24.toTextStyle())
            Text(text = "semiBold28", style = semiBold28.toTextStyle())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDaysTypographyPreview() {
    Surface {
        DaysTypographyPreview()
    }
}