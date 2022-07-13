package com.android.appcomponents.customui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment

class UIDialogFragment(resLayout: Int) : DialogFragment() {

    var layoutRes: Int = resLayout

    companion object {
        fun newInstance(@LayoutRes layoutRes: Int): UIDialogFragment {
            return UIDialogFragment(layoutRes)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}