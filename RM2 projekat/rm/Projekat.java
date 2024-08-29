package projekat;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.table.*;

import com.ireasoning.protocol.snmp.SnmpPdu;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import com.ireasoning.protocol.snmp.SnmpVarBind;

public class Projekat extends Thread{
	
	class MyModel extends AbstractTableModel{

		private String[] columnNames = {"IP Adresa", "Origin", "AS_Path lista", "NextHop", 
				"MED", "LPAtomicAggregate", "AgregatorAS", "AgregatorAddress"};
		private Object[][] data = new Object[tabela_snmp.getRowCount()][8];
		
		@Override
		public int getRowCount() {
			return tabela_snmp.getRowCount();
			
		}
		
		@Override
		public String getColumnName(int col) {
		        return columnNames[col];
		    }

		@Override
		public int getColumnCount() {
			return br_kolona;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
		
		@Override
		public void setValueAt(Object value, int row, int col) {
	        data[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }
		
		
	}

	class StatusColumnCellRenderer extends DefaultTableCellRenderer {
		  @Override
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		    //Cells are by default rendered as a JLabel.
		    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		    //Get the status for the current row.
		    
		    SnmpVarBind var = tabela_snmp.get(row, 12);
			int vrednost = Integer.parseInt(var.getValue().toString());
		    
		    if (vrednost == 2) {
		      l.setBackground(Color.GREEN);
		    }
		    else {
		    	l.setBackground(Color.WHITE);
		    }
		  //Return the JLabel which renders the cell.
		  return l;

		}
	}
	
	private SnmpSession sesija = null;
	private SnmpTableModel tabela_snmp = null;
	private JTable tabela = null;
	private int br_redova = 0;
	private int br_kolona = 8;
	private JFrame f = null;
	private int sekunde = 10 * 1000;
	
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
	
	public static void showTable(TableModel model)
    {
        JFrame f = new JFrame();
        JTable jt = new JTable(model);
        jt.setAutoCreateColumnsFromModel(true);
        JScrollPane pane = new JScrollPane(jt);
        f.getContentPane().add(pane);
        f.setSize(790, 590);
        f.setVisible(true);
        f.addWindowListener(new java.awt.event.WindowAdapter()
        {
          public void windowClosing(java.awt.event.WindowEvent e)
          {
            System.exit(0);
          }
        });
    }
	
	public void napravi_tabelu() {
		tabela = new JTable(new MyModel());
	}
	
	public void popuni_tabelu() {
		
		SnmpVarBind var = null;
		String tekst = null;
		
		for(int i = 0; i < br_redova; i++) {
			
			//Ip adresa
			var = tabela_snmp.get(i, 2);
			tekst = var.getValue().toString();
			tabela.setValueAt(tekst, i, 0);
			tabela.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());
			
			//Origin
			var = tabela_snmp.get(i, 3);
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
			tabela.setValueAt(tekst, i, 1);
			tabela.getColumnModel().getColumn(1).setCellRenderer(new StatusColumnCellRenderer());
			
			//AS path lista
			var = tabela_snmp.get(i, 4);
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
			tabela.setValueAt(tekst, i, 2);
			tabela.getColumnModel().getColumn(2).setCellRenderer(new StatusColumnCellRenderer());
			
			//Next Hop
			var = tabela_snmp.get(i, 5);
			tekst = var.getValue().toString();
			tabela.setValueAt(tekst, i, 3);
			tabela.getColumnModel().getColumn(3).setCellRenderer(new StatusColumnCellRenderer());
			
			//MED
			var = tabela_snmp.get(i, 6);
			tekst = var.getValue().toString();
			if(!tekst.equals("-1")) tabela.setValueAt(tekst, i, 4);
			tabela.getColumnModel().getColumn(4).setCellRenderer(new StatusColumnCellRenderer());
			
			//LP
			var = tabela_snmp.get(i, 7);
			tekst = var.getValue().toString();
			if(!tekst.equals("-1")) tabela.setValueAt(tekst, i, 5);
			tabela.getColumnModel().getColumn(5).setCellRenderer(new StatusColumnCellRenderer());
			
			//AggregatorAS
			var = tabela_snmp.get(i, 9);
			tekst = var.getValue().toString();
			tabela.setValueAt(tekst, i, 6);
			tabela.getColumnModel().getColumn(6).setCellRenderer(new StatusColumnCellRenderer());
			
			//AggregatorAddress
			var = tabela_snmp.get(i, 10);
			tekst = var.getValue().toString();
			tabela.setValueAt(tekst, i, 7);
			tabela.getColumnModel().getColumn(7).setCellRenderer(new StatusColumnCellRenderer());
		}
		
		
	}
	
	void osvezi() {
		
		
		try {

		
			//SnmpPdu retPdu = null;
			//retPdu = sesija.snmpGetNextRequest("bgp4PathAttrOrigin");

			tabela_snmp = sesija.snmpGetTable("bgp4PathAttrTable");
			br_redova = tabela_snmp.getRowCount();
			
			
			napravi_tabelu();
			f.add(tabela, BorderLayout.CENTER);
			JScrollPane pane = new JScrollPane(tabela);
			pane.setSize(1200, 600);
	        f.getContentPane().add(pane);
	        f.setSize(1200, 600);
	        f.setVisible(true);
	        
	        popuni_tabelu();
		
	        f.revalidate();
	        
			/*
			 * SnmpVarBind var = tabela_snmp.get(1, 4); String tekst =
			 * var.getValue().toString();
			 * 
			 * System.out.println(tekst);
			 * 
			 * tabela_snmp.setTranslateValue(true); tabela_snmp.startPolling(30);
			 * showTable(tabela);
			 * 
			 * System.out.println(retPdu);
			 */
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Projekat() {
		try {
			f = new JFrame();
			f.addWindowListener(new java.awt.event.WindowAdapter()
	        {
	          public void windowClosing(java.awt.event.WindowEvent e)
	          {
	            System.exit(0);
	          }
	        });
			
			//SnmpSession.loadMib("CISCO-PROCESS-MIB");
			SnmpSession.loadMib("BGP4-MIB");
			sesija = new SnmpSession("192.168.10.1", 161, "si2019", "si2019", 1);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			osvezi();
			
			try {
				Thread.sleep(sekunde);
			} catch (InterruptedException e) {e.printStackTrace(); }
			
			f.remove(tabela);
			f.dispose();
			f = new JFrame();
			f.addWindowListener(new java.awt.event.WindowAdapter()
	        {
	          public void windowClosing(java.awt.event.WindowEvent e)
	          {
	            System.exit(0);
	          }
	        });
			
		}
		
	}
	
	
	public static void main(String[] args) {
		Projekat projekat = new Projekat();
		projekat.start();
	}
	
}
