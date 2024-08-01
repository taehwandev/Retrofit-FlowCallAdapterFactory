package tech.thdev.network.flowcalladapterfactory.internal

import kotlin.coroutines.*
import kotlinx.coroutines.*
import retrofit2.*

internal fun Call<*>.registerOnCancellation(
    continuation: CancellableContinuation<*>,
) {

    continuation.invokeOnCancellation {
        try {
            cancel()
        } catch (e: Exception) {
            // Ignore cancel exception
        }
    }
}

internal fun <T> Call<T>.registerCallback(
    continuation: CancellableContinuation<*>,
    success: (response: Response<T>) -> Unit,
) {
    enqueue(
        object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                success(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        }
    )
}
