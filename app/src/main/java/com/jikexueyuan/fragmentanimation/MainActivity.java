package com.jikexueyuan.fragmentanimation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         frameLayout= (FrameLayout) findViewById(R.id.container);
        ReplaceFragmentMethod();
    }

    //初始化view
    private void ReplaceFragmentMethod() {
        FragmentTransaction tration = getSupportFragmentManager()
                .beginTransaction();
        tration.replace(R.id.container, new MainFragment());
        tration.commit();
    }


        /**
         * 设置一个新的三维旋转的容器视图。只翻一般，然后设置新的现实内容
         *
         * @param direction
         *            一个判断机制 如果为true 则向右翻转，如果false则向左翻转
         * @param fragment
         *            传入的片段
         * @param start
         *            起始位置
         * @param end
         *            结束位置
         */
        public void applyRotation(final boolean direction, final Fragment fragment,
        final float start, final float end) {
            //获取view的中心
            final float centerX = frameLayout.getWidth() / 2.0f;
            final float centerY = frameLayout.getHeight() / 2.0f;

            //创建一个围绕Y轴旋转的动画
            final Rotate3DAnimation rotation = new Rotate3DAnimation(
                            start, end, centerX, centerY, 310.0f, true);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            //添加一个animationListener，在上半场动画执行完毕时执行下半场动画
            rotation.setAnimationListener(new DisplayNextView(direction, fragment));
            //开始执行上半场动画
            frameLayout.startAnimation(rotation);
        }

         //执行完上半部分旋转之后，设置要显示的新的View然后继续执行下半部分旋转
        private final class DisplayNextView implements Animation.AnimationListener {
            private final boolean mDirection;
            private final Fragment mfragment;

            private DisplayNextView(boolean direction, Fragment fragment) {
                mDirection = direction;
                mfragment = fragment;
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                //view.post(new Runnable()),将runnable包装成message的形式将其加到messageQueue中，这样就能在UI线程中更新view
                frameLayout.post(new SwapViews(mDirection, mfragment));
            }

            public void onAnimationRepeat(Animation animation) {
            }
        }

        // 添加要显示的新的View，并执行下半部分的旋转操作
        private final class SwapViews implements Runnable {
            private final boolean mDirection;
            private final Fragment mfragment;

            public SwapViews(boolean direction, Fragment fragment) {
                mDirection = direction;
                mfragment = fragment;
            }

            public void run() {
                final float centerX = frameLayout.getWidth() / 2.0f;
                final float centerY = frameLayout.getHeight() / 2.0f;
                Rotate3DAnimation rotation;
                FragmentTransaction tration = getSupportFragmentManager()
                        .beginTransaction();
                tration.replace(R.id.container, mfragment);
                if (mDirection) {
                    rotation = new Rotate3DAnimation(-90, 0, centerX, centerY,
                            310.0f, false);
                } else {
                    rotation = new Rotate3DAnimation(90, 0, centerX, centerY,
                            310.0f, false);
                }
                tration.commit();
                rotation.setDuration(500);
                rotation.setFillAfter(true);
                rotation.setInterpolator(new DecelerateInterpolator());
                frameLayout.startAnimation(rotation);
            }
        }
    }


