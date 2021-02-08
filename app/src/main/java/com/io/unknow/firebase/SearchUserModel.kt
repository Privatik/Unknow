package com.io.unknow.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.io.unknow.app.App
import com.io.unknow.currentuser.CurrentUser
import com.io.unknow.livedata.SearchUserLiveData
import com.io.unknow.model.Search
import com.io.unknow.model.SearchMen
import com.io.unknow.model.User
import com.io.unknow.parse.CheckParamentMode
import com.io.unknow.parse.DataParse
import javax.inject.Inject

class SearchUserModel(private val liveData: SearchUserLiveData) {
    @Inject
    lateinit var mAuth: FirebaseAuth
    @Inject
    lateinit var databaseReference: DatabaseReference

    private var push: DatabaseReference? = null
    private val createModel: CreateChatModel
    private val checkMode = CheckParamentMode()
    private val baseId = "searchBase"

    init {
        App.appComponent.inject(this)
        createModel = CreateChatModel(databaseReference)
    }

    fun searchUser(search: Search){
        var isFound = false

        val isSex = search.sex == 2
        val isAgeStart = search.ageStart != null
        val isAgeEnd = search.ageEnd != null
        val isHeightStart = search.heightStart != null
        val isHeightEnd = search.heightEnd != null
        val isWeightStart = search.weightStart != null
        val isWeightEnd = search.weightEnd != null
        val isLocal = search.local != ""

        val users = CurrentUser.map.keys

        databaseReference.child(baseId).addListenerForSingleValueEvent(object :
            ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    val user = CurrentUser.user
                    for (userSnaphot in snapshot.children) {
                        val userF = userSnaphot.child("user").getValue(User::class.java)!!
                        val searchF = userSnaphot.child("search").getValue(Search::class.java)!!
                        val uuid = userSnaphot.child("uuid").getValue(String::class.java)!!
                        val searchMen = SearchMen(searchF, userF, uuid)

                        if (searchMen.user.id == user!!.id || searchMen.user.id in users) continue

                        //Проверка на пол
                        if (!isSex) {
                            if (searchMen.user.sex != search.sex) continue
                        }
                        //Проверка на возраст
                        if (checkMode.mode(isAgeStart, isAgeEnd, search.ageStart, search.ageEnd, DataParse.getYear(searchMen.user.age))) continue
                        if (checkMode.mode(searchMen.search.ageStart != null,searchMen.search.ageEnd != null,searchMen.search.ageStart,searchMen.search.ageEnd, DataParse.getYear(user!!.age))) continue
                        //Проверка на рост
                        if (checkMode.mode(isHeightStart,isHeightEnd,search.heightStart,search.heightEnd, searchMen.user.height)) continue
                        if (checkMode.mode(searchMen.search.heightStart != null,searchMen.search.heightEnd != null,search.heightStart,search.heightEnd, user.height)) continue
                        //Проверка на вес
                        if (checkMode.mode(isWeightStart,isWeightEnd,search.weightStart,search.weightEnd, searchMen.user.weight)) continue
                        if (checkMode.mode(searchMen.search.weightStart != null,searchMen.search.weightEnd != null,searchMen.search.weightStart,searchMen.search.weightEnd, user.weight)) continue

                        isFound = true
                        userSnaphot.ref.child("uidUserFriend").setValue(user.id)
                        userSnaphot.ref.removeValue()
                        createModel.createChat(searchMen.uuid,user.id,searchMen.user.id)
                        liveData.founding(searchMen.uuid,searchMen.user.id)
                        break
                    }

                    if (!isFound) { push() }
            }

            override fun onCancelled(error: DatabaseError) { push() }

            private fun push(){
                val pushId = databaseReference.child(baseId).push().key!!
                push = databaseReference.child(baseId).child(pushId)
                push?.setValue(SearchMen(search, CurrentUser.user!!))
                push?.addChildEventListener(object : ChildEventListener{
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}

                    var uidFriend: String? = null
                    var idUser: String? = null
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        Log.i("Delete",snapshot.key)
                        Log.i("Delete","remove ")
                        for (snap in snapshot.children){
                            Log.i("Delete",snap.key)
                        }
                        if (snapshot.key == "user"){
                            //Log.i("Delete", snapshot.child("user").child("id").value as String)
                            idUser = snapshot.child("id").getValue(String::class.java)
                            Log.i("Delete",if (idUser == null) "Yes" else "No" )
                        }
                       if (snapshot.key == "uuid") {
                           if (uidFriend != null) {
                               val uuid = snapshot.getValue(String::class.java)!!
                               createModel.createChat(uuid, idUser!!, uidFriend!!)
                               liveData.founding(uuid, uidFriend!!)
                           }
                       }
                        if (snapshot.key == "uidUserFriend"){
                           uidFriend = snapshot.getValue(String::class.java)!!
                        }
                    }
                })
            }

        })
    }

    fun close() {
        if (push != null) {
              push?.removeValue()
        }
    }
}