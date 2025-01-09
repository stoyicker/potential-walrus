package co.touchlab.spotnotes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import co.touchlab.spotnotes.note.NoteFactory
import co.touchlab.spotnotes.permission.Permission
import co.touchlab.spotnotes.permission.RequestResult
import co.touchlab.spotnotes.permission.buildPermissionManager
import co.touchlab.spotnotes.ui.HistoryView
import co.touchlab.spotnotes.ui.SaveView
import co.touchlab.spotnotes.ui.ui.theme.SpotNotesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    sealed class Screen(
        val route: String, @StringRes val resourceId: Int, val imageVector: ImageVector
    ) {
        data object Save : Screen("save", R.string.app_name, Icons.Filled.Edit)
        data object History :
            Screen("history", R.string.title_history, Icons.AutoMirrored.Filled.List)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()
        setContent { ContentView(NoteFactory()) }
    }

    private fun requestPermissions() {
        lifecycleScope.launch {
            val permissionManager = buildPermissionManager(this@MainActivity)
            when (permissionManager.request(Permission.LOCATION_PRECISION_HIGH)) {
                RequestResult.NEWLY_GRANTED, RequestResult.ALREADY_GRANTED ->
                    Log.i("Location", "Have ${Permission.LOCATION_PRECISION_HIGH.asAndroidKey}")

                else -> when (permissionManager.request(Permission.LOCATION)) {
                    RequestResult.NEWLY_GRANTED, RequestResult.ALREADY_GRANTED ->
                        Log.i("Location", "Have ${Permission.LOCATION.asAndroidKey}")

                    else -> Log.i("Location", "Did not get location permissions")
                }
            }
        }
    }
}

@Composable
fun ContentView(noteFactory: NoteFactory) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel(factory = viewModelFactory {
        initializer { MainViewModel(noteFactory) }
    })

    val uiState: UIState by mainViewModel.uiState.collectAsState()

    SpotNotesTheme {
        Scaffold(containerColor = MaterialTheme.colorScheme.background, bottomBar = {
            NavBar(navController = navController)
        }) {
            NavHost(navController, startDestination = MainActivity.Screen.Save.route, Modifier.padding(it)) {
                composable(MainActivity.Screen.Save.route) {
                    SaveView { noteText ->
                        mainViewModel.saveNote(noteText)
                        navController.navigate(MainActivity.Screen.History.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                composable(MainActivity.Screen.History.route) { HistoryView(uiState.notes) }
            }
        }
    }
}

@Composable
fun NavBar(navController: NavController) {
    val items = listOf(MainActivity.Screen.Save, MainActivity.Screen.History)
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            NavigationBarItem(icon = {
                Icon(
                    screen.imageVector, contentDescription = null
                )
            },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}
