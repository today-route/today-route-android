package com.maru.data.network.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.maru.data.model.CoupleInfo
import com.maru.data.model.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseHelper @Inject constructor(
    private val db: FirebaseFirestore
) {

//    suspend fun registerNewUser(user: SignInRequest): User =
//        suspendCoroutine { continuation ->
//            db.collection("idSequence").get().addOnSuccessListener { result ->
//                val userId = result.documents[0].data?.get("number").toString().toInt()
//                println(userId)
//                val newUser = User(
//                    userId,
//                    Constants.EMPTY_STRING,
//                    generateInvitationCode(),
//                    Constants.EMPTY_STRING,
//                    user.gender,
//                    user.nickname,
//                    user.profileUrl,
//                    user.birthday,
//                    getCurrentDate()
//                )
//
//                db.collection("users").add(newUser).addOnSuccessListener { println("success") }.addOnFailureListener { println("fail") }
//                db.collection("idSequence").document("idSequence").update("number", userId + 1)
//
//                continuation.resume(newUser)
//            }
//        }

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
                user1Id = coupleInfo.user1Id,
                user2Id = coupleInfo.user2Id
            )

            db.collection("couples").add(newCouple)
            db.collection("coupleSequence").document("coupleSequence").update("number", coupleId + 1)

            continuation.resume(newCouple)
        }
    }

    suspend fun findCoupleInfoById(id: Int): CoupleInfo = suspendCoroutine { continuation ->
        db.collection("couples").whereEqualTo("user2Id", id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    val coupleInfo = it.documents[0].toObject<CoupleInfo>()
                    coupleInfo?.let { continuation.resume(coupleInfo) }
                } else {
                    continuation.resume(CoupleInfo())
                }
            }
    }

    suspend fun getUserById(id: Int): User = suspendCoroutine { continuation ->
        db.collection("users").whereEqualTo("id", id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    val user = it.documents[0].toObject<User>()
                    user?.let { continuation.resume(user) }
                } else {
                    continuation.resume(User())
                }
            }
    }

    suspend fun getCoupleInfoById(id:Int): CoupleInfo = suspendCoroutine { continuation ->
        db.collection("couples").whereEqualTo("id", id).get()
            .addOnSuccessListener {
                if (it.documents.isNotEmpty()) {
                    val coupleInfo = it.documents[0].toObject<CoupleInfo>()
                    coupleInfo?.let { continuation.resume(coupleInfo) }
                } else {
                    continuation.resume(CoupleInfo())
                }
            }
    }
}