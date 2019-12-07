package kanielOutis.october.data

import android.app.Application
import android.util.Log
import java.net.Socket

class SocketApp : Application() {

    lateinit var mSocket: Socket

    override fun onCreate() {
        super.onCreate()
        try{
           // mSocket = socket("")
        }catch (e: Exception){
            Log.e("SOCKET", "Socket init")
        }
    }
    fun getSocketInstance() = mSocket

}