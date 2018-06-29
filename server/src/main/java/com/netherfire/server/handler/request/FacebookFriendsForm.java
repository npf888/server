package com.netherfire.server.handler.request;

import java.util.List;

public class FacebookFriendsForm {

	private List<Long> friendIdList;

	public List<Long> getFriendIdList() {
		return friendIdList;
	}

	public void setFriendIdList(List<Long> friendIdList) {
		this.friendIdList = friendIdList;
	}
	
	
}
