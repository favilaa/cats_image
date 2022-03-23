import javax.swing.*;
import java.io.IOException;

public class Inicio {
    public static void main(String[] args) throws IOException, InterruptedException {
        int opcionMenu =-1;
        String[] botones = {"1. Ver gatos","2. Ver favoritos", "3. Salir"};
        do{
            //El menu principal
            String opcion = (String)JOptionPane.showInputDialog(null,"Gatitos java","Menu Principal",
                   JOptionPane.INFORMATION_MESSAGE,null,botones,botones[0]);
            //Validamos que opcion selecciona el usuario
            for (int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    opcionMenu=i;
                }
            }
            switch (opcionMenu){
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavoritos(gato.getApikey());
                default:
                    break;
            }
        }while(opcionMenu != 1);
    }
}
