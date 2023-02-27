package com.example.chat

data class Message(
    var message: String?,
    var sendId: String?
){
    constructor(): this("","")
}
