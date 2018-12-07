/**
 *
 * @author Dennis Vassilopoulos
 * Creation Date: 23/1/2018
 * 
 * This class defines all mathods used in computing all kind of similarities, 
 * Initially only one composite method was implemented, but during the implementation of
 * the project, rose the need to split the original method into several, district, and more
 * managable methods.
 */

package phd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
//import static phd.FN_Amazon_Video_Games.usersRatingSet;

public class Similarities {

/*  *****
    Print Similarities: For every user, prints both its similar users and the similarity score.
    Handles normal, reverse and NO3 similarities.
    *****
*/
public static void Print_Similarities (int totalUsers, List<UserSimilarity>[] userSim){

int  i, k=5000;
List<UserSimilarity> UserList = new ArrayList<>();

System.out.println("Print Similarities:"+totalUsers+"\n");

for (i=0;i<=totalUsers;i++) 
{
    UserList=userSim[i];
    //System.out.println(UserList.size());
    if (UserList.size()>0) 
    {   
        if (k>UserList.size()) k=UserList.size();
        for (UserSimilarity io: UserList)
        {
            System.out.print(io.FUser_Id+" "+io.SUser_Id+" "+io.Similarity+"<-->");
        } //for io
        System.out.println();
    }//if 
                  
}//for i

}//END Print_Similarities


/**
 * 
 * Compute_Positive-Similarity: Method to compute ONLY POSITIVE similarities among all neighbors. 
 * Accepts as imput the following variables
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param Users
 * @param userMovies
 * @param usersRatingSet
 
 */

public static void Positive_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] Users,
UserMovie[][] userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies
//int absMinTimeStamp,
//int absMaxTimeStamp
)
    
{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated

//System.out.println("Similarity"+simBase);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=Users[i].UserAverageRate();            
    if ((Users[i].getMaxTimeStamp()-Users[i].getMinTimeStamp())>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=Users[j].UserAverageRate();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            userRatingSet=usersRatingSet[i];
            for (int k: userRatingSet)
            {

                if (!(userMovies[i][k]==null) && !(userMovies[j][k]==null))
                {
                    
                    tempMovies++;
                    if (FN_100K_OLD.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(userMovies[i][k].Time_Stamp-Users[i].getMinTimeStamp())/(double)(Users[i].getMaxTimeStamp()-Users[i].getMinTimeStamp());
                        userMovies[i][k].setWeight(tempWeight);   
                        tempWeight=(double)(userMovies[j][k].Time_Stamp-Users[j].getMinTimeStamp())/(double)(Users[j].getMaxTimeStamp()-Users[j].getMinTimeStamp());
                        userMovies[j][k].setWeight(tempWeight);   
                    }
                    else
                    {
                        userMovies[i][k].setWeight(1);   
                        userMovies[j][k].setWeight(1);   
                    }
                    
                    
                    numeratorSimij += (userMovies[i][k].getRating()-averageUI)*(userMovies[j][k].getRating()-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight();
                    denominatorPartA += (userMovies[i][k].getRating()-averageUI)*(userMovies[i][k].getRating()-averageUI);
                    denominatorPartB += (userMovies[j][k].getRating()-averageUJ)*(userMovies[j][k].getRating()-averageUJ);

                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
                
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies) {
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //System.out.println(Similarity+"Den");
                        userSim[i].add(new UserSimilarity(i,j,Similarity,1));
                        userSim[j].add(new UserSimilarity(j,i,Similarity,1));

                } 
            }    
        }//for i

}
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
} //END OF METHOD Positive_Similarity



