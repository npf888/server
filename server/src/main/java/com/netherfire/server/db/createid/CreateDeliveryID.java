package com.netherfire.server.db.createid;

import com.netherfire.server.Global;
import com.netherfire.server.db.DeliveryDaoServer;

public class CreateDeliveryID {

private static final CreateDeliveryID info = new CreateDeliveryID();
	
	private long iD = Global.getInstance().getConfig().getDef_delivery_ID();
	
	private CreateDeliveryID(){}
	
	public static CreateDeliveryID getInstance(){
		return info;
	}
	
	public void init(){
		long maxId = DeliveryDaoServer.getDeliveryMaxId();
		if(maxId != 0){
			iD = maxId;
		}
	}
	
	public synchronized long getDeliveryMaxId(){
		return ++iD;
	}
}
