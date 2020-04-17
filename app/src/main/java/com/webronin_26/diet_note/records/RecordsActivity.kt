package com.webronin_26.diet_note.records

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.webronin_26.diet_note.DietNoteApplication
import com.webronin_26.diet_note.R
import com.webronin_26.diet_note.databinding.RecordsActBinding
import com.webronin_26.diet_note.recorddetail.RecordDetailActivity
import com.webronin_26.diet_note.util.DateFormater
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.records_act.*
import kotlinx.coroutines.*

class RecordsActivity: AppCompatActivity() {

    private lateinit var viewModel: RecordsViewModel

    private lateinit var viewDataBinding: RecordsActBinding

    private lateinit var pagedListAdapter: RecordsAdapter

    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.exitTransition = Slide(Gravity.START)

        viewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        getCurrentDate()
        viewModel.setDate(date)
        viewModel.setRespository((application as DietNoteApplication).recordsRepository)
        lifecycle.addObserver(viewModel)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.records_act)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewmodel = viewModel

        setupButton()
        setupPagedListAdapter()
    }

    override fun onResume() {
        super.onResume()

        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Loading ...")
            .setCancelable(false)
            .build()

        GlobalScope.launch(Dispatchers.Main) {
            dialog.show()
            countSecond()
            dialog.dismiss()
        }
    }

    private suspend fun countSecond() = withContext(Dispatchers.IO) { Thread.sleep(1000) }

    private fun setupButton() {

        records_back_button.setOnClickListener { finish() }

        records_action_button.setOnClickListener {
            val intent = Intent(this, RecordDetailActivity::class.java)
            intent.putExtra("date", date)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun setupPagedListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            pagedListAdapter = RecordsAdapter(viewModel)
            viewDataBinding.recordsRecyclerView.adapter = pagedListAdapter
        }
    }

    private fun getCurrentDate() {
        date = intent.getStringExtra("date")
        if(date.isEmpty()){ date = DateFormater().getCurrentDate() }
    }
}