package com.example.sofarotator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Information screen with educational content
 * This is a placeholder - will be expanded in the next phase
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top app bar
        TopAppBar(
            title = { 
                Text(
                    text = "4D Tesseract Info",
                    color = Color.White
                ) 
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Cyan
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.8f)
            )
        )
        
        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome to the 4th Dimension",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Cyan,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            item {
                Text(
                    text = """
                    The tesseract is a fantastic thing to behold. While dimensions seem straightforward - left-right, up-down, and back-forth - nature has a way of being more imaginative than expected.

                    The first encounter with a rotating tesseract triggers an interesting response in the human brain. It automatically attempts to interpret the motion within the familiar framework of three dimensions. The initial impression might be of two cubes, one nested inside the other, performing an intricate dance. But this interpretation barely scratches the surface of what's actually occurring.

                    Consider an ant living on a flat piece of paper. In that two-dimensional world, forward-backward and left-right are the only possible movements. The concept of "up" would be as incomprehensible to the ant as a fourth dimension is to three-dimensional thinking. If a three-dimensional cube were to pass through the ant's flat world, it would appear as a series of inexplicable, changing shapes that seem to violate every rule of planar geometry.

                    This same principle applies to the tesseract, but shifted up one dimension. The three-dimensional space we inhabit is like that flat paper, but with one extra dimension. The tesseract passing through this space creates apparent impossibilities - lines and faces that seem to do impossible things. Yet these movements are perfectly natural in four dimensions, only appearing strange when projected into three-dimensional space.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4
                )
            }
            
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Blue.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Controls",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Cyan
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = """
                            • Settings Button: Open rotation controls
                            • Play/Pause: Toggle auto-rotation
                            • Wireframe/Solid: Change visualization mode
                            • Cross-Section: View 4D slices
                            • Speed Controls: Adjust rotation speeds for each 4D plane
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }
            
            item {
                Text(
                    text = """
                    The most remarkable aspect occurs after sustained observation. Much like the sudden clarity that comes when understanding an optical illusion, there can be moments of profound insight. For a fraction of a second, the mind grasps the possibility of movement in a direction beyond the usual three - not left or right or up or down or forward or back, but somewhere entirely different. It's a glimpse into the geometry of higher dimensions.

                    Mathematics transcends mere symbol manipulation. It reveals patterns that challenge everyday experience, patterns that demonstrate how the universe extends beyond simple imagination. The tesseract serves as a window into this larger reality, offering a concrete visualization of abstract higher-dimensional mathematics.

                    What a marvelous glimpse into the architecture of higher dimensions.
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4
                )
            }
        }
    }
} 