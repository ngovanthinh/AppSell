package com.example.appsell.base

class Constant {
    companion object {
        public const val ODER = "order_doing" // đặt hàng
        public const val SELL = "order_success"  // bán hàng
        public const val CANCEL = "order_cancel"  // bán hàng

        const val TYPE_CART = "payment_cash"
        const val TYPE_RECEIPT = "payment_receipt"
        const val TYPE_TRANSFER = "payment_transfer"

        const val USER_PROFILE = "user_profile"

        const val PRODUCT_VEGETABLE: String = "vegetable"
        const val PRODUCT_PACKAGED: String = "packaged_food"
        const val PRODUCT_MEAT: String = "fresh_meat"
        const val PRODUCT_DIFFERENT: String = "different"

        const val PRODUCT_TYPE : String="product_type"
    }
}