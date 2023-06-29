package easysent.in.Response.Login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{
	public void setUser(User user) {
		this.user = user;
	}

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private User user;

	public int getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public User getUser(){
		return user;
	}
}