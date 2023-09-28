package com.desafiolatam.desafiomarsrover

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.desafiolatam.desafiomarsrover.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.coroutines.CoroutineContext

class MainActivity() : AppCompatActivity(), CoroutineScope {

    private lateinit var binding: ActivityMainBinding

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val urlImageMarsRover: Array<String> = arrayOf(
        "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044630280503588E02_DXXX.jpg",
        "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631220503682E01_DXXX.jpg",
        "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631290503689E01_DXXX.jpg",
        "https://mars.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044630010503561E01_DXXX.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonThreadUi.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Mensaje en Thread UI")
            builder.setMessage("Alerta para mostrar el funcionamiento de corrutinas en background")
            builder.setCancelable(true)
            builder.setPositiveButton("OK") {
                dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }

    }

    override fun onStart() {
        super.onStart()
        downloadImages()
    }

    private fun downloadImages(){
        launch {
            binding.progressBarOne.visibility = View.VISIBLE

            for(i in urlImageMarsRover.indices){
                val bitmap: Bitmap? = withContext(Dispatchers.IO) {
                    downloadImagesNasaRover(urlImageMarsRover[i])
                }

                if(bitmap != null){
                    when(i) {
                        0 -> {
                            binding.imageViewOne.setImageBitmap(bitmap)
                            binding.progressBarOne.visibility = View.GONE
                            binding.progressBarTwo.visibility = View.VISIBLE
                        }
                        1 -> {
                            binding.imageViewTwo.setImageBitmap(bitmap)
                            binding.progressBarTwo.visibility = View.GONE
                            binding.progressBarThree.visibility = View.VISIBLE
                        }
                        2 -> {
                            binding.imageViewThree.setImageBitmap(bitmap)
                            binding.progressBarThree.visibility = View.GONE
                            binding.progressBarFour.visibility = View.VISIBLE
                        }
                        3 -> {
                            binding.imageViewFour.setImageBitmap(bitmap)
                            binding.progressBarFour.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun downloadImagesNasaRover(url:String): Bitmap?{
        val bitmap: Bitmap?
        try {
            val inputStream = URL(url).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            return bitmap
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }
}