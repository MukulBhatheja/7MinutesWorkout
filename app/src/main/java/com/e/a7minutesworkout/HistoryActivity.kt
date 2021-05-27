package com.e.a7minutesworkout

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirm.*


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


        setSupportActionBar(toolbar_history_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle("HISTORY")

        }
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()

    }

    private fun getAllCompletedDates() {

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)

        val allCompletedDatesList = dbHandler.getAllCompletedDatesList() // List of history table data

       if(allCompletedDatesList.size>0)
       {
           tvHistory.visibility= View.VISIBLE
           rvHistory.visibility= View.VISIBLE

           tvNoDataAvailable.visibility = View.GONE

           rvHistory.layoutManager = LinearLayoutManager(this)
           val historyAdapter = HistoryAdapter(this , allCompletedDatesList )
            rvHistory.adapter = historyAdapter

       }else{
           tvHistory.visibility= View.GONE
           rvHistory.visibility= View.GONE

           tvNoDataAvailable.visibility = View.VISIBLE
       }

    }

    fun delDate(string: String){

        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.deleteDate(string)
    }

    /*fun customDialogForDeleteButton(tdate:String){

        val customDialog = Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_del_confirm)

        customDialog.tvYes.setOnClickListener {

            val dbHandler = SqliteOpenHelper(this, null)
            dbHandler.deleteDate(tdate)

            getAllCompletedDates()
            customDialog.dismiss()
        }

        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }*/

}