# SimpleImageSelect
Easy to use Image Picker library
- minSdkVersion 10

### Installation

Step 1. Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```
	dependencies {
	        compile 'com.github.samkazmi:SimpleImageSelect:1.0'
	}
```

## How it works?

### First

Initalize some variables in your `onCreate` method
 `SimpleImageSelect.Config(this, "Choose Profile Picture from", "FolderName");`

 `FolderName` is the name of the folder where your selected image will be saved
 it uses ExternalStorage for now to save files.

 ### Then
 ###### Displays a chooser with both image and camera option
`SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_BOTH, this);`
 ###### Direclty open's camera
`SimpleImageSelect.chooseSingleImage(Config.TYPE_CAMERA, this);`
 ###### Displays a chooser for gallery apps only
`SimpleImageSelect.chooseSingleImage(Config.TYPE_CHOOSER_GALLERY_ONLY, this);`

Note: If you are using this method in a fragment you must use `this` as the second param

##### To Get Picked Image

 ```
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

###### and lastly to clear config variables

```
SimpleImageSelect.ClearConfig(this);
```



