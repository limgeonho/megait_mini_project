package miniproject;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
/**
 * Join클래스는 로그인페이지에서 회원가입버튼을 눌렀을 때 나타나는 페이지이다.
 * 아이디, 비밀번호, 이름, 성별, 연락처, 나이, 주소, 이메일을 입력하는 창들이 있다.
 * 각각의 항목은 미리 설정해 놓은 제한사항에 통과하면 입력된 데이터들이 데이터베이스에 저장되지만 통과하지 못하면 해당사유와 함께 데이터베이스에 저장되지 않고 사용자에게 반환된다.
 * @author 임건호
 *
 */
public class Join extends JFrame implements CheckJoin{
	private JLabel titleLabel;
	
	private JLabel idLabel, pwLabel, pwCheckLabel, nameLabel, genderLabel, addressLabel, contactLabel, ageLabel, emailLabel;
	
	private JTextField idTextField, pwTextField, pwCheckTextField, nameTextField, contactTextField, ageTextField, emailTextField;
	
	private JLabel idMessage, pwMessage, nameMessage, genderMessage, contactMessage, ageMessage, emailMessage;
	
	private JRadioButton maleButton, femaleButton, seoulButton, busanButton, daejeonButton, daeguButton, ulsanButton, incheonButton, gwangjuButton;
	
	private JButton joinBtn, cancelBtn, pwCheckBtn, containBtn;
	
	private JPanel southPanel;
	
	private String gender, id, address;
	
	private UserVo uvo = new UserVo();
	private UserDao dao = null;

	private BufferedImage background = null;
	
	private ButtonGroup genderBtnGroup, addressBtnGroup;

	/////////////////////////////////////////////////////////////////////////////////////////  
	/**
	 * ActionListener의 listener1을 통해서 입력된 버튼들의 메서드를 실행한다.
	 */
	ActionListener listener1 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();

			/**
			 * pwCheckBtn(비밀번호 확인)버튼이 눌리면 다음과 같이 동작
			 * pwTextField에 입력된 텍스트를 pwCheckTextField에 입력된 텍스트와 문자열 비교를 해서 return한다.
			 * @return 일치하면 '비밀번호가 일치합니다.' + pwCheckBtn을 더이상 작동하지 못하게 하고 일치하지 않으면 '비밀번호가 일치하지 않습니다.'를 반환한다.
			 */
			if (button == pwCheckBtn) {
				if (pwTextField.getText().equals(pwCheckTextField.getText())) {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치합니다.");
					pwCheckBtn.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
				}
			}

			/**
			 * cancelBtn(취소)버튼이 눌리면 다음과 같이 동작
			 * 현재 페이지를 종료하고 다시 로그인페이지를 동작시킴.
			 */
			if (button == cancelBtn) {
				dispose();
				new Login();
			}
			
