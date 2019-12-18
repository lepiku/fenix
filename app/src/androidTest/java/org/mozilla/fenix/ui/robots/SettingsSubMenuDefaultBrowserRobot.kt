/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

@file:Suppress("TooManyFunctions")

package org.mozilla.fenix.ui.robots

import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By.textContains
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until.findObject
import org.junit.Assert.assertFalse
import org.mozilla.fenix.helpers.TestAssetHelper.waitingTime
import org.mozilla.fenix.helpers.click

/**
 * Implementation of Robot Pattern for the settings DefaultBrowser sub menu.
 */

class SettingsSubMenuDefaultBrowserRobot {

    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun verifyOpenLinksInPrivateTabEnabled() = assertOpenLinksInPrivateTabEnabled()

    fun verifyOpenLinksInPrivateTabUnchecked() = assertOpenLinksInPrivateTabUnchecked()

    fun clickOpenLinksInPrivateTabCheckbox() = openLinksInPrivateTabCheckbox().click()

    fun clickSetDefaultBrowserToggle() = setDefaultBrowserToggle().clickAndWaitForNewWindow()

    fun selectDefaultBrowser() {
        mDevice.waitForWindowUpdate("com.android.settings", waitingTime)
        mDevice.wait(findObject(textContains("Browser app")), waitingTime)
        defaultBrowserAppList().click()
        mDevice.wait(findObject(textContains("Firefox Preview")), waitingTime)
        defaultAppPreference().click()
        mDevice.pressBack()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDevice.pressBack()
        }
        mDevice.waitForWindowUpdate("org.mozilla.fenix.debug", waitingTime)
    }

    class Transition {
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        fun goBack(interact: SettingsRobot.() -> Unit): SettingsRobot.Transition {
            mDevice.waitForIdle()
            goBackButton().perform(ViewActions.click())

            SettingsRobot().interact()
            return SettingsRobot.Transition()
        }
    }
}

fun settingsSubMenuDefaultBrowser(interact: SettingsSubMenuDefaultBrowserRobot.() -> Unit): SettingsSubMenuDefaultBrowserRobot.Transition {
    SettingsSubMenuDefaultBrowserRobot().interact()
    return SettingsSubMenuDefaultBrowserRobot.Transition()
}

private fun assertOpenLinksInPrivateTab() {
    openLinksInPrivateTabCheckbox()
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

private fun assertOpenLinksInPrivateTabEnabled() =
    openLinksInPrivateTabCheckbox().check(matches(isEnabled()))


private fun assertOpenLinksInPrivateTabUnchecked() {
    assertFalse(mDevice.findObject(UiSelector().resourceId("android:id/checkbox")).isChecked())
}

private fun goBackButton() = onView(withContentDescription("Navigate up"))

private fun setDefaultBrowserToggle() =
    mDevice.findObject(UiSelector().resourceId("org.mozilla.fenix.debug:id/switch_widget"))

private fun openLinksInPrivateTabCheckbox() = onView(withText("Open links in a private tab"))

private fun defaultBrowserAppList() = mDevice.findObject(
    UiSelector().textContains("Browser app")
)

private fun defaultAppPreference() =
    mDevice.findObject(UiSelector().textContains("Firefox Preview"))
