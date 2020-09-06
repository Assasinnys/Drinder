package by.hackathon.drinder

import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import by.hackathon.drinder.data.Storage
import by.hackathon.drinder.data.repository.SharedPreferenceStorage
import by.hackathon.drinder.ui.activity.MainActivity
import com.google.android.material.textfield.TextInputLayout
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginDataTest {
    lateinit var preferencesStorage: Storage
    lateinit var userManager: UserManager
    lateinit var appContext: Context

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        preferencesStorage = SharedPreferenceStorage(appContext)
        userManager = UserManager(preferencesStorage)
    }

    @Test
    fun savingEnterDataTest() {
        val expectedPair: Pair<String, String> = preferencesStorage.getPreviousLoginData()
        val mainActivity = InstrumentationRegistry.getInstrumentation()
            .startActivitySync(Intent(appContext, MainActivity::class.java))
        val login: String =
            mainActivity.findViewById<TextInputLayout>(R.id.tl_login).editText?.text.toString()
        val pass: String =
            mainActivity.findViewById<TextInputLayout>(R.id.tl_password).editText?.text.toString()
        Assert.assertEquals(expectedPair.first, login)
        Assert.assertEquals(expectedPair.second, pass)
    }
}
