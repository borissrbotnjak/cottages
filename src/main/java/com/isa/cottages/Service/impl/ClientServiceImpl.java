package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Client;
import com.isa.cottages.Repository.ClientRepository;
import com.isa.cottages.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findById(Long id) throws Exception {
        if (this.clientRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(Client service)");
        }
        return this.clientRepository.findById(id).get();
    }

    @Override
    public Client findByEmail(String email) throws Exception {
        Client cl = this.clientRepository.findByEmail(email);
        if (cl == null) {
            throw new Exception("No such email(Client service)");
        }
        return cl;
    }

    @Override
    public Client updateProfile(Client client) throws Exception {
        Client forUpdate = this.findById(client.getId());

        forUpdate.setCity(client.getCity());
        forUpdate.setState(client.getState());
        forUpdate.setResidence(client.getResidence());
        forUpdate.setFirstName(client.getFirstName());
        forUpdate.setLastName(client.getLastName());
        forUpdate.setPhoneNumber(client.getPhoneNumber());

        this.clientRepository.save(forUpdate);
        return forUpdate;
    }
    @Override
    public Client updateBasicInfo(Client client, Client forUpdate) {
        forUpdate.setCity(client.getCity());
        forUpdate.setState(client.getState());
        forUpdate.setResidence(client.getResidence());
        forUpdate.setFirstName(client.getFirstName());
        forUpdate.setLastName(client.getLastName());

        return this.clientRepository.save(forUpdate);
    }

    public Client update(Client client) throws Exception {
        Client forUpdate = this.findById(client.getId());

        forUpdate.setCity(client.getCity());
        forUpdate.setState(client.getState());
        forUpdate.setResidence(client.getResidence());
        forUpdate.setFirstName(client.getFirstName());
        forUpdate.setLastName(client.getLastName());
        forUpdate.setPhoneNumber(client.getPhoneNumber());
        forUpdate.setLoyaltyProgram(client.getLoyaltyProgram());
        forUpdate.setBoatSubscriptions(client.getBoatSubscriptions());
        forUpdate.setCottageSubscriptions(client.getCottageSubscriptions());
        forUpdate.setInstructorSubscriptions(client.getInstructorSubscriptions());
        forUpdate.setReservations(client.getReservations());
        forUpdate.setUserRole(client.getUserRole());
        forUpdate.setPassword(client.getPassword());
        forUpdate.setEmail(client.getEmail());
        forUpdate.setEnabled(client.getEnabled());

        this.clientRepository.save(forUpdate);
        return forUpdate;
    }
}
