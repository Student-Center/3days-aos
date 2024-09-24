package com.weave.design_system.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weave.design_system.DaysTheme
import com.weave.design_system.DaysTheme.colors
import com.weave.design_system.DaysTheme.typography
import com.weave.design_system.extension.addFocusCleaner
import com.weave.design_system.extension.applyShadow
import com.weave.design_system.extension.drawScrollbar
import com.weave.design_system.extension.noRippleClickable

@Composable
fun DaysAutoCompleteTextField(
    modifier: Modifier = Modifier,
    suggestions: List<String>,
    selectedSuggestion: String,
    onSuggestionSelected: (String) -> Unit,
    placeholderText: String = "",
    allowDirectInput: Boolean = false,
    focusManager: FocusManager
) {
    var inputText by remember { mutableStateOf("") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var isFocusedState by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        DaysAutoCompleteInputField(
            text = inputText,
            onTextChange = { newText ->
                inputText = newText
                isDropdownVisible = newText.isNotBlank()
                onSuggestionSelected("")
            },
            selectedSuggestion = selectedSuggestion,
            isFocused = isFocusedState,
            placeholderText = placeholderText,
            onFocusChange = { isFocused ->
                isFocusedState = isFocused
                isDropdownVisible = isFocused && inputText.isNotBlank()
            },
            focusManager = focusManager
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (isDropdownVisible) {
            SuggestionDropdown(
                query = inputText,
                allowDirectInput = allowDirectInput,
                suggestions = suggestions.filter { it.contains(inputText, ignoreCase = true) },
                onSuggestionClick = { clickedSuggestion ->
                    inputText = clickedSuggestion
                    isDropdownVisible = false
                    onSuggestionSelected(clickedSuggestion)

                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Composable
private fun DaysAutoCompleteInputField(
    text: String,
    onTextChange: (String) -> Unit,
    selectedSuggestion: String,
    isFocused: Boolean,
    placeholderText: String,
    onFocusChange: (Boolean) -> Unit,
    focusManager: FocusManager
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .applyShadow(RoundedCornerShape(61.dp))
            .background(color = colors.white, shape = RoundedCornerShape(61.dp))
            .onFocusChanged { onFocusChange(it.isFocused) }
            .border(
                BorderStroke(if (isFocused) 1.dp else 0.dp, colors.grey100),
                shape = RoundedCornerShape(61.dp)
            )
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = colors.green100,
                    backgroundColor = colors.green50
                )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    cursorBrush = SolidColor(colors.grey500),
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    textStyle = typography.medium16.toTextStyle().copy(color = colors.grey500),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (text.isEmpty()) {
                                Text(
                                    text = placeholderText,
                                    color = colors.grey100,
                                    style = typography.medium16.toTextStyle()
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            SuggestionIcon(selected = selectedSuggestion.isNotBlank())
        }
    }
}

@Composable
private fun SuggestionIcon(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                color = if (selected) Color(0x262DE76B) else Color(0x26CAC7C5),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (selected) Icons.Default.Check else Icons.Default.Search,
            modifier = Modifier.size(20.dp),
            contentDescription = if (selected) "Selected" else "Search",
            tint = if (selected) Color(0xFF2DE76B) else colors.grey100
        )
    }
}

@Composable
private fun SuggestionDropdown(
    query: String,
    allowDirectInput: Boolean,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    if (suggestions.isNotEmpty() && query.isNotBlank()) {
        SuggestionList(suggestions, onSuggestionClick)
    } else if (allowDirectInput && query.isNotBlank()) {
        DirectInputOption(query, onSuggestionClick)
    }
}

@Composable
private fun SuggestionList(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    Box(modifier = Modifier.padding(bottom = 8.dp)) {
        DropdownWithScrollbar(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .applyShadow(RoundedCornerShape(24.dp))
                .background(color = colors.white, shape = RoundedCornerShape(24.dp))
                .padding(start = 24.dp, end = 24.dp, top = 14.dp),
            items = suggestions,
            onItemSelected = onSuggestionClick
        )
    }
}

@Composable
private fun DirectInputOption(
    query: String,
    onSuggestionClick: (String) -> Unit
) {
    Box(modifier = Modifier.padding(bottom = 8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .applyShadow(RoundedCornerShape(24.dp))
                .background(color = colors.white, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "'$query' 직접 입력",
                style = typography.medium16.toTextStyle(),
                color = colors.grey200,
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { onSuggestionClick(query) }
            )
        }
    }
}

@Composable
private fun DropdownWithScrollbar(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    val state = rememberLazyListState()
    val scrollBarColor = colors.grey100
    val scrollbarWidth = 4.dp

    LazyColumn(
        state = state,
        modifier = modifier
            .drawScrollbar(
                state = state,
                barColor = scrollBarColor,
                barWidth = scrollbarWidth,
                barBottomPadding = 18.dp
            )
            .padding(end = scrollbarWidth)
    ) {
        itemsIndexed(items) { index, item ->
            Text(
                text = item,
                style = typography.regular14.toTextStyle(),
                color = colors.grey500,
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { onItemSelected(item) }
                    .padding(
                        top = 4.dp,
                        bottom = if (index == items.size - 1) 18.dp else 0.dp
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DaysAutoCompleteTextFieldPreview() {
    val items = listOf(
        "Apple", "Banana", "Cherry", "Date", "Elderberry", "Apple",
        "Banana", "Cherry", "Date", "Elderberry", "Apple", "Banana", "Cherry", "Date", "Elderberry"
    )

    DaysTheme {
        val focusManager = LocalFocusManager.current
        var selectedSuggestion by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            DaysAutoCompleteTextField(
                suggestions = items,
                selectedSuggestion = selectedSuggestion,
                onSuggestionSelected = { selectedItem ->
                    selectedSuggestion = selectedItem

                    println("Selected: $selectedSuggestion")
                },
                placeholderText = "내 회사 검색 혹은 직접 입력",
                focusManager = focusManager,
                allowDirectInput = true
            )
        }
    }
}