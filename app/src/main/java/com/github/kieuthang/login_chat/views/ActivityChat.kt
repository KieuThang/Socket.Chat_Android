package com.github.kieuthang.login_chat.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.github.kieuthang.login_chat.ChatApplication
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.AppConstants.TAG
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.common.utils.TimeUtils
import com.github.kieuthang.login_chat.data.entity.Message
import com.github.kieuthang.login_chat.data.entity.UserModel
import com.github.kieuthang.login_chat.data.entity.UserResponseModel
import com.github.kieuthang.login_chat.views.common.BaseFragmentActivity
import com.github.kieuthang.login_chat.views.widget.SFUITextView
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat.*
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONException
import org.json.JSONObject

class ActivityChat : BaseFragmentActivity() {
    private var mRoomName: String? = null
    private var mSocket: Socket? = null

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val DEFAULT_PAGE_SIZE = 100
    private var isLoading: Boolean = false
    private var isConnected: Boolean = true
    private var mTyping: Boolean = false

    private var mMessage: ArrayList<Message> = ArrayList()
    private var mUserModel: UserModel? = null
    private var mUsername: String? = null

    companion object {
        fun createIntent(context: Context, roomName: String): Intent {
            val intent = Intent(context, ActivityChat::class.java)
            intent.putExtra("room_name", roomName)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_drawer)

        mRoomName = intent.getStringExtra("room_name")
        if (TextUtils.isEmpty(mRoomName)) {
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            finish()
            return
        }

        setupData()
        setupLayout()
    }

    private fun setupLayout() {
        btnSendMessage.setOnClickListener {
            val message = edtSendMessage.text.toString()
            if (TextUtils.isEmpty(message))
                return@setOnClickListener
            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {
        if (null == mUsername) return
        if (!mSocket!!.connected()) return

        mTyping = false

        //addMessage(mUsername, message)
        val jsonObject = JSONObject()
        try {
            jsonObject.put("username", mUsername)
            jsonObject.put("message", message)
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        // perform the sending message attempt.
        mSocket!!.emit("client__sent_message", jsonObject.toString())
    }

    private fun setupData() {
        val app = application as ChatApplication
        mSocket = app.socket
        mSocket!!.on("server__join_room_welcome", onLogin)
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on("server___user_disconnect", onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        mSocket!!.on("server__sent_message", onNewMessage)
        mSocket!!.on("server__new_user_joined", onUserJoined)
        mSocket!!.on("server__user_left", onUserLeft)
        mSocket!!.on("server__user_typing", onTyping)
        mSocket!!.on("server__user_stop_typing", onStopTyping)
        mSocket!!.on("server__update_room", onUpdateRoom)
        mSocket!!.on("server__join_room_welcome", onJoinRoomWelcome)
        mSocket!!.connect()

        mDataPresenter!!.getMyProfile(false)
    }

    override fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?) {
        super.onGetMyProfileResult(t, throwable)
        if (t?.userModel != null) {
            mUserModel = t.userModel
            mUsername = mUserModel!!.firstName + " " + mUserModel!!.lastName
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mSocket!!.off("server__new_user_joined", onLogin)
    }

    private val onLogin = Emitter.Listener { args ->
        val data = args[0] as JSONObject

        val numUsers: Int
        try {
            numUsers = data.getInt("numUsers")
        } catch (e: JSONException) {
            return@Listener
        }
        AppLog.d(AppConstants.TAG, "numUsers: $numUsers")
    }

    private val onConnect = Emitter.Listener {
        runOnUiThread {
            if (!isConnected) {
                if (null != mUsername)
                    mSocket!!.emit("client__add_user", mUsername)
                showToastMessage(getString(R.string.connect))
                isConnected = true
            }
        }
    }

    private val onDisconnect = Emitter.Listener {
        AppLog.d(TAG, "diconnected")
        runOnUiThread {
            isConnected = false
            showToastMessage(getString(R.string.disconnect))
        }
    }

    private val onConnectError = Emitter.Listener {
        runOnUiThread {
            showToastMessage(getString(R.string.error_connect))
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val message: String
            try {
                username = data.getString("username")
                message = data.getString("message")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            removeTyping(username)
//            if (TextUtils.equals(mUsername, username))
//                return@Runnable
//            addMessage(username, message)
        })
    }

    private val onUserJoined = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            addLog(resources.getString(R.string.message_user_joined, username))
//            addParticipantsLog(numUsers)
        })
    }

    private val onUserLeft = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            addLog(resources.getString(R.string.message_user_left, username))
//            addParticipantsLog(numUsers)
//            removeTyping(username)
        })
    }

    private val onTyping = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            try {
                username = data.getString("username")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            if (TextUtils.equals(mUsername, username))
//                return@Runnable
//            addTyping(username)
        })
    }

    private val onStopTyping = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            try {
                username = data.getString("username")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            removeTyping(username)
        })
    }

    private val onUpdateRoom = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val username: String
            val numUsers: Int
            try {
                username = data.getString("username")
                numUsers = data.getInt("numUsers")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            addLog(resources.getString(R.string.message_user_joined, username))
//            addParticipantsLog(numUsers)
        })
    }

    private val onJoinRoomWelcome = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val data = args[0] as JSONObject
            val room: String

            try {
                room = data.getString("room")
            } catch (e: JSONException) {
                Log.e(TAG, e.message)
                return@Runnable
            }

