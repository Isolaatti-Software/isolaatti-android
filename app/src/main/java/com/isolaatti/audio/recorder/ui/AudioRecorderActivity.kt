package com.isolaatti.audio.recorder.ui

import android.Manifest
import android.content.Intent
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.isolaatti.databinding.ActivityAudioRecorderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.UUID

@AndroidEntryPoint
class AudioRecorderActivity : AppCompatActivity() {

    companion object {
        const val LOG_TAG = "AudioRecorderActivity"
    }

    private lateinit var binding: ActivityAudioRecorderBinding

    private var audioRecorder: MediaRecorder? = null

    private val audioUID = UUID.randomUUID()
    private lateinit var outputFile: String

    private val requestAudioPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it) {
            startRecording()
        } else {
            showPermissionRationale()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioRecorderBinding.inflate(layoutInflater)

        setContentView(binding.root)
        File("${filesDir.absolutePath}/audios/").let {
            if(!it.isDirectory) {
                it.mkdir()
            }
        }
        outputFile =  "${filesDir.absolutePath}/audios/${audioUID}.3gp"

        setupListeners()
    }

    private fun checkRecordAudioPermission(): Boolean {
        return when {
            PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PERMISSION_GRANTED -> true
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) -> {
                showPermissionRationale()
                false
            }

            else -> {
                requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                false
            }
        }
    }

    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Record audio permission")
            .setMessage("We need permission to access your microphone so that you can record your audio. Go to settings.")
            .setPositiveButton("Go to settings"){_, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("No, thanks", null)
            .show()
    }


    private fun setupListeners() {
        binding.recordButton.setOnClickListener {
            if(checkRecordAudioPermission()) {
                Log.d(LOG_TAG, "Starts recording")
                startRecording()
            } else {
                Log.d(LOG_TAG, "Failed to start recording: mic permission not granted")
            }
        }
        binding.stopRecording.setOnClickListener {
            stopRecording()
        }

        binding.cancelButton.setOnClickListener {
            discardRecording()
        }

        binding.pauseRecording.setOnClickListener {
            pauseRecording()
        }

        binding.acceptButton.setOnClickListener {
            acceptRecording()
        }

        binding.playPauseButton.setOnClickListener {

        }
    }

    // region timer
    private var timer: Job? = null
    private var timerValue = 0

    private fun setDisplayTime(seconds: Int, showTotalTime: Boolean) {
        val stringBuilder = StringBuilder()

        stringBuilder.append(seconds)

        binding.time.text = stringBuilder.toString()
    }
    private fun startTimerRecorder() {

        timer = CoroutineScope(Dispatchers.Main).launch {
            setDisplayTime(timerValue, false)
            delay(1000)
            timerValue++
            startTimerRecorder()
        }
    }

    private fun startTimerPlayer() {
        timer?.cancel()
        timer = CoroutineScope(Dispatchers.Main).launch {
            setDisplayTime(timerValue, true)
            delay(1000)
            timerValue++
        }
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    // end region

    // region record functions
    private fun startRecording() {
        audioRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
                start()
                timerValue = 0
                startTimerRecorder()
                binding.viewAnimator.displayedChild = 1
            } catch(e: IOException) {
                Log.e(LOG_TAG, "prepare() failed\n${e.message}")
            }
        }
    }

    private fun stopRecording() {
        audioRecorder?.apply {
            stop()
            release()
        }
        stopTimer()
        audioRecorder = null

        // shows third state: audio recorded
        binding.viewAnimator.displayedChild = 2
    }

    private fun pauseRecording() {
        audioRecorder?.pause()
        stopTimer()
    }

    private fun discardRecording(){
        File(outputFile).apply {
            try {
                delete()
            } catch(e: SecurityException) {
                Log.e(LOG_TAG, "Could not delete file\n${e.message}")
            }
        }
        binding.viewAnimator.displayedChild = 0
    }

    // end region

    private fun acceptRecording() {

    }

    private fun playPauseRecording() {

    }


}