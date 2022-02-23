#### Этот плагин был написан для личного сервера. После его закрытия, этот проект стал публичным.

# Подробнее о BetterInteraction
  Плагин на основе API форка [глобальной серверной модификации MC](https://github.com/PaperMC/Paper) на языке Java. Для сборки используется Maven.
  
  Структура проекта:<br><br>
![image](https://user-images.githubusercontent.com/78872275/155320634-1dba0dc8-c81f-4aae-aeb9-0a5ca5b5f16a.png)

### База данных (SQLite)
  Для хранения различных данных о игроках, используется база данных **SQLite**.<br>На момент написания этого текста в базе ханятся: количество доната, активные подписки и дата их окончания.<br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155319203-a93609ba-a954-4634-88e3-dae841203c16.png)

### Улучшенный чат
  - Текстовое и звуковое оповещение при заходе на сервер. (Используется API плагина [AuthMe](https://github.com/AuthMe/AuthMeReloaded/))<br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155309037-1b265d75-ca2b-4f49-9e1c-d0988953f64f.png)<br><br>
  - Фильтрация чата. (Используются регулярные выражения) <br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155314385-a1245712-c5af-488a-930a-02d2a8fd872d.png)<br><br>
  - Локальный и глобальный чат. <br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155314553-609fdb90-e248-4033-b81a-480de3e6f1a9.png)<br><br>
  - Функция удаление отдельных сообщений для администрации. Реализовано через массив объектов с историями чатов игроков. (Используется API плагина [ProtocolLib](https://github.com/dmulloy2/ProtocolLib/))<br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155315452-e60c6ff4-e255-4cd8-9041-8810fe873617.png) **->** ![image](https://user-images.githubusercontent.com/78872275/155315496-24f77300-0688-4e52-a3b9-73f31daecc9c.png)<br><br>
  - Личные сообщения со звуковым оповещением. (Используется API плагина [EssentialsX](https://github.com/EssentialsX/Essentials/))<br><br>
  ![image](https://user-images.githubusercontent.com/78872275/155316194-7278a714-ec45-46e3-bb88-df4a3513abce.png)<br>

### Другое
  В плагине используются и прочие API, WebHooks и др. Подробнее можно увидеть в коде.
