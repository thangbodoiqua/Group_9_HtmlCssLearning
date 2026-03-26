document.addEventListener('DOMContentLoaded', function () {
    const noteKey = "lesson_notes_v2_" + window.location.pathname;

    function getNotes() {
        const saved = localStorage.getItem(noteKey);
        return saved ? JSON.parse(saved) : [];
    }

    function saveNotes(notes) {
        localStorage.setItem(noteKey, JSON.stringify(notes));
    }

    function renderNotes() {
        const notes = getNotes();
        const list = document.getElementById("noteList");
        if (!list) return;

        list.innerHTML = notes.map((note, index) => `
            <div class="note-item d-flex justify-content-between align-items-start mb-2 p-2 border rounded bg-light">
                <div class="note-item-text small" style="white-space: pre-wrap; flex: 1;">${note.text}</div>
                <button class="btn btn-sm text-danger ml-2 p-0 border-0 bg-transparent" onclick="deleteNote(${index})">&times;</button>
            </div>
        `).join('');
    }

    window.deleteNote = function (index) {
        const notes = getNotes();
        notes.splice(index, 1);
        saveNotes(notes);
        renderNotes();
    };

    function addNote() {
        const textarea = document.getElementById("noteTextarea");
        const text = textarea?.value?.trim();
        if (!text) return;

        const notes = getNotes();
        notes.push({ text: text, date: new Date().toISOString() });
        saveNotes(notes);
        textarea.value = "";
        renderNotes();
    }

    function openNoteModal() {
        const modal = document.getElementById("noteModal");
        if (modal) {
            modal.classList.add("show");
            document.getElementById("noteTextarea")?.focus();
            renderNotes();
        }
    }

    function closeNoteModal() {
        const modal = document.getElementById("noteModal");
        if (modal) modal.classList.remove("show");
    }

    const titleText = document.querySelector('.lesson-title')?.textContent || document.title;
    const dialogTitle = document.querySelector('.lesson-note-dialog-title');
    if (dialogTitle) dialogTitle.textContent = "My Notes: " + titleText;

    document.getElementById("openNoteFab")?.addEventListener("click", openNoteModal);
    document.getElementById("closeNoteBtn")?.addEventListener("click", closeNoteModal);
    document.getElementById("dismissNoteBtn")?.addEventListener("click", closeNoteModal);
    document.getElementById("saveNoteBtn")?.addEventListener("click", addNote);

    document.getElementById("noteModal")?.addEventListener("click", function (e) {
        if (e.target === this) closeNoteModal();
    });

    document.addEventListener("keydown", function (e) {
        if (e.key === "Escape") closeNoteModal();
    });
});
