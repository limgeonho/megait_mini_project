package miniproject;
/**
 * ContentVo클래스는 사용자의 내용에 관한 인자들을 가지고 있으며 이를 객체에 담아 registerContent테이블에 저장하는 역할을 한다.
 * @author 임건호
 *
 */
public class ContentVo {
	private int no;
	private String id, content, topic, time, filepath;
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 filepath값을 반환한다.
	 * @return filepath 콘텐츠의 파일경로를 반환한다.
	 */
	public String getFilepath() {
		return filepath;
	}
	/**
	 * ContentVo의 객체에 인자로 filepath값을 입력한다.
	 * @param filepath 콘텐츠의 filepath값.
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 time값을 반환한다.
	 * @return time 콘텐츠의 시간을 반환한다.
	 */
	public String getTime() {
		return time;
	}
	/**
	 * ContentVo의 객체에 인자로 time값을 입력한다.
	 * @param time 콘텐츠의 time값.
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 topic값을 반환한다.
	 * @return topic 콘텐츠의 주제를 반환한다.
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * ContentVo의 객체에 인자로 topic값을 입력한다.
	 * @param topic 콘텐츠의 topic값.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 no값을 반환한다.
	 * @return no 콘텐츠의 번호를 반환한다.
	 */
	public int getNo() {
		return no;
	}
	/**
	 * ContentVo의 객체에 인자로 no값을 입력한다.
	 * @param no 콘텐츠의 no값.
	 */
	public void setNo(int no) {
		this.no = no;
	}
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 id값을 반환한다.
	 * @return id 콘텐츠의 아이디를 반환한다.
	 */
	public String getId() {
		return id;
	}
	/**
	 * ContentVo의 객체에 인자로 id값을 입력한다.
	 * @param id 콘텐츠의 id값.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * ContentVo의 객체의 인자로 입력되어 있는 content값을 반환한다.
	 * @return content 콘텐츠의 내용을 반환한다.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * ContentVo의 객체에 인자로 content값을 입력한다.
	 * @param content 콘텐츠의 content값.
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
