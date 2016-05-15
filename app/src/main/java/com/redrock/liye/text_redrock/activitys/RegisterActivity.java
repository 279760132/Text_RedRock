package com.redrock.liye.text_redrock.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redrock.liye.text_redrock.R;
import com.redrock.liye.text_redrock.datas.UserDatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText password;
    EditText user;
    Button lodin;
    Button register;
    private UserDatabaseHelper dbHelper;
    private SQLiteDatabase userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        password = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.account);
        register = (Button) findViewById(R.id.login);
        lodin = (Button) findViewById(R.id.register);
        dbHelper = new UserDatabaseHelper(this,"List.db",null,1);
        userDB = dbHelper.getWritableDatabase();
        register.setOnClickListener(this);
        lodin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login :
                Cursor cursor =userDB.query("User",null,null,null,null,null,null);

                if (cursor.moveToFirst()){
                    do {
                        String account_t = cursor.getString(cursor.getColumnIndex("account"));
                        String password_t = cursor.getString(cursor.getColumnIndex("password"));
                        Log.i("Redrock1", account_t);
                        Log.i("Redrock1", password_t);
                        if (account_t.equals(user.getText().toString())&& password_t.equals(password.getText().toString())){
                            Log.i("Redrock1","aa");
                            Intent intent = new Intent(RegisterActivity.this, OneActivity.class);
                            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT);
                            startActivity(intent);

                            break;
                        }
                    }while (cursor.moveToNext());
                }
                Toast.makeText(this,"你是不是没得账号哟？个人去注册瑟",Toast.LENGTH_SHORT);
                break;
            case R.id.register:


                ContentValues values = new ContentValues();
                values.put("account",user.getText().toString());
                values.put("password",password.getText().toString());
                userDB.insert("User", null, values);
                values.clear();
                break;
            default:
                break;
        }
    }
}

