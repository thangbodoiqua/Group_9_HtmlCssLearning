require.config({ paths: { 'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.45.0/min/vs' } });

require(['vs/editor/editor.main'], function () {
    const initialCodeContent = document.getElementById('initialCode').textContent;

    window.editor = monaco.editor.create(document.getElementById('editorContainer'), {
        value: initialCodeContent,
        language: 'html',
        theme: 'vs-light',
        automaticLayout: true,
        minimap: { enabled: false },
        fontSize: 14,
        lineNumbers: 'on',
        scrollBeyondLastLine: false,
        wordWrap: 'on'
    });

    runCode();
});

function formatCode() {
    if (!window.editor) return;
    window.editor.getAction('editor.action.formatDocument').run();
}

function runCode() {
    if (!window.editor) return;
    const htmlContent = window.editor.getValue();
    const previewFrame = document.getElementById('previewFrame').contentWindow.document;

    previewFrame.open();
    previewFrame.write(htmlContent);
    previewFrame.close();
}

function saveCode() {
    if (!window.editor) return;
    const htmlContent = window.editor.getValue();
    const blob = new Blob([htmlContent], { type: 'text/html' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'my_Markuply_project.html';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}
