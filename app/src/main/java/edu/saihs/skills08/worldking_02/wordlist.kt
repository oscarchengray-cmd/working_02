package edu.saihs.skills08.worldking_02

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import kotlin.jvm.java



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun wordlist(navController: NavHostController) {
    val context = LocalContext.current
    val jsonString = context.assets.open("words.json").bufferedReader().use { it.readText() }
    val words=Gson().fromJson(jsonString, words::class.java)
    var all=words.words
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("所有", "學習中")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("單字列表") },
                actions = {
                    IconButton(onClick = { navController.navigate("newcardpage") }) {
                        Icon(
                            Icons.Default.Add,
                            null
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.list),
                            null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    label = { Text("單字列表") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("cardpage") },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.card),
                            null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    label = { Text("單字卡") }
                )
            }
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SingleChoiceSegmentedButtonRow {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex,
                        label = { Text(label) },
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
            all.forEachIndexed { index, word ->

                if (selectedIndex == 0) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column() {
                            Text(
                                word.english, fontWeight = FontWeight.SemiBold
                            )
                            Text(word.chinese)
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = if (word.learning) Color.Gray else Color.White)
                        ) {
                            IconButton(onClick = {
                                word.learning = !word.learning
                            }) {
                                Icon(Icons.Default.Check, null)
                            }
                        }
                    }
                } else {
                    if (word.learning) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column() {
                                Text(
                                    word.english, fontWeight = FontWeight.SemiBold
                                )
                                Text(word.chinese)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = if (word.learning) Color.Gray else Color.White)
                            ) {
                                IconButton(onClick = { word.learning = !word.learning }) {
                                    Icon(Icons.Default.Check, null)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}