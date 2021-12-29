package resto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@Getter
@AllArgsConstructor
public class ItemNotFoundException extends RuntimeException{
    private UUID id;
}
