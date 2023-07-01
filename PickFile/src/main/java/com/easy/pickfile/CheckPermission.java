package com.easy.pickfile;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.easy.pickfile.Interface.IsSelect;
import com.easy.pickfile.Interface.Onselect;

import java.util.ArrayList;
import java.util.List;

public class CheckPermission {
    AppCompatActivity activity;
    List<String> required_permissions= new ArrayList<>();

    public CheckPermission(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void is_permitted (List<String> permission, boolean shoudrequest, IsSelect onselect){
        for (int i = 0; i < permission.size(); i++) {
          if (  ContextCompat.checkSelfPermission(activity, permission.get(i))== PackageManager.PERMISSION_DENIED){
              required_permissions.add(permission.get(i));
          }

        }
        if (shoudrequest && required_permissions.size()>0){
            request();
        }
        if (!shoudrequest && required_permissions.size()>0 && onselect!=null){
            onselect.isSelect(true);
        }
    }

    private void request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(required_permissions.toArray(new String[0]), 9009);
        }
    }
}