			/**
			 * joinBtn(회원가입)버튼이 눌리면 다음과 같이 동작
			 * 아래의 조건들을 모두 충족시켰을 때 최종적으로 입력된 데이터들을 해당 데이터베이스에 저장하고 페이지를 닫은 후에 다시 로그인페이지를 동작시킨다.
			 */
			if (button == joinBtn) {
				try {
					dao = UserDao.getInstance();
										
						/**
						 * 아이디가 해당 제한사항을 충족하면 UserVo의 객체에 id값을 저장하고 다음단계로 넘어간다.
						 */
						if(!idTextField.getText().matches(CheckJoin.ID_REGEX)) {
							JOptionPane.showMessageDialog(null, "아이디는 4~20자리 영문 + 숫자로 입력하세요.");
							return;
						}else {
							uvo.setId(idTextField.getText());
						}
						
						/**
						 * 비밀번호가 해당 제한사항을 충족하면 UserVo의 객체에 password값을 저장하고 다음단계로 넘어간다.
						 */
						if(!pwTextField.getText().matches(CheckJoin.PASSWORD_REGEX)) {
							JOptionPane.showMessageDialog(null, "비밀번호는 4~20자리 영문(대) + 영문(소) + 숫자 + 특수문자로 입력하세요.");
							return;
						}else {
							uvo.setPassword(pwTextField.getText());
						}
						
						/**
						 * 이름이 해당 제한사항을 충족하면 UserVo의 객체에 name값을 저장하고 다음단계로 넘어간다.
						 */
						if(!nameTextField.getText().matches(CheckJoin.NAME_REGEX)) {
							JOptionPane.showMessageDialog(null, "이름은 한글로 입력하세요.");
							return;
						}else {
							uvo.setName(nameTextField.getText());
						}
						
						/**
						 * 주소가 해당 제한사항을 충족하면 UserVo의 객체에 address값을 저장하고 다음단계로 넘어간다.
						 */
						uvo.setAddress(address);
						
						/**
						 * 연락처가 해당 제한사항을 충족하면 UserVo의 객체에 contact값을 저장하고 다음단계로 넘어간다.
						 */
						if(!contactTextField.getText().matches(CheckJoin.TEL_REGEX)) {
							JOptionPane.showMessageDialog(null, "연락처는 000-0000-0000형식으로 입력하세요.");
							return;
						}else {
							uvo.setContact(contactTextField.getText());
						}
						
						/**
						 * 나이가 해당 제한사항을 충족하면 UserVo의 객체에 age값을 저장하고 다음단계로 넘어간다.
						 */
						if(!ageTextField.getText().matches(CheckJoin.AGE_REGEX)) {
							JOptionPane.showMessageDialog(null, "나이는 1~2자리 숫자로 입력하세요.");
							return;
						}else {
							uvo.setAge(Integer.parseInt(ageTextField.getText()));
						}
						
						/**
						 * 성별이 해당 제한사항을 충족하면 UserVo의 객체에 gender값을 저장하고 다음단계로 넘어간다.
						 */
						uvo.setGender(gender);
						
						/**
						 * 이메일이 해당 제한사항을 충족하면 UserVo의 객체에 email값을 저장하고 다음단계로 넘어간다.
						 */
						if(!emailTextField.getText().matches(CheckJoin.MAIL_REGEX)) {
							JOptionPane.showMessageDialog(null, "이메일은 0000@0000.000형식으로 입력하세요.");
							return;
						}else {
							uvo.setEmail(emailTextField.getText());
						}
						
						/**
						 * pwCheckBtn이 성공적으로 실행되어서 더이상 작동할 수 없는 상태이면 다음단계로 넘어간다.
						 */
						if (pwCheckBtn.isEnabled()) {
							JOptionPane.showMessageDialog(null, "'비밀번호 확인'버튼을 먼저 체크해주세요.");
							return;
						}
					/**
					 * 최종적으로 insert()메서드에 지금까지 저장된 UserVo의 객체인 uvo를 입력받아 데이터베이스에 데이터를 저장하는 절차를 밟는다.
					 * @param uvo는 UserVo의 객체로 위에서 회원가입에 필요한 정보들을 uvo의 인자로 저장한뒤 insert()메서드의 매개변수로 활용한다.
					 */
					dao.insert(uvo);
					
					JOptionPane.showMessageDialog(null, "회원가입 완료!");
					dispose();
					new Login();
					
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			/**
			 * containBtn(중복확인)버튼이 눌리면 다음과 같이 동작
			 * 현재 입력된 아이디와 데이터베이스에 입력된 아이디를 비교하여 return한다.
			 * @return 이미 존재하면 '이미 등록된 사용자입니다.'를 반환하고 존재하지 않으면 '등록가능한 아이디 입니다.'를 반환한다.
			 * @param id 해당 id는 사용자가 idTextField에 입력한 텍스트이다.
			 */
			if(button == containBtn) {
				try {
					dao = UserDao.getInstance();
					id = idTextField.getText();
					
					dao.select(id);
				
				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				} 
			}
			
		}
	};
	/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * ActionListener의 listener2을 통해서 입력된 버튼들의 메서드를 실행한다.
	 */
	ActionListener listener2 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton button = (JRadioButton) e.getSource();
			/**
			 * 눌려진 버튼에 따라 gender에는 '남자' 혹은 '여자'가 입력되고
			 * address에는 '서울', '부산', '인천', '대구', '대전', '광주', '울산'중 1개가 입력된다.
			 */
			if (button == maleButton) {
				gender = "남자";
			}

			if (button == femaleButton) {
				gender = "여자";
			}
			
			if (button == seoulButton) {
				address = "서울";
			}
			
			if (button == busanButton) {
				address = "부산";
			}	

			if (button == incheonButton) {
				address = "인천";
			}

			if (button == daeguButton) {
				address = "대구";
			}

			if (button == daejeonButton) {
				address = "대전";
			}

			if (button == gwangjuButton) {
				address = "광주";
			}

