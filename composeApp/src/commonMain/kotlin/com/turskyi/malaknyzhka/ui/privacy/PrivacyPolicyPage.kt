package com.turskyi.malaknyzhka.ui.privacy

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.logo
import malaknyzhka.composeapp.generated.resources.logo_description
import malaknyzhka.composeapp.generated.resources.privacy_policy_ukr_eng
import malaknyzhka.composeapp.generated.resources.privacy_ukr_eng
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PrivacyPolicyPage(onBack: () -> Unit) {
    val scrollState: ScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.privacy_policy_ukr_eng),
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 2.dp,
                        ),
                        onClick = onBack
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = stringResource(
                                Res.string.logo_description,
                            ),
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            SelectionContainer {
                Text(text = stringResource(Res.string.privacy_ukr_eng))
            }
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}
