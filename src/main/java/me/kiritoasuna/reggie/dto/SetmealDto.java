package me.kiritoasuna.reggie.dto;

import me.kiritoasuna.reggie.entity.Setmeal;
import me.kiritoasuna.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
