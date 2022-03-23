package com.over.parkulting.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.over.parkulting.DBHelper;
import com.over.parkulting.Iris;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutPoints extends AppCompatActivity {
    SharedPreferences sPref;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView imageView;
    final int CAMERA_REQUEST = 1;

    private int roudeId;
    private String[] points;
    private Integer[] isTake;
    private View[] views;
    private int clickPoint;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.route_points);
        /*receiveIntent();
        LinearLayout scrollPoints = findViewById(R.id.scrollPoints);
        views = new View[points.length];
        for (int i = 0; i < points.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.line_point, null, false);
            TextView text = view.findViewById(R.id.pointName);

            ImageButton photoBut = view.findViewById(R.id.takePhoto);
            View.OnClickListener onClickListenerPhoto = view1 -> {
                try {
                    clickPoint = view1.getId();
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(captureIntent, CAMERA_REQUEST);
                } catch (ActivityNotFoundException e) {
                    String errorMessage = "Ваше устройство не поддерживает съемку";
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            };
            photoBut.setOnClickListener(onClickListenerPhoto);
            photoBut.setId(i);

            ImageButton mapBut = view.findViewById(R.id.location);
            mapBut.setId(i);
            View.OnClickListener onClickListenerMap = view1 -> {
                Intent intent = new Intent(this, MapsActivity.class);
                Cursor pointCursor = db.rawQuery("SELECT * FROM points_in_park where point = \"" + points[view1.getId()] + "\"", null);
                pointCursor.moveToFirst();
                intent.putExtra("point", pointCursor.getInt(0));
                pointCursor.close();
                startActivity(intent);
            };
            mapBut.setOnClickListener(onClickListenerMap);

            text.setText(points[i]);
            if (i == 0 || isTake[i - 1] == 1) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
            views[i] = view;
            scrollPoints.addView(views[i]);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap image = extras.getParcelable("data");
                String attractions = Iris.classifyImage(image, getApplicationContext());
                try {
                    SavePicture(image, "Park", attractions);
                } catch (IOException e) {
                }
                if (points[clickPoint].equals(attractions)) {
                    Toast toast = Toast.makeText(this, attractions + " ага", Toast.LENGTH_LONG);
                    Cursor pointCursor = db.rawQuery("SELECT * FROM points_in_park where point = \"" + points[clickPoint] + "\"", null);
                    pointCursor.moveToFirst();
                    String strSQL = "UPDATE user_points SET is_take=1 where point_id = " + pointCursor.getInt(0);
                    db.execSQL(strSQL);

                } else {
                    Toast toast = Toast.makeText(this, attractions, Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }
    }

    private void SavePicture(Bitmap img, String folderToSave, String name) throws IOException {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dir = new File(pictures + "/" + folderToSave);
        dir.mkdir();
        File file = new File(dir, name + "_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        img.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
    }

    public void receiveIntent() {
        connectDB();
        Intent i = getIntent();
        if (i != null) {
            roudeId = i.getIntExtra("position", 0);
        }
        upDate();
    }
    private void upDate(){
        List<String> tpoints = new ArrayList<>();
        List<Integer> tisTake = new ArrayList<>();
        Cursor pointCursor = db.rawQuery("SELECT * FROM points_in_park where park_id = " + (roudeId + 1), null);
        Cursor userCursor;
        pointCursor.moveToFirst();
        while (!pointCursor.isAfterLast()) {
            tpoints.add(pointCursor.getString(2));
            userCursor = db.rawQuery("SELECT * FROM user_points where point_id = " + pointCursor.getInt(0), null);
            userCursor.moveToFirst();
            tisTake.add(userCursor.getInt(1));
            pointCursor.moveToNext();
        }
        points = new String[tpoints.size()];
        tpoints.toArray(points);
        isTake = new Integer[tisTake.size()];
        tisTake.toArray(isTake);
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

    @Override
    protected void onResume() {
        super.onResume();
        upDate();
        for (int i = 0; i < points.length; i++) {
            if (i == 0 || isTake[i - 1] == 1) {
                views[i].setVisibility(View.VISIBLE);
            } else {
                views[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
