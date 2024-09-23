package com.weave.design_system.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme

enum class BtnType {
    Tall, Short
}

@Composable
fun DaysNextButton(
    modifier: Modifier = Modifier,
    message: String = "다음",
    type: BtnType = BtnType.Tall,
    isEnabled: Boolean = false,
    onClick: () -> Unit
) {
    Log.i("Keyboard", type.toString())

    val buttonHeight = when (type) {
        BtnType.Tall -> 90.dp
        BtnType.Short -> 68.dp
    }

    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = ButtonColors(
            containerColor = DaysTheme.colors.grey500,
            contentColor = DaysTheme.colors.white,
            disabledContainerColor = DaysTheme.colors.grey100,
            disabledContentColor = DaysTheme.colors.white,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
    ) {
        Text(
            text = message,
            style = DaysTheme.typography.semiBold18.toTextStyle(),
            textAlign = TextAlign.Center
        )
    }
}

//@Composable
//fun SampleCode(){
//    val context = LocalContext.current
//
//    var inputText by remember { mutableStateOf("") }
//    val isEnabled = inputText.length >= 2
//
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val isKeyboardVisible by keyboardAsState()
//    val buttonType = if (isKeyboardVisible == Keyboard.Opened) BtnType.Short else BtnType.Tall
//
//    DaysTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .imePadding()
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                TextField(
//                    value = inputText,
//                    onValueChange = { newText ->
//                        inputText = newText
//                    },
//                    label = { Text("Enter Text") },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(
//                        onDone = { keyboardController?.hide() }),
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            DaysNextButton(
//                message = "다음",
//                type = buttonType,
//                isEnabled = isEnabled,
//                onClick = {
//                    if (isEnabled) {
//                        Toast.makeText(context, "성공", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                modifier = Modifier
//                    .align(Alignment.BottomCenter)
//            )
//        }
//    }
//}