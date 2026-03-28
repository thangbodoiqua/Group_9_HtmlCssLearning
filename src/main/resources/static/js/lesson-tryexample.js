document.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-cm-primary');
    if (btn && btn.textContent.trim().toLowerCase().includes('try it yourself')) {
        const exampleContainer = btn.closest('.lesson-example');
        if (exampleContainer) {
            const codeBlock = exampleContainer.querySelector('.lesson-codeblock code');
            if (codeBlock) {
                e.preventDefault();
                let code = codeBlock.innerText;
                const currentCat = window.markuplyConfig?.currentCategory || 'HTML';
                const compilerUrl = window.markuplyConfig?.compilerUrl || '/compiler';

                const contextTemplate = exampleContainer.querySelector('.lesson-html-context');
                const htmlContext = contextTemplate ? contextTemplate.innerHTML.trim() : '';

                const hasHtmlTags = code.includes('<') && code.includes('>');
                const isCssOnly = !hasHtmlTags && (currentCat === 'CSS' || code.includes('{'));

                if (isCssOnly) {
                    const bodyContent = htmlContext || `
<h1>This is a heading</h1>
<p>This is a paragraph.</p>
<p id="para1">This is a paragraph with id="para1".</p>
<div class="center" style="padding:10px; border:1px solid #ccc;">This is a div with class="center".</div>
                `;

                    code = `<!DOCTYPE html>
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

                } else if (!code.toLowerCase().includes('<html')) {
                    code = `<!DOCTYPE html>
<html>
<body>
<div style="padding: 5px;">${code}</div>
</body>
</html>`;
                }

                const form = document.createElement('form');
                form.method = 'POST';
                form.action = compilerUrl;

                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'code';
                input.value = code;

                form.appendChild(input);
                document.body.appendChild(form);
                form.submit();
            }
        }
    }
});
