package com.maru.data.network.firebase

import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.database.DatabaseReference
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import com.maru.data.util.Constants
import com.maru.data.util.generateInvitationCode
import com.maru.data.util.getCurrentDate
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseHelper @Inject constructor(
    private val firebaseDatabase: DatabaseReference
) {

    suspend fun registerNewUser(user: RegisterUserRequest): User =
        suspendCoroutine { continuation ->
            firebaseDatabase.child("idSequence").get().addOnCompleteListener {
                val userId = it.result.value.toString().toInt()
                val newUser = User(
                    userId,
                    Constants.EMPTY_STRING,
                    generateInvitationCode(),
                    Constants.EMPTY_STRING,
                    user.gender,
                    user.nickname,
                    user.profileUrl,
                    user.birthday,
                    getCurrentDate()
                )

                firebaseDatabase.child("user").child(userId.toString()).setValue(newUser)
                firebaseDatabase.child("idSequence").setValue(userId + 1)

                continuation.resume(newUser)
            }
        }

        suspend fun getCodeById(id: Int): String = suspendCoroutine { continuation ->
            firebaseDatabase.child("user").child(id.toString()).child("code").get().addOnSuccessListener {
                val code = it.value.toString()
                continuation.resume(code)
            }
        }

//    suspend fun findUserByInviteCode(inviteCode: String): String = suspendCoroutine { continuation ->
//        firebaseDatabase.child("user").orderByChild("code").equalTo(inviteCode).get().addOnCompleteListener {
//            val user = it.result.value.toString()
//            continuation.resume(user)
//        }
//    }
}