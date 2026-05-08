-- ============================================================
-- NCKH - Hệ thống phản hồi người học
-- Schema SQL Server, tương ứng với JPA entities
-- Có thể mở SQL Server Management Studio (SSMS) và chạy script này
-- LƯU Ý: Phải tự tạo Database có tên NCKH_FeedBack_NguoiHoc_db trước!
-- ============================================================

USE [NCKH_FeedBack_NguoiHoc_db];
GO

-- ------------------------------------------------------------
-- Bảng không phụ thuộc FK
-- ------------------------------------------------------------

CREATE TABLE nguoi_dung (
    nguoi_dung_id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_dang_nhap   VARCHAR(50)  NOT NULL UNIQUE,
    mat_khau        VARCHAR(255) NOT NULL,
    trang_thai      BIT          NOT NULL DEFAULT 1,
    ngay_tao        DATETIME2    NULL
);
GO

CREATE TABLE vai_tro (
    vai_tro_id  BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_vai_tro VARCHAR(50) NOT NULL UNIQUE,
    mo_ta       NVARCHAR(MAX) NULL
);
GO

CREATE TABLE quyen (
    quyen_id    BIGINT IDENTITY(1,1) PRIMARY KEY,
    ma_quyen    VARCHAR(100) NOT NULL UNIQUE,
    mo_ta       NVARCHAR(MAX) NULL
);
GO

CREATE TABLE vai_tro_quyen (
    vai_tro_id BIGINT NOT NULL,
    quyen_id   BIGINT NOT NULL,
    PRIMARY KEY (vai_tro_id, quyen_id),
    CONSTRAINT fk_vtq_vai_tro FOREIGN KEY (vai_tro_id) REFERENCES vai_tro(vai_tro_id) ON DELETE CASCADE,
    CONSTRAINT fk_vtq_quyen FOREIGN KEY (quyen_id) REFERENCES quyen(quyen_id) ON DELETE CASCADE
);
GO

CREATE TABLE nguoi_dung_vai_tro (
    nguoi_dung_id BIGINT NOT NULL,
    vai_tro_id    BIGINT NOT NULL,
    PRIMARY KEY (nguoi_dung_id, vai_tro_id),
    CONSTRAINT fk_ndvt_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung(nguoi_dung_id) ON DELETE CASCADE,
    CONSTRAINT fk_ndvt_vai_tro FOREIGN KEY (vai_tro_id) REFERENCES vai_tro(vai_tro_id) ON DELETE CASCADE
);
GO

CREATE TABLE khoa (
    khoa_id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_khoa  NVARCHAR(200) NOT NULL,
    mo_ta     NVARCHAR(MAX) NULL
);
GO

CREATE TABLE nhom_dich_vu (
    nhom_dich_vu_id BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_dich_vu     NVARCHAR(200) NOT NULL,
    mo_ta           NVARCHAR(MAX) NULL
);
GO

CREATE TABLE khao_sat (
    khao_sat_id   BIGINT IDENTITY(1,1) PRIMARY KEY,
    ten_khao_sat  NVARCHAR(200) NOT NULL,
    hoc_ky        VARCHAR(20)  NULL,
    nam_hoc       VARCHAR(20)  NULL,
    ngay_tao      DATETIME2    NULL
);
GO

-- ------------------------------------------------------------
-- Bảng phụ thuộc nguoi_dung, khoa
-- ------------------------------------------------------------

CREATE TABLE nguoi_hoc (
    nguoi_hoc_id   BIGINT       IDENTITY(1,1) PRIMARY KEY,
    nguoi_dung_id  BIGINT       NOT NULL UNIQUE,
    gioi_tinh      BIT          NULL,
    nam_sinh       INT          NULL,
    khoa_id        BIGINT       NOT NULL,
    nganh_hoc      NVARCHAR(100) NULL,
    khoa_hoc       VARCHAR(50)  NULL,
    CONSTRAINT fk_nguoi_hoc_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE,
    CONSTRAINT fk_nguoi_hoc_khoa       FOREIGN KEY (khoa_id)       REFERENCES khoa (khoa_id)
);
GO

CREATE TABLE giang_vien (
    giang_vien_id      BIGINT       IDENTITY(1,1) PRIMARY KEY,
    nguoi_dung_id      BIGINT       NOT NULL UNIQUE,
    ho_ten             NVARCHAR(100) NOT NULL,
    hoc_vi             NVARCHAR(50)  NULL,
    khoa_id            BIGINT       NOT NULL,
    so_nam_kinh_nghiem INT          NULL,
    nam_bat_dau        INT          NULL,
    CONSTRAINT fk_giang_vien_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE,
    CONSTRAINT fk_giang_vien_khoa       FOREIGN KEY (khoa_id)       REFERENCES khoa (khoa_id)
);
GO

CREATE TABLE quan_tri (
    quan_tri_id   BIGINT       IDENTITY(1,1) PRIMARY KEY,
    nguoi_dung_id BIGINT       NOT NULL UNIQUE,
    ho_ten        NVARCHAR(100) NOT NULL,
    ghi_chu       NVARCHAR(MAX) NULL,
    CONSTRAINT fk_quan_tri_nguoi_dung FOREIGN KEY (nguoi_dung_id) REFERENCES nguoi_dung (nguoi_dung_id) ON DELETE CASCADE
);
GO

