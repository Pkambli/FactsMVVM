package com.example.factslistapplication

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.example.factslistapplication.facts.view.FactsItemAdapter
import com.example.factslistapplication.facts.view.HomeActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class FactsListFragmentTest {

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
                isDisplayingAtLeast(85)
            )
        )
    }

    /**
     * Test that the item with the text at particular position exist
     */
    @Test(expected = PerformException::class)
    fun itemWithText_aParticularPosition() {
        // scrollTo will fail the test if no item matches.
        onView(withId(R.id.factRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FactsItemAdapter.FactsViewHolder>(
                    3,
                    ViewActions.click()
                )
            )

        val itemElementText = "${"Hockey Night in Canada"} ${3}"
        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    /**
     * Test that the item with the text does not exist in list.
     */
    @Test(expected = PerformException::class)
    fun itemWithText_doesNotExist() {
        // scrollTo will fail the test if no item matches.
        onView(withId(R.id.factRecyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    hasDescendant(withText("not in the list"))
                )
            )
    }

    /**
     * Test that the list is long enough for this sample, the last item shouldn't appear.
     */
    @Test
    fun lastItem_NotDisplayed() {
        // Last item should not exist if the list wasn't scrolled down.
        onView(withText(30)).check(doesNotExist())
    }

    fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }


    @Test(expected = PerformException::class)
    fun test_view_on_click() {
        onView(withId(R.id.factRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FactsItemAdapter.FactsViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Thread.sleep(3000)

        onView(withId(R.id.imageViewDetails)).check(matches(withDrawable(R.drawable.defaultimage)))
    }

    class DrawableMatcher(resourceId: Int) : TypeSafeMatcher<View?>() {
        override fun matchesSafely(item: View?): Boolean {
            return false
        }

        override fun describeTo(description: Description) {}
    }

    private fun withDrawable(resourceId: Int): Matcher<View?> {
        return DrawableMatcher(resourceId)
    }

    fun noDrawable(): Matcher<View?> {
        return DrawableMatcher(-1)
    }

    private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
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
