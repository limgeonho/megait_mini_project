package miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * ContentDao클래스는 mariaDB에 만들어 놓은 registerContent테이블에 데이터를 저장, 수정, 삭제, 읽어오기 등 기능을 수행하는 클래스이다.
 * mariaDB에 보내는 쿼리문들이 있다.
 * @author 임건호
 *
 */
public class ContentDao {
	private static final String URL = "jdbc:mysql://127.0.0.1/testdb";
	private static final String ID = "testdba";
	private static final String PASSWORD = "test1234";

	private Connection connection;
	private PreparedStatement ps;
	private ResultSet rs;
	
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * ContentDao의 객체를 싱글톤 패턴으로 만든다.
	 */
	private static ContentDao instance; 
	private ContentDao() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(URL, ID, PASSWORD);
	}

	public static ContentDao getInstance() throws ClassNotFoundException, SQLException {
		if (null == instance) {
			instance = new ContentDao();
		}
		return instance;
	}
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * registerContent에 select()통해 쿼리문을 보내는 메서드이다.
	 * 입력받은 아이디와 현재시간을 통해 데이터베이스에 데이터가 존재하면 저장되어 있던 데이터를 자동으로 불러온다.
	 * @param id 사용자가 로그인한 아이디
	 * @param time 사용자가 클릭한 날짜
	 * @return 데이터베이스에 데이터가 존재하면 불러오고 존재하지 않다면 새롭게 내용작성을 요청하는 메세지를 반환한다.
	 * @throws SQLException
	 */
	public ContentVo select(String id, String time) throws SQLException {
		String sql = "SELECT content, topic, filepath FROM registerContent WHERE id = ? AND time = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, time);
		rs = ps.executeQuery();

		ContentVo cvo = new ContentVo();

		if (!rs.next()) {
			cvo.setContent("내용 작성하기");
			cvo.setTopic("제목 작성하기");
			return cvo;
		}

		cvo.setContent(rs.getString(1));
		cvo.setTopic(rs.getString(2));
		cvo.setFilepath(rs.getString(3));
		rs.close();
		ps.close();
		return cvo;
	}
	
	/**
	 * registerContent에 select()통해 쿼리문을 보내는 메서드이다.
	 * textArea, 제목입력, 사진불러오기에 각각의 데이터들이 입력되면 이를 cvo객체의 인자에 저장한 후에 
	 * registerContent테이블에 전달하여 저장한다. 
	 * @param cvo ContentVo의 인자들을 가지고 있는 객체
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(ContentVo cvo) throws SQLException {
		String sql = "INSERT INTO registerContent(no, id, content, topic, time, filepath) VALUES(DEFAULT, ?, ?, ?, ?, ?)";
		ps = connection.prepareStatement(sql);
		ps.setString(1, cvo.getId());
		ps.setString(2, cvo.getContent());
		ps.setString(3, cvo.getTopic());
		ps.setString(4, cvo.getTime());
		ps.setString(5, cvo.getFilepath());
		ps.execute();

		ps.close();
		return false;
	}

	/**
	 * registerContent에 update()통해 쿼리문을 보내는 메서드이다.
	 * 저장되어 있던 데이터들을 새로운 입력값으로 수정하는 메서드이다.
	 * @param cvo ContentVo의 인자들을 가지고 있는 객체
	 * @return
	 * @throws SQLException
	 */
	public boolean update(ContentVo cvo) throws SQLException {
		String sql = "UPDATE registerContent SET content = ?, topic = ?, filepath = ? WHERE id = ? AND time = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, cvo.getContent());
		ps.setString(2, cvo.getTopic());
		ps.setString(3, cvo.getFilepath());
		ps.setString(4, cvo.getId());
		ps.setString(5, cvo.getTime());
		ps.execute();

		ps.close();
		return false;
	}
	
	/**
	 * registerContent에 delete()통해 쿼리문을 보내는 메서드이다.
	 * 사용자의 아이디와 시간에 따라 해당 메서드가 실행되면 registerContent의 아이디와 시간에 해당하는 레코드를 삭제한다.
	 * @param cvo 사용자가 삭제하기 원하는 아이디와 날짜가 저장되어 있는 객체
	 * @return 메서드가 실행되면 해당 데이터는 전부 registerContent에서 삭제된다.
	 * @throws SQLException
	 */
	public boolean delete(ContentVo cvo) throws SQLException {
		String sql = "DELETE FROM registerContent WHERE id = ? AND time = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, cvo.getId());
		ps.setString(2, cvo.getTime());
		ps.execute();

		ps.close();
		return false;
	}
}
