package app.services;

import app.dto.admin.PembayaranDto;
import app.entities.Booking;
import app.entities.GambarPembayaran;
import app.entities.Lapangan;
import app.entities.Pembayaran;
import app.repositories.GambarPembayaranRepository;
import app.repositories.PembayaranRepository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class PembayaranService {

    private PembayaranRepository pembayaranRepository;
    private GambarPembayaranRepository gambarPembayaranRepository;
    private final Integer  FIX_SIZE = 10;


    public PembayaranService(PembayaranRepository pembayaranRepository,GambarPembayaranRepository gambarPembayaranRepository){
        this.pembayaranRepository = pembayaranRepository;
        this.gambarPembayaranRepository = gambarPembayaranRepository;

    }

    @Transactional
    public void addPembayaran(Booking booking, String imageLocation, Lapangan lapangan){
        log.info("add data pembayaran ");
        Pembayaran pembayaran = new Pembayaran();

        pembayaran.setIdBooking(booking.getId());
        pembayaran.setLapangan(lapangan);
        pembayaran.setUsername(booking.getUsername());
        pembayaran.setNoHandphone(booking.getHandphone());
        pembayaran.setWaktuBooking(LocalDate.now());

        pembayaranRepository.save(pembayaran);

        GambarPembayaran gambarPembayaran = new GambarPembayaran();
        gambarPembayaran.setIdPembayaran(pembayaran);
        gambarPembayaran.setId(imageLocation);

        gambarPembayaranRepository.save(gambarPembayaran);
    }

    @Transactional(readOnly = true)
    public PagedModel<PembayaranDto> getAll(Integer page,Integer idPembayaran,String fieldName,Integer idBooking,LocalDate tanggal, String username, String noHandphone){
        PageRequest pageRequest = PageRequest.of(page, FIX_SIZE);
        Page<PembayaranDto> pembayaranDtos;
        boolean searchWithName = false;
        if(fieldName != null && !fieldName.isBlank()){
            searchWithName = true;
        }

        if(idPembayaran == null && searchWithName && tanggal == null && username == null && noHandphone == null){
            Page<Pembayaran> pembayarans = pembayaranRepository.findAll(pageRequest);
            List<PembayaranDto> list = pembayarans.stream().map(
                    p -> new PembayaranDto(
                            p.getId(),
                            p.getIdBooking(),
                            p.getLapangan().getNamaLapangan(),
                            p.getUsername(),
                            p.getNoHandphone(),
                            p.getWaktuBooking(),
                            p.getGambarPembayaran().getId()
                    )
            ).toList();
            pembayaranDtos = new Page<>() {
                @Override
                public int getTotalPages() {
                    return pembayarans.getTotalPages();
                }

                @Override
                public long getTotalElements() {
                    return pembayarans.getTotalElements();
                }

                @Override
                public <U> Page<U> map(Function<? super PembayaranDto, ? extends U> converter) {
                    return null;
                }

                @Override
                public int getNumber() {
                    return pembayarans.getNumber();
                }

                @Override
                public int getSize() {
                    return pembayarans.getSize();
                }

                @Override
                public int getNumberOfElements() {
                    return pembayarans.getNumberOfElements();
                }

                @Override
                public List<PembayaranDto> getContent() {
                    return list;
                }

                @Override
                public boolean hasContent() {
                    return pembayarans.hasContent();
                }

                @Override
                public Sort getSort() {
                    return pembayarans.getSort();
                }

                @Override
                public boolean isFirst() {
                    return pembayarans.isFirst();
                }

                @Override
                public boolean isLast() {
                    return pembayarans.isLast();
                }

                @Override
                public boolean hasNext() {
                    return pembayarans.hasNext();
                }

                @Override
                public boolean hasPrevious() {
                    return pembayarans.hasPrevious();
                }

                @Override
                public Pageable nextPageable() {
                    return pembayarans.nextPageable();
                }

                @Override
                public Pageable previousPageable() {
                    return pembayarans.previousPageable();
                }

                @Override
                public Iterator<PembayaranDto> iterator() {
                    return list.stream().iterator();
                }
            };
            return new PagedModel<>(pembayaranDtos);
        }else{
            log.info("query spect pembayaran");
            Specification<Pembayaran> pembayaranSpecification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                if(idBooking != null){
                    log.info("cari id booking " + idBooking);
                    predicates.add(criteriaBuilder.equal(root.get("idBooking"),idBooking));
                }

                if(idPembayaran != null){
                    log.info("cari id " + idPembayaran.toString());
                    predicates.add(criteriaBuilder.equal(root.get("id"),idPembayaran));
                }

                if(fieldName != null && !fieldName.isBlank()){
                    log.info("cari lapangan");
                    predicates.add(criteriaBuilder.equal(root.get("lapangan").get("namaLapangan"),fieldName));
                }

                if (tanggal != null) {
                    log.info("cari tanggal");
                    log.info("tanggal " + tanggal);
                    Expression<LocalDate> dateExpression = criteriaBuilder.function("DATE", LocalDate.class, root.get("waktuBooking"));
                    predicates.add(criteriaBuilder.equal(dateExpression, tanggal));
                }

                if(username != null && !username.isBlank()){
                    log.info("cari username " + username);
                    predicates.add(criteriaBuilder.like(root.get("username"),username));
                }

                if(noHandphone != null && !noHandphone.isBlank()){
                    log.info("cari handphone " + noHandphone);
                    predicates.add(criteriaBuilder.like(root.get("noHandphone"),noHandphone));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
            Page<Pembayaran> all = pembayaranRepository.findAll(pembayaranSpecification,pageRequest);
            List<PembayaranDto> list = all.stream().map(
                    p -> new PembayaranDto(
                            p.getId(),
                            p.getIdBooking(),
                            p.getLapangan().getNamaLapangan(),
                            p.getUsername(),
                            p.getNoHandphone(),
                            p.getWaktuBooking(),
                            p.getGambarPembayaran().getId()
                    )
            ).toList();

            pembayaranDtos = new Page<>() {
                @Override
                public int getTotalPages() {
                    return all.getTotalPages();
                }

                @Override
                public long getTotalElements() {
                    return all.getTotalElements();
                }

                @Override
                public <U> Page<U> map(Function<? super PembayaranDto, ? extends U> converter) {
                    return null;
                }

                @Override
                public int getNumber() {
                    return all.getNumber();
                }

                @Override
                public int getSize() {
                    return all.getSize();
                }

                @Override
                public int getNumberOfElements() {
                    return all.getNumberOfElements();
                }

                @Override
                public List<PembayaranDto> getContent() {
                    return list;
                }

                @Override
                public boolean hasContent() {
                    return all.hasContent();
                }

                @Override
                public Sort getSort() {
                    return all.getSort();
                }

                @Override
                public boolean isFirst() {
                    return all.isFirst();
                }

                @Override
                public boolean isLast() {
                    return all.isLast();
                }

                @Override
                public boolean hasNext() {
                    return all.hasNext();
                }

                @Override
                public boolean hasPrevious() {
                    return all.hasPrevious();
                }

                @Override
                public Pageable nextPageable() {
                    return all.nextPageable();
                }

                @Override
                public Pageable previousPageable() {
                    return all.previousPageable();
                }

                @Override
                public Iterator<PembayaranDto> iterator() {
                    return list.stream().iterator();
                }
            };
            return new PagedModel<>(pembayaranDtos);
        }
    }

    @Transactional
    public void deletePembayaran(Integer idPembayaran){

        Pembayaran pembayaran = pembayaranRepository.findById(idPembayaran).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"data pembayaran dengan id " + idPembayaran + "tidak ditermukan" )
        );

        GambarPembayaran gambarPembayaran = gambarPembayaranRepository.findFirstByIdPembayaran(pembayaran).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"gambar verfikasi pembayaran tidak ditemukan")
        );

        try{
            log.info("gambar image pembayaran " + gambarPembayaran.getId());
            Files.deleteIfExists(Path.of(gambarPembayaran.getId()));
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal menghapus");
        }

        gambarPembayaranRepository.delete(gambarPembayaran);
        pembayaranRepository.delete(pembayaran);
    }
}
