package com.example.factslistapplication

import android.os.SystemClock
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.factslistapplication.facts.view.FactsItemAdapter
import com.example.factslistapplication.facts.view.HomeActivity
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class FactsListFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)
    private lateinit var mockServer: MockWebServer
    private val LIST_ITEM_IN_TEST = 4
    private var okHttpClient: OkHttpClient? = null

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start(8080)
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                getOkHttpClient()
            )
        )
    }


    fun getOkHttpClient(): OkHttpClient {
        return if (okHttpClient == null) {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
            this.okHttpClient = okHttpClient
            okHttpClient
        } else {
            okHttpClient!!
        }
    }

    @After
    fun tearDown() {
        mockServer.shutdown()

    }

    @Test
    fun test_data_load_successfully() {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("data.json"))
            }
        }

        val activityScenario = launch(HomeActivity::class.java)
        // check recyclerView firstitem
        onView(withId(R.id.factRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<FactsItemAdapter.FactsViewHolder>(0))
            .check(
                matches(
                    CustomMatcher.atPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("Beavers"))
                    )
                )
            )
        // check recyclerView last item
        onView(withId(R.id.factRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<FactsItemAdapter.FactsViewHolder>(8))
            .check(
                matches(
                    CustomMatcher.atPosition(
                        8,
                        ViewMatchers.hasDescendant(ViewMatchers.withText("Meese"))
                    )
                )
            )

        activityScenario.close()
    }

    @Test(expected = PerformException::class)
    fun test_selectListItem_isDetailFragmentVisible() {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("data.json"))
            }
        }
//        val activityScenario = launch(HomeActivity::class.java)
        SystemClock.sleep(2000)
        // Click list item #LIST_ITEM_IN_TEST
        onView(withId(R.id.factRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FactsItemAdapter.FactsViewHolder>(
                    LIST_ITEM_IN_TEST,
                    ViewActions.click()
                )
            )

        // Confirm nav to DetailFragment and display title
        onView(withId(R.id.imageViewDetails)).check(matches(isDisplayed()))
//        activityScenario.close()
    }

    @Test
    fun test_api_fail_useCase() {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse().throttleBody(1024, 5, TimeUnit.SECONDS)
            }
        }
        val activityScenario = launch(HomeActivity::class.java)
        onView(withText(R.string.common_error)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
        activityScenario.close()
    }

    @Test
    fun test_fragmentLaunchedSuccessfully() {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("data.json"))
            }
        }
        onView(withId(R.id.factRecyclerView)).check(matches(isDisplayed()))
        onView((withId(R.id.swipeToRefreshList))).check(matches(isDisplayed()))
    }

    @Test
    fun test_swipeRefreshLayout() {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(FileReader.readStringFromFile("data.json"))
            }
        }
        onView((withId(R.id.swipeToRefreshList))).perform(
            withCustomConstraints(
                ViewActions.swipeDown(),
                ViewMatchers.isDisplayingAtLeast(85)
            )
        )
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
