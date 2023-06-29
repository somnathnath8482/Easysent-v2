package easysent.in.Response.GroupChats;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetGroupChatsResponse {

	@SerializedName("code")
	private int code;

	@SerializedName("messages")
	private List<MessagesItem> messages;

	@SerializedName("message")
	private String message;

	public int getCode(){
		return code;
	}

	public List<MessagesItem> getMessages(){
		return messages;
	}

	public String getMessage(){
		return message;
	}
}