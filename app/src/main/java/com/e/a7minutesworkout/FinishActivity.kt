package com.e.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_finish.*
import java.text.SimpleDateFormat
import java.util.*


class FinishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        setSupportActionBar(toolbar_finish_activity)
        val actionBar= supportActionBar
        //making object of action bar
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed() // when back button is pressed or back using gestures
            // when we press back button we will not go to exercises but to the main screen
            // because we used finish() in ExerciseActivity at the end so it is not in stack now
        }

        btnFinish.setOnClickListener{
            finish()
            //will finish the current activity
        }

        addDateToDatabase()

    }
    /**
     * Function is used to insert the current system date in the sqlite database.
     */
    private fun addDateToDatabase() {


        val calendar = Calendar.getInstance() // Calendar 's Instance
        val dateTime = calendar.time // Current Date and Time of the system.
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        // Date Formatter,MMM is Jan/feb/etc
        val date = (sdf.format(dateTime)).toString()
        // dateTime is converted in the given format we selected ie in sdf.


        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date) // Add date function is called.
        Log.i("Date : ", "Added...")
        // Printed in log which is printed if the complete execution is done.
        /**
         * Here we have taken an instance of Date Formatter as it will format our
         * selected date in the format which we pass it as an parameter and Locale.
         * Here I have passed the format as dd MMM yyyy HH:mm:ss.
         *
         * The Locale : Gets the current value of the default locale for this instance
         * of the Java Virtual Machine.Locale is the place US /UK type
         */

    }
}