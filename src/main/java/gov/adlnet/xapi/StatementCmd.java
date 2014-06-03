package gov.adlnet.xapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import gov.adlnet.xapi.client.StatementClient;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Group;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementReference;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.SubStatement;
import gov.adlnet.xapi.model.Verb;
import asg.cliche.Command;

public class StatementCmd {
   private StatementClient client;
   
   /**
    * Creates the statement api shell
    * @param client The configured client jxapi to use to 
    * connect to an LRS 
    */
   public StatementCmd(StatementClient client){
      this.client = client;
   }

   /**
    * Makes a GET request to the LRS
    * @return the result
    * @throws IOException 
    */
   @Command(name="get", description="Make a GET request for statements")
   public String doGet() throws IOException{
      StatementResult results = client.getStatements();
      StringBuilder sb = new StringBuilder();
      for (Statement s : results.getStatements()) {
         sb.append(getStatementVal(s) + "\n");
      }
      if (results.hasMore()) sb.append("... there are more statements\n");
      return sb.toString();
   }
   
   private String getStatementVal(Statement s){
      return s.getId() + ": " + 
            getActorVal(s.getActor()) + " " +
            getVerbVal(s.getVerb()) + " " + 
            getObjectVal(s.getObject());
   }
   
   private String getActorVal(Actor a){
      String ret = "";
      if (a.getName() != null) ret = a.getName();
      else if (a.getMbox() != null) ret = a.getMbox();
      else if (a.getMbox_sha1sum() != null) ret = a.getMbox_sha1sum();
      else if (a.getOpenid() != null) ret = a.getOpenid().toString();
      else if (a.getAccount() != null) ret = a.getAccount().getName();
      else if (a.getObjectType() == Group.GROUP) ret = getGroupMemberVal((Group)a);
      return ret;
   }
   
   private String getGroupMemberVal(Group g){
      ArrayList<String> members = new ArrayList<String>(g.getMember().size());
      for (Agent a : g.getMember()) {
         members.add(getActorVal(a));
      }
      return "Members: " + members.toString();
   }

   private String getVerbVal(Verb verb) {
      String ret = "";
      if (verb.getDisplay() != null){
         ret = getFromLangDict(verb.getDisplay());
      }
      if (ret == null || ret.isEmpty()) ret = verb.getId();
      return ret;
   }
   
   private String getObjectVal(IStatementObject obj) {
      String ret = "";
      String type = obj.getObjectType();
      if (Activity.ACTIVITY.equals(type)) ret = getActivityVal((Activity)obj);
      else if (Agent.AGENT.equals(type)) ret = getActorVal((Actor)obj);
      else if (StatementReference.STATEMENT_REFERENCE.equals(type)) ret = ((StatementReference)obj).getId();
      else if (SubStatement.SUB_STATEMENT.equals(type)) ret = getSubStatementVal((SubStatement)obj);
      return ret;
   }
   
   private String getSubStatementVal(SubStatement sub) {
      return "SubStatement: " + getActorVal(sub.getActor()) + " " +
            getVerbVal(sub.getVerb()) + " " + 
            getObjectVal(sub.getObject());
   }
   
   private String getActivityVal(Activity act) {
      String ret = "";
      if (act.getDefinition() != null) {
         ret = getFromLangDict(act.getDefinition().getName());
      }
      if (ret == null || ret.isEmpty()){
         ret = act.getId();
      }
      return ret;
   }
   
   private String getFromLangDict(HashMap<String, String> map){
      String ret = "";
      if (map != null){
         if (map.values().size() > 1) {
            ret = (map.get("en-US") != null)?map.get("en-US"):map.get("en");
            if (ret == null){
               ret = map.values().toArray(new String[]{})[0];
            }
         }
         else {
            ret = map.values().toArray(new String[]{})[0];
         }
      }
      return ret;
   }
}
