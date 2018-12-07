/*
Version 1.2
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

Next Version:   Refine simulations for more elaborate results
                Testing algorithms on other than MovieLens 100K simple Data set. 
                Introduce Parallel programming (multithreading)
                Time specific calculations, for improving time execution
                Produce more stats concerning data
                Version 2, has to be more specific in terms of variables values during simulation. Final decisions have to be taken.

ASSUMPTIONS     A:The USER IDs in the text file, starts from 0, and are increasing by step 1.
                  e.g. The 1st user has ID=1, the 2nd user has ID=2.
                B:The MOVIES IDs are like A. Each new movie is assigned a new ID, increasing by step 1.
                
BASE ON         The new proposed algorithm is compared against algorithms presented in paper:
                "Pruning and Aging for User Histories in Collaborative Filtering", D.Margaris, C.Vassilakis
*/

/*

THIS FILE COMPUTES KEEP-N

*/

//2147483647
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
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashSet;


/**
 *
 * @author Administrator
 */
public class FN_100K_OLD extends Global_Vars{
        

static final int MAX_USERS=950;        //Maximum Users the program can handle
static final int MAX_MOVIES=1690;      //Maximum Movies the program can handle
static final int TOTAL_RATINGS=100001;

/*
static final int NO_PREDICTION=-10;    //"Prediction value" for items that cannot be predicted
static final int MAX_RATING=5;

static final int SIMILARITY_BASE_LIMIT=0;   //Compute Similarity: Greater/Lesser or equal than .. (>= or <=)
static final int SIMILARITY_UPPER_LIMIT=100;
static final int NEGATIVE_SIMILARITY_BASE_LIMIT=0;         //FOR SIMPLE CALC BEST 20
static final int NEGATIVE_SIMILARITY_UPPER_LIMIT=100;
//static final int NO3_NEGATIVE_SIMILARITY_BASE_LIMIT=0;
//static final int NO3_NEGATIVE_SIMILARITY_UPPER_LIMIT=0;   

static final int MIN_COMMON_MOVIES=0;       //Compute Similarity: Greater than .. (>)
static final int MAX_COMMON_MOVIES=0;

/*
//Compute Prediction: >
//Number of  Similar Neighbors, sorted by similarity (desc) (Min and Max Limits)
static final int MIN_SIMILAR_NEIGH=0;      
static final int MAX_SIMILAR_NEIGH=0;

NOW IT HAS BEEN SUPERSEDED BY BEST_NEIGH. IN ALL PREDICTION FUNCTIONS IT IS SET TO 0;
*/
/*
//Compute_Prediction:  Down and Upper Limit of  Neighbors that have rated lastmovieID, sorted by similarity (desc)
//This limit defines, in any case, the maximum number of best neighbors that are taken into consideration
//If STRICT_SIMILARITY=0 it MUST have a high value >1500. If STRICT_SIMILARITY=1 it may have the normal value explained above.
static final int DOWN_BEST_NEIGH=150;         //HAS TO BE GREATER THAN ZERO (0). IT IS SET TO 150 TO INCLUDE ALL NEIGHBORS
static final int UPPER_BEST_NEIGH=150; 

//Initialization: <MAX_MOST_RECENT_RATINGS
//Min and Max number of each user's ratings.
//70% of users having < 105 (Movielens 100k simple)
//When there is no need to exclude any user based on the number of his ratings, the numbes have to be hiiiiighhh >1500.
//NOW ALL CALCULATIONS ARE BASED on MAX_MOST_RECENT_RATINGS
static final int MIN_MOST_RECENT_RATINGS=150;          
static final int MAX_MOST_RECENT_RATINGS=150;          

// First and last rating must have a minimum time distance. 4 sec are the minimum.
//Used inQ COMPUTE SIMILARITY: Greater than .. (>)
static final int MIN_TIMESPACE=4;           

//General Weight Types.
//There are the following type of weights (used in computing similarity)
//0=NO Weight, 1=Time Weight per user, 2=Time weight per whole population (OBSOLETE AFTER Ver 1.3. - Code Fixed)
//THIS WEIGHT AFFECTS BOTH POSITIVE AND NEGATIVE SIMILARITIES
static final int WEIGHT_TYPE=1;     

//0=as WEIGHT_TYPE=1, but for Negative Similarity
//1=NOT APPLIED ANY MORE
static final int NEG_WEIGHT_TYPE=0; ////There are the following types of weights affecting ONLY NEGATIVE SIMILARITIES

//Flag to Keep Only Neighbors that have rated last movie ID. Obsolete after Ver1.3 - Code Fixed. HAS TO BE DELETED IN NEXT VESRION.
static final int STRICT_SIMILARITY=1; //1=Only similar neighbors that rated last movie 
                                      //0-All similar neighbors
*/
static User[] users = new User[MAX_USERS];        
static UserMovie[][] userMovies = new UserMovie[MAX_USERS][MAX_MOVIES];  //Store User Ratings
static HashSet<Integer>[] usersRatingSet = new HashSet[MAX_USERS]; //Array Set containg for each user the Movies that has rated
/*
static public int simNeighbors=0, revSimNeighbors=0, NO3RevSimNeighbors=0,
                  negAverSimNeighbors;       //The Number of user having similar/reverse similar users                
static int positivePredictions, revPredictedValues, NO3RevPredictedValues,
           negAverPredictedValues;    //The total number of actually predicted values
static int combinedPredictions, combinedNeighbors;
static double MAE=0.0, RevMAE=0.0, NO3RevMAE=0.0;                         //Mean Absolute Error of Prediction.
static double TotalMAE=0.0, NO3TotalMAE=0.0, combinedMAE=0.0, negAverMAE;              //Mean Absolute Error of Combined Prediction.
static int absMinTimeStamp=Integer.MAX_VALUE, absMaxTimeStamp=Integer.MIN_VALUE;


*/



public static void Assign_Values(double[] values, int choice) {

switch(choice) {
       case 1: simNeighbors=(int)values[0]; positivePredictions=(int)values[1];MAE=values[2];break;
       case 2: revSimNeighbors=(int)values[1];revPredictedValues=(int)values[4];RevMAE=values[7];break;
       case 3: NO3RevSimNeighbors=(int)values[2];NO3RevPredictedValues=(int)values[5]; NO3RevMAE=values[8];break;
       case 4: negAverSimNeighbors=(int)values[0];negAverPredictedValues=(int)values[1]; negAverMAE=values[2];break;       
       case 5: combinedNeighbors=(int)values[0]; combinedPredictions=(int)values[1];combinedMAE=values[2];break;
} //switch 

}// Assign_Values

public static void Print_to_File(int choice){

if (choice==1) 
{
    
}
else
{
   
}

}// Method Print_to_File

public static void main(String[] args) {
        
System.out.println("Hello World !" );
        

UserSimilarity[][] User_Similarities = new UserSimilarity [MAX_USERS][MAX_USERS];
double Similarity=0, KF_NO3_Similarity=0, MaxSimValue=0, MinSimValue=0;
        
List<UserSimilarity> UserList = new ArrayList<>();
List<UserSimilarity>[] US = new List[MAX_USERS];    //Array of list holding for each user the NN
List<UserSimilarity>[] RUS = new List[MAX_USERS];   //Array of list holding for each user the FN
List<UserSimilarity>[] NO3RUS = new List[MAX_USERS];    //Array of list holding for each user the FN
List<UserSimilarity>[] INVUS = new List[MAX_USERS];  
List<UserSimilarity>[] COMBINE = new List[MAX_USERS];  
       
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
        
long firstTime, totalTime, startTime, initTime, simTime1, simTime2, simTime3, simTime4, sortTime, strictTime, predTime1, predTime2, predTime3, predTime4, predTime5;
        
int i,j,k, l, m, n, o, p, q;
int RevMode=0;
int aa=0; 
int[] totals = new int[2];

ExecutorService es = Executors.newFixedThreadPool(2);

// PART A. INITIALISATION 
//
// -------- Start reading data file. All data are in memory (Tables) ----------- 
//
// Store all ratings in memory                          
// in two tables: a)User_Ratings b)User_Ratings_Summary 
// Also returns two values: totalUsers and totalMovies 
// Afterwards Inverse Data (for FN) are computed

firstTime=System.currentTimeMillis();
startTime=System.currentTimeMillis();
totals=Initialization.Data_Initialisation_100K_OLD("Movielens_100K_OLD_Sorted.txt", users, userMovies, usersRatingSet, absMinTimeStamp, absMaxTimeStamp);
initTime=startTime-System.currentTimeMillis();  //Estimate Initialization Time
totalUsers=totals[0];totalMovies=totals[1];
Initialization.Compute_Inverse_Data(totalUsers, totalMovies, users, userMovies);
System.out.println("Real numbers totalUsers:"+(totalUsers+1)+"totalMovies:"+(totalMovies+1)); 

// -------- End reading data file. All data are in memory (Tables) ----------- 

        
        
//PART B. MAIN PART I. COMPUTE SIMILARITIES - PART II.MAKE PREDICTIONS
//
//EXPORT RESULTS TO TAB SEPARATED FILE
//
//            CALCULATE SIMPLE COLLABORATIVE FILTERING SIMILARITIES FOR BOTH NNs and KNs

        
try(FileWriter outExcel = new FileWriter( "results_MovieLens100K_Old.txt" )) {

    //Export File HEADINGS
    
    outExcel.write("AA\tSimilarity"+"\tRevSimilarity"+"\tNO3RevSimilarity"+"\tMin Common Movies"+"\tFirst Best Neighs");
    outExcel.write("\tNN Predictions"+"\tNN Coverage"+"\tNN MAE Sum"+"\tNN MAE CF");
    outExcel.write("\tFN Predictions"+"\tFN Coverage"+"\tFN MAE Sum"+"\tFN MAE CF");
    outExcel.write("\tNO3 FN Predictions"+"\tNO3 FN Coverage"+"\tNO3 FN MAE Sum"+"\tNO3 Rev MAE CF");
    outExcel.write("\tDenFN Predictions"+"\tDenFN Coverage"+"\tDenFN MAE Sum"+"\tDenRev MAE CF");    
    outExcel.write("\tCombined NN FN Predictions"+"\tNN FN Coverage"+"\tNN FN MAE Sum"+"\tNN FN MAE CF");
    outExcel.write("\r\n");  
    
    //Print_to_File(outExcel,1);            
    
    try(FileWriter out = new FileWriter( "Time_MovieLens100K_Old.txt" ))            //Open file for writing
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
            
            
            System.out.println(" n:"+n+" l:"+l+" m:"+m);

            simNeighbors=0; revSimNeighbors=0;NO3RevSimNeighbors=0;
            positivePredictions=0; revPredictedValues=0; NO3RevPredictedValues=0;    
            MAE=0.0;RevMAE=0.0;NO3RevMAE=0.0;

            TotalPredictedValues=0;NO3TotalPredictedValues=0;            
            NO3TotalMAE=0.0;TotalMAE=0.0;                                    

            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Positive_Similarity(totalUsers, totalMovies, US, users, userMovies, usersRatingSet, (double)l/100, n); 
            simTime1=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Compute_Similarity(totalUsers, totalMovies, RUS, users, userMovies, usersRatingSet, 0, (double)-m/100, n);
            simTime2=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Compute_Similarity(totalUsers, totalMovies, NO3RUS, users, userMovies, usersRatingSet, 2, (double)-m/100, n);
            simTime3=startTime-System.currentTimeMillis();
            startTime=System.currentTimeMillis();           //Set new timer
            Similarities.Inverted_Similarity(totalUsers, totalMovies, INVUS, users, userMovies, usersRatingSet, (double)m/100, n, absMinTimeStamp, absMaxTimeStamp);
            simTime4=startTime-System.currentTimeMillis();

            //System.out.println("aaa");
            //Similarities.Print_Similarities(totalUsers, INVUS);
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
            
            startTime=System.currentTimeMillis();
            //Keep only Neighbors that have rate LastMovieID
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
            //System.out.println(negAverMAE+" "+negAverPredictedValues);            
            predTime4=startTime-System.currentTimeMillis();    
        
            startTime=System.currentTimeMillis();                    //New Timer
            Assign_Values(Predictions.Combined_Prediction(totalUsers, totalMovies, US, INVUS, COMBINE, users, userMovies, p),5);
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
            
        } //try  
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
