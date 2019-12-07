package kanielOutis.october.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import kanielOutis.october.R
import kanielOutis.october.data.getUserInformation
import kanielOutis.october.data.storeUserInformation
import kanielOutis.october.viewModel.LoginVM


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (getUserInformation()?.token != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this).get(LoginVM::class.java)

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.login(username.text.toString(), password.text.toString())
                .observeForever {
                    loading.visibility = View.INVISIBLE
                    storeUserInformation(it.user)
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }

    }
}
