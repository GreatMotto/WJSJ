package com.bm.wjsj.Circle;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.PostDetailInfo;
import com.bm.wjsj.Bean.ReviewBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DateUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.NoScrollListview;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 评论adapter
 */
public class ReviewAdapter extends BaseAdapter implements APICallback.OnResposeListener{

    private Context mContext;
    private TextView tv_review_content;
    private List<ReviewBean> listReport;
    private boolean isMine;//是否是我的帖子
    private boolean isReply;//是否有回复
    private String postid = "";
    private String id = "";
    private PostDetailInfo postDetailInfo = new PostDetailInfo();
    private TextView tv_love;
    private TextView tv_loved;
    private ListView mListView;
    public Dialog alertDialog;
    public int pldata = -1;
    private int deletePosition=-1;

    public ReviewAdapter(Context context, List<ReviewBean> listReport, boolean isMine, String postid, String id, ListView mListView) {
        super();
        this.mContext = context;
        this.listReport = listReport;
        this.isMine = isMine;
        this.postid = postid;
        this.id = id;
//        this.isReply = isReply;
        this.mListView = mListView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listReport.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listReport.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = View.inflate(mContext, R.layout.lv_item_review, null);

            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }

        final SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        final TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        final TextView tv_sex = ViewHolder.get(convertView, R.id.tv_sex);
        tv_review_content = ViewHolder.get(convertView, R.id.tv_review_content);
        final TextView tv_morereview = ViewHolder.get(convertView, R.id.tv_morereview);
        final TextView tv_review_time = ViewHolder.get(convertView, R.id.tv_review_time);
        final RelativeLayout rl_love = ViewHolder.get(convertView, R.id.rl_love);
        final RelativeLayout rl_loved = ViewHolder.get(convertView, R.id.rl_loved);
        tv_love = ViewHolder.get(convertView, R.id.tv_love);
        tv_loved = ViewHolder.get(convertView, R.id.tv_loved);
        final NoScrollListview lv_reply = ViewHolder.get(convertView, R.id.lv_reply);
//        tv_review_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        tv_review_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //表情图
                BaseActivity ba = new BaseActivity();
                if (listReport.get(position).mainuserid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
                    ShowCopyDelDlg(tv_review_content, listReport.get(position).content, listReport.get(position).id, true,position);
                }
                else{
                    ShowCopyDelDlg(tv_review_content, listReport.get(position).content, listReport.get(position).id, false,position);
                }
                //ShowCopyDelDlg(tv_review_content,);
//                if (listReport.get(position).mainuserid.equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID))) {
//                    ShowDeleteReportDlg("提示", "是否删除这条评论", false,position);
//                    pldata = position;
//                }
                return true;
            }
        });


//        android.view.ViewGroup.LayoutParams lp = lv_reply.getLayoutParams();
//        lp.height = DisplayUtil.dip2px(mContext, 31) * 2;
//        lv_reply.setLayoutParams(lp);
        if (listReport.get(position).head.equals("")) {
            if (listReport.get(position).sex.equals("0")) {
                sdv_pic.setImageResource(R.mipmap.touxiangnan);
            } else {
                sdv_pic.setImageResource(R.mipmap.touxiangnv);
            }
        } else {
            sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + listReport.get(position).head));
        }
//        sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + listReport.get(position).head));
        tv_name.setText(listReport.get(position).nickname);
        tv_sex.setText(listReport.get(position).sex.equals("0")? "帅哥" : "美女");

        //表情图
        BaseActivity ba = new BaseActivity();
        tv_review_content.setText(ba.getEmojiText(mContext, listReport.get(position).content));

//        tv_review_content.setText(listReport.get(position).content);
        tv_review_time.setText(DateUtil.getDate(listReport.get(position).createTime));
