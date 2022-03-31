package com.over.parkulting.activity;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.over.parkulting.R;
import com.over.parkulting.fragment.SlideFragment;

public class Intro extends AppIntro2 {

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SlideFragment.newInstance(R.layout.intro_1));
        addSlide(SlideFragment.newInstance(R.layout.intro_2));

    }

    private void loadMainActivity(){
    }

    @Override
    public void onNextPressed() {
        // Do something here
    }

    @Override
    public void onDonePressed() {
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something here
    }

}
