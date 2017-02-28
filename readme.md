
## Step1

* 进入到工程根目录下
* 运行 `./gradlew uploadArchives` , 将 sdkboxgradleplugin 工程打包，在根目录 `repo` 下生成 maven 插件本地仓库
* `Android Studio` 中调试运行, 查看 `Logcat`, 能看到如下log

```
D/SDKBox: this is from asm
```

