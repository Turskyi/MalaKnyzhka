package com.turskyi.malaknyzhka.ui.privacy

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import malaknyzhka.composeapp.generated.resources.Res
import malaknyzhka.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun PrivacyPolicyPage(onBack: () -> Unit) {
    val scrollState: ScrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Політика конфіденційності (Privacy Policy)",
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 2.dp,
                        ),
                        onClick = onBack
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = "Мала Книжка",
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                        )
                    }
                }
            )
        }
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            SelectionContainer {
                Text(
                    text = "Політика конфіденційності (українською мовою)\n\n"
                            + "Ця політика конфіденційності застосовується до додатка “Мала Книжка (Тарас Шевченко)” (надалі “Додаток”) для мобільних пристроїв, який створив Дмитро Турський (надалі “Постачальник послуг”) як безкоштовний сервіс. Цей сервіс надається для використання “як є”.\n\n"
                            + "Збір та використання інформації\n\n"
                            + "Додаток збирає інформацію під час завантаження та використання. Ця інформація може включати:\n"
                            + "• IP-адресу вашого пристрою (наприклад, IP-адреса);\n"
                            + "• Сторінки Додатка, які ви відвідуєте, час і дата відвідування, час, проведений на цих сторінках;\n"
                            + "• Час використання Додатка;\n"
                            + "• Операційну систему вашого мобільного пристрою.\n\n"
                            + "Додаток не збирає точну інформацію про місцезнаходження вашого мобільного пристрою.\n\n"
                            + "Додаток може збирати дані про ваше приблизне географічне місцезнаходження, які використовуються в наступних цілях:\n"
                            + "1. Аналітика та поліпшення: Агреговані та анонімні дані використовуються для аналізу поведінки користувачів, визначення тенденцій і покращення роботи Додатка.\n"
                            + "2. Сторонні сервіси: Час від часу анонімні дані можуть передаватися зовнішнім сервісам для покращення функціональності Додатка.\n\n"
                            + "Постачальник послуг може використовувати надану вами інформацію для спілкування з вами, надання важливої інформації, повідомлень та маркетингових пропозицій.\n\n"
                            + "Для кращого досвіду використання Додатка Постачальник послуг може запросити вас надати певну інформацію, яка буде зберігатися відповідно до цієї політики конфіденційності.\n\n"
                            + "Доступ третіх сторін\n\n"
                            + "Агреговані та анонімні дані періодично передаються зовнішнім сервісам для покращення Додатка. Постачальник послуг може ділитися вашою інформацією з третіми сторонами у випадках, зазначених у цій політиці.\n\n"
                            + "Сторонні сервіси, які використовує Додаток:\n"
                            + "• Google Play Services\n"
                            + "• Google Analytics for Firebase\n"
                            + "• Firebase Crashlytics\n\n"
                            + "Постачальник послуг може розкрити зібрану інформацію в таких випадках:\n"
                            + "• Вимога закону, наприклад, судовий запит.\n"
                            + "• Захист прав, безпеки користувачів або інших осіб.\n"
                            + "• Співпраця з надійними постачальниками, які дотримуються цієї політики.\n\n"
                            + "Права відмови\n\n"
                            + "Ви можете припинити збір даних, видаливши Додаток зі свого пристрою. Для цього скористайтеся стандартними методами видалення, доступними у вашому пристрої чи маркетплейсі.\n\n"
                            + "Політика зберігання даних\n\n"
                            + "Постачальник послуг зберігатиме надані дані до моменту використання Додатка або протягом розумного періоду після цього. Якщо ви бажаєте видалити свої дані, надішліть запит на адресу електронної пошти: dmytro@turskyi.com.\n\n"
                            + "Діти\n\n"
                            + "Додаток не адресований особам молодше 6 років. Постачальник послуг не збирає даних у дітей до 6 років. У разі виявлення таких даних вони будуть негайно видалені. Якщо ви — батько чи опікун і знаєте, що ваша дитина надала особисту інформацію, зв’яжіться з нами.\n\n"
                            + "Безпека\n\n"
                            + "Постачальник послуг вживає фізичних, електронних і процедурних заходів для захисту вашої інформації.\n\n"
                            + "Зміни\n\n"
                            + "Ця політика конфіденційності може оновлюватися час від часу. Постачальник послуг рекомендує регулярно перевіряти цю сторінку на наявність змін.\n\n"
                            + "Згода\n\n"
                            + "Використовуючи Додаток, ви погоджуєтесь із цією Політикою конфіденційності.\n\n"
                            + "Контакти\n\n"
                            + "Якщо у вас виникли питання щодо цієї Політики конфіденційності, зв’яжіться з нами за адресою dmytro@turskyi.com.\n\n"
                            + "Privacy Policy (English)\n\n"
                            + "This privacy policy applies to the Мала Книжка (Тарас Шевченко) app (hereby referred to as \"Application\") for mobile devices that was created by Dmytro Turskyi (hereby referred to as \"Service Provider\") as a Free service. This service is intended for use \"AS IS\".\n\n"
                            + "Information Collection and Use\n\n"
                            + "The Application collects information when you download and use it. This information may include information such as:\n"
                            + "• Your device's Internet Protocol address (e.g. IP address);\n"
                            + "• The pages of the Application that you visit, the time and date of your visit, the time spent on those pages;\n"
                            + "• The time spent on the Application;\n"
                            + "• The operating system you use on your mobile device.\n\n"
                            + "The Application does not gather precise information about the location of your mobile device.\n\n"
                            + "The Application collects your device's location, which helps the Service Provider determine your approximate geographical location and make use of in below ways:\n"
                            + "1. Analytics and Improvements: Aggregated and anonymized location data helps the Service Provider to analyze user behavior, identify trends, and improve the overall performance and functionality of the Application.\n"
                            + "2. Third-Party Services: Periodically, the Service Provider may transmit anonymized location data to external services. These services assist them in enhancing the Application and optimizing their offerings.\n\n"
                            + "The Service Provider may use the information you provided to contact you from time to time to provide you with important information, required notices and marketing promotions.\n\n"
                            + "For a better experience, while using the Application, the Service Provider may require you to provide us with certain personally identifiable information. The information that the Service Provider request will be retained by them and used as described in this privacy policy.\n\n"
                            + "Third Party Access\n\n"
                            + "Only aggregated, anonymized data is periodically transmitted to external services to aid the Service Provider in improving the Application and their service. The Service Provider may share your information with third parties in the ways that are described in this privacy statement.\n\n"
                            + "Please note that the Application utilizes third-party services that have their own Privacy Policy about handling data. Below are the links to the Privacy Policy of the third-party service providers used by the Application:\n"
                            + "• Google Play Services\n"
                            + "• Google Analytics for Firebase\n"
                            + "• Firebase Crashlytics\n\n"
                            + "The Service Provider may disclose User Provided and Automatically Collected Information:\n"
                            + "• as required by law, such as to comply with a subpoena, or similar legal process;\n"
                            + "• when they believe in good faith that disclosure is necessary to protect their rights, protect your safety or the safety of others, investigate fraud, or respond to a government request;\n"
                            + "• with their trusted services providers who work on their behalf, do not have an independent use of the information we disclose to them, and have agreed to adhere to the rules set forth in this privacy statement.\n\n"
                            + "Opt-Out Rights\n\n"
                            + "You can stop all collection of information by the Application easily by uninstalling it. You may use the standard uninstall processes as may be available as part of your mobile device or via the mobile application marketplace or network.\n\n"
                            + "Data Retention Policy\n\n"
                            + "The Service Provider will retain User Provided data for as long as you use the Application and for a reasonable time thereafter. If you'd like them to delete User Provided Data that you have provided via the Application, please contact them at dmytro@turskyi.com and they will respond in a reasonable time.\n\n"
                            + "Children\n\n"
                            + "The Service Provider does not use the Application to knowingly solicit data from or market to children under the age of 6.\n\n"
                            + "The Application does not address anyone under the age of 6. The Service Provider does not knowingly collect personally identifiable information from children under 6 years of age. In the case the Service Provider discover that a child under 6 has provided personal information, the Service Provider will immediately delete this from their servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact the Service Provider (dmytro@turskyi.com) so that they will be able to take the necessary actions.\n\n"
                            + "Security\n\n"
                            + "The Service Provider is concerned about safeguarding the confidentiality of your information. The Service Provider provides physical, electronic, and procedural safeguards to protect information the Service Provider processes and maintains.\n\n"
                            + "Changes\n\n"
                            + "This Privacy Policy may be updated from time to time for any reason. The Service Provider will notify you of any changes to the Privacy Policy by updating this page with the new Privacy Policy. You are advised to consult this Privacy Policy regularly for any changes, as continued use is deemed approval of all changes.\n\n"
                            + "This privacy policy is effective as of 2025-01-09.\n\n"
                            + "Your Consent\n\n"
                            + "By using the Application, you are consenting to the processing of your information as set forth in this Privacy Policy now and as amended by us.\n\n"
                            + "Contact Us\n\n"
                            + "If you have any questions regarding privacy while using the Application, or have questions about the practices, please contact the Service Provider via email at dmytro@turskyi.com."
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }
}
