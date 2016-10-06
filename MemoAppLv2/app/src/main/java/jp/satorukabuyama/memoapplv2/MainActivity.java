package jp.satorukabuyama.memoapplv2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private DrawView drawView;
    private Button undoBtn;
    private Button redoBtn;
    private Button resetBtn;
    public static Switch eraserSwitch;
    public static boolean eraserFlg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.memoCanvas);

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
