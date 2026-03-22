/**
 * compare-utils.js
 * Shared utility for the Compare CSS feature.
 *
 * Usage in each property file:
 *   initCompare();
 *
 * Expects these elements in the DOM:
 *   - [data-option-index]  → buttons/labels in the options panel
 *   - .compare-preview-item → preview divs (one per option)
 */
function initCompare() {
    const optionBtns = document.querySelectorAll('[data-option-index]');
    const previews   = document.querySelectorAll('.compare-preview-item');


    if (!optionBtns.length || !previews.length) {
        console.warn('[compare-utils] No option buttons or preview items found.');
        return;
    }

    function showOption(index) {
        // Show/hide previews
        previews.forEach(function (el, i) {
            el.style.display = i === index ? '' : 'none';
        });

        // Toggle active state on buttons
        optionBtns.forEach(function (btn) {
            btn.classList.remove('compare-option-btn--active');
        });
        if (optionBtns[index]) {
            optionBtns[index].classList.add('compare-option-btn--active');
        }
    }

    // Attach click listeners
    optionBtns.forEach(function (btn, i) {
        btn.addEventListener('click', function () {
            showOption(i);
        });
    });

    // Init: show first option
    showOption(0);
}

// Auto-init on DOMContentLoaded
document.addEventListener('DOMContentLoaded', function () {
    initCompare();
});
