package com.example.driverlogisticsapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.driverlogisticsapp.ui.screens.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val deliveryViewModel: DeliveryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.Companion
            ) {
                @Serializable
                data class Profile(val name: String)

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Profile(name = "Santi")) {
                    composable<Profile> { backStackEntry ->
                        val profile: Profile = backStackEntry.toRoute()
                        MainScreen(deliveryViewModel)
                    }
                }
            }
        }
        deliveryViewModel.loadDeliveryList()
    }
}