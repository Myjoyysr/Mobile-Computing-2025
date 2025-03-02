package com.myjoyy.composetutorial2

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.myjoyy.composetutorial2.db.User
import com.myjoyy.composetutorial2.db.UserDao
import com.myjoyy.composetutorial2.db.UserDatabase
import com.myjoyy.composetutorial2.notifications.NotificationHelper
import com.myjoyy.composetutorial2.ui.theme.ComposeTutorial2Theme
import kotlinx.serialization.Serializable

import com.myjoyy.composetutorial2.screens.MainMenuScreen
import com.myjoyy.composetutorial2.screens.HwOneScreen
import com.myjoyy.composetutorial2.screens.HwTwoScreen
import com.myjoyy.composetutorial2.screens.HwThreeScreen
import com.myjoyy.composetutorial2.screens.HwFourScreen
import com.myjoyy.composetutorial2.screens.SettingsScreen
import com.myjoyy.composetutorial2.screens.SettingsScreenThree
import com.myjoyy.composetutorial2.screens.SettingsScreenFour

import com.myjoyy.composetutorial2.screens.ProjectScreen
import com.myjoyy.composetutorial2.screens.SettingsScreenProject

import com.myjoyy.composetutorial2.sensor.SensorHelper
import com.myjoyy.composetutorial2.viewModels.MainViewModel
import com.myjoyy.composetutorial2.viewModels.UserFactory
import com.myjoyy.composetutorial2.viewModels.UserViewModel

@Serializable
object MainMenu

@Serializable
object HwOne

@Serializable
object HwTwo

@Serializable
object HwThree

@Serializable
object HwFour

@Serializable
object Settings

@Serializable
object SettingsThree

@Serializable
object SettingsFour

@Serializable
object ProjectScreen

@Serializable
object SettingsProject

//https://developer.android.com/develop/ui/compose/libraries

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        //https://www.youtube.com/watch?v=eFZmMSm1G1c "better" explanation than the documentation
        installSplashScreen().apply{
            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }
            setOnExitAnimationListener{ screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.5f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 1000L
                //zoomX.doOnEnd { screen.remove()}


                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.5f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1000L
                //zoomY.doOnEnd { screen.remove()}

               /* val rotate = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.ROTATION,
                    0.0f,
                    360f
                )
                rotate.interpolator = OvershootInterpolator()
                rotate.duration = 1000L*/

                //https://developer.android.com/reference/android/animation/AnimatorSet
                val animations = AnimatorSet()
                animations.playTogether(zoomX,zoomY/*,rotate*/)
                animations.doOnEnd { screen.remove() }
                animations.start()
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java, "userDatabase"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()

        val userCheck = userDao.getUserOne()

        if (userCheck == null){
            val newUser = User(uid = 1, username = "test", imagePath = "")
            userDao.insertUser(newUser)

        }

        val notificationHelper = NotificationHelper(this)
        notificationHelper.notificationChannel()

        //var sensorHelper = SensorHelper()
        //sensorHelper.startSensorListener()

        val sensorIntent = Intent(this, SensorHelper::class.java)
        startForegroundService(sensorIntent)

        setContent {
            ComposeTutorial2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TutorialNavigation(modifier = Modifier.padding(innerPadding), userDao = userDao, notificationHelper = notificationHelper)

                }
            }
        }
    }
}

@Composable
fun TutorialNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userDao: UserDao,
    notificationHelper: NotificationHelper
){
    val userViewModel: UserViewModel = viewModel(factory = UserFactory(userDao))

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainMenu
    ) {
        composable<MainMenu> {
            MainMenuScreen(
                modifier = modifier,
                onNavigateToHwOne = { navController.navigate(route = HwOne) },
                onNavigateToHwTwo = { navController.navigate(route = HwTwo) },
                onNavigateToHwThree = { navController.navigate(route = HwThree) },
                onNavigateToHwFour = { navController.navigate(route = HwFour) },
                onNavigateToProject = { navController.navigate(route = ProjectScreen) }

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
        composable<HwThree> {
            HwThreeScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                onNavigateToSettings = {navController.navigate(route = SettingsThree)},
                //userDao = userDao
                userViewModel = userViewModel
            )
        }

        composable<HwFour> {
            HwFourScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                onNavigateToSettings = {navController.navigate(route = SettingsFour)},
                //userDao = userDao
                userViewModel = userViewModel
            )
        }

        composable<ProjectScreen> {
            ProjectScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                onNavigateToSettings = {navController.navigate(route = SettingsProject)},
                //userDao = userDao
                userViewModel = userViewModel
            )
        }


        composable<Settings> {
            SettingsScreen(
                modifier = modifier,
                onBackButton = {navController.popBackStack()}
            )
        }
        composable<SettingsThree> {
            SettingsScreenThree(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                //userDao = userDao
                userViewModel = userViewModel
            )
        }
        composable<SettingsFour> {
            SettingsScreenFour(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                //userDao = userDao
                userViewModel = userViewModel,
                notificationHelper = notificationHelper
            )
        }

        composable<SettingsProject> {
            SettingsScreenProject(
                modifier = modifier,
                onBackButton = {navController.popBackStack()},
                //userDao = userDao
                userViewModel = userViewModel,
                notificationHelper = notificationHelper
            )
        }
    }
}

