package resto.controller;

import static resto.ApiPath.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import resto.entity.Item;
import resto.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ROOT + ITEMS)
@Api(tags = "Eshop Product controller")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiOperation(value = "get all items", tags = "getItems", httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "all good"),
            @ApiResponse(code = 403, message = "user doesn't have permission"),
            @ApiResponse(code = 404, message = "not found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Item> getItems(){
        return itemService.getItems();
    }

    @GetMapping(value = ITEM, produces = MediaType.APPLICATION_JSON_VALUE)
    public Item getItem(@PathVariable(ID_VARIABLE) UUID id){
        return itemService.getItemById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createItem(@RequestBody Item item){
        itemService.createItem(item);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateItem(@RequestBody Item item){
        itemService.updateItem(item);
    }

    @DeleteMapping(ITEM)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable(ID_VARIABLE) UUID id){
        itemService.deleteItem(id);
    }

    @GetMapping(SEARCH)
    public List<Item> searchProducts(@RequestParam String query){
        return itemService.findItems(query);
    }
}
