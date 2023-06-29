package easysent.in.Response.GetAllBlocks;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllBlockResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("chats")
	private List<Block_item> chats;

	@SerializedName("message")
	private String message;

	public int getCode(){
		return code;
	}

	public List<Block_item> getblocks(){
		return chats;
	}

	public String getMessage(){
		return message;
	}
}