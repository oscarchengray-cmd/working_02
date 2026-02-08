package edu.saihs.skills08.worldking_02

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Card(navController: NavHostController, viewModel: WordViewModel) {
    val allData by viewModel.wordsData.collectAsState()
    val wordList = allData?.words ?: emptyList()
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("wordlistpage") },
                    icon = { Icon(painterResource(R.drawable.list), null, Modifier.size(30.dp)) },
                    label = { Text("單字列表") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(painterResource(R.drawable.card), null, Modifier.size(30.dp)) },
                    label = { Text("單字卡") }
                )
            }
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (wordList.isEmpty()) {
                Text("目前沒有單字")
            } else {
                val pagerState = rememberPagerState(pageCount = { wordList.size })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { pageIndex ->
                    val currentWord = wordList[pageIndex]
                    var rotated by remember(pageIndex) { mutableStateOf(false) }
                    val rotation by animateFloatAsState(
                        targetValue = if (rotated) 180f else 0f,
                        animationSpec = tween(durationMillis = 700),
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (currentWord.learning) {
                            Box(
                                modifier = Modifier.background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(30.dp)
                                )
                            ) {
                                Text(
                                    "學習中",
                                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.padding(vertical = 17.dp))
                        }
                        Spacer(modifier = Modifier.padding(30.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Card(
                                modifier = Modifier
                                    .size(250.dp, 320.dp)
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = { rotated = !rotated }
                                    )
                                    .graphicsLayer {
                                        rotationY = rotation
                                        cameraDistance = 10f * density
                                    }
                            ) {
                                if (rotation <= 90f) {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("英文")
                                            Spacer(modifier = Modifier.padding(5.dp))
                                            Text(
                                                currentWord.english,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 30.sp
                                            )
                                        }

                                    }
                                } else {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .graphicsLayer { rotationY = 180f },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("中文")
                                            Spacer(modifier = Modifier.padding(5.dp))
                                            Text(
                                                currentWord.chinese,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 30.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
                Spacer(Modifier.size(50.dp))
                Text(
                    "${pagerState.currentPage + 1} / ${wordList.size}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}