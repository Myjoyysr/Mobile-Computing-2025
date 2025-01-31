package com.myjoyy.composetutorial2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier,
    onNavigateToHwOne: () -> Unit,
    onNavigateToHwTwo: () -> Unit,
    onNavigateToHwThree: () -> Unit
){
    MainMenu(
        modifier = modifier,
        onNavigateToHwOne = onNavigateToHwOne,
        onNavigateToHwTwo = onNavigateToHwTwo,
        onNavigateToHwThree = onNavigateToHwThree)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    modifier: Modifier = Modifier,
    onNavigateToHwOne: () -> Unit,
    onNavigateToHwTwo: () -> Unit,
    onNavigateToHwThree: () -> Unit)
{
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    //https://developer.android.com/develop/ui/compose/components/app-bars

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "ComposeTutorial3 App",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },/*
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },*/
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Text(text = "MainMenu")

            Spacer(modifier = Modifier.height(20.dp))

            ExtendedFloatingActionButton(
                onClick = onNavigateToHwOne,
                modifier = Modifier
                    .width(screenWidth* 0.6f)
                    .height(60.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector =  Icons.Filled.CheckCircle,
                        contentDescription = "Section 1"
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Homework 1")
                }

            }

            Spacer(modifier = Modifier.height(25.dp))

            ExtendedFloatingActionButton(
                onClick = onNavigateToHwTwo,
                modifier = Modifier
                    .width(screenWidth* 0.6f)
                    .height(60.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector =  Icons.Filled.CheckCircle,
                        contentDescription = "Section 2"
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Homework 2")
                }

            }

            Spacer(modifier = Modifier.height(25.dp))

            ExtendedFloatingActionButton(
                onClick = onNavigateToHwThree,
                modifier = Modifier
                    .width(screenWidth* 0.6f)
                    .height(60.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector =  Icons.Filled.CheckCircle,
                        contentDescription = "Section 3"
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Homework 3")
                }

            }

            Spacer(modifier = Modifier.height(25.dp))

            ExtendedFloatingActionButton(
                onClick = {/*onNavigateToHwFour*/},
                modifier = Modifier
                    .width(screenWidth* 0.6f)
                    .height(60.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector =  Icons.Filled.Warning,
                        contentDescription = "Section 4"
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Homework 4")
                }

            }

            Spacer(modifier = Modifier.height(25.dp))


            ExtendedFloatingActionButton(
                onClick = {/*onNavigateToProject*/},
                modifier = Modifier
                    .width(screenWidth* 0.6f)
                    .height(60.dp)
            ){
                Row(verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector =  Icons.Filled.Warning,
                        contentDescription = "Project"
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(text = "Project")
                }

            }
        }
    }
}

