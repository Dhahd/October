package kanielOutis.october.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kanielOutis.october.R
import kanielOutis.october.data.clearUserInformation
import kanielOutis.october.data.getUserInformation
import kanielOutis.october.recyclerView.UsersAdapter
import kanielOutis.october.viewModel.UsersViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        title = getUserInformation()?.info?.name
    }

    lateinit var usersAdapter : UsersAdapter
    lateinit var usersViewModel: UsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val token = getUserInformation()?.token!!
        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)
        usersAdapter = UsersAdapter {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("id", it.id!!)
            intent.putExtra("name", it.name)
            startActivity(intent)
        }
        usersViewModel.apply {
            getUsers(token).observeForever {
                usersAdapter.addUsers(it.users!!)
                allChat_rv.apply {
                    layoutManager = LinearLayoutManager(this@HomeActivity)
                    adapter = usersAdapter
                }
            }
            errorHandler.observeForever {
                Log.e("usersError", it.toString())
            }
            failure.observeForever {
                Log.e("usersError", it.toString())
            }
        }
        fab.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            val intent = Intent(this, LoginActivity::class.java)
            clearUserInformation()
            startActivity(intent)
            finish()
        }
        return true
    }
}
