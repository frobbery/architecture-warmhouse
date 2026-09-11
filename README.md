# Задание 1. Анализ и планирование

### 1. Описание функциональности монолитного приложения

**Управление отоплением:**

- Пользователи могут удалённо включать/выключать отопление в своих домах

**Мониторинг температуры:**

- Пользователи могут просматривать текущую температуру в своих домах через веб-интерфейс
- Система получает данные о температуре с датчиков, установленных в домах

### 2. Анализ архитектуры монолитного приложения

- **Язык программирования**: Go
- **База данных**: PostgreSQL
- **Архитектура**: Монолитная, все компоненты системы (обработка запросов, бизнес-логика, работа с данными) находятся в рамках одного приложения.
- **Взаимодействие**: Синхронное, по REST'у, запросы обрабатываются последовательно.

### 3. Определение доменов и границы контекстов

- **Домен**: управление устройствами
  - поддомен администрирования сенсоров
    - контекст: добавление устройства
    - контекст: удаление устройства
  - поддомен: обновление данных устройств
    - контекст: обновление значения сенсора
    - контекст: обновление информации о сенсоре
- **Домен**: просмотр температуры
- **Домен**: управление отоплением

### **4. Проблемы монолитного решения**

- Пользователи не могут сами регистрировать свои устройства
- Добавление нового функционала затруднено, так как при тестировании придется тестировать весь прошлый функционал
- Масштабируемость ограничена, нельзя масштабировать по компонентам
- Развертывание требует остановки всего приложения

### 5. Визуализация контекста системы — диаграмма С4

![As-is context diagram](diagrams/context/warmhouse-as-is-context.png)

# Задание 2. Проектирование микросервисной архитектуры

**Диаграмма контейнеров (Containers)**

![To-be сontainer diagram](diagrams/container/warmhouse-to-be-container.png)

**Диаграмма компонентов (Components)**

![WebApp component diagram](diagrams/component/webapp-component.png)

![UserService component diagram](diagrams/component/user-service-component.png)

![DeviceService component diagram](diagrams/component/device-service-component.png)

![ScryptService component diagram](diagrams/component/scrypt-service-component.png)

![CameraService component diagram](diagrams/component/camera-service-component.png)

![TelemetryService component diagram](diagrams/component/telemetry-service-component.png)

**Диаграмма кода (Code)**

![ScryptExecutorService code diagram](diagrams/code/scrypt-service-executor-code.png)

# Задание 3. Разработка ER-диаграммы

![User service ER-diagram](diagrams/er/user-database.png)

![Device service ER-diagram](diagrams/er/device-database.png)

![Telemetry service ER-diagram](diagrams/er/telemetry-database.png)

![Scrypt service ER-diagram](diagrams/er/scrypt-database.png)

Связи между всеми БД:

![All connections ER-diagram](diagrams/er/all-connections.png)

# Задание 4. Создание и документирование API

### 1. Тип API

Для взаимодействия между мобильным приложением и бэкендом будет использоваться REST API по HTTPS, как и для взаимодействия между основным бэкенд приложением и микросервисами, так как для таких запросов важен немедленный ответ для отображения в приложении.
Для публикации изменений телеметрии в БД выбрано асинхронное взаимодействие через брокер сообщений, так как устройствам не нужно получать ответ, и это позволяет не опрашивать устройства через микросервис. Также сохранена возможность запрашивать телеметрию в реальном времени через REST API.

### 2. Документация API

Здесь приложите ссылки на документацию API для микросервисов, которые вы спроектировали в первой части проектной работы. Для документирования используйте Swagger/OpenAPI или AsyncAPI.

# Задание 5. Работа с docker и docker-compose

Перейдите в apps.

Там находится приложение-монолит для работы с датчиками температуры. В README.md описано как запустить решение.

Вам нужно:

1) сделать простое приложение temperature-api на любом удобном для вас языке программирования, которое при запросе /temperature?location= будет отдавать рандомное значение температуры.

Locations - название комнаты, sensorId - идентификатор названия комнаты

```
	// If no location is provided, use a default based on sensor ID
	if location == "" {
		switch sensorID {
		case "1":
			location = "Living Room"
		case "2":
			location = "Bedroom"
		case "3":
			location = "Kitchen"
		default:
			location = "Unknown"
		}
	}

	// If no sensor ID is provided, generate one based on location
	if sensorID == "" {
		switch location {
		case "Living Room":
			sensorID = "1"
		case "Bedroom":
			sensorID = "2"
		case "Kitchen":
			sensorID = "3"
		default:
			sensorID = "0"
		}
	}
```

2) Приложение следует упаковать в Docker и добавить в docker-compose. Порт по умолчанию должен быть 8081

3) Кроме того для smart_home приложения требуется база данных - добавьте в docker-compose файл настройки для запуска postgres с указанием скрипта инициализации ./smart_home/init.sql

Для проверки можно использовать Postman коллекцию smarthome-api.postman_collection.json и вызвать:

- Create Sensor
- Get All Sensors

Должно при каждом вызове отображаться разное значение температуры

Ревьюер будет проверять точно так же.


