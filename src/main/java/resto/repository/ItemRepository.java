package resto.repository;


import resto.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository <Item, UUID> {
    @Query("select i from Item i where i.title like %:text% or i.category like %:text%")
    List<Item> findLike(@Param("text")String text);
}
