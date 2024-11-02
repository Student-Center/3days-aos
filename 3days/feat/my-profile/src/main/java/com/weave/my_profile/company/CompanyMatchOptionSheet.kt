package com.weave.my_profile.company

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyMatchOptionSheet(
    onClickCancel: () -> Unit,
    onClickConfirmTrue: () -> Unit,
    onClickConfirmFalse: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    ModalBottomSheet(
        onDismissRequest = onClickCancel,
        sheetState = modalBottomSheetState,
        dragHandle = { }
    ) {
        Column(
            modifier = Modifier
                .background(DaysTheme.colors.white)
                .padding(start = 28.dp, end = 28.dp, top = 30.dp, bottom = bottomPadding)
                .fillMaxWidth()
        ) {
            BottomSheetText(
                textId = R.string.my_profile_company_bottom_sheet_sub_title,
                color = DaysTheme.colors.grey200,
                style = DaysTheme.typography.regular14.toTextStyle()
            )

            Spacer(modifier = Modifier.height(2.dp))

            BottomSheetText(
                textId = R.string.my_profile_company_bottom_sheet_title,
                color = DaysTheme.colors.grey500,
                style = DaysTheme.typography.semiBold20.toTextStyle()
            )

            Spacer(modifier = Modifier.height(42.dp))

            BottomSheetButton(
                textId = R.string.my_profile_company_bottom_sheet_button_false,
                onClick = onClickConfirmFalse,
                backgroundColor = DaysTheme.colors.red300,
                textColor = DaysTheme.colors.white
            )

            Spacer(modifier = Modifier.height(8.dp))

            BottomSheetButton(
                textId = R.string.my_profile_company_bottom_sheet_button_true,
                onClick = onClickConfirmTrue,
                backgroundColor = DaysTheme.colors.grey50,
                textColor = DaysTheme.colors.grey400
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun BottomSheetText(
    textId: Int,
    color: Color,
    style: TextStyle
) {
    Text(
        text = stringResource(id = textId),
        style = style,
        color = color
    )
}

@Composable
private fun BottomSheetButton(
    textId: Int,
    onClick: () -> Unit,
    backgroundColor: Color,
    textColor: Color
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor),
        onClick = onClick
    ) {
        Text(
            text = stringResource(id = textId),
            style = DaysTheme.typography.medium16.toTextStyle(),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun CompanyMatchOptionSheetPreview() {
    CompanyMatchOptionSheet(
        onClickCancel = {},
        onClickConfirmTrue = {},
        onClickConfirmFalse = {}
    )
}