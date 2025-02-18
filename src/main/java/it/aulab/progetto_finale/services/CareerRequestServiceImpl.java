package it.aulab.progetto_finale.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import it.aulab.progetto_finale.models.CareerRequest;
import it.aulab.progetto_finale.models.User;
import it.aulab.progetto_finale.repositories.CareerRequestRepository;


@Service
public class CareerRequestServiceImpl implements CareerRequestService {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public boolean isRoleAlreadyAssigned(User user, CareerRequest careerRequest) {
        List<Long> allUserIds = careerRequestRepository.findAllUserIds();
        if (!allUserIds.contains(user.getId())) {
            return false;
        }

        List<Long> reqeusts = careerRequestRepository.findByUserId(user.getId());

        return reqeusts.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }

    public void save(CareerRequest careerRequest, User user) {
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequestRepository.save(careerRequest);

//! email ad ogni richiesta

emailService.sendSimpleEmail("admiAulabpost@admin.com", "Richiesta per il ruolo " + careerRequest.getRole().getName(), "C'è una richiesta di collaborazione da parte di " +user.getUsername());

    }

@Override
public void careerAccept(Long requestId) {
throw new UnsupportedOperationException("Not supported yet.");
}

@Override
public CareerRequest find(Long id) {
throw new UnsupportedOperationException("Not supported yet.");
}


}
