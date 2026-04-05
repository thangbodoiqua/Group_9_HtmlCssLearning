function initCompare() {
    const optionBtns = document.querySelectorAll('[data-option-index]');
    const previews   = document.querySelectorAll('.compare-preview-item');


    if (!optionBtns.length || !previews.length) {
        console.warn('[compare-utils] No option buttons or preview items found.');
        return;
    }

    function showOption(index) {
        previews.forEach(function (el, i) {
            el.style.display = i === index ? '' : 'none';
        });

        optionBtns.forEach(function (btn) {
            btn.classList.remove('compare-option-btn--active');
        });
        if (optionBtns[index]) {
            optionBtns[index].classList.add('compare-option-btn--active');
        }
    }

    optionBtns.forEach(function (btn, i) {
        btn.addEventListener('click', function () {
            showOption(i);
        });
    });

    showOption(0);
}

// Auto-init on DOMContentLoaded
document.addEventListener('DOMContentLoaded', function () {
    initCompare();
});
