package edu.saihs.skills08.worldking_02

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditScreen(
    navController: NavHostController,
    viewModel: WordViewModel,
    editIndex: Int? = null
) {
    var context = LocalContext.current
    val currentData by viewModel.wordsData.collectAsState()

    val targetWord = editIndex?.let { currentData?.words?.getOrNull(it) }

    var text1 by remember { mutableStateOf(targetWord?.english ?: "") }
    var text2 by remember { mutableStateOf(targetWord?.chinese ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editIndex == null) "建立單字" else "編輯單字") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        text1 = ""
                        text2 = ""
                    }) {
                        Icon(painter = painterResource(R.drawable.delete), null, modifier = Modifier.size(30.dp))
                    }
                }
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("英文") },
                modifier = Modifier.width(350.dp),
                trailingIcon = {
                    IconButton(onClick = { text1 = "" }) {
                        Icon(painter = painterResource(R.drawable.restore), null, modifier = Modifier.size(30.dp))
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("中文") },
                modifier = Modifier.width(350.dp),
                trailingIcon = {
                    IconButton(onClick = { text2 = "" }) {
                        Icon(painter = painterResource(R.drawable.restore), null, modifier = Modifier.size(30.dp))
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (text1.isNotBlank() && text2.isNotBlank()) {
                        val newWord = Word(
                            english = text1,
                            chinese = text2,
                            learning = targetWord?.learning ?: false
                        )
                        if (editIndex == null) {
                            val currentWords = currentData ?: Words("我的單字本", "0", emptyList())
                            val updatedData = currentWords.copy(
                                words = currentWords.words + newWord,
                                total = (currentWords.words.size + 1).toString()
                            )
                            viewModel.updateAndSave(updatedData)
                        } else {
                            viewModel.editWord(editIndex, newWord)
                        }
                        navController.navigate("wordlistpage")
                    }else{
                        Toast.makeText(context, "請輸入完整", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.width(350.dp),
            ) {
                Text("確認")
            }
        }
    }
}