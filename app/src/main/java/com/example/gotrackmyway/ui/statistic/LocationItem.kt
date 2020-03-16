package com.example.gotrackmyway.ui.statistic

import android.util.Log
import com.example.gotrackmyway.R
import com.example.gotrackmyway.data.db.ExtLatLngEntry
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.location_row.*

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class LocationItem(
    val locEntry: ExtLatLngEntry
) : Item() {

    private val LOG_TAG = this.javaClass.simpleName

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            updateState()
        }
    }

    override fun getLayout() = R.layout.location_row

    companion object {

        val dtPattern = "'Date: 'yyyy-MM-dd'  Time: 'HH:mm:ss"
        var simpleDateFormat = SimpleDateFormat(dtPattern)

        private fun dateTimeConverter(dt: Long) : String {
            return simpleDateFormat.format(Date(dt))
        }

    }

    private fun ViewHolder.updateState() {
//        Log.d(LOG_TAG,"ViewHolder.updateState for: ${locEntry.toString()}")
        statisticDlg_row_TVLocationLabel.text = "${dateTimeConverter(locEntry.evtTime)}"
        statisticDlg_row_TVLocationLatitudeVal.text = "${locEntry.latitude}"
        statisticDlg_row_TVLocationLongitudeVal.text = "${locEntry.longitude}"
        statisticDlg_row_TVWayIdVal.text = "${locEntry.way_id}"
    }

}