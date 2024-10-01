package com.weave.design_system.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.R
import com.weave.design_system.utils.KoreanPhoneNumberVisualTransformation

@Composable
fun DaysPhoneTextField(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    phoneNumber: TextFieldValue,
    isValid: (Boolean) -> Unit,
    onTextChange: (TextFieldValue) -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = Color(0xFFF7F3F1), shape = RoundedCornerShape(100.dp))
                .padding(horizontal = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_flag_ko),
                    contentDescription = "flag of Korea"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "+82",
                    style = DaysTheme.typography.medium16.toTextStyle(),
                    color = DaysTheme.colors.grey200
                )
                Spacer(modifier = Modifier.width(16.dp))
                CompositionLocalProvider(
                    LocalTextSelectionColors provides TextSelectionColors(
                        handleColor = DaysTheme.colors.green100,
                        backgroundColor = DaysTheme.colors.green50
                    )
                ) {
                    BasicTextField(
                        value = if (phoneNumber.text.length <= 3 && phoneNumber.text != "010") TextFieldValue(
                            "010",
                            TextRange(3)
                        ) else phoneNumber,
                        onValueChange = { newValue ->
                            if (newValue.text.length <= 11) onTextChange(newValue)
                        },
                        cursorBrush = SolidColor(DaysTheme.colors.grey500),
                        singleLine = true,
                        visualTransformation = KoreanPhoneNumberVisualTransformation(),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        textStyle = DaysTheme.typography.medium16.toTextStyle()
                            .copy(color = DaysTheme.colors.grey500),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                innerTextField()
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))

                val phoneValid = isValidPhoneNumber(phoneNumber.text)
                isValid(phoneValid)

                if (phoneValid) {
                    ValidatedIcon()
                }
            }
        }
    }
}

@Composable
private fun ValidatedIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                color = Color(0x262DE76B),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            modifier = Modifier.size(20.dp),
            contentDescription = "Validated",
            tint = Color(0xFF2DE76B)
        )

    }
}

private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.length == 11 &&
            phoneNumber.startsWith("010") &&
            phoneNumber.all { it.isDigit() }
}

@Preview(showBackground = true)
@Composable
fun DaysPhoneTextFieldPreview() {
    val focusManager: FocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        DaysPhoneTextField(
            focusManager = focusManager,
            phoneNumber = TextFieldValue(""),
            isValid = {},
            onTextChange = {}
        )

        DaysPhoneTextField(
            focusManager = focusManager,
            phoneNumber = TextFieldValue("01012345678"),
            isValid = {},
            onTextChange = {}
        )
    }
}
