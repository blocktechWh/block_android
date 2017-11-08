package com.blocktechwh.app.block.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/8.
 */
public class InputDialog extends Dialog {

    private Button btn_save;
    public EditText text_edit;

    public InputDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_input);

        text_edit = (EditText)findViewById(R.id.text_input);
        btn_save = (Button)findViewById(R.id.btn_save_pop);

        btn_save.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    String text = text_edit.getText().toString().trim();
                    handle(text);
                    break;
            }
        }
    };

    public void handle(String string){}
}
