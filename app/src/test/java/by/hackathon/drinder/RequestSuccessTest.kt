package by.hackathon.drinder

import by.hackathon.drinder.api.ApiImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

class RequestSuccessTest {

    private val api = ApiImplementation()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Test
    fun loginRequest() {
        coroutineScope.launch {
            assert(api.login("Asd", "123") != null)
        }
    }

    @Test
    fun registrationRequest() {
        coroutineScope.launch {
            assert(api.register("Asd", "123") == null)
        }
    }

    @Test
    fun findDrinkers() {
        coroutineScope.launch {
            assert(api.findDrinkers("1").isEmpty())
        }
    }

    @Test
    fun getUserDetailRequest1() {
        coroutineScope.launch {
            assert(api.getUserDetail("0") != null)
        }
    }

    @Test
    fun getUserDetailRequest2() {
        coroutineScope.launch {
            assert(api.getUserDetail("2") == null)
        }
    }
}
