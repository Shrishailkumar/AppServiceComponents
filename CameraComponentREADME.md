# Camera Capture Component

## 1.	Introduction :
- This component is developed to use android camera feature to capture the photo, video or both.
- This supports both primary (main camera sensor on back) and secondary camera (selfie camera).
- This allows user to take a photo or video from camera and returns the uri (path of the file from storage) so It can be further used in app as needed.

## 2.	Technology Details :
- Fo adding android's camera support, new androidX library 'CameraX' is used in this component.
- CameraX library gives some out of the box features like, image configuration, image preview, rotation use case and saving high quality images and support with jetpack component and lifecycle binding.

- This Library also gives compatibility back to Android API level 21 (5.0 - Lollipop).

    1. UI Component – Constraint Layout, Camera Preview view
    2. API Details – CameraX, Architecture components, Android API 30(for development SDK)

## 3. Gradle configuration for this library,
- minSdk :
  ``` minSdk : 23 ```

- targetSdk :
  ``` targetSdk : 30 ```

- compileOption and Java version support :
```sh
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    } 
```
- Dependencies used in component :
```
dependencies {

    // Android core libraries and KTX libraries
    implementation 'androidx.core:core-ktx:1.3.2'
    implementation "androidx.fragment:fragment-ktx:1.3.6"
    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.1'

    // CameraX
    def camerax_version = "1.0.1"
    implementation "androidx.camera:camera-camera2:$camerax_version"
    implementation "androidx.camera:camera-lifecycle:$camerax_version"
    implementation "androidx.camera:camera-view:1.0.0-alpha27"

    // Glide for image preview
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'

    // ExoPlayer for video preview
    implementation 'com.google.android.exoplayer:exoplayer:2.16.0'
    implementation 'com.google.android.exoplayer:exoplayer-core:2.16.0'
    implementation 'com.google.android.exoplayer:exoplayer-dash:2.16.0'
    implementation 'com.google.android.exoplayer:exoplayer-ui:2.16.0'
}
```
- Permission needed - AndroidManifest.xml
```
<uses-feature android:name="android.hardware.camera.any" />

<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<!-- 'tools:ignore="ScopedStorage"' - To disabled scoped storage this will be modified as per the main applications permission if added the same.-->
<uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
```

## 4. Installation
To use this component in your app you can import this component as module from android studio or you can follow manual steps as below,

1. Add 'CameraCaptureComponent' into your project at root level

2. in app module's ```build.gradle``` add this dependency,
   ```implementation project(path: ':cameracapturecomponent')```

3. In ```settings.gradle``` add this,
   ```include ':cameracapturecomponent'```
> Note: If you want to use module directly from your workspace without coping whole module in your project than add below import in ```settings.gradle```,
```project(":CameraCaptureComponent").projectDir = new File("<path to camera component>/CameraCaptureComponent")```

## 5.	Usage :
Camera component used some basic features of CameraX library and provides an easy to use wrapper  so that camera feature development can done easily and fast.

To use this component there are some methods and callbacks exposed as below,

### ```initCameraPreview()``` :
- This method is to initialize a camera component with providing needed configurations options.
- This method needs to be the *FIRST CALL* to the camera component in order to use other features.

```
fun initCameraPreview(
        cameraMode: CameraModeEnum = CameraModeEnum.PHOTO,
        shouldShowPreview: Boolean = false,
        imageCaptureCallback: OnImageCaptureCallback? = null,
        videoCaptureCallback: OnVideoCaptureCallback? = null,
        videoLimit: Long
    ): Fragment {}
```

