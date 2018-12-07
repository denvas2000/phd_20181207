/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RawDataManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
 
/**
 *
 * @author denis
 * 
 * Class File_Convert is 
 */
public class File_Convertion_Amazon_Video_Games {

public static final int MIN_RATINGS_PER_USER =2;
public static Ratings ratings[]= new Ratings[1325000];    
public static SimpleUser simpleUsers[] = new SimpleUser[860000];

public static void Print_Users (SimpleUser[] simpleUsers, int users)
{
int i;

System.out.println("USER'S DATA");
for (i=0;i<users;i++) {
    System.out.println(simpleUsers[i].userId+" "+simpleUsers[i].ratingsNum);
}

}        

public static void Print_Ratings (Ratings ratings[], int ratingnum)
{
int i;

System.out.println("RATINGS'S DATA");
for (i=0;i<ratingnum;i++) {
    System.out.println(ratings[i].userId+" "+ratings[i].itemId+" "+ratings[i].rating+" "+ratings[i].timeStamp);
}

}    
/*
Video_Games METHOD LOAD.
GETS as input, a sorted file (according to userid), and produces a new file, with all ratings with the following characteristicsQ
1.Each user has a new unique numeric ID 
2.Each user will have at least K unique ratings
3.If a movie has been rated more than once the latest rating will be taken into cosnideration    
4.Each movie will have a  new unique numeric ID
*/    
public static int[] Video_Games_Fix_USER_ID(String dataFile, SimpleUser[] simpleUsers, Ratings[] ratings)
{        
String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count = 0;
String Nums_Line[] = new String[4]; //The numeric values of each line of the file (min=3, max=4)

int RatingsNum = 0;      //The number of UserID ratings

//Vars for the data manipulation

int Running_User = 0;
int currLine = 0;
String currUser="";
String prevUser="";


    
    try {   //Read Files. Initiate tables

    /* When a line has 3 nums it's (user, 0, num of user ratings)                           */
    /* Then a number of lines follow (equal to "num of user ratings") that have 4 nums      */
    /* It's user, items, rating, timestamp                                                  */

    //System.out.println(dataFile);
    
    BufferedReader Data_File = new BufferedReader(new FileReader(dataFile)); //Open text file to read        

        /* Read text file to the end */
        while ((Line = Data_File.readLine()) != null) //While there are still lines
        {

            /* ************************************ */
            /* START: READ A SINGLE LINE */
            Nums_Per_Line_Count = 0;                      //Numbers per line
            Scan_Line = new Scanner(Line);              //Read Line from text file

            // Read the numbers in line, store them in Nums_Line, max numbers 4 (or the rest are ignored)
            while ((Scan_Line.hasNext()) && (Nums_Per_Line_Count < 4)) {
                Nums_Line[Nums_Per_Line_Count++] = Scan_Line.next(); 
            }
            
            
            Scan_Line.close();
            currUser=Nums_Line[0];
            
            
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */

            if ((!currUser.equals(prevUser)) && (currLine>0)) //Create user after all calculations are over
            {
                    //Initialize new user
                    simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);
                    RatingsNum = 0;
                    Running_User++;
            }
            
            
            ratings[currLine] = new Ratings(Running_User, Nums_Line[1], Nums_Line[2], Nums_Line[3]);

            //They HAVE TO BE AT THE END OF WHILE LOOP
            RatingsNum++;
            prevUser=currUser;
            currLine++;

        } // while (Line)

        simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);//Handle (Create) last user 
        //ratings[currLine] = new Ratings(Running_User, Nums_Line[1], Nums_Line[2], Nums_Line[3]);  


    } // try 
    catch (IOException e

    
        ) {

            e.printStackTrace();

    } //catch file error
    int[] mainStats = {currLine, (int)(Running_User+1)};
    return mainStats ;

}//END Method Video Games Load

