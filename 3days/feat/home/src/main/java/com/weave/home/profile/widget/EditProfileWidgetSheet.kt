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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.component.NextButton
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType
import com.weave.utils.keyboardAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileWidgetSheet(
    sheetState: SheetState,
    profileWidget: ProfileWidget,
    onDismissRequest: () -> Unit,
    onEditRequest: (ProfileWidgetType, String) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    var widgetContent by remember { mutableStateOf(
        TextFieldValue(
            text = profileWidget.content,
            selection = TextRange(profileWidget.content.length)
        )
    ) }

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
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "프로필 위젯 수정",
                    style = DaysTheme.typography.semiBold20.toTextStyle(),
                    color = DaysTheme.colors.black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = Color.Transparent,
                    ),
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = "닫기",
                        style = DaysTheme.typography.medium16.toTextStyle(),
                        color = DaysTheme.colors.blue300,
                        textAlign = TextAlign.Center,
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
                            profileWidget.type.color.containerColor.map { Color(it) }
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
                            text = profileWidget.type.title,
                            style = DaysTheme.typography.semiBold20.copy(fontSize = 30.dp)
                                .toTextStyle(),
                            color = Color(profileWidget.type.color.textColor)
                        )
                    }

                    BasicTextField(
                        modifier = Modifier.focusRequester(focusRequester),
                        value = widgetContent,
                        onValueChange = { newValue ->
                            widgetContent =
                                if (newValue.text.length <= 40) newValue else newValue.copy(text = newValue.text.take(40))
                        },
                        cursorBrush = SolidColor(Color(profileWidget.type.color.textColor).copy(alpha = 0.6f)),
                        textStyle = DaysTheme.typography.regular14.copy(
                            fontSize = 18.dp,
                            color = Color(profileWidget.type.color.textColor).copy(alpha = 0.6f)
                        ).toTextStyle(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (widgetContent.text.length >= 5) {
                                    onEditRequest(profileWidget.type, widgetContent.text)
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
                    text = widgetContent.text.length.toString(),
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
                isEnabled = widgetContent.text.length >= 5 && profileWidget.content != widgetContent.text,
                padding = PaddingValues(),
                onClick = {
                    if (widgetContent.text.length >= 5 && profileWidget.content != widgetContent.text) {
                        onEditRequest(profileWidget.type, widgetContent.text)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EditProfileWidgetSheetPreview() {
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden })

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    EditProfileWidgetSheet(
        sheetState = sheetState,
        profileWidget = ProfileWidget(ProfileWidgetType.HOBBY, "init"),
        onDismissRequest = { scope.launch { sheetState.hide() } },
        onEditRequest = { _, _ -> }
    )
}