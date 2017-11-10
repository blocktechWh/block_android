package com.blocktechwh.app.block.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/8.
 */
public class InputDialog extends Dialog {

    private Button btn_save;
    private Button btn_cancle;
    private EditText text_edit;
    private TextView text_title;

    public InputDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_input);

        text_edit = (EditText)findViewById(R.id.text_input);
        text_title = (TextView)findViewById(R.id.text_title);
        btn_save = (Button)findViewById(R.id.btn_save_pop);
        btn_cancle = (Button)findViewById(R.id.btn_cancle_pop);

        btn_save.setOnClickListener(mClickListener);
        btn_cancle.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }

    public void setTitleText(String text){
        text_title.setText(text);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    String text = text_edit.getText().toString().trim();
                    handle(text);
                    text_edit.setText("");
                    InputDialog.this.hide();
                    break;
                case R.id.btn_cancle_pop:
                    text_edit.setText("");
                    InputDialog.this.hide();
                    break;
            }
        }
    };

    public void handle(String string){}
}
