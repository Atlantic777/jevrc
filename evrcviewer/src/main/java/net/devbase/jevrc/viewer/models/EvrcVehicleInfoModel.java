package net.devbase.jevrc.viewer.models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.JSONObject;

import net.devbase.jevrc.Reader.ReaderListener;
import net.devbase.jevrc.EvrcCard;

public class EvrcVehicleInfoModel extends AbstractTableModel 
implements ReaderListener{
	private static final long serialVersionUID = -7931171501277486245L;
	final private String[] columnNames = {"Naziv", "Vrednost"};
	JSONObject data;
	
	private String[] keys = {
			"id_number",
			"production_year",
			"max_net_power",
			"seats_number",
			"axies_number",
			"type",
			"first_registration_date",
			"mass",
			"vehicle_category",
			"fuel_type",
			"type_approval_number",
			"commercial_description",
			"color",
			"engine_capacity",
			"load",
			"power_weight_ratio",
			"standing_places_number",
			"max_permissible_laden_mass",
			"make",
			"registration_number"
	};
	
	private HashMap<String, String> translations;
	
	public EvrcVehicleInfoModel() {
		this.translations = new HashMap<String, String>();
		
		translations.put("id_number", "id broj");
		translations.put("production_year", "godina proizvodnje");
		translations.put("max_net_power", "snaga");
		translations.put("seats_number", "broj sedišta");
		translations.put("axies_number", "broj osovina");
		translations.put("type", "tip vozila");
		translations.put("first_registration_date", "prva registracija");
		translations.put("mass", "masa");
		translations.put("vehicle_category", "kategorija vozila");
		translations.put("fuel_type", "gorivo");
		translations.put("type_approval_number", "type_approval_number");
		translations.put("commercial_description", "model");
		translations.put("color", "boja");
		translations.put("engine_capacity", "zapremina motora");
		translations.put("power_weight_ratio", "power_weight_ratio");
		translations.put("standing_places_number", "broj mesta za stajanje");
		translations.put("max_permissible_laden_mass", "nosivost");
		translations.put("make", "proizvođač");
		translations.put("registration_number", "registarska oznaka");
		
		
		data = null;
	}

  public void inserted(final EvrcCard card) {
    try {
    	data = (JSONObject) card.readEvrcInfo().toJSON().get("vehicle");
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
