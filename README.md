# SimpleImageSelect
Easy to use Image Picker library
- minSdkVersion 10

### Installation

Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```groovy
	dependencies {
	        compile 'com.github.samkazmi:SimpleImageSelect:1.0'
	}
```

## How it works?

### First

Initalize some variables in your `onCreate` method

 ```java
 SimpleImageSelect.Config(this, "Choose Profile Picture from", "FolderName");
 ```

 `FolderName` is the name of the folder where your selected image will be saved
 it uses ExternalStorage for now to save files.

 ### Then
 ###### Displays a chooser with both image and camera option
```java
SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);
```
 ###### Direclty open's camera
```java
 SimpleImageSelect.chooseSingleImage(Config.TYPE_CAMERA, this);
 ```
 ###### Displays a chooser for gallery apps only
```java
SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_GALLERY_ONLY, this);
```

Note: If you are using this method in a fragment you must use `this` as the second param

##### To Get Picked Image

 ```java
 try {
            String path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data);
            if (path != null) {
                Log.v("MainActiviy", "app: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } try {
            String path = SimpleImageSelect.onActivityResult(this, requestCode, resultCode, data);
            if (path != null) {
                Log.v("MainActiviy", "app: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
```

#### Also add these permissions in your manifest file
```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CAMERA" />
 ```
###### and lastly to clear config variables

```java
SimpleImageSelect.ClearConfig(this);
```



