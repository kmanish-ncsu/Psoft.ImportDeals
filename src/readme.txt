Tech stack used:

Spring(DI, MVC)
Hibernate(JPA api's)
JSP, Servlets
Transactions
Maven

Steps:

1) Create a mysql database "progresssoft"
2) Run the DDL's from DDLs.sql, commit
3) Clone & Import the project from https://github.com/kmanish-ncsu/Psoft.ImportDeals into IDE (Intellij/Eclipse)
4) Build & deploy
5) Home URL : "http://localhost:8080/importdeals/all"
6) Create 2 folders, one for unprocessed files & one for processed files. Replace the "unprocessed.file.folder" & "processed.file.folder" values respectively in psoft.properties file
7) Drop the 2 csv files provided into "unprocessed.file.folder" and refresh page. You should see the files under "Unprocessed files"
8) To process a file, click on "Process". Page displays the time taken to process the file. (100,000 rows take 3 seconds)
9) To view a processed file's details, click on "Detail"
10) Click "Home" to go to homepage
11) To see latest currency count (deals per Ordering currency) click on "Curreny Count" on HomePage; click "Home" to go to HomePage
12) To reprocess with same files, move all files from "processed.file.folder" to "unprocessed.file.folder" AND run the #TRUNCATE commands given in DDLs.sql

Files are not processed automatically but are triggered for processing on clicking the "Process" button from homepage
As of now, it takes 3 seconds to process & populate database for a 100,000 row csv deal file.