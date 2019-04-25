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

	// omit code 

	private final JPopupMenu jPopupMenu = new JPopupMenu();

	{

		menuItem.setFont(font);
		jPopupMenu.add(menuItem);
		menuItem.setActionCommand("menuItem");
		menuItem.addActionListener(itemAction);

		jPopupMenu.setLightWeightPopupEnabled(false);
	}


		// trayIcon

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
	
	// String to date

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
