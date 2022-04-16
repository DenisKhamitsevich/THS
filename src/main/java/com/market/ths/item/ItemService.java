package com.market.ths.item;

import com.market.ths.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void SaveItem(MultipartFile file, Item item){
        byte [] arr = new byte[0];
        try {
            arr = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        item.setImage(arr);
        itemRepository.save(item);
    }
    public boolean DeleteItem(Long id)
    {
        if(!itemRepository.existsById(id))
            return false;
        itemRepository.deleteById(id);
        return true;
    }
}
