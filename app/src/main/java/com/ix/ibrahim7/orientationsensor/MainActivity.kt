package com.ix.ibrahim7.orientationsensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , SensorEventListener {


    lateinit var sensorManager: SensorManager
    lateinit var sensorAcc: Sensor
    lateinit var sensorAmf: Sensor

     var rotationMatrix = FloatArray(9)
     var arrayAcc = FloatArray(3)
     var arrayMf = FloatArray(3)
     var value = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null && sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
            sensorAmf = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }else{
            Toast.makeText(this,"not found", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
            if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
                arrayAcc = event.values
            }else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
                arrayMf = event.values
            }

        SensorManager.getRotationMatrix(rotationMatrix,null,arrayAcc,arrayMf)
        SensorManager.getOrientation(rotationMatrix,value)

        Log.e(">>>>>>",value[0].toString())

        val final = Math.toDegrees(value[0].toDouble()).toInt()

        if (final < 0) {
            textView.text= (final.toFloat()+360).toString()
            tvImage.rotation = final.toFloat()+360
        }else{
            textView.text= final.toFloat().toString()
            tvImage.rotation = final.toFloat()
        }
    }


    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this,sensorAcc,SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this,sensorAmf,SensorManager.SENSOR_DELAY_NORMAL)
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


}