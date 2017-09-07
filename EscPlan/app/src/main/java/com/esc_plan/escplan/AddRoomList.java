package com.esc_plan.escplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esc_plan.escplan.db.PrivateRoom;
import com.esc_plan.escplan.db.PublicRoom;
import com.esc_plan.escplan.db.Room;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.esc_plan.escplan.R.id.add_field;

/**
 * Created by noy on 12/08/2017.
 */

public class AddRoomList extends AppCompatActivity {
    List<PublicRoom> rooms =  MainActivity.escaper().getAllRooms();
    String[] rates = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] fields = { "תמונה" ,"תאריך", "שותפים", "זמן יציאה", "כתובת","ביקורת", "הערה"};
    private static int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;
    private String filemanagerstring;
    AutoCompleteTextView autoCompleteText;

    private PublicRoom selectedRoom = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room_list);

        autoCompleteText = (AutoCompleteTextView) findViewById(R.id.roomNameAutoComplete);
        final ArrayAdapter adapter = new AutocompleteAdapter(this, R.layout.autocomp_item, rooms);
        autoCompleteText.setAdapter(adapter);
        autoCompleteText.setThreshold(1);

        Bundle bundle = getIntent().getExtras();
        String room_name = "";
        if(bundle != null ) {
            int roomIndex = bundle.getInt(getString(R.string.ROOM_POS));

            switch (Room.Type.vals[bundle.getInt(getString(R.string.ROOM_TYPE))]) {
                case ALL:
                    selectedRoom = MainActivity.escaper().getAllRooms().get(roomIndex);
                    break;
                case TODO:
                    selectedRoom  = MainActivity.escaper().getTodoRooms().get(roomIndex);
                    break;
                case RECOMMENDED:
                    selectedRoom  = MainActivity.escaper().getRecommendedRooms().get(roomIndex);
                    break;
                case MINE:
                    return;
            }
            room_name = selectedRoom.getName();
        }

        autoCompleteText.setText(room_name);
        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRoom = (PublicRoom) adapterView.getAdapter().getItem(i);
            }
        });

        final ArrayAdapter<String> rate_adp = new ArrayAdapter<>(AddRoomList.this,
                android.R.layout.simple_spinner_item, rates);
        final Spinner rate_sp = (Spinner) findViewById(R.id.rates_spinner);
        rate_sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        rate_sp.setAdapter(rate_adp);

        Button add_btn = (Button) findViewById(R.id.add_field);
        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddRoomList.this);
                final ArrayAdapter<String> adp = new ArrayAdapter<>(AddRoomList.this,
                        android.R.layout.simple_spinner_item, fields);

                final Spinner sp = new Spinner(AddRoomList.this);
                sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                sp.setAdapter(adp);

                builder.setView(sp);
                builder.setTitle("בחר שדה");
                builder.setIcon(R.drawable.add_icon);
                builder.setPositiveButton("הוסף",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                String field = sp.getSelectedItem().toString();
                                add_new_field(field);
                                dialog.cancel();
                            }
                        });
                builder.create().show();

            }
        });

        Button save_btn = (Button) findViewById(R.id.save);
        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String rate = rate_sp.getSelectedItem().toString();
                PrivateRoom new_room = new PrivateRoom(selectedRoom, Integer.valueOf(rate));

                //add review
                TextView review_value = (TextView) findViewById(R.id.review_value_i);
                if (review_value != null) {
                    String review = review_value.getText().toString();
                    new_room.setReview(review);
                }

                //add partners
                TextView partners_value = (TextView) findViewById(R.id.partners_value_i);
                if (partners_value != null) {
                    String partners = partners_value.getText().toString();
                    new_room.setPartners(partners);
                }

                //add time
                TextView time_value = (TextView) findViewById(R.id.time_value_i);
                if (time_value != null) {
                    String time = time_value.getText().toString();
                    new_room.setTime(Integer.valueOf(time));
                }

                //add address
                TextView address_value = (TextView) findViewById(R.id.address_value_i);
                if (address_value != null) {
                    String address = address_value.getText().toString();
                    new_room.setAddress(address);
                }

                //add note
                TextView note_value = (TextView) findViewById(R.id.note_value_i);
                if (note_value != null) {
                    String note = note_value.getText().toString();
                    new_room.setNote(note);
                }

                //add date
                TextView date_value = (TextView) findViewById(R.id.my_date_i);
                if (date_value != null) {
                    String date = date_value.getText().toString();
                    new_room.setDate(date);
                }

                MainActivity.escaper().addPrivateRoom(new_room);

                //add image
                ImageView image_value = (ImageView) findViewById(R.id.img);
                if (image_value != null) {
                    MainActivity.escaper().saveImage(AddRoomList.this, new_room, image_value);
                }

                Intent i = new Intent(AddRoomList.this, MyList.class);
                startActivity(i);
                finish();

            }
        });

    }

    private void add_new_field (String field){
        if (field == "שותפים"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.partners, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.partners);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else if (field == "הערה"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.note, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.note);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (field == "ביקורת"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.review, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.review);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (field == "כתובת"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.address, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.address);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (field == "זמן יציאה"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.time, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.time);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (field == "תאריך"){
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.date, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.date);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final TextView date = (TextView)  insertPoint.findViewById(R.id.my_date_i);
            Button pickDate = (Button) insertPoint.findViewById(R.id.pick_date_btn);
            pickDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LinearLayout layout = new LinearLayout(AddRoomList.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    final DatePicker dp = new DatePicker(AddRoomList.this);
                    layout.addView(dp);

                    new AlertDialog.Builder(AddRoomList.this)
                            .setTitle("בחר תאריך")
                            .setView(layout)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String my_date =  String.valueOf(dp.getDayOfMonth())+"."+String.valueOf(dp.getMonth()+1)+"." +String.valueOf(dp.getYear());
                                    date.setText(my_date);
                                    Toast.makeText(AddRoomList.this, "Date added successfully ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AddRoomList.this, "oh no..", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });


        }
        if (field == "תמונה") {
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.image, null);

            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.image);
            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            Button pickPic = (Button) insertPoint.findViewById(R.id.pick_pic_btn);
            final ImageView iv = (ImageView)insertPoint.findViewById(R.id.img);
            pickPic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.image, null);
            ViewGroup insertPoint = (ViewGroup) findViewById(R.id.image);
            final ImageView imageView = (ImageView)insertPoint.findViewById(R.id.img);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);

        }


    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }



}

