package com.netherfire.server.db;


import java.util.Date;

import org.apache.ibatis.session.SqlSession;

import com.netherfire.server.db.entity.Image;
import com.netherfire.server.db.factory.DataProvider;
import com.netherfire.server.db.mapper.ImageMapper;

public class ImageDaoServer {

	public static void save(String imageName){
		SqlSession sqlSession = DataProvider.getInstance().getSession();
		try {
			ImageMapper mapper = sqlSession.getMapper(ImageMapper.class);
			Image image = new Image();
			image.setCreate_time(new Date().getTime());
			image.setImage_name(imageName);
			mapper.saveImage(image);
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			sqlSession.close();
		}
	}
}
