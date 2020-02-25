# RewardDaily

Well, this is a re-imagination of daily/random rewards type of plugins for
enterprise Minecraft servers based on Bukkit, Spigotmc, Papermc, etc...

It does exactly what it offers by it's name - custom giveouts on a daily login
bases, or for votes (with different set of gifts).

1. Giveouts based on a calendar day
2. It is very simple
3. Version-proof (the plugin itself)
4. The configuration is (almost) straightforward.
5. Separate set of giveouts per daily, and per votes (/rewardvote)

## Motivation

First of all, the voting/rewarding sites/plugins are based on a
cool-down type of 24 hours period.
They force the players to shifting into a 3 AM eternal state soon.

e.g. on Monday you've voted at 10:00 PM, then on Tuesday
you have to vote at 10:00 PM + a couple of minutes or so...
Therefore, soon you will be drifted into the 3 AM eternal.

This plugin presents a 24 hours (one day) time frame
from 00:00:00 to 23:59:59, allowing you to log in and
gain rewards once a day - at any time within the frame.
(there is UTC timescale at the [reference server](http://yeuy.wtf))

Much comfortable for the players, isn't it?

Also, depending on the number of your votifier sites, you
can weight the rewards for the votes accordingly.
The default configuration assumes 8 votifier sites, giving a
chanche of 1 diamond against 7 emeralds.

## Installation

Put rewarddaily.jar into your server's plugin directory and restart.
It will serve the default gifts.

## Configuration

There is a single configuration file to specify the gifts.
It can be very long - several thousands of lines or so,
without notable memory leak.
Each line presents a gift with equal probability to the others.
A Player will be rewarded with a gift randomly selected from
this config.yml.
The plugin default seems BUKKIT version proof, and gives you 
quite basic stuff, like:

```
daily:
 reward:
  - cake 1
  - apple 1
  - apple 1
  - apple 1
  - carrot 2
  - melon 2
  - bread 2
  - iron_axe 1
  - iron_sword 1
  - iron_pickaxe 1
  - iron_shovel 1
vote:
 reward:
  - emerald 1
  - emerald 1
  - emerald 1
  - emerald 1
  - emerald 1
  - emerald 1
  - emerald 1
  - diamond 1
```

One can found more complex and exhaustive configuration examples in the
[GitHub Repository](https://github.com/meres10/RewardDaily).
Of curse, they are version dependent (check the Hints section below).

## Commands

/rewarddaily Good_Pal
/rewardvote  Good_Pal

where Good_pal can be the player's IGN, or:

* @p for command based automation (vanilla target selectors @p,@r,@a).
* %player% for plugins using placeholder API like SuperbVote does.
* or other plugin's custom target method.

## Permission nodes

There is one:

```
rewarddaily.force
```

Intended for operators only, by default.
(although moderators might have this to reward Good_Pals)

## Impacts

 - Dependency:     None (does not rely on any other plugins)
 - Server Version: Bukkit API versions 1.8.3 - 1.15.2 (maybe --- ... +++)
 - Server Startup: None (no notable delay on startup)
 - Server Memory:  Mnimal (depends on config.yml, even up to 10K lines)
 - Server CPU:     None (no impact by it's nature)
 - Client join:    Minimal (no notable delay on joining, 1K lines config)
 - Client FPS:     None (no impact by it's nature)

Note: 
This plugin versioning system reflects the latest tested BUKKIT version.
It is high likely that it works well on older, or even newer platforms.

## Hints

### Online generators

One might use online giveout generators (as of 2020.02.24):

* [Basic 1.12..1.15](https://www.gamergeeks.nz/apps/minecraft/give-command-generator)
* [Complex 1.8..1.15](https://www.digminecraft.com/generators/)
* [Old <= 1.12](https://minecraftcommand.science/custom-item-generator)

### Vote listener example

This plugin can cooperate with [SuperbVote](https://github.com/astei/SuperbVote):

```
rewards:
  - if:
      default: true
    commands:
    - rewardvote %player%
```
FYI @github.com/astei

## TODO

 - Extending the pool of gifts with banners, shields, fireworks, etc...
 - Involving (and testing) other vote listeners.
 - Adding "/rewardcommand" pool - a free-style/dangerous command executor.

Contributors are welcome!
Pull requests are welcome aswell, except over-sophisticated ones.
**Keep It Simple Stupid!**
[Check out The KISS principle](https://en.wikipedia.org/wiki/KISS_principle)

## Footnotes

That's all Folk's for now!

One might try this out at yeuy.wtf in action!
(also check my [server site](http://yeuy.wtf)).

This is my first genuine contribution to the github, and
there might be more to come (hopefully - stay tuned).
Please alert me if I'am breaking any github conventions.
(like my 5 years old bukkit.jar binary blob)

This repo runs under the terms of the MIT licence with a Beerware attribute:
"If you find my work useful, toss me a beer, or a whisky here:"
[![alt text](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=W3AA5AK542482&source=url)

Regards,
Meres10 
