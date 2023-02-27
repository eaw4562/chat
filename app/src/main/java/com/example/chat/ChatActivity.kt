package com.example.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var reciverName: String
    private lateinit var reciverUid: String


    //lateinit : 늦은 초기화 (생성과 동시에 초기화 x)
    private lateinit var binding: ActivityChatBinding

    lateinit var mAuth: FirebaseAuth //인증객체
    lateinit var mDbRef: DatabaseReference //db객체

    private lateinit var reciverRoom : String //받는 대화방
    private lateinit var senderRoom: String //보낸 대화방

    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList = ArrayList()
        val messageAdapter: MessageAdapter = MessageAdapter(this, messageList)

        //RecyclerView
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        //넘어온 데이터 변수에 담기
        reciverName = intent.getStringExtra("name").toString()
        reciverUid = intent.getStringExtra("uid").toString()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        //접속자 Uid
        val senderUid = mAuth.currentUser?.uid

        //보낸이 방
        senderRoom = reciverUid + senderUid

        //받는이 방
        reciverRoom = senderUid + reciverUid

        //액션바에 상대방 이름 보여주기
        supportActionBar?.title = reciverName

        //메시지 전송 버튼 이벤트
        binding.sendBtn.setOnClickListener {
            val message = binding.messageEdit.text.toString()
            val messageObject = Message(message, senderUid)

            //데이터 저장
            mDbRef.child("chat").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //채팅 저장 성공
                    mDbRef.child("chats").child(reciverRoom).child("messages").push()
                        .setValue(messageObject)
                }
            //입력값 초기화
            binding.messageEdit.setText("")
        }
        //메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapshat in snapshot.children){

                        val message = postSnapshat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    //적용
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}