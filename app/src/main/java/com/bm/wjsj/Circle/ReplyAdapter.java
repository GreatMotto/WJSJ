package com.bm.wjsj.Circle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ReportBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AdapterClickInterface;
import com.bm.wjsj.Utils.ViewHolder;

import java.util.List;

/**
 * 评论  adapter
 *
 */
public class ReplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReportBean> list;
    boolean isReply;//是否是回复
    boolean isMine;//是否是我的帖子
    private String postid, mainuserid;
    private int reviewposition;
    AdapterClickInterface clickInterface;
    private boolean isLongClick=false;

    public ReplyAdapter(Context context, List<ReportBean> list, boolean isReply,
                        boolean isMine, String postid, String mainuserid, int position,
                        AdapterClickInterface clickInterface) {
        super();
        this.mContext = context;
        this.list = list;
        this.isReply = isReply;//是否是用户回复我的内容
        this.isMine = isMine;// 是否是我的帖子
        this.postid = postid;
        this.mainuserid = mainuserid;
        this.reviewposition = position;
        this.clickInterface = clickInterface;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("ReplyAdapter", String.valueOf(list.size()));
        if (convertView == null) {
            convertView = View.inflate(mContext, (isReply ? R.layout.lv_item_reply : R.layout.lv_item_reply_padd), null);
        }
        isLongClick=false;
        TextView tv_reply = ViewHolder.get(convertView, R.id.tv_reply);
        final String nickname = list.get(position).nickname;
        final String content = list.get(position).content;
        final String toname = list.get(position).toname;
        final String touserid = list.get(position).touserid;
        final String userid = list.get(position).userid;
        final String id = list.get(position).id;
        String str;
        SpannableString spStr;
        BaseActivity ba = new BaseActivity();
        if (TextUtils.isEmpty(toname)) {
            str = nickname + ": " + content;
            spStr = ba.getEmojiText(mContext, str);
            spStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.theme_color));//设置文件颜色
                    ds.setUnderlineText(false);//设置下划线
                }

                @Override
                public void onClick(View widget) {
                    //评论人
//                    NewToast.show(mContext, "点击了" + nickname, Toast.LENGTH_LONG);
                    Intent intent = new Intent(mContext, MyDataActivity.class);
                    intent.putExtra(Constant.ID, userid);
                    mContext.startActivity(intent);
                }
            }, 0, nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!isReply) {
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mContext.getResources().getColor(R.color.gray));
                        ds.setUnderlineText(false);      //设置下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        if(!isLongClick) {
                            //clickInterface.onClick(widget, nickname, userid);
                            //clickInterface.getReport(widget, id);
                            //Log.e("********", "***********************1*****************************");
                        }
                    }

                }, nickname.length() + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mContext.getResources().getColor(R.color.gray));
                        ds.setUnderlineText(false);      //设置下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ReviewDetailActivity.class);
                        intent.putExtra(Constant.POSTID, postid);
                        intent.putExtra(Constant.ID, mainuserid);
                        intent.putExtra(Constant.BOOLEAN, isMine);
                        intent.putExtra("position", reviewposition);
                        mContext.startActivity(intent);
//                        clickInterface.onClick(widget, nickname, userid);
//                        clickInterface.getReport(widget, id);
                    }
                }, nickname.length() + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            str = nickname + "回复" + toname + ": " + content;
            spStr = ba.getEmojiText(mContext, str);
            spStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.theme_color));       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    //评论人
//                    NewToast.show(mContext, "点击了" + nickname, Toast.LENGTH_LONG);
                    Intent intent = new Intent(mContext, MyDataActivity.class);
                    intent.putExtra(Constant.ID, userid);
                    mContext.startActivity(intent);
                }
            }, 0, nickname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.theme_color));       //设置文件颜色
                    ds.setUnderlineText(false);      //设置下划线
                }

                @Override
                public void onClick(View widget) {
                    //被评论人
//                    NewToast.show(mContext, "点击了" + toname, Toast.LENGTH_LONG);
                    Intent intent = new Intent(mContext, MyDataActivity.class);
                    intent.putExtra(Constant.ID, touserid);
                    mContext.startActivity(intent);
                }
            }, nickname.length() + 2, nickname.length() + 2 + toname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!isReply) {
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mContext.getResources().getColor(R.color.gray));
                        ds.setUnderlineText(false);      //设置下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        if(!isLongClick) {
                            //clickInterface.onClick(widget, nickname, userid);
                            //clickInterface.getReport(widget, id);
                            //Log.e("********", "***********************2*****************************");
                        }
                    }

                }, nickname.length() + 3 + toname.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                spStr.setSpan(new ClickableSpan() {
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(mContext.getResources().getColor(R.color.gray));
                        ds.setUnderlineText(false);      //设置下划线
                    }

                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ReviewDetailActivity.class);
                        intent.putExtra(Constant.POSTID, postid);
                        intent.putExtra(Constant.ID, mainuserid);
                        intent.putExtra(Constant.BOOLEAN, isMine);
                        intent.putExtra("position", reviewposition);
                        mContext.startActivity(intent);
//                        clickInterface.onClick(widget, nickname, userid);
//                        clickInterface.getReport(widget, id);
                    }

                }, nickname.length() + 3 + toname.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tv_reply.setText(spStr);
        tv_reply.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
        tv_reply.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (clickInterface != null) {
                    isLongClick = true;
                    clickInterface.onLongClick(v, userid, id,content);
                    Log.e("********", "***********************3*****************************");
                    isLongClick = false;
                    //isLongClick = true;
                }
                return true;
            }
        });

        tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInterface != null) {
                    clickInterface.onClick(v, nickname, userid);
                    clickInterface.getReport(v, id);
                    Log.e("********", "***********************1*****************************");
                }
            }

        });
        return convertView;
    }


}
