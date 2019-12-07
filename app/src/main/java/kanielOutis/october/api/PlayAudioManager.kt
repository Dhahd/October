package kanielOutis.october.api

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri


object PlayAudioManager {
    private var mediaPlayer: MediaPlayer? = null
    var duration = 0
    var currentPosition = 0

    @Throws(Exception::class)
    fun playAudio(context: Context, url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
        }

        mediaPlayer!!.setOnCompletionListener { killMediaPlayer() }
        mediaPlayer!!.start()
        if (mediaPlayer?.isPlaying!!)
            duration = mediaPlayer?.duration!!
        currentPosition = mediaPlayer?.currentPosition!!
    }

    fun pauseAudio() {
        mediaPlayer?.isPlaying?.also {
            if (it) mediaPlayer?.pause()
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    fun killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
