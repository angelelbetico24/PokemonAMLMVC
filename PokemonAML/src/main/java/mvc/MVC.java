package mvc;

import controlador.*;
import vista.*;

/**
 *
 * @author angelelbetico24
 */
public class MVC {

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        MenuPrincipalCtrl controlador = new MenuPrincipalCtrl(menu);
        menu.setVisible(true);
    }
}
