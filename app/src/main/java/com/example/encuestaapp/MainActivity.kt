package com.example.encuestaapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.encuestaapp.ui.theme.EncuestaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EncuestaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SurveyScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

    @Composable
    fun SurveyScreen(modifier: Modifier = Modifier) {
        var AceptTerminos by remember { mutableStateOf(false) }
        val preguntas = listOf(
            "¿Cree que su Universidad es la mejor de Managua?",
            "¿Cree que la calidad de su Universidad es la mejor?",
            "¿Cree que las clases que se imparten le ayudarán a su vida como profesional?",
            "¿Cree que la clase de Programación Orientada a Objetos contribuirá a su futuro profesional?",
            "¿Considera que hacer la tarea con carácter individual y honesto le ayudará en el futuro profesional?"
        )
        var Respuestas = remember { mutableStateListOf<Int?>(null, null, null, null, null) }
        var OtrasRespuesta = remember { mutableStateListOf("", "", "", "", "") }

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Encuesta de Aceptación",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = AceptTerminos, onCheckedChange = { isChecked ->
                    AceptTerminos = isChecked
                    if (!isChecked) {
                        Respuestas.fill(null)
                        OtrasRespuesta.fill("")
                    }
                })
                Text(text = "Acepto los términos y condiciones")
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                itemsIndexed(preguntas) { index, pregunta ->
                    PreguntaItem(
                        pregunta = pregunta,
                        Bandera = AceptTerminos,
                        Respuestas = Respuestas[index],
                        onAnswerChange = { Respuestas[index] = it },
                        otraRespuesta = OtrasRespuesta[index],
                        onOtraRespuestaChange = { OtrasRespuesta[index] = it }
                    )
                }
            }
            Button(onClick = {
                if(AceptTerminos && Respuestas.all{ it != null && (it !=6 || OtrasRespuesta[Respuestas.indexOf(it)].isNotEmpty()) }){
                    Toast.makeText(context, "Encuesta enviado con exito", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context, "Debe responder todas las preguntas", Toast.LENGTH_LONG).show()
                }
            },
                enabled = AceptTerminos,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Enviar")
            }
        }
    }

    @Composable
    fun PreguntaItem(
        pregunta: String,
        Bandera: Boolean,
        Respuestas: Int?,
        onAnswerChange: (Int?) -> Unit,
        otraRespuesta: String,
        onOtraRespuestaChange: (String) -> Unit
    ) {
        Column {
            Text(text = pregunta)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                (1..5).forEach { value ->
                    RadioButton(
                        selected = Respuestas == value,
                        onClick = {onAnswerChange(value)},
                        enabled = Bandera,
                    )
                    Text(text = value.toString())
                }
                RadioButton(
                    selected = Respuestas == 6,
                    onClick = {onAnswerChange(6)},
                    enabled = Bandera
                    )
                    Text(text = "Otro")
                    if (Respuestas == 6) {
                        TextField(
                            value = otraRespuesta,
                            onValueChange = onOtraRespuestaChange,
                            enabled = Bandera,
                            modifier = Modifier.width(150.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            visualTransformation = VisualTransformation.None
                        )
                    }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SurveyScreenPreview() {
        EncuestaAppTheme {
            SurveyScreen()
        }
    }