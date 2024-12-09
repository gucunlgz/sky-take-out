package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 添加地址
     * @param addressBook
     */
    void addAddressBook(AddressBook addressBook);

    /**
     * 查询用户所有地址信息
     * @param addressBook
     * @return
     */
    List<AddressBook> getAddressBook(AddressBook addressBook);

    /**
     * 查询默认地址
     * @param addressBook
     * @return
     */
    AddressBook getDefaultAddressBook(AddressBook addressBook);

    /**
     * 修改地址信息
     * @param addressBook
     */
    void update(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param addressBook
     * @return
     */
    AddressBook getAddressBookById(AddressBook addressBook);

    /**
     * 根据id删除地址信息
     */
    void delete(Long id);
}
