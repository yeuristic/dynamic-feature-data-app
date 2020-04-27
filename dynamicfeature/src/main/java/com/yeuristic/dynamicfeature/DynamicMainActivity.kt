package com.yeuristic.dynamicfeature

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.splitcompat.SplitCompat
import com.yeuristic.dynamicfeature.util.ChecksumUtil
import kotlinx.android.synthetic.main.activity_dynamic_main.*

class DynamicMainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_main)
        val inputStream = ChecksumUtil.findSoInputStream()
        Log.d("Input Stream", "$inputStream")
        val checksum = ChecksumUtil.checksum(inputStream)
        if (checksum == null) {
            Toast.makeText(this, "Checksum null", Toast.LENGTH_SHORT).show()
        }
        textChecksum.text = checksum
    }
}
