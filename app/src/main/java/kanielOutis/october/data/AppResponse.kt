package kanielOutis.october.data


import com.google.gson.annotations.SerializedName

data class AppResponse(
    @SerializedName("data")
    var `data`: Data?,
    @SerializedName("errors")
    var errors: Errors?
)