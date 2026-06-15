document.addEventListener('DOMContentLoaded', () => {
    const interactiveSelector = [
        'button',
        '[role="button"]',
        '.nav-item',
        '.cloud-btn',
        '.circle-btn',
        '.menu-tile',
        '.footer-item',
        '.symptom-row',
        '.option-btn'
    ].join(',');

    document.querySelectorAll(interactiveSelector).forEach((element) => {
        const press = (event) => {
            const rect = element.getBoundingClientRect();
            const point = event.touches ? event.touches[0] : event;
            element.style.setProperty('--press-x', `${point.clientX - rect.left}px`);
            element.style.setProperty('--press-y', `${point.clientY - rect.top}px`);
            element.classList.add('is-pressing');
        };

        const release = () => {
            window.setTimeout(() => element.classList.remove('is-pressing'), 160);
        };

        element.addEventListener('pointerdown', press);
        element.addEventListener('pointerup', release);
        element.addEventListener('pointerleave', release);
        element.addEventListener('touchstart', press, { passive: true });
        element.addEventListener('touchend', release);
    });
});
