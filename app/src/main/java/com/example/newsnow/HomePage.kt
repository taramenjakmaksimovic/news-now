package com.example.newsnow

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.newsnow.ui.theme.DarkPurple
import com.example.newsnow.ui.theme.LightPurple
import com.kwabenaberko.newsapilib.models.Article

@Composable
fun HomePage(newsViewModel: NewsViewModel, navController: NavHostController){
    val articles by newsViewModel.articles.observeAsState(emptyList())
    Column ( modifier = Modifier.fillMaxSize()){
        CategoriesBar(newsViewModel)


        LazyColumn ( modifier = Modifier.fillMaxSize()){
         items(articles){ article->
            ArticleItem(article, navController)

         }

        }

    }

}
@Composable
fun ThemeSwitcher(
    darkTheme: Boolean = false,
    size: Dp = 150.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (darkTheme) 0.dp else size,
        animationSpec = animationSpec
    )

    Box(
        modifier = Modifier
            .width(size * 2)
            .height(size)
            .clip(shape = parentShape)
            .clickable { onClick() }
            .background(if (darkTheme) DarkPurple else LightPurple)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(if (darkTheme) LightPurple else DarkPurple)
        )

        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = if (darkTheme) DarkPurple else LightPurple
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = ImageVector.vectorResource(id = R.drawable.dark_mode),
                    contentDescription = "Dark Mode Icon",
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = ImageVector.vectorResource(id = R.drawable.light_mode),
                    contentDescription = "Light Mode Icon",
                    tint = Color.White
                )
            }
        }
    }
}
@Composable
fun ArticleItem(article: Article, navController: NavHostController){
    Card(modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightPurple
        ),
        onClick = {
            navController.navigate(NewsArticleScreen(article.url))

        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(model = article.urlToImage?:"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUrgu4a7W_OM8LmAuN7Prk8dzWXm7PVB_FmA&s",
                contentDescription = "Article image",
                modifier = Modifier.size(80.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
                )
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(start = 8.dp)
            ) {
                Text(text=article.title,
                    fontWeight = FontWeight.Bold,
                    color = DarkPurple,
                    fontFamily = FontFamily.Serif,
                    maxLines = 3
                )
                Text(text = article.source.name,
                    fontFamily = FontFamily.Serif,
                    maxLines = 1,
                    fontSize = 14.sp)
            }
        }
    }

}
@Composable
fun CategoriesBar(newsViewModel: NewsViewModel){
    var searchQuery by remember {
        mutableStateOf("")
    }

    var isSearchExpanded by remember {
        mutableStateOf(false)
    }
        val categoriesList= listOf(
        "GENERAL",
        "BUSINESS",
        "ENTERTAINMENT",
        "HEALTH",
        "SCIENCE",
        "SPORTS",
        "TECHNOLOGY"
    )
    Row(
        modifier = Modifier.fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ){
        if(isSearchExpanded){
            OutlinedTextField(
                modifier = Modifier.padding(8.dp)
                    .height(54.dp)
                    .border(1.dp, DarkPurple, CircleShape)
                    .clip(CircleShape),
                value = searchQuery,
                onValueChange = {
                searchQuery=it
            },
                trailingIcon = {
                    IconButton(onClick = {
                        isSearchExpanded=false
                        if(searchQuery.isNotEmpty()){
                            newsViewModel.fetchEverythingWithQuery(searchQuery)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
                    }
                }
                )

        }
        else{
            IconButton(onClick = {
                isSearchExpanded=true
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            }

        }

        categoriesList.forEach{ category ->
            Button(onClick = {
                newsViewModel.fetchNewsTopHeadlines(category)
            },
                modifier = Modifier.padding(4.dp).height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkPurple,
                    contentColor = Color.White
                )
                ) {
                Text(text=category,
                    fontFamily = FontFamily.Serif)

            }

        }
    }

}