package tech.thdev.network.flowcalladapterfactory

import app.cash.turbine.test
import java.io.IOException
import kotlin.reflect.javaType
import kotlin.reflect.typeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.CallAdapter
import retrofit2.Retrofit
import tech.thdev.network.flowcalladapterfactory.mock.CompletableCall

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
        adapter = factory.get(flowString.javaType, emptyArray(), retrofit)!! as CallAdapter<String, Flow<String>>
        call = CompletableCall()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test success response`() = runBlockingTest {
        val mockBody = "hey"
        call.complete(mockBody)
        Assertions.assertFalse(call.isCanceled)

        adapter.adapt(call)
            .test {
                Assertions.assertEquals(mockBody, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test error case`() = runBlockingTest {
        adapter.adapt(call)
            .test {
                call.completeWithException(IOException())
                Assertions.assertFalse(call.isCanceled)

                awaitError()
                cancelAndConsumeRemainingEvents()
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test cancel`() = runBlockingTest {
        adapter.adapt(call)
            .test {
                Assertions.assertFalse(call.isCanceled)
                call.cancel()
                Assertions.assertTrue(call.isCanceled)
                cancelAndConsumeRemainingEvents()
            }
    }
}