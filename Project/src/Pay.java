import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Pay extends BasePanel {
	JButton jB=new JButton("메인으로");
	DefaultTableModel model=new DefaultTableModel(null, new String[] {"주문일자", "시간", "제목", "저자", "수량"}) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable table=new JTable(model);
	JScrollPane scr=new JScrollPane(table);
	boolean isClicked=false;
	
	public Pay() {
		super("구매내역", 700, 300, new BorderLayout());
		design();
		action();
	}
	@Override
	void def() {

	}

	@Override
	void design() {
		JPanel south=new JPanel();
		add(scr);
		add(south, BorderLayout.SOUTH);
		south.add(jB);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		try {
			ResultSet rs=DB.stmt.executeQuery("select login_id from member where id="+id);
			rs.next();
			add(font(rs.getString(1)+"님 구매내역", 25, JLabel.CENTER), BorderLayout.NORTH);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		addRow();
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		south.setOpaque(false);
	}
	
	void addRow() {
		int row=table.getRowCount();
		try {
			ResultSet rs=DB.stmt.executeQuery("select date(order_time), time(order_time), name, author, quantity, member_id from order_log o inner join book b on b.id=o.book_id where member_id="+id+" limit 15 offset "+row);
			Object addRow[]=new Object[5];
			while(rs.next()) {
				for(int i=0; i<addRow.length; i++) addRow[i]=rs.getString(i+1);
				model.addRow(addRow);
			}
			if(table.getRowCount()==row) msg("더 이상의 구매내역은 없습니다.", "안내");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	void action() {
		jB.addActionListener(it -> book.setPanel(new MainPanel()));
		scr.getVerticalScrollBar().addAdjustmentListener(e -> {
			JScrollBar sc=(JScrollBar)e.getAdjustable();
			int extent=sc.getModel().getExtent();
			int maximum=sc.getModel().getMaximum();
			if(extent+sc.getValue()>=maximum-1 && (isClicked==false || table.getSelectedRow()!=-1)) addRow();
		});
		scr.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isClicked=true;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				isClicked=false;
			}
		});
	}

}
