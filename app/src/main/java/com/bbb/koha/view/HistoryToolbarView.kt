package com.bbb.koha.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bbb.koha.R
import com.bbb.koha.common.Constant
import com.bbb.koha.common.SharedPreference
import com.bbb.koha.databinding.HistoryToolbarViewBinding
import com.bbb.koha.utils.TrackedConstraintLayout

class HistoryToolbarView: TrackedConstraintLayout {
    lateinit var binding: HistoryToolbarViewBinding
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.history_toolbar_view, this, true)
        binding.imageViewClear.setOnClickListener {
            binding.editTextSearch.setText("")
        }
        val sharedPreference = SharedPreference(context)


    }
}