public static int[] Video_Games_Fix_ITEM_ID(String dataFile, Ratings[] ratings)
{        
String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count;
String Nums_Line[] = new String[4]; //The numeric values of each line of the file (min=3, max=4)

int RatingsNum = 0;      //The number of UserID ratings

//Vars for the data manipulation

int maxuserID=-1;
int Running_Movie = 0;
int currLine = 0;
String currMovie="";
String prevMovie="";
int userid=-1;

    
    try {   //Read Files. Initiate tables

    /* When a line has 3 nums it's (user, 0, num of user ratings)                           */
    /* Then a number of lines follow (equal to "num of user ratings") that have 4 nums      */
    /* It's user, items, rating, timestamp                                                  */

    //System.out.println(dataFile);
    
    BufferedReader Data_File = new BufferedReader(new FileReader(dataFile)); //Open text file to read        

        /* Read text file to the end */
        while ((Line = Data_File.readLine()) != null) //While there are still lines
        {

            /* ************************************ */
            /* START: READ A SINGLE LINE */
            Nums_Per_Line_Count = 0;                      //Numbers per line
            Scan_Line = new Scanner(Line);              //Read Line from text file

            // Read the numbers in line, store them in Nums_Line, max numbers 4 (or the rest are ignored)
            //while ((Scan_Line.hasNext()) && (Nums_Per_Line_Count < 4)) {
            //    Nums_Line[Nums_Per_Line_Count++] = Scan_Line.next(); 
            //}
            userid=Scan_Line.nextInt();
            Nums_Line[1] = Scan_Line.next();
            Nums_Line[2] = Scan_Line.next();
            Nums_Line[3] = Scan_Line.next();
            Scan_Line.close();

            currMovie=Nums_Line[1];
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */

            if ((!currMovie.equals(prevMovie)) && (currLine>0)) //Create user after all calculations are over
            {
                    Running_Movie++;
            }
            
            
            ratings[currLine] = new Ratings(userid, String.valueOf(Running_Movie), Nums_Line[2], Nums_Line[3]);

            //They HAVE TO BE AT THE END OF WHILE LOOP
            prevMovie=currMovie;
            currLine++;
            if (maxuserID<userid) maxuserID=userid;
        } // while (Line)
        
        //Handle (Create) last user 
        //ratings[currLine] = new Ratings(userid, String.valueOf(Running_Movie), Nums_Line[2], Nums_Line[3]); 
        System.out.println("Max user="+maxuserID); //Show one user less: user 0

    } // try 
    catch (IOException e

    
        ) {

            e.printStackTrace();

    } //catch file error
    int[] mainStats = {currLine, (int)(Running_Movie+1)};
    return mainStats ;

}//END Method Video Games Load


public static void Video_Games_Store_FixUserID(String dataFile, SimpleUser simpleUsers[], Ratings ratings[], int totals[])
{        

int ratingsNum = 0;      //The number of UserID ratings

//Vars for the data manipulation

int i;
int userid;

try(FileWriter outFile = new FileWriter( dataFile )) {

    //Export Rating to new file
    
    int totalRatings=totals[0];
    
    for (i=0;i<totalRatings;i++) {
        
        userid=ratings[i].userId;
        
        if (simpleUsers[userid].ratingsNum>=MIN_RATINGS_PER_USER) {
            
            ratingsNum++;
            outFile.write(ratings[i].userId+"\t"+ratings[i].itemId+"\t"+ratings[i].rating+"\t"+ratings[i].timeStamp);
            outFile.write("\r\n");
        
        }
    
    }

    outFile.close();
    
} //try  
catch (IOException iox) {
    //do stuff with exception
    iox.printStackTrace();
} //catch

System.out.println("Ratings Num:"+ratingsNum);

}//END Method Video Games Store

