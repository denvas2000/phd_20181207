/*
Version 2.1
Main features:  Reads a text file with limited users/ratings, compared to initial dataset (over10_movielens_simple.rar - MovieLens 100K simple)
                Calculates all data needed to perform NN-algorithm CF (reault a)*
                Calculates all data needed to perform FN-algorithm CF (reverse Pearson - tranform ratings) (result b)
                Compare result a to result b
                Dynamic arrays, during execution to save memory space
                Reads all Movielens dataset (over10_movielens_simple.rar)
                Makes CF Predictions based only on positive Pearson.
                Calculates all data needed to perform FN-algorithm CF (reverse Pearson - negative values) (result c)
                Compare result b to result c, Compare result b to result c, Compare results a, b, c
                Combine results to propose new algorithm
                Exclude values from FN calculations, excluding 3-ratings
                FN calculations, assigning weights to ratings
                Estimate NN and FN based only on K most recent ratings/per user.
                Prints stats
                Partial Separation Logic/Implementation
                Move some methods to sepa  rate files (Phd_Utils).
                New Average Method for FN (inverted similarity)
                Combined FN/NN
                Improving ability to handle large data, through the use of hastables
                One main class, for all data files. 
               Time specific calculations, for improving time execution (see Classes for observations)                
                PARALLEL COMPUTATIONS (working area: Find the similar users)

Next Version:   Refine simulations for more elaborate results
                Produce more stats concerning data

ASSUMPTIONS     All files are pre-proccessed so as userid and movieid having increasing values (by 1)
                starting with value 0.
                
BASE ON         The new proposed algorithm is compared against algorithms presented in paper:
                "Pruning and Aging for User Histories in Collaborative Filtering", D.Margaris, C.Vassilakis
*/

/*

THIS FILE COMPUTES KEEP-N

*/

package phd;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
//import UserMovie;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;


/**
 *
 * @author Administrator
 */
