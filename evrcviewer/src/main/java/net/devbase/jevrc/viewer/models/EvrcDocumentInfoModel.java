package net.devbase.jevrc.viewer.models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONObject;

import net.devbase.jevrc.Reader.ReaderListener;
import net.devbase.jevrc.EvrcCard;

public class EvrcDocumentInfoModel extends AbstractTableModel 
implements ReaderListener{
	private static final long serialVersionUID = -7931171501277486245L;
	final private String[] columnNames = {"Naziv", "Vrednost"};
	JSONObject data;
	
	private String[] keys = {
			"issuing_date",
			"issuing_state",
			"competent_authority",
			"issuing_authority",
			"unambiguous_number",
			"expiry_date",
			"serial_number"
	};
	
	private HashMap<String, String> translations;
	
	public EvrcDocumentInfoModel() {
		this.translations = new HashMap<String, String>();
		
		translations.put("issuing_date", "datum izdavanja");
		translations.put("issuing_state", "država");
		translations.put("competent_authority", "izdaje");
		translations.put("issuing_authority", "izdaje");
		translations.put("unambiguous_number", "broj dozvole");
		translations.put("expiry_date", "važi do");
		translations.put("serial_number", "serijski broj");
		
		data = null;
	}

  public void inserted(final EvrcCard card) {
    try {
    	data = (JSONObject) card.readEvrcInfo().toJSON().get("document");
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
