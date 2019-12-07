package kanielOutis.october.recyclerView

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kanielOutis.october.R
import kanielOutis.october.api.PlayAudioManager
import kanielOutis.october.data.*
import kotlinx.android.synthetic.main.chat_dark_item.view.*
import kotlinx.android.synthetic.main.chat_my_img.view.*
import kotlinx.android.synthetic.main.chat_sender_img.view.*
import kotlinx.android.synthetic.main.chat_white_item.view.*
import kotlinx.android.synthetic.main.item_his_video.view.*
import kotlinx.android.synthetic.main.item_his_voice.view.*
import kotlinx.android.synthetic.main.item_my_video.view.*
import kotlinx.android.synthetic.main.item_my_voice.view.*

class MessagesAdapter(private val click: (Message) -> Unit) :
    RecyclerView.Adapter<BaseViewHolder<Message>>() {

    private val msgList = ArrayList<Message>()

    fun addItems(items: ArrayList<Message?>) {
        items.forEach {item->
           if (!msgList.contains(item)){
               msgList.add(item!!)
               notifyDataSetChanged()
           }
        }
    }

    fun addItem(item : Message?) {
        if (item !in msgList){
            msgList.add(item!!)
            notifyDataSetChanged()
        }
    }
    fun doesPlaying(playing: Boolean){
        isPlaying = playing
    }

    companion object {
        private const val myMsg = 0
        private const val hisMsg = 1
        private const val hisVoice = 2
        private const val myVoice = 3
        private const val hisImg = 4
        private const val myImg = 5
        private const val hisVideo = 6
        private const val myVideo = 7
        private var type = ""
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Message> {
        val view = parent.inflate(ViewRegistry.getView(type))
        return when (viewType) {
            myMsg -> MyMsg(view)
            myImg -> MyImg(view)
            myVoice -> MyVoice(view)
            hisMsg -> HisMsg(view)
            hisImg -> HisImg(view)
            hisVoice -> HisVoice(view)
            hisVideo -> HisVideo(view)
            myVideo -> MyVideo(view)
            else -> throw UnknownViewType()
        }
    }

    override fun getItemCount() = msgList.size

    override fun onBindViewHolder(holder: BaseViewHolder<Message>, position: Int) {
        holder.bind(msgList[position])
    }

    override fun getItemViewType(position: Int): Int {
        type = msgList[position].sectionType!!
        return when (type) {
            "my_text" -> myMsg
            "his_text" -> hisMsg
            "his_audio" -> hisVoice
            "my_audio" -> myVoice
            "his_video" -> hisVideo
            "my_video" -> myVideo
            "his_video_text" -> hisVideo
            "my_video_text" -> myVideo
            "his_image" -> hisImg
            "his_image_text" -> hisImg
            "my_image"-> myImg
            "my_image_text" -> myImg
            else -> throw UnknownViewType()
        }
    }

    inner class MyMsg(view: View) : BaseViewHolder<Message>(view) {
        override fun bind(item: Message) {
            item.apply {
                itemView.apply {
                    item_myMsg set message
                    item_myTime set sent
                    if (item.seen == "true") item_mySeen.visibility = View.VISIBLE
                }
            }
        }
    }


    inner class MyImg(view: View) : BaseViewHolder<Message>(view) {

        override fun bind(item: Message) {
            item.apply {
                itemView.apply {
                    item_myImg.setOnClickListener { click(item) }
                    item_myImg load file
                    item_myImgTime set sent
                    item_myImgMsg set message
                    if (item.seen == "true") item_mySeen.visibility = View.VISIBLE
                }
            }
        }
    }
    inner class HisImg(view: View) : BaseViewHolder<Message>(view) {
        override fun bind(item: Message) {
            item.apply {
                itemView.apply {
                    item_senderImg.setOnClickListener { click(item) }
                    item_senderImg load file
                    item_senderImgTime set sent
                    item_senderImgMsg set message
                    if (item.seen == "true") item_mySeen.visibility = View.VISIBLE
                }
            }
        }
    }
    var voiceDuration = 0
    var isPlaying = false

    inner class MyVoice(view: View) : BaseViewHolder<Message>(view) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun bind(item: Message) {
            val random1 = (0..100).random()
            val random2 = (0..1000).random()
            val random3 = (0..100).random()
            val random4 = (0..1000).random()
            val random5 = (0..1000).random()
            val random6 = (0..1000).random()
            val random7 = (0..1000).random()
            val random8 = (0..1000).random()
            item.apply {
                itemView.apply {
                    item_myVoiceTime set sent
                    if (item.seen == "true") item_myVoiceSeen.visibility = View.VISIBLE
                    val data = byteArrayOf(
                        random1.toByte(),
                        random2.toByte(),
                        random3.toByte(),
                        random4.toByte(),
                        random5.toByte(),
                        random6.toByte(),
                        random7.toByte(),
                        random8.toByte()
                    )
                    wave.setRawData(data)
                    my_voicePlay.setOnClickListener {
                        click(item)
                        val duration = voiceDuration / 1000
                        //if (!isPlaying) {
                        //my_voiceProgress.max = voiceDuration

                        val mediaPlayer =
                            PlayAudioManager
                        if (!isPlaying) {
                            mediaPlayer.playAudio(context, file!!)
                            it.background = it.context.getDrawable(R.drawable.ic_pause_circle)
                            my_voiceDuration set duration.toString()
                            if (mediaPlayer.currentPosition == mediaPlayer.duration)
                                it.background = it.context.getDrawable(R.drawable.ic_play_circle)

                            doAfter(mediaPlayer.duration.toLong()) {
                                it.background = it.context.getDrawable(R.drawable.ic_play_circle)
                            }
                            isPlaying = true
                        } else if (isPlaying) {
                            mediaPlayer.killMediaPlayer()
                            it.background = it.context.getDrawable(R.drawable.ic_play_circle)
                            isPlaying = false
                        }

                        // }
                    }
                }
            }
        }
    }

    inner class HisVoice(view: View) : BaseViewHolder<Message>(view) {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun bind(item: Message) {
            val random1 = (0..100).random()
            val random2 = (0..1000).random()
            val random3 = (0..100).random()
            val random4 = (0..1000).random()
            val random5 = (0..1000).random()
            val random6 = (0..1000).random()
            val random7 = (0..1000).random()
            val random8 = (0..1000).random()
            val data = byteArrayOf(
                random1.toByte(),
                random2.toByte(),
                random3.toByte(),
                random4.toByte(),
                random5.toByte(),
                random6.toByte(),
                random7.toByte(),
                random8.toByte()
            )

            item.apply {
                itemView.apply {
                    hisWave.setRawData(data)
                    item_hisVoiceTime set sent
                    his_voicePlay.setOnClickListener {
                        val duration = voiceDuration / 1000
                        //if (!isPlaying) {
                        //my_voiceProgress.max = voiceDuration

                        val mediaPlayer =
                            PlayAudioManager
                        if (!isPlaying) {
                            mediaPlayer.playAudio(context, file!!)
                            it.background = it.context.getDrawable(R.drawable.ic_pause_black)
                            my_voiceDuration set duration.toString()
                            if (mediaPlayer.currentPosition == mediaPlayer.duration)
                                it.background = it.context.getDrawable(R.drawable.ic_play_black)

                            doAfter(mediaPlayer.duration.toLong()) {
                                it.background = it.context.getDrawable(R.drawable.ic_play_black)
                            }

                            isPlaying = true
                        } else if (isPlaying) {
                            mediaPlayer.killMediaPlayer()
                            it.background = it.context.getDrawable(R.drawable.ic_play_black)
                            isPlaying = false
                        }

                        // }
                    }
                }
            }
        }
    }

    inner class HisMsg(view: View) : BaseViewHolder<Message>(view) {
        override fun bind(item: Message) {
            item.apply {
                itemView.apply {
                    item_senderMsg set message
                    item_senderTime set sent
                }
            }
        }
    }



    inner class HisVideo(view: View) : BaseViewHolder<Message>(view) {
        override fun bind(item: Message) {
            itemView.apply {
                his_VideoPlay.setOnClickListener { click(item) }
                item.apply {
                    val thumb = (layoutPosition * 1000).toLong()
                    val options = RequestOptions().frame(thumb)
                    Glide.with(context).load(file).apply(options).into(his_VideoBack)
                    item_hisVideoMsg set message
                    item_hisVideoTime set sent
                }
            }
        }

    }

    inner class MyVideo(view: View) : BaseViewHolder<Message>(view) {
        override fun bind(item: Message) {
            itemView.apply {
                my_VideoPlay.setOnClickListener { click(item) }
                item.apply {
                    item_myVideoMsg set message
                    item_myVideoTime set sent
                    val thumb = (layoutPosition * 1000).toLong()
                    val options = RequestOptions().frame(thumb)
                    Glide.with(context).load(file).apply(options).into(my_VideoBack)
                    if (seen == "true") item_myVideoSeen.visibility = View.VISIBLE
                }
            }
        }

    }



    inner class UnknownViewType : Exception() {
        override val message: String?
            get() = "UNKNOWN VIEW TYPE"
    }

}

