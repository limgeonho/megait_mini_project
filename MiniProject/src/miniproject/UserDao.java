package miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
/**
 * UserDao클래스는 mariaDB에 만들어 놓은 joinDiary테이블에 데이터를 저장, 수정, 삭제, 읽어오기 등 기능을 수행하는 클래스이다.
 * mariaDB에 보내는 쿼리문들이 있다.
 * @author 임건호
 *
 */
public class UserDao {
	private static final String URL = "jdbc:mysql://127.0.0.1/testdb";
	private static final String ID = "testdba";
	private static final String PASSWORD = "test1234";

	private Connection connection;
	private PreparedStatement ps;
	private ResultSet rs;
	
	////////////////////////////////////////////////////////////////////////////////
	/**
	 * UserDao의 객체를 싱글톤 패턴으로 만든다.
	 */
	private static UserDao instance;
	private UserDao() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(URL, ID, PASSWORD);
	}

	public static UserDao getInstance() throws ClassNotFoundException, SQLException {
		if (null == instance) {
			instance = new UserDao();
		}
		return instance;
	}
	////////////////////////////////////////////////////////////////////////////////

	/**
	 * joinDiary에 select()통해 쿼리문을 보내는 메서드이다.
	 * 로그인 페이지에서 입력된 로그인 정보와 이미 저장되어 있는 로그인 정보를 비교하고 일치여부에 따라 로그인을 수행할지 다시 처음으로 돌아갈지 결정한다.
	 * 추가적으로 로그인이 성공하면 해당 아이디 데이터를 저장하고 달력페이지를 실행한다.
	 * @param id 사용자가 입력하는 아이디
	 * @param password	사용자가 입력하는 비밀번호
	 * @return 일치하는 데이터가 있으면 로그인 성공, 없으면 로그인 실패. 
	 * @throws SQLException 
	 */
	public UserVo select(String id, String password) throws SQLException {
		String sql = "SELECT * FROM joinDiary WHERE id = ? AND password = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, id);
		ps.setString(2, password);
		rs = ps.executeQuery();

		if (!rs.next()) {
			JOptionPane.showMessageDialog(null, "일치하는 정보가 없습니다.");
			new Login();
		} else {
			JOptionPane.showMessageDialog(null, "[ " + rs.getString("name") + " ] 님 환영합니다!!");
			Login.copyAddress = rs.getString("address");
			new Diary(Diary.yearSet, Diary.monthSet);
		}

		rs.close();
		ps.close();
		return null;
	}
	
	/**
	 * joinDiary에 select()통해 쿼리문을 보내는 메서드이다.
	 * 회원가입페이지에서 사용자가 입력한 아이디가 기존의 데이터에 존재하면 저장이 되지 않고 저장되어 있지 않으면 새로운 저장이 가능하다. 
	 * @param id 회원가입페이지에서 사용자가 입력한 아이디
	 * @return 데이터에 존재하면 중복확인 실패, 존재하지 않으면 중복확인 성공
	 * @throws SQLException
	 */
	public UserVo select(String id) throws SQLException {
		String sql = "SELECT * FROM joinDiary WHERE id = ?";
		ps = connection.prepareStatement(sql);
		ps.setString(1, id);
		rs = ps.executeQuery();

		if (!rs.next()) {
			JOptionPane.showMessageDialog(null, "사용가능한 아이디입니다.");
		} else {
			JOptionPane.showMessageDialog(null, "이미 등록된 아이디입니다.");
		}

		rs.close();
		ps.close();
		return null;
	}

	/**
	 * joinDiary에 insert()통해 쿼리문을 보내는 메서드이다.
	 * 회원가입페이지에서 제한사항들에 충족하는 데이터들이 최종적으로 전부 입력되면 데이터들을 uvo객체에 저장하여 joinDiary로 전달한다.
	 * @param uvo UserVo의 인자들을 가지고 있는 객체
	 * @return
	 * @throws SQLException
	 */
	public boolean insert(UserVo uvo) throws SQLException {
		String sql = "INSERT INTO joinDiary(no, id, password, name, age, gender, email, contact, address) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps = connection.prepareStatement(sql);
		ps.setString(1, uvo.getId());
		ps.setString(2, uvo.getPassword());
		ps.setString(3, uvo.getName());
		ps.setInt(4, uvo.getAge());
		ps.setString(5, uvo.getGender());
		ps.setString(6, uvo.getEmail());
		ps.setString(7, uvo.getContact());
		ps.setString(8, uvo.getAddress());
		ps.execute();

		ps.close();
		return false;
	}
}
