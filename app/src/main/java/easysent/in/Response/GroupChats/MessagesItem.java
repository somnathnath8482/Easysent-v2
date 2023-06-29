package easysent.in.Response.GroupChats;

import easysent.in.Encription.Encripter;
import com.google.gson.annotations.SerializedName;

public class MessagesItem{

	@SerializedName("is_deleted")
	private String isDeleted;

	@SerializedName("attachment")
	private String attachment;

	@SerializedName("group_id")
	private String groupId;

	@SerializedName("sender")
	private String sender;

	@SerializedName("attachment_title")
	private String attachmentTitle;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("replay_of")
	private String replayOf;

	@SerializedName("message")
	private String message;

	@SerializedName("type")
	private String type;

	@SerializedName("seen")
	private String seen;

	public String getIsDeleted(){
		return isDeleted;
	}

	public String getAttachment(){
		return attachment;
	}

	public String getGroupId(){
		return groupId;
	}

	public String getSender(){
		return sender;
	}

	public String getAttachmentTitle(){
		return attachmentTitle;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getId(){
		return id;
	}

	public String getReplayOf(){
		return replayOf;
	}

	public String getMessage(){

		Encripter encripter = new Encripter(getSender());

		return encripter.decrepit(message);
	}

	public String getType(){
		return type;
	}

	public int getSeen(){
		return Integer.parseInt(seen==null?"1":seen);
	}
}