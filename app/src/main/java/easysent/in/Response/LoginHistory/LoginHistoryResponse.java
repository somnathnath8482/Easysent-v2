package easysent.in.Response.LoginHistory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginHistoryResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("history")
	private List<HistoryItem> history;

	@SerializedName("message")
	private String message;

	public int getCode(){
		return code;
	}

	public List<HistoryItem> getHistory(){
		return history;
	}

	public String getMessage(){
		return message;
	}
}