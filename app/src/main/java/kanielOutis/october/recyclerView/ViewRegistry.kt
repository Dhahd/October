package kanielOutis.october.recyclerView

import kanielOutis.october.R

object ViewRegistry {

    private val viewMap = HashMap<String, Int>()

    init {
        viewMap["my_text"] = R.layout.chat_dark_item
        viewMap["his_text"] = R.layout.chat_white_item
        viewMap["his_image"] = R.layout.chat_sender_img
        viewMap["his_image_text"] = R.layout.chat_sender_img
        viewMap["my_image"] = R.layout.chat_my_img
        viewMap["my_image_text"] = R.layout.chat_my_img
        viewMap["his_audio"] = R.layout.item_his_voice
        viewMap["my_audio"] = R.layout.item_my_voice
        viewMap["his_video"] = R.layout.item_his_video
        viewMap["my_video"] = R.layout.item_my_video
        viewMap["his_video_text"] = R.layout.item_his_video
        viewMap["my_video_text"] = R.layout.item_my_video
    }

    fun getView(viewType : String) : Int{
        return viewMap[viewType]!!
    }

}