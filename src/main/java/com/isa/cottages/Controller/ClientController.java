package com.isa.cottages.Controller;

import com.isa.cottages.Service.impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/client")
public class ClientController {

    private ClientServiceImpl clientService;

    @Autowired
    public ClientController(ClientServiceImpl clientService){
        this.clientService = clientService;
    }
}
