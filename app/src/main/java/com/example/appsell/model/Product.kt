package com.example.appsell.model

class Product(
    var productName: String,
    var cost: Long,
    var description: String,
    var key: String,
    var type: String,
    var urlImage: String,
) {
    constructor() : this("", 0, "", "", "", "")
}
