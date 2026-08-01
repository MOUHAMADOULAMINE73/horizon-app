package com.horizon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.horizon.app.network.Categories
import com.horizon.app.network.Listing
import com.horizon.app.ui.AppViewModel
import com.horizon.app.ui.theme.HorizonOrange
import com.horizon.app.ui.theme.HorizonTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: AppViewModel,
    onListingClick: (Listing) -> Unit
) {
    val listings by viewModel.listings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.loadListings() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Horizon", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Rechercher une annonce...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSearch = { viewModel.loadListings(selectedCategory, searchQuery) }
                )
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null; viewModel.loadListings(null, searchQuery) },
                        label = { Text("Tout") }
                    )
                }
                items(Categories.ALL) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat; viewModel.loadListings(cat, searchQuery) },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (isLoading && listings.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (listings.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune annonce pour l'instant.\nSois le premier à publier !", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(listings) { listing ->
                        ListingCard(listing = listing, onClick = { onListingClick(listing) })
                    }
                }
            }
        }
    }
}

@Composable
fun ListingCard(listing: Listing, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            if (listing.mediaUrl != null) {
                AsyncImage(
                    model = imageFullUrl(listing.mediaUrl),
                    contentDescription = listing.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            Column(Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = listing.category,
                        fontSize = 12.sp,
                        color = HorizonOrange,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(HorizonOrange.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                    if (listing.price != null) {
                        Text("${listing.price.toInt()} DH", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(listing.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                Text(
                    listing.description,
                    fontSize = 13.sp,
                    color = HorizonTextSecondary,
                    maxLines = 2
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = HorizonTextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(2.dp))
                    Text(listing.city ?: "Maroc", fontSize = 12.sp, color = HorizonTextSecondary)
                }
            }
        }
    }
}

fun imageFullUrl(relativePath: String): String {
    if (relativePath.startsWith("http")) return relativePath
    val base = "https://REMPLACE-MOI.onrender.com"
    return "$base$relativePath"
}
