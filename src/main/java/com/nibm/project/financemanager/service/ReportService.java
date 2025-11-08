package com.nibm.project.financemanager.service;

import com.nibm.project.financemanager.dto.BudgetAdherenceReportDTO;
import com.nibm.project.financemanager.dto.MonthlyExpenseReportDTO;
import com.nibm.project.financemanager.dto.SavingsProgressReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Service
@Transactional(transactionManager = "oracleTransactionManager")
public class ReportService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReportService(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
        this.jdbcTemplate = new JdbcTemplate(oracleDataSource);
    }
    public List<MonthlyExpenseReportDTO> getMonthlyExpenseReport() {
        String sql = """
            SELECT
                TO_CHAR(TRANSACTION_DATE, 'YYYY-MM') AS "month",
                category,
                SUM(amount) AS "totalSpent"
            FROM
                CENTRAL_TRANSACTIONS
            GROUP BY
                TO_CHAR(TRANSACTION_DATE, 'YYYY-MM'), category
            ORDER BY
                "month" DESC, "totalSpent" DESC
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MonthlyExpenseReportDTO.class));
    }
    public List<BudgetAdherenceReportDTO> getBudgetAdherenceReport() {
        String sql = """
            WITH MonthlyExpenses AS (
                SELECT
                    TO_CHAR(TRANSACTION_DATE, 'YYYY-MM') AS "month",
                    category,
                    SUM(amount) AS "actualSpent"
                FROM
                    CENTRAL_TRANSACTIONS
                GROUP BY
                    TO_CHAR(TRANSACTION_DATE, 'YYYY-MM'), category
            )
            SELECT
                b.category,
                e."month",
                b.amount AS "budgetedAmount",
                COALESCE(e."actualSpent", 0) AS "actualSpent",
                (b.amount - COALESCE(e."actualSpent", 0)) AS "variance"
            FROM
                CENTRAL_BUDGETS b
            LEFT JOIN
                MonthlyExpenses e ON b.category = e.category
            ORDER BY
                b.category, e."month" DESC
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BudgetAdherenceReportDTO.class));
    }
    public List<SavingsProgressReportDTO> getSavingsProgressReport() {
        String sql = """
            SELECT
                name,
                target_amount AS "targetAmount",
                current_amount AS "currentAmount",
                (current_amount / target_amount) * 100 AS "progressPercentage"
            FROM
                CENTRAL_SAVINGS_GOALS
            WHERE
                target_amount > 0
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SavingsProgressReportDTO.class));
    }
}