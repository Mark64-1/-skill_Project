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
	JCheckBox save = new JCheckBox("ID ����");
	int fcnt = 1;
	Preferences pre = Preferences.userNodeForPackage(DB.class);
	
	public LoginPanel() {
		// TODO Auto-generated constructor stub
		super("�α���", 250, 200, new BorderLayout());
		design();
		action();
	}

	@Override
	void design() {
		// TODO Auto-generated method stub
		JPanel south = new JPanel(new GridLayout(0, 2)),center = new JPanel(new GridLayout(0, 1));
		add(south,BorderLayout.SOUTH);
		add(center);
		south.add(jb[0]=new JButton("�α���"));
		south.add(jb[1]=new JButton("ȸ������"));
		center.add(new JLabel("���̵� �Ǵ� �̸���, �޴��� ��ȣ"));
		center.add(id);
		center.add(new JLabel("��й�ȣ"));
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
						msg("ȯ���մϴ�!", "Welcome");
						book.clear(new MainPanel());
						fcnt = 0;
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						int now = LocalTime.now().toSecondOfDay(), blocktime = pre.getInt("block", 0);
						if(blocktime>now) {
							errmsg(blocktime-now+"���� �õ����ּ���", "�α��� Block");
							return;
						}
						if(fcnt>=3) {
							errmsg("���� 3ȸ ���з� 15�ʰ� �α����� �Ұ����մϴ�.", "Login Block");
							pre.putInt("block", LocalTime.now().plusSeconds(15).toSecondOfDay());
							fcnt=1;
							return;
						}
						errmsg("��ġ�ϴ� ������ �����ϴ�. \n"+fcnt+"ȸ Ʋ�Ƚ��ϴ�.\n���� 3ȸ Ʋ�� ��, 15�ʰ� �α��� ����� �ߴܵ˴ϴ�.", "���� Ȯ��");
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
