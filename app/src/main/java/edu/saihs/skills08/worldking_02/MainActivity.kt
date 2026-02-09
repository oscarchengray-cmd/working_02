package edu.saihs.skills08.worldking_02

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import edu.saihs.skills08.worldking_02.ui.theme.WordKing_02Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordKing_02Theme {
                mainactivity()
            }
        }
    }
}

data class Words(
    val title: String,
    val total: String,
    val words: List<Word>
)

data class Word(
    val english: String,
    val chinese: String,
    val learning: Boolean
)

class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val fileName = "my_words.json"
    private val _wordsData = MutableStateFlow<Words?>(null)
    val wordsData: StateFlow<Words?> = _wordsData

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, fileName)
                val jsonString = if (file.exists()) {
                    file.readText()
                } else {
                    val defaultJson = context.assets.open("words.json").bufferedReader().use { it.readText() }
                    saveToFile(defaultJson)
                    defaultJson
                }

                _wordsData.value = parseJsonToWords(jsonString)
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun parseJsonToWords(jsonString: String): Words {
        val root = JSONObject(jsonString)
        val wordsArray = root.getJSONArray("words")
        val wordList = mutableListOf<Word>()

        for (i in 0 until wordsArray.length()) {
            val obj = wordsArray.getJSONObject(i)
            wordList.add(
                Word(
                    english = obj.getString("english"),
                    chinese = obj.getString("chinese"),
                    learning = obj.getBoolean("learning")
                )
            )
        }

        return Words(
            title = root.getString("title"),
            total = root.optString("total"),
            words = wordList
        )
    }

    private fun wordsToJsonString(data: Words): String {
        val root = JSONObject()
        root.put("title", data.title)
        root.put("total", data.total)

        val wordsArray = JSONArray()
        data.words.forEach { word ->
            val wordObj = JSONObject()
            wordObj.put("english", word.english)
            wordObj.put("chinese", word.chinese)
            wordObj.put("learning", word.learning)
            wordsArray.put(wordObj)
        }
        root.put("words", wordsArray)

        return root.toString()
    }

    fun updateAndSave(newWords: Words) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString = wordsToJsonString(newWords)
                saveToFile(jsonString)
                _wordsData.value = newWords
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleLearning(index: Int) {
        val currentData = _wordsData.value ?: return
        val updatedWords = currentData.words.mapIndexed { i, word ->
            if (i == index) word.copy(learning = !word.learning) else word
        }
        val newData = currentData.copy(
            words = updatedWords,
            total = updatedWords.size.toString()
        )

        updateAndSave(newData)
    }



    private fun saveToFile(jsonString: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun editWord(index: Int, updatedWord: Word) {
        val currentData = _wordsData.value ?: return

        val newList = currentData.words.mapIndexed { i, w ->
            if (i == index) updatedWord else w
        }

        val newData = currentData.copy(
            words = newList,
            total = newList.size.toString()
        )

        updateAndSave(newData)
    }
}

@Composable
fun mainactivity(viewModel: WordViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "wordlistpage"
    ) {
        composable(
            "wordlistpage",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }) {
            wordlist(navController, viewModel)

        }
        composable(
            "cardpage", enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
            Card(navController, viewModel)
        }

        composable(
            "newcardpage", enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }) {
            CardEditScreen(navController, viewModel)
        }

        composable(
            "editpage/{index}", enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }) { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull()
            CardEditScreen(navController, viewModel, editIndex = index)
        }
    }
}