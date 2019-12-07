package kanielOutis.october.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("info")
    var info: Info?,
    @SerializedName("token")
    var token: String?
)