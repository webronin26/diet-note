package com.webronin_26.diet_note.main

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.webronin_26.diet_note.DietNoteApplication
import com.webronin_26.diet_note.R
import com.webronin_26.diet_note.databinding.MainActBinding
import com.webronin_26.diet_note.records.RecordsActivity
import com.webronin_26.diet_note.util.DateFormater
import kotlinx.android.synthetic.main.main_act.*
import rebus.bottomdialog.BottomDialog

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var viewDataBinding: MainActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setRepository((application as DietNoteApplication).recordsRepository)
        lifecycle.addObserver(viewModel)

        viewDataBinding = DataBindingUtil.setContentView(this,R.layout.main_act)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewmodel = viewModel

        setupMaterDialog()
        setupDailyRecordLayout()
        setupCircularProgress()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) { showAnimations() }
    }

    override fun onStop() {
        super.onStop()
        stopAnimations()
    }

    private fun showAnimations() {
        main_daily_record_animation_view.show()
        main_history_animation_view.show()
    }

    private fun stopAnimations() {
        main_daily_record_animation_view.hide()
        main_history_animation_view.hide()
    }

    private fun setupDailyRecordLayout() {
        main_daily_record_linear_layout.setOnClickListener {
            val intent = Intent(this@MainActivity , RecordsActivity::class.java)
            intent.putExtra("date",DateFormater().getCurrentDate())
            startActivity(intent , ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
        }
    }

    private fun setupMaterDialog() {
        main_history_linear_layout.setOnClickListener {
            MaterialDialog(this).show {
                datePicker { _, datetime ->
                    val dateString = DateFormater().formate(datetime.time)
                    val intent = Intent(this@MainActivity , RecordsActivity::class.java)
                    intent.putExtra("date",dateString)
                    startActivity(intent , ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle())
                }
            }
        }
    }

    private fun setupCircularProgress() {

        main_title_linear_layout.setOnClickListener {

            val dialog = BottomDialog(this)

            dialog.apply {
                title("設定每日總熱量 ：")
                cancelable(true)
                canceledOnTouchOutside(true)
                inflateMenu(R.menu.main_circular_progress_max_value)

                setOnItemSelectedListener( object : BottomDialog.OnItemSelectedListener{
                    override fun onItemSelected(id: Int): Boolean {
                        when(id) {
                            R.id.circular_value_4000 -> viewModel.setMaxCalories(4000)
                            R.id.circular_value_5000 -> viewModel.setMaxCalories(5000)
                            R.id.circular_value_6000 -> viewModel.setMaxCalories(6000)
                            R.id.circular_value_7000 -> viewModel.setMaxCalories(7000)
                            R.id.circular_value_8000 -> viewModel.setMaxCalories(8000)
                            R.id.circular_value_9000 -> viewModel.setMaxCalories(9000)
                            R.id.circular_value_10000 -> viewModel.setMaxCalories(10000)
                            else -> return false
                        }
                        return true
                    }
                })
                show()
            }
        }
    }
}
