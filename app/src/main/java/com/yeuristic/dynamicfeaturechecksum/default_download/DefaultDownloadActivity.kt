package com.yeuristic.dynamicfeaturechecksum.default_download

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.yeuristic.dynamicfeaturechecksum.R
import com.yeuristic.dynamicfeaturechecksum.split_manager.SplitInstallManagerFactory
import kotlinx.android.synthetic.main.activity_default_donwload.*

const val DATA_KEY = "DATA_KEY"
class DefaultDownloadActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    private val splitInstallManager: SplitInstallManager by lazy {
        SplitInstallManagerFactory.getSplitInstallManager(this)
    }
    lateinit var defaultDownloadData: DefaultDownloadData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_donwload)
        val data = intent.getParcelableExtra<DefaultDownloadData?>(DATA_KEY)
        if (data == null) {
            return
        } else {
            defaultDownloadData = data
        }
        textViewTitle.text = defaultDownloadData.title
        textViewDescription.text = defaultDownloadData.description

        startInstall(defaultDownloadData.modules)
    }

    private fun startInstall(modules: List<String>) {
        val installRequest = SplitInstallRequest.newBuilder().apply {
            modules.forEach { addModule(it) }
        }.build()

        val requestCode = 1
        var sessionId = 0

        val listener = object : SplitInstallStateUpdatedListener {
            override fun onStateUpdate(state: SplitInstallSessionState) {
                if (state.sessionId() == sessionId) {
                    when (state.status()) {
                        SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            splitInstallManager.startConfirmationDialogForResult(
                                state,
                                this@DefaultDownloadActivity,
                                requestCode
                            )
                        }
                        SplitInstallSessionStatus.UNKNOWN, SplitInstallSessionStatus.FAILED, SplitInstallSessionStatus.CANCELED -> {
                            splitInstallManager.unregisterListener(this)
                            finish()
                        }
                        SplitInstallSessionStatus.INSTALLED -> {
                            splitInstallManager.unregisterListener(this)
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                        SplitInstallSessionStatus.INSTALLING, SplitInstallSessionStatus.CANCELING -> {
                            progressBar.run {
                                isIndeterminate = true
                            }
                        }
                        SplitInstallSessionStatus.DOWNLOADED -> {
                           Toast.makeText(applicationContext, "Downloaded", Toast.LENGTH_SHORT).show()
                            progressBar.run {
                                progress = 100
                                isIndeterminate = false
                            }
                        }
                        SplitInstallSessionStatus.DOWNLOADING -> {
                            progressBar.run {
                                progress = (state.bytesDownloaded() * 100 / state.totalBytesToDownload()).toInt()
                                isIndeterminate = false
                            }
                        }
                    }
                }
            }
        }

        splitInstallManager.registerListener(listener)

        splitInstallManager.startInstall(installRequest)
            .addOnSuccessListener { id ->
                sessionId = id
            }.addOnFailureListener {
                splitInstallManager.unregisterListener(listener)
            }
    }
}
