package resto.service;

import resto.entity.Item;
import resto.exception.ItemNotFoundException;
import resto.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void createItem (Item item){
        itemRepository.save(item);
    }

    public List<Item> getItems(){
        return itemRepository.findAll();
    }

    public Item getItemById(UUID id){
        return itemRepository.findById(id).orElseThrow(()->new ItemNotFoundException(id));
    }

    public void updateItem(Item item){
        itemRepository.save(item);
    }

    public void deleteItem(UUID id){
        itemRepository.deleteById(id);
    }

    public List<Item> findItems(String query){
        return itemRepository.findLike(query);
    }
}
