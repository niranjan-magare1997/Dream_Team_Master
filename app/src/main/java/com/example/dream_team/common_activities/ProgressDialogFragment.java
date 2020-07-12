package com.example.dream_team.common_activities;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dream_team.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {
    int pStatus = 0;
    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.progress_dialog, container);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progress);
        final ProgressBar mProgress = (ProgressBar) root.findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);
        mProgress.setSecondaryProgress(100);
        mProgress.setMax(100);
        mProgress.setProgressDrawable(drawable);
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (pStatus < 100) {
                    pStatus += 1;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            mProgress.setProgress(pStatus);
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        setCancelable(false);
        return root;
    }
}



