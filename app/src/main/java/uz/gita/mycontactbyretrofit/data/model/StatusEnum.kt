package uz.gita.mycontactbyretrofit.data.model

enum class StatusEnum(val statusCode: Int) {
    SYNC(0), ADD(1), EDIT(2), DELETE(3)
}


fun Int.toStatusEnum() : StatusEnum =
    when(this) {
        1 -> StatusEnum.ADD
        2 -> StatusEnum.EDIT
        3 -> StatusEnum.DELETE
        else -> StatusEnum.SYNC
    }