- This method required 5 parameters,
    1. ```cameraMode``` : Camera mode can be set from the CameraModeEnum with below options,
    ```
        enum class CameraModeEnum {
            PHOTO, // To initalize camera to capture image only
            VIDEO, // To initalize camera to capture video only
            BOTH; // To initalize camera to capture image and video both
        }
    ```

    2. ```shouldShowPreview``` : This param accepts a boolean value, to enable preview that can be shown after photo or video is captured.

    3. ```imageCaptureCallback``` * : This params accepts nullable instance of ```OnImageCaptureCallback``` interface to receive the callback of capturing image.
        ```
        interface OnImageCaptureCallback {
            fun onError(message: String)
            fun onImageCaptured(capturedImageUri: Uri)
        }
        ```

    4. ```videoCaptureCallback``` * : This params accepts nullable instance of ```OnVideoCaptureCallback``` interface to receive the callback of capturing video.
        ```
        interface OnVideoCaptureCallback {
            fun onError(message: String)
            fun onVideoCaptured(capturedVideoUri: Uri)
        }  
        ```

    5. ```videoLimit``` - This param accepts a long value to define the time limit of video in milliseconds.
       - 10 seconds is default time limit.
       - -1 can be se to record video with no time limit.

> Note : ```imageCaptureCallback``` and ```videoCaptureCallback``` are optional parameters as there is separate method provided to se this callbacks as needed.

For example,
```
val fragment = cameraComponent.initCameraPreview(CameraModeEnum.PHOTO)
```
or
```
val fragment = cameraComponent.initCameraPreview(CameraModeEnum.VIDEO, videoLimit = 10000)
```
or
```
val fragment = cameraComponent.initCameraPreview(
            shouldShowPreview = false,
            cameraMode = CameraModeEnum.BOTH,
            videoLimit = 10000,
            imageCaptureCallback = object : OnImageCaptureCallback {
                override fun onError(message: String) {
                }

                override fun onImageCaptured(capturedImageUri: Uri) {
                }
            },
            videoCaptureCallback = object : OnVideoCaptureCallback {
                override fun onError(message: String) {
                }

                override fun onVideoCaptured(capturedVideoUri: Uri) {
                }
            }
        )
```


### ```setImageCaptureCallback()``` :
This method can be used to set the ```OnImageCaptureCallback``` explicitly.
``` 
val cameraComponent = CameraComponent()
        cameraComponent.setImageCaptureCallback(object : OnImageCaptureCallback {
            override fun onError(message: String) {
                // Handle error on image capture
            }

            override fun onImageCaptured(capturedImageUri: Uri) {
                // Handle success on image capture
                // capturedImageUri : will have uri or storage path of the captured image
            }
        })
```
> Note : ```initCameraPreview()``` method must be called prior to this method call.

### ```setVideoCaptureCallback()``` :
This method can be used to set the ```OnVideoCaptureCallback``` explicitly.
```
cameraComponent.setVideoCaptureCallback(object : OnVideoCaptureCallback {
            override fun onError(message: String) {
                // Handle error on video capture
            }

            override fun onVideoCaptured(capturedVideoUri: Uri) {
                // Handle success on video capture
                // capturedVideoUri : will have uri or storage path of the captured video
            }
        })
```
> Note : ```initCameraPreview()``` method must be called prior to this method call.

### ```enabledPreview()``` :
This method can be used to set the value of ```shouldShowPreview``` in ```initCameraPreview()``` method explicitly.
```
cameraComponent.enabledPreview(false)
```
or
```
cameraComponent.enabledPreview(true)
```
> Note : ```initCameraPreview()``` method must be called prior to this method call.

### ```setVideoTimeLimit()``` :
This method can be used to set video limit explicitly.
```
cameraComponent.setVideoTimeLimit(60000) // This will set video time limit to 60 seconds
```
> Note : ```initCameraPreview()``` method must be called prior to this method call.


## 3.	Technical Assumptions :
- This module will used to create functionality like short video apps.
- This module will used to create functionality like capturing image or video from camera only to use that as per need in the application.

## 4.	Limitations :
- As of now this module will not support feature like adding filter or frames or any other kind of image or video processing ability.

## 5.	Summary :
- To conclude, this module can be imported and used as a separate module in any android application. Functionality developed in this module can used as required or as per need to serve the purpose of main application.