package com.example.animation_shuxing;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="MainActivicty";

    private Thread mThread;

    // 图片View
    private ImageView imageView;


    /**
     * 透明动画
     */
    public static final String ANIMA_ALPHA = "alpha";

    /**
     * 旋转动画
     */
    public static final String ANIMA_ROTATION = "rotation";

    /**
     * 沿X轴移动动画
     */
    public static final String ANIMA_TRANSLATION_X = "translationX";

    /**
     * 沿Y轴移动动画
     */
    public static final String ANIMA_TRANSLATION_Y = "translationY";

    /**
     * 沿X轴缩放动画
     */
    public static final String ANIMA_SCALE_X = "scaleX";

    /**
     * 沿Y轴缩放动画
     */
    public static final String ANIMA_SCALE_Y = "scaleY";

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        imageView=findViewById(R.id.iamgeview);
        
        setListener( R.id.alpha,
                R.id.rotation,
                R.id.translation,
                R.id.scale,
                R.id.set,
                R.id.set_xml);
    }

    /**
    @IdRes是Android中的一个注解，用于指示资源ID.
    它可以帮助开发者在编译时检测资源ID的正确性，
    从而避免由于拼写错误等原因导致的运行时崩溃。
    例如，在使用findViewById()方法获取View对象时，可以使用@IdRes指定控件的资源ID，这样就可以在编译时检查资源ID是否正确。
     */
    private void setListener(@IdRes int... i) {
        for(int item:i){
            View v=findViewById(item);
            if(v==null){
                return ;
            }
            v.setOnClickListener(this);
        }
    }

    //透明度

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alpha:
                //透明度
                ObjectAnimator animator=ObjectAnimator.ofFloat(imageView,ANIMA_ALPHA,1f,0f,1f,0f);
                animator.setDuration(5000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //这个方法可以拿到当前的值
                        float currentValue = (float) animation.getAnimatedValue();
                        Log.d("TAG", "cuurent value is " + currentValue);
                    }
                });
                animator.start();
                //无限播放
                //animator.setRepeatCount(-1);
                break;
            case R.id.rotation:
                //旋转
                ObjectAnimator animator1=ObjectAnimator.ofFloat(imageView,ANIMA_ROTATION,0f,360f,0f);
                animator1.setDuration(2000);

                animator1.start();
                break;
            case R.id.translation:
                //平移
                float x = imageView.getTranslationX();//得到的x为0.0f
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, ANIMA_TRANSLATION_Y, x, 300f, x);
                //ObjectAnimator animator2 = ObjectAnimator.ofFloat(imageView, ANIMA_TRANSLATION_X, 0f, -300f, 0f);
                animator2.setDuration(2000);
                animator2.start();
                break;
            case R.id.scale:
                //缩放
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, ANIMA_SCALE_X, 1f, 2f, 1f);
                //ObjectAnimator animator3 = ObjectAnimator.ofFloat(imageView, ANIMA_SCALE_Y, 1f, 2f, 1f);
                animator3.setDuration(2000);
                animator3.start();
                break;
            case R.id.set:

                /**
                 * 组合动画
                 * AnimatorSet这个类，这个类提供了一个play()方法，调用后将会返回一个AnimatorSet.Builder的实例，
                 * AnimatorSet.Builder中包括以下四个方法：
                 *  after(Animator anim) 将现有动画插入到传入的动画之后执行
                 *  after(long delay) 将现有动画延迟指定毫秒后执行
                 *  before(Animator anim) 将现有动画插入到传入的动画之前执行
                 *  with(Animator anim) 将现有动画和传入的动画同时执行
                 */

                //组合动画
                //沿x轴放大
                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, ANIMA_SCALE_X, 1f, 2f, 1f);
                //沿y轴放大
                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, ANIMA_SCALE_Y, 1f, 2f, 1f);
                //移动
                ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(imageView, ANIMA_TRANSLATION_X, 0f, 200f, 0f);
                //透明动画
                ObjectAnimator animator5 = ObjectAnimator.ofFloat(imageView, ANIMA_ALPHA, 1f, 0f, 1f);

                AnimatorSet set = new AnimatorSet();
                //同时沿X,Y轴放大，且改变透明度，然后移动
                //注意：after和before不能同时使用，只能选其一
                set.play(scaleXAnimator).with(scaleYAnimator).with(animator5).before(translationXAnimator);
                //都设置5s
                set.setDuration(3000);

                //添加监听事件
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //动画开始的时候调用
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //画结束的时候调用
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        //动画被取消的时候调用
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        //动画重复执行的时候调用

                    }
                });

                //另一种设置监听的方式，里面的监听方法可以选择性重写
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                });
                set.start();
                break;
            case R.id.set_xml:
                //组合动画
                //从xml加载
                //向左移动并旋转，然后回到原来的位置
                //接着向右移动并旋转，然后回到原来的位置
                Animator animator6 = AnimatorInflater.loadAnimator(this, R.animator.set);
                animator6.setTarget(imageView);
                //匀速进行
                animator6.setInterpolator(new LinearInterpolator());
                animator6.setStartDelay(1000);
                animator6.start();
        }
    }


}