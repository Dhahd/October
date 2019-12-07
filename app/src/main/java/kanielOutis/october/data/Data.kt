package kanielOutis.october.data


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("messages")
    var messages: ArrayList<Message?>?,
    @SerializedName("user")
    var user: User?,
    @SerializedName("users")
    var users: ArrayList<Users?>?,
    @SerializedName("message")
    var message: Message?,
    @SerializedName("errors")
    var errors : Errors
)