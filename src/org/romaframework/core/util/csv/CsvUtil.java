package org.romaframework.core.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;

public class CsvUtil {

	/**
	 * Write a csv file on the output stream with all object data.
	 * 
	 * @param objects
	 *          to write.
	 * @param outputStream
	 *          where write.
	 * @param associateNames
	 *          an associative map of names with in the keys the name of headers and in the values the relative field name.
	 */
	public static void write(List<? extends Object> objects, OutputStream outputStream, Map<String, String> associateNames) {
		final Map<String, String> refNames = new HashMap<String, String>();
		for (Map.Entry<String, String> values : associateNames.entrySet()) {
			refNames.put(values.getValue(), values.getKey());
		}

		CsvNameSolver nameSolver = new CsvNameSolver() {

			public String getHeaderName(String fieldName) {
				return refNames.get(fieldName);
			}

			public String getFieldName(String columnHeader) {
				return null;
			}

			public List<String> getFieldToExport() {
				return new ArrayList<String>(refNames.keySet());
			}
		};
		write(objects, outputStream, nameSolver);
	}

	/**
	 * Write a csv file on the output stream with all object data.
	 * 
	 * @param objects
	 *          to write.
	 * @param outputStream
	 *          where write.
	 * @param nameSolver
	 *          a name solver for resolve the csv header an field to insert.
	 */
	public static void write(List<? extends Object> objects, OutputStream outputStream, CsvNameSolver nameSolver) {
		List<String> values = new ArrayList<String>();
		List<String> fields = nameSolver.getFieldToExport();

		for (String fieldName : fields) {
			values.add(nameSolver.getHeaderName(fieldName));
		}

		writeLine(values, outputStream, true);
		for (Object object : objects) {
			if (object != null) {
				values.clear();
				for (String field : fields) {
					Object value = SchemaHelper.getFieldValue(object, field);
					values.add(value == null ? "" : value.toString());
				}
				writeLine(values, outputStream, true);
			}
		}
	}

	/**
	 * Write a line of csv file.
	 * 
	 * @param toWrite
	 *          List of string relative of line to write.
	 * @param outputStream
	 *          the stream where write.
	 * @param quote
	 *          if true quote all field with " character.
	 */
	private static void writeLine(List<String> toWrite, OutputStream outputStream, boolean quote) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < toWrite.size(); i++) {
			String string = toWrite.get(i);
			if (quote) {
				builder.append('"').append(string != null ? string.replace("\"", "\\\"") : string).append('"');
			} else {
				builder.append(string);
			}
			if (i != toWrite.size() - 1) {
				builder.append(',');
			}
		}
		builder.append('\n');
		try {
			outputStream.write(builder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read a csv file.
	 * 
	 * @param schemaClass
	 *          relative schema class of object to fill.
	 * @param input
	 *          the stream to read.
	 * @param associateNames
	 *          an associative map of names with in the keys the name of headers and in the values the relative field name.
	 * @return the list of created and filled object.
	 */
	public static List<Object> read(SchemaClass schemaClass, InputStream input, Map<String, String> associateNames) {

		final Map<String, String> refNames = associateNames;
		CsvNameSolver mapNameResolver = new CsvNameSolver() {

			public String getHeaderName(String fieldName) {
				return null;
			}

			public List<String> getFieldToExport() {
				return null;
			}

			public String getFieldName(String columnHeader) {
				String fieldName = refNames.get(columnHeader);
				if (fieldName == null)
					fieldName = columnHeader;
				return fieldName;
			}
		};
		return read(schemaClass, input, mapNameResolver);
	}

	/**
	 * Read a csv file.
	 * 
	 * @param schemaClass
	 *          relative schema class of object to fill.
	 * @param input
	 *          the stream to read.
	 * @param nameSolver
	 *          the name solver for resolve relativa field name from the field header.
	 * @return the list of created and filled object.
	 */
	public static List<Object> read(SchemaClass schemaClass, InputStream input, CsvNameSolver nameSolver) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			List<String> headers = getRecord(reader);
			List<String> fields = new ArrayList<String>();
			for (String header : headers) {
				String curr = nameSolver.getFieldName(header);
				fields.add(curr);
			}
			List<Object> imported = new ArrayList<Object>();
			List<String> record;
			while (!(record = getRecord(reader)).isEmpty()) {
				Object newObject = EntityHelper.createObject(null, schemaClass);
				for (int i = 0; i < record.size(); i++) {
					if (fields.get(i) != null)
						SchemaHelper.setFieldValue(schemaClass, fields.get(i), newObject, record.get(i));
				}
				imported.add(newObject);
			}
			return imported;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Read a record o csv.
	 * 
	 * @param reader
	 *          reader where read.
	 * @return the list of values of a csv record.
	 */
	private static List<String> getRecord(Reader reader) {
		List<String> record = new ArrayList<String>();
		try {

			boolean inQuoteDouble = false;
			boolean inQuoteSingle = false;
			boolean escape = false;
			int val;
			char cur;
			StringBuilder builder = new StringBuilder();
			while ((val = reader.read()) != '\n' || inQuoteDouble || inQuoteSingle) {
				if (val == -1)
					break;
				cur = (char) val;
				switch (cur) {
				case '\'':
					if (!escape) {
						inQuoteSingle = !inQuoteSingle;
					} else {
						builder.append(cur);
					}
					break;
				case '"':
					if (!escape) {
						inQuoteDouble = !inQuoteDouble;
					} else {
						builder.append(cur);
					}

					break;
				case '\\':
					if (escape)
						builder.append(cur);
					escape = !escape;
					break;
				case ',':
					if (!inQuoteDouble && !inQuoteSingle) {
						record.add(builder.toString());
						builder.setLength(0);
						break;
					} else {
						builder.append(cur);
					}
					break;
				default:
					builder.append(cur);
					break;
				}
				if (cur != '\\' && cur != '"' && escape)
					escape = false;
			}
			if (builder.length() != 0) {
				record.add(builder.toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return record;
	}

}