CREATE TABLE hoc_phan (
    hoc_phan_id    BIGINT       IDENTITY(1,1) PRIMARY KEY,
    ten_hoc_phan   NVARCHAR(200) NOT NULL,
    so_tin_chi     INT          NOT NULL,
    khoa_id        BIGINT       NOT NULL,
    CONSTRAINT fk_hoc_phan_khoa FOREIGN KEY (khoa_id) REFERENCES khoa (khoa_id)
);
GO

-- ------------------------------------------------------------
-- Phân công (giang_vien, hoc_phan)
-- ------------------------------------------------------------

CREATE TABLE phan_cong (
    phan_cong_id   BIGINT      IDENTITY(1,1) PRIMARY KEY,
    giang_vien_id  BIGINT      NOT NULL,
    hoc_phan_id    BIGINT      NOT NULL,
    hoc_ky         VARCHAR(20) NULL,
    nam_hoc        VARCHAR(20) NULL,
    CONSTRAINT fk_phan_cong_giang_vien FOREIGN KEY (giang_vien_id) REFERENCES giang_vien (giang_vien_id) ON DELETE CASCADE,
    CONSTRAINT fk_phan_cong_hoc_phan   FOREIGN KEY (hoc_phan_id)   REFERENCES hoc_phan (hoc_phan_id)   ON DELETE CASCADE
);
GO

-- ------------------------------------------------------------
-- Câu hỏi (khao_sat, nhom_dich_vu)
-- ------------------------------------------------------------

CREATE TABLE cau_hoi (
    cau_hoi_id      BIGINT       IDENTITY(1,1) PRIMARY KEY,
    khao_sat_id     BIGINT       NOT NULL,
    nhom_dich_vu_id BIGINT       NOT NULL,
    noi_dung        NVARCHAR(MAX) NOT NULL,
    loai_thang_do   VARCHAR(50)  NULL,
    CONSTRAINT fk_cau_hoi_khao_sat     FOREIGN KEY (khao_sat_id)     REFERENCES khao_sat (khao_sat_id)     ON DELETE CASCADE,
    CONSTRAINT fk_cau_hoi_nhom_dich_vu FOREIGN KEY (nhom_dich_vu_id) REFERENCES nhom_dich_vu (nhom_dich_vu_id)
);
GO

-- ------------------------------------------------------------
-- Phản hồi (nguoi_hoc, khao_sat)
-- ------------------------------------------------------------

CREATE TABLE phan_hoi (
    phan_hoi_id   BIGINT    IDENTITY(1,1) PRIMARY KEY,
    nguoi_hoc_id  BIGINT    NOT NULL,
    khao_sat_id   BIGINT    NOT NULL,
    thoi_gian_gui DATETIME2  NULL,
    CONSTRAINT fk_phan_hoi_nguoi_hoc FOREIGN KEY (nguoi_hoc_id) REFERENCES nguoi_hoc (nguoi_hoc_id) ON DELETE CASCADE,
    CONSTRAINT fk_phan_hoi_khao_sat  FOREIGN KEY (khao_sat_id)  REFERENCES khao_sat (khao_sat_id)  ON DELETE CASCADE
);
GO

-- ------------------------------------------------------------
-- Chi tiết phản hồi (phan_hoi, cau_hoi)
-- ------------------------------------------------------------

CREATE TABLE chi_tiet_phan_hoi (
    chi_tiet_id   BIGINT  IDENTITY(1,1) PRIMARY KEY,
    phan_hoi_id   BIGINT  NOT NULL,
    cau_hoi_id    BIGINT  NOT NULL,
    diem_danh_gia INT     NOT NULL,
    y_kien_khac   NVARCHAR(MAX) NULL,
    CONSTRAINT fk_ctph_phan_hoi FOREIGN KEY (phan_hoi_id) REFERENCES phan_hoi (phan_hoi_id) ON DELETE CASCADE,
    CONSTRAINT fk_ctph_cau_hoi  FOREIGN KEY (cau_hoi_id)  REFERENCES cau_hoi (cau_hoi_id)
);
GO

-- ------------------------------------------------------------
-- Index hỗ trợ tra cứu
-- ------------------------------------------------------------

CREATE INDEX idx_nguoi_dung_ten_dang_nhap ON nguoi_dung (ten_dang_nhap);
CREATE INDEX idx_phan_hoi_nguoi_hoc     ON phan_hoi (nguoi_hoc_id);
CREATE INDEX idx_phan_hoi_khao_sat      ON phan_hoi (khao_sat_id);
CREATE INDEX idx_chi_tiet_phan_hoi_ph   ON chi_tiet_phan_hoi (phan_hoi_id);
CREATE INDEX idx_chi_tiet_phan_hoi_ch   ON chi_tiet_phan_hoi (cau_hoi_id);
GO
