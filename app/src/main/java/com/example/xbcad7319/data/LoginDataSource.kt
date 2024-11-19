package com.example.xbcad7319.data

import com.example.xbcad7319.data.model.LoggedInUser
import java.io.IOException


class LoginDataSource {

        fun login(username: String, password: String): Result<LoggedInUser> {
            try {

                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                return Result.Success(fakeUser)
            } catch (e: Throwable) {
                return Result.Error(IOException("Error logging in", e))
            }
        }

        fun logout() {

        }
    }
