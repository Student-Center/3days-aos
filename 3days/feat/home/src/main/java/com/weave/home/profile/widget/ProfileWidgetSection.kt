package com.weave.home.profile.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.extension.noRippleClickable
import com.weave.model.domain.user.ProfileWidget
import com.weave.model.domain.user.ProfileWidgetType

@Composable
fun ProfileWidgetSection(
    modifier: Modifier = Modifier,
    profileWidgets: List<ProfileWidget>,
    onWidgetEdit: (ProfileWidget) -> Unit = {},
    onWidgetDelete: (ProfileWidgetType) -> Unit = {},
    onBlankWidgetClick: () -> Unit = {}
) {
    var selectedWidget by remember { mutableStateOf<ProfileWidget?>(null) }
    var popupOffset by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Introduction",
            style = DaysTheme.typography.enMedium16.toTextStyle(),
            color = DaysTheme.colors.grey400,
            modifier = Modifier.padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (profileWidgets.isEmpty()) {
            WidgetInformation()
            Spacer(modifier = Modifier.height(14.dp))
            BlankWidgetItem(
                isSingle = true,
                onClick = onBlankWidgetClick
            )
            return
        }

        val isFullWidget = profileWidgets.size == ProfileWidgetType.entries.size
        val maxHeight = screenHeightCalculator(ProfileWidgetType.entries)

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false
            ) {
                items(
                    profileWidgets.size,
                    key = { index -> profileWidgets[index].type }) { index ->
                    ProfileWidgetItem(
                        widgetType = profileWidgets[index].type,
                        content = profileWidgets[index].content,
                        onClick = {
                            selectedWidget = profileWidgets[index]
                            popupOffset = it
                        }
                    )
                }

                if (!isFullWidget) {
                    item {
                        BlankWidgetItem(onClick = onBlankWidgetClick)
                    }
                }

                if(profileWidgets.size % 2 == 0) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        WidgetPopupMenu(
            widget = selectedWidget,
            offset = with(density) {
                DpOffset(
                    popupOffset.x.toDp(),
                    popupOffset.y.toDp() + 100.dp
                )
            },
            onDismiss = { selectedWidget = null },
            onEdit = { widget ->
                onWidgetEdit(widget)
                selectedWidget = null
            },
            onDelete = { widget ->
                onWidgetDelete(widget.type)
                selectedWidget = null
            }
        )
    }
}

@Composable
private fun WidgetPopupMenu(
    widget: ProfileWidget?,
    offset: DpOffset,
    onDismiss: () -> Unit,
    onEdit: (ProfileWidget) -> Unit,
    onDelete: (ProfileWidget) -> Unit
) {
    if (widget == null) return

    DropdownMenu(
        modifier = Modifier
            .size(108.dp, 92.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(10.dp),
        containerColor = Color.White,
        expanded = true,
        onDismissRequest = onDismiss,
        offset = offset
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { onEdit(widget) },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "수정하기",
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = DaysTheme.colors.black
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { onDelete(widget) },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "삭제하기",
                style = DaysTheme.typography.medium14.toTextStyle(),
                color = DaysTheme.colors.red300
            )
        }
    }
}

@Composable
private fun screenHeightCalculator(list: List<Any>): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    return remember(list) {
        ((screenWidth.value / 2) * (list.size / 2 + 1)).dp
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileWidgetSectionPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        ProfileWidgetSection(
            profileWidgets = ProfileWidgetType.entries.take(3).map {
                ProfileWidget(it, it.getExample())
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyProfileWidgetSectionPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        ProfileWidgetSection(profileWidgets = emptyList())
    }
}