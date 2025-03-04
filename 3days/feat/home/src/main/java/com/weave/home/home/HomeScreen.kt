package com.weave.home.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable
import com.weave.home.R


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    moveToChat: () -> Unit
) {
    HomeScreenContent(moveToChat)
}

@Composable
private fun HomeScreenContent(
    moveToChat: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ConnectionBoard(moveToChat)

        TextButton(
            modifier = Modifier.border(
                color = DaysTheme.colors.grey500,
                width = 1.dp
            ),
            onClick = moveToChat
        ) {
            Text(
                text = "채팅방 입장",
                style = DaysTheme.typography.medium16.copy(fontSize = 20.dp).toTextStyle(),
                color = DaysTheme.colors.grey500
            )
        }
    }
}

@Composable
private fun ConnectionBoard(
    moveToChat: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 42.dp)
//                .background()
        ) {

        }

//        Image(
//            modifier = Modifier.noRippleClickable {
//                moveToChat()
//            },
//            painter = painterResource(id = R.drawable.ic_sticker),
//            contentDescription = "sticker"
//        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFF5F1EE)
private fun HomeScreenPreview() {

    HomeScreenContent(){}
}