package com.endava.mentorship2022.service;
import com.endava.mentorship2022.exception.TechnicalDetailNotFound;
import com.endava.mentorship2022.model.TechnicalDetail;
import com.endava.mentorship2022.repository.TechnicalDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TechnicalDetailService {

    private final TechnicalDetailRepository technicalDetailRepository;


    public List<TechnicalDetail> findAllTechnicalDetails() {
        return technicalDetailRepository.findAll();
    }

    public TechnicalDetail findByIdTechnicalDetail(Long id) {
        return technicalDetailRepository.findById(id).orElseThrow(() -> new TechnicalDetailNotFound("TechnicalDetail: " + id + " not found."));
    }

    public TechnicalDetail createTechnicalDetail(TechnicalDetail technicalDetail) {
        return technicalDetailRepository.save(technicalDetail);
    }

    public TechnicalDetail updateTechnicalDetail(Long id, TechnicalDetail newTechnicalDetail) {
        TechnicalDetail technicalDetailToUpdate = findByIdTechnicalDetail(id);
        technicalDetailToUpdate.setName(newTechnicalDetail.getName());
        technicalDetailToUpdate.setValue(newTechnicalDetail.getValue());
        technicalDetailToUpdate.setProduct(newTechnicalDetail.getProduct());
        return technicalDetailRepository.save(technicalDetailToUpdate);
    }

    public void deleteByIdTechnicalDetail(Long id) {
        findByIdTechnicalDetail(id);
        technicalDetailRepository.deleteById(id);
    }

    public List<TechnicalDetail> findTechnicalDetailsByProductId(Long id){
        return technicalDetailRepository.findByProductId(id);
    }

}