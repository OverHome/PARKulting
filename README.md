# PARKulting – ассистент парковой культуры Москвы 
## Введение
Приложение «PARKulting» видит своей миссией ознакомление жителей Москвы и гостей столицы с культурой парков города Москвы с применением элементов геймификации, учитывая современные стратегии развития московских парков.

## ВАЖНО
Для полноценной работы приложения требуется [сервер](https://github.com/OverHome/PARKulting-server)

Приложение и сервер должны находиться в одной локальной сети или же пробросить порт через [ngrok](https://ngrok.com/) 

```bash
ngrok http 5000 
```

Так же нужно будет изменить BASE_URL в конфиге перед сборкой [PARKulting](https://github.com/OverHome/PARKulting/blob/main/app/src/main/java/com/over/parkulting/tools/ApiTool.java) 
```
private static final String BASE_URL = "{Ваш URl или IP сервера}";
```
