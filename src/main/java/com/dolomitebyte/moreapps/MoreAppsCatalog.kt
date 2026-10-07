package com.dolomitebyte.moreapps

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** A single allowlist for the DolomiteByte apps promoted inside our Android apps. */
enum class DolomiteApp(
    val packageName: String,
    @StringRes val nameRes: Int,
    @StringRes val summaryRes: Int,
    @DrawableRes val iconRes: Int,
    val publishedOnPlay: Boolean,
) {
    PUBDASH(
        "com.dolomitebyte.pubdash",
        R.string.more_apps_pubdash_name,
        R.string.more_apps_pubdash_summary,
        R.drawable.more_apps_pubdash,
        true,
    ),
    TOOKACTION(
        "com.dolomitebyte.tookaction",
        R.string.more_apps_tookaction_name,
        R.string.more_apps_tookaction_summary,
        R.drawable.more_apps_tookaction,
        false,
    ),
    FILECO(
        "com.dolomitebyte.fileco",
        R.string.more_apps_fileco_name,
        R.string.more_apps_fileco_summary,
        R.drawable.more_apps_fileco,
        true,
    ),
    EVERSCAN(
        "com.dolomitebyte.everscan",
        R.string.more_apps_everscan_name,
        R.string.more_apps_everscan_summary,
        R.drawable.more_apps_everscan,
        true,
    ),
    THERABUDDY(
        "com.dolomitebyte.therabuddy",
        R.string.more_apps_therabuddy_name,
        R.string.more_apps_therabuddy_summary,
        R.drawable.more_apps_therabuddy,
        true,
    ),
}

fun moreAppsFor(hostPackageName: String): List<DolomiteApp> =
    DolomiteApp.entries.filter { app ->
        app.publishedOnPlay &&
            hostPackageName != app.packageName &&
            !hostPackageName.startsWith("${app.packageName}.")
    }
