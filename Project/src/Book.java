import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Book extends BaseFrame{
	
	JButton jb[]= new JButton[2];
	JPanel north = new JPanel(new FlowLayout(0));
	JLabel jl[] = new JLabel[5],conl[] = new JLabel[4];
	int location;
	ArrayList<BasePanel> arr = new ArrayList<BasePanel>();
	GridBagConstraints g= new GridBagConstraints();
	JPanel center = new JPanel(new GridBagLayout());
	
	
	public Book() {
		// TODO Auto-generated constructor stub
		super("자바문고", 1250, 700, 3, new BorderLayout());
		try {
			setIconImage(ImageIO.read(new File("./지급자료/icon_list/main_icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		design();
		action();
		setPanel(new LoginPanel());
		setVisible(true);
	}
	void design() {
		add(north,BorderLayout.NORTH);
		north.add(jb[0]=new JButton("뒤로"));
		jb[0].setIcon(getimg("./지급자료/icon_list/previous_icon.png", 25, 25));
		north.add(jb[1]=new JButton("앞으로"));
		jb[1].setIcon(getimg("./지급자료/icon_list/next_icon.png", 25, 25));
		north.setBackground(Color.WHITE);
		north.setOpaque(true);
		north.add(jl[0]=new JLabel());
		add(center);
		for (int i = 0; i < conl.length; i++) {
			north.add(conl[i]=new JLabel());
			north.add(jl[i+1]=new JLabel());
		}
	}
	
	void action() {
		for (int i = 0; i < jb.length; i++) {
			jb[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource().equals(jb[0])) goBack();
					else goAfter();
				}
			});
		}
	}
	void goBack() {
		moveScene(-1);
	}
	void goAfter() {
		moveScene(1);
	}
	
	void setPanel(BasePanel jp) {
		arr.add(jp);
		location = arr.size()-2;
		moveScene(1);
	}
	
	void setButtonEnabled(int num) {
		jb[0].setEnabled(location!=0);
		jb[1].setEnabled(location!=arr.size()-1);
	}
	
	void clear(BasePanel jp) {
		arr.clear();
		arr.add(jp);
		location = 0;
		moveScene(0);
	}
	
	void remove() {
		arr.remove(location);
		moveScene(-1);
	}
	void moveScene(int num) {
		if(arr.size()>5) arr.remove(0);
		location+=num;
		if(location>=5) location=4;
		for(int i=0;i<jl.length;i++) jl[i].setText("");
		for(int i=0;i<conl.length;i++) conl[i].setText("");
		for (int i = 0; i < arr.size(); i++) jl[i].setText(arr.get(i).getName());
		for (int i = 0; i < arr.size()-1; i++) conl[i].setText(">");
		for (int i=0;i<arr.size();i++) jl[i].setForeground(Color.BLACK);
		jl[location].setForeground(Color.BLUE);
		setButtonEnabled(0);
		center.removeAll();
		center.add(arr.get(location),g);
		arr.get(location).def();
		center.repaint();
		center.revalidate();
	}

}
