package com.dolomitebyte.moreapps

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Full-page destination for apps with a route stack. Uses the host's Material 3 theme. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreAppsScreen(
    hostPackageName: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onOpenApp: ((DolomiteApp) -> Unit)? = null,
) {
    Scaffold(
        modifier = modifier.testTag("more_apps_screen"),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.more_apps_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.more_apps_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        MoreAppsContent(
            hostPackageName = hostPackageName,
            modifier = Modifier.padding(padding),
            onOpenApp = onOpenApp,
        )
    }
}

/** Content-only variant for a host which already owns the toolbar and back navigation. */
@Composable
fun MoreAppsContent(
    hostPackageName: String,
    modifier: Modifier = Modifier,
    onOpenApp: ((DolomiteApp) -> Unit)? = null,
) {
    val context = LocalContext.current
    val apps = moreAppsFor(hostPackageName)
    LazyColumn(
        modifier = modifier.fillMaxSize().testTag("more_apps_list"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.more_apps_intro),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        items(apps, key = DolomiteApp::packageName) { app ->
            MoreAppCard(
                app = app,
                onClick = {
                    if (onOpenApp != null) onOpenApp(app)
                    else openGooglePlayListing(context, app)
                },
            )
        }
    }
}

@Composable
private fun MoreAppCard(app: DolomiteApp, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("more_apps_${app.name.lowercase()}")
            .clickable(role = Role.Button, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(app.iconRes),
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.large),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = stringResource(app.nameRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(app.summaryRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(R.string.more_apps_open_store),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Icon(
                Icons.AutoMirrored.Rounded.OpenInNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(name = "More apps from TheraBuddy", showBackground = true, widthDp = 390, heightDp = 760)
@Composable
private fun MoreAppsScreenPreview() {
    MaterialTheme {
        MoreAppsScreen(hostPackageName = DolomiteApp.THERABUDDY.packageName, onBack = {})
    }
}

@Preview(name = "More apps from TookAction", showBackground = true, widthDp = 390, heightDp = 760)
@Composable
private fun MoreAppsContentPreview() {
    MaterialTheme {
        MoreAppsContent(hostPackageName = DolomiteApp.TOOKACTION.packageName, onOpenApp = {})
    }
}
