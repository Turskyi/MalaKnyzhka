package com.turskyi.malaknyzhka.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turskyi.malaknyzhka.ai.models.ChatMessage
import com.turskyi.malaknyzhka.ai.models.MessageRole
import com.turskyi.malaknyzhka.ui.LocalShareManager
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.ask_placeholder
import malaknyzhka.composeapp.generated.resources.back_button_description
import malaknyzhka.composeapp.generated.resources.chat_with_taras
import malaknyzhka.composeapp.generated.resources.close_description
import malaknyzhka.composeapp.generated.resources.copied_to_clipboard
import malaknyzhka.composeapp.generated.resources.copy_description
import malaknyzhka.composeapp.generated.resources.discussing_page
import malaknyzhka.composeapp.generated.resources.full_screen_description
import malaknyzhka.composeapp.generated.resources.minimize_description
import malaknyzhka.composeapp.generated.resources.send_description
import malaknyzhka.composeapp.generated.resources.share_ai_title
import malaknyzhka.composeapp.generated.resources.share_description
import malaknyzhka.composeapp.generated.resources.share_from
import malaknyzhka.composeapp.generated.resources.taras_shevchenko_name
import malaknyzhka.composeapp.generated.resources.user_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit,
) {
    DisposableEffect(viewModel) {
        viewModel.setExpanded(true)
        onDispose {
            viewModel.setExpanded(false)
        }
    }

    val chatTitle = stringResource(Res.string.share_ai_title)
    val userName = stringResource(Res.string.user_name)
    val tarasName = stringResource(Res.string.taras_shevchenko_name)
    val fromLabel = stringResource(Res.string.share_from)
    val copiedLabel = stringResource(Res.string.copied_to_clipboard)
    val shareManager = LocalShareManager.current

    Scaffold(
        topBar = {
            Surface(
                elevation = 4.dp,
                color = MaterialTheme.colors.surface
            ) {
                TopAppBar(
                    title = { Text(stringResource(Res.string.chat_with_taras)) },
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.statusBars.only(WindowInsetsSides.Top)
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    Res.string.back_button_description,
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.shareConversation(
                                shareManager = shareManager,
                                title = chatTitle,
                                userName = userName,
                                tarasName = tarasName,
                                fromLabel = fromLabel
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(Res.string.share_description)
                            )
                        }
                        IconButton(onClick = {
                            viewModel.copyConversation(
                                shareManager = shareManager,
                                title = chatTitle,
                                userName = userName,
                                tarasName = tarasName,
                                fromLabel = fromLabel,
                                toastMessage = copiedLabel
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = stringResource(Res.string.copy_description)
                            )
                        }
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.FullscreenExit,
                                contentDescription = stringResource(Res.string.minimize_description)
                            )
                        }
                    },
                    backgroundColor = Color.Transparent, // Color handled by Surface
                    contentColor = MaterialTheme.colors.primary,
                    elevation = 0.dp // Elevation handled by Surface
                )
            }
        }
    )
    { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ChatView(viewModel = viewModel)
        }
    }
}

@Composable
fun ChatView(
    viewModel: ChatViewModel,
    onClose: (() -> Unit)? = null,
    onToggleFullScreen: (() -> Unit)? = null,
    isFullScreen: Boolean = true,
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val chatTitle = stringResource(Res.string.share_ai_title)
    val userName = stringResource(Res.string.user_name)
    val tarasName = stringResource(Res.string.taras_shevchenko_name)
    val fromLabel = stringResource(Res.string.share_from)
    val copiedLabel = stringResource(Res.string.copied_to_clipboard)
    val shareManager = LocalShareManager.current

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        // Header for overlay mode
        if (!isFullScreen) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(Res.string.chat_with_taras),
                    style = MaterialTheme.typography.subtitle1,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Row {
                    IconButton(onClick = {
                        viewModel.shareConversation(
                            shareManager = shareManager,
                            title = chatTitle,
                            userName = userName,
                            tarasName = tarasName,
                            fromLabel = fromLabel
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(Res.string.share_description)
                        )
                    }
                    IconButton(onClick = {
                        viewModel.copyConversation(
                            shareManager = shareManager,
                            title = chatTitle,
                            userName = userName,
                            tarasName = tarasName,
                            fromLabel = fromLabel,
                            toastMessage = copiedLabel
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.copy_description)
                        )
                    }
                    if (onToggleFullScreen != null) {
                        IconButton(onClick = onToggleFullScreen) {
                            Icon(
                                Icons.Default.Fullscreen,
                                contentDescription = stringResource(Res.string.full_screen_description)
                            )
                        }
                    }
                    if (onClose != null) {
                        IconButton(onClick = onClose) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(Res.string.close_description)
                            )
                        }
                    }
                }
            }
            Divider()
        }

        if (viewModel.currentPageNumber != null) {
            Surface(
                color = MaterialTheme.colors.primary.copy(alpha = 0.05f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        Res.string.discussing_page,
                        viewModel.currentPageNumber!! + 1
                    ),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    ),
                    color = MaterialTheme.colors.primary
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            SelectionContainer {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages) { message ->
                        MessageBubble(message)
                    }
                }
            }
        }

        Divider()

        Surface(elevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .windowInsetsPadding(
                        if (isFullScreen) WindowInsets.ime.union(WindowInsets.navigationBars)
                        else WindowInsets(0, 0, 0, 0)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text(stringResource(Res.string.ask_placeholder)) },
                    enabled = !isLoading,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                IconButton(
                    onClick = {
                        viewModel.sendMessage(
                            text = inputText,
                            pageNumber = viewModel.currentPageNumber,
                            pageText = viewModel.currentPageText,
                        )
                        inputText = ""
                    },
                    enabled = !isLoading && inputText.isNotBlank()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = stringResource(Res.string.send_description),
                            tint = if (inputText.isNotBlank()) MaterialTheme.colors.primary else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == MessageRole.USER
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val backgroundColor = if (isUser) {
        MaterialTheme.colors.primary.copy(alpha = 0.1f)
    } else {
        MaterialTheme.colors.surface
    }
    val bubbleShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 0.dp,
        bottomEnd = if (isUser) 0.dp else 16.dp
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Surface(
            modifier = Modifier
                .clip(bubbleShape)
                .widthIn(max = 280.dp),
            color = backgroundColor,
            elevation = if (isUser) 0.dp else 2.dp,
            shape = bubbleShape,
            border = if (isUser) null else androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.LightGray.copy(alpha = 0.5f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (isUser) stringResource(Res.string.user_name) else stringResource(
                        Res.string.taras_shevchenko_name
                    ),
                    style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}
