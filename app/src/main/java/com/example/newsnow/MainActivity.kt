package com.example.newsnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsnow.ui.theme.DarkPurple
import com.example.newsnow.ui.theme.NewsNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val newsViewModel=ViewModelProvider(this)[NewsViewModel::class.java ]
        setContent {
            val navController = rememberNavController()
            var darkTheme by remember { mutableStateOf(false) }
            val onThemeUpdated: () -> Unit = {
                darkTheme = !darkTheme }

            NewsNowTheme(darkTheme=darkTheme){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                  Column(
                      modifier=Modifier.padding(innerPadding)
                          .fillMaxSize()
                          .background(MaterialTheme.colorScheme.surface)
                  ) {
                      Row(
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(horizontal = 16.dp, vertical = 8.dp),
                          verticalAlignment = Alignment.CenterVertically,
                          horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                          Text(
                              text = "NEWS NOW",
                              color = DarkPurple,
                              fontSize = 35.sp,
                              fontFamily = FontFamily.Serif
                          )
                          ThemeSwitcher(
                              darkTheme = darkTheme,
                              onClick = onThemeUpdated,
                              size = 50.dp,
                              padding = 5.dp
                          )
                      }
                      NavHost(navController=navController, startDestination = HomePageScreen ){
                        composable<HomePageScreen> {
                            HomePage(
                                newsViewModel,
                                navController
                                )
                        }
                          composable<NewsArticleScreen> {
                              val args=it.toRoute<NewsArticleScreen>()
                              NewsArticlePage(args.url)
                          }
                      }

                  }
                }
            }
        }
    }
}

