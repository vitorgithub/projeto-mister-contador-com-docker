'use strict';

document.getElementById('uploadForm').onsubmit = function(event) {
    event.preventDefault();

    const host = 'http://localhost:8080';
    var fileInput = document.querySelector('input[type="file"]');
    var formData = new FormData(this);

    if (fileInput.files.length === 0 || fileInput.files[0].type !== 'application/pdf') {
        alert('Por favor, selecione um arquivo PDF.');
        return;
    }

    fetch(`${host}/api/files/upload`, {
        method: 'POST',
        body: formData,
    })
    .then(async response => {
        if (response.status !== 201) {
            const errorRes = await response.json();
            throw new Error('O servidor retornou um erro: ' + response.status + ' ' + errorRes.message);

        return response.json();
        }})
    .then(data => {
        // Verifica se a resposta é diretamente um array ou se está dentro de uma propriedade 'data'
        const transactions = Array.isArray(data) ? data : data.data;
        if (!transactions || !Array.isArray(transactions)) {
            throw new Error('Formato de dados inesperado.');
        }
        
        displayTransactions(transactions);
    })
    .catch(error => {
        console.log(error)
        document.getElementById('resultados').innerHTML = `<p class="error">Erro no upload</p>`;
    });
};

function displayTransactions(transactions) {
    var resultadosDiv = document.getElementById('resultados');
    resultadosDiv.innerHTML = ''; // Limpar resultados anteriores

    var tabela = '<table><tr><th>Data</th><th>N° DOC</th><th>Histórico</th><th>Lançamentos (R$)</th><th>Saldo (R$)</th></tr>';
    transactions.forEach(transaction => {
        tabela += `<tr><td>${transaction.date}</td><td>${transaction.documentNumber}</td><td>${transaction.history}</td><td>${transaction.amount}</td><td>${transaction.balance}</td></tr>`;
    });
    tabela += '</table>';

    resultadosDiv.innerHTML = tabela; 
}

