/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

/**
 *
 * @author denis@@di.uoa.gr 19/11/2017
 * Ver 1.0.
 */

public class UserMovie { 
    
public int User_Id;
public int Movie_Id;
private int Rating;
public int invRating;
private double negWeight; //NOT NEEDED
public int Time_Stamp;
private double Weight;


//class constructor;
public UserMovie (int user, int movie, int rating, int timestamp, double weight) {
    
   User_Id=user;
   Movie_Id=movie;
   Rating=rating;
   Time_Stamp=timestamp;
   Weight=weight;
   if (Global_Vars.NEG_WEIGHT_TYPE==1) //NOT NEEDED
   switch(rating) {
       case 1: negWeight=0.5;break;
       case 2: negWeight=0.75;break;
       case 3: negWeight=0.75;break;
       case 4: negWeight=1;break;
       case 5: negWeight=1;break;
   } 
   else 
       negWeight=1;
}

public UserMovie () {
    
   
}

public int getRating () {
    return Rating;
}

public void setRating (int Rating) {
    this.Rating=Rating;
}

public double getWeight () {
    return Weight;
}

public void setWeight (double Weight) {
    this.Weight=Weight;
}

public double getNegWeight () {
    return negWeight;
}

public void setNegWeight (double negWeight) {
    this.negWeight=negWeight;
}

public int reverseRating () {           //NOT NEEDED

int tempMaxRating = 6;
    
return (tempMaxRating-getRating()%tempMaxRating);
        
}


} //end UserMovie class definition
 