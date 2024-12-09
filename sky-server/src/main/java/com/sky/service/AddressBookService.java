package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 添加地址
     * @param addressBook
     */
    void addAdderssBook(AddressBook addressBook);

    /**
     * 查询用户地址信息
     * @return
     */
    List<AddressBook> getAddressBook();

    /**
     * 查询默认地址
     * @return
     */
    AddressBook getDefaultAddressBook();

    /**
     * 修改地址信息
     * @param addressBook
     */
    void updateAddressBook(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook getAddressBookById(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     */
    void setDefaultAddressBook(AddressBook addressBook);

    /**
     * 根据id删除地址信息
     * @param id
     */
    void delete(Long id);
}
