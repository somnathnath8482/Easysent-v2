package easysent.in.Helper;



import static android.graphics.BitmapFactory.decodeFile;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImageGetter extends AsyncTask<File, Void, Bitmap> {
    private ImageView iv;
    public ImageGetter(ImageView v) {
        iv = v;
    }
    @Override
    protected Bitmap doInBackground(File... params) {
        Bitmap bitmap = decodeFile(params[0].getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        //iv.setImageBitmap(result);
        try {
            Glide.with(iv.getContext()).load(result).thumbnail(0.01f).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}