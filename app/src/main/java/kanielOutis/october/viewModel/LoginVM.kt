package kanielOutis.october.viewModel

import kanielOutis.october.base.BaseViewModel
import kanielOutis.october.repository.LoginRepo

class LoginVM : BaseViewModel() {
    val loginRepo = LoginRepo
    fun login(name :String, password : String) = loginRepo.login(name, password,
        {
            failure.postValue(it)
        },
        {
            errorHandler.postValue(it)
        })
}