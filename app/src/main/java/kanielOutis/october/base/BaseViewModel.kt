package kanielOutis.october.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kanielOutis.october.data.Errors

open class BaseViewModel : ViewModel() {
    val errorHandler = MutableLiveData<Errors>()
    val failure = MutableLiveData<Throwable>()
}