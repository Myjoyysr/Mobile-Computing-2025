package com.myjoyy.composetutorial2.screens


import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import coil3.compose.rememberAsyncImagePainter
import com.myjoyy.composetutorial2.R
import com.myjoyy.composetutorial2.db.User
import com.myjoyy.composetutorial2.notifications.NotificationHelper
import com.myjoyy.composetutorial2.viewModels.UserViewModel

import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenProject(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit,
    //userDao: UserDao,
    userViewModel: UserViewModel,
    notificationHelper: NotificationHelper

){
    //val userViewModel: UserViewModel = viewModel(factory = UserFactory(userDao))
    val user by userViewModel.user.observeAsState()

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "SettingsProject",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackButton()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        SettingsProject(
            modifier = Modifier.padding(innerPadding),
            user = user,
            onUpdateUser = {updateUser -> userViewModel.updateUser(updateUser)},
            notificationHelper = notificationHelper
        )
    }
}

@Composable
fun SettingsProject(
    modifier: Modifier = Modifier,
    user: User?,
    onUpdateUser: (User) -> Unit,
    notificationHelper: NotificationHelper
){
    val context = LocalContext.current

    //var user = userDao.getUser()


    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {

            val resolver = context.contentResolver

            try {
                resolver.openInputStream(uri).use { stream ->
                    // Perform operations on "stream".
                    val file = File(context.filesDir, "profilePic.jpg")

                    stream?.copyTo(out = file.outputStream())


                    val newUser = User(uid = 1, username = user?.username, imagePath = file.absolutePath)
                    //userDao.updateUser(newUser)
                    onUpdateUser(newUser)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }

        } else {

        }
    }

    var username by remember { mutableStateOf(user?.username ?: "") }

    val changeUsername = { newUsername: String ->
        val newUser = User(uid = 1, username = newUsername, imagePath = user?.imagePath)
        //userDao.updateUser(newUser)
        onUpdateUser(newUser)
        //user = userDao.getUser()
        //user by userViewModel.user.observeAsState()
    }

    Column(
        modifier = modifier
            .fillMaxSize().padding(all = 8.dp)
    ) {
        Text(text = "User:")
        Spacer(modifier = Modifier.height(8.dp))
        val userDetails = user

        val profilePainter = if (!userDetails?.imagePath.isNullOrEmpty()){
            rememberAsyncImagePainter(userDetails?.imagePath)

        }else{
            painterResource(R.drawable.profile_picture)
        }

        Image(
            painter = profilePainter,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = {newText -> username = newText},
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    changeUsername(username)
                }
            ),
            modifier = Modifier.width(200.dp)
        )

        Spacer(modifier = Modifier.height(25.dp))

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {isEnabled ->
                if (isEnabled){
                    notificationHelper.firstNotification("Hello World!","Notifications enabled!")
                }
            }
        )

        ExtendedFloatingActionButton(
            onClick = { notificationHelper.requestPermission(launcher) },
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector =  Icons.Filled.Warning,
                    contentDescription = "EnableNotifications"
                )
                Spacer(modifier = modifier.width(10.dp))
                Text(text = "Notifications")
            }
        }
    }
}

