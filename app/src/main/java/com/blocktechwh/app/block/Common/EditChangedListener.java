package com.blocktechwh.app.block.Common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

/**
 * Created by 跳跳蛙 on 2017/12/13.
 */

public class EditChangedListener implements TextWatcher {
    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置
    private final int charMaxNum = 10;
    private int maxLength;


    public EditChangedListener(int maxLength){
        this.maxLength=maxLength;
    }
    //输入文本之前的状态
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    //输入文字中的状态
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()>=maxLength){
            System.out.println("字数超过限制");
            Toast.makeText(App.getContext(),"字数超过限制",Toast.LENGTH_SHORT).show();

        }
    }

    //输入文字后的状态
    @Override
    public void afterTextChanged(Editable s) {


    }

}
