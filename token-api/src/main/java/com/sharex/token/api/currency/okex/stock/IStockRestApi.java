package com.sharex.token.api.currency.okex.stock;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


/**
 * 现货行情，交易 REST API
 * @author zhangchi
 *
 */
public interface IStockRestApi {

	/**
	 * 行情
	 * @param symbol btc_usd:比特币    ltc_usd :莱特币
	 * @return
	 * @throws IOException
	*/
    public String ticker(String symbol) throws IOException;

	/**
	 * 市场深度
	 * @param symbol btc_usd:比特币    ltc_usd :莱特币
	 * @return
	 * @throws IOException
	*/
	public String depth(String symbol) throws IOException;

	/**
	 * 现货历史交易信息
	 * @param symbol btc_usd:比特币    ltc_usd :莱特币
	 * @param since 不加since参数时，返回最近的60笔交易
	 * @return
	 * @throws IOException
	*/
	public String trades(String symbol, String since) throws IOException;

	/**
	 * 获取用户信息
	 * @return
	 * @throws IOException
	*/
	public String userinfo() throws IOException, NoSuchAlgorithmException;

	/**
	 * 下单交易
	 * @param symbol btc_usd: 比特币 ltc_usd: 莱特币
	 * @param type 买卖类型： 限价单（buy/sell） 市价单（buy_market/sell_market）
	 * @param price 下单价格 [限价买单(必填)： 大于等于0，小于等于1000000 | 
	 *                       市价买单(必填)： BTC :最少买入0.01个BTC 的金额(金额>0.01*卖一价) / LTC :最少买入0.1个LTC 的金额(金额>0.1*卖一价)]
	 *                       
	 * @param amount 交易数量 [限价卖单（必填）：BTC 数量大于等于0.01 / LTC 数量大于等于0.1 | 
	 *                        市价卖单（必填）： BTC :最少卖出数量大于等于0.01 / LTC :最少卖出数量大于等于0.1]
	 * @return
	 * @throws IOException
	*/
	public String trade(String symbol, String type,
                        String price, String amount) throws IOException, NoSuchAlgorithmException;

	/**
	 * 批量下单
	 * @param symbol   btc_usd: 比特币 ltc_usd: 莱特币
	 * @param type  买卖类型： 限价单（buy/sell） 市价单（buy_market/sell_market）
	 * @param orders_data JSON类型的字符串 例：[{price:3,amount:5},{price:3,amount:3}]
	 *        最大下单量为5，price和amount参数参考trade接口中的说明
	 * @return
	 * @throws IOException
	*/
	public String batch_trade(String symbol, String type,
                              String orders_data) throws IOException, NoSuchAlgorithmException;

	/**
	 * 撤销订单
	 * @param symbol  btc_usd: 比特币 ltc_usd: 莱特币
	 * @param order_id 订单ID(多个订单ID中间以","分隔,一次最多允许撤消3个订单)
	 * @return
	 * @throws IOException
	*/
	public String cancel_order(String symbol, String order_id) throws IOException, NoSuchAlgorithmException;

	/**
	 * 获取用户的订单信息
	 * @param symbol  btc_usd: 比特币 ltc_usd: 莱特币
	 * @param order_id  订单ID(-1查询全部订单，否则查询相应单号的订单)
	 * @return
	 * @throws IOException
	*/
	public String order_info(String symbol, String order_id) throws IOException, NoSuchAlgorithmException;

	/**
	 * 批量获取用户订单
	 * @param type  查询类型 0:未成交，未成交 1:完全成交，已撤销
	 * @param symbol  btc_usd: 比特币 ltc_usd: 莱特币
	 * @param order_id  订单ID(多个订单ID中间以","分隔,一次最多允许查询50个订单)
	 * @return
	 * @throws IOException
	*/
	public String orders_info(String type, String symbol,
                              String order_id) throws IOException, NoSuchAlgorithmException;

	/**
	 * 获取历史订单信息，只返回最近七天的信息
	 * @param symbol btc_usd: 比特币 ltc_usd: 莱特币
	 * @param status 委托状态: 0：未成交 1：已完成(最近七天的数据)
	 * @param current_page  当前页数
	 * @param page_length  每页数据条数，最多不超过200
	 * @return
	 * @throws IOException
	*/
	public String order_history(String symbol, String status,
                                String current_page, String page_length) throws IOException, NoSuchAlgorithmException;

}
