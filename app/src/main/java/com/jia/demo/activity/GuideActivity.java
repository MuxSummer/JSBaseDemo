package com.jia.demo.activity;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jia.demo.MainActivity;
import com.jia.demo.R;
import com.jia.demo.base.BaseApplication;
import com.jia.demo.base.recyclerview.JsAbsAdapter;
import com.jia.demo.utils.SharedPreferencesUtils;

/**
 * Description: 引导页
 * Created by jia on 2017/3/28
 * 人之所以能，是相信能
 */
public class GuideActivity extends Activity {

    private RelativeLayout mRootLayout;

    private ViewPager mViewPager;

    private int colorBg[];

    private ArgbEvaluator mArgbEvaluator;

    private int barAlpha = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_guide);

        // 不是第一次打开，就直接跳转主页面
        if (!SharedPreferencesUtils.getData(GuideActivity.this, "isFirstOpen", true)) {
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            finish();
        }
        SharedPreferencesUtils.saveData(GuideActivity.this,"isFirstOpen",false);
        mRootLayout = (RelativeLayout) findViewById(R.id.rl_root);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mArgbEvaluator = new ArgbEvaluator();
        colorBg = getResources().getIntArray(R.array.splash_bg);
        final IntroPager introPager = new IntroPager(R.array.splash_icon, R.array.splash_desc);
        mViewPager.setAdapter(introPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int color = (int) mArgbEvaluator.evaluate(positionOffset, colorBg[position % colorBg.length],
                        colorBg[(position + 1) % colorBg.length]);
                mRootLayout.setBackgroundColor(color);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    /**
     * 适配器
     */
    private class IntroPager extends PagerAdapter {

        private String[] mDescs;

        private TypedArray mIcons;

        public IntroPager(int icoImage, int des) {
            mDescs = getResources().getStringArray(des);
            mIcons = getResources().obtainTypedArray(icoImage);
        }

        @Override
        public int getCount() {
            return mIcons.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemLayout = getLayoutInflater().inflate(R.layout.layout_app_intro, container, false);
            ImageView mImage = (ImageView) itemLayout.findViewById(R.id.iv_imgg);
            TextView mTextView = (TextView) itemLayout.findViewById(R.id.tv_desc);
            Button mButton = (Button) itemLayout.findViewById(R.id.btn_launch);
            mImage.setImageResource(mIcons.getResourceId(position, 0));
            mTextView.setText(mDescs[position]);
            if (position == getCount() - 1) {
                mButton.setVisibility(View.VISIBLE);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(GuideActivity.this, MainActivity.class));
                        finish();
                    }
                });
            } else {
                mButton.setVisibility(View.GONE);
            }
            container.addView(itemLayout);
            return itemLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
