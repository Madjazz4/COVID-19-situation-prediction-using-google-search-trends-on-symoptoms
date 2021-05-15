# PBDAA Final Project: COVID19 Pandemic Prediction Based on Google Search Trends

The project aims at predicting if a region has relatively more confirmed cases given its google search trends for COVID-19-related symptoms. For now it is only restricted inside the US, since regions other than states of the US do not have a complete google search trend for these symptoms.

The original dataset source: https://github.com/GoogleCloudPlatform/covid-19-open-data

## Folder Content Explanation

The folder includes:
    
    data_ingest: includes all the screenshots of uploading my data to HDFS. In all cases, I use the -put command to upload my data onto HDFS.
    
    etl_code: includes all ETL codes for my datasets. Details will be explained later.
        
    profiling_code: includes the profiling code and all outputs for profiling.
    
    ana_code: includes all analytic codes.
    
    screenshots: including all terminal screenshots other than data ingestion.
    
## Detailed Procedure

Then I will explain step by step how my project works, including all the related files and screenshots you may want to inspect. Please make sure that you follow the procedures step by step, since for most procedures they use the output of the last procedure as their inputs.

Note that (Directory on HDFS) means you need to check it on HDFS, most of which are data sources, inputs and outputs. (Folder) means you need to check it inside the folder of the zip file, most of which are screenshots and codes.

### 1. Data Cleaning

#### 1.1 Cleaning for google-search-trends.csv

##### 1.1.1 RowClean

    Input: (Directory on HDFS) /user/rs6486/final_project/original_data/google-search-trends.csv

    Code: (Folder) etl_code/Clean_trend/1-RowClean

    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/1-Row_output.csv
    
    Screenshots: (Folder) screenshots/Trend/1-Row(1),1-Row(2)

    Introduction:

    The original trend data divides the regions explicitly, which means it also includes small regions under the state. But we only need data for states, not the subregions. So this is for deleting the rows for the regions that we do not want.
    
    Profiling: (Folder) profiliing_code/output_original_trend.txt, profiliing_code/output_1-Row.txt
    
    We have less data since the code simply picks those rows of a state, like "US_AL", instead of rows of smaller regions ("US_AL_01001") or other countries ("AU_ACT").
    
##### 1.1.2 ColumnClean

    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/1-Row_output.csv
    
    Code: (Folder) etl_code/Clean_trend/2-ColumnClean
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/2-Column_output.csv
    
    Screenshots: (Folder) screenshots/Trend/2-Column(1),2-Column(2)
    
    Introduction:
    
    The original trend data gives more than 200 symptoms, but we only need COVID-19 related symptoms. From another paper I extract 8 most frequent symptoms of COVID-19, so unnecessary columns need to be filtered.
    
    Profiling: (Folder) profiliing_code/output_1-Row.txt, profiling_code/output_2-Column.txt
    
    We have the same amount of data after this procedure, since we do not delete any rows.
    
##### 1.1.3 Process

    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/2-Column_output.csv
    
    Code: (Folder) etl_code/Clean_trend/3-Process
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/3-Process_output.csv
    
    Screenshots: (Folder) screenshots/Trend/3-Process(1),3-Process(2)
    
    Introduction:
    
    This is the key part. We will search a lot for symptoms at the beginning of the pandemic, but when we get used to it, we will not search for them often. From the data I can clearly see that during April 2020 trends are very high while they are low in November 2020, but confirmed cases for these two periods are equal. I reckon this is because people start to get used to the pandemic, and it indicates that absolute trends are not so reliable.
    
    As a result, I rank the trends of 51 states, and label the top 10 states "1" (more pandemic) and other states "0" (less pandemic). For example, going through trends of 51 states for "cough" on a specific day, Alaska ranks top 10 among the states, and it will be labeled 1 for cough on this day. Instead of absolute trend, we use relative trend to reduce the influence of time.
    
    The mapper considers the date as key and (state, trends) as value. For each symptom, the reducer checks its trends of 51 states, rank them and store the result in a list. Then for each state, the reducer check if it is top 10 states; if it is, label 1, else label 0. After 8 loops for 8 different symptoms, the new data will be like: 
        
        2020-04-05 US_AL 0 0 1 0 1 0 0 0
    Profiling: (Folder) profiling_code/output_2-Column.txt, profiling_code/output_3-Process.txt
    
    We have the same amount of data after this procedure, since we do not delete any rows.
    
#### 1.2 Cleaning for epidemiology.csv

##### 1.2.1 EpiClean

    Input: (Directory on HDFS) /user/rs6486/final_project/original_data/epidemiology.csv
    
    Code: (Folder) etl_code/Clean_epidemiology/1-EpiClean
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/1-EpiClean_output.csv
    
    Screenshots: (Folder) screenshots/Epidemiology/1-EpiClean(1),1-EpiClean(2)
    
    Introduction: like google-search-trends.csv, epidemiology.csv also have too many subregions. Also, we only need confirmed cases, so unnecessary columns are filtered.
    
    Profiling: (Folder) profiling_code/output_original_epidemiology.txt, profiling_code/output_1-Epiclean.txt
    
    We have less data since the code simply picks those rows of a state, like "US_AL", instead of rows of smaller regions ("US_AL_01001") or other countries ("AU_ACT").
     
##### 1.2.2 EpiProcess

    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/1-EpiClean_output.csv
    
    Code: (Folder) etl_code/Clean_epidemiology/2-EpiProcess
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/2-EpiProcess_output.csv
    
    Screenshots: (Folder) screenshots/Epidemiology/2-EpiProcess(1),2-EpiProcess(2)
    
    Introduction: like what we have done, We also label each row 1 or 0 by ranking their confirmed cases.
    
    Profiling: (Folder) profiling_code/output_1-Epiclean.txt, profiling_code/output_2-EpiProcess.txt
    
    We have the same amount of data after this procedure, since we do not delete any rows.

### 2. Data analytics
    
#### 2.1 Hive
    
    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/2-EpiProcess_output.csv, /user/rs6486/final_project/temp_data/3-Process_output.csv
    
    Code: it is executed on Hive, so no code is used.
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/hive_output.csv
    
    Screenshots: (Folder) screenshots/2-hive
    
    Introduction: We use Hive to join epidemiology and trends together. Though the number of records for these two tables are different, inner-joining them together can get all records with both data.
    
    Profiling: (Folder) profiling_code/output_2-EpiProcess.txt, profiling_code/output_3-Process.txt, profiling_code/output_hive.txt
    
    We have less data since there are rows that only have one of epidemiology or trends, so we delete them.
    
#### 2.2 Libsvm
    
    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/hive_output.csv
    
    Code: (Folder) ana_code/1-libsvm
    
    Output: (Directory on HDFS) /user/rs6486/final_project/temp_data/libsvm.txt
    
    Screenshots: (Folder) screenshots/3-Spark/libsvm(1),libsvm(2)
    
    Introduction: We change our data to libsvm format so it can be imported into spark.ml jobs.
    
    Profiling: (Folder) profiling_code/output_hive.txt, profiling_code/output_libsvm.txt
    
    We have less data since there are rows that have a null value with its confirmed cases, so we delete them.
    
#### 2.3 Spark

    Input: (Directory on HDFS) /user/rs6486/final_project/temp_data/libsvm.txt
    
    Code: (Folder) ana_code/2-spark
    
    Output/Screenshots: (Folder) screenshots/3-Spark
    
    Introduction: We use two different machine learning classifiers, while their accuracies are similar, around 82%.
