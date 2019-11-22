import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainPanel extends BasePanel {

	JButton jb[] = new JButton[4];

	public MainPanel() {
		// TODO Auto-generated constructor stub
		super("����", 600, 300, new BorderLayout(0, 10));
		design();
		action();
	}

	@Override
	void design() {
		// TODO Auto-generated method stub
		setBorder(new EmptyBorder(10, 10, 10, 10));
		if (user == 1)
			setUser();
		else
			setStaff();
	}

	void setUser() {
		JPanel center = new JPanel(new GridLayout(1, 0, 10, 0)), south = new JPanel(new GridLayout(1, 0, 10, 0));
		add(center);
		add(south, BorderLayout.SOUTH);
		center.add(jb[0] = new JButton("���� �˻�"));
		center.add(jb[1] = new JButton("���� ����"));
		south.add(jb[2] = new JButton("���� ���ų���"));
		south.add(jb[3] = new JButton("�α׾ƿ�"));
		jb[0].setIcon(getimg("./�����ڷ�/icon_list/searching_book.png", 200, 200));
		jb[1].setIcon(getimg("./�����ڷ�/icon_list/survey_icon.png", 200, 200));
		for (int i = 0; i < 2; i++) {
			jb[i].setHorizontalTextPosition(JButton.CENTER);
			jb[i].setVerticalTextPosition(JButton.BOTTOM);
		}
	}

	void setStaff() {
		add(jb[0] = new JButton("���� ���"));
		jb[0].setIcon(getimg("./�����ڷ�/icon_list/reporting_icon.png", 240, 220));
		add(jb[1] = new JButton("�α׾ƿ�"),BorderLayout.SOUTH);
		jb[0].setVerticalTextPosition(JButton.BOTTOM);
		jb[0].setHorizontalTextPosition(JButton.CENTER);
	}

	@Override
	void action() {
		// TODO Auto-generated method stub
		for (int i = 0; i < jb.length; i++) {
			if (jb[i] != null) {
				jb[i].addActionListener(it ->{
					if(user==1) {
						if(it.getSource().equals(jb[0])) book.setPanel(new Search());
						if(it.getSource().equals(jb[1])) book.setPanel(new Survey());
						if(it.getSource().equals(jb[2])) book.setPanel(new Pay());
						if(it.getSource().equals(jb[3])) book.clear(new LoginPanel());
					}else {
						if(it.getSource().equals(jb[0])) book.setPanel(new Result());
						else book.clear(new LoginPanel());
					}
				});
			}
		}
	}

	@Override
	void def() {
		// TODO Auto-generated method stub

	}

}
