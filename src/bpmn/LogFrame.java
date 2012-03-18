package bpmn;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LogFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private LogList listLog = new LogList();

	private int warningCount = 0; 
	private int errorCount = 0;

	public LogFrame() {
		super(Messages.getString("Protocol.messages")); //$NON-NLS-1$

		create();

		setLocationRelativeTo(null);
	}

	protected void create() {
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(listLog, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		JButton buttonClose = new JButton(Messages.getString("Protocol.close")); //$NON-NLS-1$
		buttonClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		panel.add(buttonClose);
		getContentPane().add(panel, BorderLayout.PAGE_END);

		setSize(400, 400);
		setLocation(new Point(0, 0));
	}

	public void addWarning(final String message) {
		listLog.addWarning(message);
		++warningCount;
	}

	public void addError(final String message) {
		listLog.addError(message);
		++errorCount;
	}

	public void addException(final Exception e) {
		listLog.addError(e.toString());
		++errorCount;
	}

	public boolean hasMessages() {
		return ((warningCount + errorCount) > 0); 
	}

	public boolean hasErrors() {
		return (errorCount > 0);
	}

}
