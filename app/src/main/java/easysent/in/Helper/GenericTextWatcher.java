package easysent.in.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;


public class GenericTextWatcher implements TextWatcher {
    private final EditText[] editText;
    int position;
    private View view;

    public GenericTextWatcher(View view, EditText editText[], int position) {
        this.editText = editText;
        this.view = view;
        this.position = position;

        //editText[position].setOnKeyListener(new liten());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.e("TAG", "afterTextChanged: ");
        String text = editable.toString();
       /* switch (view.getId()) {

            case R.id.edt1:
                if (text.length() == 1)
                    editText[1].requestFocus();
                break;
            case R.id.edt2:

                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.edt3:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;

            case R.id.edt4:
                if (text.length() == 1)
                    editText[4].requestFocus();
                else if (text.length() == 0)
                    editText[2].requestFocus();
                break;

           case R.id.edt5:
                if (text.length() == 1)
                    editText[5].requestFocus();
                else if (text.length() == 0)
                    editText[3].requestFocus();
                break;

                case R.id.edt6:
                if (text.length() == 0)
                    editText[4].requestFocus();
                break;




        }*/


        if (position == 0) {

            if (text.length() > 0) {
                editText[position + 1].requestFocus();
            }


        } else if (position == editText.length - 1) {
            if (text.length() == 0) {
                editText[position - 1].requestFocus();
            }
        } else if (position < editText.length) {


            if (text.length() > 0) {
                editText[position + 1].requestFocus();
            } else if (text.length() == 0) {
                editText[position - 1].requestFocus();
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Log.e("TAG", "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        Log.e("TAG", "onTextChanged: ");
    }


    private class liten implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                if (position == 0) {
                    editText[position].setText("");
                } else {
                    editText[position].setText("");
                    //editText[position - 1].requestFocus();
                }
                return false;
            } else {
                return false;
            }
        }
    }
}
