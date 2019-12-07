package kanielOutis.october.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

import kanielOutis.october.R
import kotlinx.android.synthetic.main.fragment_video.*
import java.lang.Exception

class VideoFragment : Fragment() {

    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val url = arguments?.getString("url")

        //exoPlayer = activity?.findViewById(R.id.exo_player)!!

        try {
            val bandwidthMeter = DefaultBandwidthMeter()
            val trackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(activity!!, trackSelector)
            val extractorsFactory = DefaultExtractorsFactory()
            val dataSource = DefaultHttpDataSourceFactory("video")
            val mediaSource = ExtractorMediaSource(
                Uri.parse(url),
                dataSource,
                extractorsFactory, null, null
            )

            exo_player.player = simpleExoPlayer
            simpleExoPlayer.prepare(mediaSource)
            simpleExoPlayer.playWhenReady = true
        } catch (e: Exception) {
            Log.d("exoPlayer", e.message.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer.seekTo(arguments?.getLong("progress")!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        arguments?.putLong("progress", simpleExoPlayer.currentPosition)
        simpleExoPlayer.stop()
    }

}
