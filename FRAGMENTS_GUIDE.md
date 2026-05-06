# Hướng dẫn sử dụng Fragments trong Thymeleaf

## Cấu trúc Fragments

Dự án đã được tổ chức với các fragments để tái sử dụng code:

```
src/main/resources/templates/
├── fragments/
│   ├── header.html      # Header navigation
│   ├── sidebar.html     # Sidebar navigation (cho admin)
│   ├── footer.html      # Footer
│   └── messages.html    # Flash messages (success/error)
└── layouts/
    ├── base.html        # Base layout
    └── admin-layout.html # Admin layout với sidebar
```

## Cách sử dụng Fragments

### 1. Sử dụng Header Fragment

```html
<!-- Trong template của bạn -->
<div th:replace="~{fragments/header :: adminHeader('/admin/khao-sat', 'Quản lý Khảo sát', 'fas fa-clipboard-list')}"></div>
```

**Tham số:**
- `backUrl`: URL để quay lại (ví dụ: `/admin/dashboard`)
- `pageTitle`: Tiêu đề trang
- `iconClass`: Class icon FontAwesome (ví dụ: `fas fa-clipboard-list`)

### 2. Sử dụng Sidebar Fragment

```html
<!-- Chỉ dùng cho admin -->
<div th:replace="~{fragments/sidebar :: adminSidebar}"></div>
```

### 3. Sử dụng Messages Fragment

```html
<!-- Hiển thị flash messages -->
<div th:replace="~{fragments/messages :: messages}"></div>
```

### 4. Sử dụng Footer Fragment

```html
<!-- Footer -->
<div th:replace="~{fragments/footer :: footer}"></div>
```

### 5. Sử dụng Admin Layout (Khuyến nghị)

Thay vì include từng fragment, bạn có thể sử dụng layout có sẵn:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" 
      th:replace="~{layouts/admin-layout :: admin-layout(pageTitle='Quản lý Khảo sát', backUrl='/admin/dashboard', iconClass='fas fa-clipboard-list')}">
<th:block th:fragment="content">
    <!-- Nội dung trang của bạn ở đây -->
    <h1>Danh sách Khảo sát</h1>
    <!-- ... -->
</th:block>
</html>
```

## Ví dụ Template hoàn chỉnh

### Template với Fragments riêng lẻ:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="vi" class="h-full">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Khảo sát - Admin</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
</head>
<body class="h-full bg-gray-50 dark:bg-gray-900">
    <!-- Header -->
    <div th:replace="~{fragments/header :: adminHeader('/admin/dashboard', 'Quản lý Khảo sát', 'fas fa-clipboard-list')}"></div>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- Messages -->
        <div th:replace="~{fragments/messages :: messages}"></div>
        
        <!-- Content -->
        <h1 class="text-3xl font-bold text-gray-800 dark:text-white">Danh sách Khảo sát</h1>
        <!-- ... -->
    </div>
    
    <!-- Footer -->
    <div th:replace="~{fragments/footer :: footer}"></div>
</body>
</html>
```

## Lợi ích

1. **Tái sử dụng code**: Chỉ cần sửa một lần ở fragment, tất cả các trang sẽ được cập nhật
2. **Dễ bảo trì**: Thay đổi header/footer/sidebar chỉ cần sửa ở một nơi
3. **Nhất quán**: Đảm bảo giao diện nhất quán trên toàn bộ ứng dụng
4. **Giảm code lặp**: Giảm đáng kể số lượng code trùng lặp

## Cập nhật các Template hiện có

Các template hiện tại có thể được cập nhật để sử dụng fragments bằng cách:

1. Thay thế phần header bằng fragment
2. Thay thế phần messages bằng fragment
3. Thêm sidebar nếu cần
4. Thêm footer nếu cần

Ví dụ: `admin/khao-sat/danh-sach.html` đã được cập nhật để sử dụng layout.

