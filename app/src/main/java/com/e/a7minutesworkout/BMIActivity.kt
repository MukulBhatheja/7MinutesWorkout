package com.e.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import kotlinx.android.synthetic.main.activity_exercise.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {


    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"
    //these two are constants
    var currentVisibleView : String = "METRIC_UNIT_VIEW"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle("CALCULATE BMI")
            //.title = ""
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
           onBackPressed()
        }

        btnCalculateUnits.setOnClickListener {

            if(currentVisibleView.equals(METRIC_UNITS_VIEW)){
                if(validMetricUnits()){
                    val heightValue : Float = etMetricUnitHeight.text.toString().toFloat()/100
                    // toString bec retrieving info and then for assigning toFloat and /100 for converting in metres
                    val weightValue : Float = etMetricUnitWeight.text.toString().toFloat()

                    val bmi = weightValue/(heightValue*heightValue)

                    displayBMIResult(bmi)

                }

                else{
                    Toast.makeText(this,"Please enter valid values",Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validUSUnits())
                {
                    val heightInchValue : Float = etUsUnitHeightInch.text.toString().toFloat()/100
                    val heightFeetValue : Float = etUsUnitHeightFeet.text.toString().toFloat()
                    val weightValue : Float = etUsUnitWeight.text.toString().toFloat()

                    val heightValue:Float = (heightFeetValue*12) + heightInchValue
                    val bmi = 703*(weightValue/(heightValue*heightValue))

                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this,"Please enter valid values",Toast.LENGTH_SHORT).show()
                }
            }
        }


        makeVisibleMetricUnitsView()
        // setting this one default
        rbMetricUnits.setOnCheckedChangeListener { buttonView, isChecked ->


            if(isChecked==true)
            {
                makeVisibleMetricUnitsView()

            }
            else{
                makeVisibleUSUnitsView()

            }
        }
        //****************COULD ALSO DO THIS WAY *******************
       /* rbMetricUnits.setOnClickListener {
            makeVisibleMetricUnitsView()
        }
        rbUsUnits.setOnClickListener {
            makeVisibleUSUnitsView()
        }
*/

    }

    private fun validMetricUnits():Boolean{
        var isValid = true

        if(etMetricUnitHeight.text.toString().isEmpty() || etMetricUnitWeight.text.toString().isEmpty() )
        {
            isValid = false
        }
        return isValid
    }

    private fun validUSUnits():Boolean{
        var isValid = true

        if(etUsUnitHeightInch.text.toString().isEmpty() ||etUsUnitHeightFeet.text.toString().isEmpty() || etUsUnitWeight.text.toString().isEmpty() )
        {
            isValid = false
        }
        return isValid
    }

    private fun displayBMIResult(bmi : Float)
    {
        val bmiLabel:String
        val bmiDescription:String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0)
        {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0)
        {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0)
        {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0 )
        {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0)
        {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0)
        {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }


        llDiplayBMIResult.visibility=View.VISIBLE
        /*
        tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE
*/
        //todo check this we made ll invisible but here using it individually

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView

    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden



        etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

        llDiplayBMIResult.visibility=View.GONE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is  Visible

        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()// height value is cleared if it is added.
        etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.

        llDiplayBMIResult.visibility=View.GONE
    }
}