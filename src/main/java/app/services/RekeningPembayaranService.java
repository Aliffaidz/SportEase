package app.services;

import app.dto.RekeningPembayaranDto;
import app.entities.RekeningPembayaran;
import app.models.pembayaran.RequestAddPaymentMethod;
import app.repositories.RekeningPembayaranRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RekeningPembayaranService {

    private RekeningPembayaranRepository rekeningPembayaranRepository;

    public RekeningPembayaranService(RekeningPembayaranRepository rekeningPembayaranRepository){
        this.rekeningPembayaranRepository = rekeningPembayaranRepository;
    }

    public List<RekeningPembayaranDto> getListRekeningPembayaran(){
        return rekeningPembayaranRepository.findAll().stream()
                .map(current -> new RekeningPembayaranDto
                        (current.getBankName(),current.getUserName(),current.getNoRekening())
                ).toList();
    }


    public void addPaymentMethod(RequestAddPaymentMethod method){
        RekeningPembayaran rekeningPembayaran = new RekeningPembayaran(
                method.bankNameOrEwallet(),
                method.atasNama(),
                method.noRekening());
        rekeningPembayaranRepository.save(rekeningPembayaran);
    }

    public void editPaymentMethod(RequestAddPaymentMethod updated,String rekeningId){

        RekeningPembayaran rekeningPembayaran = rekeningPembayaranRepository.findById(rekeningId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"rekeing tidak ditermukan")
        );

        if(updated.atasNama() != null){
            rekeningPembayaran.setUserName(updated.atasNama());
        }

        if(updated.noRekening() != null){
            rekeningPembayaran.setNoRekening(updated.noRekening());
        }

        rekeningPembayaranRepository.save(rekeningPembayaran);
    }

    public void deletePaymentMethod(String rekeningId){
        RekeningPembayaran rekeningPembayaran = rekeningPembayaranRepository.findById(rekeningId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"rekeing tidak ditermukan")
        );

        rekeningPembayaranRepository.delete(rekeningPembayaran);
    }


}
