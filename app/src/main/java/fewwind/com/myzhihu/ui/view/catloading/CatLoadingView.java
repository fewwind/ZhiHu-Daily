package fewwind.com.myzhihu.ui.view.catloading;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

import fewwind.com.myzhihu.R;

/**
 * Created by Administrator on 2016/3/30.
 */
public class CatLoadingView extends RelativeLayout {

    View view;

    public CatLoadingView(Context context) {
        this(context, null);
    }

    public CatLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        view = LayoutInflater.from(context).inflate(R.layout.catloading_main, this);
        operatingAnim = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        operatingAnim.setDuration(2000);

        eye_left_Anim = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_left_Anim.setRepeatCount(Animation.INFINITE);
        eye_left_Anim.setDuration(2000);

        eye_right_Anim = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_right_Anim.setRepeatCount(Animation.INFINITE);
        eye_right_Anim.setDuration(2000);

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        eye_left_Anim.setInterpolator(lin);
        eye_right_Anim.setInterpolator(lin);


        mouse = view.findViewById(R.id.mouse);

        eye_left = view.findViewById(R.id.eye_left);

        eye_right = view.findViewById(R.id.eye_right);

        eyelid_left = (EyelidView) view.findViewById(R.id.eyelid_left);

        eyelid_left.setColor(Color.parseColor("#d0ced1"));

        eyelid_left.setFromFull(true);

        eyelid_right = (EyelidView) view.findViewById(R.id.eyelid_right);

        eyelid_right.setColor(Color.parseColor("#d0ced1"));

        eyelid_right.setFromFull(true);

        mGraduallyTextView = (GraduallyTextView) view.findViewById(
                R.id.graduallyTextView);

        operatingAnim.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }


                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }


                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        eyelid_left.resetAnimator();
                        eyelid_right.resetAnimator();
                    }
                });
    }

    public CatLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    Animation operatingAnim, eye_left_Anim, eye_right_Anim;


    View mouse, eye_left, eye_right;

    EyelidView eyelid_left, eyelid_right;

    GraduallyTextView mGraduallyTextView;


    public void startAnim() {
        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
        mGraduallyTextView.startLoading();
    }

    public void stopAnim() {

        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
        mGraduallyTextView.stopLoading();

    }

}
