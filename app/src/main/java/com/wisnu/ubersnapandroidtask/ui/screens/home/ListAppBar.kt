package com.wisnu.ubersnapandroidtask.ui.screens.home


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wisnu.ubersnapandroidtask.ui.theme.topAppBarBackgroundColor
import com.wisnu.ubersnapandroidtask.ui.theme.topAppBarContentColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.wisnu.ubersnapandroidtask.ui.theme.LARGE_PADDING
import com.wisnu.ubersnapandroidtask.ui.theme.TOP_APP_BAR_HEIGHT
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import com.wisnu.ubersnapandroidtask.components.DisplayAlertDialog
import com.wisnu.ubersnapandroidtask.ui.viewmodels.TaskViewModel
import com.wisnu.ubersnapandroidtask.utils.Action
import com.wisnu.ubersnapandroidtask.utils.SearchAppBarState
import com.wisnu.ubersnapandroidtask.R

@Composable
fun ListAppBar(
    taskViewModel: TaskViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String,
    onSearchClicked: (SearchAppBarState) -> Unit,
    onTextChange: (String) -> Unit

) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED ->
            DefaultListAppBar(
                onSearchClicked = {
                    // taskViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                    onSearchClicked(SearchAppBarState.OPENED)
                },
                onDeleteAllConfirmed = {
                    taskViewModel.action.value = Action.DELETE_ALL
                }
            )
        else ->
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText -> onTextChange(newText) },
                onCloseClicked = {
                    onSearchClicked(SearchAppBarState.CLOSED)
                    onTextChange("")
                },
                onSearchClicked = {
                    taskViewModel.searchDatabase(searchQuery = it)
                }
            )
    }

}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                "Tasks",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )

        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onDeleteAllConfirmed: () -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    DisplayAlertDialog(
        title = "Remove All Tasks?",
        message = "Are you sure you want to remove all Tasks?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )

    SearchAction(onSearchClicked = onSearchClicked)
    DeleteAllAction(
        onDeleteAllConfirmed = {
            openDialog = true
        })
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
) {
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(id = R.string.search_tasks),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}


@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_delete_24),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = LARGE_PADDING),
                    text = stringResource(id = R.string.delete_all_action),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = "Search", //! transfer to strings.xml
                    color = Color.White,
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)

                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .alpha(ContentAlpha.disabled)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon", //! transfer to strings.xml
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon", //! transfer to strings.xml
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}


@Preview
@Composable
fun PreviewDefaultListAppBar() {
    DefaultListAppBar(
        onSearchClicked = {},
        onDeleteAllConfirmed = {}
    )
}

@Preview
@Composable
fun PreviewSearchAppBar() {
    SearchAppBar(
        text = "",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}