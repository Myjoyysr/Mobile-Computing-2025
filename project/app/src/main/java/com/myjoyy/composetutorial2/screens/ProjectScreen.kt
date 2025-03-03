package com.myjoyy.composetutorial2.screens

import android.app.Activity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import com.myjoyy.composetutorial2.R
import com.myjoyy.composetutorial2.db.User
import coil3.compose.rememberAsyncImagePainter
import com.myjoyy.composetutorial2.db.dbMessage
import com.myjoyy.composetutorial2.viewModels.UserViewModel
import com.myjoyy.composetutorial2.speechrecognition.SpeechRecognition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
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
    val dbMessages by userViewModel.messages.observeAsState(emptyList())

    //Log.d("projectScreen", "dbMessages: ${dbMessages.size}")

    var showAddDialog by remember {mutableStateOf(false)}
    var showTTSDialog by remember {mutableStateOf(false)}
    var selectedMessageId by remember { mutableStateOf<Int?>(null) }

    if (showAddDialog){
        AddMessageDialog(
            dialogTitle =  "Add message",
            icon = Icons.Filled.AccountBox,
            onDismissRequest = {
                showAddDialog = false
            },
            onAddMessage = { messageText ->
                var dbMessageToAdd = dbMessage(userId=1, message = messageText)

                userViewModel.insertMessage(dbMessageToAdd)
                showAddDialog = false
            }
        )
    }

    if (showTTSDialog){
        AddTTSMessageDialog(
            dialogTitle =  "Add message",
            icon = Icons.Filled.AccountBox,
            onDismissRequest = {
                showTTSDialog = false
            },
            onAddMessage = { messageText ->
                var dbMessageToAdd = dbMessage(userId=1, message = messageText)

                userViewModel.insertMessage(dbMessageToAdd)
                showTTSDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Project",
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
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.primary)
            {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    actions = {
                        IconButton(onClick = { showAddDialog = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "add message")
                        }


                        IconButton(onClick = { showTTSDialog = true }) {
                            Icon(
                                Icons.Filled.AddCircle,
                                contentDescription = "add tts message",
                            )
                        }
                        selectedMessageId?.let {
                            IconButton(onClick = {
                                userViewModel.deleteMessage(selectedMessageId!!)
                                selectedMessageId = null}) {
                                Icon(
                                    Icons.Filled.Clear,
                                    contentDescription = "Delete selected message",
                                )
                            }
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        ConversationProject(
            //messages = SampleData.conversationSample,
            messages = dbMessages,
            modifier = Modifier.padding(innerPadding),
            userDetails = userDetails,
            selectedMessage= selectedMessageId,
            messageSelected = { messageId ->
                if (selectedMessageId == messageId){
                    selectedMessageId = null
                }else{
                    selectedMessageId = messageId
                }
            }
        )
    }
}

@Composable
fun MessageCardProject(msg: dbMessage, userDetails: User?, onMessageSelected: (Int)-> Unit, selectedMessage: Int?) {
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
        //var isExpanded by remember { mutableStateOf(false) }

        //allow only one message to be expanded
        var isExpanded = selectedMessage == msg.id

        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface,
        )

        // We toggle the isExpanded variable when we click on this Column

        //we return the message id as selected and do swapping in screen function
        //this is to ensure that deleting works as intented and visuals are only on the selected message
        Column(modifier = Modifier.clickable {
            onMessageSelected(msg.id)
             }) {

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
                    text = msg.message,
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
fun ConversationProject(messages: List<dbMessage>, modifier: Modifier = Modifier, userDetails: User?, messageSelected: (Int) -> Unit, selectedMessage: Int?) {
    LazyColumn(modifier = modifier) {
        items(messages) { message ->
            MessageCardProject(message, userDetails, messageSelected,selectedMessage)
        }
    }
}

//https://developer.android.com/develop/ui/compose/components/dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMessageDialog(
    onDismissRequest: () -> Unit,
    onAddMessage: (String) -> Unit,
    dialogTitle: String,
    icon: ImageVector,
) {
    var messageText by remember {mutableStateOf("")}
    AlertDialog(

        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                TextField(
                    value = messageText,
                    onValueChange = {newText -> messageText = newText},
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onAddMessage(messageText)
                        }
                    ),
                    modifier = Modifier.width(200.dp)
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (messageText.isNotBlank()){
                        onAddMessage(messageText)
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.secondary)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.secondary)
            ) {
                Text("Dismiss")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTTSMessageDialog(
    onDismissRequest: () -> Unit,
    onAddMessage: (String) -> Unit,
    dialogTitle: String,
    icon: ImageVector,
) {
    var messageText by remember {mutableStateOf("")}

    val context = LocalContext.current
    val activity = context as? Activity
    val speechRecognition = remember { SpeechRecognition(context) }

    val tts by speechRecognition.text.collectAsState()

    if (speechRecognition.hasPermission(context)){
        speechRecognition.start()
    }else{
        if(activity != null){
            speechRecognition.requestPermission(activity)
        }
    }

    LaunchedEffect(tts) {
        if(tts.isNotBlank()){
            messageText = tts
        }
    }

    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                TextField(
                    value = messageText,
                    onValueChange = {newText -> messageText = newText},
                    modifier = Modifier.width(200.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                    TextButton(
                        onClick = {
                            speechRecognition.start()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Record Again")
                    }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (messageText.isNotBlank()){
                        onAddMessage(messageText)
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.secondary)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors(contentColor =  MaterialTheme.colorScheme.secondary)
            ) {
                Text("Dismiss")
            }
        }
    )
}