/**
 * 
 * Inverted_Similarity: 
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Inverted_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] Users,
UserMovie[][] userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies,
int absMinTimeStamp,
int absMaxTimeStamp)
    
{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated

//System.out.println("Similarity"+simBase);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=Users[i].UserInvertedAverageRating();            
    if ((Users[i].getMaxTimeStamp()-Users[i].getMinTimeStamp())>FN_100K_OLD.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=Users[j].UserInvertedAverageRating();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            userRatingSet=usersRatingSet[i];
            for (int k: userRatingSet)
            {

                if (!(userMovies[i][k]==null) && !(userMovies[j][k]==null))
                {
                    
                    tempMovies++;
                    if (FN_100K_OLD.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(userMovies[i][k].Time_Stamp-Users[i].getMinTimeStamp())/(double)(Users[i].getMaxTimeStamp()-Users[i].getMinTimeStamp());
                        userMovies[i][k].setWeight(tempWeight);   
                        tempWeight=(double)(userMovies[j][k].Time_Stamp-Users[j].getMinTimeStamp())/(double)(Users[j].getMaxTimeStamp()-Users[j].getMinTimeStamp());
                        userMovies[j][k].setWeight(tempWeight);   
                    }
                    else
                    {
                        userMovies[i][k].setWeight(1);   
                        userMovies[j][k].setWeight(1);   
                    }
                    
                    
                    numeratorSimij += (userMovies[i][k].invRating-averageUI)*(userMovies[j][k].invRating-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight();
                    denominatorPartA += (userMovies[i][k].invRating-averageUI)*(userMovies[i][k].invRating-averageUI);
                    denominatorPartB += (userMovies[j][k].invRating-averageUJ)*(userMovies[j][k].invRating-averageUJ);

                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
                
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //System.out.println(Similarity+"Eff");
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                
        }//for i

}

} //END OF METHOD Inverted_Similarity

/**
 * 
 * Compute_Similarity: Method to compute similarities among all neighbors. Accepts as imput the
 * following variables
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Compute_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
UserMovie[][] userMovies,
HashSet<Integer>[] usersRatingSet,
int similaritySign,
double simBase,
int commonMovies)
    
{
        
int    i, j;
double tempWeight, negWeight;
int    tempMovies, temp_no3movies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, negNumeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity, negSimilarity;
double NO3_numeratorSimij, NO3_denominatorSimij;
double NO3_denominatorPartA, NO3_denominatorPartB;
double NO3_similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated


//System.out.println("Similarity"+simBase);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=users[i].UserAverageRate();            
    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>FN_100K_OLD.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;negNumeratorSimij=0.0;denominatorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;
            NO3_numeratorSimij=0;NO3_denominatorSimij=0;
            NO3_denominatorPartA=0; NO3_denominatorPartB=0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;temp_no3movies=0;
                
            //for (k=0;k<=totalMovies;k++)
            userRatingSet=usersRatingSet[i];
            for (int k: userRatingSet)            
            {

                if (!(userMovies[i][k]==null) && !(userMovies[j][k]==null))
                {
                    
                    tempMovies++;
                    
                    if (FN_100K_OLD.NEG_WEIGHT_TYPE==0) 
                        negWeight=1;
                    else
                        negWeight=Phd_Utils.Neg_Weight(userMovies[i][k].getRating(), userMovies[j][k].getRating());
                    
                    if (FN_100K_OLD.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(userMovies[i][k].Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        userMovies[i][k].setWeight(tempWeight);   
                        tempWeight=(double)(userMovies[j][k].Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        userMovies[j][k].setWeight(tempWeight);   
                    }
                    else
                    {
                        userMovies[i][k].setWeight(1);   
                        userMovies[j][k].setWeight(1);   
                    }
                    
                    
                    numeratorSimij += (userMovies[i][k].getRating()-averageUI)*(userMovies[j][k].getRating()-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight();
                    negNumeratorSimij += (userMovies[i][k].getRating()-averageUI)*(userMovies[j][k].getRating()-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight()*negWeight;
                    denominatorPartA += (userMovies[i][k].getRating()-averageUI)*(userMovies[i][k].getRating()-averageUI);
                    denominatorPartB += (userMovies[j][k].getRating()-averageUJ)*(userMovies[j][k].getRating()-averageUJ);

                    if ((userMovies[i][k].getRating()!=3) && (userMovies[j][k].getRating()!=3) && (similaritySign==2))
                    {
                        temp_no3movies++;            
                        NO3_numeratorSimij   += (userMovies[i][k].getRating()-averageUI)*(userMovies[j][k].getRating()-averageUJ)*userMovies[i][k].getWeight()*userMovies[j][k].getWeight()*negWeight;
                        NO3_denominatorPartA += (userMovies[i][k].getRating()-averageUI)*(userMovies[i][k].getRating()-averageUI);
                        NO3_denominatorPartB += (userMovies[j][k].getRating()-averageUJ)*(userMovies[j][k].getRating()-averageUJ);
                    }
                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
            negSimilarity=(double)(negNumeratorSimij/Math.sqrt(denominatorSimij));
                
            NO3_denominatorSimij= NO3_denominatorPartA * NO3_denominatorPartB;
            NO3_similarity=(double)(NO3_numeratorSimij/Math.sqrt(NO3_denominatorSimij));
                
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            {
                if (Similarity>=simBase && similaritySign==1)    //Only Positive Similarities are Considered
                {
                    
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                else    
                   //Reverse Similarity
                if (negSimilarity<=simBase && similaritySign==0)
                {
                    
                    userSim[i].add(new UserSimilarity(i,j,negSimilarity));
                    userSim[j].add(new UserSimilarity(j,i,negSimilarity));

                } 
                
            }
            
            if (temp_no3movies>commonMovies)
                if (NO3_similarity<=simBase && similaritySign==2)
                    {
                        userSim[i].add(new UserSimilarity(i,j,NO3_similarity));
                        userSim[j].add(new UserSimilarity(j,i,NO3_similarity));                        
                    }
                        
            
        
        }//for i

}
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
}
}
