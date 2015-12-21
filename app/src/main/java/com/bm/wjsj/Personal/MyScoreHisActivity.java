package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Circle.TabViewPagerAdapter;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.R;

import java.util.ArrayList;
import java.util.List;

public class MyScoreHisActivity extends BaseActivity implements APICallback.OnResposeListener {
    private ViewPager viewpager;
    private RadioGroup radioGroup;

    private TabViewPagerAdapter adapter;
    List<Fragment> fragmentsList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score_his_adapter);
        initView();
    }

    private void initView() {
        initTitle("积分记录");
        MyExchangeFragment fragment = new MyExchangeFragment();
        fragmentsList.add(fragment);
        MyGetScoreFragment pf = new MyGetScoreFragment();
        fragmentsList.add(pf);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        adapter = new TabViewPagerAdapter(getSupportFragmentManager(), fragmentsList);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    radioGroup.check(R.id.rb_one);
                } else {
                    radioGroup.check(R.id.rb_two);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // 取得该控件的实例
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 点击事件获取的选择对象
                switch (checkedId) {
                    case R.id.rb_one:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.rb_two:
                        viewpager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
