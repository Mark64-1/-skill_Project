import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public abstract class BasePanel extends JPanel{

	static Book book;
	static String id;
	static int user;
	
	public static void main(String[] args) {
		new DB();
		book = new Book();
	}
	
	public BasePanel(String name, int w, int h, LayoutManager manager) {
		setPreferredSize(new Dimension(w, h));
		setLayout(manager);
		setName(name);
		setBackground(Color.white);
		setOpaque(true);
	}
	
	JLabel font(String str, int size, int alig) {
		JLabel jl = new JLabel(str,alig);
		jl.setFont(new Font("",Font.BOLD,size));
		return jl;
	}
	
	void addItem(String sql,JComboBox com) {
		try {
			ResultSet rs = DB.stmt.executeQuery(sql);
			while(rs.next()) com.addItem(rs.getString(1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.LIGHT_GRAY);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3,15,15);
	}
	
	void msg(String title,String msg) {
		JOptionPane.showMessageDialog(null, msg,title,JOptionPane.INFORMATION_MESSAGE);
	}
	
	void errmsg(String title,String msg) {
		JOptionPane.showMessageDialog(null, msg,title,JOptionPane.ERROR_MESSAGE);
	}
	
	abstract void design();
	abstract void action();
	abstract void def();
	
	int toint(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return -1;
		}
	}
	
	void jcomsize(Component com,int w,int h	) {
		com.setPreferredSize(new Dimension(w, h));
	}
	
	ImageIcon getimg(String path,int w,int h) {
		try {
			return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(w, h, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
}
