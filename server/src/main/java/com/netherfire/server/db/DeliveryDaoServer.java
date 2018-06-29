package com.netherfire.server.db;

import org.apache.ibatis.session.SqlSession;

import com.netherfire.server.db.entity.Delivery;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.DeliveryMapper;

public class DeliveryDaoServer {


	

	
	public static Long getDeliveryMaxId(){
         SqlSession sqlSession = DataProvider.getInstance().getSession();
         DeliveryMapper mapper = sqlSession.getMapper(DeliveryMapper.class);
		 Long id = mapper.getMaxId();
		 if(id == null){
			 id = 0l;
		 }
		 sqlSession.close();
		 return id;
	}
	
}
