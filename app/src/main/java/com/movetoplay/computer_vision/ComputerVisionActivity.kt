package com.movetoplay.computer_vision

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.movetoplay.R
import com.movetoplay.computer_vision.mlkit_utils.*
import com.movetoplay.model.Touch
import com.movetoplay.presentation.app_nav.AppNav
import com.movetoplay.presentation.child_main_nav.ContentRoute
import com.movetoplay.presentation.vm.profile_childe_vm.ProfileChildVM
import java.io.IOException
import java.util.ArrayList

class ComputerVisionActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private val viewModel: ProfileChildVM by viewModels()
    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var selectedModel = POSE_DETECTION
    private var btnStopCamera: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }

        preview = findViewById(R.id.preview_view)
        if (preview == null) {
            Log.d(TAG, "Preview is null") // Предварительный просмотр пуст
        }

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null") // графическое наложение равно null
        }

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        facingSwitch.setOnCheckedChangeListener(this)

        btnStopCamera = findViewById<Button>(R.id.btn_stop)
        createCameraSource(selectedModel)
        initListeners()
    }

    private fun initListeners() {
        btnStopCamera?.setOnClickListener {
            preview?.stop()
            finish()
        //    viewModel.sendTouch(Touch("", 0, 0))
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        Log.d(TAG, "Set facing") // Установить лицом
        if (cameraSource != null) {
            if (isChecked) {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
            } else {
                cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
            }
        }
        preview?.stop()
        startCameraSource()
    }

    private fun createCameraSource(model: String) {
        // If there's no existing cameraSource, create one.
        // Если нет существующего cameraSource, создайте его.
        if (cameraSource == null) {
            cameraSource = CameraSource(this, graphicOverlay)
        }
        try {
            val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
            Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions") // Использование детектора позы с параметрами
            val shouldShowInFrameLikelihood =
                PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
            val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
            val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
            val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)
            cameraSource!!.setMachineLearningFrameProcessor(
                PoseDetectorProcessor(
                    this,
                    poseDetectorOptions,
                    shouldShowInFrameLikelihood,
                    visualizeZ,
                    rescaleZ,
                    true,
                    /* isStreamMode = */ true
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Can not create image processor: $model", e) // Не могу создать процессор изображений
            Toast.makeText(
                applicationContext,
                "Can not create image processor: " + e.message, // Не могу создать процессор изображений
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    /**
     * Запускает или перезапускает источник камеры, если он существует. Если источник камеры еще не существует
     * (например, поскольку onResume был вызван до того, как был создан источник камеры), это будет вызвано
     * снова, когда источник камеры создан.
     */
    private fun startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null") // резюме: Предварительный просмотр является нулевым
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null") // резюме: graphOverlay имеет значение null
                }
                preview!!.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e) // Не удалось запустить источник камеры.
                cameraSource!!.release()
                cameraSource = null
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        createCameraSource(selectedModel)
        startCameraSource()
    }

    /** Stops the camera. */
    /** Останавливает камеру. */
    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (cameraSource != null) {
            cameraSource?.release()
        }
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission") // Разрешение получено:
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission") // Разрешение НЕ предоставлено:
        return false
    }

    companion object {
        private const val PERMISSION_REQUESTS = 1

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        private const val OBJECT_DETECTION = "Object Detection"
        private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
        private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
        private const val FACE_DETECTION = "Face Detection"
        private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
        private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese"
        private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
        private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese"
        private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean"
        private const val BARCODE_SCANNING = "Barcode Scanning"
        private const val IMAGE_LABELING = "Image Labeling"
        private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
        private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
        private const val POSE_DETECTION = "Pose Detection"
        private const val SELFIE_SEGMENTATION = "Selfie Segmentation"

        private const val TAG = "LivePreviewActivity"
    }
}
