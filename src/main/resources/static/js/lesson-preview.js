document.addEventListener('DOMContentLoaded', function () {
    function setupGlobalPreviews() {
        const bodies = document.querySelectorAll('.lesson-example-body');

        bodies.forEach(body => {
            let previewPanel = body.querySelector('.lesson-example-preview');
            if (!previewPanel) {
                previewPanel = document.createElement('div');
                previewPanel.className = 'lesson-example-preview';
                previewPanel.innerHTML = `
                    <div class="lesson-example-preview-label">Preview</div>
                    <div class="lesson-example-preview-surface">
                        <iframe class="preview-iframe" style="width: 100%; height: 100%; border: none;"></iframe>
                    </div>
                `;
                body.appendChild(previewPanel);
            }

            const codeBlock = body.querySelector('.lesson-codeblock code');
            if (!codeBlock) return;

            const iframe = previewPanel.querySelector('.preview-iframe');
            if (!iframe) return;

            let code = codeBlock.innerText;
            const hasHtmlTags = code.includes('<') && code.includes('>');
            const isCssOnly = !hasHtmlTags && (window.location.pathname.includes('/css/') || code.includes('{'));

            let finalHtml = '';
            if (isCssOnly) {
                const exampleContainer = body.closest('.lesson-example');
                const contextTemplate = exampleContainer ? exampleContainer.querySelector('.lesson-html-context') : null;
                const bodyContent = contextTemplate ? contextTemplate.innerHTML.trim() : `
 <h1>This is a heading</h1>
<p>This is a paragraph.</p>
<p id="para1">This is a paragraph with id="para1".</p>
<div class="center" style="padding:10px; border:1px solid #ccc;">This is a div with class="center".</div>`;
                finalHtml = `
<!DOCTYPE html>
<html>
<head>
    <style>
        ${code}
    </style>
</head>
<body>
    <div style="padding: 5px;">
        ${bodyContent}
    </div>
</body>
</html>`;
            } else {
                if (!code.toLowerCase().includes('<html')) {
                    finalHtml = `<!DOCTYPE html><html><body><div style="padding: 5px;">${code}</div></body></html>`;
                } else {
                    finalHtml = code;
                }
            }

            const doc = iframe.contentWindow.document;
            doc.open();
            doc.write(finalHtml);
            doc.close();
        });
    }

    setupGlobalPreviews();
});
