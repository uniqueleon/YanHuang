package org.aztec.deadsea.sql.impl.result;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;

public class MultipleResultSet implements ResultSet {
	
	List<ResultSet> resultList;
	private int recordCursor = 0;
	private int setCursor = 0;
	private int currentRow = 0;
	private int totalSize = 0;
	private int fetchSize = -1;
	public static final String ErrorMessagePrefix = "[DEADSEA JDBC IMPLEMENT]:";
	public static final String UNSURPPORTED_OPERATION_HINT =  ErrorMessagePrefix + " Unsupported operation!";

	public MultipleResultSet(List<ResultSet> resultList) throws SQLException {
		super();
		this.resultList = resultList;
		init();
	}
	
	public void merge(MultipleResultSet mrs) {
		this.resultList.addAll(mrs.getResultList());
		this.totalSize += mrs.totalSize;
	}
	
	private void init() throws SQLException {
		if(CollectionUtils.isNotEmpty(resultList)) {
			while(next()) {
				totalSize += 1;
			}
			beforeFirst();
		}
	}


	public MultipleResultSet() {
		resultList = Lists.newArrayList();
	}
	

	public List<ResultSet> getResultList() {
		return resultList;
	}

	public void setResultList(List<ResultSet> resultList) {
		this.resultList = resultList;
	}
	
	public ResultSet getCurrentResultSet() {

		ResultSet currentSet = resultList.get(setCursor);
		return currentSet;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException(ErrorMessagePrefix + "Unsupported Operation!");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException(ErrorMessagePrefix + "Unsupported Operation!");
	}

	@Override
	public boolean next() throws SQLException {
		ResultSet rs = resultList.get(setCursor);
		boolean hasNext = rs.next();
		if(!hasNext && setCursor < (resultList.size() - 1)) {
			setCursor ++;
			return next();
		}
		else {
			if(hasNext) {
				currentRow ++;
			}
			return hasNext;
		}
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		for(ResultSet rs : resultList) {
			rs.close();
		}
	}

	@Override
	public boolean wasNull() throws SQLException {
		ResultSet currentSet = getCurrentResultSet();
		return currentSet.wasNull();
	}

