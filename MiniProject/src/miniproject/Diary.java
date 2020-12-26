package miniproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**
 * Diary클래스는 로그인페이지에서 성공적으로 로그인이 실시되었을 때 나타나는 달력페이지이다.
 * 달력페이지에서는 사용자가 접속한 현재 날짜가 해당하는 달력에 나타나고 
 * 사용자가 원하는 대로 년과 월을 이동하면서 새로운 달력들을 보여준다.
 * @author 임건호
 *
 */
public class Diary extends JFrame {
	protected static Calendar cal = Calendar.getInstance();
	protected static int yearSet = cal.get(Calendar.YEAR);
	protected static int monthSet = cal.get(Calendar.MONTH);
	protected static int today = cal.get(Calendar.DATE);
	protected static String dateSet;
	protected JPanel mainPanel;
	
	private JPanel centerPanel, northPanel;

	private JLabel northLabel;

	private ArrayList<JButton> btnList, dayList;

	private JButton nextMonth, nextYear, lastMonth, lastYear, exitBtn;
	/**
	 * ActionListener의 listener1을 통해서 입력된 버튼들의 메서드를 실행한다.
	 */
	ActionListener listener1 = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			/**
			 * nextMonth(▶)버튼이 눌리면 다음과 같이 동작
			 * 해당 월은(12월 이상일 경우) 년이 +1 되고 월은 1로 초기화된다. 
			 * 추가적으로 적용된 년과 월을 기반으로 달력이 형성되고 기존의 달력은 종료된다.
			 */
			if (button == nextMonth) {
				if (monthSet + 2 > 12) {
					yearSet++;
					monthSet = 0;
				} else {
					monthSet++;
				}
				dispose();
				new Diary(yearSet, monthSet);
			}
			
			/**
			 * lastMonth(◀)버튼이 눌리면 다음과 같이 동작
			 * 해당 월은(1월 이하일 경우) 년이 -1 되고 월은 12로 초기화된다. 
			 * 추가적으로 적용된 년과 월을 기반으로 달력이 형성되고 기존의 달력은 종료된다.
			 */
			if (button == lastMonth) {
				if (monthSet < 1) {
					yearSet--;
					monthSet += 11;
				} else {
					monthSet--;
				}
				dispose();
				new Diary(yearSet, monthSet);
			}
			
			/**
			 * nextYear(▶▶)버튼이 눌리면 다음과 같이 동작
			 * 해당 년은 +1된다. 
			 * 추가적으로 적용된 년과 월을 기반으로 달력이 형성되고 기존의 달력은 종료된다.
			 */
			if (button == nextYear) {
				yearSet++;
				dispose();
				new Diary(yearSet, monthSet);
			}
			
			/**
			 * lastYear(◀◀)버튼이 눌리면 다음과 같이 동작
			 * 해당 년은 -1된다. 
			 * 추가적으로 적용된 년과 월을 기반으로 달력이 형성되고 기존의 달력은 종료된다.
			 */
			if (button == lastYear) {
				yearSet--;
				dispose();
				new Diary(yearSet, monthSet);
			}
			
