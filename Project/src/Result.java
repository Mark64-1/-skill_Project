import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Result extends BasePanel {

	JButton jb = new JButton("메인으로");
	Color color[] = {new Color(255, 129, 129),new Color(255, 195, 129),new Color(201, 201, 201),new Color(143,255,194),new Color(0,176,80)};
	int x = 220;
	String str[] = {"매우 불만족","불만족","보통","만족","매우 만족"};
	
	public Result() {
		// TODO Auto-generated constructor stub
		super("설문결과", 1200, 550, new BorderLayout());
		design();
		action();
	}
	
	@Override
	void design() {
		// TODO Auto-generated method stub
		JPanel south =new JPanel(),center = new JPanel(new GridLayout(0, 1, 0, 10)),cen = new JPanel(new GridLayout(1, 0, 10, 0));
		add(font("고객 서비스 설문결과", 20,JLabel.CENTER ),BorderLayout.SOUTH);
		add(south,BorderLayout.SOUTH);
		add(center);
		south.add(jb);
		center.add(drawchart("전체 결과"));
		center.add(cen);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		south.setOpaque(false);
		
		cen.add(drawchart("검색"));
		cen.add(drawchart("구매"));
		cen.add(drawchart("서비스"));
	}
	
	JPanel drawchart(String sql) {
		JPanel jp = new JPanel(new BorderLayout()),chart = new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				try {
					int y = getHeight()-20,sum = 0,value[] = new int[5];
					String s = "";
					if(!sql.equals("전체 결과")) s = " where `group`='"+sql+"'";
					ResultSet rs = DB.stmt.executeQuery("select count(*), rating from survey_results s right join survey_category c on c.id = s.survey_category_id"+s+" group by rating order by rating asc");
					System.out.println("12");
					while(rs.next()) sum +=rs.getInt(1);
					rs.beforeFirst();
					y/=sum;
					while(rs.next()) {
						value[rs.getInt(2)-1] = rs.getInt(1);
					}
					for(int i=0;i<5;i++) {
						g.setColor(color[i]);
						int x = getWidth()/5*(i+1);
						g.fillRect(x-20-getWidth()/10, getHeight()-value[i]*y-40, 40, value[i]*y);
						g.setColor(Color.BLACK);
						JLabel jl = new JLabel(str[i],JLabel.CENTER);
						jl.setBounds(getWidth()/5*i, getHeight()-30, getWidth()/5, 30);
						add(jl);
						jl = new JLabel(new DecimalFormat("0.00").format(100.0*value[i]/sum)+"%("+value[i]+"건)",JLabel.CENTER);
						jl.setBounds(getWidth()/5*i, getHeight()-value[i]*y-80, getWidth()/5, 30);
						add(jl);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		jp.setBorder(new LineBorder(Color.BLACK));
		jp.add(font(sql+" 항목", 20, JLabel.CENTER),BorderLayout.NORTH);
		jp.add(chart);
		return jp;
	}

	
	
	@Override
	void action() {
		// TODO Auto-generated method stub
		jb.addActionListener(it -> book.setPanel(new MainPanel()));
	}

	@Override
	void def() {
		// TODO Auto-generated method stub

	}

}
