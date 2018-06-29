package com.netherfire.server.handler.imple.ios;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.netherfire.server.Global;
import com.netherfire.server.config.Config;
import com.netherfire.server.handler.BaseHandler;
import com.netherfire.server.handler.request.IOSRechargeForm;
import com.netherfire.server.handler.response.Response;
import com.netherfire.server.handler.result.IOSRechargeReturn;
import com.netherfire.server.handler.result.RestReturnVO;
import com.netherfire.server.handler.result.ResultCode;
import com.netherfire.server.utils.HttpsUtil;
import com.netherfire.server.utils.UtilsIO;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public class IosPay extends BaseHandler {
	public static Logger log = LogManager.getLogger(IosPay.class);
	@Override
		public void handlePost(ChannelHandlerContext ctx, FullHttpRequest request, boolean iskeepAlive) {
	
		try{
			ByteBuf byteBuf = request.content();
			String data = UtilsIO.byteBufToStr(byteBuf);
			
			IOSRechargeForm form = JSON.parseObject(data, IOSRechargeForm.class);
			
			asyTask.addSynTask(()->{
				log.info("IOS-1-开始验证..."+data);
				RestReturnVO vo = new RestReturnVO();
				vo.setErrorCode(ResultCode.FAIL);
				
				IOSReceiptReq req = new IOSReceiptReq();
				req.setReceipt_data(form.getReceiptData());
				
				String url = Global.getInstance().getConfig().getIosPay().getProduction();
				if(form.isSandbox()){
					log.info("IOS-2-开始验证...");
					 url = Global.getInstance().getConfig().getIosPay().getSandbox();
				}
				log.info("IOS-3-开始验证");
				String result = HttpsUtil.doPostSslJSON(url, JSON.toJSONString(req));
				log.info("IOS-4-开始验证。。。"+result);
				if(result != null){
					log.info("IOS-5-开始验证。。。"+result);
					log.info("IOS-5-1-开始验证。。。"+result);
					IOSReceiptRes receiptRes = JSON.parseObject(result, IOSReceiptRes.class);
					log.info("IOS-6-开始验证。。。"+result);
					if(receiptRes.getStatus() == Config.IOSPAYSTAT21007){
						 url = Global.getInstance().getConfig().getIosPay().getSandbox();
						 String resultData = HttpsUtil.doPostSslJSON(url, JSON.toJSONString(req));
						 log.info("IOS-6-1-开始验证。。。"+resultData);
						 receiptRes = JSON.parseObject(resultData, IOSReceiptRes.class);
					}
					log.info("IOS-7-开始验证。。。"+result);
					if(receiptRes.getStatus() == Config.IOSPAYSTAT0){
						log.info("IOS-8-开始验证。。。"+result);
						String productId = null;
						String orderIdStr = String.valueOf(form.getOrderId());
						
						for(IOSReceiptUnit unit : receiptRes.getReceipt().getIn_app()){
							   if(unit.getTransaction_id().equals(orderIdStr)){
								   productId = unit.getProduct_id();
								   break;
							   }
						}
						log.info("IOS-9-开始验证。。。"+productId);
						if(productId != null){
							IOSRechargeReturn returnVo = new IOSRechargeReturn();
							returnVo.setDeveloperPayload(orderIdStr);                      
							returnVo.setKind(receiptRes.getEnvironment());
							returnVo.setProductId(productId);
							vo.setErrorCode(ResultCode.SUCCESS);
							vo.setResult(returnVo);
						}else{
							log.info("IOS-10-开始验证。。。"+productId);
							log.error(JSON.toJSONString(receiptRes));
						}
					}else {
						log.error(JSON.toJSONString(receiptRes));
					}
				}
				log.info("IOS-11-开始验证。。。");
				FullHttpResponse response = Response.build(JSON.toJSONBytes(vo, features),iskeepAlive);
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
		        ctx.flush();
			});
		}catch(Exception e){
			log.error("", e);
		}
	}
	
	
	/*public static void main(String[] args){
		String s = "{\"status\":0, \"environment\":\"Production\", "
					+"\"receipt\":{\"receipt_type\":\"Production\", \"adam_id\":1238014182, \"app_item_id\":1238014182, \"bundle_id\":\"com.rastargames.casino\", \"application_version\":\"17\", \"download_id\":112019317630727, \"version_external_identifier\":823072261, \"receipt_creation_date\":\"2017-08-01 07:13:36 Etc/GMT\", \"receipt_creation_date_ms\":\"1501571616000\", \"receipt_creation_date_pst\":\"2017-08-01 00:13:36 America/Los_Angeles\", \"request_date\":\"2017-08-01 07:13:37 Etc/GMT\", \"request_date_ms\":\"1501571617630\", \"request_date_pst\":\"2017-08-01 00:13:37 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 05:36:44 Etc/GMT\", \"original_purchase_date_ms\":\"1501565804000\", \"original_purchase_date_pst\":\"2017-07-31 22:36:44 America/Los_Angeles\", \"original_application_version\":\"17\"," 
					+"\"in_app\":["
					+"{\"quantity\":\"1\", \"product_id\":\"casinolegends.gems.1\", \"transaction_id\":\"720000197863869\", \"original_transaction_id\":\"720000197863869\", \"purchase_date\":\"2017-08-01 05:46:14 Etc/GMT\", \"purchase_date_ms\":\"1501566374000\", \"purchase_date_pst\":\"2017-07-31 22:46:14 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 05:46:14 Etc/GMT\", \"original_purchase_date_ms\":\"1501566374000\", \"original_purchase_date_pst\":\"2017-07-31 22:46:14 America/Los_Angeles\", \"is_trial_period\":\"false\"}, "
					+"{\"quantity\":\"1\", \"product_id\":\"casinolegends.gems.1\", \"transaction_id\":\"720000197865623\", \"original_transaction_id\":\"720000197865623\", \"purchase_date\":\"2017-08-01 06:00:13 Etc/GMT\", \"purchase_date_ms\":\"1501567213000\", \"purchase_date_pst\":\"2017-07-31 23:00:13 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 06:00:13 Etc/GMT\", \"original_purchase_date_ms\":\"1501567213000\", \"original_purchase_date_pst\":\"2017-07-31 23:00:13 America/Los_Angeles\", \"is_trial_period\":\"false\"}, " 
					+"{\"quantity\":\"1\", \"product_id\":\"casinolegends.gems.2\", \"transaction_id\":\"720000197871002\", \"original_transaction_id\":\"720000197871002\", \"purchase_date\":\"2017-08-01 06:50:24 Etc/GMT\", \"purchase_date_ms\":\"1501570224000\", \"purchase_date_pst\":\"2017-07-31 23:50:24 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 06:50:24 Etc/GMT\", \"original_purchase_date_ms\":\"1501570224000\", \"original_purchase_date_pst\":\"2017-07-31 23:50:24 America/Los_Angeles\", \"is_trial_period\":\"false\"}, "
					+"{\"quantity\":\"1\", \"product_id\":\"casinolegends_chips10\", \"transaction_id\":\"720000197863745\", \"original_transaction_id\":\"720000197863745\", \"purchase_date\":\"2017-08-01 05:45:08 Etc/GMT\", \"purchase_date_ms\":\"1501566308000\", \"purchase_date_pst\":\"2017-07-31 22:45:08 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 05:45:08 Etc/GMT\", \"original_purchase_date_ms\":\"1501566308000\", \"original_purchase_date_pst\":\"2017-07-31 22:45:08 America/Los_Angeles\", \"is_trial_period\":\"false\"}, "
					+"{\"quantity\":\"1\", \"product_id\":\"casinolegends_chips10\", \"transaction_id\":\"720000197873460\", \"original_transaction_id\":\"720000197873460\", \"purchase_date\":\"2017-08-01 07:13:36 Etc/GMT\", \"purchase_date_ms\":\"1501571616000\", \"purchase_date_pst\":\"2017-08-01 00:13:36 America/Los_Angeles\", \"original_purchase_date\":\"2017-08-01 07:13:36 Etc/GMT\", \"original_purchase_date_ms\":\"1501571616000\", \"original_purchase_date_pst\":\"2017-08-01 00:13:36 America/Los_Angeles\", \"is_trial_period\":\"false\"}]}}";
	
		IOSReceiptRes receiptRes = JSON.parseObject(s, IOSReceiptRes.class);
		System.out.println(receiptRes.getEnvironment());
	
	}*/

}
