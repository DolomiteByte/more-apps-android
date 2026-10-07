package com.dolomitebyte.moreapps

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MoreAppsCatalogTest {
    @Test fun eachHostSeesOtherPublishedApps() {
        for (host in DolomiteApp.entries) {
            val visible = moreAppsFor(host.packageName)
            assertFalse(visible.contains(host))
            assertFalse(visible.contains(DolomiteApp.TOOKACTION))
            assertEquals(4 - if (host.publishedOnPlay) 1 else 0, visible.size)
        }
    }

    @Test fun debugBuildDoesNotPromoteItself() {
        assertFalse(moreAppsFor("com.dolomitebyte.pubdash.debug").contains(DolomiteApp.PUBDASH))
    }
}
