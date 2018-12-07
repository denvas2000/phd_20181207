/*
 * Class contains 3 methods
 * Data_Initializaton: Reads Data File and Stores Results to arrays for manimulation.
 * Compute_Inverse_Data: Computes Data for researching Far Neighbours
 * New_Rating: The inverted value 
 */
package phd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import phd.Global_Vars;


/**
 *
 * @author Administrator
 */
public class Initialization {

public static int New_Rating (int tempRating, double diff) {
int newRating;

newRating=tempRating;

if (Math.abs(diff)<=0.5)    
    switch(tempRating) {
    case 1:newRating=5;break;
    case 2:newRating=4;break;
    case 3:newRating=3;break;
    case 4:newRating=2;break;
    case 5:newRating=1;break;
}
else
if (diff<-0.5)    
    switch(tempRating) {
    case 1:newRating=4;break;
    case 2:newRating=3;break;
    case 3:newRating=2;break; 
    case 4:newRating=1;break;
    case 5:newRating=1;break;//This is the catch
}
else
if (diff>0.5)    
    switch(tempRating) {
    case 1:newRating=5;break;//This is the catch
    case 2:newRating=5;break;
    case 3:newRating=4;break; 
    case 4:newRating=3;break;
    case 5:newRating=2;break;
}    
    
return newRating;    
}
    
public static void Compute_Inverse_Data(
int totalUsers, 
int totalMovies,
User[] users, 
UserMovie[][] userMovies) {

int i, j;
int tempRating;
int tempInvRating;
int tempSum;
double tempAverage;      
double diff;

System.out.println("totaluser:"+totalUsers);
System.out.println("totalmovies:"+totalMovies);
for (i=0;i<=totalUsers;i++)
{
    //System.out.println("i"+i+" average"+users[i].UserAverageRate());
    //System.out.println("i"+i);
    tempAverage=users[i].UserAverageRate();
    tempSum=0;
    
    for (j=0;j<=totalMovies;j++)
    {
        
        if (!(userMovies[i][j]==null))
        {
    
            tempRating=userMovies[i][j].getRating();
            diff=tempAverage-3;
            tempInvRating=New_Rating(tempRating,diff);
            tempSum+=tempInvRating;
            userMovies[i][j].invRating=tempInvRating;
        }
    } //for j
    
    users[i].invRatingSum=tempSum;
    
} //for i

} //END of class Compute_Inverse_Data

/* ***********************
METHOD: Data_Initialisation_1M_OLD
The only difference with method Data_Initialisation_100K_OLD is that the datafile lacks any header row per user. So the end 
of ratings for each user has to be estimated in a different way.
   *********************** */

public static int[] Data_Initialisation_1M_OLD(
String dataFile, 
User[] users, 
UserMovie[][] userMovies, 
HashSet<Integer>[] usersRatingSet, 
int absMinTimeStamp, 
int absMaxTimeStamp)
{

String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count=0;
int Nums_Line[] = new int[4]; //The numeric values of each line of the file (min=3, max=4)

//Vars read directly from the rating file

int UserID=0;         //UserID
int MovieID=0;        //MovieID
int UserRating=0;     //UserID Rating for MovieID
int RatingTimeStamp=0; //When UserID rates MovieID
int RatingsNum=0;      //The number of UserID ratings
int RatingsSum=0;      //The sum of all UserID ratings
int NO3_RatingsNum=0;  //The number of UserID ratings, excluding 3-ratings
int NO3_RatingsSum=0;  //The sum of all UserID ratings, excluding 3-ratings

HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated

//Vars for the data manipulation

//The user we deal with
//In 1M_Old first user has userID=1
int Running_User=1;             
int Previous_User=1;        

int User_Ratings_Sum=0;       //Sum of all rating values of a single UseiID
int Min_Time = 0;             //Min time stamp of a UseiID
int Max_Time = 0;             //Max time stamp of a UseiID
int maxRating=1;
int minRating=5;
int Last_Movie=0;             //The Movie ID the UserID last, concerning time, rated


int totalUsers=0;
int totalMovies=0;
        
try {   //Read Files. Initiate tables

    /* When a line has 3 nums it's (user, 0, num of user ratings)                           */
    /* Then a number of lines follow (equal to "num of user ratings") that have 4 nums      */
    /* It's user, items, rating, timestamp                                                  */

    //System.out.println(dataFile);
    
    BufferedReader Data_File = new BufferedReader(new FileReader(dataFile)); //Open text file to read        
                
        /* Read text file to the end */
        while((Line = Data_File.readLine()) != null)    //While there are still lines
        {
            
            /* ************************************ */
            /* START: READ A SINGLE LINE */
            
            Nums_Per_Line_Count=0;                      //Numbers per line
            Scan_Line = new Scanner(Line);              //Read Line from text file
            

            // Read the numbers in line, store them in Nums_Line, max numbers 4 (or the rest are ignored)
            while((Scan_Line.hasNext()) && (Nums_Per_Line_Count<4)) 
            {
                            //System.out.println("AAA");
                Nums_Line[Nums_Per_Line_Count++]=Scan_Line.nextInt();
                //System.out.println("BBB");
            }
            
            Scan_Line.close();
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
           
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */
            
            Running_User=Nums_Line[0];
            //System.out.println("Running:"+Running_User);
            if (Running_User!=Previous_User)        //Create user after all calculations are over
            {
                //Initialize new user
                users[Previous_User-1]= new User(Previous_User-1, Last_Movie-1, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
                usersRatingSet[Previous_User-1]= new HashSet<>();
                usersRatingSet[Previous_User-1].addAll(userRatingSet);
                RatingsNum=0;
                userRatingSet.clear();
             

                //Start a new user
                Previous_User=Running_User;             //The Current User we deal with
                
                //UserID=Nums_Line[0];                //ID of cuurent user. COULD BE EQUAL TO Running_User or vice versa
                
                //Initialize current user running data
                Min_Time=Integer.MAX_VALUE;
                Max_Time=Integer.MIN_VALUE;
                minRating=5;maxRating=1;
                RatingsSum=0;
                NO3_RatingsSum=0;NO3_RatingsNum=0;
                Last_Movie=0;                           //If movieID=0 exists then it has to be a negative value
                
            }
                                                // Read each user Data Line (user id, movie id 1, rating, time stamp
            if (RatingsNum<Global_Vars.MAX_MOST_RECENT_RATINGS)
            {                                       // Store values in User_Ratings[0=user_id, 1=movie_id, 2=rating, 3=time stamp)
                RatingsNum++;
                //Get data values
                MovieID=Nums_Line[1];
                UserRating=Nums_Line[2];
                RatingsSum += UserRating;           //New Sum of the ratings of UserID
                userRatingSet.add(MovieID);
                if (UserRating!=3) 
                {
                    NO3_RatingsSum+=UserRating;
                    NO3_RatingsNum++;
                }
                RatingTimeStamp=Nums_Line[3];
                
                if (totalMovies<MovieID) totalMovies=MovieID;   //Renew the total number of rated Movies, by all Users
                                                                  //IF MOVIESIDs WERE NOT JUST INCREMENTAL, IT HAD TO BE TREATED DIFFERENTLY
                
                //Running_Users_Rating++; //Next rating
                
                //Find min/max timestamp of u user
                if (Max_Time<RatingTimeStamp) 
                {
                    Max_Time=RatingTimeStamp;
                    Last_Movie=MovieID;                    

                }
                if (Min_Time>RatingTimeStamp) 
                {
                    Min_Time=RatingTimeStamp;

                }
                if (absMinTimeStamp>RatingTimeStamp) absMinTimeStamp=RatingTimeStamp;
                //if (absMinTimeStamp==0) {System.out.println("Running User:"+Running_User+" Movie:"+MovieID+" Time:"+RatingTimeStamp);}
                if (absMaxTimeStamp<RatingTimeStamp) absMaxTimeStamp=RatingTimeStamp;
                
                if (minRating>UserRating) minRating=UserRating;
                if (maxRating<UserRating) maxRating=UserRating;
                
                //Create the main Users*Movies Table. Each cell holds the rating of a user to a
                //specific movie.
                userMovies[Running_User-1][MovieID-1]=new UserMovie(Running_User-1,MovieID-1,UserRating, RatingTimeStamp,0);

            }//else if
            
        } // while (Line)
        
        if (Running_User!=1)        //Handle (Create) last user 
        {
            //Initialize new user
            users[Running_User-1]= new User(Running_User-1, Last_Movie-1, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
            usersRatingSet[Running_User]= new HashSet<>();
            usersRatingSet[Running_User].addAll(userRatingSet);                        
        } 
        
        totalUsers=Running_User-1;
        
        } // try 
        catch (IOException e) {

            e.printStackTrace();

	} //catch file error
        int[] mainStats={totalUsers, totalMovies-1};
        return mainStats;
        
} //method Data_Initialisation_1M_OLD


/* ***********************
METHOD: Data_Initialisation_100K_OLD
Users ratings are grouped per user. The first line indicates the number of ratings of the user (then timestamp=2147483647).
Using excel, the file is sorted per user, in descending time order (newest rating first, look the timestamp data field )
   *********************** */

public static int[] Data_Initialisation_100K_OLD(
String dataFile, 
User[] users, 
UserMovie[][] userMovies, 
HashSet<Integer>[] usersRatingSet, 
int absMinTimeStamp, 
int absMaxTimeStamp)
{

String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count=0;
int Nums_Line[] = new int[4]; //The numeric values of each line of the file (min=3, max=4)

//Vars read directly from the rating file

int UserID=0;         //UserID
int MovieID=0;        //MovieID
int UserRating=0;     //UserID Rating for MovieID
int RatingTimeStamp=0; //When UserID rates MovieID
int RatingsNum=0;      //The number of UserID ratings
int RatingsSum=0;      //The sum of all UserID ratings
int NO3_RatingsNum=0;  //The number of UserID ratings, excluding 3-ratings
int NO3_RatingsSum=0;  //The sum of all UserID ratings, excluding 3-ratings

HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated

//Vars for the data manipulation

//The user we deal with
//In 100K_Old first user has userID=0

int Running_User=0;             
int Next_User=0;

int User_Ratings_Sum=0;       //Sum of all rating values of a single UseiID
int Min_Time = 0;             //Min time stamp of a UseiID
int Max_Time = 0;             //Max time stamp of a UseiID
int maxRating=0;
int minRating=5;
int Last_Movie=0;             //The Movie ID the UserID last, concerning time, rated


int totalUsers=0;
int totalMovies=0;
        
try {   //Read Files. Initiate tables

    /* When a line has 3 nums it's (user, 0, num of user ratings)                           */
    /* Then a number of lines follow (equal to "num of user ratings") that have 4 nums      */
    /* It's user, items, rating, timestamp                                                  */

    //System.out.println(dataFile);
    
    BufferedReader Data_File = new BufferedReader(new FileReader(dataFile)); //Open text file to read        
                
        /* Read text file to the end */
        while((Line = Data_File.readLine()) != null)    //While there are still lines
        {
            
            /* ************************************ */
            /* START: READ A SINGLE LINE */
            
            Nums_Per_Line_Count=0;                      //Numbers per line
            Scan_Line = new Scanner(Line);              //Read Line from text file
            
            // Read the numbers in line, store them in Nums_Line, max numbers 4 (or the rest are ignored)
            while((Scan_Line.hasNext()) && (Nums_Per_Line_Count<4)) 
            {
                Nums_Line[Nums_Per_Line_Count++]=Scan_Line.nextInt();
            }
            
            Scan_Line.close();
            
            /* FINISH: READ A SINGLE LINE   */
            /* ************************************ */
           
            /* ************************************ */
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */
            
            
            if ((Nums_Line[3]==2147483647) || (Nums_Per_Line_Count==3))   // Read each user Header Line (user id, 0, number of ratings 
            {                                                             // Value 2147483647 in timestamp data field indicates Header Line

                if (Running_User!=Next_User)        //Create user after all calculations are over
                {
                    //Initialize new user
                    users[Running_User]= new User(Running_User, Last_Movie, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
                    usersRatingSet[Running_User]= new HashSet<>();
                    usersRatingSet[Running_User].addAll(userRatingSet);
                    RatingsNum=0;
                    userRatingSet.clear();
                } 

                //Start a new user
                
                Running_User=Next_User;             //The Current User we deal with
                
                UserID=Nums_Line[0];                //ID of cuurent user. COULD BE EQUAL TO Running_User or vice versa
                //RatingsNum=Nums_Line[2];            //The number of UserID ratings
                
                //Initialize current user running data
                Min_Time=Integer.MAX_VALUE;
                Max_Time=Integer.MIN_VALUE;
                minRating=5;maxRating=0;
                RatingsSum=0;
                NO3_RatingsSum=0;NO3_RatingsNum=0;
                Last_Movie=0;
                Next_User++;

            }
            else                                    // Read each user Data Line (user id, movie id 1, rating, time stamp
            if (RatingsNum<Global_Vars.MAX_MOST_RECENT_RATINGS) //At most the first MAX_MOST_RECENT_RATINGS
            {                                       // Store values in User_Ratings[0=user_id, 1=movie_id, 2=rating, 3=time stamp)
                RatingsNum++;
                //Get data values
                MovieID=Nums_Line[1];
                UserRating=Nums_Line[2];
                RatingsSum += UserRating;           //New Sum of the ratings of UserID
                userRatingSet.add(MovieID);
                if (UserRating!=3) 
                {
                    NO3_RatingsSum+=UserRating;
                    NO3_RatingsNum++;
                }
                RatingTimeStamp=Nums_Line[3];
                
                if (totalMovies<MovieID) totalMovies=MovieID;   //Renew the total number of rated Movies, by all Users
                                                                //IF MOVIESIDs WERE NOT JUST INCREMENTAL, IT HAD TO BE TREATED DIFFERENTLY
                
                //Find min/max timestamp of u user
                if (Max_Time<RatingTimeStamp) 
                {
                    Max_Time=RatingTimeStamp;
                    Last_Movie=MovieID;                    

                }
                if (Min_Time>RatingTimeStamp) 
                {
                    Min_Time=RatingTimeStamp;

                }
                if (absMinTimeStamp>RatingTimeStamp) absMinTimeStamp=RatingTimeStamp;
                //if (absMinTimeStamp==0) {System.out.println("Running User:"+Running_User+" Movie:"+MovieID+" Time:"+RatingTimeStamp);}
                if (absMaxTimeStamp<RatingTimeStamp) absMaxTimeStamp=RatingTimeStamp;
                
                if (minRating>UserRating) minRating=UserRating;
                if (maxRating<UserRating) maxRating=UserRating;
                
                //Create the main Users*Movies Table. Each cell holds the rating of a user to a
                //specific movie.
                userMovies[Running_User][MovieID]=new UserMovie(Running_User,MovieID,UserRating, RatingTimeStamp,0);

            }
            
        } // while (Line)
        
        if (Running_User!=Next_User)        //Handle (Create) last user 
        {
            //Initialize new user
            users[Running_User]= new User(Running_User, Last_Movie, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
            usersRatingSet[Running_User]= new HashSet<>();
            usersRatingSet[Running_User].addAll(userRatingSet);            
        } 
        
        totalUsers=Running_User;
        
        } // try 
        catch (IOException e) {

            e.printStackTrace();

	} //catch file error

        int[] mainStats={totalUsers, totalMovies};
        
        return mainStats;
        
} //Data_Initialisation_100K_OLD

public static int[] Data_Initialisation_Amazon_Video_Games(String dataFile, User[] users, UserMovie[][] userMovies, HashSet<Integer>[] usersRatingSet, int absMinTimeStamp, int absMaxTimeStamp)
{

String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count=0;
int Nums_Line[] = new int[4]; //The numeric values of each line of the file (min=3, max=4)

//Vars read directly from the rating file

int UserID=0;         //UserID
int MovieID=0;        //MovieID
int UserRating=0;     //UserID Rating for MovieID
int RatingTimeStamp=0; //When UserID rates MovieID
int RatingsNum=0;      //The number of UserID ratings
int RatingsSum=0;      //The sum of all UserID ratings
int NO3_RatingsNum=0;  //The number of UserID ratings, excluding 3-ratings
int NO3_RatingsSum=0;  //The sum of all UserID ratings, excluding 3-ratings

HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated

//Vars for the data manipulation

//The user we deal with
//In 1M_Old first user has userID=0
int Running_User=0;             
int Previous_User=0;        
int Last_Movie=0;             //The Movie ID the UserID last, concerning time, rated

int User_Ratings_Sum=0;       //Sum of all rating values of a single UseiID
int Min_Time = 0;             //Min time stamp of a UseiID
int Max_Time = 0;             //Max time stamp of a UseiID
int maxRating=1;
int minRating=5;

int totalUsers=0;
int totalMovies=0;
        
try {   //Read Files. Initiate tables

    /* When a line has 3 nums it's (user, 0, num of user ratings)                           */
    /* Then a number of lines follow (equal to "num of user ratings") that have 4 nums      */
    /* It's user, items, rating, timestamp                                                  */

    //System.out.println(dataFile);
    
    BufferedReader Data_File = new BufferedReader(new FileReader(dataFile)); //Open text file to read        
                
        /* Read text file to the end */
        while((Line = Data_File.readLine()) != null)    //While there are still lines
        {
            
            /* ************************************ */
            /* START: READ A SINGLE LINE */
            
            Nums_Per_Line_Count=0;                      //Numbers per line
            Scan_Line = new Scanner(Line);              //Read Line from text file
            

            // Read the numbers in line, store them in Nums_Line, max numbers 4 (or the rest are ignored)
            while((Scan_Line.hasNext()) && (Nums_Per_Line_Count<4)) 
            {
                            //System.out.println("AAA");
                Nums_Line[Nums_Per_Line_Count++]=Scan_Line.nextInt();
                //System.out.println("BBB");
            }
            
            Scan_Line.close();
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
           
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */
            
            Running_User=Nums_Line[0];
            //System.out.println("Running:"+Running_User);
            if (Running_User!=Previous_User)        //Create user after all calculations are over
            {
                //Initialize new user
                users[Previous_User]= new User(Previous_User, Last_Movie, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
                usersRatingSet[Previous_User]= new HashSet<>();
                usersRatingSet[Previous_User].addAll(userRatingSet);
                
                RatingsNum=0;
             

                //Start a new user
                Previous_User=Running_User;             //The Current User we deal with
                
                //UserID=Nums_Line[0];                //ID of cuurent user. COULD BE EQUAL TO Running_User or vice versa
                
                //Initialize current user running data
                Min_Time=Integer.MAX_VALUE;
                Max_Time=Integer.MIN_VALUE;
                minRating=5;maxRating=1;
                RatingsSum=0;
                NO3_RatingsSum=0;NO3_RatingsNum=0;
                Last_Movie=0;                           //If movieID=0 exists then it has to be a negative value
                userRatingSet.clear();
                
            }
                                                // Read each user Data Line (user id, movie id 1, rating, time stamp
            if (RatingsNum<Global_Vars.MAX_MOST_RECENT_RATINGS)
            {                                       // Store values in User_Ratings[0=user_id, 1=movie_id, 2=rating, 3=time stamp)
                RatingsNum++;
                //Get data values
                MovieID=Nums_Line[1];
                UserRating=Nums_Line[2];
                RatingsSum += UserRating;           //New Sum of the ratings of UserID
                userRatingSet.add(MovieID);
                if (UserRating!=3) 
                {
                    NO3_RatingsSum+=UserRating;
                    NO3_RatingsNum++;
                }
                RatingTimeStamp=Nums_Line[3];
                
                if (totalMovies<MovieID) totalMovies=MovieID;   //Renew the total number of rated Movies, by all Users
                                                                  //IF MOVIESIDs WERE NOT JUST INCREMENTAL, IT HAD TO BE TREATED DIFFERENTLY
                
                //Running_Users_Rating++; //Next rating
                
                //Find min/max timestamp of user
                if (Max_Time<RatingTimeStamp) 
                {
                    Max_Time=RatingTimeStamp;
                    Last_Movie=MovieID;                    

                }
                if (Min_Time>RatingTimeStamp) 
                {
                    Min_Time=RatingTimeStamp;

                }
                if (absMinTimeStamp>RatingTimeStamp) absMinTimeStamp=RatingTimeStamp;
                //if (absMinTimeStamp==0) {System.out.println("Running User:"+Running_User+" Movie:"+MovieID+" Time:"+RatingTimeStamp);}
                if (absMaxTimeStamp<RatingTimeStamp) absMaxTimeStamp=RatingTimeStamp;
                
                if (minRating>UserRating) minRating=UserRating;
                if (maxRating<UserRating) maxRating=UserRating;
                
                //Create the main Users*Movies Table. Each cell holds the rating of a user to a
                //specific movie.
                userMovies[Running_User][MovieID]=new UserMovie(Running_User-1,MovieID-1,UserRating, RatingTimeStamp,0);

            }//else if
            
        } // while (Line)
        
        if (Running_User!=1)        //Handle (Create) last user 
        {
            //Initialize new user
            users[Running_User]= new User(Running_User, Last_Movie, RatingsSum, RatingsNum, NO3_RatingsSum, NO3_RatingsNum, 
                                          Min_Time, Max_Time, minRating, maxRating);
            usersRatingSet[Running_User]= new HashSet<>();
            usersRatingSet[Running_User].addAll(userRatingSet);
        } 
        
        totalUsers=Running_User-1;
        
        } // try 
        catch (IOException e) {

            e.printStackTrace();

	} //catch file error
        int[] mainStats={totalUsers, totalMovies-1};
        return mainStats;
        
} //method Data_Initialisation_Amazon_Video_Games

}//END of class Initialization

