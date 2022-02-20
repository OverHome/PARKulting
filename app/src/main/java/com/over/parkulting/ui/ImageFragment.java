package com.over.parkulting.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.over.parkulting.DBHelper;
import com.over.parkulting.R;

import java.io.IOException;

public class ImageFragment extends AppCompatActivity {
    ImageView imageView;
    TextView info, url, title;
    String titleName;
    String imgDerectory;
    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imageView = findViewById(R.id.image);
        title = findViewById(R.id.title);
        info = findViewById(R.id.info);
        url = findViewById(R.id.url);
        Intent i = getIntent();
        if (i != null) {
           titleName = i.getStringExtra("title");
           imgDerectory = i.getStringExtra("file");
        }
        imageView.setImageDrawable(Drawable.createFromPath(imgDerectory));
        title.setText(titleName);
        connectDB();
        try {
            Cursor pointCursor = db.rawQuery("SELECT * FROM points_in_park where point = \"" + titleName + "\"", null);
            pointCursor.moveToFirst();
            info.setText(pointCursor.getString(3));
            url.setText(pointCursor.getString(4));

        }catch (Exception e){

        }

    }
    public void connectDB() {
        dbHelper = new DBHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }
}