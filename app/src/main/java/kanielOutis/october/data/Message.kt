package kanielOutis.october.data


import com.google.gson.annotations.SerializedName
import java.time.Duration

data class Message(
    @SerializedName("edited")
    var edited: Boolean?,
    @SerializedName("file")
    var `file`: String?,
    @SerializedName("from")
    var from: String?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("messageId")
    var messageId: Int?,
    @SerializedName("sectionType")
    var sectionType: String?,
    @SerializedName("seen")
    var seen: String?,
    @SerializedName("sent")
    var sent: String?,
    @SerializedName("to")
    var to: String?

)