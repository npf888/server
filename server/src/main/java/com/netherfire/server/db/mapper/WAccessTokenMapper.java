package com.netherfire.server.db.mapper;

import java.util.List;

import com.netherfire.server.db.entity.WAccessToken;

public interface WAccessTokenMapper {

	public List<WAccessToken> queryList(WAccessToken params) ;

	public int update(WAccessToken entity);

	public int insert(WAccessToken entity);

}
