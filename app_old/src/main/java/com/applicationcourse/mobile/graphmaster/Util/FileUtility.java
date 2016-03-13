package com.applicationcourse.mobile.graphmaster.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by teresa on 24/02/16.
 */
public class FileUtility {
        public static String TAG = "File Utility";
        private FileUtility() {
            // empty constructor
        }

        // Copy File Utility - Uses a string as input instead of filestreams
        public static void copyFile(String fromFileString, String toFileString) throws IOException {
            File fromFile = new File(fromFileString);
            File toFile = new File(toFileString);
            copyFile(new FileInputStream(fromFile), new FileOutputStream(toFile));
        }

        // Copy File Utility using File streams
        public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
            FileChannel fromChannel = null;
            FileChannel toChannel = null;
            try {
                fromChannel = fromFile.getChannel();
                toChannel = toFile.getChannel();
                fromChannel.transferTo(0, fromChannel.size(), toChannel);
            } finally {
                try {
                    if (fromChannel != null) {
                        fromChannel.close();
                    }
                } finally {
                    if (toChannel != null) {
                        toChannel.close();
                    }
                }
            }
        }

        // Move file function.  Less robust since it will not handle movement from different file systems
        public static void moveFile (String fromFileString, String toFileString) throws IOException {
            File fromFile = new File(fromFileString);
            File toFile = new File(toFileString);
            fromFile.renameTo(toFile);
        }

        // More robust since it will handle different filesystems.
        public static void moveFileRobust(String fromFileString, String toFileString) throws IOException {
            // Copies the file (regardless if it is on different file systems
            copyFile(fromFileString, toFileString);
            // delete
            new File(fromFileString).delete();
        }

        // gets the list of files in the directory listed.
        public static String[] getFiles (String dirPath) {
            File dir = new File(dirPath);
            if (dir.exists()) {
                return dir.list();
            }
            return null;
        }

        public static void writeToFile (File file, byte[] data) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(FileUtility.TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(FileUtility.TAG, "Error accessing file: " + e.getMessage());
            }
        }
        /** Create a file Uri for saving an image or video */
        public static Uri getOutputMediaFileUri(int type){
            return Uri.fromFile(getOutputMediaFile(type));
        }

        /** Create a File for saving an image or video */
        public static File getOutputMediaFile(int type){
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Assignment3");
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (! mediaStorageDir.exists()){
                if (! mediaStorageDir.mkdirs()){
                    Log.d("MyCameraApp", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_"+ timeStamp + ".jpg");
            } else if(type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "VID_"+ timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }

        //decodes image and scales it to reduce memory consumption
        public static Bitmap decodeFile(File f, final int size){
            // size refers to the final size, approximately 100 for a preview is adequate,
            // scales to ~100 pixels (exact scaled size depends on aspect ratio)
            try {
                //Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f), null, o);


                //Find the correct scale value. It should be the power of 2.
                int scale=1;
                while(o.outWidth/scale/2>=size && o.outHeight/scale/2>=size)
                    scale*=2;

                //Decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {}
            return null;
        }
}
