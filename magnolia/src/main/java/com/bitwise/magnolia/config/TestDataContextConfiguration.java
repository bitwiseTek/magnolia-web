package com.bitwise.magnolia.config;
/**
 *  
 * @author Sika Kay
 * @date 10/06/16
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import com.bitwise.magnolia.domain.support.InitialDataSetup;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataContextConfiguration {
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Bean(initMethod = "initialize")
	public InitialDataSetup setupData() {
		return new InitialDataSetup(new TransactionTemplate(this.transactionManager));
	}

}
