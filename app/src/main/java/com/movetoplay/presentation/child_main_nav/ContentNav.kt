package com.movetoplay.presentation.child_main_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.movetoplay.domain.model.TypeExercise
import com.movetoplay.presentation.ui.component_widgets.BottomNavItem
import com.movetoplay.presentation.ui.child_home.Home
import com.movetoplay.presentation.ui.child_my_achievements.MyAchievements
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentNav(
    viewModel: ProfileChildVM = hiltViewModel(),
    openCameraForExercise: (TypeExercise)->Unit
) {
    val nav= rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(nav, listOf(ContentRoute.Home, ContentRoute.MyAccomplishments) )
        }
    ){ paddingValues ->
        NavHost(nav, ContentRoute.Home.route, Modifier.padding(paddingValues)){
            composable(ContentRoute.Home.route){
                Home(
                    viewModel = viewModel,
                    openCameraForExercise = openCameraForExercise
                )
            }
            composable(ContentRoute.MyAccomplishments.route){
                MyAchievements(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    nav: NavHostController,
    listRoute: List<ContentRoute>
){
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Row(
        Modifier
            .shadow(8.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listRoute.forEach { item->
            BottomNavItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    nav.navigate(item.route) {
                        popUpTo(nav.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                iconRes = item.iconRes,
            )
        }
    }
}
