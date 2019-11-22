import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Search extends BasePanel {

	JPanel list = new JPanel(new GridLayout(0, 3)),paylist = new JPanel();
	JComboBox combo = new JComboBox<>(),main = new JComboBox<>(), sub = new JComboBox<>();
	JTextField txt = new JTextField(20);
	JButton jb[] = new JButton[3];
	ArrayList<String> selectedBooks = new ArrayList<String>(),no = new ArrayList<>(), st = new ArrayList<>();
	
	public Search() {
		// TODO Auto-generated constructor stub
		super("도서검색", 1150, 600, new BorderLayout(5,5));
		design();
		action();
	}
	
	@Override
	void design() {
		// TODO Auto-generated method stub
		JPanel north = new JPanel(),east = new JPanel(new BorderLayout()), south = new JPanel();
		add(north,BorderLayout.NORTH);
		JScrollPane scr;
		add(scr = new JScrollPane(list));
		scr.setBorder(new LineBorder(Color.BLACK));
		list.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(east,BorderLayout.EAST);
		add(south,BorderLayout.SOUTH);
		south.add(jb[2]=new JButton("메인으로"));
		north.add(combo);
		north.add(main);
		north.add(sub);
		north.add(txt);
		north.add(jb[0]=new JButton(getimg("./지급자료/icon_list/search_icon.png", 20, 20)));
		jb[0].setText("검색");
		east.add(jb[1]=new JButton("구매하기"),BorderLayout.SOUTH);
		north.setOpaque(false);
		south.setOpaque(false);
		jcomsize(east, 220, 150);
		east.add(font("-구매 목록-", 20, JLabel.CENTER),BorderLayout.NORTH);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		east.setBorder(new LineBorder(Color.black));
		east.add(new JScrollPane(paylist));
		combo.addItem("통합검색");
		combo.addItem("분류검색");
		combo.addItem("태그검색");
		main.addItem("전체");
		addItem("select name from main_category", main);
		main.setVisible(false);
		sub.setVisible(false);
		setList("");
		setsub();
	}
	void setList(String sql) {
		sql = "select b.name, s.name, m.name, b.hashtag, b.author, b.isbn, b.intro, b.created_by, b.price, b.stock, b.id from book b inner join sub_category s on b.sub_category_id = s.id inner join main_category m on s.main_category_id = m.id" + sql;
		list.removeAll();
		try {
			ResultSet rs = DB.stmt.executeQuery(sql);
			int cnt = 0;
			while(rs.next()) {
				JPanel temp=new JPanel(new BorderLayout(5, 5)), cen=new JPanel(new BorderLayout()), south=new JPanel(new FlowLayout(FlowLayout.LEFT)), center=new JPanel(new GridLayout(0, 1)), sou=new JPanel(), in=new JPanel(new GridLayout(0, 1));
				JTextArea txt = new JTextArea();
				JLabel img  =new JLabel();
				JSpinner spin = new JSpinner();
				JButton jb = new JButton("담기");
				String name = rs.getString(1);
				try {
					spin.setModel(new SpinnerNumberModel(1, 1, rs.getInt("stock"), 1));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					spin.setEnabled(false);
					jb.setEnabled(false);
				}
				int num = rs.getInt("id");
				temp.add(cen);
				img.setToolTipText("크게보기");
				img.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				temp.add(img,BorderLayout.WEST);
				temp.add(south,BorderLayout.SOUTH);
				south.setBorder(new LineBorder(Color.BLACK));
				String tag[] = rs.getString("hashtag").split(",");
				for (int i = 0; i < tag.length; i++) {
					JLabel hash;
					south.add(hash=new JLabel(tag[i]));
					hash.setForeground(Color.BLUE);
					hash.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					hash.addMouseListener(new MouseAdapter() {
						String ha = hash.getText();
						@Override
						public void mousePressed(MouseEvent e) {
							// TODO Auto-generated method stub
							setList(" where hashtag like '%"+ha+"%'");
						}
						@Override
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							hash.setText("<html><U>"+ha+"</U></html>");
						}
						@Override
						public void mouseExited(MouseEvent e) {
							// TODO Auto-generated method stub
							hash.setText(ha);
						}
						
					});
				}
				String path = "./지급자료/categories/"+rs.getString(3)+"/"+rs.getString(2)+"/"+name+".jpg";
				img.setIcon(getimg(path, 110, 160));
				temp.setBorder(new CompoundBorder(new LineBorder(Color.BLACK),new EmptyBorder(5, 5, 5, 5)));
				cen.add(center);
				cen.add(sou,BorderLayout.SOUTH);
				center.add(in);
				center.add(new JScrollPane(txt));
				txt.setLineWrap(true);
				txt.setText(rs.getString("intro"));
				in.add(font(rs.getString(1), 15, JLabel.LEFT));
				in.add(new JLabel(rs.getString("author")));
				in.add(new JLabel(new SimpleDateFormat("yyyy년 MM월 dd일 출간").format(rs.getDate("created_by"))));
				in.add(new JLabel(rs.getString("isbn")));
				sou.add(new JLabel(rs.getString("price")+"원"));
				sou.add(spin);
				jcomsize(spin, 30, 25);
				sou.add(jb);
				spin.setBorder(new LineBorder(Color.BLACK));
				jcomsize(temp, 100, 200);
				list.add(temp);
				jb.addActionListener(it ->{
					JPanel pay=new JPanel(new BorderLayout()), info=new JPanel(new FlowLayout(FlowLayout.LEFT));
					JButton delete=new JButton("X");
					JLabel jL;
					for(int i=0; i<selectedBooks.size(); i++) 
						if(selectedBooks.get(i).equals(name)) {
							errmsg("이미 추가된 도서입니다!", "확인");
							return;
						}
					info.add(jL=new JLabel(name));
					jcomsize(jL, 120, 25);
					info.add(new JLabel(spin.getValue()+"권"));
					pay.add(delete, BorderLayout.EAST);
					pay.add(info);
					paylist.add(pay);
					pay.setBorder(new LineBorder(Color.BLACK));
					selectedBooks.add(name);
					no.add(num+"");
					st.add(spin.getValue()+"");
					delete.addActionListener(e -> {
						paylist.remove(pay);
						repaint();
						revalidate();
					});
					jcomsize(paylist, 200, 43*selectedBooks.size()-10);
					repaint();
					revalidate();
				});
				img.addMouseListener(new MouseAdapter() {
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						JButton jb = new JButton("OK");
						JDialog dialog = new JDialog();
						JPanel south = new JPanel();
						dialog.setTitle("크게 보기");
						dialog.setSize(250,450);
						dialog.add(south,BorderLayout.SOUTH);
						dialog.add(new JLabel(getimg(path, 230, 400)));
						dialog.setModal(true);
						jb.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								dialog.dispose();
							}
						});
						south.add(jb);
						((JPanel)dialog.getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
						dialog.setLocationRelativeTo(null);
						dialog.setVisible(true);
					}
				});
			}
			cnt++;
			for(int i=cnt;i<8;i++) list.add(new JPanel());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
		revalidate();
	}
	void setsub() {
		sub.removeAllItems();
		sub.addItem("전체");
		String sql = "";
		if(main.getSelectedIndex()!=0) sql = " where main_category_id = "+main.getSelectedIndex();
		addItem("select name from sub_category"+sql, sub);
	}

	@Override
	void action() {
		// TODO Auto-generated method stub
		for (int i = 0; i < jb.length; i++) {
			jb[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource().equals(jb[0])) {
						
						if(combo.getSelectedIndex()==0) setList(" where b.name like '%"+txt.getText()+"%' or b.author like '%"+txt.getText()+"%'");
						else if(combo.getSelectedIndex()==1) {
							String sql  ="";
							int idx = 0;
							if(main.getSelectedIndex()==2) idx = 2;
							if(main.getSelectedIndex()==3) idx = 5;
							
							if(main.getSelectedIndex()!=0) sql = " where m.id ="+main.getSelectedIndex();
							if(sub.getSelectedIndex()!=0) sql+=(sql==""?" where ":" and ")+"s.id="+(sub.getSelectedIndex()+idx);
							setList(sql);
						}else {
							if(txt.getText().contains(",")) setList(" where hashtag='asdfasdf'");
							else setList(" where hashtag like '%"+txt.getText()+"%'");
						}
					}if(e.getSource().equals(jb[1])) {
						if(selectedBooks.size()==0) {
							errmsg("1권 이상의 도서를 구매해야 합니다.", "오류");
							return;
						}
						paylist.removeAll();
						jcomsize(paylist, 0, 0);
						for (int j = 0; j < selectedBooks.size(); j++) {
							DB.execute("insert into order_log values(0, "+no.get(j)+", "+id+", "+st.get(j)+", now())");
							DB.execute("update book set stock = stock"+st.get(j)+" where id ="+no.get(j));
						}
						msg("총 "+selectedBooks.size()+"권의 주문이 완료 되었습니다!", "구매 완료");
						selectedBooks.clear();
						st.clear();
						no.clear();
						repaint();
						revalidate();
					}if(e.getSource().equals(jb[2])) {
						book.setPanel(new MainPanel());
					}
				}
			});
		}
	}

	@Override
	void def() {
		// TODO Auto-generated method stub

	}

}
