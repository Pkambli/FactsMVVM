package com.example.factslistapplication


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.factslistapplication.facts.view.HomeActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    val rule = activityScenarioRule<HomeActivity>()

    private lateinit var scenario: ActivityScenario<HomeActivity>

    @Before
    fun setup() {
        scenario = rule.scenario
        assertNotNull(scenario)
    }

    @Test
    fun test_ActivityLaunched() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.factslistapplication", appContext.packageName)
    }
}
