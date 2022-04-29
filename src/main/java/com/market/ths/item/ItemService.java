package com.market.ths.item;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void saveItem(MultipartFile file, Item item){
        String fileName = itemRepository.count()+"_"+StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get("uploads/");
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            System.out.println("File exception!");

        }
        item.setImage("/uploads/"+fileName);
        itemRepository.save(item);
    }

    public void updateItem(MultipartFile file, Item item){
        Item prev=itemRepository.getById(item.getId());
        if(!file.getOriginalFilename().equals(""))
        {
            String fileName = itemRepository.count()+"_"+StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get("uploads/");
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {
                System.out.println("File exception!");
            }
            prev.setImage("/uploads/"+fileName);
        }
        prev.setDescription(item.getDescription());
        prev.setPrice(item.getPrice());
        prev.setTitle(item.getTitle());
        itemRepository.save(prev);
    }



    public boolean deleteItem(Long id)
    {
        if(!itemRepository.existsById(id))
            return false;
        itemRepository.deleteById(id);
        return true;
    }

    public List<Item> getItemsById(Set<Long> ids)
    {
        return itemRepository.findAllById(ids);
    }

    public List<Item> getPageOfItems(int from, int amount, HashMap<String,String> filters)
    {
        List<Item> values=itemRepository.findByPriceBetweenAndTitleContainingIgnoreCaseOrderByIdDesc(Double.valueOf(filters.get("minPrice")),
                Double.valueOf(filters.get("maxPrice")),filters.get("search"));
        List<Item> result=values.subList(from,Math.min(values.size(),from+amount));
        filters.put("size", String.valueOf(values.size()));
        return result;
    }

    public Item getItemById(Long id)
    {
        return itemRepository.getById(id);
    }

    public Boolean itemExists(Long id)
    {
        return itemRepository.existsById(id);
    }
}
