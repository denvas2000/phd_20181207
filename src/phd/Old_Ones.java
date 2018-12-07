/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Old_Ones {

/**
 * 
 * Negative_Average_Similarity: Method to compute negative similarities among all neighbors based on the "Average Method".
 * Accepts as imput the following variables 
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Negative_Average_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
UserMovie[][] userMovies,
double simBase,
int commonMovies,
int absMinTimeStamp,
int absMaxTimeStamp)
    
{
        
int    i, j, k;
double tempWeight, negWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB, denominatorSimij;      //Denominator consists of two parts        
double similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;

//System.out.println("Similarity"+simBase);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=users[i].UserAverageRate();            
    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>FN_100K_OLD.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;
                
            for (k=0;k<=totalMovies;k++)
            {

                if (!(userMovies[i][k]==null) && !(userMovies[j][k]==null))
                {
                    //The WEIGHT assigned to negative similarities
                    if (FN_100K_OLD.NEG_WEIGHT_TYPE==0) 
                        negWeight=1;
                    else
                        negWeight=Phd_Utils.Neg_Weight(userMovies[i][k].getRating(), userMovies[j][k].getRating());
                    
                    tempMovies++;
                    if (FN_100K_OLD.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(userMovies[i][k].Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        userMovies[i][k].setWeight(tempWeight);   
                        tempWeight=(double)(userMovies[j][k].Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        userMovies[j][k].setWeight(tempWeight);   
                    }
                    else
                        if (FN_100K_OLD.WEIGHT_TYPE==2)
                        {
                            // It seems of no use. 
                            // Weight based on overall timespan 
                            tempWeight=(double)(userMovies[i][k].Time_Stamp-absMinTimeStamp)/(absMaxTimeStamp-absMinTimeStamp);
                            userMovies[i][k].setWeight(tempWeight);   
                            tempWeight=(double)(userMovies[j][k].Time_Stamp-absMinTimeStamp)/(absMaxTimeStamp-absMinTimeStamp);
                            userMovies[j][k].setWeight(tempWeight);                    
                            //System.out.println(tempWeight);
                        }
                        else
                        {
                            userMovies[i][k].setWeight(1);   
                            userMovies[j][k].setWeight(1);   
                        }
                    
                    
                    if (((userMovies[i][k].getRating()<averageUI) && (userMovies[j][k].getRating()>averageUJ)) ||
                        ((userMovies[i][k].getRating()>averageUI) && (userMovies[j][k].getRating()<averageUJ)))
                    {
                        tempMovies++;            
                        numeratorSimij   += (userMovies[i][k].getRating()-averageUI)*(userMovies[j][k].getRating()-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight()*negWeight;
                        denominatorPartA += (userMovies[i][k].getRating()-averageUI)*(userMovies[i][k].getRating()-averageUI);
                        denominatorPartB += (userMovies[j][k].getRating()-averageUJ)*(userMovies[j][k].getRating()-averageUJ);
                    }
                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
                
            //find min/max similarity values
            if (MinSimValue>similarity) MinSimValue=similarity;
            else
            if (maxSimValue<similarity) maxSimValue=similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            {
                //Reverse Similarity
                if (similarity<=simBase)
                {
                    
                    userSim[i].add(new UserSimilarity(i,j,similarity));
                    userSim[j].add(new UserSimilarity(j,i,similarity));

                } 
                
            }
            
        }//for j

}//for i

//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);

}//END OF METHOD Negative_Average_Similarity

public static double[] Negative_Average_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] Users,
UserMovie[][] userMovies,
int minSimNeigh,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0;
List<UserSimilarity> UserList = new ArrayList<>();
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=0;    //The total number of actually predicted values
double MAE=0.0;                           //Mean Absolute Error of Prediction.

for (i=0;i<=totalUsers;i++) 
{

    UserList=userSim[i];
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=Users[i].lastMovieId;
            
    //if (!UserList.isEmpty()) 
    if (UserList.size()>minSimNeigh) 
    {   
        if (bestNeigh<userSim[i].size())
            UserList=userSim[i].subList(0, bestNeigh-1);
        simNeighbors++;

        for (UserSimilarity io: UserList)
        {
                    
            if (userMovies[io.SUser_Id][k]!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies[io.SUser_Id][k].getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }
        }
    }
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        Users[i].negAverPrediction=FN_100K_OLD.NO_PREDICTION; 
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
        Users[i].negAverPrediction=((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are FN that rated LastMovie

        if (Users[i].negAverPrediction>5) Users[i].negAverPrediction=5;
        else
        if ((Users[i].negAverPrediction<1) && (Users[i].negAverPrediction!=FN_100K_OLD.NO_PREDICTION)) Users[i].negAverPrediction=1;          

        MAE += Math.abs(Users[i].negAverPrediction-userMovies[i][k].getRating());
        predictedValues++;
    }
           
    UserList=new ArrayList<>();
    
}    
return new double[] {simNeighbors, predictedValues, MAE};

} //END OF METHOD Negative_Average_Prediction 


}
