package projekat;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class MojaLabela extends JLabel {
	private Border border = BorderFactory.createLineBorder(new Color(1, 1, 1), 1);
	public MojaLabela() {
		this.setBorder(border);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setFont(new Font("Serif", Font.BOLD, 14));
		//this.setFont(new Font("Open Sans", Font.BOLD, 14));
		this.setForeground(Color.BLACK);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(LEFT);
	}
	public MojaLabela(String text) {
		super(text);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setFont(new Font("Open Sans", Font.BOLD, 20));
		this.setForeground(Color.BLACK);
		this.setHorizontalAlignment(SwingConstants.CENTER);
	}
}
