 package jp.satorukabuyama.memoapplv2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import static jp.satorukabuyama.memoapplv2.DrawView.mLastDrawBitmap;
import static jp.satorukabuyama.memoapplv2.DrawView.mLastDrawCanvas;
import static jp.satorukabuyama.memoapplv2.DrawView.mPaint;
import static jp.satorukabuyama.memoapplv2.DrawView.mUndoStack;

 public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private DrawView drawView;
    private Button undoBtn;
    private Button redoBtn;
    private Button resetBtn;
    public static Switch eraserSwitch;
     //public static ImageButton eraserSwitch;
    public static boolean eraserFlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.memoCanvas);

        eraserSwitch = (Switch) findViewById(R.id.eraserSwitch);
        eraserSwitch.setOnCheckedChangeListener(this);

//        eraserSwitch = (ImageButton) findViewById(R.id.eraserSwitch);
//        eraserSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int i = 1;
//                i = i++;
//                if (i%2 == 0) {
//                    eraserFlg = true;
//                } else {
//                    eraserFlg = false;
//                }
//
//            }
//        });

//        final View multipleAction = findViewById(R.id.multiple_actions);
//        WindowManager mWindowMgr = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
//        layoutParams.x = 100;//表示位置 x
//        layoutParams.y = 100;//表示位置 y
//        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//        layoutParams.format = PixelFormat.TRANSLUCENT;//透明を有効化
//
//        mWindowMgr.addView(multipleAction, layoutParams);

        //一番上のボタン
        final View actionA = findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.saveToFile();
            }
        });

        //二番目のボタン
        final View actionB = findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.reset();
            }
        });

        //三番目のボタン
        final View actionC = findViewById(R.id.action_c);
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.redo();
            }
        });

        //一番下のボタン
        final View actionD = findViewById(R.id.action_d);
        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.undo();
            }
        });



    }

     @Override
     protected void onStart() {
         super.onStart();

     }

    @Override
    protected void onResume() {

        super.onResume();

//        if (mLastDrawBitmap == null) {
//            mLastDrawBitmap = Bitmap.createBitmap(drawView.getWidth(), drawView.getHeight(), Bitmap.Config.ARGB_8888);
//        }
//
//        if (mLastDrawCanvas == null) {
//            mLastDrawCanvas = new Canvas(mLastDrawBitmap);
//        }
//
//        mLastDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//        for (PaintLog paintLog : mUndoStack) {
//                //☆色を設定
//                mPaint.setColor(paintLog.getColor());
//                mLastDrawCanvas.drawPath(paintLog.getPath(), mPaint);
//            }

//        if(mLastDrawCanvas != null) {
//            mLastDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//
//            for (PaintLog paintLog : mUndoStack) {
//                //☆色を設定
//                mPaint.setColor(paintLog.getColor());
//                mLastDrawCanvas.drawPath(paintLog.getPath(), mPaint);
//            }
//        } else {
//            return;
//        }
    }

     @Override
     protected void onPause() {
         super.onPause();

     }

//☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        for (PaintLog paintLog : mUndoStack) {
//                outState.putParcelable("paintlog", (Parcelable) paintLog);
//            }
//
//    }
//
//     @Override
//     protected void onRestoreInstanceState(Bundle savedInstanceState) {
//         super.onRestoreInstanceState(savedInstanceState);
//
//         Parcelable value = savedInstanceState.getParcelable("paintlog");
//         PaintLog paintlog = (PaintLog) value;
//
//         mLastDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
//             mPaint.setColor(paintlog.getColor());
//             mLastDrawCanvas.drawPath(paintlog.getPath(), mPaint);
//
//     }

    @Override
     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
         if (b == true) {
             eraserFlg = true;
         } else {
             eraserFlg = false;
         }
     }
}

 //あとでやること
 // onPouseとかした時点で、画像を作成し、
 //それをonStartとかしたときに、画面に貼り付け
