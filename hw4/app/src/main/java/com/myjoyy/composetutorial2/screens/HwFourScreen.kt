package com.myjoyy.composetutorial2.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.myjoyy.composetutorial2.ui.theme.ComposeTutorial2Theme
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

import com.myjoyy.composetutorial2.R
import com.myjoyy.composetutorial2.db.User


import coil3.compose.rememberAsyncImagePainter


import com.myjoyy.composetutorial2.viewModels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HwFourScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit,
    onNavigateToSettings: () -> Unit,
    //userDao: UserDao,
    userViewModel: UserViewModel

){
    //val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    //https://developer.android.com/develop/ui/compose/components/app-bars
    //https://developer.android.com/develop/ui/compose/libraries


    //val userDetails = userDao.getUser()
    //val userViewModel: UserViewModel = viewModel(factory = UserFactory(userDao))
    val userDetails by userViewModel.user.observeAsState()


    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Homework 4",
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
                actions = {
                    IconButton(onClick = { onNavigateToSettings() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Conversation4(
            messages = SampleData.conversationSample,
            modifier = Modifier.padding(innerPadding),
            userDetails = userDetails
        )
    }
}



@Composable
fun MessageCard4(msg: Message, userDetails: User?) {
    Row(modifier = Modifier.padding(all = 8.dp)) {

        val profilePainter = if (!userDetails?.imagePath.isNullOrEmpty()){
            rememberAsyncImagePainter(userDetails?.imagePath)

        }else{
            painterResource(R.drawable.profile_picture)
        }



        Image(
            painter = profilePainter,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this
        // variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            if (userDetails != null) {
                userDetails.username?.let {
                    Text(
                        //text = msg.author,
                        //text = "Janne3", //
                        text = it,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
fun Conversation4(messages: List<Message>, modifier: Modifier = Modifier, userDetails: User?) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            MessageCard4(message, userDetails)
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewConversation4() {
    ComposeTutorial2Theme {
        Conversation(SampleData.conversationSample)
    }
}
