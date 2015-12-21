package com.bm.wjsj.SpiceStore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

public class AliPayActivity  {

	// 商户PID
	public static final String PARTNER = "2088911997104797";
	// 商户收款账号
	public static final String SELLER = "wanjingwl@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANOorni+Pk5abvH4p/096/2hI/fIWaKDW5QSvbkCe71DkA0fVxTUy/2MSgPQue2QAAsdtKcNbDd4Ba1dJVhx/702hr6W4spc42tMphh28O7EDWH38z9ltVUFP2MpAFbdNxTlg+eHM1kyrtvWEvy8C7Fbb5W9oUvLq3zg1YRnWU9NAgMBAAECgYEAyw73Y4tQhycnbSkbMU9oykkAEsW5iLnw79wS/B2vzOG2n4BDsQ5+Ld+bWpGy7oDKKd/z4ph0C7sv5ySHoZSsvYGB+qnVYLfk8VjmVKm6ZNOrs3GfrfvzZQ6vlVcKKXJZ0lFHVo42Nkz4Ks4bxGNevZmsU4bZgHFl8JUPqaJ67dkCQQDuKm2SzbpnbwCavUvHgJTNQYQSmnSgjIFdwf97atKZfw9G18XqabTXEQFVW6vuWSSu+G952S+1C0U4iaps9RD/AkEA44If/oUvTiLn3aXv2xlKGccXHP5OPqjb/AfXVZsusWU37Klx139de3BI4jq6ztdP+u8tKEcVHTQ821ybhMSTswJALxJxr7c7pV/aFbteM+MlBeByEx8199ltZDOIpEL7ttzXDyBsfKVB2dQBmHZS5/v0dSSjG8kiVb0RhFgpN/nDzwJAAgDGSVZg2T5Dblckqngph9qR4IZ4p+KStUBYa/+GxLcQa/v97ZjIeOq/KYa82E9a++mZKy6dB/nKw1+oWt3kMwJBAMuvVEvQOyaBwz7TTvd22wydciMMjKuJ5n+oVgs5EF86BVAtqblaArL6dvBPLqdjWd2NBPlNdEkc7CgaHayPEA0=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDTqK54vj5OWm7x+Kf9Pev9oSP3yFmig1uUEr25Anu9Q5ANH1cU1Mv9jEoD0LntkAALHbSnDWw3eAWtXSVYcf+9Noa+luLKXONrTKYYdvDuxA1h9/M/ZbVVBT9jKQBW3TcU5YPnhzNZMq7b1hL8vAuxW2+VvaFLy6t84NWEZ1lPTQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private DecimalFormat decimalFormat =new DecimalFormat("0.00");

	private Context context;
	private orderBean orderBean;

	public AliPayActivity(Context context,orderBean orderBean)
	{
		this.context=context;
		this.orderBean=orderBean;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);

					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();

					String resultStatus = payResult.getResultStatus();

					SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
					String page = sp.getValue(Constant.SP_PAY_PAGE);
					String flagid = sp.getValue(Constant.SP_PAY_PRODUCTID);
					if ("DETAIL".equals(page)) {
						/*
						// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
						if (TextUtils.equals(resultStatus, "9000")) {
							gotoShopDetailTip("支付成功，请前往我的订单查看");
							//gotoOrder();
						} else if (TextUtils.equals(resultStatus, "8000")) {
							// 判断resultStatus 为非“9000”则代表可能支付失败
							// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
							//Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
							//gotoOrder();
							gotoShopDetailTip("支付结果确认中，请前往我的订单查看");
						} else if (TextUtils.equals(resultStatus, "6001")) {//用户取消
							//gotoOrder();
							gotoShopDetailTip("支付已取消，请前往我的订单查看");
						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							//Log.e("alipay:", ">>>>>>>>>>>>>>>>>^*************>>>>>>>>>>>>>" + resultStatus);
							//Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
							//gotoOrder();
							gotoShopDetailTip("支付失败，请前往我的订单查看");
						}
						*/
						gotoShopDetailTip(resultStatus);
						gotoShopDetail(flagid);
					} else if ("SHOPCAT".equals(page)) {
						gotoShopDetailTip(resultStatus);
						gotoShopCat();
					} else {
						gotoOrder();
					}
					break;
				}
				case SDK_CHECK_FLAG: {
					Toast.makeText(context, "检查结果为：" + msg.obj,
							Toast.LENGTH_SHORT).show();
					break;
				}
				default:
					break;
			}
		}
	};

	private void gotoShopDetailTip(String resultStatus)
	{
		String message="";
		Log.e("alipay:", ">>>>>>>>>>>>>>>>>^*************>>>>>>>>>>>>>" + resultStatus);
		if (TextUtils.equals(resultStatus, "9000")) {
			message="支付成功，请前往我的订单查看";
		} else if (TextUtils.equals(resultStatus, "8000")) {
			// 判断resultStatus 为非“9000”则代表可能支付失败
			// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
			message="支付结果确认中，请前往我的订单查看";
		} else if (TextUtils.equals(resultStatus, "6001")) {//用户取消
			message="支付已取消，请前往我的订单查看";
		} else {
			// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
			message="支付失败，请前往我的订单查看";
		}
		View layout_header = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null);
		TextView tv_pay= (TextView)layout_header.findViewById(R.id.tv_pay_hint);
		tv_pay.setText(message);
		Toast t=new Toast(context);
		t.setView(layout_header);
		t.setGravity(Gravity.CENTER,0,50);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}

	private void gotoShopDetail(String flagid)
	{
		Intent intent1 = new Intent(context, ShopDeataisActivity.class);
		intent1.putExtra(Constant.ID, flagid);
		intent1.putExtra(Constant.SCORE, 0);
		context.startActivity(intent1);
	}

	private void gotoShopCat()
	{
		Intent intent1 = new Intent(context, MyShopCatActivity.class);
		context.startActivity(intent1);
	}

	private void gotoOrder()
	{
		Intent intent1 = new Intent(context, MainActivity.class);
		intent1.putExtra("istag", true);
		intent1.putExtra("noTog", true);
		context.startActivity(intent1);
	}

	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
		orderBean =(orderBean) getIntent().getSerializableExtra("orderBean");
	}
*/

	/**
	 * call alipay sdk pay. 调用SDK支付
	 *
	 */
	public void pay() {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(context)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//finish();
								}
							}).show();
			return;
		}
		// 订单
		String orderInfo = getOrderInfo("陌客-订单编号"+orderBean.ordernum, "陌客-订单编号"+orderBean.ordernum, orderBean.realpay);
		//String orderInfo = getOrderInfo("陌客-订单编号"+orderBean.ordernum, "陌客-订单编号"+orderBean.ordernum, "0.01");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity)context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 *
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask((Activity)context);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 *
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask((Activity)context);
		String version = payTask.getVersion();
		Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 *
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" +orderBean.ordernum + "\"";
		//orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + getNotify_url()
				+ "\"";
//		orderInfo += "&notify_url=" + "\"" + "http://139.196.9.230:8080/wj/service/alipay/alipayCallBack"
//				+ "\"";
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 *
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 *
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 *
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private String getNotify_url()
	{
		return Urls.HOST+"/alipay/alipayCallBack";
	}

	private String  getPrice(String sourcePrice)
	{
		String newPrice="0";
		try{

			String tempPrice= decimalFormat.format(sourcePrice);
			float price = Float.parseFloat(tempPrice);
			newPrice=""+(price*100);

		}catch (Exception ex){
			return "0";
		}
		return  newPrice;
	}

}
