//package jp.satorukabuyama.memoapplv2;
//
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.PixelFormat;
//
//import android.graphics.PorterDuff.Mode;
//import android.media.Image;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.widget.CompoundButton;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.ArrayDeque;
//import java.util.Calendar;
//import java.util.Deque;
//import java.util.SimpleTimeZone;
//
////サーフェイスビューを利用するには、ViewクラスではなくSurfaceViewクラスを継承します。
////そして、サーフェイスビューの状態変化時のイベント処理を行うSurfaceHolder.Callbackインターフェースを
////実装します。SurfaceHolder.Callbackインターフェースには、以下の3つのメソッドが定義されています。
////surfaceCreated、surfaceChanged、surfaceDestroyed
//public class DrawView2 extends SurfaceView implements Callback {
//
//    private SurfaceHolder mHolder;//サーフェイスホルダー
//    private Paint mPaint;
//    private Path mPath;
//    private Bitmap mLastDrawBitmap;
//    private Canvas mLastDrawCanvas;
//    private Deque<Path> mUndoStack = new ArrayDeque<Path>();//undo用のpathを格納する変数（スタック）
//    private Deque<Path> mRedoStack = new ArrayDeque<Path>();//redo用のpathを格納するスタック
//    //スタックとは、現在生きているアクティビティを管理する入れ物みたいなもの
//    //例えば、起動時のアクティビティがActivityAで、そこからstartActivityで別のActivityBにとおんで、
//    //さらにActivityCに飛んだとするとスタックの中は
//    //「 A-B-C 」になっている
//    //ここで、ActibityCをfinish()したり、戻るボタンを押すと。「 A-B 」になる
//    private float mLastTouchX;
//    private float mLastTouchY;
//    private boolean mFirstMovedFlg;
//
//    public DrawView2(Context context) {
//        super(context);
//        init();
//    }
//
//    public DrawView2(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        //サーフェイスホルダーの生成  サーフェイスビューの設定は、サーフェイスホルダーを取得して行います
//        //サーフェイスホルダーを取得するには、SurfaceViewクラスのgetHolder()メソッドを使います。
//        mHolder = getHolder();
//
//        // 透過します。
//        //RelativeLayoutを基底とするViewとカメラプレビューを表示させるViewがそれぞれ独立したサーフェイス
//        //ってのをもっているから、表示順序がおかしくなることがあるらしい。なので、View(content_main)を
//        //ｚ軸の最上部に配備し、その中で対象となるSurfaceViewを上に持ってくるので定義したすべてのviewが表示される
//        setZOrderOnTop(true);
//        //これで実際に背景に透過を設定
//        mHolder.setFormat(PixelFormat.TRANSPARENT);
//
//        // コールバックを設定します。
//        //取得したサーフェイスホルダーにaddCallback()メソッドで、
//        // サーフェイスイベントの通知先を指定
//        mHolder.addCallback(this);
//
//        // ペイントを設定します。
//        mPaint = new Paint();
//        mPaint.setColor(Color.rgb(0, 0, 0));//黒
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(6);
//
//    }
//
//    public void penColor() {
//        if(!MainActivity.eraserFlg) {
//            mPaint.setColor(Color.rgb(0, 0, 0));//黒
//        } else {
//            mPaint.setColor(Color.rgb(255, 255, 255));//白
//        }
//    }
//
//
//    //サーフェイスの生成
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        // 描画状態を保持するBitmapを生成します。
//        clearLastDrawBitmap();
//    }
//
//    //サーフェイスの変更
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//    }
//
//    //サーフェイスの破棄
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        mLastDrawBitmap.recycle();
//    }
//
//
//    private void clearLastDrawBitmap() {
//        if (mLastDrawBitmap == null) {
//            //bitmapを作成
//            //Bitmap.Config.ARGB_8888とは、32ビットのARGBデータでBitmapを作成することを、示しています
//            //通常androidで使用しているcolorの値がこれに該当しますので、特に指定の必要がなければこの値を入れてください
//            mLastDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
//        }
//
//        //すでにcanvasが描かれていたら、書かれた描画（上でbitmapに変えたmLastDrawBitmap）を追加する
//        if (mLastDrawCanvas == null) {
//            mLastDrawCanvas = new Canvas(mLastDrawBitmap);
//        }
//
//        //canvasで描画した領域を動的に非表示にしたり表示したりする場合には、
//        //canvas.drawColor(0, PorterDuff.Mode.CLEAR)を使います。
//        //また同時に、クリアした後で再描画しないと非表示になりません。
//        mLastDrawCanvas.drawColor(0, Mode.CLEAR);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                onTouchDown(event.getX(), event.getY());
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                onTouchMove(event.getX(), event.getY());
//                break;
//
//            case MotionEvent.ACTION_UP:
//                onTouchUp(event.getX(), event.getY());
//                break;
//
//            default:
//        }
//        return true;
//    }
//
//    private void onTouchDown(float x, float y) {
//        //パス、つまり線のこと、線をかくための準備
//        mPath = new Path();
//        //パス開始座標の指定
//        mPath.moveTo(x, y);
//        mLastTouchX = x;
//        mLastTouchY = y;
//        mFirstMovedFlg = false;
//    }
//
//    private void onTouchMove(float x, float y) {
//        //終点からのラインの追加
//        //mPath.lineTo(x, y);
//        if(!mFirstMovedFlg) {
//            mFirstMovedFlg = true;
//            mLastTouchX = x;
//            mLastTouchY = y;
//            return;
//        }
//
//        float middlePointX = (mLastTouchX + x) / 2;
//        float middlePointY = (mLastTouchY + y) / 2;
//        //quadTO()  エクセルとかの自由今日苦戦と同じアルゴリズムで、４点するからの二次計算で曲線を描画
//        //↑よくわからん！
//        //onTouchEvent()で取得した座標に対してlineTo()とかで直線を引き続けると、どうしても角ばってしまう。
//        //quadTo()を使うと滑らか！！
//        //1つ目と２つ目の間の点と、2つ目と三つ目の間の点を直線で結んだあと、入力された３つの点で引っ張って曲げた曲線
//        //というイメージ
//        mPath.quadTo(mLastTouchX, mLastTouchY, middlePointX, middlePointY);
//        //ライン（線）の描画
//        drawLine(mPath);
//        mLastTouchX = x;
//        mLastTouchY = y;
//    }
//
//    private void onTouchUp(float x, float y) {
//        //mPath.lineTo(x, y);
//        mPath.quadTo(mLastTouchX, mLastTouchY, x, y);
//        drawLine(mPath);
//        //書き終わったところ（画面から指を離したところで）パスの描画
//        //path(path  パス情報,  paint  描画オブジェクト)
//        mLastDrawCanvas.drawPath(mPath, mPaint);
//        //タッチした指を話したタイミングで、undoスタックにパスを追加し
//        //（スタックに１つの線が入り、どんどん後からきたものを格納されていくことで、「一つ戻る」というこがスタックだからこそ出来る）
//        // redoスタックを空にします
//        // （書き終わったタイミングでredoをクリアしないと、線がスタックに残ったままで「一つ進む」ということができなくなる。
//        // というか変になる。redoはundoが押されたときにスタックへいれると進むボタンのようになる）
//        mUndoStack.addLast(mPath);
//        mRedoStack.clear();
//    }
//
//    private void drawLine(Path path) {
//        //surfaceViewにはdrawメソッドがありあすが。このメソッドをお＾ば＾ライドしただけでは何も描画されません。
//        //androidではリソースを節約するためなのか、アプリから描画を求めなければ描画されないのです。
//        //そのため、キーイベントやメインループなどをトリガーとして、描画メソッドを呼び出します。
//        //描画メソッドはdrawをオーバーライドしなくても構いませんが,描画に必要なのはCanvasのインスタンスになります
//        //このCanvasのインスタンスを取得するためには、surfaceHolderのlockCanvasメソッドを使います。
//        // ロックしてキャンバスを取得します。
//        Canvas canvas = mHolder.lockCanvas();
//
//        // キャンバスをクリアします。
//        //Mode.CLEARで描画色が有効な範囲を透明にする処理を実行
//        canvas.drawColor(0, Mode.CLEAR);
//
//        // 前回描画したビットマップをキャンバスに描画します。
//        canvas.drawBitmap(mLastDrawBitmap, 0, 0, null);
//
//        // パスを描画します。
//        canvas.drawPath(path, mPaint);
//
//        // ロックを外します。
//        mHolder.unlockCanvasAndPost(canvas);
//    }
//
//    //各メソッドは、undoスタックとredoスタックにパスを出し入れして描画処理を行うシンプルな流れ
//    public void undo() {
//        if (mUndoStack.isEmpty()) {
//            return;
//        }
//
//        // undoスタックからパスを取り出し、redoスタックに格納します。
//        Path lastUndoPath = mUndoStack.removeLast();
//        mRedoStack.addLast(lastUndoPath);
//
//        // ロックしてキャンバスを取得します。
//        Canvas canvas = mHolder.lockCanvas();
//
//        // キャンバスをクリアします。
//        canvas.drawColor(0, Mode.CLEAR);
//
//        // 描画状態を保持するBitmapをクリアします。
//        clearLastDrawBitmap();
//
//        // パスを描画します。
//        for (Path path : mUndoStack) {
//            canvas.drawPath(path, mPaint);
//            mLastDrawCanvas.drawPath(path, mPaint);
//        }
//
//        // ロックを外します。
//        mHolder.unlockCanvasAndPost(canvas);
//    }
//
//    public void redo() {
//        if (mRedoStack.isEmpty()) {
//            return;
//        }
//
//        // redoスタックからパスを取り出し、undoスタックに格納します。
//        Path lastRedoPath = mRedoStack.removeLast();
//        mUndoStack.addLast(lastRedoPath);
//
//        // パスを描画します。
//        drawLine(lastRedoPath);
//
//        mLastDrawCanvas.drawPath(lastRedoPath, mPaint);
//    }
//
//    public void reset() {
//        mUndoStack.clear();
//        mRedoStack.clear();
//
//        clearLastDrawBitmap();
//
//        Canvas canvas = mHolder.lockCanvas();
//        canvas.drawColor(0, Mode.CLEAR);
//        mHolder.unlockCanvasAndPost(canvas);
//    }
//
//    //画像保存
//    public void saveToFile() {
//        if (!sdcardWriteReady()) {
//            Toast.makeText(getContext(), "SDcardが認識されません。", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String saveDir = Environment.getExternalStorageDirectory().getPath() + "/mome/";
//
//        //SDカードフォルダを取得
//        File file = new File(saveDir);
//
//
//        try {
//            if(!file.exists()) {
//                file.mkdir();
//            }
//        } catch (SecurityException e) {
//            Log.e("-----------", e.toString());
//        }
//
//        //画像保存パス
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String imgPath = saveDir + "/" + sf.format(cal.getTime()) + ".jpg";
//
//        //ファイル保存
//        FileOutputStream fos;
//        try{
//            fos = new FileOutputStream(imgPath, true);
//            fos.write(mLastDrawBitmap);
//            fos.close();
//
//            //アンドロイドのデータベースへ登録
//            //登録しないとギャラリーなどにすぐに反映されないため
//            registAndroidDB(imgPath);
//
//        } catch (FileNotFoundException e) {
//            Log.e("-------------", e.toString());
//        }
//
//        fos = null;
//
//    }
//
//    private static boolean sdcardWriteReady() {
//        String state = Environment.getExternalStorageState();
//        return (Environment.MEDIA_MOUNTED.equals(state));
//    }
//
//    //アンドロイドのデータベースへ画像のパスを登録
//    //path  登録するパス
//    private void registAndroidDB(String path) {
//        //アンドロイドのデータベースへ登録
//        //登録しないとギャラリーにすぐに反映されないため
//        ContentValues values = new ContentValues();
//        ContentResolver contentResolver = getContext().getContentResolver();
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
//        values.put("_data", path);
//        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//    }
//
//}