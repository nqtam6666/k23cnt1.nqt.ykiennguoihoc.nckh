// Dark Mode Toggle Script
(function() {
    // Kiểm tra theme đã lưu hoặc mặc định là light
    const currentTheme = localStorage.getItem('theme') || 'light';
    
    // Áp dụng theme ngay khi load
    if (currentTheme === 'dark') {
        document.documentElement.classList.add('dark');
    }
    
    // Hàm toggle theme
    function toggleTheme() {
        const html = document.documentElement;
        const isDark = html.classList.contains('dark');
        
        if (isDark) {
            html.classList.remove('dark');
            localStorage.setItem('theme', 'light');
        } else {
            html.classList.add('dark');
            localStorage.setItem('theme', 'dark');
        }
    }
    
    // Expose function globally
    window.toggleTheme = toggleTheme;
    
    // Lắng nghe sự kiện khi DOM ready
    document.addEventListener('DOMContentLoaded', function() {
        const themeToggle = document.getElementById('theme-toggle');
        if (themeToggle) {
            themeToggle.addEventListener('click', toggleTheme);
        }
    });
})();

