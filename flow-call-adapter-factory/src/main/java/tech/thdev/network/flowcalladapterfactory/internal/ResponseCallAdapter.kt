package tech.thdev.network.flowcalladapterfactory.internal

import java.lang.reflect.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.*

internal class ResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {

    override fun responseType(): Type =
        responseType

    override fun adapt(call: Call<T>): Flow<Response<T>> = flow {
        emit(
            suspendCancellableCoroutine { continuation ->
                call.registerCallback(continuation) { response ->
                    continuation.resumeWith(kotlin.runCatching {
                        if (response.isSuccessful) {
                            response
                        } else {
                            throw HttpException(response)
                        }
                    })
                }

                call.registerOnCancellation(continuation)
            }
        )
    }
}
