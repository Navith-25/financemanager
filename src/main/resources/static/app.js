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

    // --- Initial Load ---
    function loadAllData() {
        loadTransactions();
        loadBudgets();
        loadSavingsGoals();
    }
    loadAllData();
});