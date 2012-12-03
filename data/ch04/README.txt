README.txt

Clustering data example:

[1] Age is measured in years.

[2] Income Range IDs are based on the following table:

	ID, Range (USD)
	0, "65,000 to 80,000"	
	1, "80,001 to 90,000"
	2, "90,001 to 105,000"
	3, "105,001 to 120,000"
	4, "More than 120,001"
	
[3] Education IDs are based on the following table:

	ID, Education level
	0, "High school"
	1, "College"
	2, "Graduate school (MBA, M.Sc.)"
	3, "Medical school (M.D.)"
	4, "Doctorate (Ph.D.)"
	
[4] "Skills" refers to the degree that they consider their participation as 
    a good way of honing their professional skills, on a scale from 1 to 5.
    
[5] "Social" refers to the degree that they consider their participation as 
    a good way of building social relationships with people that have the same 
    interests as they do, measured on a scale from 1 to 5. 

[6] "isPaid" refers to whether a user is paid to participate or not (Boolean)

___________________________________________________________________________________

Load the data in MySQL
___________________________________________________________________________________

CREATE DATABASE iweb2;

USE iweb2;

CREATE TABLE sf_users (Name VARCHAR(250) NOT NULL PRIMARY KEY, Age INT NOT NULL, IncomeRange INT NOT NULL, 
           Education INT NOT NULL, Skills INT NOT NULL, Social INT NOT NULL, isPaid INT NOT NULL);
                       
LOAD DATA INFILE 'C:\\iWeb2\\data\\ch04\\clusteringSF.dat' INTO TABLE sf_users FIELDS TERMINATED BY ',' ENCLOSED BY '"' LINES TERMINATED BY '\n' IGNORE 1 LINES;