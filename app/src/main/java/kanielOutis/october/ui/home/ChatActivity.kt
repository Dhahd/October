package kanielOutis.october.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.squti.androidwaverecorder.WaveRecorder
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.stfalcon.contentmanager.ContentManager
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import kanielOutis.october.R
import kanielOutis.october.api.PlayAudioManager
import kanielOutis.october.recyclerView.MessagesAdapter
import kanielOutis.october.viewModel.MessagesVM
import kotlinx.android.synthetic.main.activity_chat.*
import android.os.Vibrator;
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import kanielOutis.october.base.MyService
import kanielOutis.october.data.*
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoCallback
import net.mrbin99.laravelechoandroid.EchoOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception
import java.util.*
import javax.sql.ConnectionEvent
import javax.sql.ConnectionEventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity(), ContentManager.PickContentListener {
    override fun onStartContentLoading() {

    }

    override fun onContentLoaded(uri: Uri?, contentType: String?) {

        if (contentType.equals(ContentManager.Content.IMAGE.toString())) {
            openMediaDialog(uri!!)
            //You can use any library for display image Fresco, Picasso, ImageLoader
            //For sample:
            //Glide.getInstance().displayImage(uri.toString(), ivPicture);
        } else if (contentType.equals(ContentManager.Content.VIDEO.toString())) {
            //handle file result
            openMediaDialog(uri!!, false)
            //tvUri.setText(uri.toString());
        }
    }

    override fun onCanceled() {
    }

    override fun onError(error: String?) {
    }

    private lateinit var contentManager: ContentManager

    var id: Int = 0
    lateinit var echo: Echo
    //private lateinit var streamingManager : AudioStreamingManager
    private lateinit var updateTimeTask: Runnable

    private var mediaPlayer: PlayAudioManager? = PlayAudioManager
    private lateinit var chatVM: MessagesVM
    private lateinit var chatAdapter: MessagesAdapter
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val extras = intent.extras ?: return
        id = extras.getInt("id")
        val token = getUserInformation()?.token!!
        title = extras.getString("name")
        chatVM = ViewModelProviders.of(this).get(MessagesVM::class.java)


        val headers = HashMap<String, String>()
        headers["Authorization"] = token
        Log.d("echo", token)

        val options = EchoOptions()
        options.auth.put("Authorization" , token)
        options.host = "http://192.168.0.0:6001"
        options.headers["Authorization"] = token

        val echo = Echo(options)
        echo.connect({ array ->
            array.forEach {
                Log.d("echo", it.toString())
            }
        }, {
            Log.d("echo errors", it.toString())
        })

        echo.channel("private-messages-channel")
            .listen(".message.sent") { arrayOfAnys ->
                arrayOfAnys.forEach {
                    Log.d("echo ch", it.toString())
                }
            }
        val authorizer = HttpAuthorizer("http://192.168.0.0:8000/message/store/$id")
        authorizer.setHeaders(headers)
        contentManager = ContentManager(this, this)

      /*  try {


            val options = PusherOptions()
            options.authorizer = authorizer
            options.setCluster("ap2")
            val pusher = Pusher("91339ff42f931e4ae436", options)

            pusher.connect(object : com.pusher.client.connection.ConnectionEventListener {
                override fun onConnectionStateChange(change: ConnectionStateChange?) {
                    Log.e(
                        "pusher",
                        change?.currentState.toString()
                    )
                    if (change?.currentState.toString() == "DISCONNECTED") {
                        pusher.connect()
                    }
                }

                override fun onError(message: String?, code: String?, e: Exception?) {
                    Log.e("pusher", message.toString() + code.toString() + e.toString())
                    if (message == "Connection not authorized within timeout") pusher.connect()
                }
            }, ConnectionState.ALL)

            val channel = pusher.subscribe("messages-channel")

            channel.bind("newMessage") {
                Log.e("pusher", it.data + it.channelName + it.userId + it.eventName)
                val jsonData = Gson().fromJson(it.data, Data::class.java)
                runOnUiThread {
                    chatAdapter.apply {
                        addItem(jsonData.message)
                        chat_rv.scrollToPosition(this.itemCount - 1)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }*/

        msg_container.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty()!!) {
                    sendBtn.visibility = View.VISIBLE
                    chooseButton.visibility = View.GONE
                } else if (p0.isEmpty()) {
                    sendBtn.visibility = View.GONE
                    chooseButton.visibility = View.VISIBLE
                }
            }

        })
        sendBtn.setOnClickListener {
            val msg = msg_container.text.toString()
            chatVM.apply {
                msg_container.text.clear()

                sendMsg(token, id, msg).observeForever {
                    chatVM.getMassages(token, id).observeForever { data ->
                        chatAdapter.addItems(data.messages!!)
                        chat_rv.scrollToPosition(chatAdapter.itemCount - 1)
                    }
                    Log.e("msg", it.toString())
                }
                errorHandler.observeForever {
                    Log.e("msg", it.toString())
                }
                failure.observeForever {
                    Log.e("msg", it.toString())
                }

            }
        }

        chooseButton.setOnClickListener {
            openDialog()
        }

        val filePath: String = externalCacheDir?.absolutePath + "/${UUID.randomUUID()}.ogg"

        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val waveRecorder = WaveRecorder(filePath)
        record.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {

                MotionEvent.ACTION_DOWN -> {
                    view.background = getDrawable(R.drawable.ic_mic)
                    waveRecorder.startRecording()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                50,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        );
                    } else {
                        vibrator.vibrate(50)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    view.background = getDrawable(R.drawable.ic_mic_off)
                    waveRecorder.stopRecording()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                50,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        );
                    } else {
                        vibrator.vibrate(50)
                    }
                    val requestBody = File(filePath).asRequestBody()

                    val fileToUpload =
                        MultipartBody.Part.createFormData(
                            "message_file",
                            File(filePath).name,
                            requestBody
                        )
                    chatVM.apply {
                        sendFile(token = token, file = fileToUpload, id = id).observeForever {
                            Log.e("recording", it.toString())
                            getMassages(token, id).observeForever { data ->
                                chatAdapter.addItems(data.messages!!)
                                chat_rv.scrollToPosition(chatAdapter.itemCount - 1)
                            }
                            errorHandler.observeForever {
                                Log.e("msg", it.toString())
                            }
                            failure.observeForever {
                                Log.e("msg", it.toString())
                            }

                        }
                    }
                    chatVM.errorHandler.observeForever {
                        Log.e("recording", it.toString())
                    }
                    chatVM.failure.observeForever {
                        Log.e("recording", it.toString())
                    }
                }
            }
            return@setOnTouchListener true
        }
        var isPlaying = false
        chatAdapter = MessagesAdapter {
            if (it.sectionType?.contains("audio")!!) {
                if (!isPlaying) {
                    chatAdapter.doesPlaying(true)
                    isPlaying = true
                } else if (isPlaying) {
                    chatAdapter.doesPlaying(false)
                    isPlaying = false
                }
            } else if (it.sectionType?.contains("video")!!) {
                val bundle = Bundle()
                bundle.putString("url", it.file)
                val fragment = VideoFragment()
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.mainContainer, fragment)
                    .addToBackStack(null).commit()
            } else if (it.sectionType?.contains("image")!!) {
                val fragment = ImageFragment()
                val bundle = Bundle()
                bundle.putString("url", it.file)
                fragment.arguments = bundle
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.mainContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        chatVM.getMassages(token, id).observeForever {
            Log.e("chatData", it.toString())
            chatAdapter.addItems(it.messages!!)
            chat_rv.apply {
                scrollToPosition(it.messages?.size!!)
                adapter = chatAdapter
                layoutManager = LinearLayoutManager(this@ChatActivity)
            }
            chat_rv.scrollToPosition(chatAdapter.itemCount - 1)

        }

        doAfter(1000) {
        }
        chatVM.errorHandler.observeForever {
            Log.e("chatData", it.toString())
        }
        chatVM.failure.observeForever {
            Log.e("chatData", it.toString())
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.refresh, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refreshItem) {
            val token = getUserInformation()?.token!!
            chatVM.getMassages(token, id).observeForever {
                Log.e("chatData", it.toString())
                chatAdapter.addItems(it.messages!!)
                chat_rv.scrollToPosition(chatAdapter.itemCount - 1)
            }
            chatVM.errorHandler.observeForever {
                Log.e("chatData", it.toString())
            }
            chatVM.failure.observeForever {
                Log.e("chatData", it.toString())
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (fileType == "video")
            contentManager.onActivityResult(requestCode, resultCode, data)
        //openMediaDialog(data?.data!!)
        if (fileType == "image") {
            if (data != null) {
                val photos: ArrayList<*> =
                    data?.getSerializableExtra(GalleryActivity.PHOTOS) as ArrayList<*>
                Log.e("file", photos[0].toString())
                openMediaDialog(Uri.parse(photos[0].toString()))
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        contentManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private lateinit var fileType: String

    private fun openDialog() {
        val builder = AlertDialog.Builder(this).create()

        val customLayout: View = layoutInflater.inflate(R.layout.choose_dialog, null)
        builder.setView(customLayout)
        val imageBtn = customLayout.findViewById<Button>(R.id.chooseImg)
        val videoBtn = customLayout.findViewById<Button>(R.id.chooseVideo)
        imageBtn.setOnClickListener {
            fileType = "image"
            val config = GalleryConfig.Build()
                .limitPickPhoto(1)
                .singlePhoto(true)
                .build()
            GalleryActivity.openActivity(this, 1, config)
            builder.dismiss()
        }
        videoBtn.setOnClickListener {
            fileType = "video"
            contentManager.pickContent(ContentManager.Content.VIDEO)
            builder.dismiss()
        }
        builder
            .show()
    }


    private fun openMediaDialog(file: Uri, isImage: Boolean = true) {
        val builder = AlertDialog.Builder(this).create()
        val customLayout: View = layoutInflater.inflate(R.layout.file_dialog, null)
        val path = file.toString()
        val files = File(path)
        var requestBody: RequestBody
        var fileUpload: MultipartBody.Part? = null
        builder.setView(customLayout)
        if (fileType == "video") {
            val video = files.path.removePrefix("file:")
            val videoFile = File(video)
            requestBody = videoFile.asRequestBody()
            fileUpload =
                MultipartBody.Part.createFormData("message_file", files.name, requestBody)
            Log.e("file", video)
            //val requestVideo = video(path).asRequestBody()
        } else if (fileType == "image") {
            requestBody = files.asRequestBody()
            fileUpload = MultipartBody.Part.createFormData("message_file", files.name, requestBody)

        }
        val sendButton = customLayout.findViewById<Button>(R.id.dialog_sendBtn)
        val dialogText = customLayout.findViewById<EditText>(R.id.dialog_msg)
        val dialogImage = customLayout.findViewById<ImageView>(R.id.dialog_Image)

        val token = getUserInformation()?.token!!
        sendButton.setOnClickListener {

            val service = MyService()

            chatVM.apply {
                sendFile(
                    token,
                    id,
                    dialogText.text.toString()
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    , fileUpload!!
                ).observeForever {
                    getMassages(token, id).observeForever {
                        chatAdapter.addItems(it.messages!!)
                        chat_rv.scrollToPosition(chatAdapter.itemCount - 1)
                    }

                }
                errorHandler.observeForever {
                    Log.e("media error", it.toString())
                }
                failure.observeForever {
                    Log.e("media error", it.toString())
                }
            }

            startService(Intent(this, service::class.java))
            builder.dismiss()
        }

        builder
            .show()


        if (isImage) {
            dialogImage load file.toString()
        } else if (!isImage) {
            val thumb = (1 * 1000).toLong()
            val options = RequestOptions().frame(thumb)
            Glide.with(this).load(file).apply(options).into(dialogImage)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        contentManager.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentManager.onSaveInstanceState(outState)
    }
}
