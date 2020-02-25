#*
#* Makefile for a single source java compilation
#*
#* Meres Ten <meres10 at yeuy.eu>
#*
#*----------------------------------------------------------------------------
MAIN=RewardDaily
PROJECT=rewarddaily
AUTH=meres
DOM=com
#*
BASEDIR=$(DOM)/$(AUTH)/$(PROJECT)
BASECP=$(DOM).$(AUTH).$(PROJECT)
#*
#* Toolchain - adjust according to your JDK deployment
#*----------------------------------------------------------------------------
#*
JDKROOT=/opt/jdk1.8.0_51
JC=$(JDKROOT)/bin/javac
JAR=$(JDKROOT)/bin/jar
#*
#* Includes and libraries - adjust to actual BUKKIT/SPIGOT/PAPER version
#* (Beat me, but I've included an ancient bukkit version - still working)
#*
CLASSPATH=lib/bukkit.jar:./
#*
#* Options:
#* ATM only "getServer().getPlayerExact" is deprecated, but it is OK for me.
#* I am using "oneuuid" plugin to stick players to their joined names/UUID.
#*
JFLAGS=-classpath $(CLASSPATH) -Xlint:deprecation
JARFLAGS=

#*
#* General rule to compile JAVA
#*
%.class:   %.java
	$(JC) $(JFLAGS) $<

# Targets and components to build
# ----------------------------------------------------------------------------
ASSETS=plugin.yml config.yml
TARGET=$(PROJECT).jar
OBJS=$(BASEDIR)/$(MAIN).class     

$(TARGET): $(OBJS)
	$(JAR) $(JARFLAGS) cf $(TARGET) -C ./ $(OBJS) $(ASSETS)

#
#* Install plugin on your target Minecraft server (adjust it aswell!)
# ----------------------------------------------------------------------------
install: $(TARGET)
	scp $(TARGET) you@mc.your.tld:/home/you/mcserver/plugins/.

clean:
	rm -f $(TARGET) $(OBJS)
	find . -name '*.bak' -exec rm '{}' '+'

spotless:	clean

