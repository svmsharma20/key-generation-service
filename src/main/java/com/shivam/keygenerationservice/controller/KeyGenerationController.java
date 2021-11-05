
package com.shivam.keygenerationservice.controller;

import com.shivam.keygenerationservice.services.KeyEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KeyGenerationController {

    @Autowired
    private KeyEntryService service;

    @GetMapping("/kgs/key")
    public ResponseEntity<String> getKeys(){
        return ResponseEntity.ok(service.getKey());
    }
}
