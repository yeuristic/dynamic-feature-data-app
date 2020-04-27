package com.yeuristic.dynamicfeaturechecksum.split_manager

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManagerFactory
import com.yeuristic.dynamicfeaturechecksum.BuildConfig
import java.io.File

object SplitInstallManagerFactory {
    fun getSplitInstallManager(context: Context): SplitInstallManager =
        if (BuildConfig.DEBUG) {
            val modulesDir = File(context.getExternalFilesDir(null), "splits")
            FakeSplitInstallManagerFactory.create(context, modulesDir)
        } else {
            SplitInstallManagerFactory.create(context)
        }
}