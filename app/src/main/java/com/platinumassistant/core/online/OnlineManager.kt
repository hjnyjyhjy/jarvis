package com.platinumassistant.core.online

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import timber.log.Timber

data class RemoteNluRequest(val text: String)
data class RemoteNluResponse(
    @SerializedName("intent") val intent: String?,
    @SerializedName("confidence") val confidence: Float? = null,
    @SerializedName("entities") val entities: Map<String, String>? = null,
    @SerializedName("response") val response: String? = null
)

interface RemoteNluService {
    @POST
    suspend fun analyze(@Url url: String, @Body req: RemoteNluRequest): RemoteNluResponse
}

object OnlineManager {
    private var retrofit: Retrofit? = null
    private var nluService: RemoteNluService? = null

    private fun ensure(url: String) {
        try {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                nluService = retrofit!!.create(RemoteNluService::class.java)
            }
        } catch (t: Throwable) {
            Timber.e(t, "Failed to create retrofit for %s", url)
        }
    }

    suspend fun analyzeText(nluUrl: String, text: String): RemoteNluResponse? {
        return withContext(Dispatchers.IO) {
            try {
                ensure(nluUrl)
                val res = nluService?.analyze(nluUrl, RemoteNluRequest(text))
                res
            } catch (t: Throwable) {
                Timber.e(t, "Remote NLU call failed")
                null
            }
        }
    }
}
