package protocol

import com.google.gson.Gson
import protocol.message.Message

fun Message.encode() =
    Gson().toJson(this)

fun String.asMessage() = Gson().fromJson(this, Message::class.java)