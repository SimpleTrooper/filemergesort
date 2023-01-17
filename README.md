### Приложение для сортировки файлов слиянием

Входные файлы содержат данные одного из двух видов: целые числа или строки. Данные записаны
в столбик (каждая строка файла – новый элемент). Строки могут содержать любые не пробельные
символы, строки с пробелами считаются ошибочными. Также считается, что файлы предварительно
отсортированы. __Если строка ошибочная или нарушает сортировку - то она пропускается.__

Параметры программы задаются при запуске через аргументы командной строки, по порядку:
1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию;
2. тип данных (-s или -i), обязательный;
3. имя выходного файла, обязательное;
4. остальные параметры – имена входных файлов, не менее одного.
   Примеры запуска из командной строки для Windows:

       sort-it.exe -i -a out.txt in.txt (для целых чисел по возрастанию)
       sort-it.exe -s out.txt in1.txt in2.txt in3.txt (для строк по возрастанию)
       sort-it.exe -d -s out.txt in1.txt in2.txt (для строк по убыванию)


### Зависимости и запуск
- Используется Java 17. Сборщик - Apache Maven 3.8.7
- Зависимости:
  - lombok 1.18.24
  
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
        </dependency>
  - logback-classic 1.4.5
  
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.4.5</version>
        </dependency>
  - junit-jupiter 5.9.2

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.2</version>
            <scope>test</scope>
        </dependency>

Для сборки по умолчанию (без тестов) используйте команду:

    mvn clean package
Для сборки с тестами используйте:

    mvn -DskipTests=false clean package