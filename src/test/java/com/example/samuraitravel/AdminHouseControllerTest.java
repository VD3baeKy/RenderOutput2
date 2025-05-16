package com.example.samuraitravel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.samuraitravel.controller.AdminHouseController;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

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
    public void testIndex() {
        // モックデータの準備
        List<House> houses = new ArrayList<>();
        House house = new House();
        house.setId(1);
        house.setName("テスト宿");
        houses.add(house);
        Page<House> housePage = new PageImpl<>(houses);
        
        // モックの振る舞いを設定
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(housePage);
        
        // コントローラメソッドを呼び出し
        String viewName = adminHouseController.index(model);
        
        // 検証
        assertEquals("admin/houses/index", viewName);
        verify(model).addAttribute(eq("housePage"), any(Page.class));
    }
    
    @Test
    public void testShow() {
        // モックデータの準備
        House house = new House();
        house.setId(1);
        house.setName("テスト宿");
        
        // モックの振る舞いを設定
        when(houseRepository.findById(1)).thenReturn(Optional.of(house));
        
        // コントローラメソッドを呼び出し
        String viewName = adminHouseController.show(1, model);
        
        // 検証
        assertEquals("admin/houses/show", viewName);
        verify(model).addAttribute("house", house);
    }
    
    @Test
    public void testCreateForm() {
        // コントローラメソッドを呼び出し
        String viewName = adminHouseController.createForm(model);
        
        // 検証
        assertEquals("admin/houses/register", viewName); // パスが "register" であることに注意
        verify(model).addAttribute(eq("houseRegisterForm"), any(HouseRegisterForm.class));
    }
    
    @Test
    public void testCreateWithValidInput() {
        // テスト用フォームオブジェクトを作成
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName("テスト宿");
        
        // バリデーションエラーなし
        when(bindingResult.hasErrors()).thenReturn(false);
        
        // コントローラメソッドを呼び出し
        String viewName = adminHouseController.create(form, bindingResult);
        
        // 検証
        assertEquals("redirect:/admin/houses", viewName);
        verify(houseService).create(form);
    }
    
    @Test
    public void testCreateWithInvalidInput() {
        // テスト用フォームオブジェクトを作成
        HouseRegisterForm form = new HouseRegisterForm();
        form.setName(""); // 不正な入力
        
        // バリデーションエラーあり
        when(bindingResult.hasErrors()).thenReturn(true);
        
        // コントローラメソッドを呼び出し
        String viewName = adminHouseController.create(form, bindingResult);
        
        // 検証
        assertEquals("admin/houses/register", viewName); // エラー時は登録フォームに戻る
        verify(houseService, never()).create(any()); // createメソッドは呼ばれないことを確認
    }
}
