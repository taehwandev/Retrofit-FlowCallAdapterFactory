package tech.thdev.network.flowcalladapterfactory.internal

import java.lang.reflect.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.*

internal class BodyCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<T>> {

    override fun responseType(): Type =
        responseType

    override fun adapt(call: Call<T>): Flow<T> = flow {
        emit(suspendCancellableCoroutine { continuation ->
            call.registerCallback(continuation) { response ->
                continuation.resumeWith(kotlin.runCatching {
                    if (response.isSuccessful) {
                        response.body()
                            ?: throw NullPointerException("Response body is null: $response")
                    } else {
                        throw HttpException(response)
                    }
                })
            }

            call.registerOnCancellation(continuation)
        })
    }
}