//        tv_love.setText(listReport.get(position).praisenum);
//        tv_loved.setText(listReport.get(position).praisenum);
        if (listReport.get(position).report.size() == 0) {
            tv_morereview.setVisibility(View.GONE);
        }
        ReplyAdapter replyAdapter = new ReplyAdapter(mContext, listReport.get(position).report, true,
                isMine, postid, listReport.get(position).mainuserid, position, null);
        lv_reply.setAdapter(replyAdapter);

        if (listReport.get(position).praise.equals("0")) {
            tv_loved.setVisibility(View.GONE);
            tv_love.setVisibility(View.VISIBLE);
            tv_love.setText(listReport.get(position).praisenum);
        } else {
            tv_love.setVisibility(View.GONE);
            tv_loved.setVisibility(View.VISIBLE);
            tv_loved.setText(listReport.get(position).praisenum);
        }
        tv_love.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().addPraise(listReport.get(position).id, "1", ReviewAdapter.this, mContext);
                // 更新界面
                updateItemData(listReport.get(position));
            }
        });
        tv_loved.setOnClickListener(new View.OnClickListener() {//取消点赞
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().deletePraise(listReport.get(position).id, "1", ReviewAdapter.this, mContext);
                // 更新界面
                updateItemData(listReport.get(position));
            }
        });

        rl_love.setOnClickListener(new View.OnClickListener() {//点赞
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().addPraise(listReport.get(position).id, "1", ReviewAdapter.this, mContext);
                // 更新界面
                updateItemData(listReport.get(position));
            }
        });
        rl_loved.setOnClickListener(new View.OnClickListener() {//取消点赞
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().deletePraise(listReport.get(position).id, "1", ReviewAdapter.this, mContext);
                // 更新界面
                updateItemData(listReport.get(position));
            }
        });

        sdv_pic.setOnClickListener(new View.OnClickListener() {//跳转到个人主页
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, MyDataActivity.class);
                intent.putExtra(Constant.ID, listReport.get(position).mainuserid);
                mContext.startActivity(intent);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ReviewDetailActivity.class);
                intent.putExtra(Constant.POSTID, postid);
                intent.putExtra(Constant.ID, listReport.get(position).mainuserid);
                intent.putExtra(Constant.BOOLEAN, isMine);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1:
                    break;
                case 3:
                    NewToast.show(mContext, "点赞成功", Toast.LENGTH_LONG);
                    //((PostDetailActivity)mContext).onCallBack();
                    //WebServiceAPI.getInstance().postDetail(postid, ReviewAdapter.this, mContext);
                    //this.notifyDataSetChanged();
                    break;
                case 4:
                    NewToast.show(mContext, "点赞已取消", Toast.LENGTH_LONG);
                    //((PostDetailActivity)mContext).onCallBack();
                    //WebServiceAPI.getInstance().postDetail(postid, ReviewAdapter.this,mContext);
                    //this.notifyDataSetChanged();
                    break;
                case 66:
                    NewToast.show(mContext, "评论已删除", Toast.LENGTH_LONG);
                    if(deletePosition>-1&&deletePosition<listReport.size())
                    {
                        listReport.remove(deletePosition);
                    }
                    this.notifyDataSetInvalidated();
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }


    private String getPraisenum(String praise, String oldPrisenum) {
        int pNum = 0;
        try {

            pNum = Integer.parseInt(oldPrisenum);
            if ("0".equals(praise)) {
                pNum++;
            } else {
                pNum--;
            }
        } catch (Exception ex) {

        }
        return "" + pNum;
    }

    @SuppressLint("HandlerLeak")
    private Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            updateItem(msg.arg1);
        }

        ;
    };

    /**
     * 刷新指定item
     *
     * @param index item在listview中的位置
     */
    private void updateItem(int index) {
        if (mListView == null) {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        View view = mListView.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        //View view = mListView.getChildAt(index - visiblePosition);
        //TextView txt = (TextView) view.findViewById(R.id.tv_love);
        // 获取mDataList.set(ids, item);更新的数据
        ReviewBean data = (ReviewBean) getItem(index);
        // 重新设置界面显示数据
        //txt.setText(data.getData());
        //txt.setText(data.praisenum);

        if (data.praise.equals("0")) {
            TextView txtLove = (TextView) view.findViewById(R.id.tv_love);
            TextView txtLoved = (TextView) view.findViewById(R.id.tv_loved);
            txtLoved.setVisibility(View.GONE);
            txtLove.setVisibility(View.VISIBLE);
            txtLove.setText(data.praisenum);

        } else {
            TextView txtLove = (TextView) view.findViewById(R.id.tv_love);
            TextView txtLoved = (TextView) view.findViewById(R.id.tv_loved);
            txtLove.setVisibility(View.GONE);
            txtLoved.setVisibility(View.VISIBLE);
            txtLoved.setText(data.praisenum);

        }
        //Log.e("itemAfter:",data.praisenum+"^^praiseAfter:"+data.praise);
    }

    /**
     * update listview 单条数据
     *
     * @param item 新数据对象
     */
    public void updateItemData(ReviewBean item) {
        //Log.e("itemBefore:",item.praisenum+"^^praiseBefore:"+item.praise);
        item.praisenum = getPraisenum(item.praise, item.praisenum);
        item.praise = item.praise.equals("0") ? "1" : "0";
        Message msg = Message.obtain();
        int ids = -1;
        // 进行数据对比获取对应数据在list中的位置
        for (int i = 0; i < listReport.size(); i++) {
            if (listReport.get(i).id == item.id) {
                ids = i;
            }
        }
        msg.arg1 = ids;
        // 更新mDataList对应位置的数据
        listReport.set(ids, item);
        // handle刷新界面
        han.sendMessage(msg);

    }
    private void ShowDeleteReportDlg(String title, String toast, final boolean isClose,final int position) {
        showDialog();
        alertDialog.getWindow().setContentView(R.layout.toast_have_cancle);
        TextView tv_toast_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_toast_title);
        TextView tv_content = (TextView) alertDialog.getWindow().findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_content.setText(toast);
        tv_toast_title.setText(title);
        tv_sure.setText("删除");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                deletePosition=position;
                WebServiceAPI.getInstance().deleteReportDetailReprot(listReport.get(position).id, ReviewAdapter.this, mContext);
            }
        });
