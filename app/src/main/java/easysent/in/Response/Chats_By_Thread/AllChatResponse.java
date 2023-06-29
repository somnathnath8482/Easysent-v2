package easysent.in.Response.Chats_By_Thread;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllChatResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("chats")
	private List<ChatsItem> chats;

	@SerializedName("message")
	private String message;

	public int getCode(){
		return code;
	}

	public List<ChatsItem> getChats(){
		return chats;
	}

	public String getMessage(){
		return message;
	}
}