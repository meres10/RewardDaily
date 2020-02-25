/*..................................................... RewardDaily.java .... */
package com.meres.rewarddaily;                                                    
/*                                                                            */
/*  RewardDaily.java - Minecraft server plugin that gives random gifts        */
/*                     to our players daily (and/or for votes)                */
/*                                                                            */
/*  Application - Minecraft Bukkit server/variants, plugin section            */
/*                                                                            */
/*  Created:    2017.12.07  -  by Meres Ten (M10) for 1.x.x servers           */
/*  Modified:   2017.12.07  -  First approach                                 */
/*  Modified:   2020.02.22  -  Added rewardvote, and some cleanup/clearance   */
/*                                                                            */
/*  Command Usage: /rewarddaily [player]  (gives unconditional daily reward)  */
/*                 /rewardvote  [player]  (giveouts for votes by command)     */
/*                                                                            */
/*  Impacts:                                                                  */
/*          - Server Version: Bukkit versions 1.8.3 - 1.15.2 (maybe +++)      */
/*                            (bulletproof by it's nature, except config.yml) */
/*          - Server Memory:  minimal (depends on config.yml, up to 10K lines)*/
/*          - Server Startup: None (no notable delay on startup)              */
/*          - Server CPU:     None (no impact by it's nature)                 */
/*          - Dependency:     None (does not rely on any other plugins)       */
/*          - Game Rules:     None (no impact by it's nature)                 */
/*          - Client Side:    None (no impact by it's nature)                 */
/*                                                                            */
/*  @author meres10                                                           */
/*  Disclaimer: (C) meres10 at yeuy.eu 2020                                   */
/*              under the terms of MIT License with Beerware attribute.       */
/**/
/* - Imports */
/**/
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**/
import org.bukkit.Bukkit;    
import org.bukkit.Server;    
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player; 
import org.bukkit.event.player.PlayerJoinEvent;
/**/
/* - Main plugin class. Register this in plugin.yml as follows:               */
/*        name: RewardDaily                                                   */
/*        main: com.meres.rewarddaily.RewardDaily                             */
/**/
/* ========================================================================== */
public class RewardDaily extends JavaPlugin implements Listener
/* ========================================================================== */
{
private final Server                   server    = getServer();
private final PluginManager            pm        = server.getPluginManager();
private final String                   luckyfn   = "plugins/RewardDaily/lucky.yml";
private       HashMap<UUID, String>    luckyguys = new HashMap<UUID, String>();
private       Random                   rand      = new Random(System.currentTimeMillis());
private       int                      today     = -1;
private       PluginDescriptionFile    pdfFile;
private       List<String>             reward_list_daily;
private       String[]                 reward_daily;
private       List<String>             reward_list_vote;
private       String[]                 reward_vote;
private       File                     luckyFile;
/* - Some helper functions */
private void conout( String string ) { this.getLogger().info(string); }
private void concmd( String string ) { server.dispatchCommand( server.getConsoleSender(), string ); }
/**/
/* - The main stuff */
/**/
/*                    -------- */
@Override public void onEnable() 
/*                    -------- */
   {
   int i = 0;
   pm.registerEvents( this, this );
   this.saveDefaultConfig();
   pdfFile           = this.getDescription();
/* Pool of Daily rewards */
   reward_list_daily = this.getConfig().getStringList("daily.reward");
   reward_daily      = reward_list_daily.toArray(new String[reward_list_daily.size()]);
   conout( pdfFile.getName() + " version " + pdfFile.getVersion() + ", " + reward_daily.length + " daily rewards loaded!" );
/* Pool of Rewards for Votes */
   reward_list_vote  = this.getConfig().getStringList("vote.reward");
   reward_vote       = reward_list_vote.toArray(new String[reward_list_vote.size()]);
   conout( pdfFile.getName() + " version " + pdfFile.getVersion() + ", " + reward_vote.length + " voting rewards loaded!" );
/**/
   try   
     {
     Scanner scanlines = new Scanner( new File( luckyfn ) );
/*   Read timestamp and UUID's line by line to add lucky guys to daily .... */
     if( scanlines.hasNextLine() )
       {
       today = Integer.parseInt(scanlines.nextLine());
       while( scanlines.hasNextLine() )
          {
          luckyguys.put( UUID.fromString( scanlines.nextLine() ), "daily" );
          i++;
          }
       }
     conout( i + " lucky guys were found..." );
     }
   catch( FileNotFoundException e )
     {
     conout( "...not a single lucky guy was found..." );
     }
   }
/**/
/*                    -------- */
@Override public void onDisable()
/*                    -------- */
   {
   try
     {
     FileWriter out  = new FileWriter( luckyfn );
     out.write( Integer.toString(today) + "\n" );
     for( UUID uuid : luckyguys.keySet() ) out.write( uuid.toString() + "\n" );
     out.close();
     }
   catch ( IOException e )
     {
     /* TBD... */
     }
   conout("disabled, " + luckyguys.keySet().size() + " lucky guys were saved..." );
   }
/**/
/*           ---------- */
private void checkDaily()
/*           ---------- */
{
LocalDateTime  date = LocalDateTime.now();
int tnow = date.getDayOfYear();
if( tnow != today )  /* Fire all daily triggers */
  {
  HashMap<UUID, String> guysgone = luckyguys;  /* Send to garbage collector */
  luckyguys = new HashMap<UUID, String>();     /* Here is the new one...    */
  try                                          /* Remove daily persistence  */
    {
    File todel = new File( luckyfn );
    todel.delete();
    }
  catch ( Exception e )
    {
    /* TBD... */
    }
  today = tnow;
  }
}
/**/
/* - See if a player is lucky or not */
/**/
/*             -------- */
public boolean isLucky( UUID uuid, String category )
/*             -------- */
   {
   if( luckyguys.containsKey( uuid ) ) return true;
   return false;
   }
/**/
/* - Set player lucky */
/**/
/*          --------- */
public void setLucky( Player player, String category )
/*          --------- */
   {
   luckyguys.put( player.getUniqueId(), category );
   }
/**/
/* - Give out the gift */
/**/
/*          ------- */
public void giveOut( Player player, String category )
/*          ------- */
   {
   int i; /* Index to random goodies */
   if( player.getInventory().firstEmpty() == -1 )
     {
     player.sendMessage( ChatColor.RED + "Hard luck! There is no room for your gift..." );
     }
   else
     {
     player.sendMessage( ChatColor.GREEN + "Surprise! Check out your inventory for your "+category+" gifts!" );
/*   See if it is a reward for a vote - using old java method */
     if( category.equals("vote") )
       {
       i = rand.nextInt(reward_vote.length);
       concmd( "give " + player.getName() + " " + reward_vote[i] );
       }
     else  /* Default is daily */
       {
       i = rand.nextInt(reward_daily.length);
       concmd( "give " + player.getName() + " " + reward_daily[i] );
       setLucky( player, "daily" );
       }
     }
   }
/**/
/* - Command handler for /reward{daily,vote} */
/**/
/*             --------- */
public boolean onCommand( CommandSender sender, Command command, String label, String[] args )
/*             --------- */
   {
   Player   player      = null;
   Player   toMyPal     = null;
   String   commandName = command.getName().toLowerCase();
   String   aGoodie     = "daily";                          /* Default set of goodies = daily  */
   if( commandName.equals("rewardvote") ) aGoodie = "vote"; /* Switch to voting set of goodies */
/* or something else, like "rewardcommand" - TODO! */
/**/
   if( sender instanceof Player ) player = (Player) sender; 
   if( args.length == 0 ) /* Without player argument: surprise me! :-) */
     {
     if( player == null )
       {
       /* Issued from the server console without player specified */
       sender.sendMessage( ChatColor.RED + "Must be in game for do that!" );
       return false;
       }
     if( player.hasPermission( "rewarddaily.force" ) ) toMyPal = player;
     }
   else  /* With a player argument - toMyPal */
     {
     if( player != null && !player.hasPermission("rewarddaily.force") ) return false;
     toMyPal = getServer().getPlayerExact( args[0] );
     if( toMyPal != null )
         sender.sendMessage(ChatColor.GREEN + "You made " + args[0] + " lucky.");
     else
         sender.sendMessage(ChatColor.RED + "Player " + args[0] + " is unkown!");
     }
   if( toMyPal == null )   return false;
   /* Surprise someone */ 
   giveOut( toMyPal, aGoodie );
   return true;
   }
/**/
/* - Handle players join for Rewards Daily */
/**/
/*                        ---------- */
@EventHandler public void playerJoin(PlayerJoinEvent event) 
/*                        ---------- */
   {
   Player player = event.getPlayer();
   checkDaily();
   if( isLucky( player.getUniqueId(), "daily" ) ) return;
   giveOut( player, "daily" );
   }
}
/*.................. END OF ........................... RewardDaily.java .... */
