package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Model.LoyaltyProgram;
import com.isa.cottages.Repository.LoyaltyProgramRepository;
import com.isa.cottages.Service.LoyaltyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyProgramServiceImpl implements LoyaltyProgramService {

    @Autowired
    private LoyaltyProgramRepository loyaltyRepository;

    @Override
    public Double calculateClientDiscount(Client client) {
        double maxDiscount = 0.4;
        double discount = 0;

        if (client.getLoyaltyProgram().getGoldPoints() > 5) {
            discount += 0.15;
        }
        if (client.getLoyaltyProgram().getSilverPoints() > 5) {
            discount += 0.1;
        }
        if (client.getLoyaltyProgram().getRegularPoints() > 5) {
            discount += 0.05;
        }

        if (discount > maxDiscount) { discount = maxDiscount; }

        return discount;
    }

    public void updateAfterReservation(LoyaltyProgram loyaltyProgram) {
        if ((loyaltyProgram.getSilverPoints()-5 ) >= 0) {
            loyaltyProgram.setSilverPoints(loyaltyProgram.getSilverPoints()-5);
        } else { loyaltyProgram.setSilverPoints(0.0); }

        if ((loyaltyProgram.getGoldPoints()-5 ) >= 0) {
            loyaltyProgram.setGoldPoints(loyaltyProgram.getGoldPoints()-5);
        } else { loyaltyProgram.setGoldPoints(0.0); }

        if ((loyaltyProgram.getRegularPoints()-5 ) >= 0) {
            loyaltyProgram.setRegularPoints(loyaltyProgram.getRegularPoints()-5);
        } else { loyaltyProgram.setRegularPoints(0.0); }

        loyaltyProgram.setReservationPoints(loyaltyProgram.getReservationPoints()+1);

        this.update(loyaltyProgram);
    }

    LoyaltyProgram update(LoyaltyProgram loyaltyProgram) {
        LoyaltyProgram toUpdate = this.loyaltyRepository.getById(loyaltyProgram.getId());

        toUpdate.setClient(loyaltyProgram.getClient());
        toUpdate.setReservationPoints(loyaltyProgram.getReservationPoints());
        toUpdate.setRegularPoints(loyaltyProgram.getRegularPoints());
        toUpdate.setGoldPoints(loyaltyProgram.getGoldPoints());
        toUpdate.setSilverPoints(loyaltyProgram.getSilverPoints());
        toUpdate.setPenalties(loyaltyProgram.getPenalties());
        toUpdate.setDiscountGold(loyaltyProgram.getDiscountGold());
        toUpdate.setDiscountSilver(loyaltyProgram.getDiscountSilver());
        toUpdate.setDiscountSilver(loyaltyProgram.getDiscountSilver());

        this.loyaltyRepository.save(toUpdate);

        return toUpdate;
    }

}
