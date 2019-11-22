import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class DB  {
	static Connection con;
	static Statement stmt;
	JTextPane txt = new JTextPane();
	JScrollPane scr = new JScrollPane(txt);
	int cnt = 0;

	public static void main(String[] args) {
		new DB();
	}
	
	public DB() {
		// TODO Auto-generated constructor stub
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true",
					"user", "1234");
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		execute("use bookdb");
	}

	void loadData(String table, String name) {
		execute("load data local infile './지급자료/" + table + ".csv' into table " + table
				+ " fields terminated by ',' ignore 1 lines");
		setText(name + " 데이터 생성 성공", Color.blue);
	}

	void category() throws FileNotFoundException {
		File files[] = new File("./지급자료/categories/").listFiles();
		boolean fail = false;
		int cnt = 1, bid = 1;
		for (File file : files) {
			if (fail)
				continue;
			setText("main_category " + file.getName() + " 추가 시작", Color.blue);
			execute("insert into main_category values(0, '" + file.getName() + "')");
			File list[] = new File("./지급자료/categories/" + file.getName() + "/").listFiles();
			int cnt2 = 1;
			for (File file2 : list) {
				setText("ㄴ sub_category " + file2.getName() + " 추가 시작", Color.blue);
				execute("insert into sub_category values(0, " + cnt + ", '" + file2.getName() + "')");
				File list2[] = new File("./지급자료/categories/" + file.getName() + "/" + file2.getName()).listFiles();
				Blob blob;
				for (File file3 : list2) {
					String description = file3.getName().split("\\.")[1];
					if (description.toLowerCase().equals("jpg")) {
						continue;
					}
					HashMap<String, String> map = new HashMap<>();
					map.put("sub_category_id", cnt2 + "");
					map.put("image", "asdf");
					readData("./지급자료/categories/" + file.getName() + "/" + file2.getName() + "/" + file3.getName(), map,
							file3.getName());
//					execute("insert into book values(0, "+cnt2+", '"+file3.getName()+"', 'asdf', 0, 0, '0', '0', 0, '0', curdate(), null)");
				}
				setText("sub_category " + file2.getName() + " 추가 완료", Color.blue);
				cnt2++;
			}
			setText("main_category " + file.getName() + " 추가 완료", Color.blue);
			cnt++;
		}
	}

	void readData(String path, HashMap<String, String> map, String name) {
		String line;
		File file = new File(path);
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
			while ((line = buffer.readLine()) != null) {
				String info[] = line.split("\t");
				map.put(info[0], info[1]);
			}
			String field[] = { "id", "sub_category_id", "name", "image", "stock", "price", "author", "intro",
					"numpages", "isbn", "created_by", "hashtag" };
			String val = "";
			for (int i = 0; i < field.length; i++) {
				if (i > 0)
					val += ", ";
				val += "'" + map.get(field[i]) + "'";
			}
			execute("insert into book values(" + val + ")");
			setText(" ㄴ " + name.replace(".txt", "") + " 추가 완료", Color.blue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void read(String path, String table) {
		String line;
		File fileList[] = new File(path).listFiles();
		int cnt = 0;
		for (File file : fileList) {
			if (!file.canRead())
				continue;
			try {
				BufferedReader buffer = new BufferedReader(new FileReader(file));
				execute("insert into member values(0, 'a', 'a', curdate(), 'a', 'a', 'a')");
				cnt++;
				while ((line = buffer.readLine()) != null)
					execute("update " + table + " set " + line.split("\t")[0] + "='" + line.split("\t")[1]
							+ "' where id=" + cnt);
				execute("update member set login_pwd=md5(login_pwd)");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setText("회원 기본 데이터 생성 성공", Color.blue);
	}

	void createUser() {
		execute("drop user if exists user@localhost");
		setText("DB user 제거 성공", Color.blue);
		execute("create user user@localhost identified by '1234'");
		setText("DB user 생성 성공", Color.blue);
		execute("grant insert, delete, update, select on bookdb.* to user@localhost");
		setText("DB user 권한 부여 성공", Color.blue);
	}

	void foreign(String table, String field, String field2) {
		execute("alter table " + table + " add constraint fks_" + cnt + " foreign key(" + field + ") references "
				+ field2);
		cnt++;
	}

	void exit() {
		for (int i = 5; i > 0; i--) {
			setText(i + "초후 프로그램이 종료됩니다.", Color.black);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void cDB() {
		execute("drop database if exists bookdb");
		setText("DB제거 성공", Color.blue);
		execute("create database bookdb default character set utf8");
		setText("DB생성 성공", Color.blue);
		execute("set global local_infile=1");
		execute("use bookdb");
	}

	void createTable(String table, String column, boolean chk) {
		execute("create table " + table + "(" + column.replaceAll(",", " not null,") + (chk ? " not null)" : ")"));
		setText(table + " 데이터 생성 성공", Color.blue);
	}

	static void execute(String sql) {
		try {
			stmt = con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
	}

	void design() {
		scr.setBorder(new TitledBorder("Log"));
		txt.setOpaque(true);
		txt.setBackground(Color.white);
	}
	
	void setText(String log, Color col) {
		StyleContext con = StyleContext.getDefaultStyleContext();
		AttributeSet attr = con.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, col);
		txt.setCharacterAttributes(attr, false);
		txt.replaceSelection(log + "\n");
		txt.setCaretPosition(txt.getDocument().getLength());
		scr.getVerticalScrollBar().setValue(scr.getVerticalScrollBar().getMaximum());
	}
}
