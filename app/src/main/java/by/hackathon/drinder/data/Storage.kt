package by.hackathon.drinder.data

interface Storage {
    fun saveTheme(themeId: Int)
    fun getSavedTheme(): Int
    fun saveLoginData(login: String, pass: String)
    fun getPreviousLoginData(): Pair<String, String>
}
