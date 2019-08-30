 /*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.journaldev.mongodb.dao;

//import com.journaldev.mongodb.converter.PersonConverter;
import com.journaldev.mongodb.converter.RegistrationConverter;
//import com.journaldev.mongodb.model.Person;
import com.journaldev.mongodb.model.Registration;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author sumit
 */
public class Registration_DAO {
    
    
    private DBCollection col;
    MongoClient mongo;
    public Registration_DAO(MongoClient mongo) {
		this.mongo = mongo;
        this.col = mongo.getDB("Registration").getCollection("UserDetails");
    }
    
    public Registration createUserOld(Registration registration) {
        DBObject doc = RegistrationConverter.toDBObject(registration);
        this.col.insert(doc);//data inserted here
        ObjectId id = (ObjectId) doc.get("_id");//primary key auto generated is feyched here
        registration.setId(id.toString());//PK set to object
        return registration;
    }
	
	public Registration createUser(Registration model){
		
	//MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    // Now connect to your databases
    //DB db = mongo.getDB("Registration");
    //DBCollection collection = db.getCollection("UserCounters");
    BasicDBObject document = new BasicDBObject();

    document.put("_id", getNextSequence("userid"));
    document.put("user_id",model.getUser_id());
    document.put("name",model.getName());
    document.put("contact",model.getContact());
    document.put("emp_id",model.getEmp_id());
    document.put("password",model.getPassword());
    col.insert(document); // insert first doc
	return model;
	}
	
	public  Object getNextSequence(String name) {
    //MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    // Now connect to your databases
   // DB db = mongoClient.getDB("Registration");
   // DBCollection collection = db.getCollection("userDetails");
    BasicDBObject find = new BasicDBObject();
    find.put("_id", name);
    BasicDBObject update = new BasicDBObject();
    update.put("$inc", new BasicDBObject("seq", 1));
    DBObject obj =  col.findAndModify(find, update);
    return obj.get("seq");
}
    
    public void updateUser(Registration p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        this.col.update(query, RegistrationConverter.toDBObject(p));
    }
    
    public List<Registration> readAllUser() {
        List<Registration> data = new ArrayList<Registration>();
        DBCursor cursor = col.find();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            Registration p = RegistrationConverter.toRegistration(doc);
            data.add(p);
        }
        return data;
    }
    
    public boolean checkLogin(String userId, String pass){
        boolean isUserPresent = false;
        List<Registration> list = readAllUser();
        for(Registration model:list)
        {
            if(userId.equals(model.getUser_id()) && pass.equals(model.getPassword())){
                isUserPresent = true;
                break;
            }
        }
        return isUserPresent;
    }
    
    public boolean isUserPresent(String userId){
         boolean isUserPresent = false;
        List<Registration> list = readAllUser();
        for(Registration model:list)
        {
            if(userId.equals(model.getUser_id())){
                isUserPresent = true;
                break;
            }
        }
        return isUserPresent;
    }
    
    public void deleteUser(Registration p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        this.col.remove(query);
    }
    
    public Registration readUser(Registration p) {
        DBObject query = BasicDBObjectBuilder.start()
                .append("_id", new ObjectId(p.getId())).get();
        DBObject data = this.col.findOne(query);
        return RegistrationConverter.toRegistration(data);
    }
    
	 public String getNextID(){
       try {
           int id;
           List<Registration> list = readAllUser();
           if(list!=null){
               id = Integer.parseInt(list.get(list.size()).getid) + 1;
           }else{
               id = 1;

          }

          return String.valueOf(id);
       } catch (NumberFormatException e) {
           e.printStackTrace();
           return "1";
 
     }
 
 }
    }

    
    
//    public Registration readUser(){
//        DBObject document1 = new BasicDBObject("id", "abc");
//        DBObject document2 = new BasicDBObject("id", "xyz");
//
//        or.add(document1);
//        or.add(document2);
//
//        DBObject query = new BasicDBObject("$or", or);
//
//        DBCursor cur=db.getCollection("user").find(query);//user is the collection
//
//        while(cur.hasNext()){
//            System.out.println(cur.next());
//        }
//    }
    
