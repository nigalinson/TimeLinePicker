package cn.sloth.com.timepool.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author nigal
 */
public class AnimHelper {

    public static void fadeOut(View v){
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,"alpha",1,0);
        animator.setDuration(500);
        animator.start();
    }

    public static void fadeIn(View v){
        ObjectAnimator animator = ObjectAnimator.ofFloat(v,"alpha",0,1);
        animator.setDuration(500);
        animator.start();
    }
}
