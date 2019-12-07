package kanielOutis.october.repository

import kanielOutis.october.base.BaseRepository
import kanielOutis.october.data.Errors
import okhttp3.MultipartBody
import okhttp3.RequestBody

object MessagesRepo : BaseRepository() {

    fun getMessages(
        token : String,
        id: Int,
        failure: (Throwable) -> Unit,
        errors: (Errors) -> Unit
    ) = callApi(retrofit.getMessages(token, "message/index/$id"), failure, errors)

    fun sendFile(
        token: String,
        msg : RequestBody? = null,
        id: Int,
        file: MultipartBody.Part,
        failure: (Throwable) -> Unit,
        errors: (Errors) -> Unit
    ) = callApi(
            retrofit.sendFile(
                "message/store/$id",
                token,
                msg,
                file
            ), failure, errors
        )
    fun sendMsg(
        token: String,
        msg : String,
        id: Int,
        failure: (Throwable) -> Unit,
        errors: (Errors) -> Unit
    ) = callApi(
            retrofit.sendMsg(
                "message/store/$id",
                token,
                msg
            ), failure, errors
        )
}