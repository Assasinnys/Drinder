package by.hackathon.drinder.ui.activity

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import by.hackathon.drinder.data.Storage
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class MainViewModel @Inject constructor(val storage: Storage) : ViewModel(),
    DefaultLifecycleObserver {

    private var themeId: Int = storage.getSavedTheme()

    override fun onCreate(owner: LifecycleOwner) {
        AppCompatDelegate.setDefaultNightMode(themeId)
    }

    fun notifyThemeChanged() {
        themeId = if (AppCompatDelegate.MODE_NIGHT_YES != themeId)
            AppCompatDelegate.MODE_NIGHT_YES
        else
            AppCompatDelegate.MODE_NIGHT_NO

        AppCompatDelegate.setDefaultNightMode(themeId)
        storage.saveTheme(themeId)
    }
}
