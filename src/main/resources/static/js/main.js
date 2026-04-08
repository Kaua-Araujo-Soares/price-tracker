/**
 * main.js — Price Tracker
 * Renderiza o gráfico de histórico de preços com Chart.js
 */

function renderPriceChart(data) {
    const ctx = document.getElementById('priceChart');
    if (!ctx || !data) return;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.labels,
            datasets: [{
                label: 'Preço (R$)',
                data: data.values,
                borderColor: '#6366f1',
                backgroundColor: 'rgba(99, 102, 241, 0.1)',
                borderWidth: 2.5,
                pointBackgroundColor: '#6366f1',
                pointBorderColor: '#0f172a',
                pointBorderWidth: 2,
                pointRadius: 5,
                pointHoverRadius: 7,
                tension: 0.35,
                fill: true,
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    backgroundColor: '#1e293b',
                    borderColor: '#334155',
                    borderWidth: 1,
                    titleColor: '#94a3b8',
                    bodyColor: '#f1f5f9',
                    callbacks: {
                        label: ctx => `R$ ${Number(ctx.parsed.y).toFixed(2).replace('.', ',')}`
                    }
                }
            },
            scales: {
                x: {
                    grid: { color: 'rgba(51, 65, 85, 0.5)' },
                    ticks: { color: '#94a3b8', font: { size: 11 } }
                },
                y: {
                    grid: { color: 'rgba(51, 65, 85, 0.5)' },
                    ticks: {
                        color: '#94a3b8',
                        font: { size: 11 },
                        callback: v => `R$ ${Number(v).toFixed(2).replace('.', ',')}`
                    }
                }
            }
        }
    });
}
