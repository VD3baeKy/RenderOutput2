package com.example.samuraitravel.controller;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Owner;
import io.qameta.allure.Description;
import io.qameta.allure.DisplayName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Epic("管理画面")
@Feature("民宿管理")
@Owner("TestUser")
public class AdminHouseControllerTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private HouseService houseService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AdminHouseController adminHouseController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("キーワードあり検索")
    @Story("一覧取得")
    public void testIndex_withKeyword() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = "民宿";
        List<House> houses = new ArrayList<>();
        Page<House> housePage = new PageImpl<>(houses);
        when(houseRepository.findByNameLike(anyString(), any(Pageable.class))).thenReturn(housePage);

        String view = adminHouseController.index(model, pageable, keyword);

        assertEquals("admin/houses/index", view);
        verify(houseRepository).findByNameLike("%民宿%", pageable);
        verify(model).addAttribute(eq("housePage"), eq(housePage));
        verify(model).addAttribute(eq("keyword"), eq(keyword));
    }

    @Test
    @DisplayName("キーワードなし検索")
    @Story("一覧取得")
    public void testIndex_withoutKeyword() {
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = null;
        List<House> houses = new ArrayList<>();
        Page<House> housePage = new PageImpl<>(houses);
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(housePage);

        String view = adminHouseController.index(model, pageable, keyword);

        assertEquals("admin/houses/index", view);
        verify(houseRepository).findAll(pageable);
        verify(model).addAttribute(eq("housePage"), eq(housePage));
        verify(model).addAttribute(eq("keyword"), eq(keyword));
    }

    @Test
    @DisplayName("詳細表示")
    @Story("詳細表示")
    public void testShow() {
        Integer id = 123;
        House house = new House();
        house.setId(id);
        when(houseRepository.getReferenceById(id)).thenReturn(house);

        String view = adminHouseController.show(id, model);

        assertEquals("admin/houses/show", view);
        verify(model).addAttribute("house", house);
    }

    @Test
    @DisplayName("登録画面表示")
    @Story("登録")
    public void testRegister() {
        String view = adminHouseController.register(model);

        assertEquals("admin/houses/register", view);
        verify(model).addAttribute(eq("houseRegisterForm"), any(HouseRegisterForm.class));
    }

    @Test
    @DisplayName("登録成功")
    @Story("登録")
    public void testCreate_valid() {
        HouseRegisterForm form = new HouseRegisterForm();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        String view = adminHouseController.create(form, bindingResult, redirectAttributes);

        assertEquals("redirect:/admin/houses", view);
        verify(houseService).create(form);
        verify(redirectAttributes).addFlashAttribute("successMessage", "民宿を登録しました。");
    }

    @Test
    @DisplayName("登録時バリデーションエラー")
    @Story("登録")
    public void testCreate_validationError() {
        HouseRegisterForm form = new HouseRegisterForm();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        String view = adminHouseController.create(form, bindingResult, redirectAttributes);

        assertEquals("admin/houses/register", view);
        verify(houseService, never()).create(any());
    }

    @Test
    @DisplayName("編集画面表示")
    @Story("編集")
    public void testEdit() {
        Integer id = 789;
        House house = new House();
        house.setId(id);
        house.setName("さくら荘");
        house.setDescription("説明");
        house.setPrice(12345);
        house.setCapacity(4);
        house.setPostalCode("123-4567");
        house.setAddress("大阪");
        house.setPhoneNumber("090-1234-5678");
        house.setImageName("house-1.jpg");
        when(houseRepository.getReferenceById(id)).thenReturn(house);

        String view = adminHouseController.edit(id, model);

        assertEquals("admin/houses/edit", view);
        verify(model).addAttribute(eq("imageName"), eq("house-1.jpg"));
        verify(model).addAttribute(eq("houseEditForm"), any(HouseEditForm.class));
    }

    @Test
    @DisplayName("編集成功")
    @Story("編集")
    public void testUpdate_valid() {
        HouseEditForm form = new HouseEditForm(
            1, "テスト宿", null, "新しい説明", 6000, 5, "111-2222", "東京都港区2-2-2", "080-3333-4444"
        );
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = adminHouseController.update(form, bindingResult, redirectAttributes);

        assertEquals("redirect:/admin/houses", view);
        verify(houseService).update(form);
        verify(redirectAttributes).addFlashAttribute("successMessage", "民宿情報を編集しました。");
    }

    @Test
    @DisplayName("編集バリデーションエラー")
    @Story("編集")
    public void testUpdate_validationError() {
        HouseEditForm form = new HouseEditForm(
            2, "エラー宿", null, "エラー説明", 2000, 1, "555-6666", "大阪市西区3-3-3", "070-7777-8888"
        );
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = adminHouseController.update(form, bindingResult, redirectAttributes);

        assertEquals("admin/houses/edit", view);
        verify(houseService, never()).update(any());
    }

    @Test
    @DisplayName("削除")
    @Story("削除")
    public void testDelete() {
        Integer id = 99;
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = adminHouseController.delete(id, redirectAttributes);

        assertEquals("redirect:/admin/houses", view);
        verify(houseRepository).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "民宿を削除しました。");
    }
}
