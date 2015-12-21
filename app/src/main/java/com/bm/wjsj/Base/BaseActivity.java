package com.bm.wjsj.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.Emoji.EmojiParser;
import com.bm.wjsj.Utils.Emoji.ParseEmojiMsgUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.WJSJApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangk on 2015/7/20.
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    private int x, y;
    public TextView tv_back, tv_title;
    public Dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    // 返回时关闭软键盘
    public void CloseKeyboard() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void initTitle(String title) {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_back.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // TODO Auto-generated method stub
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        CloseKeyboard();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) ev.getX();
                y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs((int) ev.getY() - y) <= Math.abs((int) ev.getX() - x)) {
                    View v = getCurrentFocus();
                    if (isShouldHideInput(v, ev)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                    x = 0;
                    y = 0;
                    return super.dispatchTouchEvent(ev);
                }
                x = 0;
                y = 0;
                break;

            default:
                break;
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //根据触摸位置计算是否隐藏输入法
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1] - 20;
            int bottom = top + v.getHeight() + 40;
            int right = left + v.getWidth() + 10000;
            if (event.getY() > top) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 跳转到其他Activity
    public void gotoOtherActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }
    // 跳转到其他Activity
    public void gotoActivity(Class<?> cls, String id) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(Constant.ID, id);
        startActivity(intent);
    }

    public void gotoTextActivity(String title, String url) {
        Intent intent = new Intent();
        intent.setClass(this, TextActivity.class);
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra(Constant.URL, url);
        startActivity(intent);
    }

    public List<ImageBean> getImageList(String[] strs, int size) {
        List<ImageBean> list = new ArrayList<>();
        if (size > strs.length) {
            size = strs.length;
        }
        ImageBean bean;
        for (int i = 0; i < size; i++) {
            bean = new ImageBean();
            bean.path = strs[i];
            list.add(bean);
        }
        return list;
    }
    public List<ImageBean> getImageBean(List<ImageBean> strs, int size) {
        List<ImageBean> list = new ArrayList<>();
        if (size > strs.size()) {
            size = strs.size();
        }
        ImageBean bean;
        for (int i = 0; i < size; i++) {
            bean = new ImageBean();
            bean.path = Urls.PHOTO+strs.get(i).path;
            bean.id=strs.get(i).id;
            bean.width=strs.get(i).width;
            bean.height=strs.get(i).height;
            bean.bannerid=strs.get(i).bannerid;
            //Log.e("bean", Urls.PHOTO+strs.get(i).path);
            list.add(bean);
        }
        return list;
    }

    public void showdialog(int gravity) {
        alertDialog = new Dialog(this, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(gravity);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }



    /**
     * 转换为Emoji表情
     * @param context 上下文
     * @param text 带有Emoji的普通文本
     * @return
     */
    public SpannableString getEmojiText(Context context, String text) {
        String unicode = EmojiParser.getInstance(context).parseEmoji(getEmojiContent(text));
        SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, unicode);
        return spannableString;
    }

    public String getEmojiContent(String str) {
        String[] content = str.split("]");
        for (int i = 0; i < content.length; i++) {
            if (content[i].contains("[")){
                content[i] = content[i].replace("[", "[e]") + "[/e]";
            }
        }
        String finalcontent = "";
        for (int i = 0; i < content.length; i++) {
            finalcontent = finalcontent + content[i];
        }
        return finalcontent;
    }

    public void gotoLoginAc(Context context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Constant.BOOLEAN, true);
        startActivityForResult(intent, requestCode);
    }

    public boolean checkStatus(Context context)
    {
        if ("1".equals(WJSJApplication.getInstance().getSp().getValue(Constant.STATUS))) {
            NewToast.show(context, "用户被禁用！", Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

}
