package tech.thdev.network.flowcalladapterfactory

import app.cash.turbine.*
import java.io.*
import kotlin.reflect.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import retrofit2.*
import tech.thdev.network.flowcalladapterfactory.mock.*

internal class CancelTest {

    private val factory = FlowCallAdapterFactory()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://example.thdev.tech")
        .callFactory { TODO() }
        .build()

    private lateinit var adapter: CallAdapter<String, Flow<String>>
    private lateinit var call: CompletableCall<String>

    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalStdlibApi::class)
    @BeforeEach
    fun setUp() {
        val flowString = typeOf<Flow<String>>()
        adapter = factory.get(
            flowString.javaType,
            emptyArray(),
            retrofit
        )!! as CallAdapter<String, Flow<String>>
        call = CompletableCall()
    }

    @Test
    fun `test success response`() = runTest {
        val mockBody = "hey"
        call.complete(mockBody)
        Assertions.assertFalse(call.isCanceled)

        adapter.adapt(call)
            .test {
                Assertions.assertEquals(mockBody, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test error case`() = runTest {
        adapter.adapt(call)
            .test {
                call.completeWithException(IOException())
                Assertions.assertFalse(call.isCanceled)

                awaitError()
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun `test cancel`() = runTest {
        adapter.adapt(call)
            .test {
                Assertions.assertFalse(call.isCanceled)
                call.cancel()
                Assertions.assertTrue(call.isCanceled)
                cancelAndConsumeRemainingEvents()
            }
    }
}
