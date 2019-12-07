package kanielOutis.october.repository

import kanielOutis.october.base.BaseRepository
import kanielOutis.october.data.Errors

object LoginRepo : BaseRepository() {

    fun login(name : String,
              password : String,
              failure: (Throwable) -> Unit,
              errors: (Errors) -> Unit) = callApi(retrofit.login(name, password), failure, errors)
}