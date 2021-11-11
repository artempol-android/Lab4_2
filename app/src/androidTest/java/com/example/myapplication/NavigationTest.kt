package com.example.myapplication

import android.content.pm.ActivityInfo
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    var mainRule = ActivityScenarioRule(MainActivity::class.java)

    private fun checkAboutViaOptions() {
        fun openAbout() = openAboutViaOptions()
        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))
    }

    private fun checkAboutViaDrawer() {
        fun openAbout() = openAboutViaDrawer()
        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))
    }

    private fun checkAboutViaBottomNav() {
        openAbout()
        onView(withId(R.id.activity_about)).check(matches(isDisplayed()))
    }

    private fun upButton() {
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
    }
/*
  ==============================================================================
  Проверяем всевозможные перемещения между фрагментами и состояние backStack`a
  ===============================================================================
*/

    @Test
    fun fr1_destroy() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // C помощью pressBack
    @Test
    fun fr1_fr2_fr1_destroy() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // C помощью кнопок
    @Test
    fun fr1_fr2_fr1_destroy_viaButtons() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // C помощью pressBack
    @Test
    fun fr1_fr2_fr3_fr2_fr1_destroy() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // c помощью кнопок
    @Test
    fun fr1_fr2_fr3_fr2_fr1_destroy_viaButtons() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    // тут pressBack`ом не проверить
    @Test
    fun fr1_fr2_fr3_fr1_destroy() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToThird)).perform(click())
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

/*
  ==============================================================================================
  Проверяем открывается ли AboutActivity из каждого фрагмента, а затем провереям
  Сохранился ли BackStack в правиьном порядке

  Также проверяем возножность выхода c помощью кнопки "Вверх"

  (сначала хотел протестировать со всеми 3мя видами открытия, но это будет x3 кода и, как я понял,
  это необязательно)

  И + пока мы не добрались до AboutActivity, мы не будем проверять, какой фрагмент отбражается,
   т.к. это проверялось ранее
  ===============================================================================================
*/
    // Bottom
    @Test
    fun fr1_about_fr1_destroy1_viaUp() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkAboutViaBottomNav()

        upButton()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun fr1_about_fr1_destroy1() {
        launchActivity<MainActivity>()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkAboutViaBottomNav()

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента2 возвращаемся pressBack`om
    @Test
    fun fr1_fr2_about_fr2_fr1_destroy_viaUpButton1() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkAboutViaBottomNav()
        upButton()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента2 возвращаемся button`om
    @Test
    fun fr1_fr2_about_fr2_fr1_destroy_viaUpButton2() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkAboutViaBottomNav()
        upButton()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента2 возвращаемся pressBack`om
    @Test
    fun fr1_fr2_about_fr2_fr1_destroy1() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkAboutViaBottomNav()
        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента2 возвращаемся button`om
    @Test
    fun fr1_fr2_about_fr2_fr1_destroy2() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())

        checkAboutViaBottomNav()
        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента3 возвращаемся pressBack`om
    @Test
    fun fr1_fr2_fr3_about_fr3_fr2_fr1_destroy() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента3 возвращаемся button`amи
    @Test
    fun fr1_fr2_fr3_about_fr3_fr2_fr1_destroy2() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента3 возвращаемся pressBack`om
    @Test
    fun fr1_fr2_fr3_about_fr3_fr2_fr1_destroy_viaUpButton() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        upButton()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //из фрагмента3 возвращаемся button`amи
    @Test
    fun fr1_fr2_fr3_about_fr3_fr2_fr1_destroy_viaUpButton2() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        upButton()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun fr1_fr2_fr3_about_fr3_fr1_destroy() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun fr1_fr2_fr3_about_fr3_fr1_destroy_viaUpButton() {
        launchActivity<MainActivity>()

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        checkAboutViaBottomNav()
        upButton()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)

    }

    //Options
    @Test
    fun fr1_about_fr1_destroy2_viaUp() {
        launchActivity<MainActivity>()

        checkAboutViaOptions()
        upButton()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun fr1_about_fr1_destroy2() {
        launchActivity<MainActivity>()

        checkAboutViaOptions()
        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    //Drawer
    @Test
    fun fr1_about_fr1_destroy3_viaUp() {
        launchActivity<MainActivity>()

        checkAboutViaDrawer()
        upButton()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

    @Test
    fun fr1_about_fr1_destroy3() {
        launchActivity<MainActivity>()

        checkAboutViaDrawer()
        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        pressBackUnconditionally()
        assertTrue(mainRule.scenario.state == Lifecycle.State.DESTROYED)
    }

/*
  ==============================================================================
  Проверяем как смена ориентации сказывается на отображении фрагментов и кнопок,
  а также какой контент отображается в кнопках
  ===============================================================================
*/

    private fun makeLandScapeOrientation(activityScenario: ActivityScenario<MainActivity>) {
        activityScenario.onActivity { activity ->
            activity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            )
        }
        Thread.sleep(1000)
    }

    private fun makePortraitOrientation(activityScenario: ActivityScenario<MainActivity>) {
        mainRule.scenario.onActivity { activity ->
            activity.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            )
        }
        Thread.sleep(1000)
    }

    private fun checkButton(id: Int, text: Int) {
        onView(withId(id))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(withText(text)))
    }

    @Test
    fun fr1_rotate_fr2() {
        val activityScenario = launchActivity<MainActivity>()

        makeLandScapeOrientation(activityScenario)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        makePortraitOrientation(activityScenario)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))

        makeLandScapeOrientation(activityScenario)
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
    }

    @Test
    fun fr2_rotate_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())

        makeLandScapeOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        makePortraitOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))

        makeLandScapeOrientation(activityScenario)
        onView(withId(R.id.bnToFirst)).perform(click())
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr3_rotate_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        makeLandScapeOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        makePortraitOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        makeLandScapeOrientation(activityScenario)
        onView(withId(R.id.bnToFirst)).perform(click())
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr3_rotate_fr2() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        makeLandScapeOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        makePortraitOrientation(activityScenario)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))

        makeLandScapeOrientation(activityScenario)
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToThird, R.string.title_to_third)
        checkButton(R.id.bnToFirst, R.string.title_to_first)
    }

    @Test
    fun fr1_about_rotate_upButton() {
        val activityScenario = launchActivity<MainActivity>()

        openAbout()
        makeLandScapeOrientation(activityScenario)

        upButton()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr1_about_rotate_back() {
        val activityScenario = launchActivity<MainActivity>()

        openAbout()
        makeLandScapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr2_about_rotate_upButton_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        upButton()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr2_about_rotate_back_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)

        pressBackUnconditionally()
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr3_about_rotate_back_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr3_about_rotate_upButton_fr1() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        upButton()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToFirst)).perform(click())
        onView(withId(R.id.fragment1)).check(matches(isDisplayed()))
        checkButton(R.id.bnToSecond, R.string.title_to_second)
    }

    @Test
    fun fr3_about_rotate_upButton_fr2() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        upButton()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
    }

    @Test
    fun fr3_about_rotate_back_fr2() {
        val activityScenario = launchActivity<MainActivity>()
        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.bnToThird)).perform(click())

        openAbout()
        makeLandScapeOrientation(activityScenario)

        pressBackUnconditionally()
        onView(withId(R.id.fragment3)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToSecond, R.string.title_to_second)

        onView(withId(R.id.bnToSecond)).perform(click())
        onView(withId(R.id.fragment2)).check(matches(isDisplayed()))
        checkButton(R.id.bnToFirst, R.string.title_to_first)
        checkButton(R.id.bnToThird, R.string.title_to_third)
    }
}