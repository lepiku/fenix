/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.fenix.ui

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.helpers.HomeActivityTestRule
import org.mozilla.fenix.helpers.TestAssetHelper
import org.mozilla.fenix.helpers.TestHelper.restartApp
import org.mozilla.fenix.ui.robots.addToHomeScreen
import org.mozilla.fenix.ui.robots.homeScreen

/**
 *  Tests for verifying navigation and automatically launching links in Private mode
 *
 *
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class PrivateModeTest {
    /* ktlint-disable no-blank-line-before-rbrace */ // This imposes unreadable grouping.

    val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private lateinit var mockWebServer: MockWebServer

    private val pageShortcutName = "TestShortcut"

    @get:Rule
    val activityTestRule = HomeActivityTestRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            setDispatcher(AndroidAssetDispatcher())
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun launchPageShortcutInPrivateModeTest() {
        val defaultWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        setOpenLinksInPrivateOn()

        addToHomeScreen {
            addHomeScreenShortcut(defaultWebPage.url, pageShortcutName)
        }.openHomeScreenShortcut(pageShortcutName) {
        }.openHomeScreen {
            verifyPrivateSessionHeader()
        }
    }

    @Test
    fun launchLinksInPrivateToggleOffStateDoesntChangeTest() {
        val defaultWebPage = TestAssetHelper.getGenericAsset(mockWebServer, 1)

        setOpenLinksInPrivateOn()

        addToHomeScreen {
            addHomeScreenShortcut(defaultWebPage.url, pageShortcutName)
        }.openHomeScreenShortcut(pageShortcutName) {
        }.openHomeScreen { }

        setOpenLinksInPrivateOff()

        restartApp(activityTestRule)

        addToHomeScreen {
        }.openHomeScreenShortcut(pageShortcutName) {
        }.openHomeScreen {
            verifyOpenTabsHeader()
        }.openThreeDotMenu {
        }.openSettings {
        }.openDefaultBrowserSubMenu {
            verifyOpenLinksInPrivateTabUnchecked()
        }

    }

    @Ignore
    @Test
    fun persistPrivateModeTest() {

    }
}

fun setOpenLinksInPrivateOn() {

    homeScreen {
    }.openThreeDotMenu {
    }.openSettings {
    }.openDefaultBrowserSubMenu {
        clickSetDefaultBrowserToggle()
        selectDefaultBrowser()
        verifyOpenLinksInPrivateTabEnabled()
        clickOpenLinksInPrivateTabCheckbox()
    }.goBack {
    }.goBack {
    }
}

private fun setOpenLinksInPrivateOff() {
    homeScreen {
    }.openThreeDotMenu {
    }.openSettings {
    }.openDefaultBrowserSubMenu {
        clickOpenLinksInPrivateTabCheckbox()
        verifyOpenLinksInPrivateTabUnchecked()
    }.goBack {
    }.goBack {}
}