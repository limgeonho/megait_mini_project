package miniproject;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/**
 * Login 클래스는 프로그램을 시작하는 로그인창을 나타내는 클래스이다.
 * id, password 입력창이 있고 로그인과 회원가입페이지로 넘어가는 버튼이 있다.
 * 또한, 데이터베이스와 연동되어 로그인에 성공하게 되면 해당 id는 static변수에 저장된다.
 * @author 임건호
 *
 */
public class Login extends JFrame{
	public static String copyId, copyAddress;

	private JLabel titleNameLabel, labelId, labelPw;

	private JTextField textFieldId, textFieldPw;

	private JButton buttonLogin, buttonJoin;

	private String id, password;
	
	private UserDao dao = null;

	private BufferedImage background = null;
	
	/**
	 * ActionListener를 통해서 버튼이 눌렸을 때 동작하는 메서드를 각각 설점
	 */
	ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			/**
			 * buttonLogin(로그인)버튼이 눌리면 다음과 같이 동작
			 * 싱클톤 패턴으로 dao객체를 만들고 id, password에 입력된 텍스트를 읽어서 데이터베이스에 저장되어 있는 데이터와 일치 여부를 확인한다.
			 * 일치여부가 확인되면, 현재창을 종료하고 다음 달력화면으로 넘어간다.
			 * 이때, id창에 입력된 id정보는 static copyId에 저장되어 프로그램이 종료 될 때까지 활용한다. 
			 */
			if (button == buttonLogin) {
				
				try {
					dao = UserDao.getInstance();

					id = textFieldId.getText();
					copyId = id;
					password = textFieldPw.getText();
					/**
					 * 입력받은 id, password를 데이터베이스에 저장되어 있는 정보와 일치 여부를 확인한다.
					 * @param id id창에서 입력받은 텍스트
					 * @param password password창에서 입력받은 텍스트
					 * @return 문자열 "id님" 환영합니다!를 반환한다.
					 */
					dao.select(id, password);

					textFieldId.setText(null);
					textFieldPw.setText(null);
					dispose();

				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}

			}

			if (button == buttonJoin) {
				/**
				 * buttonJoin(회원가입)버튼이 눌리면 현재창은 종료되고 회원가입창이 나타난다.
				 */
				dispose();
				new Join();
			}
		}
	};
	/////////////////////////////////////////////////////////////////////////////////////////	

	/**
	 * KeyListener를 통해서 password창에 비밀번호를 입력하고 마우스로 로그인 버튼을 클릭하지 않고 ENTER(엔터)키를 눌러도 로그인기능이 실행되도록 한다.
	 */
	KeyListener keyListener = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				try {
					dao = UserDao.getInstance();

					id = textFieldId.getText();
					copyId = id;
					password = textFieldPw.getText();

					dao.select(id, password);

					textFieldId.setText(null);
					textFieldPw.setText(null);
					dispose();

				} catch (ClassNotFoundException | SQLException e1) {
					e1.printStackTrace();
				}
			}

		}
	};
	/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 로그인창에 나타나는 Label들을 보여준다.
	 * 전체제목, id, password label의 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewLabel() {
		titleNameLabel = new JLabel("MY DIARY", JLabel.CENTER);
		titleNameLabel.setFont(new Font("고딕", Font.BOLD, 30));
		titleNameLabel.setBounds(230, 30, 150, 40);
		add(titleNameLabel);
		
		labelId = new JLabel("ID", JLabel.RIGHT);
		labelId.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		labelId.setBounds(50, 100, 150, 30);
		add(labelId);
		
		labelPw = new JLabel("PASSWORD", JLabel.RIGHT);
		labelPw.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		labelPw.setBounds(50, 150, 150, 30);
		add(labelPw);
	}
	/**
	 * 로그인창에 나타나는 TextField들을 보여준다.
	 * id, password textField의 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewTextField() {
		textFieldId = new JTextField();
		textFieldId.setBounds(210, 100, 200, 30);
		add(textFieldId);
		
		textFieldPw = new JPasswordField();
		textFieldPw.setBounds(210, 150, 200, 30);
		add(textFieldPw);
		textFieldPw.addKeyListener(keyListener);
	}
	/**
	 * 로그인창 하단에 로그인, 회원가입버튼을 보여준다.
	 * 로그인, 회원가입버튼의 위치, 글꼴, 크기를 지정하여 Panel에 붙인뒤에 전제 frame에 붙여준다.
	 */
	private void viewPanel() {
		buttonJoin = new JButton("회원가입");
		buttonJoin.setFont(new Font("고딕", Font.BOLD, 15));
		buttonJoin.setBounds(215, 200, 95, 25);
		add(buttonJoin);
		buttonJoin.addActionListener(listener);
		
		buttonLogin = new JButton("로그인");
		buttonLogin.setFont(new Font("고딕", Font.BOLD, 15));
		buttonLogin.setBounds(312, 200, 95, 25);
		add(buttonLogin);
		buttonLogin.addActionListener(listener);
	}
	/**
	 * 로그인 페이지의 생성자로서
	 * viewLabel(), viewTextField(), viewPanel()의 메서드를 실행하고 
	 * 로그인창의 배경화면 이미지를 설정해서 사용자에게 보여준다.
	 */
	public Login() {
		super("다이어리 프로그램 로그인 페이지");
		
		setSize(600, 370);
		setLayout(null);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
				
		viewLabel();
		viewTextField();
		viewPanel();
		
		try {
			background = ImageIO.read(new File("C:\\Users\\임건호\\Desktop\\웹개발\\02. myworkspace\\MiniProject\\images\\diaryImage2.jpg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "사진을 불러오지 못함");
			e.printStackTrace();
		}
		
		ImagePanel backgroundPanel = new ImagePanel();
		backgroundPanel.setSize(600, 370);
		this.add(backgroundPanel);
	
		this.setVisible(true);
	}
	
	class ImagePanel extends JPanel {
		public void paint(Graphics g) {
			g.drawImage(background, 0, 0, null);
		}
	}
	
	public static void main(String[] args) {
		new Login();
		
	}
}	
