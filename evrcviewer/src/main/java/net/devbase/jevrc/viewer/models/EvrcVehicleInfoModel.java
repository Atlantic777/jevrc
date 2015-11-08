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
			"registration_number",
			"engine_id_number",
			"owner_change_restriction"
	};
	
	private HashMap<String, String> translations;
	
	public EvrcVehicleInfoModel() {
		this.translations = new HashMap<String, String>();
		
		translations.put("id_number", "Broj šasije");
		translations.put("production_year", "Godina proizvodnje");
		translations.put("max_net_power", "Snaga motora u kW");
		translations.put("seats_number", "Broj mesta za sedenje");
		translations.put("axies_number", "Broj osovina");
		translations.put("type", "Tip");
		translations.put("first_registration_date", "Datum prve registracije");
		translations.put("mass", "Masa");
		translations.put("vehicle_category", "Vrsta vozila");
		translations.put("fuel_type", "Vrsta goriva ili pogona");
		translations.put("type_approval_number", "Homologacijska oznaka");
		translations.put("commercial_description", "Komercijalna oznaka (model)");
		translations.put("color", "Boja vozila");
		translations.put("engine_capacity", "Radna zapremina motora");
		translations.put("power_weight_ratio", "Odnos snaga/masa u kg/kW (moto)");
		translations.put("standing_places_number", "Broj mesta za stajanje");
		translations.put("max_permissible_laden_mass", "Najveća dozvoljena masa");
		translations.put("make", "Marka");
		translations.put("registration_number", "Registarski broj vozila");
		translations.put("engine_id_number", "Broj motora");
		translations.put("owner_change_restriction", "Zabrana otuđenja vozila do");
		translations.put("load", "Nosivost vozila");
		
		
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
