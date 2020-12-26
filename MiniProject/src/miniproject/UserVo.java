package miniproject;
/**
 * UserVo클래스는 사용자의 회원가입에 관한 인자들을 가지고 있으며 이를 객체에 담아 joinDiary테이블에 저장하는 역할을 한다.
 * @author 임건호
 *
 */
public class UserVo {
	private int no, age;
	private String id,password, name, gender, email, contact, address;
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 no값을 반환한다.
	 * @return no 사용자의 번호를 반환한다.
	 */
	public int getNo() {
		return no;
	}
	
	public void setNo(int no) {
		this.no = no;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 no값을 반환한다.
	 * @return no 사용자의 번호를 반환한다.
	 */
	public String getId() {
		return id;
	}
	/**
	 * UserVo의 객체에 인자로 id값을 입력한다.
	 * @param id 사용자의 id값.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 id값을 반환한다.
	 * @return id 사용자의 아이디를 반환한다.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * UserVo의 객체에 인자로 password값을 입력한다.
	 * @param password 사용자의 password값.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 password값을 반환한다.
	 * @return password 사용자의 비밀번호를 반환한다.
	 */
	public String getName() {
		return name;
	}
	/**
	 * UserVo의 객체에 인자로 name값을 입력한다.
	 * @param name 사용자의 name값.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 name값을 반환한다.
	 * @return name 사용자의 이름을 반환한다.
	 */
	public int getAge() {
		return age;
	}
	/**
	 * UserVo의 객체에 인자로 age값을 입력한다.
	 * @param age 사용자의 age값.
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 gender값을 반환한다.
	 * @return gender 사용자의 성별을 반환한다.
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * UserVo의 객체에 인자로 gender값을 입력한다.
	 * @param gender 사용자의 gender값.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 email값을 반환한다.
	 * @return email 사용자의 이메일을 반환한다.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * UserVo의 객체에 인자로 email값을 입력한다.
	 * @param email 사용자의 email값.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 contact값을 반환한다.
	 * @return contact 사용자의 연락처를 반환한다.
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * UserVo의 객체에 인자로 contact값을 입력한다.
	 * @param contact 사용자의 contact값.
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * UserVo의 객체의 인자로 입력되어 있는 address값을 반환한다.
	 * @return address 사용자의 주소를 반환한다.
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * UserVo의 객체에 인자로 address값을 입력한다.
	 * @param address 사용자의 address값.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
		
}
