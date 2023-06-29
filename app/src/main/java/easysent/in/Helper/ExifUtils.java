package easysent.in.Helper;



import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.io.File;

public class ExifUtils {


    public static Bitmap getRotatedBitmap(Context context, String uri) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {

                Bitmap bitmap = BitmapFactory.decodeFile(uri);
                float width = (float) bitmap.getWidth();
                float height = (float) bitmap.getHeight();
                float max = Math.max(width / 1280.0f, height / 1280.0f);
                if ( max > 1.0f) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width / max), (int) (height / max), false);
                }
                Bitmap rotateBitmap = rotateBitmap(bitmap,
                        new ExifInterface(uri)
                                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1));
                if (rotateBitmap != bitmap) {
                    bitmap.recycle();
                }
                return rotateBitmap;
            }else{
                //todo
               /* filePickerHelper.isPermissionsAllowed(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, true, 1007);*/
                return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.setRotate(180.0f);
                break;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 6:
                matrix.setRotate(90.0f);
                break;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 8:
                matrix.setRotate(-90.0f);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}