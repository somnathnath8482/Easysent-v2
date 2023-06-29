package easysent.in.Response.getIp;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetIp{

	@SerializedName("code")
	private int code;

	@SerializedName("ips")
	private List<String> ips;

	public int getCode(){
		return code;
	}

	public List<String> getIps(){
		return ips;
	}
}