package uz.gita.mycontactbyretrofit.domain

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import uz.gita.mycontactbyretrofit.data.mapper.ContactMapper.toUIData
import uz.gita.mycontactbyretrofit.data.local.dao.ContactDao
import uz.gita.mycontactb7.data.source.local.entity.ContactEntity
import uz.gita.mycontactbyretrofit.data.model.ContactUIData
import uz.gita.mycontactbyretrofit.data.model.StatusEnum
import uz.gita.mycontactbyretrofit.data.model.toStatusEnum
import uz.gita.mycontactbyretrofit.data.remote.api.Api
import uz.gita.mycontactbyretrofit.data.remote.request.CreateContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.EditContactRequest
import uz.gita.mycontactbyretrofit.data.remote.request.LoginRequest
import uz.gita.mycontactbyretrofit.data.remote.request.RegisterRequest
import uz.gita.mycontactbyretrofit.data.remote.request.VerifySmsRequest
import uz.gita.mycontactbyretrofit.data.remote.response.ContactResponse
import uz.gita.mycontactbyretrofit.data.remote.response.ErrorResponse
import uz.gita.mycontactbyretrofit.data.remote.response.LoginResponse
import uz.gita.mycontactbyretrofit.data.remote.response.RegisterResponse
import uz.gita.mycontactbyretrofit.data.remote.response.VerifySmsResponse
import uz.gita.mycontactbyretrofit.utils.NetworkStatusValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val pref: SharedPreferences,
    private val api: Api,
    private val contractDao: ContactDao,
    private val gson: Gson,
    private val networkStatusValidator: NetworkStatusValidator
): AppRepository {
    override var token: String = pref.getString("token","")!!
        set(value) {
            field = value
            pref.edit().putString("token",value).apply()
        }
    override var phone: String = pref.getString("phone","")!!
        set(value) {
            field = value
            pref.edit().putString("phone",value).apply()
        }

    override fun registerUser(registerRequest: RegisterRequest): Call<RegisterResponse> {
        return api.registerUser(registerRequest)
    }

    override fun verifySmsCode(verifySmsRequest: VerifySmsRequest): Call<VerifySmsResponse> {
        return api.verifySmsCode(verifySmsRequest)
    }

    override fun loginUser(loginRequest: LoginRequest): Call<LoginResponse> {
        return api.loginUser(loginRequest)
    }

    override fun addContact(
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit,
    ) {
        if (networkStatusValidator.hasNetwork) {
            val request = CreateContactRequest(firstName, lastName, phone)
            api.addContact(token,request).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(
                    call: Call<ContactResponse>,
                    response: Response<ContactResponse>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(
                            response.errorBody()!!.string(), ErrorResponse::class.java
                        )
                        if (data != null) {
                            errorBlock.invoke(data.message)
                        } else {
                            errorBlock.invoke("Unknown error!")
                        }
                    } else errorBlock.invoke("Unknown error!!")
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contractDao.insertContact(
                ContactEntity(
                    0, 0, firstName, lastName, phone, StatusEnum.ADD.statusCode
                )
            )
            successBlock.invoke()
        }
    }

    override fun editContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit
    ) {
        if (networkStatusValidator.hasNetwork) {
            val request = EditContactRequest(id,firstName, lastName, phone)
            api.editContactByID(token,request).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(
                    call: Call<ContactResponse>,
                    response: Response<ContactResponse>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(
                            response.errorBody()!!.string(), ErrorResponse::class.java
                        )
                        if (data != null) {
                            errorBlock.invoke(data.message)
                        } else {
                            errorBlock.invoke("Unknown error!")
                        }
                    } else errorBlock.invoke("Unknown error!!")
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contractDao.insertContact(
                ContactEntity(
                    0, id, firstName, lastName, phone, StatusEnum.EDIT.statusCode
                )
            )
            successBlock.invoke()
        }
    }

    override fun deleteContact(
        id: Int,
        firstName: String,
        lastName: String,
        phone: String,
        successBlock: () -> Unit,
        errorBlock: (String) -> Unit
    ) {
        if (networkStatusValidator.hasNetwork) {
            api.deleteContactByID(token,id).enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>,
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(
                            response.errorBody()!!.string(), ErrorResponse::class.java
                        )
                        if (data != null) {
                            errorBlock.invoke(data.message)
                        } else {
                            errorBlock.invoke("Unknown error!")
                        }
                    } else errorBlock.invoke("Unknown error!!")
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contractDao.insertContact(
                ContactEntity(
                    0, id, firstName, lastName, phone, StatusEnum.DELETE.statusCode
                )
            )
            successBlock.invoke()
        }
    }
    override fun getAllContacts(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit){
        api.getAllContacts(token).enqueue(object : Callback<List<ContactResponse>> {
            override fun onResponse(
                call: Call<List<ContactResponse>>,
                response: Response<List<ContactResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    successBlock.invoke(
                        mergeData(
                            response.body()!!, contractDao.getAllContactFromLocal()
                        )
                    )
                } else if (response.errorBody() != null) {
                    val data =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    if (data != null) {
                        errorBlock.invoke(data.message)
                    } else {
                        errorBlock.invoke("Unknown error!")
                    }
                } else errorBlock.invoke("Unknown error!")
            }

            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
                t.message?.let { errorBlock.invoke(it) }
            }
        })
    }
    private fun mergeData(
        remoteList: List<ContactResponse>,
        localList: List<ContactEntity>,
    ): List<ContactUIData> {
        val result = ArrayList<ContactUIData>()
        result.addAll(remoteList.map { it.toUIData() })

        var index = remoteList.lastOrNull()?.id ?: 0      // face
        localList.forEach { entity ->
            when (entity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    result.add(entity.toUIData(++index))
                }

                StatusEnum.EDIT -> {
                    val findData = result.find { it.id == entity.remoteID }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.DELETE -> {
                    val findData = result.find { it.id == entity.remoteID }
                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        result[findIndex] = newData
                    }
                }

                StatusEnum.SYNC -> {}
            }
        }

        return result
    }

    override fun syncWithServer(finishBlock: () -> Unit, errorBlock: (String) -> Unit) {
        val list = contractDao.getAllContactFromLocal()
        Timber.tag("BBB").d("size ->" + list.size.toString())
        list.forEach {
            api.addContact(token, CreateContactRequest(it.firstName, it.lastName, it.phone))
                .enqueue(object : Callback<ContactResponse> {
                    override fun onResponse(
                        call: Call<ContactResponse>,
                        response: Response<ContactResponse>,
                    ) {
                        contractDao.deleteContact(it)
                        if (response.isSuccessful && response.body() != null) {
                            finishBlock.invoke()
                        } else if (response.errorBody() != null) {
                            val data = gson.fromJson(
                                response.errorBody()!!.string(), ErrorResponse::class.java
                            )
                            if (data != null) {
                                errorBlock.invoke(data.message)
                            } else {
                                errorBlock.invoke("Unknown error!")
                             }
                        } else errorBlock.invoke("Unknown error!!")
                    }

                    override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                        t.message?.let { message -> errorBlock.invoke(message) }
                    }
                })
        }
    }
}