	@Override
	public String getString(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getString(columnIndex);
	}

	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getBoolean(columnIndex);
	}

	@Override
	public byte getByte(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getByte(columnIndex);
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getShort(columnIndex);
	}

	@Override
	public int getInt(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getInt(columnIndex);
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getLong(columnIndex);
	}

	@Override
	public float getFloat(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getFloat(columnIndex);
	}

	@Override
	public double getDouble(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getDouble(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getBigDecimal(columnIndex);
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getBytes(columnIndex);
	}

	@Override
	public Date getDate(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getDate(columnIndex);
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTime(columnIndex);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTimestamp(columnIndex);
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getAsciiStream(columnIndex);
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getUnicodeStream(columnIndex);
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBinaryStream(columnIndex);
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getString(columnLabel);
	}
	
	

	@Override
	public boolean getBoolean(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBoolean(columnLabel);
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getByte(columnLabel);
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getShort(columnLabel);
	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getInt(columnLabel);
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getLong(columnLabel);
	}

	@Override
	public float getFloat(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getFloat(columnLabel);
	}

	@Override
	public double getDouble(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getDouble(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBigDecimal(columnLabel, scale);
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBytes(columnLabel);
	}

	@Override
	public Date getDate(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getDate(columnLabel);
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTime(columnLabel);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTimestamp(columnLabel);
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getAsciiStream(columnLabel);
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getUnicodeStream(columnLabel);
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBinaryStream(columnLabel);
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		for(ResultSet rs : resultList) {
			rs.clearWarnings();
		}
	}

	@Override
	public String getCursorName() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getCursorName();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getMetaData();
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnLabel);
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return findColumn(columnLabel);
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getCharacterStream(columnIndex);
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getCharacterStream(columnLabel);
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBigDecimal(columnIndex);
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBigDecimal(columnLabel);
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if(setCursor == 0) {
			return getCurrentResultSet().isBeforeFirst();
		}
		return false;
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if(setCursor == resultList.size() - 1) {
			return getCurrentResultSet().isAfterLast();
		}
		return false;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if(setCursor == 0) {
			return getCurrentResultSet().isFirst();
		}
		return false;
	}

	@Override
	public boolean isLast() throws SQLException {
		if(setCursor == resultList.size() - 1) {
			return getCurrentResultSet().isLast();
		}
		return false;
	}

	@Override
	public void beforeFirst() throws SQLException {
		setCursor = 0;
		currentRow = 0;
		for(ResultSet rs : resultList) {
			rs.beforeFirst();
		}
	}

	@Override
	public void afterLast() throws SQLException {
		// TODO Auto-generated method stub
		setCursor = resultList.size();
		currentRow = totalSize;
		for(ResultSet rs : resultList) {
			rs.afterLast();
		}
	}

	@Override
	public boolean first() throws SQLException {
		setCursor = 0;
		for(ResultSet rs : resultList) {
			if(!rs.first()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean last() throws SQLException {
		setCursor = resultList.size();
		for(ResultSet rs : resultList) {
			if(!rs.last()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getRow() throws SQLException {
		
		return currentRow;
	}

	@Override
	public boolean absolute(int row) throws SQLException {
		if(row > 0) {

			beforeFirst();
			for(int i = 0;i < row;i++) {
				if(!next()) {
					return false;
				}
			}
		}
		else {
			afterLast();
			for(int i = 0;i > row;i--) {
				if(!previous()) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean relative(int rows) throws SQLException {
		if(rows > 0) {
			for(int i = 0;i < rows;i++) {
				if(!next()) {
					return false;
				}
			}
		}
		else {
			for(int i = 0;i > rows;i--) {
				if(!previous()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean previous() throws SQLException {
		
		ResultSet rs = resultList.get(setCursor);
		boolean previous = rs.previous();
		if(!previous && setCursor > 0) {
			setCursor --;
			return previous;
		}
		else {
			if(previous) {
				currentRow --;
			}
			return previous;
		}
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		
		for(ResultSet rs : resultList) {
			rs.setFetchDirection(direction);
		}
	}

	@Override
	public int getFetchDirection() throws SQLException {
		Integer direction = null;
		for(ResultSet rs : resultList) {
			if(direction != null) {

				if(direction != rs.getFetchDirection()) {
					throw new SQLException("Multiple result set have not a united direction!");
				}
			}
			else {
				direction = rs.getFetchDirection();
			}
		}
		return direction == null ? ResultSet.FETCH_UNKNOWN : direction;
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		this.fetchSize = rows;
	}

	@Override
	public int getFetchSize() throws SQLException {
		return fetchSize;
	}

	@Override
	public int getType() throws SQLException {
		Integer type = null;
		for(ResultSet rs : resultList) {
			if(type != null) {

				if(type != rs.getType()) {
					throw new SQLException("Multiple result set have not a united type!");
				}
			}
			else {
				type = rs.getType();
			}
		}
		return type == null ? ResultSet.TYPE_FORWARD_ONLY : type;
	}

	@Override
	public int getConcurrency() throws SQLException {
		Integer concurrency = null;
		for(ResultSet rs : resultList) {
			if(concurrency != null) {

				if(concurrency != rs.getConcurrency()) {
					throw new SQLException("Multiple result set have not a united type!");
				}
			}
		}
		return concurrency == null ? ResultSet.CONCUR_READ_ONLY : concurrency;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		for(ResultSet rs : resultList) {
			if(rs.rowUpdated())
				return true;
		}
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		for(ResultSet rs : resultList) {
			if(rs.rowInserted())
				return true;
		}
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		for(ResultSet rs : resultList) {
			if(rs.rowDeleted())
				return true;
		}
		return false;
	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNull(columnIndex);
	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBoolean(columnIndex, x);
	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateByte(columnIndex, x);
	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateShort(columnIndex, x);
	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateInt(columnIndex, x);
	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateLong(columnIndex, x);
	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateFloat(columnIndex, x);
	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateDouble(columnIndex, x);
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBigDecimal(columnIndex, x);
	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateString(columnIndex, x);
	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBytes(columnIndex, x);
	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateDate(columnIndex, x);
	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateTime(columnIndex, x);
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateTimestamp(columnIndex, x);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnIndex, x,length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBinaryStream(columnIndex, x,length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnIndex, x,length);
	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateObject(columnIndex, x,scaleOrLength);
	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateObject(columnIndex, x);
	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNull(columnLabel);
	}

	@Override
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBoolean(columnLabel,x);
	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateByte(columnLabel,x);
	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateShort(columnLabel,x);
	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateInt(columnLabel,x);
	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateLong(columnLabel,x);
	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateFloat(columnLabel,x);
	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateDouble(columnLabel,x);
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBigDecimal(columnLabel,x);
	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateString(columnLabel,x);
	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBytes(columnLabel,x);
	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateDate(columnLabel,x);
	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateTime(columnLabel,x);
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateTimestamp(columnLabel,x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnLabel,x,length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBinaryStream(columnLabel,x,length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnLabel,reader,length);
	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateObject(columnLabel,x,scaleOrLength);
	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateObject(columnLabel,x);
	}

	@Override
	public void insertRow() throws SQLException {
		throw new SQLException(UNSURPPORTED_OPERATION_HINT);
	}

	@Override
	public void updateRow() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateRow();
	}

	@Override
	public void deleteRow() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.deleteRow();
	}

	@Override
	public void refreshRow() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.refreshRow();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.cancelRowUpdates();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw new SQLException(UNSURPPORTED_OPERATION_HINT);
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new SQLException(UNSURPPORTED_OPERATION_HINT);
	}

	@Override
	public Statement getStatement() throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getStatement();
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {

		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnIndex,map);
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getRef(columnIndex);
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBlob(columnIndex);
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getClob(columnIndex);
	}

	@Override
	public Array getArray(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getArray(columnIndex);
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnLabel,map);
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getRef(columnLabel);
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getBlob(columnLabel);
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getClob(columnLabel);
	}

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getArray(columnLabel);
	}

	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getDate(columnIndex,cal);
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getDate(columnLabel,cal);
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTime(columnIndex,cal);
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTime(columnLabel,cal);
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTimestamp(columnIndex,cal);
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getTimestamp(columnLabel,cal);
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getURL(columnIndex);
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getURL(columnLabel);
	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateRef(columnIndex,x);
	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateRef(columnLabel,x);
	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnIndex,x);
	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnLabel,x);
	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnIndex,x);
	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnLabel,x);
	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateArray(columnIndex,x);
	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateArray(columnLabel,x);
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getRowId(columnIndex);
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getRowId(columnLabel);
	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateRowId(columnIndex,x);
	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateRowId(columnLabel,x);
	}

	@Override
	public int getHoldability() throws SQLException {
		Integer holdability = null;
		for(ResultSet rs : resultList) {
			if(holdability != null) {
				if(holdability != rs.getHoldability()) {
					throw new SQLException("Multiple result set have not a united direction!");
				}
			}
			else {
				holdability = rs.getHoldability();
			}
		}
		return holdability == null ? ResultSet.HOLD_CURSORS_OVER_COMMIT : holdability;
	}

	@Override
	public boolean isClosed() throws SQLException {
		for(ResultSet rs : resultList) {
			if(!rs.isClosed()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void updateNString(int columnIndex, String nString) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNString(columnIndex, nString);
	}

	@Override
	public void updateNString(String columnLabel, String nString) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNString(columnLabel, nString);
	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnIndex, nClob);
	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnLabel, nClob);
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNClob(columnIndex);
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNClob(columnLabel);
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getSQLXML(columnIndex);
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getSQLXML(columnLabel);
	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateSQLXML(columnIndex,xmlObject);
	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateSQLXML(columnLabel,xmlObject);
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNString(columnIndex);
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNString(columnLabel);
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNCharacterStream(columnIndex);
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getNCharacterStream(columnLabel);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNCharacterStream(columnIndex,x,length);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNCharacterStream(columnLabel,reader,length);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnIndex,x,length);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnIndex,x,length);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnIndex,x,length);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnLabel,x,length);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBinaryStream(columnLabel,x,length);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnLabel,reader,length);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnIndex,inputStream,length);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnLabel,inputStream,length);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnIndex,reader,length);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnLabel,reader,length);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnIndex,reader,length);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnLabel,reader,length);
	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNCharacterStream(columnIndex,x);
	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNCharacterStream(columnLabel,reader);
	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnIndex,x);
	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBinaryStream(columnIndex,x);
	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnIndex,x);
	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateAsciiStream(columnLabel,x);
	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBinaryStream(columnLabel,x);
	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateCharacterStream(columnLabel,reader);
	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnIndex,inputStream);
	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateBlob(columnLabel,inputStream);
	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnIndex,reader);
	}

	@Override
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateClob(columnLabel,reader);
	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnIndex,reader);
	}

	@Override
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		rs.updateNClob(columnLabel,reader);
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnIndex,type);
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
		ResultSet rs = getCurrentResultSet();
		return rs.getObject(columnLabel,type);
	}

	public void appendResult(ResultSet set) throws SQLException {
		if(set == null) {
			return ;
		}
		int tmpSize = totalSize;
		synchronized (set) {
			set.beforeFirst();
			while(set.next()) {
				tmpSize++;
			}
			set.beforeFirst();
			this.resultList.add(set);
			totalSize = tmpSize;
		}
	}
}
