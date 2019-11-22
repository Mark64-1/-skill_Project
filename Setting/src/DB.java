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

public class DB extends BaseFrame {
	static Connection con;
	static Statement stmt;
	JTextPane txt=new JTextPane();
	JScrollPane scr=new JScrollPane(txt);
	int cnt=0;
	public static void main(String[] args) {
		DB db=new DB();
		db.design();
		db.setVisible(true);
		
		try {
			con=DriverManager.getConnection("jdbc:mysql://localhost?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		db.cDB();
		db.createTable("main_category", "id int auto_increment primary key, name varchar(20)", true);
		db.createTable("sub_category", "id int primary key auto_increment, main_category_id int, name varchar(20), foreign key(main_category_id) references main_category(id)", false);
		db.createTable("book", "id int primary key auto_increment, sub_category_id int, name varchar(45), image mediumblob, stock int, price int, author varchar(45), intro text, numpages int, isbn varchar(25), created_by date, hashtag text", false);
		db.foreign("book", "sub_category_id", "sub_category(id)");
		db.createTable("member", "id int primary key auto_increment, login_id varchar(20) unique, login_pwd varchar(32), birthdate date, authority varchar(5), phone varchar(15) unique, email varchar(50) unique", true);
		db.createTable("order_log", "id int primary key auto_increment, book_id int, member_id int, quantity int, order_time datetime, foreign key(book_id) references book(id)", false);
		db.foreign("order_log", "member_id", "member(id)");
		db.createTable("survey_category", "id int primary key auto_increment, `group` varchar(5), description varchar(80) not null, index groupIdx(`group`)", false);
		db.createTable("survey_results", "id int primary key auto_increment, member_id int, survey_category_id int, rating int, foreign key (member_id) references member(id)", false);
		db.foreign("survey_results", "survey_category_id", "survey_category(id)");
		db.createUser();
		db.cnt=0;
		db.read("./지급자료/member_list", "member");
		try {
			db.category();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		db.loadData("survey_category", "설문 항목");
		db.loadData("order_log", "주문 내역");
		db.loadData("survey_results", "설문 결과");
		db.setText("DB 초기화 완료", Color.blue);
		db.exit();
	}
	
	void loadData(String table, String name) {
		execute("load data local infile './지급자료/"+table+".csv' into table "+table+" fields terminated by ',' ignore 1 lines");
		setText(name+" 데이터 생성 성공", Color.blue);
	}
	
	void category() throws FileNotFoundException{
		File files[]=new File("./지급자료/categories/").listFiles();
		boolean fail=false;
		int cnt=1, bid=1;
		for(File file : files) {
			if(fail) continue;
			setText("main_category "+file.getName()+" 추가 시작", Color.blue);
			execute("insert into main_category values(0, '"+file.getName()+"')");
			File list[]=new File("./지급자료/categories/"+file.getName()+"/").listFiles();
			int cnt2=1;
			for(File file2 : list) {
				setText("ㄴ sub_category "+file2.getName()+" 추가 시작", Color.blue);
				execute("insert into sub_category values(0, "+cnt+", '"+file2.getName()+"')");
				File list2[]=new File("./지급자료/categories/"+file.getName()+"/"+file2.getName()).listFiles();
				Blob blob;
				for(File file3 : list2) {
					String description=file3.getName().split("\\.")[1];
					if(description.toLowerCase().equals("jpg")) {
						continue;
					}
					HashMap<String, String> map=new HashMap<>();
					map.put("sub_category_id", cnt2+"");
					map.put("image", "asdf");
					readData("./지급자료/categories/"+file.getName()+"/"+file2.getName()+"/"+file3.getName(), map, file3.getName());
//					execute("insert into book values(0, "+cnt2+", '"+file3.getName()+"', 'asdf', 0, 0, '0', '0', 0, '0', curdate(), null)");
				}
				setText("sub_category "+file2.getName()+" 추가 완료", Color.blue);
				cnt2++;
			}
			setText("main_category "+file.getName()+" 추가 완료", Color.blue);
			cnt++;
		}
	}
	
	void readData(String path, HashMap<String, String> map, String name) {
		String line;
		File file=new File(path);
		try {
			BufferedReader buffer=new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF8"));
			while((line=buffer.readLine())!=null) {
				String info[]=line.split("\t");
				map.put(info[0], info[1]);
			}
			String field[]= {"id", "sub_category_id", "name", "image", "stock", "price", "author", "intro", "numpages", "isbn", "created_by", "hashtag"};
			String val="";
			for(int i=0; i<field.length; i++) {
				if(i>0) val+=", ";
				val+="'"+map.get(field[i])+"'";
			}
			execute("insert into book values("+val+")");
			setText(" ㄴ "+name.replace(".txt", "")+" 추가 완료", Color.blue);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void read(String path, String table) {
		String line;
		File fileList[]=new File(path).listFiles();
		int cnt=0;
		for(File file : fileList) {
			if(!file.canRead()) continue;
			try {
				BufferedReader buffer=new BufferedReader(new FileReader(file));
				execute("insert into member values(0, 'a', 'a', curdate(), 'a', 'a', 'a')");
				cnt++;
				while((line=buffer.readLine())!=null) execute("update "+table+" set "+line.split("\t")[0]+"='"+line.split("\t")[1]+"' where id="+cnt);
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
		execute("alter table "+table+" add constraint fks_"+cnt+" foreign key("+field+") references "+field2);
		cnt++;
	}
	
	
	void exit() {
		for(int i=5; i>0; i--) {
			setText(i+"초후 프로그램이 종료됩니다.", Color.black);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dispose();
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
		execute("create table "+table+"("+column.replaceAll(",", " not null,")+(chk?" not null)":")"));
		setText(table +" 데이터 생성 성공", Color.blue);
	}
	
	static void execute(String sql) {
		try {
			stmt=con.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		}
	}
	
	
	void design() {
		add(scr);
		scr.setBorder(new TitledBorder("Log"));
		txt.setOpaque(true);
		txt.setBackground(Color.white);
	}
	
	DB() {
		super("bookdb 초기화", 300, 300, 3, new BorderLayout());
	}
	
	void setText(String log, Color col) {
		StyleContext con=StyleContext.getDefaultStyleContext();
		AttributeSet attr=con.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, col);
		txt.setCharacterAttributes(attr, false);
		txt.replaceSelection(log+"\n");
		txt.setCaretPosition(txt.getDocument().getLength());
		scr.getVerticalScrollBar().setValue(scr.getVerticalScrollBar().getMaximum());
	}
}
