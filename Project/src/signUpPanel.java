import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class signUpPanel extends BasePanel {

	JButton jb[] = new JButton[2];
	JTextField jtf[] = new JTextField[4];
	JPasswordField jpf[] = new JPasswordField[2];
	JLabel jl[] = new JLabel[6];
	String str[] = { "아이디", "비밀번호", "비밀번호 확인", "생년월일", "휴대폰", "이메일" };

	public signUpPanel() {
		// TODO Auto-generated constructor stub
		super("회원가입", 400, 500, new BorderLayout(0, 20));
		design();
		action();
		setBackground(Color.WHITE);
	}

	@Override
	void design() {
		// TODO Auto-generated method stub
		setBorder(new EmptyBorder(5, 20, 20, 20));
		JPanel center = new JPanel(new GridLayout(0, 1)), south = new JPanel(new GridLayout(0, 2));
		add(center);
		add(south, BorderLayout.SOUTH);
		south.add(jb[0] = new JButton("회원가입"));
		south.add(jb[1] = new JButton("취소	"));
		for (int i = 0; i < str.length; i++) {
			JLabel jl1, jl2;
			JPanel temp = new JPanel(new FlowLayout(0, 5, 0));
			temp.add(jl1 = new JLabel(str[i]));
			if (i == 0 || i > 2) {
				temp.add(jtf[i == 0 ? 0 : i - 2] = new JTextField(20));
				jcomsize(jtf[i == 0 ? 0 : i - 2], 20, 30);
			} else {
				temp.add(jpf[i - 1] = new JPasswordField(20));
				jcomsize(jpf[i - 1] = new JPasswordField(), 20, 30);
			}
			temp.add(jl2 = new JLabel());
			temp.add(jl[i] = new JLabel());
			jl[i].setForeground(Color.RED);
			jl[i].setName(str[i]);
			center.add(temp);
			jcomsize(jl1, 120, 25);
			jcomsize(jl2, 120, 25);
			temp.setOpaque(false);
		}
		south.setOpaque(false);
		center.setOpaque(false);
		check();
	}

	void check() {
		for (int i = 0; i < 6; i++) {
			jl[i].setText("");
			if (i == 0 || i > 2) {
				int idx = i == 0 ? i : i - 2;
				if (jtf[idx].getText().equals("")) {
					jl[i].setText(" ");
				}
			} else {
				if (jpf[i - 1].getText().equals(""))
					jl[i].setText(" ");
			}
		}

		chkID();
		chkPW();
		chkBirth();
		chkNum();
		chkMail();
	}

	void chkID() {
		String id = jtf[0].getText();
		if (id.equals(""))
			return;
		if (id.matches(".*[\\W].*")) {
			jl[0].setText("영문과 숫자만 입력");
			return;
		}
		
		try {
			ResultSet rs = DB.stmt.executeQuery("select * from member where login_id ='"+id+"'");
			if(rs.next()) jl[0].setText("이미 존재하는 아이디");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void chkPW() {
		if(jpf[1].getText().equals("") && !jpf[0].getText().equals(jpf[1].getText())) jl[2].setText("비밀번호와 일치해야함");
		String txt = jpf[0].getText();
		if(txt.equals("")) return;
		String key="`1234567890-=\n~!@#$%^&*()_+\nqwertyuiop[]\\\nQWERTYUIOP{}|\nasdfghjkl;'\nASDFGHJKL:\"\nzxcvbnm,./\\nZXCVBNM<>?\n][poiuytrewq\n}{POIUYTREWQ\n';lkjhgfdsa\n\":LKJHGFDSA\n/.,mnbvcxz\n?><MNBVCXZ";
		if(txt.length()<3) return;
		for(int i=0;i<txt.length()-2;i++) {
			if(key.contains(txt.substring(i, i+3))) {
				jl[1].setText("연속되는 3자리 없어야함");
				return;
			}
		}
		if(!jpf[0].getText().matches(".*[0-9].*")) {
			jl[1].setText("숫자가 포함되어야 함");
			return;
		}
		if(!jpf[0].getText().matches(".*[a-zA-Z].*")) {
			jl[1].setText("영문이 포함되어야 함");
			return;
		}
	}

	void chkBirth() {
		String birth = jtf[1].getText();
		if(birth.equals("")) return;
		int num = toint(birth.split("-")[0]);
		if(num<1000) {
			jl[3].setText("yyyy-MM-dd 형식으로 입력");
			return;
		}	
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			format.parse(birth);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			jl[3].setText("yyyy-MM-dd 형식으로 입력");
			return;
		}
		format.setLenient(false);
		try {
			format.parse(birth);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			jl[3].setText("년월일 정확히 입력");
			return;
		}
	}

	void chkNum() {
		String mail=jtf[3].getText();
		if(mail.equals("")) return;
		if(!mail.matches("[\\w_]+@[\\w_]+\\.[\\w_]+")) {
			jl[5].setText("<HTML>xxx@xxx.xxx 형식으로 입력<br>x는 영대소문자,숫자,'_'만 허용함");
			return;
		}
		try {
			ResultSet rs=DB.stmt.executeQuery("select * from member where email='"+mail+"'");
			if(rs.next()) jl[5].setText("이미 존재하는 이메일");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void chkMail() {
		String mail = jtf[3].getText();
		if(mail.equals(""))return;
		if(!mail.matches("[\\w_]+@[\\w_]+\\.[\\w_]+")) {
			jl[5].setText("<html>xxx@xxx.xxx 형식으로 입력<br>x는 영대소문자, 숫자, '_'만 허용함");
			return;
		}
		try {
			ResultSet rs = DB.stmt.executeQuery("select * from member wher email ='"+mail+"'");
			if(rs.next())jl[5].setText("이미 존재하는 이메일");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void limitTxt(int limit,JTextField jtf) {
		if(jtf.getText().length()>limit) {
			String edit = jtf.getText().substring(0,limit);
			jtf.setText(edit);
		}
	}
	
	@Override
	void action() {
		// TODO Auto-generated method stub
		for (int i = 0; i < jtf.length; i++) {
			jtf[i].getDocument().addDocumentListener((textListener)e -> check());
			jtf[i].addKeyListener(new KeyAdapter() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					int limit = 0;
					limit = e.getSource()==jtf[0]?20:e.getSource()==jtf[1]?10:e.getSource()==jtf[2]?13:50;
					limitTxt(limit,(JTextField)e.getSource());
				}
				
			});
		}
		for (int i = 0; i < jb.length; i++) {
			jpf[i].getDocument().addDocumentListener((textListener)e -> check());
			jb[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource().equals(jb[0])) {
						String err = "";
						for(int j=0;j<jl.length;j++) if(!jl[j].getText().equals("")) err+=(err.equals("")?"":", ")+jl[j].getName();
						if(err!="") {
							errmsg("입력되지 않았거나,잘못 입력된 필드가 있습니다.\n해당 필드:"+err, "입력 오류");
							return;
						}
						DB.execute("insert into member values(0, '"+jtf[0].getText()+"', md5('"+jpf[1].getText()+"'), '"+jtf[1].getText()+"', 'USER', '"+jtf[2].getText()+"', '"+jtf[3].getText()+"')");
						msg("회원가입을 축하합니다.", "회원가입");
						book.setPanel(new LoginPanel());
					}else {
						book.remove();
						book.setPanel(new LoginPanel());
					}
				}
			});
		}
	}

	@Override
	void def() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 6; i++) {
			jl[i].setText("");
			if (i == 0 || i > 2) {
				String before = jtf[i == 0 ? 0 : i - 2].getText();
				jtf[i == 0 ? 0 : i - 2].setText("");
				jtf[i == 0 ? 0 : i - 2].setText(before);
			} else {
				String before = jpf[i - 1].getText();
				jpf[i - 1].setText("");
				jpf[i - 1].setText(before);
			}
		}
	}

}
