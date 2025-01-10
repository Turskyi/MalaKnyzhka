package com.turskyi.malaknyzhka.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import com.turskyi.malaknyzhka.models.PageSettings
import com.turskyi.malaknyzhka.router.NavigationDestination
import com.turskyi.malaknyzhka.ui.book.Page
import com.turskyi.malaknyzhka.ui.landing.LandingPage
import com.turskyi.malaknyzhka.ui.privacy.PrivacyPolicyPage
import com.turskyi.malaknyzhka.util.isOnWeb
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    settings: Settings,
    navController: NavHostController = rememberNavController()
) {
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = if (isOnWeb())
                NavigationDestination.Landing.name
            else
                NavigationDestination.Book.name
        ) {
            composable(route = NavigationDestination.Landing.name) {
                LandingPage(
                    onNavigateToBook = {
                        navController.navigate(NavigationDestination.Book.name)
                    },
                    onNavigateToPrivacyPolicy = {
                        navController.navigate(
                            NavigationDestination.PrivacyPolicy.name,
                        )
                    }
                )
            }
            composable(route = NavigationDestination.Book.name) {
                Page(PageSettings(settings))
            }
            composable(route = NavigationDestination.PrivacyPolicy.name) {
                PrivacyPolicyPage(onBack = { navController.popBackStack() })
            }
        }
    }
}