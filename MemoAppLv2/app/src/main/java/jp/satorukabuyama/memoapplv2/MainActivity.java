 package jp.satorukabuyama.memoapplv2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;
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
     private boolean oncreateFlg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.memoCanvas);

        eraserSwitch = (Switch) findViewById(R.id.eraserSwitch);
        eraserSwitch.setOnCheckedChangeListener(this);

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


        oncreateFlg = true;

    }

     @Override
     protected void onStart() {
         super.onStart();

     }

     //api16以上にはこの下の@TargetApi(Build.VERSION_CODES.JELLY_BEAN)ところを描く
     //api16以下のを対応させるのは下のを描く
     //@SuppressWarnings("deprecation")
     //private void setBackgroundV16Minus(View view, Bitmap bitmap) {
     //    view.setBackgroundDrawable(new BitmapDrawable(bitmap));
     //}
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();


        InputStream input = null;
        if (mLastDrawCanvas == null) {
            return;

        } else {
            try {
                input = this.openFileInput("image.png");
            } catch (FileNotFoundException e) {
                Log.e("---------", e.toString());
            }

            Bitmap image = BitmapFactory.decodeStream(input);

            DrawView mDrawView = (DrawView) findViewById(R.id.memoCanvas);
            BitmapDrawable ob = new BitmapDrawable(getResources(), image);
            mDrawView.setBackground(ob);

             }


        }

     @Override
     protected void onPause() {
         super.onPause();

         //保存する前に背面を白にし、保存する
         mLastDrawCanvas.drawColor(Color.WHITE);
         for (PaintLog paintLog : mUndoStack) {
             //☆色と線を描画
             mPaint.setColor(paintLog.getColor());
             mLastDrawCanvas.drawPath(paintLog.getPath(), mPaint);
         }

         try{
             FileOutputStream out = this.openFileOutput("image.png", Context.MODE_PRIVATE);
             mLastDrawBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
             out.flush();
             out.close();

         } catch (Exception e) {
             Log.e("-----------", e.toString());

         }

         mLastDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
         for (PaintLog paintLog : mUndoStack) {
             //☆色と線を描画
             mPaint.setColor(paintLog.getColor());
             mLastDrawCanvas.drawPath(paintLog.getPath(), mPaint);
         }

     }

    @Override
     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
         if (b == true) {
             eraserFlg = true;
         } else {
             eraserFlg = false;
         }
     }
}

