package com.example.appsell.model

class Order(
    var product: Product?,
    var count: Int,
) {
    constructor() : this(null, 0)
}