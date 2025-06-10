package com.mindshield.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindshield.app.ui.theme.RedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "MindShield", 
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RedPrimary,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Handle settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = RedPrimary
            ) {
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Home, 
                            contentDescription = "Home",
                            tint = if (selectedTab == 0) RedPrimary else Color.Gray
                        ) 
                    },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.BarChart, 
                            contentDescription = "Stats",
                            tint = if (selectedTab == 1) RedPrimary else Color.Gray
                        ) 
                    },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { 
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RedPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add, 
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    selected = false,
                    onClick = { /* Handle add action */ }
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Notifications, 
                            contentDescription = "Alerts",
                            tint = if (selectedTab == 3) RedPrimary else Color.Gray
                        ) 
                    },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Person, 
                            contentDescription = "Profile",
                            tint = if (selectedTab == 4) RedPrimary else Color.Gray
                        ) 
                    },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RedPrimary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    ) { padding ->
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome to MindShield",
                style = MaterialTheme.typography.headlineMedium,
                color = RedPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Your mental wellness companion",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    }
}
