package com.movetoplay.services

import android.accessibilityservice.GestureDescription
import android.graphics.Path

typealias ClickCallback = (GestureDescription) -> Unit

class commandHandler {

    var clickCallback: ClickCallback = {}

//    override fun invoke(command: Command.Click) {
//        val path = Path()
//        path.moveTo(command.x.toFloat(), command.y.toFloat())
//        val builder = GestureDescription.Builder()
//        val gestureDescription = builder
//            .addStroke(GestureDescription.StrokeDescription(path, command.startTime, command.duration))
//            .build()
//        clickCallback(gestureDescription)
//    }

    companion object {
        private const val TAG = "ClickCommandHandler"
    }
}