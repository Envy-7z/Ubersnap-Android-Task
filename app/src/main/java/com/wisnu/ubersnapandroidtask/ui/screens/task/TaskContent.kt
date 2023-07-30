package com.wisnu.ubersnapandroidtask.ui.screens.task

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wisnu.ubersnapandroidtask.ui.theme.LARGE_PADDING
import com.wisnu.ubersnapandroidtask.ui.theme.MEDIUM_PADDING
import com.wisnu.ubersnapandroidtask.ui.theme.fabBackgroundColor
import com.wisnu.ubersnapandroidtask.ui.theme.topAppBarContentColor
import java.util.Calendar
import java.util.Date

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    dueDate: String,
    onDueDateChange: (String) -> Unit
) {
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            mDate.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
            onDueDateChange("$mDayOfMonth/${mMonth + 1}/$mYear")
        }, mYear, mMonth, mDay
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { newTitle -> onTitleChange(newTitle) },
            label = { Text(text = "Title") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        Button(onClick = {
            mDatePickerDialog.show()
        }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.fabBackgroundColor)) {
            Text(text = "Select Date", color = MaterialTheme.colors.topAppBarContentColor)
        }
        Spacer(modifier = Modifier.size(8.dp))

        // Displaying the mDate value in the Text
        if (dueDate.isNotEmpty()) {
            Text(text = "Selected Date: $dueDate", fontSize = 12.sp, textAlign = TextAlign.Center)
        } else {
            Text(
                text = "Selected Date: ${mDate.value}",
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

        }

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { newDescription -> onDescriptionChange(newDescription) },
            label = { Text(text = "Description") },
            textStyle = MaterialTheme.typography.body1
        )
    }
}

@Composable
@Preview
fun PreviewTaskContent() {
    TaskContent(
        title = "Wisnuuuu",
        onTitleChange = {},
        description = "Description related to title",
        onDescriptionChange = {},
        dueDate = "27/7/2023",
        onDueDateChange = {}
    )
}