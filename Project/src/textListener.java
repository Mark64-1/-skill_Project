import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public interface textListener extends DocumentListener{

	void action(DocumentEvent e);
	
	@Override
	default void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		action(e);
	}

	@Override
	default void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		action(e);
	}

	@Override
	default void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		action(e);
	}
	
	
	

}
