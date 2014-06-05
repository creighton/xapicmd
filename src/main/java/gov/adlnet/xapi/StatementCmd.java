package gov.adlnet.xapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import gov.adlnet.xapi.model.adapters.ActorAdapter;
import gov.adlnet.xapi.model.adapters.StatementObjectAdapter;
import asg.cliche.Command;
import asg.cliche.Param;

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
   
   @Command(name="get", description="Make a GET request for statements")
   public String doGet() throws IOException{
      return doGet(new String[]{});
   }
   
   @Command(name="getstmt", description="Make a GET request for statements")
   public String doGet(
         @Param(name="statementid", description="The GUID of the statement you are requesting")
         String stmtid) throws IOException{
      return getStatementVal(client.get(stmtid));
   }

   @Command(name="get", description="Make a GET request for statements")
   public String doGet(
         @Param(name="filters", description="key=value pairs of space delimited filters to add to the get request")
         String... filters) throws IOException{
      StatementClient currentclient = addFilters(client, filters);
      StatementResult results = currentclient.getStatements();
      StringBuilder sb = new StringBuilder();
      for (Statement s : results.getStatements()) {
         sb.append(getStatementVal(s) + "\n");
      }
      if (results.hasMore()) sb.append("... there are more statements\n");
      return sb.toString();
   }
   
   @Command(name="send", description="Send a statement to the LRS")
   public String doSend(
         @Param(name="statement", description="the json statement")
         String stmtstr) throws UnsupportedEncodingException, IOException {
      Statement stmt = getStatement(stmtstr);
      return client.publishStatement(stmt);
   }
   
   private Statement getStatement(String stmtstr) {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(IStatementObject.class, new StatementObjectAdapter());
      builder.registerTypeAdapter(Actor.class, new ActorAdapter());
      Gson gson = builder.create();
      return (Statement)gson.fromJson(stmtstr, Statement.class);
   }

   private StatementClient addFilters(StatementClient c, String[] filters) {
      StatementClient ret = client.exact(); // default.. just doing so i have a local config'd client
      for (String f : filters) {
         if (f.indexOf("=") < 0) continue;
         String[] kv = f.split("=");
         if ("agent".equals(kv[0])) ret = ret.filterByActor(getActor(kv[1]));
         else if ("verb".equals(kv[0])) ret = ret.filterByVerb(kv[1]);
         else if ("activity".equals(kv[0])) ret = ret.filterByActivity(kv[1]);
         else if ("related_agents".equals(kv[0])) ret = ret.includeRelatedAgents(Boolean.valueOf(kv[1]));
         else if ("related_activities".equals(kv[0])) ret = ret.includeRelatedActivities(Boolean.parseBoolean(kv[1]));
      }
      return ret;
   }
   
   private Actor getActor(String actstr){
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Actor.class, new ActorAdapter());
      Gson gson = builder.create();
      return gson.fromJson(actstr, Agent.class);
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
