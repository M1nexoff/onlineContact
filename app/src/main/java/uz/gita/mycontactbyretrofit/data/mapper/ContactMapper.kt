package uz.gita.mycontactbyretrofit.data.mapper

import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.data.model.StatusEnum
import uz.gita.mycontactbyretrofit.data.model.toStatusEnum
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse

object ContactMapper {

    fun ContactResponse.toUIData() : ContactUIData =
        ContactUIData(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = StatusEnum.SYNC
        )

    fun ContactEntity.toUIData(id : Int) : ContactUIData =
        ContactUIData(
            id = id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = this.statusCode.toStatusEnum()
        )
}

