package me.kiritoasuna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.kiritoasuna.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
