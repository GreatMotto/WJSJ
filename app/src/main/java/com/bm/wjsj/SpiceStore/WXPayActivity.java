package com.bm.wjsj.SpiceStore;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import android.content.Context;
import android.util.Xml;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;


public class WXPayActivity  {

	private static final String TAG = "WXPayActivity";
	private DecimalFormat decimalFormat =new DecimalFormat("0");
	private Context context;
	private orderBean orderBean;
	private String ordernum;

	PayReq req;
	IWXAPI msgApi ;
	//TextView show;
	Map<String,String> resultunifiedorder;
	StringBuffer sb;



	public WXPayActivity(Context context,orderBean orderBean,String ordernum)
	{
		this.context=context;
		this.orderBean=orderBean;
		this.ordernum=ordernum;
		msgApi = WXAPIFactory.createWXAPI(context, null);
		req = new PayReq();
		sb=new StringBuffer();

		msgApi.registerApp(Constant.APP_ID);
		String packageSign = com.bm.wjsj.Utils.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
	}


	public  void sendWXPayReq() {
		//生成prepay_id
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();

	}

/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wxpay);
		show =(TextView)findViewById(R.id.editText_prepay_id);
		req = new PayReq();
		sb=new StringBuffer();
		msgApi = WXAPIFactory.createWXAPI(WXPayActivity.this, null);

		msgApi.registerApp(Constant.APP_ID);
		//生成prepay_id
		Button payBtn = (Button) findViewById(R.id.unifiedorder_btn);
		payBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
				//getPrepayId.execute();
			}
		});
		Button appayBtn = (Button) findViewById(R.id.appay_btn);
		appayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
				getPrepayId.execute();

//				genPayReq();
//
//				sendPayReq();
			}
		});


		//生成签名参数
		Button appay_pre_btn = (Button) findViewById(R.id.appay_pre_btn);
		appay_pre_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//genPayReq();
			}
		});


		String packageSign = com.bm.wjsj.Utils.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

	}

*/

	/**
	 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constant.API_KEY);


		String packageSign =  com.bm.wjsj.Utils.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("packageSign1:",packageSign);
		return packageSign;
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constant.API_KEY);

		this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign =  com.bm.wjsj.Utils.MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("packageSign2:", appSign);
		return appSign;
	}
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");


			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion2:",sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {

		private ProgressDialog dialog;


		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "提示", "正在获取支付订单...");
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			try {
				sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
				//show.setText(sb.toString());

				resultunifiedorder = result;

				genPayReq();

				sendPayReq();
			}
			catch (Exception ex){
				Log.e("postError:","^^^^^^^^^^^^^^^^^^^^^^"+ex.toString());
			}

		}

		@Override
		protected void onCancelled() {
			//Log.e("wxpay:","###########################################canceld...");
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			//String url= String.format("http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android");
			String entity = genProductArgs();

			//Log.e("orion3:", entity);

			byte[] buf = com.bm.wjsj.Utils.Util.httpPost(url, entity);

			//String content = new String(buf);
			try {
				String content = new String(buf);
				Log.e("orion4:", content);
				Map<String, String> xml = decodeXml(content);
				return xml;
			}
			catch (Exception ex){
				Log.e("code ErrorException:", "--------------------------------");
				Map<String, String> xml = decodeXml(entity);
				return xml;
			}
		}
	}



	public Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}


	private String genNonceStr() {
		Random random = new Random();
		return  com.bm.wjsj.Utils.MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}



	private String genOutTradNo() {
		Random random = new Random();
		return  com.bm.wjsj.Utils.MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}


	//
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();


			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constant.APP_ID));
			if(ordernum==null||"".equals(ordernum)) {
				packageParams.add(new BasicNameValuePair("body", "陌客-订单编号" + orderBean.ordernum));
				//packageParams.add(new BasicNameValuePair("body", "test" + orderBean.ordernum));
			}
			else{
				//packageParams.add(new BasicNameValuePair("body", "test" + ordernum));
				packageParams.add(new BasicNameValuePair("body", "陌客-订单编号" + ordernum));
			}
			packageParams.add(new BasicNameValuePair("mch_id", Constant.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			//packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
			packageParams.add(new BasicNameValuePair("notify_url", getNotify_url()));
			if(ordernum==null||"".equals(ordernum)) {
				packageParams.add(new BasicNameValuePair("out_trade_no", orderBean.ordernum));
			}else{
				packageParams.add(new BasicNameValuePair("out_trade_no", ordernum));
			}
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			//packageParams.add(new BasicNameValuePair("total_fee", "1"));
			packageParams.add(new BasicNameValuePair("total_fee", getPrice(orderBean.realpay)));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));


			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));


			String xmlstring =toXml(packageParams);
			//Log.e("orion33:", xmlstring);
			return new String(xmlstring.getBytes(),"ISO8859-1");

			//return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}


	}


	private String getNotify_url()
	{
		return Urls.HOST+"/alipay/wechatCallBack";
	}

	private String  getPrice(String sourcePrice)
	{
		String newPrice="0";
		try{

			float price = Float.parseFloat(sourcePrice)*100;
			newPrice= decimalFormat.format(price);

		}catch (Exception ex){
			return "0";
		}
		return  newPrice;
	}

	private void genPayReq() {

		req.appId = Constant.APP_ID;
		req.partnerId = Constant.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n"+req.sign+"\n\n");

		Log.e("genPayReq()sb:", sb.toString());

		Log.e("orion1:", signParams.toString());

	}
	private void sendPayReq() {


		//msgApi.registerApp(Constant.APP_ID);
		int api= msgApi.getWXAppSupportAPI();
		Log.e("beforeSend::", "****************************************"+api+","+ordernum);
		msgApi.sendReq(req);

		Log.e("afterSend::", "****************************************"+api);
	}




}

