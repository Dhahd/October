package kanielOutis.october.viewModel

import kanielOutis.october.base.BaseViewModel
import kanielOutis.october.repository.UsersRepo

class UsersViewModel : BaseViewModel() {
    private val users = UsersRepo
    fun getUsers(token: String) = users.getUsers(token,
        {
            failure.postValue(it)
        }, {
            errorHandler.postValue(it)
        })
}