package app.services;

import app.dto.BookingTime;
import app.dto.admin.LapanganAdminDto;
import app.dto.LapanganDto;
import app.dto.LapanganDtoDetail;
import app.entities.Booking;
import app.entities.GambarLapangan;
import app.entities.JamOprasional;
import app.entities.Lapangan;
import app.entities.enums.BookingStatus;
import app.entities.enums.StatusLapangan;
import app.repositories.BookingRepository;
import app.repositories.GambarLapanganRepository;
import app.models.lapangan.RequestEditLapangan;
import app.models.lapangan.RequestTambahLapangan;
import app.repositories.LapanganRepository;
import app.utility.SimpanGambarUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LapanganService {

    private final LapanganRepository lapanganRepository;
    private final GambarLapanganRepository gambarLapanganRepository;
    private final SimpanGambarUtil lapanganSimpanGambarUtil;
    private final BookingRepository bookingRepository;
    private final Integer DEFAULT_AUTOMATE_DATE = 7;
    @Value("$application.image-locator")
    private String IMAGE_PATH_LOCATION;

    public LapanganService(LapanganRepository lapanganRepository, GambarLapanganRepository gambarLapangan, SimpanGambarUtil lapanganSimpanGambarUtil,BookingRepository bookingRepository){
        this.lapanganRepository = lapanganRepository;
        this.gambarLapanganRepository = gambarLapangan;
        this.lapanganSimpanGambarUtil = lapanganSimpanGambarUtil;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public void addFutsalFutsal(RequestTambahLapangan newLapangan, List<MultipartFile> gambar){

        Lapangan lapangan = new Lapangan();

        lapangan.setNamaLapangan(newLapangan.getNama());
        lapangan.setJenisLapangan(newLapangan.getJenis());
        lapangan.setHargaLapanganPerjam(newLapangan.getHarga());
        lapangan.setStatusLapangan(newLapangan.getStatus());
        lapangan.setDeskripsi(newLapangan.getDeskripsi());
        lapangan.setLokasi(newLapangan.getLokasi());
        lapangan.setJamOprasional(newLapangan.getJamOprasional());
        lapangan.setTanggalBeroperasi(newLapangan.getTanggalOpersional());
        lapangan.setMaximalWaktuBooking(newLapangan.getMaximalWaktuBooking());

        lapanganRepository.save(lapangan);

        try{
            List<GambarLapangan> list = lapanganSimpanGambarUtil.saveImages(gambar, lapangan);
            gambarLapanganRepository.saveAll(list);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"terjadi masalah dalam menyimpan gambar " + e.getMessage());
        }catch (ResponseStatusException es){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,es.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<LapanganAdminDto> getAll() {

        List<Lapangan> lapanganList = lapanganRepository.findAll();


        if (lapanganList.isEmpty()) {
            return List.of(new LapanganAdminDto());
        }

        return lapanganList.stream()
                .map(lapangan -> {
                    List<String> stringsGambar = lapangan.getGambarLapangan().stream()
                            .map(GambarLapangan::getId)
                            .collect(Collectors.toList());
                    return new LapanganAdminDto(
                            lapangan.getId(),
                            lapangan.getNamaLapangan(),
                            lapangan.getJenisLapangan(),
                            lapangan.getStatusLapangan(),
                            lapangan.getHargaLapanganPerjam(),
                            lapangan.getJamOprasional(),
                            lapangan.getLokasi(),
                            lapangan.getDeskripsi(),
                            lapangan.getTanggalBeroperasi(),
                            lapangan.getMaximalWaktuBooking(),
                            stringsGambar
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyFutsalField(Integer id,RequestEditLapangan editLapangan, List<MultipartFile> image){

        Lapangan updatedLapangan = null;

        updatedLapangan = lapanganRepository.ambilSemuaDataDanGambar(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"lapangan dengan id" + id + "tidak ditemukan")
        );

        if(Objects.nonNull(editLapangan.getNama())){
            updatedLapangan.setNamaLapangan(editLapangan.getNama());
        }

        if(Objects.nonNull(editLapangan.getJenis())){
            updatedLapangan.setJenisLapangan(editLapangan.getJenis());
        }

        if(Objects.nonNull(editLapangan.getHarga())){
            updatedLapangan.setHargaLapanganPerjam(editLapangan.getHarga());
        }

        if(Objects.nonNull(editLapangan.getLokasi())){
            updatedLapangan.setLokasi(editLapangan.getLokasi());
        }

        if(Objects.nonNull(editLapangan.getDeskripsi())){
            updatedLapangan.setDeskripsi(editLapangan.getDeskripsi());
        }


        if(Objects.nonNull(editLapangan.getStatus())){
            updatedLapangan.setStatusLapangan(editLapangan.getStatus());
        }

        if(Objects.nonNull(editLapangan.getMaximalWaktuBooking())){
            updatedLapangan.setMaximalWaktuBooking(editLapangan.getMaximalWaktuBooking());
        }

        if(Objects.nonNull(editLapangan.getTanggalOpersional())){

            if(editLapangan.getTanggalOpersional().isBefore(LocalDate.now())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"tanggal operasioanl tidak valid");
            }

            updatedLapangan.setTanggalBeroperasi(editLapangan.getTanggalOpersional());
        }

        if(Objects.nonNull(editLapangan.getJamOprasional())){
            JamOprasional jamOprasional = editLapangan.getJamOprasional();

            if(Objects.nonNull(jamOprasional.getWaktuMulai())){
                updatedLapangan.getJamOprasional().setWaktuMulai(jamOprasional.getWaktuMulai());
            }

            if(Objects.nonNull(jamOprasional.getWaktuSelesai())){
                updatedLapangan.getJamOprasional().setWaktuSelesai(jamOprasional.getWaktuSelesai());
            }
        }
        lapanganRepository.save(updatedLapangan);

        if(!image.isEmpty()){
            try{
                List<GambarLapangan> gambarLapanganList = lapanganSimpanGambarUtil.saveImages(image, updatedLapangan); // simpan gambar baru dilocal
                gambarLapanganRepository.saveAll(gambarLapanganList); // simpan direpo

                for (String currentImage : editLapangan.getGambarList()){
                    System.out.println("data gambar lama ====================" + currentImage);
                }

                List<String> listLocationImageToDelete = editLapangan.getGambarList().stream()
                        .filter(img -> img != null && !img.isBlank())
                        .toList();
                List<GambarLapangan> listImageFromDbToDelete = gambarLapanganRepository.findAllById(listLocationImageToDelete);

                lapanganSimpanGambarUtil.deleteImagesFieldFootball(listImageFromDbToDelete); // menghapus gambar di local
                gambarLapanganRepository.deleteAllById(listLocationImageToDelete); // menghapus gambar di db

            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"terjadi masalah dalam menyimpan gambar " + e.getMessage());
            }catch (ResponseStatusException es){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,es.getMessage());
            }
        }
    }

    @Transactional
    public void deleteField(Integer id){
        Lapangan lapangan = lapanganRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"lapangan dengan id" + id + "tidak ditemukan")
        );

        List<GambarLapangan> allByLapangan = gambarLapanganRepository.findAllByLapanganGambar(lapangan);

        gambarLapanganRepository.deleteAll(allByLapangan);
        lapanganRepository.delete(lapangan);

        try{
            lapanganSimpanGambarUtil.deleteImagesFieldFootball(allByLapangan);
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal menghapus gambar");
        }
    }

    public Lapangan getCurrentFiled(Integer idField){
        return lapanganRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Lapangan tidak ditemukan")
        );
    }

    @Transactional(readOnly = true)
    public List<LapanganDto> getField(LocalDate tanggal) {


        if(tanggal == null){
            tanggal = LocalDate.now();
        }

        List<Lapangan> lapanganList = lapanganRepository.ambilLapanganBerdasarkanStatus(StatusLapangan.TERSEDIA);
        List<LapanganDto> lapanganDtoSets = new ArrayList<>();

        for (Lapangan currentField : lapanganList) {

            List<BookingTime> bookingTimeField = getBookingTimeField(currentField.getId(), tanggal);


            LapanganDto lapanganDto = new LapanganDto();
            lapanganDto.setId(currentField.getId());
            lapanganDto.setFieldName(currentField.getNamaLapangan());
            lapanganDto.setJenisLapangan(currentField.getJenisLapangan());
            lapanganDto.setImage(
                    currentField.getGambarLapangan() != null &&
                            !currentField.getGambarLapangan().isEmpty()
                            ? currentField.getGambarLapangan().getFirst().getId()
                            : null
            );
            lapanganDto.setPrice(currentField.getHargaLapanganPerjam());
            lapanganDto.setJamOprasional(currentField.getJamOprasional());
            lapanganDto.setBookingTimes(bookingTimeField);
            lapanganDto.setMaximalWaktuBooking(currentField.getMaximalWaktuBooking());
            lapanganDto.setTanggalBeroperasi(currentField.getTanggalBeroperasi());

            lapanganDtoSets.add(lapanganDto);
        }

        return lapanganDtoSets;
    }

    public List<BookingTime> getBookingTimeField(Integer idField,LocalDate tanggal){
        log.info("get booking times");
        Lapangan currentField = lapanganRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"lapangan tidak ditemukan")
        );

        List<Booking> bookingList =
                bookingRepository.findAllByDateAndField(tanggal, currentField.getId());

        LocalTime open  = currentField.getJamOprasional().getWaktuMulai();
        LocalTime close = currentField.getJamOprasional().getWaktuSelesai();

        LocalDateTime start = LocalDateTime.of(tanggal, open);
        LocalDateTime end   = LocalDateTime.of(tanggal, close);

        List<BookingTime> bookingTimeSet = new ArrayList<>();

        while (start.isBefore(end)){

            LocalDateTime slotEnd = start.plusHours(1);

            // 🔹 LOOP BIASA → TIDAK ADA ERROR LAMBDA
            boolean bentrok = false;
            for (Booking b : bookingList) {
                if (
                        start.isBefore(b.getWaktuSelesai()) &&
                                slotEnd.isAfter(b.getWaktuMulai())
                ) {
                    bentrok = true;
                    break;
                }
            }

            if (!bentrok) {
                BookingTime bookingTime = new BookingTime();
                bookingTime.setDate(tanggal);
                bookingTime.setStartTime(start.toLocalTime());
                bookingTime.setEndedTime(slotEnd.toLocalTime());

                if(slotEnd.isAfter(LocalDateTime.now())){
                    log.info("waktu tersedia 1 " + start.toString());
                    bookingTime.setBookingStatus(BookingStatus.TERSEDIA);
                }else {
                    log.info("waktu selesai " + start.toString());
                    bookingTime.setBookingStatus(BookingStatus.SELESAI);
                }
                bookingTimeSet.add(bookingTime);
            }else {
                BookingTime bookingTime = new BookingTime();
                bookingTime.setDate(tanggal);
                bookingTime.setStartTime(start.toLocalTime());
                bookingTime.setEndedTime(slotEnd.toLocalTime());

                if(slotEnd.isAfter(LocalDateTime.now())){
                    log.info("waktu terbooking " + start.toString());
                    bookingTime.setBookingStatus(BookingStatus.TERBOOKING);
                }else {
                    log.info("waktu selesai 2 " + start.toString());
                    bookingTime.setBookingStatus(BookingStatus.SELESAI);
                }
                bookingTimeSet.add(bookingTime);
            }

            start = slotEnd;
        }
        return bookingTimeSet;
    }

    @Transactional(readOnly = true)
    public LapanganDtoDetail getDetailField(Integer idField){

        Lapangan currentField = lapanganRepository.findById(idField).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"lapangan tidak ditemukan")
        );

        List<GambarLapangan> currentImagesField = gambarLapanganRepository.findAllByLapanganGambar(currentField);

        return new LapanganDtoDetail(
                currentField.getId(),
                currentField.getNamaLapangan(),
                currentField.getJenisLapangan(),
                currentField.getHargaLapanganPerjam(),
                currentField.getLokasi(),
                currentField.getDeskripsi(),
                currentField.getMaximalWaktuBooking(),
                currentImagesField.stream().map(GambarLapangan::getId).toList()
        );

    }

    @Transactional(readOnly = true)
    public List<String> getNameFields(){
        return lapanganRepository.ambiNamaLapangan();
    }




}
