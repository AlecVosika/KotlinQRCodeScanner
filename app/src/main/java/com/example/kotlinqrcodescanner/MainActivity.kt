package com.example.kotlinqrcodescanner

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kotlinqrcodescanner.databinding.ActivityMainBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : AppCompatActivity() {
    // Permission launcher for camera access
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showCamera()
            } else {
                // Explain why you need permission
                // This could be implemented further to educate users on why camera permission is required.
            }
        }

    // QR code scan launcher
    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }
        }

    private lateinit var binding: ActivityMainBinding

    // Set the scanned result to the UI
    private fun setResult(string: String) {
        // Set the scanned result string to the text view
        binding.textResult.text = string
    }

    // Launches the camera for QR code scanning
    private fun showCamera() {
        // Configure options for the barcode scanner
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR Code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        // Launch the barcode scanner with configured options
        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize view binding and UI
        initBinding()
        initViews()
    }

    // Initialize views and event listeners
    private fun initViews() {
        // Set click listener for the FAB (Floating Action Button)
        binding.fab.setOnClickListener {
            // Check and request camera permission before showing camera
            checkPermissionCamera(this)
        }
    }

    // Checks if camera permission is granted, requests permission if necessary
    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // If camera permission is granted, show the camera
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            // Explain why camera permission is required
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
        } else {
            // Request camera permission if it has not been granted
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    // Initialize binding using ViewBinding
    private fun initBinding() {
        // Inflate the layout and initialize the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the root view of the activity to the inflated layout
        setContentView(binding.root)
    }
}