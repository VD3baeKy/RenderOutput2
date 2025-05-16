package com.example.samuraitravel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.samuraitravel.controller.AdminHouseController;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

@WebMvcTest(AdminHouseController.class)
public class AdminHouseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private HouseRepository houseRepository;
    
    @MockBean
    private HouseService houseService;
    
    // 基本テスト - 管理者がリストページにアクセスできる
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanAccessHouseList() throws Exception {
        when(houseRepository.findAll()).thenReturn(new ArrayList<>());
        
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().isOk());
    }
    
    // 基本テスト - 管理者が詳細ページにアクセスできる
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanAccessHouseDetail() throws Exception {
        House house = new House();
        house.setId(1);
        when(houseRepository.findById(anyInt())).thenReturn(Optional.of(house));
        
        mockMvc.perform(get("/admin/houses/1"))
               .andExpect(status().isOk());
    }
    
    // 基本テスト - 管理者が登録フォームにアクセスできる
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanAccessRegisterForm() throws Exception {
        mockMvc.perform(get("/admin/houses/register"))  // URLを実際のパスに修正
               .andExpect(status().isOk());
    }
    
    // 基本テスト - 認証なしではアクセスできない
    @Test
    public void testUnauthenticatedUserCannotAccess() throws Exception {
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().is3xxRedirection());  // ログインページにリダイレクト
    }
    
    // 基本テスト - CSRFトークンありで投稿可能
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminCanPostWithCsrf() throws Exception {
        mockMvc.perform(post("/admin/houses/create")
                .with(csrf())
                .param("name", "テスト宿")
                .param("price", "5000")
                .param("capacity", "2")
                .param("address", "テスト住所"))
               .andExpect(status().is3xxRedirection());  // 成功時はリダイレクト
    }
}
