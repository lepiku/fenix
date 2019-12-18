package org.mozilla.fenix.ui

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mozilla.fenix.IntentReceiverActivity
import org.mozilla.fenix.helpers.AndroidAssetDispatcher
import org.mozilla.fenix.ui.robots.settingsSubMenuDefaultBrowser

class LaunchScreenIntentTest {

    private val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private lateinit var mockWebServer: MockWebServer

    private val pageShortcutName = "TestShortcut"

    /**
     * A JUnit [@Rule][Rule] to init and release Espresso Intents before and after each
     * test run.
     *
     *
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the [@Before][Before] method.
     *
     *
     * This rule is based on [ActivityTestRule] and will create and launch the activity
     * for you and also expose the activity under test.
     */
    @get:Rule
    var activityRule = ActivityTestRule(IntentReceiverActivity::class.java, true, false)

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
    fun clickTheSwitch() {
        val intent = Intent()
        val uri = Uri.fromParts(
            "org.mozilla.fenix.settings",
            "DefaultBrowserSettingsFragment.kt",
            null
        )
        intent.setData(uri)
        intent.putExtra("R.string.pref_key_open_links_in_a_private_tab", true)
        activityRule.launchActivity(intent)

        settingsSubMenuDefaultBrowser {
            verifyOpenLinksInPrivateTabEnabled()
        }
    }
}

//@Test
//fun turnOnTheSwitch() {
//    homeScreen {
//    }.openThreeDotMenu {
//    }.openSettings {
//    }.openDefaultBrowserSubMenu {
//        clickSetDefaultBrowserToggle()
//    }
//    intended(
//        allOf(
//            hasPackage("com.android.settings"),
//            hasAction(ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
//        )
//    )
//
//    mDevice.findObject((UiSelector().text("Browser app"))).click()
//    mDevice.findObject(UiSelector().text("Firefox Preview")).click()
//    mDevice.pressBack()
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        mDevice.pressBack()
//    }
//}
//}