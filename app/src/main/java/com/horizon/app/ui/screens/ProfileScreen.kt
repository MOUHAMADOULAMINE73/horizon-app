package com.horizon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.horizon.app.ui.AppViewModel
import com.horizon.app.ui.theme.HorizonOrange
import com.horizon.app.ui.theme.HorizonTextSecondary

@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(HorizonOrange.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user?.fullName?.take(1)?.uppercase() ?: "?",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = HorizonOrange
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(user?.fullName ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(user?.email ?: "", fontSize = 14.sp, color = HorizonTextSecondary)
        if (!user?.city.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(user?.city ?: "", fontSize = 13.sp, color = HorizonTextSecondary)
        }

        Spacer(Modifier.height(40.dp))

        OutlinedButton(
            onClick = { viewModel.logout(); onLogout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se déconnecter")
        }
    }
}
