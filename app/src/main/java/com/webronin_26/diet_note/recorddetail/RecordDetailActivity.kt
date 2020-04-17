package com.webronin_26.diet_note.recorddetail

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.webronin_26.diet_note.DietNoteApplication
import com.webronin_26.diet_note.R
import com.webronin_26.diet_note.databinding.DetailActBinding
import com.webronin_26.diet_note.util.DateFormater
import kotlinx.android.synthetic.main.detail_act.*

class RecordDetailActivity: AppCompatActivity() {

    private lateinit var viewModel: RecordDetailViewModel

    private lateinit var viewDataBinding: DetailActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.enterTransition = Slide(Gravity.END)

        viewModel = ViewModelProvider(this).get(RecordDetailViewModel::class.java)
        viewModel.setRespository((application as DietNoteApplication).recordsRepository)
        lifecycle.addObserver(viewModel)
        viewModel.setDate(getCurrentDate())

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.detail_act)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewmodel = viewModel

        setupEditText()
        setupSeekBarMaxValue(0f)
        setupButton()
    }

    private fun getCurrentDate(): String {
        val date = intent.getStringExtra("date")
        if (date.isNullOrEmpty()) {
            return DateFormater().getCurrentDate()
        }
        return date
    }

    private fun setupEditText() {

        detail_calories_edit_text.setRawInputType(Configuration.KEYBOARD_QWERTY)

        detail_calories_edit_text.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun afterTextChanged(p0: Editable?) {

                detail_seek_bar_carbohydrate.configBuilder.progress(0f).build()
                detail_seek_bar_protein.configBuilder.progress(0f).build()
                detail_seek_bar_fat.configBuilder.progress(0f).build()

                if (!p0.isNullOrEmpty()) {
                    try {
                        val value = p0.toString().toFloat().toInt()
                        if (value < 0) {
                            showAlertToast("請輸入大於 0 的數字")
                            setupSeekBarMaxValue(0f)
                        } else if (value > 100000) {
                            showAlertToast("熱量請輸入小於 10 萬")
                            setupSeekBarMaxValue(0f)
                        } else {
                            setupSeekBarMaxValue(value.toFloat())
                        }
                    } catch (e: Exception) {
                        setupSeekBarMaxValue(0f)
                        showAlertToast("熱量請輸入「數字」類型")
                    }
                }
            }
        })
    }

    private fun setupSeekBarMaxValue(f: Float) {
        detail_seek_bar_carbohydrate.configBuilder.max(f).build()
        detail_seek_bar_protein.configBuilder.max(f).build()
        detail_seek_bar_fat.configBuilder.max(f).build()
    }

    private fun showAlertToast( str: String ) { Toast.makeText(this, str, Toast.LENGTH_SHORT).show() }

    private fun setupButton() {
        detail_cancel_button.setOnClickListener { finish()}

        detail_check_button.setOnClickListener {

            val alertString = viewModel.checkCurrentInput(
                name = detail_name_edit_text.text,
                calories = detail_calories_edit_text.text,
                carbohydrate = detail_seek_bar_carbohydrate.progress,
                protein = detail_seek_bar_protein.progress,
                fat = detail_seek_bar_fat.progress
            )

            if (alertString != null) {
                showAlertToast(alertString)
            } else {
                viewModel.saveRecord()
                viewModel.saveTotalRecord()

                showAlertToast("登陸成功！請繼續輸入")
                clearUI()
            }
        }
    }

    private fun clearUI() {

        setupSeekBarMaxValue(0f)

        detail_name_edit_text.setText("")
        detail_calories_edit_text.setText("")

        detail_seek_bar_carbohydrate.configBuilder.progress(0f).build()
        detail_seek_bar_protein.configBuilder.progress(0f).build()
        detail_seek_bar_fat.configBuilder.progress(0f).build()
    }
}
