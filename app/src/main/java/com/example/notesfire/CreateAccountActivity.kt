package com.example.notesfire

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesfire.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        email = ""
        password = ""

        onFocusEmailListener()
        onFocusPasswordListener()

        binding.edtConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateConfirmPasswords()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.createAccountBtn.setOnClickListener {
            if (binding.edtEmail.text?.isEmpty() == false && binding.edtPassword.text?.isEmpty() == false && binding.edtConfirmPassword.text?.isEmpty() == false) {
                validateAll()
                if (binding.emailContainer.error == null && binding.passwordContainer.error == null && binding.confirmPasswordContainer.error == null) {
                    createAccount()
                }
            }
            else {
                validateAll()
            }
        }

        binding.btnToLogin.setOnClickListener {
            moveToSignIn()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount() {
        showLoading(true)
        email = binding.edtEmail.getText().toString().trim()
        password = binding.edtPassword.getText().toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    showLoading(false)
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    showLoading(false)
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.signUpLoading.visibility = View.VISIBLE
        } else {
            binding.signUpLoading.visibility = View.GONE
        }
    }

    private fun moveToSignIn() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateConfirmPasswords() {
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtConfirmPassword.text.toString()

        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            binding.confirmPasswordContainer.error = "Password not match"
        } else {
            binding.confirmPasswordContainer.error = null
        }
    }

    private fun onFocusEmailListener() {
        binding.edtEmail.setOnFocusChangeListener{ _, focused ->
            if (!focused) {
                binding.emailContainer.error = validateEmail()
            }
        }
    }

    private fun validateEmail(): String? {
        val emailText = binding.edtEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid email address"
        }

        return null
    }

    private fun onFocusPasswordListener() {
        binding.edtPassword.setOnFocusChangeListener{ _, focused ->
            if (!focused) {
                binding.passwordContainer.error = validatePassword()
            }
        }
    }

    private fun validatePassword(): String? {
        val passwordText = binding.edtPassword.text.toString()
        if (passwordText.length < 8) {
            return "Password must have at least 8 characters"
        }
        if (!passwordText.matches(".*[A-Z]+.*".toRegex())) {
            return "Password must contain an uppercase"
        }
        if (!passwordText.matches(".*[0-9]+.*".toRegex())) {
            return "Password must contain a number"
        }

        return null
    }

    private fun validateAll() {
        binding.passwordContainer.error = validatePassword()
        binding.emailContainer.error = validateEmail()
        validateConfirmPasswords()
    }

}