//            addLog(getString(R.string.welcom_to_join, room))
        })
    }

    private val onTypingTimeout = Runnable {
        if (!mTyping) return@Runnable

        mTyping = false
        mSocket!!.emit("client__stop_typing", mUsername)
    }

    private inner class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val mInflater: LayoutInflater = LayoutInflater.from(this@ActivityChat)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == VIEW_TYPE_ITEM) {
                val view = mInflater.inflate(R.layout.layout_item_chat, parent, false)
                return ViewHolder(view)
            } else if (viewType == VIEW_TYPE_LOADING) {
                val loadingView = mInflater.inflate(R.layout.item_loading, parent, false)
                return LoadingViewHolder(loadingView)
            }
            return null!!
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val message = mMessage[position]
            if (holder is ViewHolder) {
                holder.bind(message)
            } else if (holder is LoadingViewHolder) {
                holder.progressBar.isIndeterminate = true
            }
        }

        override fun getItemCount(): Int {
            return mMessage.size
        }

        override fun getItemViewType(position: Int): Int {
            val alarmChat = mMessage[position]
            return if (alarmChat.type == Message.TYPE_LOADING_VIEW) {
                VIEW_TYPE_LOADING
            } else
                VIEW_TYPE_ITEM
        }

        fun updateData() {
            notifyDataSetChanged()
        }

        internal inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            internal var progressBar: ProgressBar = view.findViewById(R.id.progressBar1)
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val layoutContainerLeft = itemView.findViewById<RelativeLayout>(R.id.layout_container_left)
            private val layoutContainerRight = itemView.findViewById<RelativeLayout>(R.id.layout_container_right)
            private val tvUserInfoLeft = itemView.findViewById<SFUITextView>(R.id.tv_user_info_left)
            private val tvMessageLeft = itemView.findViewById<SFUITextView>(R.id.tv_message_left)
            private val tvTimeLeft = itemView.findViewById<SFUITextView>(R.id.tv_time_left)
            private val tvUserInfoRight = itemView.findViewById<SFUITextView>(R.id.tv_user_info_right)
            private val tvMessageRight = itemView.findViewById<SFUITextView>(R.id.tv_message_right)
            private val tvTimeRight = itemView.findViewById<SFUITextView>(R.id.tv_time_right)
            private val layoutChatRight = itemView.findViewById<LinearLayout>(R.id.layout_chat_right)
            private val layoutChatLeft = itemView.findViewById<LinearLayout>(R.id.layout_chat_left)

            init {
                Linkify.addLinks(tvMessageLeft, Linkify.WEB_URLS)
                Linkify.addLinks(tvMessageRight, Linkify.WEB_URLS)
            }

            fun bind(message: Message) {
                updateChatStyle(message)

                if (!TextUtils.isEmpty(message.message))
                    message.message = message.message!!.trim()
                tvMessageLeft.text = StringEscapeUtils.unescapeJava(message.message)
                tvMessageRight.text = StringEscapeUtils.unescapeJava(message.message)
                tvUserInfoLeft.text = message.createdByName

                tvUserInfoRight.text = message.createdByName
                tvTimeLeft.text = TimeUtils.convertChatItemDateTime(message.sentOn)
                tvTimeRight.text = TimeUtils.convertChatItemDateTime(message.sentOn)
            }

            private fun updateChatStyle(message: Message) {
                layoutContainerLeft.visibility = View.GONE
                layoutContainerRight.visibility = View.GONE
                if (message.sentByMe) {
                    layoutContainerRight.visibility = View.VISIBLE
                    layoutChatRight.setBackgroundResource(R.drawable.bg_chat_blue_right)
                    tvUserInfoRight.setTextColor(resources.getColor(android.R.color.white))
                    tvMessageRight.setTextColor(resources.getColor(android.R.color.white))
                    tvTimeRight.setTextColor(resources.getColor(android.R.color.white))
                } else {
                    layoutContainerLeft.visibility = View.VISIBLE
                    layoutChatLeft.setBackgroundResource(R.drawable.bg_chat_grey_left)
                    tvUserInfoLeft.setTextColor(resources.getColor(R.color.colorActiveAlarm))
                    tvMessageLeft.setTextColor(resources.getColor(android.R.color.black))
                    tvTimeLeft.setTextColor(resources.getColor(R.color.colorTextAlarmChatTime))
                }
            }
        }

        fun addChatItem(testAlarmChat: Message) {
            // mMessage.add(testAlarmChat)
            val size = mMessage.size
            notifyItemInserted(size)
        }

        fun setLoaded() {
            isLoading = false
        }
    }
}