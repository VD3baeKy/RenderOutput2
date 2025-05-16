package com.example.samuraitravel.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminHouseControllerTest2 {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private HouseRepository houseRepository;
    
    @MockBean
    private HouseService houseService;
    
    private List<House> houses;
    private House testHouse;
    
    @BeforeEach
    public void setUp() {
        // テスト用のデータを準備
        houses = new ArrayList<>();
        testHouse = new House();
        testHouse.setId(1);
        testHouse.setName("テスト民宿");
        testHouse.setDescription("テスト用の説明文です");
        testHouse.setPrice(10000);
        testHouse.setCapacity(4);
        testHouse.setPostalCode("123-4567");
        testHouse.setAddress("東京都テスト区1-2-3");
        testHouse.setPhoneNumber("03-1234-5678");
        testHouse.setImageName("test.jpg");
        houses.add(testHouse);
    }
    
    @Test
    @DisplayName("管理者ページにアクセスすると認証ページにリダイレクトされること")
    public void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("http://localhost/login"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("民宿一覧ページが正しく表示されること")
    public void testIndex() throws Exception {
        // モックの設定
        Page<House> housePage = new PageImpl<>(houses, PageRequest.of(0, 10), 1);
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(housePage);
        
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/index"))
               .andExpect(model().attributeExists("housePage"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("キーワード検索が正しく動作すること")
    public void testIndexWithKeyword() throws Exception {
        // モックの設定
        Page<House> housePage = new PageImpl<>(houses, PageRequest.of(0, 10), 1);
        when(houseRepository.findByNameLike(anyString(), any(Pageable.class))).thenReturn(housePage);
        
        mockMvc.perform(get("/admin/houses").param("keyword", "テスト"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/index"))
               .andExpect(model().attributeExists("housePage"))
               .andExpect(model().attribute("keyword", "テスト"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("民宿詳細ページが正しく表示されること")
    public void testShow() throws Exception {
        when(houseRepository.getReferenceById(1)).thenReturn(testHouse);
        
        mockMvc.perform(get("/admin/houses/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/show"))
               .andExpect(model().attribute("house", testHouse));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("登録フォームが正しく表示されること")
    public void testRegister() throws Exception {
        mockMvc.perform(get("/admin/houses/register"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/register"))
               .andExpect(model().attributeExists("houseRegisterForm"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("民宿登録が正しく処理されること")
    public void testCreate() throws Exception {
        // FIXME: 必要に応じてHouseRegisterFormの初期化方法を修正
        doNothing().when(houseService).create(any(HouseRegisterForm.class));
        
        mockMvc.perform(post("/admin/houses/create")
                .param("name", "新規民宿")
                .param("description", "新規の説明文です")
                .param("price", "15000")
                .param("capacity", "6")
                .param("postalCode", "987-6543")
                .param("address", "大阪府テスト市4-5-6")
                .param("phoneNumber", "06-9876-5432"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"))
               .andExpect(flash().attributeExists("successMessage"));
        
        verify(houseService, times(1)).create(any(HouseRegisterForm.class));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("編集フォームが正しく表示されること")
    public void testEdit() throws Exception {
        when(houseRepository.getReferenceById(1)).thenReturn(testHouse);
        
        mockMvc.perform(get("/admin/houses/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/edit"))
               .andExpect(model().attributeExists("houseEditForm"))
               .andExpect(model().attribute("imageName", testHouse.getImageName()));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("民宿情報の更新が正しく処理されること")
    public void testUpdate() throws Exception {
        // 引数なしのコンストラクタは存在しないため、完全なコンストラクタを使用
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test image content".getBytes());
        
        // サービスのモック設定
        doNothing().when(houseService).update(any(HouseEditForm.class));
        
        mockMvc.perform(post("/admin/houses/1/update")
                .param("id", "1")
                .param("name", "更新後の民宿")
                .param("description", "更新後の説明文です")
                .param("price", "12000")
                .param("capacity", "5")
                .param("postalCode", "567-8901")
                .param("address", "京都府テスト町7-8-9")
                .param("phoneNumber", "075-1234-5678"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"))
               .andExpect(flash().attributeExists("successMessage"));
        
        verify(houseService, times(1)).update(any(HouseEditForm.class));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("民宿の削除が正しく処理されること")
    public void testDelete() throws Exception {
        doNothing().when(houseRepository).deleteById(1);
        
        mockMvc.perform(post("/admin/houses/1/delete"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"))
               .andExpect(flash().attributeExists("successMessage"));
        
        verify(houseRepository, times(1)).deleteById(1);
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("バリデーションエラーがある場合、登録フォームが再表示されること")
    public void testCreateWithValidationErrors() throws Exception {
        mockMvc.perform(post("/admin/houses/create")
                .param("name", "") // 名前が空でバリデーションエラー
                .param("price", "invalid")) // 無効な価格
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/register"))
               .andExpect(model().attributeHasFieldErrors("houseRegisterForm", "name", "price"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("バリデーションエラーがある場合、編集フォームが再表示されること")
    public void testUpdateWithValidationErrors() throws Exception {
        mockMvc.perform(post("/admin/houses/1/update")
                .param("id", "1")
                .param("name", "") // 名前が空でバリデーションエラー
                .param("price", "invalid")) // 無効な価格
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/edit"))
               .andExpect(model().attributeHasFieldErrors("houseEditForm", "name", "price"));
    }
}
