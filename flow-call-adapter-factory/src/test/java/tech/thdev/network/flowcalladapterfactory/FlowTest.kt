package tech.thdev.network.flowcalladapterfactory

import app.cash.turbine.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import okhttp3.mockwebserver.*
import org.junit.jupiter.api.*
import retrofit2.*
import retrofit2.http.*
import tech.thdev.network.flowcalladapterfactory.mock.*

class FlowTest {

    private val server = MockWebServer()

    private lateinit var service: Service

    interface Service {

        @GET("/")
        fun body(): Flow<String>

        @GET("/")
        fun response(): Flow<Response<String>>
    }

    @BeforeEach
    fun setUp() {
        server.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(StringConverterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .build()

        service = retrofit.create(Service::class.java)
    }

    @Test
    fun `test body responseCode 200`() = runTest {
        service.body()
            .test {
                server.enqueue(MockResponse().setResponseCode(200).setBody(MOCK_RESPONSE_MESSAGE))

                Assertions.assertEquals(MOCK_RESPONSE_MESSAGE, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test body responseCode 404`() = runTest {
        service.body()
            .test {
                server.enqueue(MockResponse().setResponseCode(404))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test body failure`() = runTest {
        service.body()
            .test {
                server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AFTER_REQUEST))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response responseCode 200`() = runTest {
        service.response()
            .test {
                server.enqueue(MockResponse().setBody(MOCK_RESPONSE_MESSAGE))

                Assertions.assertEquals(MOCK_RESPONSE_MESSAGE, awaitItem().body())
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response responseCode 404`() = runTest {
        service.response()
            .test {
                server.enqueue(MockResponse().setResponseCode(404))
                awaitError()
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `test response failure`() = runTest {
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
