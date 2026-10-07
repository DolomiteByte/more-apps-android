package com.dolomitebyte.moreapps

import android.content.Context
import android.content.Intent
import android.net.Uri

/** Opens a listing only after an explicit tap. Never requests an install. */
fun openGooglePlayListing(context: Context, app: DolomiteApp): Boolean {
    if (!app.publishedOnPlay) return false
    val httpsUri = Uri.parse("https://play.google.com/store/apps/details?id=${app.packageName}")
    val storeIntent = Intent(Intent.ACTION_VIEW, httpsUri)
        .setPackage("com.android.vending")
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (runCatching { context.startActivity(storeIntent) }.isSuccess) return true
    val browserIntent = Intent(Intent.ACTION_VIEW, httpsUri)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return runCatching { context.startActivity(browserIntent) }.isSuccess
}
