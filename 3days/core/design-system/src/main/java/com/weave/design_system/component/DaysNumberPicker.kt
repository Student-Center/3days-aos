package com.weave.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.weave.design_system.DaysTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun Picker(
    items: List<String>,
    state: PickerState = rememberPickerState(),
    visibleItemsCount: Int = 7,
    listStartIndex: Int = 0,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    val visibleItemsMiddle = visibleItemsCount / 2

    val newItems = buildList {
        addAll(List(visibleItemsMiddle) { "" })
        add("상관없어요")
        addAll(items)
        addAll(List(visibleItemsMiddle) { "" })
    }

    fun getItem(index: Int) = newItems[index % newItems.size]

    val listState =
        rememberLazyListState(initialFirstVisibleItemIndex = if (listStartIndex == 0) 0 else listStartIndex + 1)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(newItems.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (getItem(index) == state.selectedItem) Color.Black.copy(alpha = 0.04f) else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getItem(index),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = textStyle,
                    )
                }
            }
        }
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

@Preview
@Composable
private fun PickerPreview() {
    PickerExample()
}

@Composable
fun PickerExample() {
    val values = remember { (0..15).map { it.toString() } }
    val valuesPickerState = rememberPickerState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DaysTheme.colors.bgDefault),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(start = 24.dp, end = 24.dp, bottom = 113.dp)
        ) {
            Picker(
                state = valuesPickerState,
                items = values,
                visibleItemsCount = 7,
                textStyle = TextStyle(fontSize = 22.sp)
            )
        }
    }
}