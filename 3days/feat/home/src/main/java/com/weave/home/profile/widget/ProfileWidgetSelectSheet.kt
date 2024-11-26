package com.weave.home.profile.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.model.domain.user.ProfileWidgetType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileWidgetSelectSheet(
    sheetState: SheetState,
    myProfileWidgets: List<ProfileWidgetType>,
    onDismissRequest: () -> Unit,
    onSelectedType: (ProfileWidgetType) -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null,
        containerColor = Color.White,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "프로필 위젯 추가",
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
                text = "추가하고 싶은 프로필 위젯을 눌러 소개를 작성해요!",
                style = DaysTheme.typography.regular14.toTextStyle(),
                color = DaysTheme.colors.grey500,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Box {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }

                    itemsIndexed(
                        ProfileWidgetType.entries,
                        key = { _, item -> item.title }) { _, item ->
                        AddWidgetItem(
                            widgetType = item,
                            onClick = onSelectedType,
                            alreadyDone = myProfileWidgets.contains(item)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.White,
                                    Color.White.copy(alpha = 0.1f)
                                )
                            )
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProfileWidgetSelectSheetPreview() {
    val sheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden })

    val scope = rememberCoroutineScope()

    val myProfileWidgets = listOf(
        ProfileWidgetType.HOBBY,
        ProfileWidgetType.MUSIC
    )

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    ProfileWidgetSelectSheet(
        sheetState = sheetState,
        myProfileWidgets = myProfileWidgets,
        onDismissRequest = { scope.launch { sheetState.hide() } },
        onSelectedType = {}
    )
}