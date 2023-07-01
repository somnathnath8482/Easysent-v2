package com.easy.pickfile;

import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import com.easy.pickfile.Interface.Onselect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;


public class PickFile {
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    AppCompatActivity activity;
    Onselect onselect;
    Handler handler;

    public PickFile(AppCompatActivity activity, android.os.Handler handler) {
        this.activity = activity;
        this.handler = handler;
        pickMedia = activity.registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                readUri(uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
        pickMultipleMedia =
                activity.registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(3), uris -> {
                    // Callback is invoked after the user selects media items or closes the
                    // photo picker.
                    if (!uris.isEmpty()) {
                        Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


    }

    public void setOnselect(Onselect onselect) {
        this.onselect = onselect;
    }

    public void PickImage(boolean multiple) {
        if (multiple) {
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        } else {
            pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        }
    }

    public void PickVideo() {
        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE).build());

    }

    public void PickImageVideo() {
        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE).build());
    }

    public void PickImageGief() {
        String mimeType = "image/gif";
        pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType(new ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)).build());
    }


    private void readUri(Uri returnUri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (returnUri.getScheme().equalsIgnoreCase("file")) {
                    File file = new File(returnUri.getPath());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (file.exists()) {
                                onselect.onSelect(file.getName(), file.getAbsolutePath());
                            } else {
                                onselect.onSelect();
                            }
                        }
                    });


                } else if (returnUri.getScheme().equalsIgnoreCase("content")) {
                    DocumentFile documentFile = DocumentFile.fromSingleUri(activity, returnUri);


                    if (documentFile != null && documentFile.exists()) {

                        String path = new File(activity.getFilesDir(), "EasySent/Cash").getAbsolutePath();
                        File file = new File(path + "/" + documentFile.getName());


                        try {
                            if (file.exists()) {
                                file.delete();
                                file.createNewFile();

                            } else {
                                file.createNewFile();
                            }


                            InputStream inputStream = activity.getContentResolver().openInputStream(returnUri);
                            FileOutputStream out = new FileOutputStream(file);
                            copyStream(inputStream, out);
                            out.close();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (file.exists()) {
                                        onselect.onSelect(documentFile.getName(), file.getAbsolutePath());
                                    } else {
                                        onselect.onSelect();
                                    }

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onselect.onSelect();
                            }
                        });

                    }


                }
            }
        }).start();


    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
                /*out.flush();
                out.close();*/
        }
        out.flush();
        out.close();
    }


}
