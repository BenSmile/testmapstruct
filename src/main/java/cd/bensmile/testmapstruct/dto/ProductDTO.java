package cd.bensmile.testmapstruct.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Integer id;
    private String name;
    private String desc;
    private int quantity;
    private double price;
}
