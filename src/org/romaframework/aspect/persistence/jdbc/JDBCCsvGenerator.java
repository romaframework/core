/*
 *
 * Copyright 2010 Luca Molino (luca.molino--AT--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.aspect.persistence.jdbc;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.romaframework.core.Roma;

/**
 * @author molino
 * 
 */
public class JDBCCsvGenerator {

	public static final String	TEXT_DELIMITER	= "\"";
	public static final String	FIELD_DELIMITER	= ";";

	public static void generateCsv(String queryString, Object[] queryParameter, OutputStream out) throws SQLException, IOException {
		Connection conn = ((JDBCDatasource) Roma.persistence()).getConnection();
		try {
			PreparedStatement statement = getStatement(conn, queryString, queryParameter);
			statement.setFetchDirection(ResultSet.FETCH_FORWARD);
			statement.setFetchSize(5000);
			ResultSet result = statement.executeQuery();
			int row = 0;
			int columnCount = result.getMetaData().getColumnCount();

			generateHeaderCsv(out, result.getMetaData(), columnCount);
			row++;

			while (result.next()) {
				generateRowCsv(out, result, columnCount);
				row++;
			}
			result.close();
			statement.close();
		} finally {
			conn.close();
		}

	}

	private static PreparedStatement getStatement(Connection conn, String queryString, Object[] queryParameter) throws SQLException {
		PreparedStatement statement = conn.prepareStatement(queryString);
		if (queryParameter != null) {
			for (int i = 0; i < queryParameter.length; i++) {
				statement.setObject(i + 1, queryParameter[i]);
			}
		}
		return statement;
	}

	private static void generateHeaderCsv(OutputStream stream, ResultSetMetaData metaData, int columnCount) throws SQLException,
			IOException {
		for (int i = 1; i <= columnCount; i++) {
			stream.write(TEXT_DELIMITER.getBytes());
			String header = metaData.getColumnName(i);
			stream.write(header.getBytes());
			stream.write(TEXT_DELIMITER.getBytes());
			stream.write(FIELD_DELIMITER.getBytes());
		}
		stream.write("\n".getBytes());
	}

	private static void generateRowCsv(OutputStream stream, ResultSet result, int columnCount) throws SQLException, IOException {
		for (int i = 1; i <= columnCount; i++) {
			stream.write(TEXT_DELIMITER.getBytes());
			Object value = result.getObject(i);
			stream.write(String.valueOf(value).getBytes());
			stream.write(TEXT_DELIMITER.getBytes());
			stream.write(FIELD_DELIMITER.getBytes());
		}
		stream.write("\n".getBytes());
	}

}
