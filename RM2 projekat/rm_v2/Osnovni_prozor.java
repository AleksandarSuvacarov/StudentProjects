package projekat;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;

import com.ireasoning.protocol.TimeoutException;
import com.ireasoning.protocol.snmp.SnmpPdu;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import com.ireasoning.protocol.snmp.SnmpVarBind;



@SuppressWarnings("serial")
public class Osnovni_prozor extends Frame implements Runnable{
	
	private SnmpSession sesija = null;
	private SnmpTableModel tabela_snmp = null;
	private int br_redova = 0;
	private int br_redova_grid = 20;
	private int br_kolona = 9;
	private int sekunde = 10 * 1000;
	private MojaLabela labele[][];
	private Thread nit;
	boolean kraj = false;
	
	void postavi_zaglavlje() {
		
		labele[0][0].setText("IP Adresa");
		labele[0][0].setBackground(Color.GRAY);
		labele[0][0].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][1].setText("Origin");
		labele[0][1].setBackground(Color.GRAY);
		labele[0][1].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][2].setText("AS Path");
		labele[0][2].setBackground(Color.GRAY);
		labele[0][2].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][3].setText("Next Hop");
		labele[0][3].setBackground(Color.GRAY);
		labele[0][3].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][4].setText("MED");
		labele[0][4].setBackground(Color.GRAY);
		labele[0][4].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][5].setText("Local Preference");
		labele[0][5].setBackground(Color.GRAY);
		labele[0][5].setFont(new Font("Open Sans", Font.BOLD, 16));

		labele[0][6].setText("Atomic Aggregate");
		labele[0][6].setBackground(Color.GRAY);
		labele[0][6].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][7].setText("Agregator AS");
		labele[0][7].setBackground(Color.GRAY);
		labele[0][7].setFont(new Font("Open Sans", Font.BOLD, 16));
		
		labele[0][8].setText("Agregator Address");
		labele[0][8].setBackground(Color.GRAY);
		labele[0][8].setFont(new Font("Open Sans", Font.BOLD, 16));
		
	}
	
	private void populateWindow() {
		labele = new MojaLabela[br_redova_grid][br_kolona];
		JPanel GUI= new JPanel(new GridLayout(br_redova_grid, br_kolona, 0, 0));
		JPanel GlavniGUI= new JPanel(new BorderLayout());
		MojaLabela TekstGore= new MojaLabela("BGP Tabela");
		
		GUI.setBackground(Color.WHITE);
		for (int i = 0; i < br_redova_grid;i++) {
			for (int j = 0; j < br_kolona; j++) {
				labele[i][j] = new MojaLabela();
				GUI.add(labele[i][j]);
			}
		}
		
		postavi_zaglavlje();
		
		GlavniGUI.add(GUI, BorderLayout.CENTER);
		GlavniGUI.add(TekstGore, BorderLayout.NORTH);
		
//		JPanel donji_panel = new JPanel();
//		donji_panel.setBackground(Color.WHITE);
//		
//		TextField ispis_labele = new TextField(50);
//		ispis_labele.setEditable(false);
//		
//		donji_panel.add(ispis_labele);
//		
//		MojaLabela TekstDole= new MojaLabela("Ruter:");
//		TextField polje = new TextField(20);
//		Button dugme = new Button("Potvrdi");
//		donji_panel.add(TekstDole);
//		donji_panel.add(polje);
//		donji_panel.add(dugme);
//		
//		GlavniGUI.add(donji_panel, BorderLayout.SOUTH);
//		
//		dugme.addActionListener(arg->{
//			String adr = polje.getText();
//			try {
//				sesija = new SnmpSession(adr, 161, "si2019", "si2019", 1);
//				sesija.setTimeout(1000);
//			} catch (IOException e) {e.printStackTrace();}
//			nit.interrupt();
//		});
//		
//		for(int i = 0; i < br_redova_grid; i++) {
//			for(int j = 0; j < br_kolona; j++) {
//				if(true) {
//					labele[i][j].addMouseListener(new MouseAdapter() {
//						
//						@Override
//						public void mouseClicked(MouseEvent e) {
//							ispis_labele.setText(((JLabel)e.getSource()).getText());
//						}
//						
//					});
//				}
//			}
//		}
		
		add(GlavniGUI, BorderLayout.CENTER);
	}
	
	private int pretvori(String s) {
		switch (s) {
			case "A", "a":
				return 10;
			case "B", "b":
				return 11;
			case "C", "c":
				return 12;
			case "D", "d":
				return 13;
			case "E", "e":
				return 14;
			case "F", "f":
				return 15;
		}
		return Integer.parseInt(s);
		
	}
	
	private void osvezi() {
		
		try {
			tabela_snmp = sesija.snmpGetTable("bgp4PathAttrTable");
		} catch (IOException e) {
			System.out.println("Greska! Proverite da li je ip adresa rutera odgovarajuca!");
			kraj = true;
			dispose();
			nit.stop();
		}
		
		br_redova = tabela_snmp.getRowCount();
		
		SnmpVarBind var = null;
		String tekst = null;
		
		
		for(int i = 1; i < br_redova + 1; i++) {
			
			//Ip adresa
			var = tabela_snmp.get(i - 1, 2);
			tekst = var.getValue().toString();
			labele[i][0].setText(tekst);
			
			//Origin
			var = tabela_snmp.get(i - 1, 3);
			switch(var.getValue().toString()) {
				case "1": 
					tekst = "igp";
					break;
				case "2": 
					tekst = "egp";
					break;
				case "3": 
					tekst = "incomplete";
					break;
			}
			labele[i][1].setText(tekst);
			
			//AS path lista
			var = tabela_snmp.get(i - 1, 4);
			tekst = var.getValue().toString();
			String niz[] = tekst.split(" ");
			tekst = "";
			for(int k = 2; k + 1 < niz.length; k = k + 2) {
				char ch1 = niz[k].charAt(2);
				String str1 = String.valueOf(ch1);
				int c1 = pretvori(str1);
				
				char ch2 = niz[k].charAt(3);
				String str2 = String.valueOf(ch2);
				int c2 = pretvori(str2);
				
				char ch3 = niz[k + 1].charAt(2);
				String str3 = String.valueOf(ch3);
				int c3 = pretvori(str3);
				
				char ch4 = niz[k + 1].charAt(3);
				String str4 = String.valueOf(ch4);
				int c4 = pretvori(str4);
				
				c1 = c1 * 16*16*16 + c2*16*16 + c3*16 + c4;
				tekst = tekst + c1 + ((k + 1 == niz.length - 1)? "" : " ");
				
			}
			labele[i][2].setText(tekst);
			
			//Next Hop
			var = tabela_snmp.get(i - 1, 5);
			tekst = var.getValue().toString();
			labele[i][3].setText(tekst);
			
			//MED
			var = tabela_snmp.get(i - 1, 6);
			tekst = var.getValue().toString();
			if(tekst.equals("-1")) labele[i][4].setText("");
			else labele[i][4].setText(tekst);
			
			//LP
			var = tabela_snmp.get(i - 1, 11);
			tekst = var.getValue().toString();
			if(tekst.equals("-1")) labele[i][5].setText("");
			else labele[i][5].setText(tekst);
			
			//AtomicAgregator
			var = tabela_snmp.get(i - 1, 8);
			tekst = var.getValue().toString();
			if(tekst.equals("1")) labele[i][6].setText("lessSpecificRouteNotSelected");
			else if(tekst.equals("2"))labele[i][6].setText("lessSpecificRouteSelected");
			
			//AggregatorAS
			var = tabela_snmp.get(i - 1, 9);
			tekst = var.getValue().toString();
			labele[i][7].setText(tekst);
			
			//AggregatorAddress
			var = tabela_snmp.get(i - 1, 10);
			tekst = var.getValue().toString();
			labele[i][8].setText(tekst);
		}	

		
		for(int i = 1; i < br_redova + 1; i++) {
			for(int j = 0; j < br_kolona; j++) {
				var = tabela_snmp.get(i - 1, 12);
				int vrednost = Integer.parseInt(var.getValue().toString());
				if (vrednost == 2) {
						labele[i][j].setBackground(Color.GREEN);
				    }
				else {
						labele[i][j].setBackground(Color.WHITE);
				    }
			}
		}
		
		
		for(int i = br_redova + 1; i < br_redova_grid; i++) {
			for(int j = 0; j < br_kolona; j++) {
				labele[i][j].setText("");
				labele[i][j].setBackground(Color.WHITE);
			}
		}
		
		revalidate();
	}
	
	
	public Osnovni_prozor() throws HeadlessException {
		//setResizable(false);
		setBounds(50, 50, 1800, 700);
		setTitle("RM2 Projekat");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				kraj = true;
				nit.interrupt();
				dispose();
			}
		});
		
		try {
			SnmpSession.loadMib("BGP4-MIB");
			
			sesija = new SnmpSession("192.168.10.1", 161, "si2019", "si2019", 1);
			sesija.setTimeout(1000);

		} catch (TimeoutException e1) {
			System.out.println("Timeout");
		}catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		
		
		populateWindow();
		setVisible(true);
		
		nit = new Thread(this);
		nit.start();
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			if(kraj) break;		
			osvezi();
			
			try {
				Thread.sleep(sekunde);
			} catch (InterruptedException e) {}
			
		}
		
	}
	
	
	public static void main(String[] args) {
		Osnovni_prozor op = new Osnovni_prozor();
		
	}

	
}
