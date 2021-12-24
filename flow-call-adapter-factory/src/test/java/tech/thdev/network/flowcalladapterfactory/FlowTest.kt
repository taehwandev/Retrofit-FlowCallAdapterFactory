package tech.thdev.network.flowcalladapterfactory

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import tech.thdev.network.flowcalladapterfactory.mock.StringConverterFactory

class FlowTest {

    private lateinit var server: MockWebServer

    private lateinit var service: Service

    interface Service {
        @GET("/")
        fun body(): Flow<String>

        @GET("/")
        fun response(): Flow<Response<String>>
    }

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(StringConverterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()

        service = retrofit.create(Service::class.java)
    }

    @Test
    fun `test body responseCode 200`() = runBlocking(Dispatchers.Unconfined) {

        service.body()
            .test {
                server.enqueue(MockResponse().setResponseCode(200).setBody(MOCK_RESPONSE_MESSAGE))

                Assertions.assertEquals(MOCK_RESPONSE_MESSAGE, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test body responseCode 404`() = runBlocking(Dispatchers.Unconfined) {
        service.body()
            .test {
                server.enqueue(MockResponse().setResponseCode(404))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test body failure`() = runBlocking(Dispatchers.Unconfined) {
        service.body()
            .test {
                server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response responseCode 200`() = runBlocking(Dispatchers.Unconfined) {
        service.response()
            .test {
                server.enqueue(MockResponse().setBody(MOCK_RESPONSE_MESSAGE))

                Assertions.assertEquals(MOCK_RESPONSE_MESSAGE, awaitItem().body())
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response responseCode 404`() = runBlocking(Dispatchers.Unconfined) {
        service.response()
            .test {
                server.enqueue(MockResponse().setResponseCode(404))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response failure`() = runBlocking(Dispatchers.Unconfined) {
        service.response()
            .test {
                server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    companion object {
        private const val MOCK_RESPONSE_MESSAGE = "CallAdapterFactory Flow<T>"
    }
}