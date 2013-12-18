package actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitSoftPanel implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Save everything
		// Exit the program
		System.exit(0);
	}
}
