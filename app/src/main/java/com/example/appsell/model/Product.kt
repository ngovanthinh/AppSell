package com.example.appsell.model

class Product(
    var productName: String,
    var cost : Long,
    var description : String
){
    constructor(): this("",0,"")
}
