package com.jooketechnologies.user;





public class User implements Comparable<User>{
	public String userIp;
	public String userName;
	public String userId;
	public String userProfileImgUrl;
	public String instagramUrl;
	public String twitterUrl;
	public String facebookUrl;
	protected String jookeMode;
	
	@Override
	public int compareTo(User user) {
		return userName.compareTo(user.userName);
	}

	public User(String userIp, String userId, String userName){
		this.userIp = userIp;
		this.userId = userId;
		this.userName = userName;
	}
	
	
	public User(String userIp, String userId,String fullname,String userProfileImgUrl,String facebookUrl, String twitterUrl, String instagramUrl){
		this.userIp = userIp;
		this.userId = userId;
		updateUserProfile(fullname,userProfileImgUrl,facebookUrl, twitterUrl, instagramUrl);
	}
	
	
	public void updateUserProfile(String fullname,String userProfileImgUrl,String facebookUrl, String twitterUrl, String instagramUrl){
		this.facebookUrl = facebookUrl;
		this.userName = fullname;
		this.instagramUrl = instagramUrl;
		this.twitterUrl = twitterUrl;
		this.userProfileImgUrl=userProfileImgUrl;
	}

}