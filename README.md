Usage:
`./install app:bundleDebug`
this will use fake install manager\
If you can't use install script you can do these steps manually:
1. build aab: `./gradlew app:bundleDebug`
1. create device specific apks: `java -jar bundletool.jar build-apks --bundle="<aabfile.aab>" --output="<output.apks>" --connected-device`
1. install apks: `java -jar bundletool.jar install-apks --apks="<output.apks>"`
1. create complete apks: `java -jar bundletool.jar build-apks --bundle="<aabfile.aab>" --output=<complete-output.apks>"
1. unzip apks: `unzip "<complete-output.apks>" -d "tempDir/apks"`
1. these split apk will be pushed to `$apksDir= /sdcard/Android/data/$package/files/splits`
1. clean target directory: `adb shell rm -rf "$apksDir"`
1. push apks to target directory: `adb push "tempDir/apks/splits" "$apksDir"`
1. launch app
