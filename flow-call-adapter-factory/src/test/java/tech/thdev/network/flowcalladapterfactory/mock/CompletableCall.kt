package tech.thdev.network.flowcalladapterfactory.mock

import java.util.concurrent.*
import okhttp3.*
import okio.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val DUMMY_REQUEST = Request.Builder()
    .url("https://example.thdev.tech")
    .build()

class CompletableCall<T>(
    private val request: Request = DUMMY_REQUEST
) : Call<T> {

    private val lock = Any()
    private var exception: Throwable? = null
    private var callback: Callback<T>? = null
    private var done = CountDownLatch(1)
    private var response: Response<T>? = null
    private var canceled = false
    private var executed = false

    fun completeWithException(t: Throwable) {
        synchronized(lock) {
            exception = t
            callback?.onFailure(this, t)
        }
        done.countDown()
    }

    fun complete(body: T) {
        println("body $body")
        complete(Response.success(body))
    }

    private fun complete(response: Response<T>) {
        synchronized(lock) {
            this.response = response
            println("this.response ${this.response}")
            callback?.onResponse(this, response)
        }
        done.countDown()
    }

    override fun request(): Request = request

    override fun isCanceled(): Boolean = synchronized(lock) {
        canceled
    }

    override fun isExecuted(): Boolean = synchronized(lock) {
        executed
    }

    override fun clone(): Call<T> =
        CompletableCall(request)

    override fun cancel() {
        synchronized(lock) {
            if (canceled) return
            canceled = true

            val exception = InterruptedException("canceled")
            this.exception = exception
            callback?.onFailure(this, exception)
        }
        done.countDown()
    }

    override fun execute(): Response<T> {
        synchronized(lock) {
            check(executed.not()) {
                "Already executed"
            }
            executed = true
        }
        done.await()
        synchronized(lock) {
            if (exception != null) {
                throw exception!!
            }
            return response!!
        }
    }

    override fun enqueue(callback: Callback<T>) {
        synchronized(lock) {
            when {
                exception != null -> callback.onFailure(this, exception!!)
                response != null -> callback.onResponse(this, response!!)
                else -> this.callback = callback
            }
        }
    }

    override fun timeout(): Timeout =
        Timeout.NONE
}
