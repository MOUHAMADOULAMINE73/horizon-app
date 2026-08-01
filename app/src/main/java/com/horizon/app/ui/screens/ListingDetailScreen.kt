package com.horizon.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.horizon.app.network.Listing
import com.horizon.app.ui.theme.HorizonOrange
import com.horizon.app.ui.theme.HorizonTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listing: Listing,
    onBack: () -> Unit,
    onContactSeller: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(listing.title, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { onContactSeller(listing.userId) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
                ) {
                    Text("Contacter ${listing.authorName ?: "le vendeur"}")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (listing.mediaUrl != null) {
                AsyncImage(
                    model = imageFullUrl(listing.mediaUrl),
                    contentDescription = listing.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }

            Column(Modifier.padding(20.dp)) {
                Text(
                    text = listing.category,
                    color = HorizonOrange,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(listing.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))

                if (listing.price != null) {
                    Text("${listing.price.toInt()} DH", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = HorizonOrange)
                    Spacer(Modifier.height(8.dp))
                }

                Row {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = HorizonTextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(listing.city ?: "Maroc", color = HorizonTextSecondary, fontSize = 14.sp)
                }

                Spacer(Modifier.height(20.dp))
                Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(6.dp))
                Text(listing.description, fontSize = 14.sp, lineHeight = 20.sp)

                Spacer(Modifier.height(20.dp))
                Text("Publié par ${listing.authorName ?: "Utilisateur Horizon"}", fontSize = 13.sp, color = HorizonTextSecondary)
                Spacer(Modifier.height(80.dp))
            }
        }
    }
}
