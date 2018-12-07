/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RawDataManagement;

/**
 *
 * @author denis
 */
public class Ratings {

int userId;
String itemId;
String rating;
String timeStamp;

public Ratings (int uid, String itid, String rat, String ts) {

userId=uid;
itemId=itid;
rating=rat;
timeStamp=ts;

}
}
