Дано:

Директория, в которой лежат файлы по следующей структуре: topic_name/history/run_timestamp/offsets.csv
topic_name - имя топика (по сути просто строка) run_timestamp - таймстемп запуска некоей процедуры, 
в формате YYYY-MM-DD-HH-mm-ss (минуты и секунды с лидирующими нулями) offsets.csv - файл с двумя колонками, 
разделенными запятыми. 

Первая колонка - номер партиции (int), вторая колонка - число сообщений (long). В одном файле десятки записей.

Нужно разработать REST-сервис, который позволяет получить:
- Список топиков, данные по которым есть данные в этой директории
- По топику:  таймстемп последнего запуска
- По топику  для последнего запуска, статистику: суммарное число сообщений во всех патрициях, 
  максимальное/минимальное число сообщений, среднее число сообщений
- По топику и таймстемпу запуска: список партиций и число сообщений по каждой партиции

Сервис должен поставляться в виде запускаемого jar-файла, 
параметром запуска сервиса должен быть base_dir - директория откуда начинать искать топики. 
Язык реализации - Scala, для http использовать spray.io или akka-http. Все остальное - на ваше усмотрение.
