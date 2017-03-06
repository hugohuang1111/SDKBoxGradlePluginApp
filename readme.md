
## Step1

* 进入到工程根目录下
* 运行 `./gradlew uploadArchives` , 将 sdkboxgradleplugin 工程打包，在根目录 `repo` 下生成 maven 插件本地仓库
* `Android Studio` 中调试运行
* 点击 `Load Insterstitial`, `Show Insterstitial`
* 查看 `Logcat`, 搜索 `NetBridge`, 应该能看到 `HttpURLConnect_getResponseCode` 之类的 log 信息
这是 `InMobi` SDK 联网时调到 `NetBridge` 类中，`NetBridge` 输出的 log
