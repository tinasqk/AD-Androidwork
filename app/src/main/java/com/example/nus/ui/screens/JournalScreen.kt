package com.example.nus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nus.viewmodel.JournalViewModel

@Composable
fun JournalScreen(viewModel: JournalViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Save & Go Home Button (top right) - 对应XML第10-18行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { 
                        viewModel.saveAndGoHome()
                        // TODO: 实现返回主页逻辑
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Save & Go Home")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp)) // 对应android:layout_marginTop="16dp"
            
            // Date Header - 对应XML第21-30行
            Text(
                text = viewModel.getFormattedDate(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp)) // 对应android:layout_marginTop="12dp"
            
            // Icon Navigation Row (empty) - 对应XML第33-43行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                // 空的图标行，保持与原XML一致
            }
            
            Spacer(modifier = Modifier.height(24.dp)) // 对应android:layout_marginTop="24dp"
            
            // Prompt - 对应XML第46-54行
            Text(
                text = "Write about the day's events and feelings.",
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp)) // 对应android:layout_marginTop="12dp"
            
            // Title EditText - 对应XML第57-68行
            OutlinedTextField(
                value = viewModel.title.value,
                onValueChange = { viewModel.title.value = it },
                label = { Text("Start with a title to remember this day.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp), // 模拟原XML的背景样式
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp)) // 对应android:layout_marginTop="12dp"
            
            // Journal Body EditText - 对应XML第71-86行
            OutlinedTextField(
                value = viewModel.content.value,
                onValueChange = { viewModel.content.value = it },
                label = { Text("Today, I...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // 对应XML的0dp height和约束到底部
                shape = RoundedCornerShape(4.dp),
                minLines = 8 // 对应android:minLines="8"
            )
            
            Spacer(modifier = Modifier.height(16.dp)) // 对应android:layout_marginBottom="16dp"
        }
        
        // Save & Continue Button (bottom center) - 对应XML第89-96行
        Button(
            onClick = { 
                viewModel.saveAndContinue()
                // TODO: 实现继续逻辑
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text("Save & Continue")
        }
    }
}
