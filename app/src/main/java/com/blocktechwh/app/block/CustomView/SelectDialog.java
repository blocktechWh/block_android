package com.blocktechwh.app.block.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by eagune on 2017/11/9.
 */

public class SelectDialog extends Dialog {

    private Button btn_save;
    private Button btn_cancle;
    private RadioGroup radioGroup;
    private TextView text_title;

    public SelectDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select);

        text_title = (TextView)findViewById(R.id.text_title);
        btn_save = (Button)findViewById(R.id.btn_save_pop);
        btn_cancle = (Button)findViewById(R.id.btn_cancle_pop);
        radioGroup = (RadioGroup)findViewById(R.id.id_redio_group);

        btn_save.setOnClickListener(mClickListener);
        btn_cancle.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }

    public void setTitleText(String text){
        text_title.setText(text);
    }

    @Override
    protected void onStop() {
        super.onStop();
        radioGroup.removeAllViews();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save_pop:
                    handle(findViewById(radioGroup.getCheckedRadioButtonId()).getTag().toString());
                    radioGroup.removeAllViews();
                    SelectDialog.this.hide();
                    break;
                case R.id.btn_cancle_pop:
                    radioGroup.removeAllViews();
                    SelectDialog.this.hide();
                    break;
            }
        }
    };

    public void handle(String string){}

    public void setChoices(HashMap<Integer,String> map){
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();

            RadioButton radioButton = new RadioButton(App.getContext());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(lp);
            radioButton.setText(entry.getValue().toString());
            radioButton.setTag(entry.getKey());
            radioGroup.addView(radioButton);
        }
    }

}
