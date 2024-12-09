package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {


    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result addAddressBook(@RequestBody AddressBook addressBook) {
        log.info("添加地址{}", addressBook);
        addressBookService.addAdderssBook(addressBook);
        return Result.success();
    }


    /**
     * 查询所有地址信息
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> getAddressBook() {
        log.info("查询用户{}所有地址信息", BaseContext.getCurrentId());
        List<AddressBook> addressBooks=addressBookService.getAddressBook();
        return Result.success(addressBooks);
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook() {
        log.info("查询用户{}的默认地址", BaseContext.getCurrentId());
        AddressBook addressBook= addressBookService.getDefaultAddressBook();
        return Result.success(addressBook);
    }

    /**
     * 修改地址信息
     * @param addressBook
     * @return
     */
    @PutMapping()
    public Result updateAddressBook(@RequestBody AddressBook addressBook) {
        log.info("修改用户{}的地址信息", BaseContext.getCurrentId());
        addressBookService.updateAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable Long id) {
        log.info("查询id为{}的地址",id);
        AddressBook addressBook=addressBookService.getAddressBookById(id);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result setDefaultAddressBook(@RequestBody AddressBook addressBook) {
        log.info("设置id为{}号地址为默认地址",addressBook.getId());
        addressBookService.setDefaultAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 根据id删除地址信息
     * @param id
     * @return
     */
    @DeleteMapping("/")
    public Result deleteAddressBookById(@RequestParam Long id) {
        log.info("删除id为{}的地址",id);
        addressBookService.delete(id);
        return Result.success();
    }
}
