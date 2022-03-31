package com.over.parkulting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.over.parkulting.tools.DBHelper;
import com.over.parkulting.R;

public class ImageInfoActivity extends AppCompatActivity {

    ImageView imageView;
    TextView info, url, title;
    FloatingActionButton floatingActionButton;
    String titleName;
    String imgDerectory;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);

        imageView = findViewById(R.id.imageView3);
        title = findViewById(R.id.textView2);
        info = findViewById(R.id.textView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        //url = findViewById(R.id.url);
        Intent i = getIntent();
        if (i != null) {
            titleName = i.getStringExtra("title");
            imgDerectory = i.getStringExtra("file");
        }
        imageView.setImageDrawable(Drawable.createFromPath(imgDerectory));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + "/sdcard/test.jpg"), "image/*");
                startActivity(intent);
            }
        });
        title.setText(titleName);
        db = DBHelper.connectDB(this);
        try {
            Cursor pointCursor = db.rawQuery("SELECT * FROM points_in_park where point = \"" + titleName + "\"", null);
            pointCursor.moveToFirst();
            info.setText(pointCursor.getString(3));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(pointCursor.getString(4)));
                    startActivity(intent);
                }
            });

        }catch (Exception e){

        }
    }
}