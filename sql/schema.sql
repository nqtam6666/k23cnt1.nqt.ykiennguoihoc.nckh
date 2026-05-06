-- ============================================================
-- NCKH - Hệ thống phản hồi người học
-- Schema MySQL, tương ứng với JPA entities
-- Chạy: mysql -u root -p < sql/schema.sql
-- Hoặc tạo DB trước: CREATE DATABASE NCKH_FeedBack_NguoiHoc_db
--   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------
-- Bảng không phụ thuộc FK
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS nguoi_dung (
    nguoi_dung_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap   VARCHAR(50)  NOT NULL UNIQUE,
    mat_khau        VARCHAR(255) NOT NULL,
    vai_tro         VARCHAR(20)  NOT NULL COMMENT 'admin, giang_vien, nguoi_hoc',
    trang_thai      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '1: hoạt động, 0: khóa',
    ngay_tao        DATETIME     NULL,
    CONSTRAINT chk_nguoi_dung_vai_tro CHECK (vai_tro IN ('admin', 'giang_vien', 'nguoi_hoc'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS khoa (
    khoa_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_khoa  VARCHAR(200) NOT NULL,
    mo_ta     TEXT         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS nhom_dich_vu (
    nhom_dich_vu_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dich_vu     VARCHAR(200) NOT NULL,
    mo_ta           TEXT         NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS khao_sat (
    khao_sat_id   BIGINT    AUTO_INCREMENT PRIMARY KEY,
    ten_khao_sat  VARCHAR(200) NOT NULL,
    hoc_ky        VARCHAR(20)  NULL,
    nam_hoc       VARCHAR(20)  NULL,
    ngay_tao      DATETIME     NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Bảng phụ thuộc nguoi_dung, khoa
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS nguoi_hoc (
    nguoi_hoc_id   BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id  BIGINT       NOT NULL UNIQUE,
    gioi_tinh      TINYINT(1)   NULL COMMENT '1: nam, 0: nữ',
    nam_sinh       INT          NULL,
    khoa_id        BIGINT       NOT NULL,
    nganh_hoc      VARCHAR(100) NULL,
    khoa_hoc       VARCHAR(50)  NULL,
    CONSTRAINT fk_nguoi_hoc_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE,
    CONSTRAINT fk_nguoi_hoc_khoa       FOREIGN KEY (khoa_id)       REFERENCES khoa (khoa_id)       ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS giang_vien (
    giang_vien_id      BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id      BIGINT       NOT NULL UNIQUE,
    ho_ten             VARCHAR(100) NOT NULL,
    hoc_vi             VARCHAR(50)  NULL,
    khoa_id            BIGINT       NOT NULL,
    so_nam_kinh_nghiem INT          NULL,
    nam_bat_dau        INT          NULL COMMENT 'Năm bắt đầu làm việc',
    CONSTRAINT fk_giang_vien_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE,
    CONSTRAINT fk_giang_vien_khoa       FOREIGN KEY (khoa_id)       REFERENCES khoa (khoa_id)       ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS quan_tri (
    quan_tri_id   BIGINT       AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id BIGINT       NOT NULL UNIQUE,
    ho_ten        VARCHAR(100) NOT NULL,
    ghi_chu       TEXT         NULL,
    CONSTRAINT fk_quan_tri_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS hoc_phan (
    hoc_phan_id    BIGINT       AUTO_INCREMENT PRIMARY KEY,
    ten_hoc_phan   VARCHAR(200) NOT NULL,
    so_tin_chi     INT          NOT NULL,
    khoa_id        BIGINT       NOT NULL,
    CONSTRAINT fk_hoc_phan_khoa FOREIGN KEY (khoa_id) REFERENCES khoa (khoa_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Phân công (giang_vien, hoc_phan)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS phan_cong (
    phan_cong_id   BIGINT      AUTO_INCREMENT PRIMARY KEY,
    giang_vien_id  BIGINT      NOT NULL,
    hoc_phan_id    BIGINT      NOT NULL,
    hoc_ky         VARCHAR(20) NULL,
    nam_hoc        VARCHAR(20) NULL,
    CONSTRAINT fk_phan_cong_giang_vien FOREIGN KEY (giang_vien_id) REFERENCES giang_vien (giang_vien_id) ON DELETE CASCADE,
    CONSTRAINT fk_phan_cong_hoc_phan   FOREIGN KEY (hoc_phan_id)   REFERENCES hoc_phan (hoc_phan_id)   ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Câu hỏi (khao_sat, nhom_dich_vu)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS cau_hoi (
    cau_hoi_id      BIGINT       AUTO_INCREMENT PRIMARY KEY,
    khao_sat_id     BIGINT       NOT NULL,
    nhom_dich_vu_id BIGINT       NOT NULL,
    noi_dung        TEXT         NOT NULL,
    loai_thang_do   VARCHAR(50)  NULL COMMENT 'Likert 1-5',
    CONSTRAINT fk_cau_hoi_khao_sat     FOREIGN KEY (khao_sat_id)     REFERENCES khao_sat (khao_sat_id)     ON DELETE CASCADE,
    CONSTRAINT fk_cau_hoi_nhom_dich_vu FOREIGN KEY (nhom_dich_vu_id) REFERENCES nhom_dich_vu (nhom_dich_vu_id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Phản hồi (nguoi_hoc, khao_sat)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS phan_hoi (
    phan_hoi_id   BIGINT    AUTO_INCREMENT PRIMARY KEY,
    nguoi_hoc_id  BIGINT    NOT NULL,
    khao_sat_id   BIGINT    NOT NULL,
    thoi_gian_gui DATETIME  NULL,
    CONSTRAINT fk_phan_hoi_nguoi_hoc FOREIGN KEY (nguoi_hoc_id) REFERENCES nguoi_hoc (nguoi_hoc_id) ON DELETE CASCADE,
    CONSTRAINT fk_phan_hoi_khao_sat  FOREIGN KEY (khao_sat_id)  REFERENCES khao_sat (khao_sat_id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Chi tiết phản hồi (phan_hoi, cau_hoi)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS chi_tiet_phan_hoi (
    chi_tiet_id   BIGINT  AUTO_INCREMENT PRIMARY KEY,
    phan_hoi_id   BIGINT  NOT NULL,
    cau_hoi_id    BIGINT  NOT NULL,
    diem_danh_gia INT     NOT NULL COMMENT '1-5 Likert',
    y_kien_khac   TEXT    NULL,
    CONSTRAINT fk_ctph_phan_hoi FOREIGN KEY (phan_hoi_id) REFERENCES phan_hoi (phan_hoi_id) ON DELETE CASCADE,
    CONSTRAINT fk_ctph_cau_hoi  FOREIGN KEY (cau_hoi_id)  REFERENCES cau_hoi (cau_hoi_id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- Index hỗ trợ tra cứu (ngoài PK, FK đã có index mặc định)
-- ------------------------------------------------------------

CREATE INDEX idx_nguoi_dung_ten_dang_nhap ON nguoi_dung (ten_dang_nhap);
CREATE INDEX idx_nguoi_dung_vai_tro     ON nguoi_dung (vai_tro);
CREATE INDEX idx_phan_hoi_nguoi_hoc     ON phan_hoi (nguoi_hoc_id);
CREATE INDEX idx_phan_hoi_khao_sat      ON phan_hoi (khao_sat_id);
CREATE INDEX idx_chi_tiet_phan_hoi_ph   ON chi_tiet_phan_hoi (phan_hoi_id);
CREATE INDEX idx_chi_tiet_phan_hoi_ch   ON chi_tiet_phan_hoi (cau_hoi_id);

SET FOREIGN_KEY_CHECKS = 1;