public class Phd_Hub_Hashtable_Parallel extends Global_Vars{
        
//Movielens 1M_Old
static int MAX_USERS;              //Maximum Users the program can handle
static int MAX_MOVIES;       //Maximum Movies the program can handle
//static final int TOTAL_RATINGS=100000;

static User[] users;                                              //Store User Details (see class declaration)
static HashMap<CellCoor,UserMovie> userMovies;                  //Store User Ratings
static HashSet<Integer>[] usersRatingSet;                         //Array Set containg for each user the Movies that has rated


public static void Assign_Values(double[] values, int choice) {

switch(choice) {
       case 1: simNeighbors=(int)values[0]; positivePredictions=(int)values[1];MAE=values[2];break;
       case 2: revSimNeighbors=(int)values[1];revPredictedValues=(int)values[4];RevMAE=values[7];break;
       case 3: NO3RevSimNeighbors=(int)values[2];NO3RevPredictedValues=(int)values[5]; NO3RevMAE=values[8];break;
       case 4: negAverSimNeighbors=(int)values[0];negAverPredictedValues=(int)values[1]; negAverMAE=values[2];break;       
       case 5: combinedNeighbors=(int)values[0]; combinedPredictions=(int)values[1];combinedMAE=values[2];break;
} //switch 

}// Assign_Values

public static void Thread_Similarities(
int option,
int totalUsers,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie> userMovies,
HashSet<Integer>[] usersRatingSet,
int similaritySign,
double simBase,
int commonMovies)
{
int i;
Parallel_Sim PS1, PS2;
Thread t1, t2;
Thread[] threadPool = new Thread[THREADS];
Parallel_Sim[] PS = new Parallel_Sim[THREADS];
int lowbound=0;
int upperbound=-1;

//A. Similarities have to be synchronized, as THREAD functions may overlap
long firstTime=System.currentTimeMillis();
for (i=0;i<=totalUsers;i++)
    userSim[i]=Collections.synchronizedList(new ArrayList<>());
System.out.println("Synchr:"+Long.toString(firstTime-System.currentTimeMillis()));
// THREAD SECTION: SPLIT SIMILARITY COMPUTATIONS TO SEVERAL THREADS

//B.Split Job to THREADS. Find low and upper bounds to assign task
//  Create a threadpool of THREAD items

for (i=0;i<=THREADS-1;i++)
{
    lowbound=upperbound+1;upperbound=(int)((i+1)*totalUsers/THREADS);
    PS[i]= new Parallel_Sim(option,lowbound, upperbound, totalUsers, userSim, users, userMovies, usersRatingSet, similaritySign, simBase,commonMovies);
    threadPool[i]= new Thread(PS[i],"t"+String.valueOf(i));
}

//usersRatingSet = new HashSet[MAX_USERS];
//userMovies = new Hashtable(134999); 

//  Start all threads
for (i=0;i<=THREADS-1;i++)
{
    threadPool[i].start();
}

//  Wait all threads to come to an end
try {
    
    System.out.println("Waiting for threads to finish.");
    for (i=0;i<=THREADS-1;i++)
    {
        threadPool[i].join();
    }
}
catch (InterruptedException e) {
    System.out.println("Main thread Interrupted");
}

System.out.println("ALL FINISHED");

} //Thread_Similarities


public static void Print_to_File(int choice){

if (choice==1) 
{
    
}
else
{
   
}

}// Methid Print_to_File

public static void main(String[] args) {

//    
// VARIABLES DEFINITIONS
//

System.out.println("Variables are being initialized.... Wait..." );
        

UserSimilarity[][] User_Similarities = new UserSimilarity [MAX_USERS][MAX_USERS];
double Similarity=0, KF_NO3_Similarity=0, MaxSimValue=0, MinSimValue=0;
        
List<UserSimilarity> UserList = new ArrayList<>();
List<UserSimilarity>[] US;    //Array of list holding for each user the NN
List<UserSimilarity>[] RUS;   //Array of list holding for each user the FN
List<UserSimilarity>[] NO3RUS;    //Array of list holding for each user the FN
List<UserSimilarity>[] INVUS;  
List<UserSimilarity>[] COMBINE;  


//These 2 vars can be estimated in advanced through real data and not through the code.
int totalUsers;                                  //The number of users          
int totalMovies;                                 //The number of unique movies in DB

int newTotal,newrevtotal,no3newrevtotal;
double Numerator_Sim_ij, Denominator_Sim_ij;        //Numerator and Denominator of Similarity (Pearson) function.
double KF_NO3_Numerator_Sim_ij, KF_NO3_Denominator_Sim_ij;

double KF_NO3_Denominator_Part_A,KF_NO3_Denominator_Part_B;
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.

int TotalPredictedValues;                              //The total number of actually predicted values
int NO3TotalPredictedValues;
        
int temp_prediction;                                //values holding current (rev)predictions
int temp_rev_prediction;
int temp_no3_rev_prediction;

int lowbound, upperbound;
// Time stamps to calculate execution times
long firstTime, totalTime, startTime, initTime, simTime1, simTime2, simTime3, simTime4, sortTime, strictTime, predTime1, predTime2, predTime3, predTime4, predTime5;
        
int i,j,k, l, m, n, o, p, q;
int RevMode=0;
int aa=0; 
int[] totals = new int[2];

int datasetSelection;
String datasetFile = new String();
String outFileResults = new String();
String outFileTiming = new String();

// PART A. INITIALISATION 

//
// Initialize Main Variables
//

datasetSelection=1;
switch (datasetSelection) {
    case 1: datasetFile="/home/denis/Documents/Datasets/01.Movielens_100k_old/ratings_Movielens_100K_OLD_Sorted_Pure.txt";
            MAX_USERS= 945; 
            users = new User[MAX_USERS];
            usersRatingSet = new HashSet[MAX_USERS];
            userMovies = new HashMap(134999);    //Realsize/0.75 for good performance
                                                   //HAS to BE a PRIME or odd.I use 134999.
            outFileResults="src/phd/Results/Results_Movielens_100K_Old_Hash_Parallel.txt"; 
            outFileTiming ="src/phd/Timings/Timing_Movielens_100K_Old_Hash_Parallel.txt"; 
            break;
    case 2: datasetFile="/home/denis/Documents/Datasets/02.Movielens_1M_Old/ratings_MovieLens_1M_Old.txt";
            MAX_USERS= 6045; 
            users = new User[MAX_USERS];
            usersRatingSet = new HashSet[MAX_USERS];
            userMovies = new HashMap(1335991);    //Realsize/0.75 for good performance
                                                    //HAS to BE a PRIME or odd.I use 1335991.
            outFileResults="src/phd/Results/Results_Movielens_1M_Old_Hash_Parallel.txt"; 
            outFileTiming ="src/phd/Timings/Timing_Movielens_1M_Old_Hash_Parallel.txt"; 
            //You have to set Heapsize to at least 4096MB (-Xms4096m)
            break;
    case 3: datasetFile="/home/denis/Documents/Datasets/03.Amazon_Video_Games/ratings_Video_Games_Final.tab";
            MAX_USERS= 8060; 
            users = new User[MAX_USERS];
            usersRatingSet = new HashSet[MAX_USERS];
            userMovies = new HashMap(210011);    //Realsize/0.75 for good performance
                                                    //HAS to BE a PRIME or odd.I use 1335991.
            outFileResults="src/phd/Results/Results_Amazon_VG_Hash_Par.txt"; 
            outFileTiming ="src/phd/Timings/Timing_Amazon_VG_Hash_Par.txt"; 
            //You have to set Heapsize to at least 4096MB (-Xms8000m) in order to increase speed
            break;
    case 4: datasetFile="/home/denis/Documents/Datasets/01.Movielens_100k_old/ratings_Movielens_100K_OLD_Sorted.txt";break;
} //switch

System.out.println("Variables initialization finished. Program execution started..." );
System.out.println("Data File reading started..." );

//
// -------- Start reading data file. All data are in memory (Tables) ----------- 
//
// Store all ratings in memory                          
// in two tables: a)User_Ratings b)User_Ratings_Summary 
// Also returns two values: totalUsers and totalMovies 
// Afterwards Inverse Data (for FN) are computed

firstTime=System.currentTimeMillis();
startTime=System.currentTimeMillis();
totals=Initialization.Data_Initialisation_General(datasetFile, users, userMovies, usersRatingSet, absMinTimeStamp, absMaxTimeStamp);
initTime=startTime-System.currentTimeMillis();  //Estimate Initialization Time
System.out.println("Size after initialization:"+userMovies.size());

totalUsers=totals[0];totalMovies=totals[1];     
//Phd_Utils.Print_Ratings(totalUsers, totalMovies, users, userMovies);

Initialization.Compute_Inverse(totalUsers, totalMovies, users, userMovies);
System.out.println("Users from 0 to:"+totalUsers+", Movies from 0 to:"+totalMovies); 
//Phd_Utils.Print_Ratings(totalUsers, totalMovies, users, userMovies);
//Phd_Utils.Print_UserItems(totalUsers, users, usersRatingSet);
// -------- End reading data file. All data are in memory (Tables) ----------- 

System.out.println("Data File reading finished..." );
        
        
//PART B. MAIN PART I. COMPUTE SIMILARITIES - PART II.MAKE PREDICTIONS
//
//EXPORT RESULTS TO TAB SEPARATED FILE
//
//            CALCULATE SIMPLE COLLABORATIVE FILTERING SIMILARITIES FOR BOTH NNs and KNs

        
try(FileWriter outExcel = new FileWriter( outFileResults )) {

    //Export File HEADINGS
    
    outExcel.write("AA\tSimilarity"+"\tRevSimilarity"+"\tNO3RevSimilarity"+"\tMin Common Movies"+"\tFirst Best Neighs");
    outExcel.write("\tNN Predictions"+"\tNN Coverage"+"\tNN MAE Sum"+"\tNN MAE CF");
    outExcel.write("\tFN Predictions"+"\tFN Coverage"+"\tFN MAE Sum"+"\tFN MAE CF");
    outExcel.write("\tNO3 FN Predictions"+"\tNO3 FN Coverage"+"\tNO3 FN MAE Sum"+"\tNO3 Rev MAE CF");
    outExcel.write("\tDenFN Predictions"+"\tDenFN Coverage"+"\tDenFN MAE Sum"+"\tDenRev MAE CF");    
    outExcel.write("\tCombined NN FN Predictions"+"\tNN FN Coverage"+"\tNN FN MAE Sum"+"\tNN FN MAE CF");
    outExcel.write("\r\n");  
    
    //Print_to_File(outExcel,1);            
    
    try(FileWriter out = new FileWriter( outFileTiming ))            //Open file for writing
    {

        //All parameters used fot the simulation process
        
        //for (q=MIN_MOST_RECENT_RATINGS;q<=MIN_MOST_RECENT_RATINGS;q+=10)
        for (p=DOWN_BEST_NEIGH;p<=UPPER_BEST_NEIGH;p+=10)
        for (n=MIN_COMMON_MOVIES;n<=MAX_COMMON_MOVIES;n+=10)
 //       for (o=MIN_SIMILAR_NEIGH;o<=MAX_SIMILAR_NEIGH;o+=10)  //OBSOLETE - NOT USED ANY MORE
        for (l=SIMILARITY_BASE_LIMIT;l<=SIMILARITY_UPPER_LIMIT;l+=20)
        for (m=NEGATIVE_SIMILARITY_BASE_LIMIT;m<=NEGATIVE_SIMILARITY_UPPER_LIMIT;m+=20)    
        {            
            
            //Compute SIMILARITIES
            
            
            System.out.println(" n:"+n+" l:"+l+" m:"+m+" Size:"+userMovies.size());

            simNeighbors=0; revSimNeighbors=0;NO3RevSimNeighbors=0;
            positivePredictions=0; revPredictedValues=0; NO3RevPredictedValues=0;    
            MAE=0.0;RevMAE=0.0;NO3RevMAE=0.0;

            TotalPredictedValues=0;NO3TotalPredictedValues=0;            
            NO3TotalMAE=0.0;TotalMAE=0.0;                                    
            US = new List[MAX_USERS];
            startTime=System.currentTimeMillis();           //Set new timer
            Thread_Similarities(1, totalUsers, US, users, userMovies, usersRatingSet, -1, (double)l/100,n);
            //Similarities.Positive_Similarity(totalUsers, totalMovies, US = new List[MAX_USERS], users, userMovies, usersRatingSet, (double)l/100, n); 
            simTime1=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Thread_Similarities(2, totalUsers, RUS = new List[MAX_USERS], users, userMovies, usersRatingSet, 0, (double)-m/100,n);
            //Similarities.Compute_Similarity(totalUsers, totalMovies, RUS = new List[MAX_USERS], users, userMovies, usersRatingSet, 0, (double)-m/100, n);
            simTime2=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Thread_Similarities(2, totalUsers, NO3RUS = new List[MAX_USERS], users, userMovies, usersRatingSet, 2, (double)-m/100,n);
            //Similarities.Compute_Similarity(totalUsers, totalMovies, NO3RUS = new List[MAX_USERS], users, userMovies, usersRatingSet, 2, (double)-m/100, n);
            simTime3=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Thread_Similarities(3, totalUsers, INVUS = new List[MAX_USERS], users, userMovies, usersRatingSet, -1, (double)m/100,n);
            //Similarities.Inverted_Similarity(totalUsers, totalMovies, INVUS = new List[MAX_USERS], users, userMovies, usersRatingSet, (double)m/100, n);
            simTime4=startTime-System.currentTimeMillis();
            //System.out.println("aaa");
            //Similarities.Print_Similarities(totalUsers, US);

            //For each User there is a sorted array with all its NN/FN calculated
            startTime=System.currentTimeMillis();           //Set new timer
            for (i=0;i<=totalUsers;i++)
            {
                Collections.sort(US[i],Collections.reverseOrder());
                Collections.sort(RUS[i]);
                Collections.sort(NO3RUS[i]);
                Collections.sort(INVUS[i],Collections.reverseOrder());
            }
            //System.out.println("bbb");
            //Similarities.Print_Similarities(totalUsers, INVUS);
            //Similarities.Print_Similarities(totalUsers, US);
            sortTime=startTime-System.currentTimeMillis();

            //Keep only Neighbors that have rate LastMovieID
            startTime=System.currentTimeMillis();
            Phd_Utils.Strict_Similarities(totalUsers, US, users, userMovies);
            Phd_Utils.Strict_Similarities(totalUsers, RUS, users, userMovies);
            Phd_Utils.Strict_Similarities(totalUsers, NO3RUS, users, userMovies);
            Phd_Utils.Strict_Similarities(totalUsers, INVUS, users, userMovies);     
            strictTime=startTime-System.currentTimeMillis();
            //System.out.println("ccc");
            //Similarities.Print_Similarities(totalUsers, INVUS);
            //Similarities.Print_Similarities(totalUsers, US);
            /* 
                CALCULATE USER'S PREDICTION FOR LAST MOVIE FROM NN
            */


            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Positive_Prediction(totalUsers, totalMovies, US, users, userMovies, p),1);
            predTime1=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings 

            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Compute_Prediction(totalUsers, totalMovies, RUS, users, userMovies, 1, p),2);            
            predTime2=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings         

            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Compute_Prediction(totalUsers, totalMovies, NO3RUS, users, userMovies, 2, p),3);                 
            predTime3=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings         

            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Inverted_Prediction(totalUsers, totalMovies, INVUS, users, userMovies, p),4);     
            System.out.println(negAverMAE+" "+negAverPredictedValues);            
            predTime4=startTime-System.currentTimeMillis();    
        
            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Combined_Prediction(totalUsers, totalMovies, US, INVUS, COMBINE = new List[MAX_USERS], users, userMovies, p),5);
            predTime5=startTime-System.currentTimeMillis();                          //Time for the calculation of Predicted ratings 

            totalTime=firstTime-System.currentTimeMillis(); 
            
        //Testing the process so far 
            aa++;    

            outExcel.write(aa+"\t"+(double)l/100+"\t"+(double)-m/100+"\t"+(double)-m/100+"\t"+n+"\t"+p);
            outExcel.write("\t"+positivePredictions+"\t"+(double)positivePredictions/(totalUsers+1)+"\t"+MAE+"\t"+(double)(MAE/positivePredictions));
            outExcel.write("\t"+revPredictedValues+"\t"+(double)revPredictedValues/(totalUsers+1)+"\t"+RevMAE+"\t"+(double)(RevMAE/revPredictedValues));
            outExcel.write("\t"+NO3RevPredictedValues+"\t"+(double)NO3RevPredictedValues/(totalUsers+1)+"\t"+NO3RevMAE+"\t"+(double)(NO3RevMAE/NO3RevPredictedValues));
            outExcel.write("\t"+negAverPredictedValues+"\t"+(double)negAverPredictedValues/(totalUsers+1)+"\t"+negAverMAE+"\t"+(double)(negAverMAE/negAverPredictedValues));            
            outExcel.write("\t"+combinedPredictions+"\t"+(double)combinedPredictions/(totalUsers+1)+"\t"+combinedMAE+"\t"+(double)(combinedMAE/combinedPredictions));
            outExcel.write("\r\n"); 

            //  Print Statistics

            out.write("Max Similarity Value: "+MaxSimValue+" Min Similarity Value:"+MinSimValue);
            out.write("\r\n");            
            out.write("\r\n");                        
            out.write("Initialization time (Read File, Fill in User, UserMovies tables): "+Long.toString(initTime));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (NN): "+Long.toString(simTime1));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (FN): "+Long.toString(simTime2));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (NO3 FN): "+Long.toString(simTime3));
            out.write("\r\n");
            out.write("Calculate time to find Similarities (Dennis FN): "+Long.toString(simTime4));
            out.write("\r\n");
            out.write("Sort Similarity arrays for all users: "+Long.toString(sortTime));
            out.write("\r\n");
            out.write("Strict Similarities Computational Time: "+Long.toString(strictTime));
            out.write("\r\n");
            out.write("Calculate time to make Predictions (NN): "+predTime1);
            out.write("\r\n");
            out.write("Calculate time to make Predictions (FN): "+predTime2);
            out.write("\r\n");
            out.write("Calculate time to make Predictions (NO3 FN): "+predTime3);
            out.write("\r\n");
            out.write("Calculate time to make Predictions (FN Dennis): "+predTime4);
            out.write("\r\n");
            out.write("Calculate time to make Predictions (Combined NN - FN Dennis): "+predTime5);
            out.write("\r\n");
            out.write("Total Time: "+totalTime);
            out.write("\r\n");            
            out.write("********************************************************\r\n");
            out.write("********************************************************\r\n");            
            out.write("\r\n");
        }    
            out.close();     //Close output file
            
        } //try    //try   
        catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        } //catch
            outExcel.close();
        }
        catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        } //catch
        
        System.out.println("World ended !" );    

        //System.out.println("dd");        
        //The following are working examples!
        //Similarities.Print_Similarities(totalUsers, US);
        //Similarities.Print_Similarities(totalUsers, INVUS);
        //Similarities.Print_Similarities(totalUsers, COMBINE);
        //Phd_Utils.Print_UserRatings(totalUsers, totalMovies, users, userMovies);
    } //Main
    
} //Class Phd
