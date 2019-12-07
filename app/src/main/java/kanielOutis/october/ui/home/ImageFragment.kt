package kanielOutis.october.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kanielOutis.october.R
import kanielOutis.october.data.load
import kotlinx.android.synthetic.main.fragment_blank.*
import java.net.ServerSocket

class ImageFragment : Fragment() {
    private val serverSocket: ServerSocket? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageUrl = arguments?.getString("url")
        photo_view load imageUrl
    }

}
