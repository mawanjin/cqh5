package com.join.papa.spread.com.join.mgps.customview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by lala on 16/8/19.
 */
public class LoadingImageView extends ImageView {

    private AnimationDrawable mAnimationDrawable;
    public LoadingImageView(Context context) {
        super(context);
        mAnimationDrawable = (AnimationDrawable) getBackground();
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnimationDrawable = (AnimationDrawable) getBackground();
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAnimationDrawable = (AnimationDrawable) getBackground();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mAnimationDrawable!=null){
            mAnimationDrawable.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mAnimationDrawable!=null){
            mAnimationDrawable.stop();
        }
    }
}
