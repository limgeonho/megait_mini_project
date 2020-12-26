package miniproject;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
/**
 * 다이어리 내부페이지는 달력페이지에서 해당날짜버튼을 눌렀을 때 나타나는 페이지이다.
 * 미니달력(Diary클래스의 mainPanel), 주제, 내용, 사진, 날씨, 현재시간, 저장, 삭제, 수정버튼과 창들이 있는 페이지이다.
 * @author 임건호
 *
 */
public class InsideDiary extends Diary implements Runnable {
	private JFrame insideFrame;

	private JButton backToCal, saveButton, deleteButton, updateButton, imgButton, exitBtn;

	private JPanel btnsPanel, miniCalPanel; 
	
	// imgPanel은 미리 실행되어있어야함
	private JPanel imgPanel = new JPanel();
	
	private JLabel getDateLabel = new JLabel(Diary.yearSet + "년 " + (Diary.monthSet + 1) + "월 " + Diary.dateSet + "일");
	
	private JLabel deleteLabel, getWeatherLabel, dateLabel, topicLabel, weatherLabel, contentLabel, timeLabel, imgLabel, imageLabel;
	
	private JTextField topicTextField;
	
	private JTextArea contentArea;

	private Document doc;
	
	private Elements element;
	String dataFilePath;
	ContentVo cvo = new ContentVo();
	ContentDao dao = null;
	
