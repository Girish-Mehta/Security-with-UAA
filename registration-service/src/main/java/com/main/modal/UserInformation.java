package com.main.modal;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;

@Document(collection = "userInformation")
@ApiModel(description="All details about the user. ")
public class UserInformation {

	@Id
	private String userId;
	@Size(min=6,max=12,message="Password should be atleast of length 6 and max of length 12")
	private String password;
	
	public UserInformation() {
		super();

	}

	@Override
	public String toString() {
		return "UserInformation [userId=" + userId + ", password=" + password + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInformation(String userId,
			@Size(min = 6, max = 12, message = "Password should be atleast of length 6 and max of length 12") String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	



}
