package com.bm.wjsj.wxapi;


import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;


import com.bm.wjsj.SpiceStore.MyOrderActivity;
import com.bm.wjsj.SpiceStore.MyShopCatActivity;
import com.bm.wjsj.SpiceStore.ShopDeataisActivity;
import com.bm.wjsj.Utils.DataCleanManager;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.pay);

		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		//errCode:-2 返回键   0:完成
		Log.e(TAG, "onPayFinish, errCode = " + resp.errCode + ",getType = " + resp.getType() + ",errStr=" + resp.errStr + ",openId=" + resp.openId+",transtion="+resp.transaction+",toString()="+resp.toString());
		//NewToast.show(this, "errCode = " + resp.errCode+",getType = "+resp.getType()+",errStr="+resp.errStr+",openId="+resp.openId, Toast.LENGTH_LONG);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {//微信支付回调，返回个人中心
			SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
			String page=sp.getValue(Constant.SP_PAY_PAGE);
			String flagid= sp.getValue(Constant.SP_PAY_PRODUCTID);

			if("DETAIL".equals(page)){
				gotoShopDetailTip(resp.errCode);
				Intent intent1 = new Intent(this, ShopDeataisActivity.class);
				intent1.putExtra(Constant.ID, flagid);
				intent1.putExtra(Constant.SCORE, 0);
				startActivity(intent1);
				finish();
			}
			else if ("SHOPCAT".equals(page)) {
				gotoShopDetailTip(resp.errCode);
				Intent intent1 = new Intent(this, MyShopCatActivity.class);
				startActivity(intent1);
				finish();
			}
			else {
				Intent intent1 = new Intent(this, MainActivity.class);
				intent1.putExtra("istag", true);
				intent1.putExtra("noTog", true);
				startActivity(intent1);
				finish();
			}
			if(resp.errCode==-1)//如果支付失败，清除缓存
			{
				try {
					DataCleanManager.cleanInternalCache(this);
					DataCleanManager.cleanExternalCache(this);
				}catch (Exception ex) {
					Log.e("clearUP fail.....", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
				}
			}
		}
	}

	private void gotoShopDetailTip(int errCode)
	{
		String message="";
		if(-2==errCode){
			//NewToast.show(this, "没有订单信息！", Toast.LENGTH_LONG);
			message="支付已取消，请前往我的订单查看";
		}
		else if(0==errCode){
			//NewToast.show(this, "没有订单信息！", Toast.LENGTH_LONG);
			message="支付成功，请前往我的订单查看";
		}
		else{
			message="支付失败，请前往我的订单查看";
		}
		View layout_header = LayoutInflater.from(this).inflate(R.layout.dialog_pay, null);
		TextView tv_pay= (TextView)layout_header.findViewById(R.id.tv_pay_hint);

		tv_pay.setText(message);
		Toast t=new Toast(this);
		t.setView(layout_header);
		t.setGravity(Gravity.CENTER,0,50);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}
}