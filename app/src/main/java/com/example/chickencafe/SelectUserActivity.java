package com.example.chickencafe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SelectUserActivity extends AppCompatActivity {

    private Button customer_btn;
    private Button admin_btn;

    public static int width = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectuser);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar));
        }

        Display display = getWindowManager().getDefaultDisplay(); Point size = new Point(); display.getSize(size);
        width = size.x;

        customer_btn = (Button) findViewById(R.id.customer_btn);
        admin_btn = (Button) findViewById(R.id.admin_btn);

        customer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
                startActivity(intent);
                 */

                AlertDialog.Builder builder = new AlertDialog.Builder(SelectUserActivity.this, R.style.AlertDialogTheme);
                builder.setTitle("배달지를 설정하세요.");
                builder.setMessage("방 번호를 입력하여 배달 장소를 설정하세요.");
                builder.setView(R.layout.dialog);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog f = (Dialog) dialog;
                                EditText input = (EditText) f.findViewById(R.id.addboxdialog);
                                String value = input.getText().toString();

                                if(!value.isEmpty() && isNumeric(value)) {
                                    //Toast.makeText(getApplicationContext(), edittext.getText().toString(), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
                                    intent.putExtra("room_number", value);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 방 번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        });

        admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);
            }
        });

    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
