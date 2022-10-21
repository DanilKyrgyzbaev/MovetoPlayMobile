package com.movetoplay.util

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import java.util.regex.Pattern

object ValidationUtil {

    fun showToast(context: Context, message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    fun isNullOrEmpty(input: String?): Boolean = input == null || input.isEmpty()

    fun isValidUsername(context: Context, username: String?, regex: String = "^[a-zA-Z0-9._-]{3,20}$"): Boolean {
        when {
            isNullOrEmpty(username) -> showToast(context, "Please enter User name first.")
            !Pattern.matches(regex, username!!) -> showToast(context, "Please enter a valid User name.")
            else -> return true
        }
        return false
    }

    fun isValidEmail(context: Context, email: String): Boolean {
        when {
            isNullOrEmpty(email) -> showToast(context, "Please enter Email first.")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast(context, "Please enter a valid Email address.")
            else -> return true
        }
        return false
    }

    fun isValidPassword(context: Context, password: String): Boolean {
        when {
            isNullOrEmpty(password) -> showToast(context, "Please enter Password first.")
            password.length < 6 -> showToast(context, "Password length should not be less than 6 characters")
            password.length > 30 -> showToast(context, "Password length should not be greater than 30 characters")
            else -> return true
        }
        return false
    }
    fun isValidAge(context: Context, age: String): Boolean {
        when {
            isNullOrEmpty(age) -> showToast(context, "Please enter Age first.")
            !Pattern.matches("\\b([1-9]|[1-9][0-9]|100)\\b", age) -> showToast(context,
                "Please enter valid age")
            else -> return true
        }
        return false
    }

    fun isValidName(context: Context, name: String): Boolean {
        when {
            isNullOrEmpty(name) -> showToast(context, "Please enter Name first.")
            name.length < 2 -> showToast(context,
                "Name length should not be less than 2 characters")
            else -> return true
        }
        return false
    }
}