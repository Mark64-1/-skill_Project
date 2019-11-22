import java.awt.Image;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class BaseFrame extends JFrame {

	public BaseFrame(String title, int w, int h, int operation, LayoutManager manager) {
		super(title);
		setSize(w, h);
		setLayout(manager);
		setDefaultCloseOperation(operation);
		setLocationRelativeTo(null);
	}
	
	ImageIcon img(String path, int w, int h) {
		try {
			return new ImageIcon(ImageIO.read(new File(path)).getScaledInstance(w, h, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(path);
			return null;
		}
	}
}
