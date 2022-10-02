package com.maru.data.util

import java.text.SimpleDateFormat
import java.util.*


fun getCurrentDate(): String {
    val currentTimeMillis = System.currentTimeMillis()
    val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ", Locale.KOREAN)

    return simpleDataFormat.format(currentTimeMillis)
}

fun generateInvitationCode(): String {
    var invitationCode = ""
    val charPool = (0..9) + ('A'..'Z')

    repeat(6) {
        val randomIndex = Random().nextInt(charPool.size)
        invitationCode += charPool[randomIndex]
    }

    return invitationCode
}