//        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                listReport.get(position).id = "";
//                if (isClose) {
//                    ReviewDetailActivity.this.finish();
//                }
//            }
//        });

    }
    private void showDialog() {
        alertDialog = new Dialog(mContext, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = ((BaseActivity)mContext).getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }
    private void ShowCopyDelDlg(final View v, final String text,final String deleteID,final boolean showDel,final int curIndex) {
        LayoutInflater mLayoutInflater = (LayoutInflater)   mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.dlg_long_press_del, null);
        // R.layout.pop为PopupWindow 的布局文件
        final PopupWindow pop = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 指定 PopupWindow 的背景

        pop.setFocusable(true);

        pop.setOutsideTouchable(true);
        //指定PopupWindow显示在你指定的view下
        int[] location = new int[2];


        // 获取当前可以看到的item位置
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        View view = mListView.getChildAt(curIndex - visiblePosition + 1);
        view.getLocationOnScreen(location);
        //Log.e("v:", "---------------------x:" + location[0] + ",y:" + location[1]);
        pop.showAtLocation(v, Gravity.TOP, location[0], location[1]);

        //showDialog();
        //alertDialog.getWindow().setContentView(R.layout.dlg_long_press_del);
        TextView tv_copy = (TextView) contentView.findViewById(R.id.tv_dal_copy);
        TextView tv_del = (TextView) contentView.findViewById(R.id.tv_dlg_del);
        if(showDel){
            tv_del.setVisibility(View.VISIBLE);
        }
        else{
            tv_del.setVisibility(View.GONE);
        }


        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(text); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                //cm.getText();//获取粘贴信息
                pop.dismiss();
            }
        });

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebServiceAPI.getInstance().deleteReportDetailReprot(listReport.get(position).id, ReviewAdapter.this, mContext);
                deletePosition=curIndex;
                WebServiceAPI.getInstance().deleteReportDetailReprot(deleteID, ReviewAdapter.this, mContext);
                pop.dismiss();
            }
        });

    }
}
