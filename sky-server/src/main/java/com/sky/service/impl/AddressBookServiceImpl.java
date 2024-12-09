package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 添加地址
     * @param addressBook
     */
    @Override
    public void addAdderssBook(AddressBook addressBook) {

        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.addAddressBook(addressBook);
    }

    /**
     * 查询用户所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> getAddressBook() {
        Long userId = BaseContext.getCurrentId();
        AddressBook addressBook=AddressBook.builder()
                .userId(userId)
                .build();
        return addressBookMapper.getAddressBook(addressBook);
    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public AddressBook getDefaultAddressBook() {
        AddressBook addressBook=AddressBook.builder()
                .isDefault(1)
                .userId(BaseContext.getCurrentId())
                .build();
        return addressBookMapper.getDefaultAddressBook(addressBook);
    }

    /**
     * 修改地址信息
     * @param addressBook
     */
    @Override
    public void updateAddressBook(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        addressBookMapper.update(addressBook);

    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook getAddressBookById(Long id) {
        AddressBook addressBook=AddressBook.builder()
                .id(id)
                .build();
        return addressBookMapper.getAddressBookById(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     */
    @Override
    public void setDefaultAddressBook(AddressBook addressBook) {
        AddressBook address=AddressBook.builder()
                .isDefault(1)
                .userId(BaseContext.getCurrentId())
                .build();

        AddressBook defaultAddressBook = addressBookMapper.getDefaultAddressBook(address);
        defaultAddressBook.setIsDefault(0);
        addressBookMapper.update(defaultAddressBook);

        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);

    }

    /**
     * 根据id删除地址信息
     * @param id
     */
    @Override
    public void delete(Long id) {
        addressBookMapper.delete(id);
    }
}
