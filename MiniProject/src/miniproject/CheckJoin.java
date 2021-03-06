package miniproject;
/**
 * CheckJoin인터페이스는 회원가입 페이지에서 사용되는 제한사항들의 정규표현식이다.
 * @author 임건호
 *
 */
public interface CheckJoin {
	/**
	 * 이메일형식(0000@0000.000)
	 */
	String MAIL_REGEX = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
	/**
	 * 비밀번호형식(영문(대) + 영문(소) + 특수문자 + 숫자 + 4~20자리)
	 */
	String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,20}$";
	/**
	 * 아이디형식(영문(소) + 숫자 + 4~20자리)
	 */
	String ID_REGEX = "^(?=.*[0-9])(?=.*[a-z]).{4,20}$";
	/**
	 * 연락처형식(000-0000-0000 or 000-000-0000)
	 */
	String TEL_REGEX = "^\\d{3}-\\d{3,4}-\\d{4}$";
	/**
	 * 나이형식(숫자 + 1~2자리)
	 */
	String AGE_REGEX = "^(?=.*[0-9]).{1,2}$";
	/**
	 * 이름형식(한글)
	 */
	String NAME_REGEX = "^[가-힣]*$";
}
