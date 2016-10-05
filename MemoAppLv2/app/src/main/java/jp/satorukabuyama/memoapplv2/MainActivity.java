package jp.satorukabuyama.memoapplv2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private DrawView drawView;
    //private DrawView2 drawView2;
    private Button undoBtn;
    private Button redoBtn;
    private Button resetBtn;
    public static Switch eraserSwitch;
    public static boolean eraserFlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.memoCanvas);
        //drawView2 = (DrawView2) findViewById(R.id.memoCanvas);

//        undoBtn = (Button) findViewById(R.id.undoBtn);
//        undoBtn.setOnClickListener(this);
//
//        redoBtn = (Button) findViewById(R.id.redoBtn);
//        redoBtn.setOnClickListener(this);
//
//        resetBtn = (Button) findViewById(R.id.resetBtn);
//        resetBtn.setOnClickListener(this);

        eraserSwitch = (Switch) findViewById(R.id.eraserSwitch);
        eraserSwitch.setOnCheckedChangeListener(this);

        final View actionB = findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.redo();
            }
        });

        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("UNDO");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                drawView.undo();
            }
        });

        FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.addButton(actionC);

        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionA.setTitle("RESET");
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.reset();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "確認", Toast.LENGTH_SHORT).show();
//                drawView.saveToFile();
//                //drawView2.saveToFile();
//            }
//        });
    }

//    @Override
//    public void onClick(View view) {
//        if(view == undoBtn) {
//            drawView.undo();
//            //drawView2.undo();
//        } else if (view == redoBtn) {
//            drawView.redo();
//            //drawView2.redo();
//        } else if (view == resetBtn) {
//            drawView.reset();
//            //drawView2.reset();
//        }
//    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b == true) {
            eraserFlg = true;
            //drawView.penColor();
            //drawView2.penColor();
        } else {
            eraserFlg = false;
            //drawView.penColor();
            //drawView2.penColor();
        }
    }
}
