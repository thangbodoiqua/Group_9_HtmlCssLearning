function submitToAi(challengeId, modelId) {
    if (!editor) {
        console.error("Editor not initialized");
        return;
    }

    const currentCode = editor.getValue();
    const btnSubmit = document.getElementById('btnSubmit');
    const originalText = btnSubmit.innerHTML;

    if (!currentCode || currentCode.trim() === "") {
        alert("Please provide some code before submitting.");
        return;
    }

    if (currentCode.trim() === initialCode.trim()) {
        alert("No changes detected. Please try to match the design first.");
        return;
    }
    btnSubmit.disabled = true;
    btnSubmit.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Grading...';

    const payload = {
        challengeId: challengeId,
        userCode: currentCode,
        modelId: modelId || document.getElementById('aiModel').value
    };
   // AJAX - kh bi treo trinh duyet
    fetch('/practice/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) throw new Error('Submission failed');
        return response.json();
    })
    .then(data => {
        if (data.success) {
            window.location.href = `/practice/result/${data.submissionId}`;
        } else {
            alert("Error: " + data.message);
            resetButton();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("Evaluation failed: " + error.message);
        resetButton();
    });

    function resetButton() {
        btnSubmit.disabled = false;
        btnSubmit.innerHTML = originalText;
    }
}
