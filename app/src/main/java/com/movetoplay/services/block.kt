package com.movetoplay.screens

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.movetoplay.R

var autoClickService: AutoClickService? = null

class AutoClickService : AccessibilityService() {

    private lateinit var windowManager: WindowManager
    private lateinit var params: WindowManager.LayoutParams

    private lateinit var overlayMenu: View

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0.0f
    private var initialTouchY = 0.0f
    private var moving = false

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayMenu = LayoutInflater.from(this)
            .inflate(R.layout.activity_test_block, null, false)

//        overlayMenu.apply {
//            findViewById<ImageButton>(R.id.playBtn).setOnClickListener() { toast("Play btn pressed") }
//            findViewById<ImageButton>(R.id.addBtn).setOnClickListener() { toast("Add btn pressed") }
//            findViewById<ImageButton>(R.id.removeBtn).setOnClickListener() { toast("Remove btn pressed") }
//        }

        overlayMenu.setOnTouchListener { view, event ->

            view.performClick()

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    moving = true
                }
                MotionEvent.ACTION_UP -> {
                    moving = true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(overlayMenu, params)
                }
            }
            true
        }

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            200,
            200,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 100

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        open()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.block)
            .build()
    }

//    private val events = mutableListOf<Event>()
//    private val commandHandler by inject<commandHandler>()

    private fun toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun open() {
        try {
            if (overlayMenu.windowToken == null) {
                if (overlayMenu.parent == null) {
                    windowManager.addView(overlayMenu, params)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    override fun onInterrupt() {
        // NO-OP
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // NO-OP
    }

//    override fun onServiceConnected() {
//        super.onServiceConnected()
//        commandHandler.clickCallback = { gestureDescription ->
//            dispatchGesture(gestureDescription, null, null)
//
//        }
//        autoClickService = this
//        startActivity(
//            Intent(this, MainActivity::class.java)
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//    }

//    fun click(x: Int, y: Int) {
//        scenariosHandler.invoke(
//            Scenarios.Single(listOf(Command.Click.PinCodeClick(x = x, y = y))) //TODO: revert refactor
//        )
//    }

    fun click(x: Int, y: Int) {
        Toast.makeText(applicationContext, "Click", Toast.LENGTH_SHORT).show()

        val path = Path()
        path.moveTo(x.toFloat(), y.toFloat())
        val builder = GestureDescription.Builder()
        val gestureDescription = builder
            .addStroke(GestureDescription.StrokeDescription(path, 10, 10))
            .build()
        dispatchGesture(gestureDescription, null, null)
    }

//    fun run(newEvents: MutableList<Event>) {
//        events.clear()
//        events.addAll(newEvents)
//        val builder = GestureDescription.Builder()
//        events.forEach { builder.addStroke(it.onEvent()) }
//        dispatchGesture(builder.build(), null, null)
//    }

    override fun onUnbind(intent: Intent?): Boolean {
        autoClickService = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        autoClickService = null
//        commandHandler.clickCallback = {}
        windowManager.removeView(overlayMenu)
        super.onDestroy()
    }

    companion object {
        private const val TAG = "AutoClickService"
        private const val CHANNEL_ID = "AutoClick"
        private const val CHANNEL_NAME = "AutoServiceClicker"
        private const val NOTIFICATION_ID = 1
        fun newIntent(context: Context) = Intent(context, AutoClickService::class.java)
    }
}
