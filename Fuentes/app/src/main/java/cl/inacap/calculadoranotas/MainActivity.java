package cl.inacap.calculadoranotas;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.inacap.calculadoranotas.R;
import cl.inacap.calculadoranotas.dto.Notas;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int porcentajeActual = 0;
    private List<Notas> notas = new ArrayList<>();
    private EditText notaTxt;
    private EditText porcentajeTxt;

    private ListView notasLv;
    private ArrayAdapter<Notas> notasAdapter;
    private Button agregarBtn;
    private Button limpiarBtn;
    private LinearLayout promedioLl;
    private TextView promedioTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.promedioLl = findViewById(R.id.promedioLl);
        this.promedioTxt = findViewById(R.id.promedioTxt);
        this.notaTxt =  findViewById(R.id.notaTxt);
        this.porcentajeTxt = findViewById(R.id.porcentajeTxt);
        this.notasLv = findViewById(R.id.notasLv);
        this.notasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notas);
        this.notasLv.setAdapter(notasAdapter);
        this.agregarBtn = findViewById(R.id.agregarBtn);
        this.limpiarBtn = findViewById(R.id.limpiarBtn);
        this.limpiarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. Limpiar EditTexts
                notaTxt.setText("");
                promedioTxt.setText("");
                porcentajeTxt.setText("");
                //2. Hacer invisible el layout de resultados
                promedioLl.setVisibility(View.INVISIBLE);
                //3. Limpiar la lista de notas
                notas.clear();
                //4. Notificar al data adaptr para que se limpie
                notasAdapter.notifyDataSetChanged();
                //5. Dejar en - el porcentaje actual
                porcentajeActual = 0;

            }
        });
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
                    if(porcentajeActual + porcetaje > 100){
                        Toast.makeText(MainActivity.this, "No puede Exeder mas 100%", Toast.LENGTH_SHORT).show();
                    }else{

                    Notas n = new Notas();
                    n.setValor(nota);
                    n.setPorcentaje(porcetaje);
                    notas.add(n);
                    notasAdapter.notifyDataSetChanged();//Decir al adaptador que se actualizo
                    porcentajeActual+=porcetaje; // Imcremental el porcentaje
                    mostrarPromedio();
                    }
                }else{
                    mostrarErrores(errores);
                }
            }
        });
    }

    private void mostrarPromedio() {
        //0. Calcular promedi
        double promedio = 0;
        for(Notas n: notas){
            promedio+= n.getValor() * n.getPorcentaje()/100; //
        }
        //1. Mostrar el promedio en el TextView
        this.promedioTxt.setText(String.format("%.1f",promedio));//Todo: Y los decimale?
        //2. Colorear TextViee
        if(promedio < 4.0){// Si el pro es menor que 4 voy a definir el textColor
            this.promedioTxt.setTextColor(ContextCompat.getColor(this, R.color.colorRojo));
        }else{
            this.promedioTxt.setTextColor(ContextCompat.getColor(this, R.color.colorVerde));
        }
        //3. Hacer visible el layout
        this.promedioLl.setVisibility(View.VISIBLE);
    }

    private void mostrarErrores(List<String> errores){
        String mensaje = " ";
        for(String e: errores){
            mensaje+= "-" + e + "\n";// Debe Ingresar
        }                           // Debe Ingresar -->
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