	/**
	 * 현재시간을 실시간으로 나타내고있는 디지털시계를 보여주는 쓰레드이다.
	 * 멀티쓰레드 방식으로 System.currentTimeMillis()를 통해 현재시간을 반환한다.
	 */
	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (true) {
			timeLabel.setText("현재시간 : " + sdf.format(System.currentTimeMillis()));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * ActionListener의 listener1을 통해서 입력된 버튼들의 메서드를 실행한다.
	 */
	ActionListener listener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			
			/**
			 * backToCal(뒤로가기)버튼이 눌리면 다음과 같이 동작
			 * 현재창은 종료되고 static로 이미 저장되어 있는 yearSet, monthSet을 기반으로 새로운 달력페이지를 실행한다.
			 */
			if (button == backToCal) {
				insideFrame.dispose();
				new Diary(Diary.yearSet, Diary.monthSet);
			}
			
			/**
			 * exitBtn(취소)버튼이 눌리면 다음과 같이 동작
			 * 프로그램을 종료한다.
			 */ 
			if (button == exitBtn) {
				System.exit(0);
			}
			
			/**
			 * saveButton(저장)버튼이 눌리면 다음과 같이 동작
			 * dao(싱클톤 패턴)객체를 만들고 static copyId를 키값으로 id, content, topic, time, filepath를 인자로 저장한다.
			 * 성공적으로 dao의 인자들이 저장되면 이를 insert()메서드를 통해 registerContent테이블에 저장한다.
			 * 저장이 완료되면 saveButton은 사용이 불가능하고 deleteButton과 updateButton은 사용이 가능하게 한다.
			 */
			if (button == saveButton) {
				try {
					dao = ContentDao.getInstance();
					cvo.setId(Login.copyId);
					if (contentArea.getText().equals("내용 작성하기")) {
						JOptionPane.showMessageDialog(null, "내용을 작성해주세요.");
						updateButton.setEnabled(false);
					} else {
						cvo.setContent(contentArea.getText());
						cvo.setTopic(topicTextField.getText());
						cvo.setTime(getDateLabel.getText());
						// 사진이 저장되면 경로도 보냄 없으면 null보냄
						cvo.setFilepath(dataFilePath);
						
						dao.insert(cvo);
						JOptionPane.showMessageDialog(null, "내용 저장 완료!");
						saveButton.setEnabled(false);
						deleteButton.setEnabled(true);
						updateButton.setEnabled(true);
					}
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			/**
			 * updateButton(수정)버튼이 눌리면 다음과 같이 동작
			 * 현재 contentArea에 저장된 데이터가 없다면 updateButton은 사용불가능하고,
			 * 존재한다면 dao(싱클톤 패턴)객체를 만들고 static copyId를 키값으로 id, content, topic, time, filepath를 인자로 저장한다.
			 * 이때, deleteButton은 사용가능하다.
			 */
			if (button == updateButton) {
				try {
					dao = ContentDao.getInstance();
					cvo.setId(Login.copyId);
					if (contentArea.getText().equals("내용 작성하기")) {
						JOptionPane.showMessageDialog(null, "저장된 내용이 없습니다.");
						updateButton.setEnabled(false);
					} else {
						cvo.setContent(contentArea.getText());
						cvo.setTopic(topicTextField.getText());
						cvo.setTime(getDateLabel.getText());
						cvo.setFilepath(dataFilePath);
						
						dao.update(cvo);
						JOptionPane.showMessageDialog(null, "내용 수정 완료!");
						deleteButton.setEnabled(true);
					}
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			/**
			 * deleteButton(삭제)버튼이 눌리면 다음과 같이 동작
			 * 현재 contentArea에 저장된 데이터가 있다면
			 * 존재한다면 dao(싱클톤 패턴)객체를 만들고 static copyId를 키값으로 id, content, topic, time, filepath를 초기화하여 저장한다.
			 * 이때, saveButton은 사용가능하다.
			 */
			if (button == deleteButton) {
				try {
					dao = ContentDao.getInstance();
					cvo.setId(Login.copyId);
					if (contentArea.getText().equals("내용 작성하기")) {
						JOptionPane.showMessageDialog(null, "삭제할 내용이 없습니다.");
						deleteButton.setEnabled(false);
					} else {
						cvo.setTime(getDateLabel.getText());

						dao.delete(cvo);

						topicTextField.setText("주제 작성하기");
						contentArea.setText("내용 작성하기");
						
						JOptionPane.showMessageDialog(null, "내용 삭제 완료!");
						saveButton.setEnabled(true);
					}
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			
			/**
			 * imgButton(사진불러오기)버튼이 눌리면 다음과 같이 동작
			 * JFileChooser클래스를 통해 사진의 경로를 가져온다.
			 * 가져온 사진의 경로를 Image클래스를 통해 Label에 크기를 지정하여 붙여준다.
			 */
			if (button == imgButton) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("png", "jpg", "PNG");
				chooser.setFileFilter(filter);
				int ret = chooser.showOpenDialog(null);
				
				if (ret != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.");
					return;
				}
				
				dataFilePath = chooser.getSelectedFile().getPath();
//				dataFilePath를 받아서 자동 불러오기에 넘겨줘야함.
				ImageIcon icon = new ImageIcon(dataFilePath);
				Image im1 = icon.getImage();
				Image im2 = im1.getScaledInstance(400, 200, Image.SCALE_DEFAULT);
				imageLabel = new JLabel();
				imageLabel.setIcon(new ImageIcon(im2));
				imgPanel.setBounds(20, 450, 400, 220);
				imgPanel.add(imageLabel);
			}

		}
	};
	/**
	 * 사용자가 회원가입을 통해 static copyAddress에 저장한 위치정보를 통해 외부프로그램(네이버)에서 해당지역의 현재 날씨를 가져온다.
	 * 이때, Jsoup를 활용하여 날씨 getWeatherLabel에 붙여준다. 
	 */
	private void getWeather() {
		try {
			switch (Login.copyAddress) {
			case "서울":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=서울날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "부산":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=부산날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "인천":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=인천날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "대구":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=대구날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "대전":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=대전날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "광주":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=광주날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;

			case "울산":
				doc = Jsoup.connect("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=울산날씨").get();
				element = doc.getElementsByAttributeValue("class", "cast_txt");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ContentVo의 객체인 cvo에 로그인시 저장되어 있던 id값과 현재 페이지의 date값을 변수로 select()메서드를 통해
	 * registerContent테이블에 저장되어 있던 해당 cvo의 값들을 불러와서 자동으로 다리어리 내부 페이지에 출력한다.
	 * 이때, registerContent테이블의 filepath의 값은 다시 Image클래스에 전달되어 저장되어 있던 사진 또한 자동 출력한다.
	 * @return cvo는 ContentVo의 객체이며  사용자의 content관련 인자들을 포함하고 있다.
	 */
	private ContentVo getDataContent() {
		try {
			dao = ContentDao.getInstance();
			cvo = dao.select(Login.copyId, getDateLabel.getText());

			if (cvo.getContent().equals("내용  작성하기")) {
				return cvo;
			} else {
				if(!(cvo.getFilepath() == null)) {
					ImageIcon icon = new ImageIcon(cvo.getFilepath());
					Image im1 = icon.getImage();
					Image im2 = im1.getScaledInstance(400, 200, Image.SCALE_DEFAULT);
					imageLabel = new JLabel();
					imageLabel.setIcon(new ImageIcon(im2));
					imgPanel.setBounds(20, 450, 400, 220);
					imgPanel.add(imageLabel);
					return cvo;
				}else {
					return cvo;
				}
					
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return cvo;
	}
	/**
	 * 다이어리 내부에 나타나는 Button을 보여준다.
	 * 위치, 글꼴, 크기, actionListener를 지정하여 전체 frame에 붙여준다.
	 */
	private void viewButton() {
		btnsPanel = new JPanel();
		btnsPanel.setLayout(new FlowLayout());
		saveButton = new JButton("저장");
		deleteButton = new JButton("삭제");
		updateButton = new JButton("수정");
		btnsPanel.setBounds(660, 670, 300, 60);
		btnsPanel.add(saveButton);
		btnsPanel.add(deleteButton);
		btnsPanel.add(updateButton);
		saveButton.addActionListener(listener);
		updateButton.addActionListener(listener);
		deleteButton.addActionListener(listener);
		insideFrame.add(btnsPanel);

		imgButton = new JButton("사진불러오기");
		imgButton.setBounds(300, 415, 120, 28);
		imgButton.addActionListener(listener);
		insideFrame.add(imgButton);
//		imgPanel = new JPanel();
		insideFrame.add(imgPanel);

		backToCal = new JButton("뒤로가기");
		backToCal.setBounds(1000, 30, 90, 28);
		backToCal.addActionListener(listener);
		insideFrame.add(backToCal);

		exitBtn = new JButton("종료");
		exitBtn.setBounds(1100, 30, 60, 28);
		exitBtn.addActionListener(listener);
		insideFrame.add(exitBtn);
	}
	/**
	 * 다이어리 내부에 나타나는 TextField를 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 * 추가적으로 기존에 저장되어 있던 데이터를 불러온다.
	 */
	private void viewTextField() {
		topicTextField = new JTextField();
		topicTextField.setBounds(530, 90, 200, 30);
		topicTextField.setText(cvo.getTopic());
		insideFrame.add(topicTextField);
	}
	/**
	 * 다이어리 내부에 나타나는 TextArea를 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 * 추가적으로 기존에 저장되어 있던 데이터를 불러온다.
	 */
	private void viewTextArea() {
		contentArea = new JTextArea();
		contentArea.setBounds(460, 260, 680, 400);
		contentArea.setText(cvo.getContent());
		insideFrame.add(contentArea);
	}
	/**
	 * 다이어리 내부에 나타나는 Label을 보여준다.
	 * 위치, 글꼴, 크기를 지정하여 전체 frame에 붙여준다.
	 * getWeather()메서드를 실행하여 해당지역의 날씨를 불러온다.
	 * 추가적으로 기존에 저장되어 있던 데이터를 불러온다.
	 */
	private void viewLabel() {
		dateLabel = new JLabel("날짜", JLabel.RIGHT);
		dateLabel.setFont(new Font("고딕", Font.BOLD, 20));
		dateLabel.setBounds(420, 20, 100, 50);
		insideFrame.add(dateLabel);

		getDateLabel.setFont(new Font("고딕", Font.BOLD, 20));
		getDateLabel.setBounds(530, 20, 200, 50);
		insideFrame.add(getDateLabel);

		topicLabel = new JLabel("제목", JLabel.RIGHT);
		topicLabel.setFont(new Font("고딕", Font.BOLD, 20));
		topicLabel.setBounds(420, 80, 100, 50);
		insideFrame.add(topicLabel);

		weatherLabel = new JLabel("날씨", JLabel.RIGHT);
		weatherLabel.setFont(new Font("고딕", Font.BOLD, 20));
		weatherLabel.setBounds(420, 140, 100, 50);
		insideFrame.add(weatherLabel);

		getWeather();
		getWeatherLabel = new JLabel();
		getWeatherLabel.setFont(new Font("고딕", Font.BOLD, 20));
		getWeatherLabel.setBounds(530, 140, 350, 50);
		getWeatherLabel.setText(Login.copyAddress + " : " + element.get(0).text());
		insideFrame.add(getWeatherLabel);

		contentLabel = new JLabel("내용", JLabel.RIGHT);
		contentLabel.setFont(new Font("고딕", Font.BOLD, 20));
		contentLabel.setBounds(420, 200, 100, 60);
		insideFrame.add(contentLabel);

		imgLabel = new JLabel("사진", JLabel.LEFT);
		imgLabel.setFont(new Font("고딕", Font.BOLD, 20));
		imgLabel.setBounds(20, 400, 100, 50);
		insideFrame.add(imgLabel);

		timeLabel = new JLabel();
		timeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		timeLabel.setBounds(20, 700, 400, 50);
		insideFrame.add(timeLabel);
	}
	/**
	 * 다이어리 내부페이지의 생성자로서
	 * getDataContent(), viewLabel(), viewTextArea(), viewTextField(), viewButton()메서드를 실행하고
	 * Diary클래스의 mainPanel을 통해 miniCalPanel에 미니달력을 불러온다.
	 * 멀티쓰레드를 통해 현재 실시간 시계 또한 나타낸다.
	 * @param year 사용자가 접속한 해당 년을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param month 사용자가 접속한 해당 월을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param date 사용자가 접속하고 클릭한 해당 일을 Diary클래스의 dateSet에서 가져온다.	
	 */
	public InsideDiary(int year, int month, String date) {
		insideFrame = new JFrame("다이어리 내부");
		
		insideFrame.setSize(1200, 800);
		insideFrame.setLayout(null);
		insideFrame.setLocationRelativeTo(null);
//		insideFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		miniCalPanel = new JPanel();
		miniCalPanel.add((JPanel) getMainPanel(year, month));
		miniCalPanel.setBounds(20, 20, 420, 380);

		getDataContent();
		viewLabel();
		viewTextArea();
		viewTextField();
		viewButton();
		
		insideFrame.add(miniCalPanel);
		
		new Thread(this).start();

		insideFrame.setVisible(true);
	}

	public static void main(String[] args) {
//		new InsideDiary(yearSet, monthSet, dateSet);
	}

}
