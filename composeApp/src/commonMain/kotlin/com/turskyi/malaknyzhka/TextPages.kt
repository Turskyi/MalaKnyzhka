import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TextPages(
    currentPage: Int
) {
    val texts = listOf(
        """
        Думи мої, думи мої,
        Ви мої єдині,
        Не кидайте хоч ви мене
        При лихій годині.
        Прилітайте, сизокрилі
        Мої голуб’ята,
        Із-за Дніпра широкого
        У степ погуляти
        З киргизами убогими,
        Вони вже убогі,
        Уже голі… Та на волі
        Ще моляться Богу.
        Прилітайте ж, мої любі,
        Тихими речами
        Привітаю вас, як діток,
        І заплачу з вами.
        """.trimIndent(),
        """
        Зоре моя вечірняя,
        Зійди над горою,
        Поговорим на чужині
        Тихенько з тобою.
        Розкажи, як за горою
        Сонечко сідає,
        Як у Дніпра веселочка
        Води позичає
        Як широкая сокорина
        Віти розпустила…
        А над самою водою
        Верба похилилась;
        Аж по воді розіслала
        Зеленії віти,
        А на вітах гойдаються
        Нехрещені діти.
        Як у полі на могилі
        Вовкулак ночує,
        А сич в лісі, то на стрісі
        Недолю віщує.
        Як сон-трава при долині
        Вночі процвітає…
        """.trimIndent()
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(texts[currentPage])
    }
}
