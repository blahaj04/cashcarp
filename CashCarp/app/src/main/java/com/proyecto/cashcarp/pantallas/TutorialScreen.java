package com.proyecto.cashcarp.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.proyecto.cashcarp.R;
import com.proyecto.cashcarp.clases.ViewPagerAdapter;

public class TutorialScreen extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button btnVolver, btnSaltar, btnSiguiente;

    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_screen);

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
                    Intent i = new Intent(TutorialScreen.this, CreateAccountActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnSaltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TutorialScreen.this, CreateAccountActivity.class);
                startActivity(i);
                finish();
            }
        });

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.pageIndicator);

        viewPagerAdapter = new ViewPagerAdapter(this);

        mSlideViewPager.setAdapter(viewPagerAdapter);

        setUpIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
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
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

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
        public void onPageScrollStateChanged(int state) {}
    };

    private int getItem(int i) {
        return mSlideViewPager.getCurrentItem() + i;
    }
}
