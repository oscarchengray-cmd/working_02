package edu.saihs.skills08.worldking_02

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun card(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("wordlistpage") },
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
                    selected = true,
                    onClick = { },
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

            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                val pagerState = rememberPagerState(pageCount = {2})
                HorizontalPager(
                    state = pagerState, modifier = Modifier.fillMaxWidth()
                ) {
                    when (it) {
                        0 -> {
                            var rotated by remember { mutableStateOf(false) }
                            val rotation by animateFloatAsState(
                                targetValue = if (rotated) 180f else 0f,
                                animationSpec = tween(durationMillis = 1000),
                                label = "CardRotation"
                            )
                            Box(
                                modifier = Modifier
                                    .clickable { rotated = !rotated },
                                contentAlignment = Alignment.Center,

                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(250.dp, 350.dp)
                                        .graphicsLayer {
                                            rotationY = rotation
                                        }
                                ) {
                                    if (rotation <= 90f) {
                                        Box(
                                            Modifier.fillMaxSize().background(Color.Gray),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("英文")
                                        }
                                    } else {
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(Color.Gray)
                                                .graphicsLayer {
                                                    rotationY = 180f
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("中文")
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            Image(
                                painter = painterResource(R.drawable.ic_launcher_foreground), null,
                                Modifier.size(300.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Text("${pagerState.currentPage+1}/2")
            }

        }

    }
}