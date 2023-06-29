package easysent.in.Response.GetGroupUser;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetGroupResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("users")
	private List<UsersItem> users;

	public int getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public List<UsersItem> getUsers(){
		return users;
	}
}