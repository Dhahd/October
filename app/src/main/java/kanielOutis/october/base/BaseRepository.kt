package kanielOutis.october.base

import androidx.lifecycle.LiveData
import kanielOutis.october.api.ApiBuilder
import kanielOutis.october.data.AppResponse
import kanielOutis.october.data.Data
import kanielOutis.october.data.Errors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseRepository {
    val retrofit = ApiBuilder.getApi()
    fun callApi(
        call: Call<AppResponse>,
        failure: (Throwable) -> Unit,
        errors: (Errors) -> Unit
    ): LiveData<Data> {
        return object : LiveData<Data>() {
            override fun onActive() {
                super.onActive()
                call.clone().enqueue(object : Callback<AppResponse> {
                    override fun onFailure(call: Call<AppResponse>, t: Throwable) {
                        failure(t)
                    }

                    override fun onResponse(
                        call: Call<AppResponse>,
                        response: Response<AppResponse>
                    ) {
                        val data = response.body()?.data
                        if (data != null) {
                            postValue(data)
                        } else {
                            if (response.body()?.errors != null) {
                                errors(response.body()?.errors!!)
                            }
                        }
                    }
                })
            }
        }
    }
}