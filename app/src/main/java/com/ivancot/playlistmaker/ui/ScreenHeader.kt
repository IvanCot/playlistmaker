package com.ivancot.playlistmaker.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.R

@Composable
internal fun ScreenHeader(@StringRes title: Int, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp), verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onBack) {
            Icon(painterResource(R.drawable.ic_back), stringResource(R.string.back))
        }
        Text(stringResource(title), fontSize = 22.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(8.dp))
    }
}
