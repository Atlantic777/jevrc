package net.devbase.jevrc.viewer.models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONObject;

import net.devbase.jevrc.Reader.ReaderListener;
import net.devbase.jevrc.EvrcCard;

public class EvrcOwnerInfoModel extends AbstractTableModel 
implements ReaderListener{
	private static final long serialVersionUID = -7931171501277486245L;
	final private String[] columnNames = {"Naziv", "Vrednost"};
	JSONObject data;
	
	private String[] keys = {
			"owner_legal_name",
			"owner_address",
			"owner_first_name",
			"owner_personalno"
	};
	
	private HashMap<String, String> translations;
	
	public EvrcOwnerInfoModel() {
		this.translations = new HashMap<String, String>();
		
		translations.put("owner_legal_name", "prezime");
		translations.put("owner_address", "adresa");
		translations.put("owner_first_name", "ime");
		translations.put("owner_personalno", "JMBG");
		
		data = null;
	}

  public void inserted(final EvrcCard card) {
    try {
    	data = (JSONObject) card.readEvrcInfo().toJSON().get("personal");
	} catch (Exception e) {
		data = null;
		e.printStackTrace();
	}
    this.fireTableDataChanged();
  }

  public void removed() {
    System.out.println("Card removed");
    this.data = null;
    this.fireTableDataChanged();
  }

@Override
public int getRowCount() {
	if(this.data != null)
		return keys.length;
	else
		return 0;
}

@Override
public int getColumnCount() {
	return columnNames.length;
}

@Override
public Object getValueAt(int rowIndex, int columnIndex) {
	if(columnIndex == 0) {
		return translations.get(keys[rowIndex]);
	}
	else if(columnIndex == 1) {
		return data.get(keys[rowIndex]);
	}
	else {
		return 0;
	}
}

public String getColumnName(int col) {
	return columnNames[col];
}

}
