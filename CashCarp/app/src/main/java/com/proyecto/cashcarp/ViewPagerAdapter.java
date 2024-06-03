package com.proyecto.cashcarp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int images[] = {
            R.drawable.foto_tutorial_1,
            R.drawable.foto_tutorial_2,
            R.drawable.foto_tutorial_3,
    };

    int titulo[] = {
            R.string.titulo_tutorial_1,
            R.string.titulo_tutorial_2,
            R.string.titulo_tutorial_3
    };

    int descripcion[] = {
            R.string.descripcion_tutorial_1,
            R.string.descripcion_tutorial_2,
            R.string.descripcion_tutorial_3
    };

    public  ViewPagerAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideTutorialImage = (ImageView) view.findViewById(R.id.tutorialImage);
        TextView slideTutorialTitle = (TextView) view.findViewById(R.id.tutorialTitle);
        TextView slideTutorialDesc = (TextView)  view.findViewById(R.id.tutorialDescription);

        slideTutorialImage.setImageResource(images[position]);
        slideTutorialTitle.setText(titulo[position]);
        slideTutorialDesc.setText(descripcion[position]);

        container.addView(view);

        return view;
    }
}
