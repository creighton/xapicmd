package gov.adlnet.xapi;

import gov.adlnet.xapi.client.StatementClient;

import java.io.IOException;
import java.net.MalformedURLException;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellDependent;
import asg.cliche.ShellFactory;
import asg.cliche.ShellManageable;

public class XAPICmd implements ShellDependent, ShellManageable {
   private Shell thisShell;
   
   private StatementClient client; 
   
   /**
    * Creates a shell with default configurations 
    * @throws MalformedURLException
    */
   public XAPICmd() throws MalformedURLException{
      client = new StatementClient("https://lrs.adlnet.gov/xAPI/", 
            "tom", "1234");
   }
   
   /**
    * Sends user to the statements subcommand shell to interact
    * with the statement api
    * @throws IOException
    */
   @Command
   public void statement() throws IOException{
      ShellFactory.createSubshell("statements", thisShell, "Statements", 
            new StatementCmd(client)).commandLoop();
   }
   
   /**
    * Configures the xAPI Statement Client
    * @param url Endpoint of the LRS
    * @param username Username used to authenticate with the LRS server
    * @param password Password used to authenticate with the LRS server
    */
   @Command(description="Configure the xAPI settings for your LRS")
   public void config(
         @Param(name="endpoint", description="Endpoint of the LRS API")
         String url, 
         @Param(name="username", description="Client username used to authenticate to the LRS")
         String username, 
         @Param(name="password", description="Client password used to authenticate to the LRS")
         String password){
      try{
         client = new StatementClient(url, username, password);
      } catch (MalformedURLException mue) {
         System.out.println("Malformed URL");
      }
   }
   

   /* (non-Javadoc)
    * @see asg.cliche.ShellDependent#cliSetShell(asg.cliche.Shell)
    */
   public void cliSetShell(Shell shell) {
      thisShell = shell;
   }

   /* (non-Javadoc)
    * @see asg.cliche.ShellManageable#cliEnterLoop()
    */
   public void cliEnterLoop() {
      System.out.println("Welcome to the XAPICmd tool. "
            + "Type '?list' for a list of commands.");
   }

   /* (non-Javadoc)
    * @see asg.cliche.ShellManageable#cliLeaveLoop()
    */
   public void cliLeaveLoop() {
      System.out.println("Exiting XAPICmd tool.");
   }

   /**
    * Main.. starts the console app
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException{
      ShellFactory.createConsoleShell("xapi", "xAPI Commands", 
            new XAPICmd()).commandLoop();
   }
}
