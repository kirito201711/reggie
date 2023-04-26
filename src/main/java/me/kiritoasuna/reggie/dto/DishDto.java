package me.kiritoasuna.reggie.dto;


import lombok.Data;
import me.kiritoasuna.reggie.entity.Dish;
import me.kiritoasuna.reggie.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
