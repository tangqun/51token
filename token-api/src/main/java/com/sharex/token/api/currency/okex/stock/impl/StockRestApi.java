package com.sharex.token.api.currency.okex.stock.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.okex.stock.IStockRestApi;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.HttpUtil;
import com.sharex.token.api.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class StockRestApi implements IStockRestApi {

	private String secret_key;
	
	private String api_key;

	private static final String API_HOST = "www.okcoin.com";
	private static final String API_URL = "https://" + API_HOST;
	
	public StockRestApi(String api_key, String secret_key){
		this.api_key = api_key;
		this.secret_key = secret_key;
	}
	
	/**
	 * 现货行情URL
	 */
	private final String TICKER_URL = "/api/v1/ticker.do?";
	
	/**
	 * 现货市场深度URL
	 */
	private final String DEPTH_URL = "/api/v1/depth.do?";
	
	/**
	 * 现货历史交易信息URL
	 */
	private final String TRADES_URL = "/api/v1/trades.do?";
	
	/**
	 * 现货获取用户信息URL
	 */
	private final String USERINFO_URL = "/api/v1/userinfo.do?";
	
	/**
	 * 现货 下单交易URL
	 */
	private final String TRADE_URL = "/api/v1/trade.do?";
	
	/**
	 * 现货 批量下单URL
	 */
	private final String BATCH_TRADE_URL = "/api/v1/batch_trade.do";
	
	/**
	 * 现货 撤销订单URL
	 */
	private final String CANCEL_ORDER_URL = "/api/v1/cancel_order.do";
	
	/**
	 * 现货 获取用户订单URL
	 */
	private final String ORDER_INFO_URL = "/api/v1/order_info.do";
	
	/**
	 * 现货 批量获取用户订单URL
	 */
	private final String ORDERS_INFO_URL = "/api/v1/orders_info.do";
	
	/**
	 * 现货 获取历史订单信息，只返回最近七天的信息URL
	 */
	private final String ORDER_HISTORY_URL = "/api/v1/order_history.do";

	@Override
	public String ticker(String symbol) throws IOException {
		String param = "";
		if(!StringUtils.isEmpty(symbol)) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;
		}
		String result = HttpUtil.get(API_URL + TICKER_URL + param);
	    return result;
	}

	@Override
	public String depth(String symbol) throws IOException {
		String param = "";
		if(!StringUtils.isEmpty(symbol )) {
			if(!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;
		}
		String result = HttpUtil.get(API_URL + this.DEPTH_URL + param);
	    return result;
	}

	@Override
	public String trades(String symbol, String since) throws IOException {
		String param = "";
		if(!StringUtils.isEmpty(symbol )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "symbol=" + symbol;
		}
		if(!StringUtils.isEmpty(since )) {
			if (!param.equals("")) {
				param += "&";
			}
			param += "since=" + since;
		}
		String result = HttpUtil.get(API_URL + this.TRADES_URL + param);
	    return result;
	}

	@Override
	public String userinfo() throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);

		// 发送post请求
		String result = HttpUtil.post(API_URL + this.USERINFO_URL,
				StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String trade(String symbol, String type,
			String price, String amount) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(type)){
			params.put("type", type);
		}
		if(!StringUtils.isEmpty(price)){
			params.put("price", price);
		}
		if(!StringUtils.isEmpty(amount)){
			params.put("amount", amount);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);

		// 发送post请求
		String result = HttpUtil.post(API_URL + this.TRADE_URL, StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String batch_trade( String symbol, String type,
			String orders_data) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(type)){
			params.put("type", type);
		}
		if(!StringUtils.isEmpty(orders_data)){
			params.put("orders_data", orders_data);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);
		
		// 发送post请求
		String result = HttpUtil.post(API_URL + this.BATCH_TRADE_URL, StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String cancel_order(String symbol, String order_id) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(order_id)){
			params.put("order_id", order_id);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);
		
		// 发送post请求
		String result = HttpUtil.post(API_URL + this.CANCEL_ORDER_URL, StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String order_info(String symbol, String order_id) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(order_id)){
			params.put("order_id", order_id);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);
		
		// 发送post请求
		String result = HttpUtil.post(API_URL + this.ORDER_INFO_URL, StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String orders_info(String type, String symbol,
			String order_id) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(type)){
			params.put("type", type);
		}
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(order_id)){
			params.put("order_id", order_id);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);
		
		// 发送post请求
		String result = HttpUtil.post(API_URL + this.ORDERS_INFO_URL, StringUtil.toQueryString(params));

		return result;
	}

	@Override
	public String order_history(String symbol, String status,
			String current_page, String page_length) throws IOException, NoSuchAlgorithmException {
		// 构造参数签名
		Map<String, String> params = new HashMap<String, String>();
		params.put("api_key", api_key);
		if(!StringUtils.isEmpty(symbol)){
			params.put("symbol", symbol);
		}
		if(!StringUtils.isEmpty(status)){
			params.put("status", status);
		}
		if(!StringUtils.isEmpty(current_page)){
			params.put("current_page", current_page);
		}
		if(!StringUtils.isEmpty(page_length)){
			params.put("page_length", page_length);
		}
		String sign = CryptoUtil.md5(StringUtil.toQueryString(params) + "&secret_key=" + this.secret_key);
		params.put("sign", sign);

		// 发送post请求
		String result = HttpUtil.post(API_URL + this.ORDER_HISTORY_URL, StringUtil.toQueryString(params));

		return result;
	}

}
