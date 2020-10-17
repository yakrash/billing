package service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import su.bzz.springcourse.dao.PostgreAccountsDAO;
import su.bzz.springcourse.model.Account;
import su.bzz.springcourse.model.FinancialTransaction;
import su.bzz.springcourse.service.AccountManager;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AccountManagerTest {
    private static AccountManager accountManager;

    @BeforeClass
    public static void beforeClass() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:accounts.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        PostgreAccountsDAO postgreAccountsDAO = new PostgreAccountsDAO(jdbcTemplate);
        accountManager = new AccountManager(postgreAccountsDAO);
        accountManager.createAccount();
        accountManager.createAccount();
        accountManager.createAccount();
    }

    @Test
    public void methodModifyCheckSum() {
        FinancialTransaction financialTransaction = new FinancialTransaction(1, 2, 100);
        Account account1AfterModify = new Account(1, 0, 100);
        Account account2AfterModify = new Account(2, 100, 0);

        accountManager.modify(financialTransaction);
        assertEquals(account1AfterModify, accountManager.getAccount(1));
        assertEquals(account2AfterModify, accountManager.getAccount(2));
    }

    @Test
    public void methodGetTest() {
        Account account = new Account(3, 0, 0);
        assertEquals(account, accountManager.getAccount(3));
    }

    @Test
    public void methodGetWhenAccountNull() {
        assertNull(accountManager.getAccount(99));
    }
}
