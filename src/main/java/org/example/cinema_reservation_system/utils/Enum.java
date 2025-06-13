package org.example.cinema_reservation_system.utils;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Enum {

    public enum TrangThaiRole {
        HOAT_DONG, KHONG_HOAT_DONG
    }

    public enum TrangThaiUserAccount {
        HOAT_DONG, KHONG_HOAT_DONG
    }

    public enum TrangThaiNhanVien {
        HOAT_DONG, KHONG_HOAT_DONG
    }

    public enum TrangThaiThanhToan {
        CHO_XU_LY, HOAN_THANH, DA_HUY
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public enum TrangThaiPhim {
        HOAT_DONG, KHONG_HOAT_DONG
    }

    public enum TrangThaiVoucher {
        HOAT_DONG, HET_HAN, DA_SU_DUNG
    }

    public enum TrangThaiKhachHang {
        HOAT_DONG, KHONG_HOAT_DONG, BI_CAM
    }

    public enum TrangThaiHoaDon {
        CHO_THANH_TOAN, DA_THANH_TOAN, DA_HUY
    }

    public enum TrangThaiVePhim {
        CON_TRONG, DA_BAN, DA_DAT
    }

    public enum TrangThaiGheNgoi {
        CON_TRONG, DA_DAT, DANG_SU_DUNG
    }

    public enum TrangThaiRapChieu {
        HOAT_DONG, KHONG_HOAT_DONG, BAO_TRI
    }

    public enum TrangThaiPhongChieu {
        HOAT_DONG, KHONG_HOAT_DONG, BAO_TRI
    }

    public enum TrangThaiSuatChieu {
        DA_LEN_LICH, DANG_CHIEU, HOAN_THANH, DA_HUY
    }

}
