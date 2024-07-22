package com.juyeon.springdatajpa.menu.controller;

import com.juyeon.springdatajpa.common.Pagenation;
import com.juyeon.springdatajpa.common.PagingButtonInfo;
import com.juyeon.springdatajpa.menu.model.dto.CategoryDTO;
import com.juyeon.springdatajpa.menu.model.dto.MenuDTO;
import com.juyeon.springdatajpa.menu.model.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }

    // 메뉴 단일 조회 기능
    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable("menuCode") int menuCode,
                                 Model model) {
        log.info("menuCode = {}", menuCode);

        MenuDTO menu = menuService.findMenuByCode(menuCode);

        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    // 페이징 처리 전
//    @GetMapping("/list")
//    public String findMenuList(Model model){
//        List<MenuDTO> menuList = menuService.findMenuList();
//        model.addAttribute("menuList", menuList);
//        return "menu/list";
//    }

    // 페이징 처리 후
    @GetMapping("/list")
    public String findAllMenus(@PageableDefault Pageable pageable, Model model) {
        log.info("pageable = {}", pageable);    // pageable = Page request [number: 0, size 10, sort: UNSORTED]

        Page<MenuDTO> menuList = menuService.findAllMenus(pageable);

        log.info("조회한 내용 목록 : {}", menuList.getContent());  // 조회한 내용 목록 : [MenuDTO(menuCode=21, menuName=돌미나리백설기, menuPrice=5000, categoryCode=11, orderableStatus=Y), MenuDTO(menuCode=20, menuName=마라깐쇼한라봉, menuPrice=22000, categoryCode=5, orderableStatus=N), MenuDTO(menuCode=19, menuName=까나리코코넛쥬스, menuPrice=9000, categoryCode=9, orderableStatus=Y), MenuDTO(menuCode=18, menuName=붕어빵초밥, menuPrice=35000, categoryCode=6, orderableStatus=Y), MenuDTO(menuCode=17, menuName=아이스가리비관자육수, menuPrice=6000, categoryCode=10, orderableStatus=Y), MenuDTO(menuCode=16, menuName=신메뉴, menuPrice=9000, categoryCode=8, orderableStatus=Y), MenuDTO(menuCode=15, menuName=멸치튀김우동, menuPrice=11000, categoryCode=6, orderableStatus=N), MenuDTO(menuCode=14, menuName=과메기커틀릿, menuPrice=13000, categoryCode=6, orderableStatus=Y), MenuDTO(menuCode=13, menuName=직화구이젤라또, menuPrice=8000, categoryCode=12, orderableStatus=Y), MenuDTO(menuCode=12, menuName=날치알스크류바, menuPrice=2000, categoryCode=10, orderableStatus=Y)]
        log.info("총 페이지 수 : {}", menuList.getTotalPages());  // 3
        log.info("총 메뉴 수 : {}", menuList.getTotalElements()); // 21
        log.info("해당 페이지에 표시 될 요소 수 : {}", menuList.getSize()); // 10
        log.info("해당 페이지에 실제 요소 수 : {}", menuList.getNumberOfElements());   // 10
        log.info("첫 페이지 여부 : {}", menuList.isFirst());   // true
        log.info("마지막 페이지 여부 : {}", menuList.isLast()); // false
        log.info("정렬 방식 : {}", menuList.getSort());       // menuCode: DESC
        log.info("여러 페이지 중 현재 인덱스 : {}", menuList.getNumber()); // 0

        PagingButtonInfo paging = Pagenation.getPagingButtonInfo(menuList);
        model.addAttribute("paging", paging);
        model.addAttribute("menuList", menuList);

        return "menu/list";
    }

    @GetMapping("/querymethod")
    public void queryMethodPage(){}

    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model){
        log.info("menuPrice ========= {}",menuPrice);

        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);

        return "menu/searchResult";
    }

    @GetMapping("/register")
    public void registerPage() {}

    // @GetMapping + @ResponseBody = RestController
    @GetMapping(value = "category", produces = "application/json; charset=utf-8")
    @ResponseBody   // 리턴한 값이 json형식으로 response body에 담김
    public List<CategoryDTO> categoryPage(){
        List<CategoryDTO> categoryList = menuService.findAllCategory();
        log.info("categoryList ============ {}", categoryList);

        return categoryList;
    }

    @PostMapping("/register")
    public String registerNewMenu(@ModelAttribute MenuDTO newMenu) {
        log.info("newMenu =============== > {}", newMenu);
        menuService.registerNewMenu(newMenu);

        return "redirect:/menu/list";
    }


    @GetMapping("/modify/{menuCode}")
    public String modifyPage(@PathVariable int menuCode, Model model) {
        log.info("menuCode = {}", menuCode);

        // 메뉴코드로 메뉴 조회해오는 기능
        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/modify";
    }


    @PostMapping("/modify")
    // ModelAttribute 생략가능
    public String modifyMenu(MenuDTO modifyMenu) {
        log.info("modifyMenu =========== {}", modifyMenu);
        menuService.modifyMenu(modifyMenu);

        return "redirect:/menu/modify/" + modifyMenu.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage(){}

    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Integer menuCode) {
        menuService.deleteMenu(menuCode);
        return "redirect:/menu/list";
    }
}
