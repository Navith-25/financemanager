document.addEventListener('DOMContentLoaded', () => {
    // --- Global Elements ---
    const tabs = document.querySelectorAll('.tab-link');
    const contents = document.querySelectorAll('.tab-content');
    const syncButton = document.getElementById('sync-button');
    const syncStatus = document.getElementById('sync-status');

    // --- Form Elements ---
    const txForm = document.getElementById('tx-form');
    const budgetForm = document.getElementById('budget-form');
    const goalForm = document.getElementById('goal-form');

    // --- List Elements ---
    const txList = document.getElementById('tx-list');
    const budgetList = document.getElementById('budget-list');
    const goalList = document.getElementById('goal-list');

    // --- ADDED: Report Elements ---
    const monthlyReportBtn = document.getElementById('report-btn-monthly');
    const monthlyReportOutput = document.getElementById('report-output-monthly');
    const budgetReportBtn = document.getElementById('report-btn-budget');
    const budgetReportOutput = document.getElementById('report-output-budget');
    const savingsReportBtn = document.getElementById('report-btn-savings');
    const savingsReportOutput = document.getElementById('report-output-savings');

    // --- API Helper ---
    const api = {
        get: (url) => fetch(url).then(res => res.json()),
        post: (url, data) => fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        }).then(res => res.json())
    };

    // --- Tab Switching Logic ---
    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            contents.forEach(c => c.classList.remove('active'));
            tab.classList.add('active');
            document.getElementById(tab.dataset.tab).classList.add('active');
        });
    });

    // --- Sync Logic ---
    syncButton.addEventListener('click', async () => {
        syncStatus.textContent = 'Syncing...';
        try {
            const result = await api.post('/api/sync', {});
            syncStatus.textContent = `Sync complete! (T: ${result.transactions}, B: ${result.budgets}, G: ${result.goals})`;
            loadAllData(); // Refresh all lists
        } catch (err) {
            syncStatus.textContent = 'Sync failed.';
        }
    });

    // --- Transaction Logic ---
    txForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            description: document.getElementById('tx-desc').value,
            amount: parseFloat(document.getElementById('tx-amount').value),
            category: document.getElementById('tx-category').value,
            date: document.getElementById('tx-date').value
        };
        await api.post('/api/transactions', data);
        txForm.reset();
        loadTransactions();
    });

    async function loadTransactions() {
        const transactions = await api.get('/api/transactions');
        txList.innerHTML = '';
        transactions.forEach(tx => {
            const sync = tx.isSynced ? 'Synced' : 'Local';
            const syncClass = tx.isSynced ? 'status-synced' : 'status-local';
            txList.innerHTML += `
                <li>
                    <span>${tx.date}: ${tx.description} ($${tx.amount}) [${tx.category}]</span>
                    <span class="${syncClass}">${sync}</span>
                </li>`;
        });
    }

    // --- Budget Logic ---
    budgetForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            category: document.getElementById('budget-category').value,
            amount: parseFloat(document.getElementById('budget-amount').value)
        };
        await api.post('/api/budgets', data);
        budgetForm.reset();
        loadBudgets();
    });

    async function loadBudgets() {
        const budgets = await api.get('/api/budgets');
        budgetList.innerHTML = '';
        budgets.forEach(b => {
            const sync = b.isSynced ? 'Synced' : 'Local';
            const syncClass = b.isSynced ? 'status-synced' : 'status-local';
            budgetList.innerHTML += `
                <li>
                    <span>${b.category}: $${b.amount} / month</span>
                    <span class="${syncClass}">${sync}</span>
                </li>`;
        });
    }

    // --- Savings Goal Logic ---
    goalForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const data = {
            name: document.getElementById('goal-name').value,
            targetAmount: parseFloat(document.getElementById('goal-target').value)
        };
        await api.post('/api/savings-goals', data);
        goalForm.reset();
        loadSavingsGoals();
    });

    async function loadSavingsGoals() {
        const goals = await api.get('/api/savings-goals');
        goalList.innerHTML = '';
        goals.forEach(g => {
            const sync = g.isSynced ? 'Synced' : 'Local';
            const syncClass = g.isSynced ? 'status-synced' : 'status-local';
            goalList.innerHTML += `
                <li>
                    <span>${g.name} ($${g.currentAmount} / $${g.targetAmount})</span>
                    <span class="${syncClass}">${sync}</span>
                </li>`;
        });
    }

    // --- ADDED: Reporting Logic ---

    // Monthly Expense Report
    monthlyReportBtn.addEventListener('click', async () => {
        monthlyReportOutput.innerHTML = 'Loading...';
        try {
            const data = await api.get('/api/reports/monthly-expenses');
            if (data.length === 0) {
                monthlyReportOutput.innerHTML = 'No data found. Try syncing first.';
                return;
            }
            let table = `
                <table>
                    <thead>
                        <tr>
                            <th>Month</th>
                            <th>Category</th>
                            <th>Total Spent</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.map(row => `
                            <tr>
                                <td>${row.month}</td>
                                <td>${row.category}</td>
                                <td>$${row.totalSpent.toFixed(2)}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>`;
            monthlyReportOutput.innerHTML = table;
        } catch (err) {
            monthlyReportOutput.innerHTML = 'Error loading report.';
        }
    });

    // Budget Adherence Report
    budgetReportBtn.addEventListener('click', async () => {
        budgetReportOutput.innerHTML = 'Loading...';
        try {
            const data = await api.get('/api/reports/budget-adherence');
            if (data.length === 0) {
                budgetReportOutput.innerHTML = 'No data found. Try syncing first.';
                return;
            }
            let table = `
                <table>
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Month</th>
                            <th>Budgeted</th>
                            <th>Spent</th>
                            <th>Variance</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.map(row => `
                            <tr>
                                <td>${row.category}</td>
                                <td>${row.month || 'N/A'}</td>
                                <td>$${row.budgetedAmount.toFixed(2)}</td>
                                <td>$${row.actualSpent.toFixed(2)}</td>
                                <td style="color: ${row.variance >= 0 ? 'green' : 'red'}">
                                    $${row.variance.toFixed(2)}
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>`;
            budgetReportOutput.innerHTML = table;
        } catch (err) {
            budgetReportOutput.innerHTML = 'Error loading report.';
        }
    });

    // Savings Progress Report
    savingsReportBtn.addEventListener('click', async () => {
        savingsReportOutput.innerHTML = 'Loading...';
        try {
            const data = await api.get('/api/reports/savings-progress');
            if (data.length === 0) {
                savingsReportOutput.innerHTML = 'No data found. Try syncing first.';
                return;
            }
            let table = `
                <table>
                    <thead>
                        <tr>
                            <th>Goal Name</th>
                            <th>Target</th>
                            <th>Current</th>
                            <th>Progress</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${data.map(row => `
                            <tr>
                                <td>${row.name}</td>
                                <td>$${row.targetAmount.toFixed(2)}</td>
                                <td>$${row.currentAmount.toFixed(2)}</td>
                                <td>${row.progressPercentage.toFixed(1)}%</td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>`;
            savingsReportOutput.innerHTML = table;
        } catch (err) {
            savingsReportOutput.innerHTML = 'Error loading report.';
        }
    });

    // --- Initial Load ---
    function loadAllData() {
        loadTransactions();
        loadBudgets();
        loadSavingsGoals();
    }
    loadAllData();
});