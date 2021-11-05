
package com.shivam.keygenerationservice.services;

import com.shivam.keygenerationservice.repositories.KeyEntryQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeyEntryServiceImpl implements KeyEntryService{

    @Autowired
    private KeyEntryQueryRepository repository;

    @Override
    public String getKey() {
        return repository.getKey();
    }

}
