document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("noteModal");
    const lessonId = modal?.getAttribute("data-lesson-id");
    const userId = modal?.getAttribute("data-user-id");
    const textarea = document.getElementById("noteTextarea");
    const currentNoteDisplay = document.getElementById("currentNoteDisplay");
    const noteContentDiv = document.getElementById("noteContent");

    let lastNoteContent = ""; // Store the last saved note content

    if (!lessonId) {
        console.warn("No lessonId found for notes feature.");
        return;
    }

    // Function to show premium toasts
    function showToast(message, type = 'success') {
        let container = document.querySelector('.cm-toast-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'cm-toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `cm-toast ${type}`;
        const icon = type === 'success' ? '✓' : '✕';
        
        toast.innerHTML = `
            <div class="cm-toast-icon">${icon}</div>
            <div class="cm-toast-message">${message}</div>
        `;

        container.appendChild(toast);
        
        // Remove toast after animation
        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => toast.remove(), 400); // 400ms for fade out
        }, 3000);
    }

    // Common function to render note in both Modal and Persistent Bar
    function renderNote(content) {
        const persistentContainer = document.getElementById("persistentNoteContainer");
        const persistentContent = document.getElementById("persistentNoteContent");

        if (content && content.trim() !== "") {
            // Update Modal Preview
            if (noteContentDiv) noteContentDiv.textContent = content;
            if (currentNoteDisplay) currentNoteDisplay.classList.remove("d-none");

            // Update Persistent Bar (Thanh Note)
            if (persistentContent) persistentContent.textContent = content;
            if (persistentContainer) {
                persistentContainer.classList.add("show");
                persistentContainer.classList.remove("d-none");
            }
            lastNoteContent = content; // Keep sync
        } else {
            // Hide everything if no content
            if (currentNoteDisplay) currentNoteDisplay.classList.add("d-none");
            if (persistentContainer) {
                persistentContainer.classList.remove("show");
                persistentContainer.classList.add("d-none");
            }
            lastNoteContent = "";
        }
    }

    // Fetch the single note for this lesson
    async function fetchNote() {
        try {
            const response = await fetch(`/api/notes?userId=${userId}&lessonId=${lessonId}`);
            if (response.status === 200) {
                const data = await response.json();
                renderNote(data.content || "");
            } else if (response.status === 204) {
                renderNote("");
            }
        } catch (error) {
            console.error("Error fetching note:", error);
        }
    }

    async function saveNote() {
        const text = textarea?.value?.trim();
        const saveBtn = document.getElementById("saveNoteBtn");

        if (!text) {
            showToast("Please enter some content for your note.", "error");
            return;
        }

        if (saveBtn) {
            saveBtn.disabled = true;
            saveBtn.textContent = "Saving...";
        }

        try {
            const response = await fetch('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    userId: parseInt(userId),
                    lessonId: parseInt(lessonId),
                    content: text
                })
            });

            if (response.ok) {
                const data = await response.json();
                renderNote(data.content || text);
                textarea.value = ""; // Clear after success
                showToast("Note saved successfully!");
                closeModal();
            } else {
                showToast("Failed to save note. Please try again.", "error");
            }
        } catch (error) {
            console.error("Error saving note:", error);
            showToast("Connection error. Could not save note.", "error");
        } finally {
            if (saveBtn) {
                saveBtn.disabled = false;
                saveBtn.textContent = "Save Note";
            }
        }
    }

    function openModal() {
        if (modal) {
            if (textarea) textarea.value = ""; // Clear initially as per request
            modal.classList.add("show");
            textarea?.focus();
            fetchNote(); // Refresh content on open
        }
    }

    function closeModal() {
        if (modal) modal.classList.remove("show");
    }

    // Event Listeners
    document.getElementById("openNoteFab")?.addEventListener("click", openModal);
    document.getElementById("closeNoteBtn")?.addEventListener("click", closeModal);
    document.getElementById("dismissNoteBtn")?.addEventListener("click", closeModal);
    document.getElementById("saveNoteBtn")?.addEventListener("click", saveNote);

    // Auto-populate on focus if empty
    textarea?.addEventListener("focus", function() {
        if (!textarea.value.trim() && lastNoteContent) {
            textarea.value = lastNoteContent;
        }
    });

    modal?.addEventListener("click", function (e) {
        if (e.target === this) closeModal();
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") closeModal();
    });

    // Initialize display with existing content from model (if any)
    const initialContent = document.getElementById("persistentNoteContent")?.textContent?.trim();
    if (initialContent) {
        renderNote(initialContent);
    } else {
        fetchNote(); // Fallback to fetch if not pre-rendered
    }
});
