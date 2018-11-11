package com.github.kieuthang.login_chat.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.data.entity.*
import com.github.kieuthang.login_chat.views.common.BaseFragmentActivity
import kotlinx.android.synthetic.main.activity_home.*

class ActivityHome : BaseFragmentActivity() {
    private var mUserModel: UserModel? = null
    private var mRooms: ArrayList<RoomModel>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mDataPresenter!!.getMyProfile(true)

        btnJoinRoom.setOnClickListener {
            val selectedRoomName = spinner.selectedItem as String
            var selectedRoom: RoomModel? = null
            for(room in mRooms!!){
                if(room.name == selectedRoomName){
                    selectedRoom = room
                    break
                }
            }
            if(selectedRoom == null){
                showError(getString(R.string.something_went_wrong_please_try_again_later))
                return@setOnClickListener
            }
            startActivity(ActivityChat.createIntent(this, selectedRoom))
            finish()
        }

        btnCreateRoom.setOnClickListener {
            val roomName = edtRoomName.text.toString()
            if (TextUtils.isEmpty(roomName)) {
                showToastMessage(getString(R.string.room_name_cannot_be_empty))
                return@setOnClickListener
            }

            mDataPresenter!!.addRoom(roomName)
        }
    }

    override fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?) {
        super.onGetMyProfileResult(t, throwable)
        if (throwable != null || t == null) {
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }
        mUserModel = t.userModel
        updateUI()

        mDataPresenter!!.getRooms()
    }

    override fun onAddRoomResult(t: RoomResponseModel?, throwable: Throwable?) {
        super.onAddRoomResult(t, throwable)
        if (throwable != null || t == null || t.room == null) {
            showToastMessage(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }

        startActivity(ActivityChat.createIntent(this, t.room!!))
        finish()
    }

    override fun onGetRoomsResult(t: RoomsResponseModel?, throwable: Throwable?) {
        super.onGetRoomsResult(t, throwable)
        if (throwable != null || t == null) {
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }

        //create a list of items for the spinner.
        mRooms = t.rooms
        val items: ArrayList<String> = ArrayList()
        for (room in t.rooms!!) {
            items.add(room.name!!)
        }
        val adapter = ArrayAdapter(this@ActivityHome, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
    }

    private fun updateUI() {
        if (mUserModel != null) {
            val userName = mUserModel!!.firstName + " " + mUserModel!!.lastName
            tvUserName.text = "Welcome:  $userName"
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ActivityHome::class.java)
        }
    }
}