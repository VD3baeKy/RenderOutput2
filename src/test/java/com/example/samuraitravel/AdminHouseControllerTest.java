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

import com.example.samuraitravel.controller.AdminHouseController;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        Pageable pageable = PageRequest.of(0, 10);
        String keyword = null;

        List<House> houses = new ArrayList<>();
        Page<House> housePage = new PageImpl<>(houses);
        when(houseRepository.findAll(any(Pageable.class))).thenReturn(housePage);

        String view = adminHouseController.index(model, pageable, keyword);

        assertEquals("admin/houses/index", view);
        verify(model).addAttribute(eq("housePage"), any(Page.class));
    }

    @Test
    public void testCreate() {
        HouseRegisterForm form = new HouseRegisterForm();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        String view = adminHouseController.create(form, bindingResult, redirectAttributes);

        assertEquals("redirect:/admin/houses", view);
        verify(houseService).create(form);
    }
}
