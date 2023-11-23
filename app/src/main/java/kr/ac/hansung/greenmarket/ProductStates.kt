package kr.ac.hansung.greenmarket

enum class ProductStates(val code: Int, val description: String) {
    ON_SALE(0, "판매중"),
    RESERVED(1, "예약중"),
    SOLD_OUT(2, "판매 완료")
}