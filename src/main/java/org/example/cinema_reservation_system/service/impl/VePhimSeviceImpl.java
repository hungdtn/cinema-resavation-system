package org.example.cinema_reservation_system.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.cinema_reservation_system.dto.VePhimDto;
import org.example.cinema_reservation_system.entity.VePhim;
import org.example.cinema_reservation_system.repository.VePhimRepo;
import org.example.cinema_reservation_system.service.VePhimService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VePhimSeviceImpl implements VePhimService {
    private final VePhimRepo vePhimRepo;
    private ModelMapper modelMapper;

    public VePhimSeviceImpl(VePhimRepo vePhimRepo, ModelMapper modelMapper) {
        this.vePhimRepo = vePhimRepo;
        this.modelMapper = modelMapper;

        // Create TypeMap for VePhim to VePhimDto
        TypeMap<VePhim, VePhimDto> propertyTypeMap = modelMapper.createTypeMap(VePhim.class, VePhimDto.class);
        propertyTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getPhim().getTenPhim(), VePhimDto::setTenPhim);
            mapper.map(src -> src.getNhanVien().getTenNhanVien(), VePhimDto::setTenNhanVien);
            mapper.map(src -> src.getSuatChieu().getNgayChieu(), VePhimDto::setNgayChieu);
            mapper.map(src -> src.getSuatChieu().getGioChieu(), VePhimDto::setGioChieu);
            mapper.map(src -> src.getGheNgoi().getSoGhe(), VePhimDto::setSoGhe);
            mapper.map(src -> src.getSuatChieu().getTenSuatChieu(), VePhimDto::setTenSuatChieu);
            mapper.map(src -> src.getKhachHang().getTenKhachHang(), VePhimDto::setTenKhachHang);
            mapper.map(src -> src.getKhachHang().getSoDienThoaiKhachHang(), VePhimDto::setSoDienThoaiKhachHang);
        });
    }

    @Override
    public List<VePhimDto> getAllVePhim() {
        return vePhimRepo.findAll().stream()
                .map(vePhim -> modelMapper.map(vePhim, VePhimDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VePhim> getVePhim() {
        return vePhimRepo.findAll();
    }

    @Override
    public VePhim createVePhim(VePhim vePhim) {
        vePhimRepo.insertVePhimNative(
                vePhim.getGheNgoi().getIdGheNgoi(),
                vePhim.getGiaVe(),
                vePhim.getHoaDon().getIdHoaDon(),
                vePhim.getKhachHang().getIdKhachHang(),
                java.sql.Date.valueOf(vePhim.getNgayDat()),
                vePhim.getNhanVien().getIdNhanVien(),
                vePhim.getPhim().getIdPhim(),
                vePhim.getSuatChieu().getIdSuatChieu(),
                vePhim.getTrangThai().name() // enum → String
        );

        return vePhim;
    }

    @Override
    public VePhimDto getVePhimDtoById(Integer id) {
        VePhim vePhim = vePhimRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vé phim với id: " + id));
        return modelMapper.map(vePhim, VePhimDto.class);
    }
}
