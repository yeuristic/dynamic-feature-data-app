package com.yeuristic.dynamicfeaturechecksum

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitcompat.SplitCompat
import com.yeuristic.dynamicfeaturechecksum.default_download.DATA_KEY
import com.yeuristic.dynamicfeaturechecksum.default_download.DefaultDownloadActivity
import com.yeuristic.dynamicfeaturechecksum.default_download.DefaultDownloadData
import com.yeuristic.dynamicfeaturechecksum.split_manager.SplitInstallManagerFactory
import kotlinx.android.synthetic.main.activity_main.*

const val DYNAMIC_FEATURE_NAME = "dynamicfeature"
const val REQUEST_CODE = 12
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val splitInstallManager by lazy {
        SplitInstallManagerFactory.getSplitInstallManager(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonNavigate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            buttonNavigate -> {
                if (splitInstallManager.installedModules.contains(DYNAMIC_FEATURE_NAME)) {
                    navigateToDynamicFeature()
                } else {
                    navigateToDownloadActivity(DYNAMIC_FEATURE_NAME)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    navigateToDynamicFeature()
                }
            }
        }
    }

    private fun navigateToDownloadActivity(dynamicFeatureName: String) {
        startActivityForResult(
            Intent(this, DefaultDownloadActivity::class.java).apply {
                putExtra(DATA_KEY, DefaultDownloadData(
                    title = "Title",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In ut vestibulum nunc, auctor accumsan massa. Aenean et mi nec augue molestie dapibus.",
                    modules = listOf(dynamicFeatureName)
                    ))
            },
            REQUEST_CODE
        )
    }

    private fun navigateToDynamicFeature() {
        Intent().setClassName(
                "com.yeuristic.dynamicfeaturechecksum",
                "com.yeuristic.dynamicfeature.DynamicMainActivity"
        ).let {
            startActivity(it)
        }
    }
}
