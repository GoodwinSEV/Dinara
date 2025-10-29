package com.example.dinara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimalQuestApp()
        }
    }
}

@Composable
fun AnimalQuestApp() {
    //создаём переменные для статуса игры, числа, ввода и сообщений
    var chislo by remember { mutableStateOf((0..100).random()) }
    var konetsIgri by remember { mutableStateOf(false) }
    var vvod by remember { mutableStateOf("") }
    var message1 by remember { mutableStateOf("Зверюшки загадали число от 0 до 100!") }
    var message2 by remember { mutableStateOf("Попробуйте угадать его!") }

    //переменные на проверку состояния экрана(планшет или телефон)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isTablet = screenWidth >= 600

    //добавляем условия для заднего фона
    val backgroundRes = if (isTablet) {
        R.drawable.background_tablet
    } else {
        R.drawable.background
    }

    //для удобства будем всё собирать в боксе
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //высталяем по порядку задний фон
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "Фон со зверюшками",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        //затем столбец со всеми основными элементами
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message1,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 15.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = message2,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            //поле ввода
            OutlinedTextField(
                value = vvod,
                onValueChange = { vvod = it },
                label = { Text(
                    "Число",
                    color = Color.Black,
                    fontSize = 18.sp )
                        },
                placeholder = {
                    Text("",
                        color = Color.Black.copy(alpha = 0.7f),
                        fontSize = 24.sp )
                              },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 24.sp
                ),
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 30.dp)
            )

            Button(
                onClick = {
                    //дальше логика для кнопки
                    if (!konetsIgri) {
                        if (vvod.isEmpty()) {
                            message2 = "Введите число!"
                            return@Button
                        }

                        try {
                            //создали внутреннюю переменную и приняли туда числовое значие из ввода
                            val inp = vvod.toInt()

                            //делаем логические проверки вводимых данных
                            if (inp !in 0..100) {
                                message2 = "Число должно быть от 0 до 100!"
                                return@Button
                            }

                            //так же реакция приложения на ввод в виде сообщений для пользователя
                            when {
                                inp > chislo -> message2 = "Перелет!"
                                inp < chislo -> message2 = "Недолет!"
                                else -> {
                                    message1 = "Ура, вы угадали! Поздравляем!"
                                    message2 = "В точку!"
                                    konetsIgri = true
                                }
                            }
                            //проверка на корректность ввода
                        } catch (e: NumberFormatException) {
                            message2 = "Ошибка ввода!"
                        }
                    } else {
                        //замыкаем логический цикл чтобы пользователь мог перезапустить игру по нажатию кнопки
                        chislo = (0..100).random()
                        konetsIgri = false
                        message1 = "Зверюшки загадали число от 0 до 100!"
                        message2 = "Попробуйте угадать его!"
                    }
                    vvod = ""
                },

                //немного работы со стилем кнопки
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(65.dp)
            ) {
                //когда статус конца игры станет true - меняем текст кнопки на "Сыграть ещё"
                Text(
                    if (konetsIgri) "Сыграть еще" else "Ввести значение",
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAnimalQuestApp() {
    AnimalQuestApp()
}