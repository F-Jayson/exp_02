package com.mobleApp.exp_02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PageBackground = Color(0xFFF4F5F7)
private val AccentRed = Color(0xFFE53935)
private val HintGray = Color(0xFFB0B0B0)
private val DoneGray = Color(0xFF9E9E9E)

data class StudyTask(
    val id: Int,
    val title: String,
    val done: Boolean,
)

class ComposeTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourseTaskTheme {
                CourseTaskScreen()
            }
        }
    }
}

@Composable
private fun CourseTaskTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        shapes = Shapes(
            extraSmall = RoundedCornerShape(0.dp),
            small = RoundedCornerShape(0.dp),
            medium = RoundedCornerShape(0.dp),
            large = RoundedCornerShape(0.dp),
            extraLarge = RoundedCornerShape(0.dp),
        ),
        content = content,
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "课程学习任务")
@Composable
private fun CourseTaskPreview() {
    CourseTaskTheme {
        CourseTaskScreen()
    }
}

@Composable
fun CourseTaskScreen() {
    var input by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(4) }
    val tasks = remember {
        listOf(
            StudyTask(1, "学习 Column 和 Row", true),
            StudyTask(2, "学习状态管理", false),
            StudyTask(3, "完成 Compose 实验", false),
        ).toMutableStateList()
    }
    val doneCount = tasks.count { it.done }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.ime))
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = "课程学习任务",
            color = AccentRed,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = { Text("请输入学习任务", color = HintGray) },
                singleLine = true,
                shape = RoundedCornerShape(0.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AccentRed,
                ),
            )
            Button(
                onClick = {
                    val title = input.trim()
                    if (title.isEmpty()) {
                        return@Button
                    }
                    tasks.add(StudyTask(nextId, title, false))
                    nextId += 1
                    input = ""
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            ) {
                Text("添加")
            }
        }
        Text(
            text = "已完成：$doneCount / ${tasks.size}",
            color = DoneGray,
            fontSize = 13.sp,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    onToggle = { tasks.toggle(task.id) },
                    onDelete = { tasks.removeAll { it.id == task.id } },
                )
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: StudyTask,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = task.done,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentRed,
                    uncheckedColor = Color(0xFFBDBDBD),
                    checkmarkColor = Color.White,
                ),
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = if (task.done) DoneGray else Color(0xFF212121),
                    fontSize = 16.sp,
                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
                ),
            )
            Text(
                text = "删除",
                color = AccentRed,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable(onClick = onDelete)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
            )
        }
    }
}

private fun SnapshotStateList<StudyTask>.toggle(id: Int) {
    val index = indexOfFirst { it.id == id }
    if (index >= 0) {
        this[index] = this[index].copy(done = !this[index].done)
    }
}
