package com.example.appsell.model

class Profile(
    var email: String,
    var userName: String,
    var date: String,
    var address: String
) {
    constructor() : this("", "0", "", "")
}