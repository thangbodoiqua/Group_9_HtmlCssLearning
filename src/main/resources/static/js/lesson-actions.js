document.addEventListener("DOMContentLoaded", function () {
    const btnCompleted = document.getElementById("btnMarkCompleted");

    if (btnCompleted && !btnCompleted.disabled) {
        btnCompleted.addEventListener("click", function () {
            const lessonId = this.getAttribute("data-lesson-id");
            const userId = this.getAttribute("data-user-id");

            fetch('/api/lesson/mark-completed', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    'lessonId': lessonId,
                    'userId': userId
                })
            })
            .then(async response => {
                if (response.ok) {
                    btnCompleted.innerHTML = '✔ Completed';
                    btnCompleted.classList.remove('btn-cm-primary');
                    btnCompleted.classList.add('btn-success');
                    btnCompleted.disabled = true;
                    btnCompleted.style.pointerEvents = 'none';
                } else {
                    const text = await response.text();
                    if (text === "Already completed") {
                        alert("Already finished");
                        btnCompleted.innerHTML = '✔ Completed';
                        btnCompleted.classList.remove('btn-cm-primary');
                        btnCompleted.classList.add('btn-success');
                        btnCompleted.disabled = true;
                        btnCompleted.style.pointerEvents = 'none';
                    } else {
                        alert("Error: " + text);
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Cant connect to the server.");
            });
        });
    }
});
