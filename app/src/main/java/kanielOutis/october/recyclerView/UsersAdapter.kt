package kanielOutis.october.recyclerView

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kanielOutis.october.R
import kanielOutis.october.data.BaseViewHolder
import kanielOutis.october.data.Users
import kanielOutis.october.data.inflate
import kanielOutis.october.data.set
import kotlinx.android.synthetic.main.user_item.view.*

class UsersAdapter(private val itemClick : (Users) -> Unit) : RecyclerView.Adapter<BaseViewHolder<Users>>() {

    private val users = ArrayList<Users>()

    fun addUsers(items : ArrayList<Users?>?){
        items?.forEach {
            if (it !in users) {
                users.add(it!!)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Users> {
        return UsersViewHolder(parent.inflate(R.layout.user_item))
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: BaseViewHolder<Users>, position: Int) {
        holder.bind(users[position])
    }

    inner class UsersViewHolder(view : View) : BaseViewHolder<Users>(view){
        override fun bind(item: Users) {
            itemView.setOnClickListener { itemClick(item) }
            itemView.item_user_name set item.name
        }

    }
}