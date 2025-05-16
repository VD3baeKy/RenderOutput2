package com.example.samuraitravel.controller;

// Allure 依存関係をすべて削除
// import io.qameta.allure.Epic;
// import io.qameta.allure.Feature;
// import io.qameta.allure.Story;
// import io.qameta.allure.Owner;
// import io.qameta.allure.Description;

// JUnit Jupiter のアノテーションを使用 (すでにインポート済み)
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;    // 追加：タグ付けのため
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;  // 追加：MockitoExtension用
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;  // 追加：MockitoExtension用
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

// Allure アノテーションを JUnit アノテーションに置き換え
@DisplayName("管理画面 - 民宿管理")
@ExtendWith(MockitoExtension.class)  // MockitoAnnotations.openMocks(this) の代わりに使用
@Tag("AdminTests")                 // @Epic("管理画面") の代わり
@Tag("HouseManagement")            // @Feature("民宿管理") の代わり
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

    // MockitoExtension を使用するので不要
    // @BeforeEach
    // public void setup() {
    //     MockitoAnnotations.openMocks(this);
    // }

    @Test
    @DisplayName("キーワードあり検索")
    @Tag("List")       // @Story("一覧取得") の代わり
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
    @Tag("List")       // @Story("一覧取得") の代わり
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
    @Tag("Detail")     // @Story("詳細表示") の代わり
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
    @Tag("Register")   // @Story("登録") の代わり
    public void testRegister() {
        String view = adminHouseController.register(model);

        assertEquals("admin/houses/register", view);
        verify(model).addAttribute(eq("houseRegisterForm"), any(HouseRegisterForm.class));
    }

    @Test
    @DisplayName("登録成功")
    @Tag("Register")   // @Story("登録") の代わり
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
    @Tag("Register")   // @Story("登録") の代わり
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
    @Tag("Edit")       // @Story("編集") の代わり
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
    @Tag("Edit")       // @Story("編集") の代わり
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
    @Tag("Edit")       // @Story("編集") の代わり
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
    @Tag("Delete")     // @Story("削除") の代わり
    public void testDelete() {
        Integer id = 99;
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String view = adminHouseController.delete(id, redirectAttributes);

        assertEquals("redirect:/admin/houses", view);
        verify(houseRepository).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "民宿を削除しました。");
    }
}
