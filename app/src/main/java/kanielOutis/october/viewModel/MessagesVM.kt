package kanielOutis.october.viewModel

import kanielOutis.october.base.BaseViewModel
import kanielOutis.october.repository.MessagesRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MessagesVM : BaseViewModel() {
    private val repo = MessagesRepo

    fun getMassages(token : String, id: Int) = repo.getMessages(token, id,
        {
            failure.postValue(it)
        },
        {
            errorHandler.postValue(it)
        }
    )
    fun sendFile(token: String, id: Int, msg : RequestBody? = null, file : MultipartBody.Part) = repo.sendFile(token,msg, id, file, {
        failure.postValue(it)
    },
        {
            errorHandler.postValue(it)
        })
    fun sendMsg(token: String, id : Int, msg : String) = repo.sendMsg(token, msg , id, {
        failure.postValue(it)
    },
        {
            errorHandler.postValue(it)
        })
}