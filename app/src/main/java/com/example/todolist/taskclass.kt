package com.example.todolist

import java.util.*

data class taskclass (
    var id: Int = getId(),
    var task: String = "",
    var date: String = "",
){
    companion object {
        fun getId(): Int {
            val number = Random()
            return number.nextInt(10000)
        }
    }
}