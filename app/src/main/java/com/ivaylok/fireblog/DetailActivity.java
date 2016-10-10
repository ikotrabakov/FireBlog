package com.ivaylok.fireblog;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private ImageView ivPostImage;
    private TextView tvTitle;
    private TextView tvDesc;
    private TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent keyIntent = getIntent();
        String post_key = keyIntent.getStringExtra(MainActivity.POST_KEY);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child(post_key);

        ivPostImage = (ImageView) findViewById(R.id.detail_image);
        tvTitle = (TextView) findViewById(R.id.detail_title);
        tvDesc = (TextView) findViewById(R.id.detail_desc);
        tvUser = (TextView) findViewById(R.id.detail_username);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Uri image = Uri.parse(dataSnapshot.child("image").getValue().toString());
                ivPostImage.setImageURI(image);
                tvTitle.setText(dataSnapshot.child("title").getValue().toString());
                tvDesc.setText(dataSnapshot.child("desc").getValue().toString());
                tvUser.setText(dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
