/**
 * 
 */
package com.manning.sbia.sandbox.chunk;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.manning.sbia.ch02.domain.Product;

/**
 * @author acogoluegnes
 *
 */
public class StagingProductItemWriter implements ItemWriter<Product> {
	
	private JdbcTemplate jdbcTemplate;
	
	public StagingProductItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(final List<? extends Product> items) throws Exception {
		jdbcTemplate.batchUpdate(
				"insert into product (id,name,description,price) values(?,?,?,?)", 
				new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1,items.get(i).getId());
				ps.setString(2,items.get(i).getName());
				ps.setString(3,items.get(i).getDescription());
				ps.setBigDecimal(4,items.get(i).getPrice());
			}
			
			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
		
		jdbcTemplate.batchUpdate(
				"update staging_product set processed = ? where id = ?", 
				new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setBoolean(1,true);
				ps.setString(2,items.get(i).getId());
			}
			
			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
		Thread.sleep(10);
	}
	
}
