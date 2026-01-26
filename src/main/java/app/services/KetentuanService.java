package app.services;

import app.entities.Ketentuan;
import app.repositories.KetentuanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class KetentuanService {

    private KetentuanRepository ketentuanRepository;

    public KetentuanService(KetentuanRepository ketentuanRepository){
        this.ketentuanRepository = ketentuanRepository;
    }

    @Transactional
    public void add(String ketentuan){
        ketentuanRepository.save(new Ketentuan(ketentuan));
    }

    @Transactional(readOnly = true)
    public List<Ketentuan> getAll(){
        return ketentuanRepository.findAll();
    }

    @Transactional
    public void update(Integer id, String description){
        Ketentuan ketentuan = ketentuanRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ketentuan tidak ditemukan untuk diupdate")
        );
        ketentuan.setKetentuan(description);
        ketentuanRepository.save(ketentuan);
    }

    @Transactional
    public void delete(Integer id){
        Ketentuan ketentuan = ketentuanRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ketentuan tidak ditemukan untuk dihapus")
        );
        ketentuanRepository.delete(ketentuan);
    }




}
