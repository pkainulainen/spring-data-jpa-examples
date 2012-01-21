package net.petrikainulainen.spring.datajpa.controller;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Petri Kainulainen
 */
public class HomeControllerTest {

    private HomeController controller;
    
    @Before
    public void setUp() {
        controller = new HomeController();
    }
    
    @Test
    public void showPage() {
        String homeView = controller.showPage();
        assertEquals(HomeController.HOME_VIEW, homeView);
    }
}
