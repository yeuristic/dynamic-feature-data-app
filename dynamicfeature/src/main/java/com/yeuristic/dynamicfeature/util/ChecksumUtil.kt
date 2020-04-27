package com.yeuristic.dynamicfeature.util

import android.os.Build
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object ChecksumUtil {
    @JvmStatic
    fun findSoInputStream(): InputStream? {
        val supportedAbi = Build.SUPPORTED_ABIS[0]
        val path = "/lib/$supportedAbi/libtest.so"
        return ChecksumUtil::class.java.getResourceAsStream(path)
    }

    fun checksum(inputStream: InputStream?): String? {
        var str: String? = null
        try {
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            if (inputStream != null) {
                val bArr = ByteArray(8192)
                while (true) {
                    try {
                        val read: Int = inputStream.read(bArr)
                        if (read <= 0) {
                            break
                        }
                        instance.update(bArr, 0, read)
                    } catch (e: IOException) { //                        throw a(ContractErrorBrizzi.ERROR_CODE_CARD_FAILED);
                    } catch (th: Throwable) {
                        try {
                            inputStream.close()
                            throw th
                        } catch (e2: IOException) { //                            throw a(ContractErrorBrizzi.ERROR_CODE_CARD_FAILED);
                        }
                    }
                }
                str = String.format(
                    "%32s",
                    *arrayOf<Any>(BigInteger(1, instance.digest()).toString(16))
                ).replace(' ', '0')
                try {
                    inputStream.close()
                } catch (e3: IOException) { //                    throw a(ContractErrorBrizzi.ERROR_CODE_CARD_FAILED);
                }
            }
        } catch (e4: NoSuchAlgorithmException) {
        }
        return str
    }
}