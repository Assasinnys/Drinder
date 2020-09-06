package by.hackathon.drinder.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import by.hackathon.drinder.util.LOGIN_KEY
import by.hackathon.drinder.util.PASS_KEY
import by.hackathon.drinder.util.PREF_NAME
import by.hackathon.drinder.util.THEME
import javax.inject.Inject

class SharedPreferenceStorage @Inject constructor(appContext: Context) : Storage {

    private val prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override fun saveTheme(themeId: Int) {
        prefs.edit().apply {
            putInt(THEME, themeId)
            apply()
        }
    }

    override fun getSavedTheme() = prefs.getInt(THEME, AppCompatDelegate.MODE_NIGHT_NO)

    override fun saveLoginData(login: String, pass: String) {
        prefs.edit().apply {
            putString(LOGIN_KEY, login)
            putString(PASS_KEY, pass)
            apply()
        }
    }

    override fun getPreviousLoginData(): Pair<String, String> {
        return prefs.getString(LOGIN_KEY, "")!! to prefs.getString(
            PASS_KEY, "")!!
    }
}
