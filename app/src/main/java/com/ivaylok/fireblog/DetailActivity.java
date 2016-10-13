package com.ivaylok.fireblog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageView ivPostImage;
    private TextView tvTitle;
    private TextView tvDesc;
    private TextView tvUser;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent keyIntent = getIntent();
        final String post_key = keyIntent.getStringExtra(MainActivity.POST_KEY);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child(post_key);
        mAuth = FirebaseAuth.getInstance();

        ivPostImage = (ImageView) findViewById(R.id.detail_image);
        tvTitle = (TextView) findViewById(R.id.detail_title);
        tvDesc = (TextView) findViewById(R.id.detail_desc);
        tvUser = (TextView) findViewById(R.id.detail_username);
        btnRemove = (Button) findViewById(R.id.detail_remove);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = (String) dataSnapshot.child("title").getValue();
                String desc = (String) dataSnapshot.child("desc").getValue();
                String username = (String) dataSnapshot.child("username").getValue();
                String user_id =(String) dataSnapshot.child("uid").getValue();

                tvTitle.setText(title);
                tvDesc.setText(desc);
                tvUser.setText(username);

                String image_post = dataSnapshot.child("image").getValue(String.class);
                Picasso.with(DetailActivity.this).load(image_post).into(ivPostImage);

                if(mAuth.getCurrentUser().getUid().equals(user_id)){
                    btnRemove.setVisibility(View.VISIBLE);

                    btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDatabase.removeValue();
                            Intent mainIntent = new Intent(DetailActivity.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