			if (button == ulsanButton) {
				address = "울산";
			}
		}
	};
	/////////////////////////////////////////////////////////////////////////////////////////   
	/**
	 * 회원가입창에 나타나는 Label들을 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewLabel() {
		titleLabel = new JLabel("회원가입", JLabel.CENTER);
		titleLabel.setFont(new Font("고딕", Font.BOLD, 30));
		titleLabel.setBounds(7, 50, 750, 100);
		add(titleLabel);

		idLabel = new JLabel("아이디", JLabel.RIGHT);
		idLabel.setBounds(150, 150, 100, 40);
		add(idLabel);

		pwLabel = new JLabel("비밀번호", JLabel.RIGHT);
		pwLabel.setBounds(150, 190, 100, 40);
		add(pwLabel);

		pwCheckLabel = new JLabel("비밀번호 확인", JLabel.RIGHT);
		pwCheckLabel.setBounds(150, 230, 100, 40);
		add(pwCheckLabel);

		nameLabel = new JLabel("이름", JLabel.RIGHT);
		nameLabel.setBounds(150, 270, 100, 40);
		add(nameLabel);

		genderLabel = new JLabel("성별", JLabel.RIGHT);
		genderLabel.setBounds(150, 310, 100, 40);
		add(genderLabel);

		addressLabel = new JLabel("주소", JLabel.RIGHT);
		addressLabel.setBounds(150, 340, 100, 40);
		add(addressLabel);

		contactLabel = new JLabel("연락처", JLabel.RIGHT);
		contactLabel.setBounds(150, 380, 100, 40);
		add(contactLabel);

		ageLabel = new JLabel("나이", JLabel.RIGHT);
		ageLabel.setBounds(150, 420, 100, 40);
		add(ageLabel);

		emailLabel = new JLabel("이메일", JLabel.RIGHT);
		emailLabel.setBounds(150, 460, 100, 40);
		add(emailLabel);
	}
	/**
	 * 회원가입창에 나타나는 TextField들을 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewTextField() {
		idTextField = new JTextField();
		idTextField.setBounds(270, 150, 200, 40);
		add(idTextField);

		pwTextField = new JPasswordField();
		pwTextField.setBounds(270, 190, 200, 40);
		add(pwTextField);

		pwCheckTextField = new JPasswordField();
		pwCheckTextField.setBounds(270, 230, 200, 40);
		add(pwCheckTextField);

		nameTextField = new JTextField();
		nameTextField.setBounds(270, 270, 200, 40);
		add(nameTextField);

		contactTextField = new JTextField();
		contactTextField.setBounds(270, 380, 200, 40);
		add(contactTextField);

		ageTextField = new JTextField();
		ageTextField.setBounds(270, 420, 200, 40);
		add(ageTextField);

		emailTextField = new JTextField();
		emailTextField.setBounds(270, 460, 200, 40);
		add(emailTextField);
	}
	/**
	 * 회원가입창에 나타나는 Label들을 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewMessage() {
		idMessage = new JLabel("* 영문 + 숫자", JLabel.LEFT);
		idMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		idMessage.setBounds(490, 150, 80, 40);
		add(idMessage);

		pwMessage = new JLabel("* 영문 + 숫자 + 특수문자", JLabel.LEFT);
		pwMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		pwMessage.setBounds(490, 190, 180, 40);
		add(pwMessage);

		nameMessage = new JLabel("* 한글로 입력", JLabel.LEFT);
		nameMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		nameMessage.setBounds(490, 270, 180, 40);
		add(nameMessage);

		genderMessage = new JLabel("* 반드시 선택", JLabel.LEFT);
		genderMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		genderMessage.setBounds(490, 310, 180, 40);
		add(genderMessage);

		contactMessage = new JLabel("* 000-0000-0000", JLabel.LEFT);
		contactMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		contactMessage.setBounds(490, 380, 180, 40);
		add(contactMessage);

		ageMessage = new JLabel("* 숫자로 입력", JLabel.LEFT);
		ageMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		ageMessage.setBounds(490, 420, 180, 40);
		add(ageMessage);

		emailMessage = new JLabel("* 이메일은 0000@0000.000로 입력", JLabel.LEFT);
		emailMessage.setFont(new Font("고딕", Font.ITALIC, 13));
		emailMessage.setBounds(490, 460, 220, 40);
		add(emailMessage);
	}
	/**
	 * 회원가입창에 나타나는 Button들을 보여준다.
	 * 위치, 글꼴, 크기, actionListener를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewButton() {
		containBtn = new JButton("중복확인");
		containBtn.setBounds(580, 150, 100, 40);
		add(containBtn);
		containBtn.addActionListener(listener1);
		
		pwCheckBtn = new JButton("비밀번호 확인");
		pwCheckBtn.setBounds(490, 230, 130, 40);
		add(pwCheckBtn);
		pwCheckBtn.addActionListener(listener1);
		
		genderBtnGroup = new ButtonGroup();
		maleButton = new JRadioButton("남자");
		femaleButton = new JRadioButton("여자");
		maleButton.setBounds(270, 310, 95, 40);
		femaleButton.setBounds(365, 310, 95, 40);
		add(maleButton);
		add(femaleButton);
		maleButton.addActionListener(listener2);
		femaleButton.addActionListener(listener2);
		maleButton.setOpaque(false);
		femaleButton.setOpaque(false);
		genderBtnGroup.add(maleButton);
		genderBtnGroup.add(femaleButton);
	
		addressBtnGroup = new ButtonGroup();
		seoulButton = new JRadioButton("서울");
		busanButton = new JRadioButton("부산");
		incheonButton = new JRadioButton("인천");
		daeguButton = new JRadioButton("대구");
		daejeonButton = new JRadioButton("대전");
		gwangjuButton = new JRadioButton("광주");
		ulsanButton = new JRadioButton("울산");
		
		seoulButton.setBounds(270, 340, 60, 40);
		busanButton.setBounds(330, 340, 60, 40);
		incheonButton.setBounds(390, 340, 60, 40); 
		daeguButton.setBounds(450, 340, 60, 40); 
		daejeonButton.setBounds(510, 340, 60, 40); 
		gwangjuButton.setBounds(570, 340, 60, 40); 
		ulsanButton.setBounds(630, 340, 60, 40); 
		
		add(seoulButton);
		add(busanButton);
		add(incheonButton);
		add(daeguButton);
		add(daejeonButton);
		add(gwangjuButton);
		add(ulsanButton);
		
		seoulButton.addActionListener(listener2);
		busanButton.addActionListener(listener2);
		incheonButton.addActionListener(listener2);
		daeguButton.addActionListener(listener2);
		daejeonButton.addActionListener(listener2);
		gwangjuButton.addActionListener(listener2);
		ulsanButton.addActionListener(listener2);
		
		seoulButton.setOpaque(false);
		busanButton.setOpaque(false);
		incheonButton.setOpaque(false);
		daeguButton.setOpaque(false);
		daejeonButton.setOpaque(false);
		gwangjuButton.setOpaque(false);
		ulsanButton.setOpaque(false);
		
		addressBtnGroup.add(seoulButton);
		addressBtnGroup.add(busanButton);
		addressBtnGroup.add(incheonButton);
		addressBtnGroup.add(daeguButton);
		addressBtnGroup.add(daejeonButton);
		addressBtnGroup.add(gwangjuButton);
		addressBtnGroup.add(ulsanButton);
	}
	/**
	 * 회원가입창에 하단부에 나타나는 Panel을 보여준다.
	 * 버튼의 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewSouthPanel() {
		southPanel = new JPanel();
		joinBtn = new JButton("회원가입");
		cancelBtn = new JButton("취소");
		southPanel.setBounds(150, 530, 450, 40);
		southPanel.add(joinBtn);
		southPanel.add(cancelBtn);
		joinBtn.addActionListener(listener1);
		cancelBtn.addActionListener(listener1);
		add(southPanel);
		southPanel.setOpaque(false);
	}
	/**
	 * 회원가입 페이지의 생성자로서
	 * viewLabel(), viewTextField(), viewButton(), viewMessage(), viewSouthPanel()의 메서드를 실행하고 
	 * 회원가입창의 배경화면 이미지를 설정해서 사용자에게 보여준다.
	 */
	public Join() {
		super("회원가입 창");
		setSize(750, 700);
		setLayout(null);
		setLocationRelativeTo(null);

		viewLabel();
		viewTextField();
		viewButton();
		viewMessage();
		viewSouthPanel();

		try {
			background = ImageIO.read(
					new File("C:\\Users\\임건호\\Desktop\\웹개발\\02. myworkspace\\MiniProject\\images\\diaryImage3.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "사진을 불러오지 못함");
			e.printStackTrace();
		}

		ImagePanel backgroundPanel = new ImagePanel();
		backgroundPanel.setSize(750, 700);
		this.add(backgroundPanel);

		setVisible(true);
	}

	class ImagePanel extends JPanel {
		public void paint(Graphics g) {
			g.drawImage(background, 0, 0, null);
		}
	}

	public static void main(String[] args) {
//		new Join();
	}
}
