/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

/**
 *
 * @author denis@di.uoa.gr 20/11/2017
 * Ver 1.0.
 */

public class User { 
    
public int UserId;            //Define Member Variables
public int lastMovieId;
private int ratingSum;
public int invRatingSum;
private int ratingNum;
private int NO3RatingSum;
private int NO3RatingNum;
private int MinTimeStamp;
private int MaxTimeStamp;
public int maxRating;
public int minRating;
private int Prediction;
private int RevPrediction;
private int NO3RevPrediction;
public int negAverPrediction;
public int combinedPrediction;


//class constructor 1;
public User (int user, int mintime, int maxtime) {
    
   UserId=user;
   MinTimeStamp=mintime;
   MaxTimeStamp=maxtime;
   
}

//class constructor 2;
public User (int UserId, int lastMovieId, int ratingssum, int ratingsnum, int NO3RatingSum, int NO3RatingNum, 
             int mintime, int maxtime, int minRating, int maxRating) {
    
   this.UserId=UserId;
   this.lastMovieId=lastMovieId;
   ratingSum=ratingssum;
   ratingNum=ratingsnum;
   this.NO3RatingSum=NO3RatingSum;
   this.NO3RatingNum=NO3RatingNum;
   MinTimeStamp=mintime;
   MaxTimeStamp=maxtime;
   this.minRating=minRating;
   this.maxRating=maxRating;
      
}
//Access Mofifiers. Set/Get Methods.

/*
public int getUserId () {
    return UserId;
}


public int getLastMovieId () {
    return LastMovieId;
}
*/
public int getRatingSum () {
    return ratingSum;
}

public int getRatingNum () {
    return ratingNum;
}

public int getMinTimeStamp () {
    return MinTimeStamp;
}

public int getMaxTimeStamp () {
    return MaxTimeStamp;
}

public int getPrediction () {
    return Prediction;
}

public int getRevPrediction () {
    return RevPrediction;
}

public int getNO3RevPrediction () {
    return NO3RevPrediction;
}

public void setMaxTimeStamp (int MaxTimeStamp) {
    this.MaxTimeStamp=MaxTimeStamp;
}

public void setMinTimeStamp (int MinTimeStamp) {
    this.MinTimeStamp=MinTimeStamp;
}

public void setPrediction (int Prediction) {
    this.Prediction=Prediction;
}

public void setRevPrediction (int Prediction) {
    this.RevPrediction=Prediction;
}

public void setNO3RevPrediction (int Prediction) {
    this.NO3RevPrediction=Prediction;
}
public double UserAverageRate () {
    
    return (double)ratingSum/(double)ratingNum;
    
}

public double UserInvertedAverageRating () {
    
    return (double)invRatingSum/(double)ratingNum;
    
}
} //end User class definition
 