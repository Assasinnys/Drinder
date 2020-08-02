package by.hackathon.drinder.model

data class User(
    var login: String? = null,
    var pass: String? = null,
    var id: Int? = null
)

data class UserLocation(
    var userId: Int? = null,
    var lat: Float? = null,
    var lon: Float? = null
)

data class UserInfo(
    var alcohol: String? = null,
    var userName: String? = null,
    var gender: String? = null,
    var age: Int? = null
)