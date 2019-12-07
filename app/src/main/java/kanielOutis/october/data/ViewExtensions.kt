package kanielOutis.october.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson

infix fun ViewGroup.inflate(layoutResource: Int): View =
    LayoutInflater.from(context).inflate(
        layoutResource, this, false
    )

infix fun TextView?.set(text : String?){
    text?.also {
        this?.text = it
    }
}

infix fun ImageView.load(url : String?){
    url?.also {
        Glide.with(this.context).load(it).into(this)
    }
}

fun doAfter(long: Long, action: () -> Unit) {
    Handler().postDelayed({
        action()
    }, long)
}

fun Context.storeUserInformation(userInformation: User?) {
    if (userInformation != null) {
        this.getSharedPreferences("user_information", Context.MODE_PRIVATE)
            ?.edit()?.putString("userData", Gson().toJson(userInformation))?.apply()
    }
}

fun Context.getUserInformation(): User? {
    return try {
        Gson().fromJson(
            this.getSharedPreferences("user_information", Context.MODE_PRIVATE)?.getString("userData", "")
            , User::class.java
        )
    } catch (exception: Exception) {
        null
    }
}

fun Context?.clearUserInformation() {
    val sharedPreferences: SharedPreferences? =
        this?.getSharedPreferences("user_information", Context.MODE_PRIVATE)
    val editor = sharedPreferences?.edit()
    editor?.clear()?.apply()
}