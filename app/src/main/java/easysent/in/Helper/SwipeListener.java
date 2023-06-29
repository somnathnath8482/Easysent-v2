package easysent.in.Helper;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SwipeListener implements View.OnTouchListener {
    View left,right;
SwpeListener swpeListener;

    GestureDetector gestureDetector;

    public SwipeListener(View view,SwpeListener swpeListener) {
        final int threashold = 100;
        final int velocity_Threashold = 100;

      this.swpeListener = swpeListener;


        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return  true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x1 = e1.getX();
                float y1 = e1.getY();

                float x2 = e2.getX();
                float y2 = e2.getY();

                Direction direction = getDirection(x1,y1,x2,y2);
                if (direction==Direction.left){
                    swpeListener.onSwipe(1);
                }
                else if (direction==Direction.right){
                    swpeListener.onSwipe(0);
                }
                Log.e("By Direction", "onFling: By Direction  "+direction.toString() );

/*
                float xDiff = e2.getX() - e1.getX();
                float yDidd = e2.getY() -e1.getY();
                try {
                    if (Math.abs(xDiff)>Math.abs(yDidd)){
                        if (Math.abs(xDiff)>threashold && Math.abs(velocityX) > velocity_Threashold){
                            if (xDiff>0){
                                // Toast.makeText(chatImage.this, "Right", Toast.LENGTH_SHORT).show();
                               // if (left!=null)
                                //left.performClick();
                                    swpeListener.onSwipe(0);
                            }else{
                                //Toast.makeText(chatImage.this, "left", Toast.LENGTH_SHORT).show();
                                //if (right!=null)
                                //right.performClick();

                                swpeListener.onSwipe(1);

                            }
                            return true;
                        }


                    }
                    else{
                        if (Math.abs(yDidd)>threashold && Math.abs(velocityY) >velocity_Threashold){
                            if (yDidd>0){
                                //Toast.makeText(chatImage.this, "down", Toast.LENGTH_SHORT).show();
                            }else{
                                //Toast.makeText(chatImage.this, "top", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }*/
                return false;

            }
        };
        gestureDetector = new GestureDetector(listener);
        view.setOnTouchListener(this);

    }

    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }
    public enum Direction{
        up,
        down,
        left,
        right;

        public static Direction fromAngle(double angle){
            if(inRange(angle, 45, 135)){
                return Direction.up;
            }
            else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
                return Direction.right;
            }
            else if(inRange(angle, 225, 315)){
                return Direction.down;
            }
            else{
                return Direction.left;
            }

        }


        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }

    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.fromAngle(angle);
    }

public interface SwpeListener{
        void onSwipe(int direction);

}
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        return  gestureDetector.onTouchEvent(motionEvent);
    }
}
