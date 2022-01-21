package com.isa.cottages.Service.impl;

import com.isa.cottages.Model.Instructor;
import com.isa.cottages.Repository.InstructorRepository;
import com.isa.cottages.Service.InstructorService;
import com.isa.cottages.authFasace.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private AuthenticationFacade facade;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository,AuthenticationFacade facade) {
        this.instructorRepository = instructorRepository;
        this.facade=facade;
    }

    @Override
    public Instructor updateAdventures(Instructor instructor) throws Exception {
        Instructor forUpdate = this.findById(instructor.getId());
        forUpdate.setAdventures(instructor.getAdventures());
        return this.instructorRepository.save(forUpdate);
    }

    @Override
    public Instructor findById(Long id) throws Exception {
        if (this.instructorRepository.findById(id).isEmpty()) {
            throw new Exception("No such value(Instructor service)");
        }
        return this.instructorRepository.findById(id).get();
    }
    @Override
    public Instructor findInstructorByEmail(String email) throws Exception {
        Instructor instructor = this.instructorRepository.findByEmail(email);
        if (instructor == null) {
            throw new Exception("Instructor with this email does not exist");
        }
        return instructor;
    }
    @Override
    public Instructor getInstructorFromPrincipal() throws Exception {
        String principal = this.facade.getPrincipalEmail();

        return findInstructorByEmail(principal);
    }
}
