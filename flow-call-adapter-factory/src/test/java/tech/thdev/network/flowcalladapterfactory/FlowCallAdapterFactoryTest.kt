/*
 * Copyright (C) 2016 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tech.thdev.network.flowcalladapterfactory

import kotlin.reflect.*
import kotlinx.coroutines.flow.*
import okhttp3.mockwebserver.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*
import retrofit2.*
import tech.thdev.network.flowcalladapterfactory.mock.*


internal class FlowCallAdapterFactoryTest {

    @ExtendWith
    val server: MockWebServer = MockWebServer()

    private val factory: CallAdapter.Factory = FlowCallAdapterFactory()
    private lateinit var retrofit: Retrofit

    @BeforeEach
    fun setUp() {
        retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(StringConverterFactory())
            .addCallAdapterFactory(factory)
            .build()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun `test responseType`() {
        val bodyClass = typeOf<Flow<String>>()
        Assertions.assertEquals(
            String::class.java,
            factory[bodyClass.javaType, NO_ANNOTATIONS, retrofit]!!.responseType()
        )

        val bodyGeneric = typeOf<Flow<List<String>>>()
        Assertions.assertEquals(
            typeOf<List<String>>().javaType,
            factory[bodyGeneric.javaType, NO_ANNOTATIONS, retrofit]!!.responseType()
        )

        val responseClass = typeOf<Flow<Response<String>>>()
        Assertions.assertEquals(
            String::class.java,
            factory[responseClass.javaType, NO_ANNOTATIONS, retrofit]!!.responseType()
        )
    }

    @Test
    fun `test nonListenableFutureReturnsNull`() {
        val adapter = factory[String::class.java, NO_ANNOTATIONS, retrofit]
        Assertions.assertNull(adapter)
    }

    companion object {
        private val NO_ANNOTATIONS = arrayOfNulls<Annotation>(0)
    }
}
