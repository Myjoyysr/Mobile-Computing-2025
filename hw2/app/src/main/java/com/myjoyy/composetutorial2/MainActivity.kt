package com.myjoyy.composetutorial2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myjoyy.composetutorial2.ui.theme.ComposeTutorial2Theme
import kotlinx.serialization.Serializable

import com.myjoyy.composetutorial2.screens.MainMenuScreen
import com.myjoyy.composetutorial2.screens.HwOneScreen
import com.myjoyy.composetutorial2.screens.HwTwoScreen
import com.myjoyy.composetutorial2.screens.SettingsScreen

@Serializable
object MainMenu

@Serializable
object HwOne

@Serializable
object HwTwo

@Serializable
object Settings

//https://developer.android.com/develop/ui/compose/libraries

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTutorial2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TutorialNavigation(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun TutorialNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainMenu
    ) {
        composable<MainMenu> {
            MainMenuScreen(
                modifier = modifier,
                onNavigateToHwOne = { navController.navigate(route = HwOne) },
                onNavigateToHwTwo = { navController.navigate(route = HwTwo) }

                /*...*/
            )
        }
        composable<HwOne> {
            HwOneScreen(
                modifier = modifier
            )

        }
        composable<HwTwo> {
            HwTwoScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                onNavigateToSettings = {navController.navigate(route = Settings)}
            )
        }
        composable<Settings> {
            SettingsScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()}
            )
        }
    }
}

