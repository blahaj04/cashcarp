package com.proyecto.cashcarp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button btnVolver, btnSaltar, btnSiguiente;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVolver = findViewById(R.id.botonVolver);
        btnVolver.setVisibility(View.INVISIBLE);
        btnSiguiente = findViewById(R.id.botonSiguiente);
        btnSaltar = findViewById(R.id.botonSaltar);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getItem(0) > 0) {
                    mSlideViewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) < 2) {
                    mSlideViewPager.setCurrentItem(getItem(+1), true);
                } else {
                    Intent i = new Intent(MainActivity.this, mainscreen.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnSaltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, mainscreen.class);
                startActivity(i);
                finish();
            }
        });

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.paginaIndicador);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

/*
        //Sexo firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("usuario")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

 */
    }

    public void setUpIndicator(int position) {
        dots = new TextView[3];
        mDotLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(33);
            dots[i].setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral30));
            mDotLayout.addView(dots[i]);
        }

        dots[position].setTextColor(getResources().getColor(com.google.android.material.R.color.material_dynamic_neutral90));


    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpIndicator(position);

            if (position > 0) {
                btnVolver.setVisibility(View.VISIBLE);
            } else {
                btnVolver.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i) {
        return mSlideViewPager.getCurrentItem() + i;
    }
}