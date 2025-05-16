package com.example.samuraitravel;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // フィルタを無効化
@ActiveProfiles("test")
public class AdminHouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // テストメソッドの前に設定を追加
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminHouseController(houseRepository, houseService))
                .setViewResolvers((viewName, locale) -> new AbstractUrlBasedView() {
                    @Override
                    protected boolean isUrlRequired() {
                        return false;
                    }

                    @Override
                    public boolean checkResource(Locale locale) {
                        return true;
                    }
                })
                .build();
    }

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private HouseRepository houseRepository;
    
    @MockBean
    private HouseService houseService;
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testIndex() throws Exception {
        List<House> houses = new ArrayList<>();
        House house1 = new House();
        house1.setId(1);
        house1.setName("テスト宿1");
        House house2 = new House();
        house2.setId(2);
        house2.setName("テスト宿2");
        houses.add(house1);
        houses.add(house2);
        
        when(houseRepository.findAll()).thenReturn(houses);
        
        mockMvc.perform(get("/admin/houses"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/index"))
               .andExpect(model().attributeExists("houses"))
               .andExpect(content().string(containsString("テスト宿1")))
               .andExpect(content().string(containsString("テスト宿2")));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShow() throws Exception {
        House house = new House();
        house.setId(1);
        house.setName("テスト宿");
        house.setDescription("テスト説明");
        
        when(houseRepository.findById(1)).thenReturn(Optional.of(house));
        
        mockMvc.perform(get("/admin/houses/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/show"))
               .andExpect(model().attributeExists("house"))
               .andExpect(content().string(containsString("テスト宿")))
               .andExpect(content().string(containsString("テスト説明")));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/admin/houses/create"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/create"))
               .andExpect(model().attributeExists("houseRegisterForm"));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreate() throws Exception {
        // HouseRegisterForm クラスの引数1つを受け取るメソッド
        doNothing().when(houseService).create(any(HouseRegisterForm.class));
        
        mockMvc.perform(post("/admin/houses/create")
                      .with(csrf())
                      .param("name", "テスト宿")
                      .param("description", "テスト説明")
                      .param("price", "5000")
                      .param("capacity", "2")
                      .param("postalCode", "123-4567")
                      .param("address", "テスト住所")
                      .param("phoneNumber", "012-3456-7890"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"));
        
        verify(houseService, times(1)).create(any(HouseRegisterForm.class));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testEditForm() throws Exception {
        House house = new House();
        house.setId(1);
        house.setName("テスト宿");
        house.setDescription("テスト説明");
        house.setPrice(5000);
        house.setCapacity(2);
        house.setPostalCode("123-4567");
        house.setAddress("テスト住所");
        house.setPhoneNumber("012-3456-7890");
        
        when(houseRepository.findById(1)).thenReturn(Optional.of(house));
        
        mockMvc.perform(get("/admin/houses/1/edit"))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/houses/edit"))
               .andExpect(model().attributeExists("houseEditForm"))
               .andExpect(content().string(containsString("テスト宿")))
               .andExpect(content().string(containsString("テスト説明")));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        // HouseEditForm クラスの引数1つを受け取るメソッド
        doNothing().when(houseService).update(any(HouseEditForm.class));
        
        mockMvc.perform(post("/admin/houses/1/update")
                      .with(csrf())
                      .param("name", "更新宿")
                      .param("description", "更新説明")
                      .param("price", "6000")
                      .param("capacity", "3")
                      .param("postalCode", "234-5678")
                      .param("address", "更新住所")
                      .param("phoneNumber", "023-4567-8901"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"));
        
        verify(houseService, times(1)).update(any(HouseEditForm.class));
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDelete() throws Exception {
        doNothing().when(houseRepository).deleteById(1);
        
        mockMvc.perform(post("/admin/houses/1/delete")
                      .with(csrf()))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/houses"));
        
        verify(houseRepository, times(1)).deleteById(1);
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateWithValidationErrors() throws Exception {
        mockMvc.perform(post("/admin/houses/create")
                      .with(csrf())
                      .param("name", "")  // 名前を空欄にして検証エラーを発生させる
                      .param("description", "テスト説明")
                      .param("price", "5000")
                      .param("capacity", "2")
                      .param("postalCode", "123-4567")
                      .param("address", "テスト住所")
                      .param("phoneNumber", "012-3456-7890"))
               .andExpect(status().isOk())  // エラー時は200 OKでフォームを再表示
               .andExpect(view().name("admin/houses/create"))
               .andExpect(model().hasErrors());
    }
    
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateWithValidationErrors() throws Exception {
        mockMvc.perform(post("/admin/houses/1/update")
                      .with(csrf())
                      .param("name", "")  // 名前を空欄にして検証エラーを発生させる
                      .param("description", "テスト説明")
                      .param("price", "5000")
                      .param("capacity", "2")
                      .param("postalCode", "123-4567")
                      .param("address", "テスト住所")
                      .param("phoneNumber", "012-3456-7890"))
               .andExpect(status().isOk())  // エラー時は200 OKでフォームを再表示
               .andExpect(view().name("admin/houses/edit"))
               .andExpect(model().hasErrors());
    }
}
