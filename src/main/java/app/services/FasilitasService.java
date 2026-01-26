package app.services;

import app.dto.admin.FasilitasDto;
import app.entities.Fasilitas;
import app.entities.GambarFasilitas;
import app.models.fasilitas.RequestTambahFasilitas;
import app.models.fasilitas.RequestEditFasilitas;
import app.repositories.FasilitasRepository;
import app.repositories.GambarFasilitasRepository;
import app.utility.SimpanGambarUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class FasilitasService {

    private final GambarFasilitasRepository  gambarFasilitasRepository;
    private final SimpanGambarUtil simpanGambarUtil;
    private final FasilitasRepository fasilitasRepository;

    public FasilitasService(GambarFasilitasRepository gambarFasilitasRepository, SimpanGambarUtil simpanGambarUtil, FasilitasRepository fasilitasRepository){
        this.gambarFasilitasRepository = gambarFasilitasRepository;
        this.simpanGambarUtil = simpanGambarUtil;
        this.fasilitasRepository = fasilitasRepository;
    }

    @Transactional
    public void tambahFasilitas(RequestTambahFasilitas fasilitas, List<MultipartFile> gambar){

        Fasilitas fasilitas1 = new Fasilitas();

        fasilitas1.setName(fasilitas.getName());
        fasilitas1.setDeskripsi(fasilitas.getDeskripsi());
        fasilitas1.setWaktuDitambahkan(LocalDateTime.now());

        try{
            List<GambarFasilitas> gambarFasilitas = simpanGambarUtil.saveImages(gambar, fasilitas1);
            fasilitasRepository.save(fasilitas1);
            gambarFasilitasRepository.saveAll(gambarFasilitas);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal memasukan gambar fasilitas");
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(e.getStatusCode(),e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<FasilitasDto> ambilSemuaFasilitas(){

        List<Fasilitas> all = fasilitasRepository.findAll();

        if(all.isEmpty()){
            return List.of(new FasilitasDto());
        }
        return all.stream().
                map(fasilitas -> new FasilitasDto(
                        fasilitas.getId(),
                        fasilitas.getName(),
                        fasilitas.getDeskripsi(),
                        fasilitas.getGambarFasilitas().stream().map(GambarFasilitas::getId).toList())
                ).toList();
    }

    @Transactional
    public void modifyFacility(RequestEditFasilitas facility, List<MultipartFile> images){
        Fasilitas updatedFasilitas = fasilitasRepository.findById(facility.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"fasilitas tidak ditermukan")
        );

        if(Objects.nonNull(facility.getTitle())){
            updatedFasilitas.setName(facility.getTitle());
        }

        if(Objects.nonNull(facility.getDescription())){
            updatedFasilitas.setDeskripsi(facility.getDescription());
        }

        if(!images.isEmpty()){
            try{
                List<String> listImageToDelete = facility.getGambarList().stream()
                        .filter(currentImg -> currentImg != null && !currentImg.isBlank())
                        .toList();

                List<GambarFasilitas> savedImages = simpanGambarUtil.saveImages(images, updatedFasilitas);
                gambarFasilitasRepository.saveAll(savedImages);

                List<GambarFasilitas> listDeletedImage = gambarFasilitasRepository.findAllById(listImageToDelete);
                simpanGambarUtil.hapusGambarFasilitas(listDeletedImage);
                gambarFasilitasRepository.deleteAllById(listImageToDelete);

            }catch (IOException e){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"gagal menyimpan gambar");
            }catch (ResponseStatusException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"format gambar salah");
            }
        }
        fasilitasRepository.save(updatedFasilitas);
    }

    @Transactional
    public void hapusFasilitas(Integer id){

        Fasilitas fasilitas = fasilitasRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"fasilitas dengan id " + id + " tidak ditemukan")
        );

        List<GambarFasilitas> gambarFasilitas = gambarFasilitasRepository.findAllByFasilitas(fasilitas);

        try{
            simpanGambarUtil.hapusGambarFasilitas(gambarFasilitas);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Gagal menghapus fasilitas");
        }
        gambarFasilitasRepository.deleteAll(gambarFasilitas);
        fasilitasRepository.delete(fasilitas);
    }

}
