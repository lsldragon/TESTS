import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class BackwardClock extends JFrame {

	private static Properties prop = new Properties();

	private static String day = "";

	static {

		File file = new File("config.properties");
		if (!file.exists()) {
			JOptionPane.showMessageDialog(null, "config.properties file doesn't exits!", "ERROR",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}

		loadProp();

		day = intToString(differentDays(new Date(), stringToDate(prop.getProperty("test.endDate"))));

	}

	private String dayString = "<html><font size=8 color=#E43A50>" + day + "Ìì<html>";

	private static final long serialVersionUID = 1L;

	private Color backgroundColor = new Color(13, 72, 132);
	private Color dayColor = new Color(228, 58, 80);

	JPanel backPanel = null;
	JLabel messageLabel = null;

	JLabel dayLabel = null;
	JPanel dayPanel = null;

	Font font = new Font("consolas", Font.PLAIN, 16);

	Image iconImage = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("icon.png"));

	// Constructor function
	public BackwardClock() {
		initUI();
	}

	private static void loadProp() {

		FileInputStream fis;
		try {

			fis = new FileInputStream("config.properties");
			prop.load(fis);

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROE", JOptionPane.WARNING_MESSAGE);
		}

	}

	private void initUI() {

		backPanel = new JPanel();
		backPanel.setBackground(backgroundColor);

		messageLabel = new JLabel(prop.getProperty("messageLabel.text"));

		dayPanel = new JPanel();
		dayLabel = new JLabel(dayString);
		dayLabel.setForeground(dayColor);
		dayLabel.setBackground(Color.WHITE);
		dayPanel.add(dayLabel);

		backPanel.add(messageLabel);
		backPanel.add(dayPanel, BorderLayout.EAST);

		this.setUndecorated(true);
		this.setIconImage(iconImage);
		this.setAlwaysOnTop(true);
		this.add(backPanel, BorderLayout.CENTER);
		this.pack();
		this.setLocationRelativeTo(null);
		this.addWindowListener(windowAdapter);
		this.addMouseListener(moveAdapter);
		this.addMouseMotionListener(moveAdapter);
	}

	// actions
	private final Action itemAction = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {

			String actionCommand = e.getActionCommand();

			if (actionCommand == "menuItem" || actionCommand == "exitItem") {
                   This is your code //
			} else if (actionCommand == "openItem") {
				if (!isVisible()) {
					setVisible(true);
				}

			} else if (actionCommand == "preferenceItem") {
				This is your code //
			}
		}
	};

	JMenuItem menuItem = new JMenuItem("exit");

	private final JPopupMenu jPopupMenu = new JPopupMenu();

	{

		menuItem.setFont(font);
		jPopupMenu.add(menuItem);
		menuItem.setActionCommand("menuItem");
		menuItem.addActionListener(itemAction);

		jPopupMenu.setLightWeightPopupEnabled(false);
	}

	private final WindowAdapter windowAdapter = new WindowAdapter() {
		TrayIcon trayIcon = null;

		@Override
		public void windowOpened(WindowEvent e) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					for (float i = 0.0f; i <= 1.0f; i += 0.2f) {
						setOpacity(i);
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

		};

		// trayIcon
		PopupMenu popupMenu = new PopupMenu();

		MenuItem openItem = new MenuItem("Open");

		MenuItem exitItem = new MenuItem("Exit");
		MenuItem preferenceItem = new MenuItem("Preference");

		@Override
		public void windowClosing(WindowEvent e) {

			popMenuUI();
			setVisible(false);

			if (SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();

				trayIcon = new TrayIcon(iconImage, prop.getProperty("test.name"), popupMenu);
				trayIcon.setImageAutoSize(true);

				trayIcon.addMouseListener(new MouseAdapter() {

					int i = 1;

					@Override
					public void mouseClicked(MouseEvent e) {
						i++;

						if (i % 2 == 0) {
							setVisible(true);
						} else {
							setVisible(false);
						}

					}
				});

				try {
					tray.add(trayIcon);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
		}

		private void popMenuUI() {

			openItem.setFont(font);
			openItem.setActionCommand("openItem");
			openItem.addActionListener(itemAction);

			exitItem.setFont(font);
			exitItem.setActionCommand("exitItem");
			exitItem.addActionListener(itemAction);

			preferenceItem.setFont(font);
			preferenceItem.setActionCommand("preferenceItem");
			preferenceItem.addActionListener(itemAction);

			popupMenu.add(openItem);
			popupMenu.add(preferenceItem);
			popupMenu.add(exitItem);
		};
	};

	// move the frame
	private final MouseAdapter moveAdapter = new MouseAdapter() {

		int x, y;

		@Override
		public void mousePressed(MouseEvent e) {

			setCursor(new Cursor(Cursor.HAND_CURSOR));

			if (e.getButton() == MouseEvent.BUTTON1) {
				x = e.getX();
				y = e.getY();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
				setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			if (e.isPopupTrigger()) {
				jPopupMenu.show(getContentPane(), e.getX(), e.getY());
			}
		}
	};

	// int To String
	public static String intToString(int dayInterval) {

		String dateString = String.valueOf(dayInterval);
		return dateString;

	}

	// String to date
	public static Date stringToDate(String dateString) {

		Date date = new Date();

		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
		}

		return date;
	}

	// calculate days
	public static int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);

		if (year1 != year2) {
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
					timeDistance += 366;
				} else {
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);

		} else {
			return day2 - day1;
		}
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				new BackwardClock().setVisible(true);

			}
		});
	}
}
