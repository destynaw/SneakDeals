package com.example.sneakdeals.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.R
import com.example.sneakdeals.ui.theme.SneakDealsTheme

data class Project(
    val id: String,
    val name: String,
    val creator: String,
    val story: String,
    val imageRes: Int,
    val targetGoal: Long,
    val currentPledge: Long,
    val daysLeft: Int
)

val dummyProjects = listOf(
    Project("proj1", "Puma 'Future Rider' - Neo Edition", "by Tinker Hatfield Jr.", "A retro-futuristic take on a classic silhouette, blending 80s vibes with modern materials.", R.drawable.speedcat_galaxy, 100000000, 75000000, 15),
    Project("proj2", "Puma 'Suede' - Urban Art Series", "by Shepard Fairey", "A collaboration with street artists to bring graffiti art to the iconic Puma Suede.", R.drawable.speedcat_grey, 120000000, 40000000, 25),
    Project("proj3", "Puma 'RS-X' - Eco-Friendly Concept", "by Stella McCartney", "A sustainable sneaker made from recycled ocean plastics and natural rubber.", R.drawable.puma_boys1, 80000000, 78000000, 5),
    Project("proj4", "Puma 'Cali' - Translucent Glow", "by Virgil Abloh", "A bold redesign featuring a translucent sole that glows in the dark.", R.drawable.puma_boys10, 150000000, 110000000, 20)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SneakLaunchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SneakLaunch", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text("Wujudkan Desain Sepatu Impian", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Dukung proyek kreatif dari desainer independen dan dapatkan sepatu edisi terbatas.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }
            items(dummyProjects) { project ->
                ProjectCard(project = project, navController = navController)
            }
        }
    }
}

@Composable
fun ProjectCard(project: Project, navController: NavController) {
    val progress = project.currentPledge.toFloat() / project.targetGoal.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth().clickable { 
            // TODO: Navigate to project detail screen
            // navController.navigate(Screen.ProjectDetail.createRoute(project.id))
         },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = project.imageRes),
                contentDescription = project.name,
                modifier = Modifier.fillMaxWidth().height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(project.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text("oleh ${project.creator}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color.Red
                )

                Spacer(Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Rp${String.format("%,d", project.currentPledge)}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Red)
                    Text("${(progress * 100).toInt()}% tercapai", fontSize = 14.sp, color = Color.Gray)
                }
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                     Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.LightGray.copy(alpha = 0.3f)).padding(horizontal = 12.dp, vertical = 6.dp)){
                        Text("${project.daysLeft} hari lagi", fontWeight = FontWeight.Bold)
                     }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text("Dukung Proyek")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SneakLaunchScreenPreview() {
    SneakDealsTheme {
        SneakLaunchScreen(rememberNavController())
    }
}
