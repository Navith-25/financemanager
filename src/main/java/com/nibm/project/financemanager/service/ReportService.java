package com.nibm.project.financemanager.service;

import com.nibm.project.financemanager.dto.*;
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
    public List<CategoryExpenseReportDTO> getCategoryExpenseReport() {
        String sql = """
            SELECT
                category,
                SUM(amount) AS "totalSpent",
                (SUM(amount) / (SELECT SUM(amount) FROM CENTRAL_TRANSACTIONS)) * 100 AS "percentageOfTotal"
            FROM
                CENTRAL_TRANSACTIONS
            WHERE
                amount > 0 
            GROUP BY
                category
            ORDER BY
                "totalSpent" DESC
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CategoryExpenseReportDTO.class));
    }
    public List<ForecastedSavingsReportDTO> getForecastedSavingsReport() {
        String sql = """
            WITH MonthlyContributions AS (
                SELECT
                    TO_CHAR(updated_at, 'YYYY-MM') AS month,
                    SUM(current_amount - LAG(current_amount, 1, 0) OVER (PARTITION BY local_id ORDER BY updated_at)) AS net_change
                FROM
                    CENTRAL_SAVINGS_GOALS
                GROUP BY
                    TO_CHAR(updated_at, 'YYYY-MM')
            ),
            AvgSavings AS (
                SELECT AVG(net_change) AS "avgMonthlySave"
                FROM MonthlyContributions
                WHERE net_change > 0
            ),
            CurrentTotal AS (
                SELECT SUM(current_amount) AS "currentTotal" FROM CENTRAL_SAVINGS_GOALS
            )
            SELECT
                TO_CHAR(ADD_MONTHS(TRUNC(SYSDATE, 'MM'), level), 'YYYY-MM') AS "forecastMonth",
                (NVL(C."currentTotal", 0) + (NVL(A."avgMonthlySave", 0) * level)) AS "forecastedSavings"
            FROM
                AvgSavings A, CurrentTotal C
            CONNECT BY level <= 6
        """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ForecastedSavingsReportDTO.class));
    }
}