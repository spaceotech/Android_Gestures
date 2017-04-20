package com.spaceo.gesturesdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    ViewGroup editingLayout =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editingLayout = (ViewGroup) findViewById(R.id.rootLayout);



        Button NewImage = (Button)findViewById(R.id.btn1);
        NewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewImage();
            }

        });

        clickCount = 0;

    }



    private void addNewImage() {

        final ImageView iv = new ImageView(this);
        iv.setImageResource(R.mipmap.ic_launcher);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 150);
        iv.setLayoutParams(layoutParams);
        editingLayout.addView(iv, layoutParams);
        iv.setOnTouchListener(this);

    }


    int clickCount;
    private int posX;
    private int posY;
    long startTime = 0 ;

    public boolean onTouch(final View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        int pointerCount = event.getPointerCount();


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                posX = X - layoutParams.leftMargin;
                posY = Y - layoutParams.topMargin;
                break;

            case MotionEvent.ACTION_UP:
                if (startTime == 0){

                    startTime = System.currentTimeMillis();

                }else {
                    if (System.currentTimeMillis() - startTime < 200) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to delete this?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                view.setVisibility(View.GONE);

                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                    startTime = System.currentTimeMillis();

                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                if (pointerCount == 1){
                    RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    Params.leftMargin = X - posX;
                    Params.topMargin = Y - posY;
                    Params.rightMargin = -500;
                    Params.bottomMargin = -500;
                    view.setLayoutParams(Params);
                }

                if (pointerCount == 2){

                    Log.e("TAG","2 finger touched");

                    RelativeLayout.LayoutParams layoutParams1 =  (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams1.width = posX +(int)event.getX();
                    layoutParams1.height = posY + (int)event.getY();
                    view.setLayoutParams(layoutParams1);
                }

                //Rotation
                if (pointerCount == 3){
                    //Rotate the ImageView
                    view.setRotation(view.getRotation() + 10.0f);
                }

                break;
        }

// Schedules a repaint for the root Layout.
        editingLayout.invalidate();
        return true;
    }

}
