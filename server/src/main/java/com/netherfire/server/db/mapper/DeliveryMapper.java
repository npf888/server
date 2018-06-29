package com.netherfire.server.db.mapper;

import com.netherfire.server.db.entity.Delivery;

public interface DeliveryMapper {

	public Delivery getDelivery(String extraparam1);
	
	public void saveDelivery(Delivery delivery);
	
	public Long getMaxId(); 
}
