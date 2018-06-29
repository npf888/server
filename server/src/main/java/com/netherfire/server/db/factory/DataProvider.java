package com.netherfire.server.db.factory;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author GuoJunWei
 *
 */
public class DataProvider {
	private static final Logger logger = LogManager.getLogger(DataProvider.class);
	
	private static final DataProvider dataProvider = new DataProvider();
	
	private String resource = "sqlMapConfig.xml";
	

	private SqlSessionFactory sqlSessionFactory;
	
	private DataProvider(){}
	
	public void init(){
           try {
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	public static DataProvider getInstance(){
		return dataProvider;
	}
	
	public SqlSession getSession(){
		return sqlSessionFactory.openSession();
	}
	
	
	

}
