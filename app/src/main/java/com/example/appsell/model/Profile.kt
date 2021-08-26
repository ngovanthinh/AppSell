package com.example.appsell.model

class Profile(
    var userName: String,
    var date: String,
    var address: String,
    var isManager: Boolean = false
) {
    constructor() : this("", "0", "", false)
}