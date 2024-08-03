package com.example.travelphotoapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout.DispatchChangeEvent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.travelphotoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentImageIndex = 0

    private var count = 0

    private var titles = arrayOf("Tibet", "Spain", "The Alps", "Ottawa", "Cuba")

    private val imageViews: List<ImageView> by lazy {

        listOf(
            binding.tibetValley,
            binding.spainChurch,
            binding.alpsRiver,
            binding.ottawaCity,
            binding.cubaStreet
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        updateVisibility()

        binding.next.setOnClickListener {

            lifecycleScope.launch {

                transitionToNext()

            }
        }

        binding.prev.setOnClickListener {

            lifecycleScope.launch {

                transitionToPrevious()

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private suspend fun transitionToNext() {

        withContext(Dispatchers.Main) {

            imageViews[currentImageIndex].alpha = 0f

            currentImageIndex = (currentImageIndex + 1) % imageViews.size

            updateVisibility()

            count = (count + 1) % titles.size

            updateTitle()

        }
    }

    private suspend fun transitionToPrevious() {

        withContext(Dispatchers.Main) {

            imageViews[currentImageIndex].alpha = 0f

            currentImageIndex = (currentImageIndex - 1 + imageViews.size) % imageViews.size

            updateVisibility()

            count = (count - 1 + titles.size) % titles.size

            updateTitle()

        }
    }

    private fun updateVisibility() {

        imageViews.forEachIndexed {

            index, imageView -> imageView.alpha = if (index == currentImageIndex) {

                1f

            } else {

                0f

            }

        }

    }

    private fun updateTitle() {

        binding.placeTitle.text = titles[count]
    }
}