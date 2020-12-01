package com.example.factslistapplication

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.factslistapplication.facts.view.HomeActivity
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsListFragmentTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun test_fragmentLaunchedSuccessfully() {
        onView(withId(R.id.factRecyclerView)).check(matches(isDisplayed()))
        onView((withId(R.id.swipeToRefreshList))).check(matches(isDisplayed()))
    }

    @Test
    fun test_swipeRefreshLayout() {
        onView((withId(R.id.swipeToRefreshList))).perform(
            withCustomConstraints(
                ViewActions.swipeDown(),
                ViewMatchers.isDisplayingAtLeast(85)
            )
        )
    }

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }

}
