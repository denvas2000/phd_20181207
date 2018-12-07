/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

/**
 *
 * @author Administrator
 */
public class Global_Vars {

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
static final int MIN_MOST_RECENT_RATINGS=1500;          
static final int MAX_MOST_RECENT_RATINGS=1500;          

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


static public int simNeighbors=0, revSimNeighbors=0, NO3RevSimNeighbors=0,
                  negAverSimNeighbors;       //The Number of user having similar/reverse similar users                
static int positivePredictions, revPredictedValues, NO3RevPredictedValues,
           negAverPredictedValues;    //The total number of actually predicted values
static int combinedPredictions, combinedNeighbors;
static double MAE=0.0, RevMAE=0.0, NO3RevMAE=0.0;                         //Mean Absolute Error of Prediction.
static double TotalMAE=0.0, NO3TotalMAE=0.0, combinedMAE=0.0, negAverMAE;              //Mean Absolute Error of Combined Prediction.
static int absMinTimeStamp=Integer.MAX_VALUE, absMaxTimeStamp=Integer.MIN_VALUE;

    
}