			/**
			 * exitBtn(취소)버튼이 눌리면 다음과 같이 동작
			 * 프로그램을 종료한다.
			 */
			if (button == exitBtn) {
				System.exit(0);
			}

		}
	};
	/**
	 * ActionListener의 listener2을 통해서 입력된 버튼들의 메서드를 실행한다.
	 */
	ActionListener listener2 = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button = (JButton) e.getSource();
			/**
			 * 달력에 있는 각각의 일버튼이 눌리면 다음과 같이 동작
			 * 해당 년, 월, 일의 값을 static 형태의 yearSet, monthSet, dateSet에 저장하고 
			 * InsideDiary()생성자를 실행하고 기존의 창은 종료한다.
			 */
			dateSet = button.getText();
			dispose();
			
			new InsideDiary(yearSet, monthSet, dateSet);
		}
	};
	/**
	 * 달력창에 나타나는 northPanel을 보여준다.
	 * 위치, 글꼴, 크기, actionListener를 지정하여 전제 frame에 붙여준다.
	 * @param year 사용자가 접속한 해당 년을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param month 사용자가 접속한 해당 월을 cal객체(싱글톤 패턴)에서 가져온다.
	 */
	private void getNorthPanel(int year, int month) {
		northPanel = new JPanel(new FlowLayout());
		northLabel = new JLabel(year + "년 " + (month + 1) + "월");
		northLabel.setFont(new Font("고딕", Font.BOLD, 30));
		lastYear = new JButton("◀◀");
		lastMonth = new JButton("◀");
		nextMonth = new JButton("▶");
		nextYear = new JButton("▶▶");
		
		northPanel.add(lastYear);
		northPanel.add(lastMonth);
		northPanel.add(northLabel);
		northPanel.add(nextMonth);
		northPanel.add(nextYear);

		nextMonth.addActionListener(listener1);
		lastMonth.addActionListener(listener1);
		nextYear.addActionListener(listener1);
		lastYear.addActionListener(listener1);
		
		exitBtn = new JButton("종료");
		exitBtn.setBounds(1100, 12, 60, 28);
		add(exitBtn);
		exitBtn.addActionListener(listener1);
	}
	/**
	 * 달력창에 나타나는 calendar을 보여준다.
	 * 위치, 글꼴, 크기, actionListener를 지정하여 전제 frame에 붙여준다.
	 * 해당 월의 첫날과 마지막날을 기반으로 달력을 완성하고 각각의 날에는 actionListener을 통해 기능을 추가한다.
	 * @param year 사용자가 접속한 해당 년을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param month 사용자가 접속한 해당 월을 cal객체(싱글톤 패턴)에서 가져온다.
	 */
	private void getCalendar(int year, int month) {
		centerPanel = new JPanel(new GridLayout(0, 7));
		cal.set(year, month, 1);
		btnList = new ArrayList<JButton>();

		int wday = cal.get(Calendar.DAY_OF_WEEK);
		int lastday = cal.getActualMaximum(Calendar.DATE);

		////////////////////////////////////////////////////////////// 
		
		String[] day = {"일", "월", "화", "수", "목", "금", "토"};
		dayList = new ArrayList<>();
		for (int i = 0; i < day.length; i++) {
			dayList.add(new JButton(day[i]));
		}

		for (JButton s : dayList) {
			s.setBackground(new Color(250, 237, 239));
			s.setFont(new Font("고딕", Font.BOLD, 20));
			centerPanel.add(s);
		}
		//////////////////////////////////////////////////////////////  
		
		for (int i = 0; i < wday - 1; i++) {
			btnList.add(new JButton(""));
		}

		for (int i = 1; i < lastday + 1; i++) {
			btnList.add(new JButton(String.valueOf(i)));
		}

		int cnt = 0;
		for (JButton s : btnList) {
			cnt++;
			if (cnt % 7 == 0) {
				s.setForeground(Color.BLUE);
			}

			if (cnt % 7 == 1) {
				s.setForeground(Color.RED);
			}

			s.setBackground(Color.WHITE);
			s.setFont(new Font("고딕", Font.BOLD, 20));
			s.addActionListener(listener2);
			s.setHorizontalAlignment(SwingConstants.NORTH_EAST);
			
			if (String.valueOf(today).equals(s.getText())) {
				s.setBackground(Color.PINK);
				
			}
			centerPanel.add(s);
		}
	}

	//////////////////////////////////////////////////////////////
	/**
	 * 달력페이지의 달력을 가져온다.
	 * getNorthPanel(), getCalendar()메서드를 실행하고
	 * 이를 mainPanel에 붙여준다.
	 * @param year 사용자가 접속한 해당 년을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param month 사용자가 접속한 해당 월을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @return mianPanel 페이지의 달력을 반환한다.
	 */
	public JPanel getMainPanel(int year, int month) {
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setSize(1200, 800);
		getNorthPanel(year, month);
		getCalendar(year, month);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		return mainPanel;
	}
	/**
	 * 달력페이지의 생성자로서
	 * getMainPanel()을 통해 return된 mainPanel을 전체 frame에 붙여준다.
	 * @param year 사용자가 접속한 해당 년을 cal객체(싱글톤 패턴)에서 가져온다.
	 * @param month 사용자가 접속한 해당 월을 cal객체(싱글톤 패턴)에서 가져온다.
	 */
	public Diary(int year, int month) {
		super("다이어리");
		
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(getMainPanel(year, month));
		setVisible(true);
	}

	public Diary() {};

	public static void main(String[] args) {
//		new Diary(yearSet, monthSet);
	}
}