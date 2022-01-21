package com.isa.cottages.Service;

import com.isa.cottages.Model.Instructor;


public interface InstructorService {

    Instructor updateAdventures(Instructor instructor) throws Exception;

    Instructor findById(Long id) throws Exception;

    Instructor findInstructorByEmail(String email) throws Exception;

    Instructor getInstructorFromPrincipal() throws Exception;
}