public static void Video_Games_Store_FixMovieID(String dataFile, Ratings ratings[], int totalRatings)
{        

int ratingsNum = 0;      //The number of UserID ratings

//Vars for the data manipulation

int i;
int movieid;

try(FileWriter outFile = new FileWriter( dataFile )) {

    //Export Rating to new file
    
    
    for (i=0;i<totalRatings;i++) {
        

            outFile.write(ratings[i].userId+"\t"+ratings[i].itemId+"\t"+ratings[i].rating+"\t"+ratings[i].timeStamp);
            outFile.write("\r\n");
        
    }

    outFile.close();
    
} //try  
catch (IOException iox) {
    //do stuff with exception
    iox.printStackTrace();
} //catch


}//END Method Video Games Store

public static void main (String args[]) {

int[] totals = new int[2];
int[] totals_item = new int[2];

//PART A
//Step 1 and Step 2 are linux based

//Step 3
//totals=Video_Games_Fix_USER_ID("/home/denis/Downloads/ratings_Video_Games_UserSorted.tab", simpleUsers, ratings);
//System.out.println("333.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);
//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 4
//Video_Games_Store_FixUserID("/home/denis/Downloads/ratings_Video_Games_FixedUserSorted.tab", simpleUsers, ratings, totals);
//System.out.println("444.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);

//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//PART B
//Step 5 is linux based

//Step 6
//Arrays.fill(ratings,null);
//totals_item=Video_Games_Fix_ITEM_ID("/home/denis/Downloads/ratings_Video_Games_ItemSorted.tab", ratings);
//System.out.println("666.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);
//Print_Ratings(ratings, totals_item[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 7
//Video_Games_Store_FixMovieID("/home/denis/Downloads/ratings_Video_Games_FixedItemSorted.tab", ratings, totals_item[0]);
//System.out.println("777.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);

//PART C 
//Step 8 is Linux based

//Step 9
//Arrays.fill(ratings,null);Arrays.fill(simpleUsers,null);
//totals=Video_Games_Fix_USER_ID("/home/denis/Downloads/ratings_Video_Games_AllFixed.tab", simpleUsers, ratings);
//System.out.println("999.Lines/Ratings:"+totals[0]+" Users:"+totals[1]);
//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 10
//Video_Games_Store_FixUserID("/home/denis/Downloads/ratings_Video_Games_Final.tab", simpleUsers, ratings, totals);
//System.out.println("101010.Lines/Ratings:"+totals[0]+" Users:"+totals[1]);

//---------------------TESTING -------------------------------
//
//------------------------------------------------------------
//

//PART A
//Step 1 and Step 2 are linux based

//Step 3
//totals=Video_Games_Fix_USER_ID("/home/denis/Downloads/aek2.txt", simpleUsers, ratings);
//System.out.println("333.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);
//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 4
//Video_Games_Store_FixUserID("/home/denis/Downloads/aek3.txt", simpleUsers, ratings, totals);
//System.out.println("444.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);

//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//PART B
//Step 5 is linux based

//Step 6
//Arrays.fill(ratings,null);
//totals_item=Video_Games_Fix_ITEM_ID("/home/denis/Downloads/aek5.txt", ratings);
//System.out.println("666.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);
//Print_Ratings(ratings, totals_item[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 7
//Video_Games_Store_FixMovieID("/home/denis/Downloads/aek7.txt", ratings, totals_item[0]);
//System.out.println("777.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);

//PART C 
//Step 8 is Linux based

//Step 9
//Arrays.fill(ratings,null);Arrays.fill(simpleUsers,null);
//totals=Video_Games_Fix_USER_ID("/home/denis/Downloads/aek8.txt", simpleUsers, ratings);
//System.out.println("999.Lines/Ratings:"+totals[0]+" Users:"+totals[1]);
//Print_Ratings(ratings, totals[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 10
//Video_Games_Store_FixUserID("/home/denis/Downloads/aek10.txt", simpleUsers, ratings, totals);
//ystem.out.println("101010.Lines/Ratings:"+totals[0]+" Users:"+totals[1]);


} //Main 

} //Class
