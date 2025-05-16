package com.example.samuraitravel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.example.samuraitravel.controller.AdminHouseController;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

public class AdminHouseControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private HouseRepository houseRepository;
    
    @Mock
    private HouseService houseService;
    
    @InjectMocks
    private AdminHouseController adminHouseController;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // ビューのレンダリングをスキップする設定
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        
        mockMvc = MockMvcBuilders.standaloneSetup(adminHouseController)
                .setViewResolvers(viewResolver)
                .build();
                
        // モックの設定：ページネーション用のモックデータ
        Page<House> mockPage = new PageImpl<>(new ArrayList<>());
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(mockPage);
        
        // 詳細表示用のモックデータ
        House house = new House();
        house.setId(1);
        house.setName("テスト宿");
        house.setDescription("テスト説明");
        when(houseRepository.findById(anyInt())).thenReturn(Optional.of(house));
        
        // サービスメソッドのモック
        doNothing().when(houseService).create(any(HouseRegisterForm.class));
        doNothing().when(houseService).update(any(HouseEditForm.class));
    }
    
    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testShow() throws Exception {
        mockMvc.perform(get("/admin/houses/1"))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/admin/houses/register"))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testCreate() throws Exception {
        mockMvc.perform(post("/admin/houses/create")
                     .with(csrf())
                     .param("name", "テスト宿")
                     .param("description", "テスト説明")
                     .param("price", "5000")
                     .param("capacity", "2")
                     .param("postalCode", "123-4567")
                     .param("address", "テスト住所")
                     .param("phoneNumber", "012-3456-7890"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testEditForm() throws Exception {
        mockMvc.perform(get("/admin/houses/1/edit"))
               .andExpect(status().isOk());
    }
    
    @Test
    public void testUpdate() throws Exception {
        mockMvc.perform(post("/admin/houses/1/update")
                     .with(csrf())
                     .param("id", "1")
                     .param("name", "更新宿")
                     .param("description", "更新説明")
                     .param("price", "6000")
                     .param("capacity", "3")
                     .param("postalCode", "234-5678")
                     .param("address", "更新住所")
                     .param("phoneNumber", "023-4567-8901"))
                .andExpect(status().is3xxRedirection());
    }
}
