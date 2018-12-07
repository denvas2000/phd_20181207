/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;


/**
 *
 * @author denis@@di.uoa.gr 20/11/2017
 * Ver 1.0.
 */

public class UserSimilarity implements Comparable<UserSimilarity>{ 
    
public int FUser_Id;
public int SUser_Id;
public Double Similarity;           //It has to be Double (object) so as to settle compareTo method
public double combinedSimilarity;
public int flag;



//class constructor;
public UserSimilarity (int fuser, int suser, Double usersimilarity) {
    
   FUser_Id=fuser;
   SUser_Id=suser;
   Similarity=usersimilarity;
   combinedSimilarity=Math.abs(usersimilarity);
}

//class constructor;
public UserSimilarity (int fuser, int suser, Double usersimilarity, int flag) {
    
   FUser_Id=fuser;
   SUser_Id=suser;
   Similarity=usersimilarity;
   combinedSimilarity=Math.abs(usersimilarity);
   this.flag=flag;
}

public UserSimilarity () {
    
   
}
@Override
public int compareTo(UserSimilarity us)
{
    //return (int)(this.Similarity - us.Similarity);
    return this.Similarity.compareTo(us.Similarity);
    
}

public double GetCombinedSimilarity() {
    return combinedSimilarity;
}
} //end UserSimilarity class definition
 