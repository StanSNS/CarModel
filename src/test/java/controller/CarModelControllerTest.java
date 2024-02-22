package controller;

import com.example.carmodels.Controler.CarModelController;
import com.example.carmodels.Models.Entity.CarModels;
import com.example.carmodels.Security.AuthUser;
import com.example.carmodels.Service.CarModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarModelControllerTest {

    @Mock
    CarModelService service;

    @Mock
    AuthUser authUser;

    @Mock
    Model model;

    @InjectMocks
    CarModelController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll_WhenUserIsLogged() {
        Model model = mock(Model.class);
        List<CarModels> carModelsList = new ArrayList<>();
        when(service.findallCarModels()).thenReturn(carModelsList);

        when(authUser.isUserLogged()).thenReturn(true);
        String viewName = controller.findall(model);

        assertEquals("all", viewName);
        verify(model).addAttribute("carmodels", carModelsList);
    }

    @Test
    public void testFindAll_WhenUserNotLogged() {
        Model model = mock(Model.class);

        when(authUser.isUserLogged()).thenReturn(false);
        String viewName = controller.findall(model);

        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    public void testLunchAddNewModel_WhenUserIsLogged() {
        Model model = mock(Model.class);

        when(authUser.isUserLogged()).thenReturn(true);
        String viewName = controller.lunchAddNewModel(model);

        assertEquals("add", viewName);
        verify(model).addAttribute(eq("carmodels"), any(CarModels.class));
    }

    @Test
    public void testLunchAddNewModel_WhenUserNotLogged() {
        Model model = mock(Model.class);

        when(authUser.isUserLogged()).thenReturn(false);
        String viewName = controller.lunchAddNewModel(model);

        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    public void testAddNewModel_WhenUserIsLogged() {
        CarModels carModels = new CarModels();

        when(authUser.isUserLogged()).thenReturn(true);
        String viewName = controller.AddNewModel(carModels);

        assertEquals("redirect:/", viewName);
        verify(service).addCarModels(carModels);
    }

    @Test
    public void testAddNewModel_WhenUserNotLogged() {
        CarModels carModels = new CarModels();

        when(authUser.isUserLogged()).thenReturn(false);
        String viewName = controller.AddNewModel(carModels);

        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    public void testOpenEditPage_WhenUserIsLogged() {
        Model model = mock(Model.class);
        int id = 1;
        CarModels carModel = new CarModels();

        when(authUser.isUserLogged()).thenReturn(true);
        when(service.findCarModelsById(id)).thenReturn(carModel);

        String viewName = controller.openEditPage(model, id);

        assertEquals("edit", viewName);
        verify(model).addAttribute("carmodels", carModel);
    }

    @Test
    public void testOpenEditPage_WhenUserNotLogged() {
        Model model = mock(Model.class);
        int id = 1;

        when(authUser.isUserLogged()).thenReturn(false);

        String viewName = controller.openEditPage(model, id);

        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    public void testUpdateModel_WhenUserIsLogged() {
        CarModels carModels = new CarModels();

        when(authUser.isUserLogged()).thenReturn(true);
        String viewName = controller.updateModel(carModels);

        assertEquals("redirect:/", viewName);
        verify(service).updateCarModels(carModels);
    }

    @Test
    public void testUpdateModel_WhenUserNotLogged() {
        CarModels carModels = new CarModels();

        when(authUser.isUserLogged()).thenReturn(false);
        String viewName = controller.updateModel(carModels);

        assertEquals("redirect:/auth/login", viewName);
    }

    @Test
    public void testDeleteModel_WhenUserIsLogged() {
        int id = 1;

        when(authUser.isUserLogged()).thenReturn(true);
        String viewName = controller.deleteModel(id);

        assertEquals("redirect:/", viewName);
        verify(service).deleteById(id);
    }

    @Test
    public void testDeleteModel_WhenUserNotLogged() {
        int id = 1;

        when(authUser.isUserLogged()).thenReturn(false);
        String viewName = controller.deleteModel(id);

        assertEquals("redirect:/auth/login", viewName);
    }

    // Similarly, you can write tests for other methods like add, edit, update, and delete.
}
