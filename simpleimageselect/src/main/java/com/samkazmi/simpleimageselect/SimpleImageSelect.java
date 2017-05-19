package com.samkazmi.simpleimageselect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SamKazmi on 5/16/2017.
 */

public class SimpleImageSelect {

    private static final String TAG = "SimpleImagePicker";
    private static String mCurrentPhotoPath;
    //private Bitmap mImageBitmap;
    private static String CHOOSE_TITLE = "Select photo from";
    private static String APP_NAME = "AppName";
    private static File photoFile = null;
    private static int type = Config.TYPE_CHOOSER_BOTH;

    public static void Config(Context context, String chooseTitle, String folderName) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("title", chooseTitle).apply();
        preferences.edit().putString("folderName", folderName).apply();
    }

    public static void ClearConfig(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove("title").apply();
        preferences.edit().remove("folderName").apply();
    }

    public static void chooseSingleImage(int type, Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        CHOOSE_TITLE = preferences.getString("title", "Select photo from");
        APP_NAME = preferences.getString("folderName", "AppName");
        Intent pickerIntent = createPickerIntent(type, activity);
        if (pickerIntent != null) {
            activity.startActivityForResult(pickerIntent, Config.REQUEST_CODE_PICK_SINGLE_IMAGE);
        }
    }

    public void chooseSingleImage(int type, Fragment fragment) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(fragment.getContext());
        CHOOSE_TITLE = preferences.getString("title", "Select photo from");
        APP_NAME = preferences.getString("folderName", "AppName");
        Intent pickerIntent = createPickerIntent(type, fragment.getContext());
        if (pickerIntent != null) {
            fragment.startActivityForResult(pickerIntent, Config.REQUEST_CODE_PICK_SINGLE_IMAGE);
        }
    }

    public static String onActivityResult(Context context, int requestCode, int resultCode, Intent data) throws IOException {
        // To get the thumnail of the image from camera
        // Bundle extras = data.getExtras();
        // Bitmap imageBitmap = (Bitmap) extras.get("data");
        if (requestCode == Config.REQUEST_CODE_PICK_SINGLE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return getPathOfImagePathTakenFromCamera();
            } else {
                return getPathOfImageTakenFromGallery(data, context);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (photoFile != null) {
                Log.v(TAG, "delete: " + photoFile.delete());
            }
        }
        clear();
        return null;
    }

    private static String getPathOfImageTakenFromGallery(Intent data, Context context) {
        Uri selectedImage = data.getData();
        // To get bitmap of image
        //InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        // Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
        String path = getRealPathFromURI(selectedImage, context);
        Log.v(TAG, "path: " + path);
        return path;
    }

    private static String getPathOfImagePathTakenFromCamera() {
        // TO get bitmap of image taken from camera
        //mImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(mCurrentPhotoPath));
        Log.v(TAG, "filePath " + mCurrentPhotoPath);
        return mCurrentPhotoPath;
    }

    private static Intent createPickerIntent(int type, Context context) {
        //Create an intent
        Intent pickerIntent = null;
        switch (type) {
            case Config.TYPE_CAMERA:
                pickerIntent = createCameraIntent(context);
                break;
            case Config.TYPE_CHOOSER_GALLERY_ONLY:
                pickerIntent = Intent.createChooser(createGalleryIntent(), CHOOSE_TITLE);
                break;
            case Config.TYPE_CHOOSER_BOTH:
                Intent[] intents = new Intent[]{createCameraIntent(context)};
                pickerIntent = Intent.createChooser(createGalleryIntent(), CHOOSE_TITLE);
                pickerIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
                break;
        }
        return pickerIntent;
    }

    private static Intent createGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        return galleryIntent;
    }

    private static Intent createCameraIntent(Context context) {
        //Create any other intents you want
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                Log.i(TAG, "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                Toast.makeText(context, "Unsuccessful in creating a file", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return cameraIntent;
    }

    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        File storageDir = new File(sdcardPath + "/" + APP_NAME + "/cache/");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =/* "file:" +*/ image.getAbsolutePath();
        return image;
    }

    private static String getRealPathFromURI(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    private static void clear() {
        photoFile = null;
        mCurrentPhotoPath = null;
    }
}
