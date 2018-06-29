package com.netherfire.server.db.mapper;

import java.util.List;

import com.netherfire.server.db.entity.MShareUser;

public interface MShareUserMapper {
	public List<MShareUser> queryList(MShareUser params) ;

	public int update(MShareUser entity);

	public int insert(MShareUser entity);
}
