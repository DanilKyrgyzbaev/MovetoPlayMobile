package com.movetoplay.screens

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.movetoplay.R

class IsChildDeviceDialogFragment(val listener: (Boolean) -> Unit) : DialogFragment() {

    private var btn_e_like: Button? = null
    private var radio_group: RadioGroup? = null
    private var isChild = false

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_dialog_auth_like, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        btn_e_like!!.setOnClickListener {
            listener(isChild)
            dismiss()
        }
        radio_group?.setOnCheckedChangeListener { _, i ->
            isChild = when (i) {
                R.id.rb_child -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun setupView(view: View) {
        btn_e_like = view.findViewById<View>(R.id.btn_enter_like) as Button?
        radio_group = view.findViewById<View>(R.id.radio_group) as RadioGroup
    }
}