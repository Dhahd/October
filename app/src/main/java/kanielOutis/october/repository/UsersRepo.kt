package kanielOutis.october.repository

import kanielOutis.october.base.BaseRepository
import kanielOutis.october.data.Errors

object UsersRepo : BaseRepository() {
    fun getUsers(
        token: String,
        failure: (Throwable) -> Unit,
        errors: (Errors) -> Unit
    ) = callApi(retrofit.getUsers(token),failure, errors)
}