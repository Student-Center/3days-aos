package com.weave.home.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.NextButton
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProfileWidgetSheet(
    sheetState: SheetState,
    profileWidgetType: ProfileWidgetType,
    onDismissRequest: () -> Unit,
    onAddRequest: (ProfileWidgetType, String) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    var widgetContent by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = profileWidgetType.titleWithoutEmoji,
                    style = DaysTheme.typography.semiBold20.toTextStyle(),
                    color = DaysTheme.colors.black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = onDismissRequest
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "",
                        tint = DaysTheme.colors.grey400,
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "최대 40자 이내로 자유롭게 작성해주세요!",
                style = DaysTheme.typography.regular14.toTextStyle(),
                color = DaysTheme.colors.grey500,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 75.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            profileWidgetType.color.containerColor.map { Color(it) }
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.matchParentSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = profileWidgetType.title,
                            style = DaysTheme.typography.semiBold20.copy(fontSize = 30.dp)
                                .toTextStyle(),
                            color = Color(profileWidgetType.color.textColor)
                        )
                    }

                    BasicTextField(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = widgetContent,
                        onValueChange = { newValue ->
                            widgetContent =
                                if (newValue.length <= 40) newValue else newValue.take(40)
                        },
                        cursorBrush = SolidColor(Color(profileWidgetType.color.textColor).copy(alpha = 0.6f)),
                        textStyle = DaysTheme.typography.regular14.copy(
                            fontSize = 18.dp,
                            color = Color(profileWidgetType.color.textColor).copy(alpha = 0.6f)
                        ).toTextStyle(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (widgetContent.length >= 5) {
                                    onAddRequest(profileWidgetType, widgetContent)
                                }
                            }
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) { innerTextField() }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = widgetContent.length.toString(),
                    style = DaysTheme.typography.regular15.toTextStyle(),
                    color = DaysTheme.colors.blue300
                )

                Text(
                    text = "/40",
                    style = DaysTheme.typography.regular15.toTextStyle(),
                    color = DaysTheme.colors.grey300
                )
            }

            NextButton(
                message = "다 썼어요",
                isKeyboardVisible = isKeyboardVisible,
                isEnabled = widgetContent.length >= 5,
                padding = PaddingValues(),
                onClick = {
                    if (widgetContent.length >= 5) {
                        onAddRequest(profileWidgetType, widgetContent)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AddProfileWidgetSheetPreview() {
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden })

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    AddProfileWidgetSheet(
        sheetState = sheetState,
        profileWidgetType = ProfileWidgetType.HOBBY,
        onDismissRequest = { scope.launch { sheetState.hide() } },
        onAddRequest = { _, _ -> }
    )
}