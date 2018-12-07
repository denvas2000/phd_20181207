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
public class File_Convertion_Amazon_Books {

public static final int MIN_RATINGS_PER_USER =10;           //A user to be included in a test dataset 
                                                            //has to rate at least MIN_RATINGS_PER_USER items
public static Ratings ratings[]= new Ratings[8655000];      //All ratings (lines of the Datafile)
public static SimpleUser simpleUsers[] = new SimpleUser[8027000];

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
BOOKS METHOD LOAD.
GETS as input, a sorted file (according to userid), and produces a new file, with all ratings with the following characteristics:
1.Each user has a new unique numeric ID 
2.Each user will have at least K unique ratings
3.If a movie has been rated more than once the latest rating will be taken into cosnideration    
4.Each movie will have a  new unique numeric ID
*/    
public static int[] Books_Fix_USER_ID(String inDataFile, String outDataFile)
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

    // All lines have 4 nums                
    // It's user, items, rating, timestamp                                                  

    //System.out.println(dataFile);
    FileWriter outFile = new FileWriter( outDataFile );
    BufferedReader Data_File = new BufferedReader(new FileReader(inDataFile)); //Open text file to read        

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
                    //simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);
                    RatingsNum = 0;
                    Running_User++;
            }
            
            outFile.write(Running_User+"\t"+Nums_Line[1]+"\t"+Nums_Line[2]+"\t"+Nums_Line[3]);
            outFile.write("\r\n");
            
            //They HAVE TO BE AT THE END OF WHILE LOOP
            RatingsNum++;
            prevUser=currUser;
            currLine++;

        } // while (Line)

        //simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);//Handle (Create) last user 
        
        outFile.close();

    } // try 
    catch (IOException e

    
        ) {

            e.printStackTrace();

    } //catch file error
    
    int[] mainStats = {currLine, (int)(Running_User++)};
    return mainStats ;

}//END Books_Fix_USER_ID

public static int[] Books_Fix_ITEM_ID(String dataFile, Ratings[] ratings)
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

}//END Books_Fix_ITEM_ID

public static int[] Books_Store_Fixed_USER_ID(String inDataFile, String outDataFile, SimpleUser simpleUsers[])
{        
String Line;        //Each Line of the Text File
Scanner Scan_Line;  //Line Scanner to find the data of each line of the Text File
int Nums_Per_Line_Count = 0;
String Nums_Line[] = new String[4]; //The numeric values of each line of the file (min=3, max=4)

int i;
int RatingsNum = 0;      //The number of UserID ratings

//Vars for the data manipulation

int Running_User = 0;
int currLine = 0;
String currUser="";
int userId;
String prevUser="";


    
try {   //Read Files. Initiate tables

    // All lines have 4 nums                
    // It's user, items, rating, timestamp                                                  

    //System.out.println(dataFile);
    BufferedReader Data_File = new BufferedReader(new FileReader(inDataFile)); //Open text file to read        

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
            currUser =Nums_Line[0];
            
            
            
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */

            if ((!currUser.equals(prevUser)) && (currLine>0)) //Create user after all calculations are over
            {
                    //Initialize new user
                    simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);
                    RatingsNum = 0;
                    Running_User++;
            }
            
            RatingsNum++;
            prevUser=currUser;
            currLine++;

        } // while (Line)

        simpleUsers[Running_User] = new SimpleUser(Running_User, RatingsNum);//Handle (Create) last user 
        
    } // try 
    catch (IOException e

    
        ) {

            e.printStackTrace();

    } //catch file error

System.out.println("Users:"+Running_User+"\n");

try {

    BufferedReader Data_File = new BufferedReader(new FileReader(inDataFile)); //Open text file to read        
    FileWriter outFile = new FileWriter( outDataFile );
    //Export Rating to new file
    
    //int totalRatings=totals[0];
    
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
            userId=Integer.parseInt(Nums_Line[0]);
            
            
            /* -----        FINISH: READ A SINGLE LINE            ----- */
            /* ------ START: STORE NUMBERS IN THE RIGHT TABLES -------- */
        
        //System.out.println("User:"+userId+"  Ratings Num:"+simpleUsers[userId].ratingsNum);
        if (simpleUsers[userId].ratingsNum>=MIN_RATINGS_PER_USER) {
            
            outFile.write(userId+"\t"+Nums_Line[1]+"\t"+Nums_Line[2]+"\t"+Nums_Line[3]);
            outFile.write("\r\n");
        
        }
            
            

    } // while (Line)

    outFile.close();
    Data_File.close();
    
} //try  
catch (IOException iox) {
    //do stuff with exception
    iox.printStackTrace();
} //catch

//System.out.println("Ratings Num:"+ratingsNum);

    int[] mainStats = {currLine, (int)(Running_User++)};
    return mainStats ;

}//END Method Books_UseFulFix_User_ID

public static void Books_Store_FixedUserID(String dataFile, SimpleUser simpleUsers[], Ratings ratings[], int totals[])
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

public static void Books_Store_FixBookID(String dataFile, Ratings ratings[], int totalRatings)
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

int[] totals = new int[2];      //Holds total number of ratings and users
int[] totals_item = new int[2];

//PART A
//Step 1 and Step 2 are linux based

//Step 3
//totals=Books_Fix_USER_ID("DataSets/04.Amazon_Books/ratings_Books_UserSorted.tab", "DataSets/04.Amazon_Books/ratings_Books_Step_03.tab");
//System.out.println("333.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);

//Print_Users(simpleUsers, totals[1]);

//Step 4
//Books_Store_Fixed_USER_ID("DataSets/04.Amazon_Books/ratings_Books_Step_03.tab", "DataSets/04.Amazon_Books/ratings_Books_Step_04.tab", simpleUsers);
//System.out.println("444.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]); //DEN to gemizv to totals!!!!

//Step 5
//totals=Books_Fix_USER_ID("DataSets/04.Amazon_Books/ratings_Books_Step_04.tab", "DataSets/04.Amazon_Books/ratings_Books_Step_05.tab");
//System.out.println("555.ITS OK! Lines/Ratings:"+totals[0]+" Users:"+totals[1]);

//Print_Users(simpleUsers, totals[1]);

//PART B
//Step 6 is linux based

//Step 7
//Arrays.fill(ratings,null);
//totals_item=Books_Fix_ITEM_ID("DataSets/04.Amazon_Books/ratings_Books_Step_06.tab", ratings);
//System.out.println("777.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);
//Print_Ratings(ratings, totals_item[0]);
//Print_Users(simpleUsers, totals[1]);

//Step 8
//Books_Store_FixBookID("DataSets/04.Amazon_Books/ratings_Books_Step_08.tab", ratings, totals_item[0]);
//System.out.println("888.Lines/Ratings:"+totals_item[0]+" Movies:"+totals_item[1]);

//PART C 
//Steps 9,10 is Linux based


} //Main 
 
} //Class
