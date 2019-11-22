import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Survey extends BasePanel {

	JButton submit, main;

	JPanel survey = new JPanel(new FlowLayout(0));

	public Survey() {
		// TODO Auto-generated constructor stub
		super("��������", 750, 700, new BorderLayout());
		add(font("�� ������ ����", 20, JLabel.CENTER), BorderLayout.NORTH);
		var s = new JPanel();
		add(s, BorderLayout.SOUTH);
		submit = new JButton("����");
		submit.addActionListener(it->{
			submit();
		});
		s.add(submit);
		main = new JButton("��������");
		s.add(main);
		submit.addActionListener(it->{
			
			
			
		});
		s.setOpaque(false);
		init();
	}

	void init() {
		add(survey);
		try {
			ResultSet rs = DB.stmt.executeQuery(
					"select *, ifnull((select rating from survey_results sr where sr.survey_category_id=sc.id and sr.member_id = "
							+ id + "), 0) from survey_category sc");
			String tmp = "";
			boolean flag = false;
			while (rs.next()) {
				if (!tmp.equals(rs.getString(2))) {
					tmp = rs.getString(2);
					survey.add(new JLabel(tmp));
				}
				if (rs.getInt(4) != 0)
					flag = true;
				survey.add(new SurveyItem(rs.getInt(1), rs.getString(3), rs.getInt(4)));
			}
			if (flag) {
				msg("�̹� ���� ���縦 ���߽��ϴ�.", "Ȯ��");
				submit.setEnabled(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SurveyItem extends JPanel {
		int id;
		JRadioButton radio[] = new JRadioButton[5];
		ButtonGroup gr = new ButtonGroup();

		public SurveyItem(int id, String q, int rating) {
			// TODO Auto-generated constructor stub
			this.id = id;
			setLayout(new FlowLayout(0));
			setPreferredSize(new Dimension(720, 40));
			JLabel jl;
			add(jl = new JLabel(id + ", " + q));
			jcomsize(jl, 300, 30);
			String scol[] = { "�ſ� �Ҹ���", "�Ҹ���", "����", "����", "�ſ� ����" };
			for (int i = 0; i < 5; i++) {
				add(radio[i] = new JRadioButton(scol[i]));
				gr.add(radio[i]);
				if (rating != 0)
					radio[i].setEnabled(false);
				if (rating - 1 == i)
					radio[i].setSelected(true);
			}
		}

		int getRate() {
			for (int i = 0; i < 5; i++) {
				if (radio[i].isSelected())
					return (i + 1);
			}
			return 0;
		}
	}

	void submit() {
		ArrayList<SurveyItem> list = new ArrayList<Survey.SurveyItem>();
		for (var com : survey.getComponents()) {
			if (com instanceof SurveyItem) {
				var item = (SurveyItem) com;
				if (item.getRate() == 0) {
					errmsg("üũ���� ���� �׸��� �ֽ��ϴ�!", "Ȯ��");
					return;
				}
				list.add(item);
			}
		}
		for (var item : list) {
			try {
				DB.stmt.execute("insert into survey_results values(0, " + BasePanel.id + ", " + item.id + ", "
						+ item.getRate() + ")");
				for (int i = 0; i < 5; i++) {
					item.radio[i].setEnabled(false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msg("������ �����ּż� �����մϴ�.", "�ȳ�");
		submit.setEnabled(false);
	}

	@Override
	void design() {
		// TODO Auto-generated method stub

	}

	@Override
	void action() {
		// TODO Auto-generated method stub

	}

	@Override
	void def() {
		// TODO Auto-generated method stub

	}

}
