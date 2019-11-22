import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class LoginPanel extends BasePanel{
	
	JButton jb[] = new JButton[2];
	JTextField id = new JTextField();
	JPasswordField pw= new JPasswordField();
	JCheckBox save = new JCheckBox("ID 저장");
	int fcnt = 1;
	Preferences pre = Preferences.userNodeForPackage(DB.class);
	
	public LoginPanel() {
		// TODO Auto-generated constructor stub
		super("로그인", 250, 200, new BorderLayout());
		design();
		action();
	}

	@Override
	void design() {
		// TODO Auto-generated method stub
		JPanel south = new JPanel(new GridLayout(0, 2)),center = new JPanel(new GridLayout(0, 1));
		add(south,BorderLayout.SOUTH);
		add(center);
		south.add(jb[0]=new JButton("로그인"));
		south.add(jb[1]=new JButton("회원가입"));
		center.add(new JLabel("아이디 또는 이메일, 휴대폰 번호"));
		center.add(id);
		center.add(new JLabel("비밀번호"));
		center.add(pw);
		center.add(save);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		south.setOpaque(false);
		center.setOpaque(false);
		save.setOpaque(false);
	}

	@Override
	void action() {
		// TODO Auto-generated method stub
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(save.isSelected()) pre.remove("id");
			}
		});
		for (int i = 0; i < jb.length; i++) {
			jb[i].addActionListener(it->{
				if(it.getSource().equals(jb[0])) {
					try {
						System.out.println(pw.getText());
						ResultSet rs = DB.stmt.executeQuery("select * from member where (login_id = '"+id.getText()+"' or phone = '"+id.getText()+"' or email ='"+id.getText()+"' and login_id ='"+pw.getText()+"')");
						rs.next();
						BasePanel.id= rs.getString(1);
						user= rs.getString(5).equals("STAFF")?0:1;
						if(save.isSelected()) pre.put("id", id.getText());
						else pre.remove("id");
						msg("환영합니다!", "Welcome");
						book.clear(new MainPanel());
						fcnt = 0;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						int now = LocalTime.now().toSecondOfDay(), blocktime = pre.getInt("block", 0);
						if(blocktime>now) {
							errmsg(blocktime-now+"초후 시도해주세요", "로그인 Block");
							return;
						}
						if(fcnt>=3) {
							errmsg("연속 3회 실패로 15초간 로그인이 불가능합니다.", "Login Block");
							pre.putInt("block", LocalTime.now().plusSeconds(15).toSecondOfDay());
							fcnt=1;
							return;
						}
						errmsg("일치하는 정보가 없습니다. \n"+fcnt+"회 틀렸습니다.\n연속 3회 틀릴 시, 15초간 로그인 기능이 중단됩니다.", "정보 확인");
						fcnt++;
						
					}
				}if(it.getSource().equals(jb[1])) {
					book.setPanel(new signUpPanel());
				}
			});
		}
	}
	

	@Override
	void def() {
		// TODO Auto-generated method stub
		String idtxt = id.getText(),pwtxt  = pw.getText();
		id.setText(idtxt);
		pw.setText(pwtxt);
	}

}
