package app.com.wj.bean;
/**
 * 描述：广告信息</br>
 * @author Eden Cheng</br>
 * @version 2015年4月23日 上午11:32:53
 */
public class ADInfo {
	String id = "";
	String url = "";
	String type = "";
	String content = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ADInfo() {

	}

	public ADInfo(String id, String url, String type, String content) {

		this.id = id;
		this.url = url;
		this.type = type;
		this.content = content;
	}
}
