package com.ivancot.playlistmaker.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivancot.playlistmaker.R

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val shareText = stringResource(R.string.share_message)
    val shareTitle = stringResource(R.string.share_app)
    val email = stringResource(R.string.developer_email)
    val subject = stringResource(R.string.email_subject)
    val body = stringResource(R.string.email_body)
    val offer = stringResource(R.string.offer_url)
    val noShare = stringResource(R.string.no_share_app)
    val noEmail = stringResource(R.string.no_email_app)
    val noBrowser = stringResource(R.string.no_browser_app)
    fun launch(intent: Intent, error: String) {
        try { context.startActivity(intent) }
        catch (_: ActivityNotFoundException) { Toast.makeText(context, error, Toast.LENGTH_LONG).show() }
    }
    Column(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        ScreenHeader(R.string.settings, onBack)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 16.dp)) {
            Row(
                Modifier.fillMaxWidth().heightIn(min = 64.dp).padding(start = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(stringResource(R.string.dark_theme), fontSize = 16.sp, modifier = Modifier.weight(1f))
                Switch(checked = false, onCheckedChange = null, enabled = false)
            }
            SettingsAction(R.string.share_app, R.drawable.ic_share) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                launch(Intent.createChooser(intent, shareTitle), noShare)
            }
            SettingsAction(R.string.contact_developers, R.drawable.ic_support) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:${Uri.encode(email, "@")}?subject=${Uri.encode(subject)}&body=${Uri.encode(body)}")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                launch(intent, noEmail)
            }
            SettingsAction(R.string.user_agreement, R.drawable.ic_chevron_right) {
                launch(Intent(Intent.ACTION_VIEW, Uri.parse(offer)), noBrowser)
            }
        }
    }
}

@Composable
private fun SettingsAction(@StringRes label: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.White, modifier = Modifier.fillMaxWidth().heightIn(min = 61.dp)) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(label), fontSize = 16.sp, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(12.dp))
            Icon(painterResource(icon), null, tint = Color(0xFFAEAFB4), modifier = Modifier.size(24.dp))
        }
    }
}
