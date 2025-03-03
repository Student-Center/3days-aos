package com.weave.home.chat.resource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.home.R

@Composable
fun ChatInput(
    textState: String,
    focusManager: FocusManager,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val textStyle = DaysTheme.typography.regular15
    val iconColor = if (textState.isEmpty()) DaysTheme.colors.grey100 else DaysTheme.colors.grey500
    var isMultiLine by remember { mutableStateOf(false) }
    val radius = if (isMultiLine) 24.dp else 100.dp
    val maxHeight = 82.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(radius))
                .background(color = Color(0xFFF7F3F1))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    BasicTextField(
                        value = textState,
                        onTextLayout = { textLayoutResult ->
                            isMultiLine = textLayoutResult.lineCount > 1
                        },
                        onValueChange = { newValue ->
                            onTextChange(newValue)
                        },
                        cursorBrush = SolidColor(DaysTheme.colors.grey500),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = maxHeight)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        keyboardActions = KeyboardActions(
                            onSend = { focusManager.clearFocus() },
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Send,
                            keyboardType = KeyboardType.Text
                        ),
                        textStyle = textStyle.copy(color = DaysTheme.colors.grey500).toTextStyle(),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (textState.isEmpty()) {
                                    Text(
                                        text = "메세지 보내기",
                                        style = textStyle.copy(color = DaysTheme.colors.grey100).toTextStyle()
                                    )
                                } else {
                                    innerTextField()
                                }
                            }
                        }
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Bottom),
                    onClick = onSend
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = "",
                        tint = iconColor
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ChatInputPreview() {
    val focusManager: FocusManager = LocalFocusManager.current

    ChatInput(
        textState = "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트",
        focusManager = focusManager,
        onSend = {},
        onTextChange = {},
    )
}