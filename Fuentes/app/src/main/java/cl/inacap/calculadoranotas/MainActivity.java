package cl.inacap.calculadoranotas;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.inacap.calculadoranotas.R;
import cl.inacap.calculadoranotas.dto.Notas;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Notas> notas = new ArrayList<>();
    private EditText notaTxt;
    private EditText porcentajeTxt;

    private ListView notasLv;
    private Button agregarBtn;
    private Button limpiarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.notaTxt =  findViewById(R.id.notaTxt);
        this.porcentajeTxt = findViewById(R.id.porcentajeTxt);
        this.notasLv = findViewById(R.id.notasLv);
        this.agregarBtn = findViewById(R.id.agregarBtn);
        this.limpiarBtn = findViewById(R.id.limpiarBtn);
        this.agregarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> errores = new ArrayList<>();
                double nota = 0;
                int porcetaje = 0;
                try{
                    nota = Double.parseDouble(notaTxt.getText().toString());
                    if(nota <1.0 || nota > 7.0){
                        throw new NumberFormatException();
                    }
                }catch (NumberFormatException ex){
                    errores.add("La nota es un valor numero entre 1.0 y 7.0");
                }
                try{
                    porcetaje = Integer.parseInt(porcentajeTxt.getText().toString());
                    if(porcetaje<1 || porcetaje>100){
                        throw new NumberFormatException();
                    }
                }catch (NumberFormatException ex){
                    errores.add("El porcentaje debe ser un numero entre 1 y 100");

                }
                if(errores.isEmpty()){
                    Notas n = new Notas();
                    n.setValor(nota);
                    n.setPorcentaje(porcetaje);
                    notas.add(n);
                }else{
                    mostrarErrores(errores);
                }
            }
        });
    }

    private void mostrarErrores(List<String> errores){
        String mensaje = " ";
        for(String e: errores){
            mensaje+= "-" + e + "\n";
        }
        //mostrar en un mensaje de alerta
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        //chainig (es un retorno de vistas)
        alertBuilder.setTitle("Error de validacion")// define titulo
                .setMessage(mensaje)// define contenido
                .setPositiveButton("Aceptar", null) //agrega boton aceptar
                .create()//crear el alert
                .show();// lo muestra
    }

}