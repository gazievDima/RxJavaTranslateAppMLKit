package com.gaziev.vcttranslate.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

class CameraPreview(
    private val lifecycleOwner: LifecycleOwner,
    private val activity: Activity,
    private val previewView: PreviewView
) {

    val labeler by lazy { ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS) }
    private val imageCapture: ImageCapture by lazy {
        ImageCapture.Builder().build()
    }
    private val cameraSelector: CameraSelector by lazy {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }
    private val preview: Preview by lazy {
        Preview.Builder().build()
    }

    /**
     * need call method, for next time used method takePhoto()
     * this method adjusts the work with the camera
     */
    fun cameraOn() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener(Runnable {

            val cameraProvider = cameraProviderFuture.get()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                imageCapture,
                preview
            )

        }, ContextCompat.getMainExecutor(activity))
    }

    /**
     * method return Single object with string recognized
     */
    fun cameraTakePhoto(): Single<String> {
        return Single.create { emmiter ->

            imageCapture.takePicture(ContextCompat.getMainExecutor(activity),
                object : ImageCapture.OnImageCapturedCallback() {

                    @SuppressLint("UnsafeOptInUsageError")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        super.onCaptureSuccess(image)

                        val obj = ImageAnalysis.Analyzer {
                            it.image?.let { img ->
                                val picture = InputImage.fromMediaImage(
                                    img,
                                    it.imageInfo.rotationDegrees
                                )
                                it.close()

                                labeler.process(picture)
                                    .addOnSuccessListener { labels ->
                                        var confidenceMax = 0f
                                        var textRecognize: String? = null

                                        for (label in labels) {

                                            if (label.confidence > confidenceMax) {
                                                confidenceMax = label.confidence
                                                textRecognize = label.text
                                            }

                                            val text = label.text
                                            val index = textRecognize
                                            Log.e(
                                                ContentValues.TAG,
                                                "text: $text, confidence: $confidenceMax, index: $index"
                                            )
                                        }

                                        if (textRecognize != null) emmiter.onSuccess(textRecognize)
                                        else emmiter.onError(Throwable())

                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(ContentValues.TAG, "Failed recognized label...")
                                    }
                            }
                        }
                        obj.analyze(image)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        super.onError(exception)
                        Log.e(ContentValues.TAG, "EXCEPTION: $exception")
                    }

                })
        }
    }

}