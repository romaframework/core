package org.romaframework.core.util.csv;

import java.util.List;

/**
 * Translate from the csv column header to field names.
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public interface CsvNameSolver {

	/**
	 * Retrieve the name of field from the column name.
	 * 
	 * @param columnHeader
	 *          the name of column.
	 * @return the name of field.
	 */
	public String getFieldName(String columnHeader);

	/**
	 * Retrieve the name of the header from name of field.
	 * 
	 * @param fieldName
	 *          the name of field.
	 * @return the name of header.
	 */
	public String getHeaderName(String fieldName);
	
	/**
	 * Retrieve the list of field to export.
	 * 
	 * @return return the list of field to export.
	 */
	public List<String> getFieldToExport();
}
