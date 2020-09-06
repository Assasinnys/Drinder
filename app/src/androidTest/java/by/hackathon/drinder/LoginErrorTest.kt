
package by.hackathon.drinder

import android.content.Context
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import by.hackathon.drinder.ui.activity.MainActivity
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginErrorTest {
    lateinit var errorText: String
    lateinit var context: Context

    fun hasTextInputLayoutHintText(expectedErrorText: String): Matcher<View> = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description?) { }

        override fun matchesSafely(item: View?): Boolean {
            if (item !is TextInputLayout) return false
            val error = item.error ?: return false
            val hint = error.toString()
            return expectedErrorText == hint
        }
    }

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
        MainActivity::class.java)

    @Before
    fun initErrorText() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        errorText = context.getString(R.string.error_empty_field)
    }

    @Test
    fun testErrors() {
        onView(withId(R.id.btn_login)).perform(click())
        onView(allOf(withId(R.id.tl_login))).check(matches(hasTextInputLayoutHintText(errorText)))
    }
}
