package com.webronin_26.diet_note.main

import androidx.databinding.BindingAdapter
import com.app.progresviews.ProgressLine
import com.vaibhavlakhera.circularprogressview.CircularProgressView

@BindingAdapter("app:totalValue")
fun setItems(circularProgressView: CircularProgressView, i: Int) {
    circularProgressView.setTotal(i)
}

@BindingAdapter("app:progressValue")
fun converterCircularProgressView(circularProgressView: CircularProgressView, i: Int) {
    circularProgressView.setProgress(i, false)
}

@BindingAdapter("app:value")
fun converterProgressLineValue(progressLine: ProgressLine, i: Int) {
    progressLine.setmValueText(i)
}

@BindingAdapter("app:valuePercentage")
fun converterProgressLineValuePercentage(progressLine: ProgressLine, i: Int) {
    progressLine.setmPercentage(i)
}