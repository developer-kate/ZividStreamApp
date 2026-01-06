package com.example.zividstreamapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var zividView: ImageView
    private lateinit var statusText: TextView
    private var webSocket: WebSocket? = null

    // 서버 주소
    //private val SERVER_URL = "ws://192.168.0.52:9090"
    private val SERVER_URL = "ws://10.0.2.2:9090"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        zividView = findViewById(R.id.zividView)
        statusText = findViewById(R.id.statusText)

        connectToRos()
    }

    private fun connectToRos() {
        val client = OkHttpClient()
        val request = Request.Builder().url(SERVER_URL).build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                android.util.Log.d("ROS_WS", "서버와 연결 성공!")
                val subscribeMsg = """{"op":"subscribe","topic":"/rgb","type":"sensor_msgs/msg/Image"}"""
                webSocket.send(subscribeMsg)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // 이 로그가 찍히는지 로그캣에서 확인해야 해!
                android.util.Log.d("ROS_WS", "데이터 수신 중...")
                try {
                    val json = JSONObject(text)
                    val msg = json.optJSONObject("msg") ?: return
                    val base64Data = msg.optString("data")

                    if (base64Data.isNotEmpty()) {
                        val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        runOnUiThread { zividView.setImageBitmap(bitmap) }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("ROS_WS", "에러 발생: ${e.message}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                android.util.Log.e("ROS_WS", "연결 실패 이유: ${t.message}")
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "앱 종료")
    }
}