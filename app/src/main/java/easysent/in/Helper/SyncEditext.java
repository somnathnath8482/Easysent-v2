package easysent.in.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SyncEditext implements TextWatcher {
    EditText e1,e2;

    public SyncEditext(EditText e1, EditText e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (e1.hasFocus()){
            e2.setText(editable);
        }else if (e2.hasFocus()){
            e1.setText(editable);
        }
    }
}
