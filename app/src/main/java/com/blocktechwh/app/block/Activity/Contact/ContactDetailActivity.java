package com.blocktechwh.app.block.Activity.Contact;

import android.os.Bundle;
import android.widget.TextView;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/13.
 */

public class ContactDetailActivity extends TitleActivity {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Bundle bundle = this.getIntent().getExtras();

        name = bundle.getString("name");
        email = bundle.getString("email");
        phone = bundle.getString("phone");
        address = bundle.getString("address");
        sex = bundle.getString("sex");

        initTitle("详细资料");
        initView();
    }

    private void initView(){
        ((TextView)findViewById(R.id.id_user_name)).setText(name);
        ((TextView)findViewById(R.id.id_text_email)).setText(email);
        ((TextView)findViewById(R.id.id_text_phone)).setText(phone);
        ((TextView)findViewById(R.id.id_user_address)).setText(address);
        ((TextView)findViewById(R.id.id_text_sex)).setText(sex);
    }

}
