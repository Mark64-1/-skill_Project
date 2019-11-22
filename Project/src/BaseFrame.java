import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class BaseFrame extends JFrame{

	public BaseFrame(String title, int w,int h,int oper, LayoutManager layout) {
		// TODO Auto-generated constructor stub
		setSize(w, h);
		setTitle(title);
		setDefaultCloseOperation(oper);
		setLocationRelativeTo(null);
		setLayout(layout);
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
