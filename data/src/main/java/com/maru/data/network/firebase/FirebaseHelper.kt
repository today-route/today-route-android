package com.maru.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import com.maru.data.network.RegisterUserRequest
import com.maru.data.util.Constants
import com.maru.data.util.generateInvitationCode
import com.maru.data.util.getCurrentDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseHelper @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun registerNewUser(user: RegisterUserRequest): User =
        suspendCoroutine { continuation ->
            db.collection("idSequence").get().addOnSuccessListener { result ->
                val userId = result.documents[0].data?.get("number").toString().toInt()
                println(userId)
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

                db.collection("users").add(newUser).addOnSuccessListener { println("success") }.addOnFailureListener { println("fail") }
                db.collection("idSequence").document("idSequence").update("number", userId + 1)

                continuation.resume(newUser)
            }
        }

        suspend fun getCodeById(id: Int): String = suspendCoroutine { continuation ->
            db.collection("users").whereEqualTo("id", id).get().addOnSuccessListener {
                val user = it.documents[0].toObject<User>()
                user?.let { continuation.resume(user.code) }
            }
        }

    suspend fun findUserByInviteCode(inviteCode: String): User =
        suspendCoroutine { continuation ->
            db.collection("users").whereEqualTo("code", inviteCode).get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {
                        val user = it.documents[0].toObject<User>()
                        user?.let { continuation.resume(user) }
                    } else {
                        continuation.resume(User())
                    }
                }
        }

    suspend fun registerNewCouple(coupleInfo: CoupleInfo): CoupleInfo = suspendCoroutine { continuation ->
        db.collection("coupleSequence").get().addOnSuccessListener { result ->
            val coupleId = result.documents[0].data?.get("number").toString().toInt()
            val newCouple = CoupleInfo(
                coupleId,
                coupleInfo.startDate,
                user1ID = coupleInfo.user1ID,
                user2Id = coupleInfo.user2Id
            )

            db.collection("couples").add(newCouple)
            db.collection("coupleSequence").document("coupleSequence").update("number", coupleId + 1)

            continuation.resume(newCouple)
        }
    }
}