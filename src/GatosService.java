import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GatosService {
    public static void verGatos() throws IOException, InterruptedException {
        //1. Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        //Cortar los corchetes
        elJson = elJson.substring(1,elJson.length());
        elJson = elJson.substring(0,elJson.length()-1);

        //Crear un objeto de la clase Gson

        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson,Gatos.class);

        //redimensionar en caso de necesitar
        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);
            if(fondoGato.getIconWidth()> 800){
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            String menu = "Opciones: \n"
                   +"1. Ver otra imagen \n"
                    +"2. Favorito \n"
                    +"3. Volver \n";
            String[] botones ={"ver otra imagen","favorito","volver"};
            String id_gato = (gatos.getId());
            String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);

            int seleccion = -1;

            for (int i=0;i<botones.length;i++) {
                if (opcion.equals(botones[i])) {
                    seleccion = i;
                }
            }
            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;

            }

        }catch(IOException e){
            System.out.println(e);
        }

    }

    public static void favoritoGato(Gatos gatos){
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n    \"image_id\":\""+ gatos.getId()+"\"\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatos.apikey)
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e) {
            System.out.println(e);
        }
    }
    public static void verFavoritos(String apikey) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .method("GET", null)
                .addHeader("x-api-key", apikey)
                .build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();
        Gson gson = new Gson();

        GatosFav[] gatosArray = gson.fromJson(elJson,GatosFav[].class);
        if(gatosArray.length > 0 ){
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int)(Math.random() * ((max-min)+1)) + min;
            int indice = aleatorio-1;

            GatosFav gatosfav = gatosArray[indice];

            //redimensionar en caso de necesitar
            Image image = null;
            try{
                URL url = new URL(gatosfav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);
                if(fondoGato.getIconWidth()> 800){
                    //redimensionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800,600, Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }
                String menu = "Opciones: \n"
                        +"1. Ver otra imagen \n"
                        +"2. Eliminar favorito \n"
                        +"3. Volver \n";
                String[] botones ={"ver otra imagen","eliminar favorito","volver"};
                String id_gato = (gatosfav.getId());
                String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato,JOptionPane.INFORMATION_MESSAGE,fondoGato,botones,botones[0]);

                int seleccion = -1;

                for (int i=0;i<botones.length;i++) {
                    if (opcion.equals(botones[i])) {
                        seleccion = i;
                    }
                }
                switch (seleccion){
                    case 0:
                        verFavoritos(apikey);
                        break;
                    case 1:
                        borrarFavorito(gatosfav);
                        break;
                    default:
                        break;

                }

            }catch(IOException e){
                System.out.println(e);
            }

        }
        }

    public static void borrarFavorito(GatosFav gatofav) {

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/" + gatofav.getId() + "")
                    .method("DELETE", body)
                    .addHeader("x-api-key", gatofav.apikey)
                    .build();
            Response response = client.newCall(request).execute();

        } catch (IOException e) {
            System.out.println(e);

        }
    }
}
