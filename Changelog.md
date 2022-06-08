The Cosmere Project - For 1.16 Minecraft

B44

Fix tapping gold only healing one hp regardless of tap amount
Crafting filled vials now take into account how much metal was in the vial before.
Copper/Bronze allomancy checks strength
Fix nicrosil crash
Tin store now affects render distance.
Brass store has reduceable fire damage
Fix some menu radial button icon overlap
Lerasatium spikes get all powers at 50% of donors strength.
Hide cosmere curios when invisible potion effect applied
Fix F-Zinc being inverted.
Spawned mob metalborn have ferring/misting names if they only have one power
Fix deactivate manifestations not clearing modes on client
Strength in metal dependent on how you got it. Misting/twinborn are slightly stronger than full feruchemist/mistborn. God metal granting powers grant 'ancient' strength.
A-Bronze now competes against copper cloud effect in order to make entities and see information in hwyla.
Mode increase/decrease affected by shift/ctrl.
Wearing gold metalminds/spikes makes piglins neutral.


B43

Allomantic tin now has does incremental night vision improvement, rather than doing the potion effect.
HWYLA limited to bronze allomancy and what's active
Fix feruchemy bronze not storing to metalmind.
Fixed a bug where manifestation strength was incorrect
Made it so Feruchemical strength actually matters. Dictates how much you can store/tap.
Fix feruchemical iron to increase gravity.
Fix bands of mourning creative item not granting powers


B42

Fix icons not rendering when certain other mods are installed.
Fix metalminds from loot chests incorrectly listing themselves as unsealed instead of unkeyed.


B41

Changed compound amount to be dependent on allomantic strength and mode.
Tidied up player clone code for when player dies or returns from the end.
Feruchemical copper now stores full levels, rather than xp points.
Updated cosmere to build against forge version 40.1.30
Renamed lynch to linch.
Spikes available in more chests.
Fix creative mode lerasium spikes containing powers, they've been moved to lerasatium spikes.
Clear iron/steel push pull list when turning off powers.
Fix crash from trying to use the guide book when patchouli is not installed.


B40

Curio spike and bracelet rendering again! Yay :D 
Allomancy boost fix. Was accidentally making weaker instead of stronger.
Allow tapping aluminum at a larger rate, so it's faster to drain and re-use.
Fix spikes not auto equipping from mods like corail tombstone.
Adding more stuff to the default iron/steel whitelist


B39

Fix hemalurgic spikes not stealing powers properly


B38

Make allo bendalloy only slow people around you
fix feru gold storing not working when health was full.
fix feru bendalloy for real this time. 
Better menu handling for over 16 powers
Better menu information for what powers are active.
fix mobs getting reinitialized on being reloaded, gaining powers when they shouldn't.
fix metalworking table not dropping when broken
fix hemalurgy not actually taking powers from player entities.


B37

Buffed compounding attribute gain amount.
Added death by spike localisation.
Fixed mod blocks being slow to mine. Should be faster with correct tool.
Fixed tapping1 gold not doing anything.
Fixed storing gold not updating health properly.
Fixed feru gold removing from metalmind even when fully healthy
Made feru gold reduce harmful effect timer each time gold tap effect ticks.
Fixed a bunch of effects not running at tap 1 or two
Fixed feru bendalloy not giving saturation.


B36

Fixed hemalurgic spikes constantly damaging when used as feruchemical storage.
Fixed aluminum feruchemy so it can't make unkeyed metalminds.
Some progress towards fixing nicrosil metalminds.
Some set up for proper advancements. Currently not triggering correctly. Does not have correct localisation.
Attempts at making metal traders not so imbalanced.

B35

Fix commands not working on server.
Fixed bug that crashed players trying to use the coin pouch or allomantic iron/steel.

B34

Fixed server crashing due to Cosmere. Duralumin feruchemy was trying to run client side code on the server, for hiding a nameplate while storing oops

B33

Coinshots can now shoot coins! Crouch and right click a coin pouch to open pouch inventory and add metal nuggets. 
Then hold your allomancy push keybind and use (right click) the coin pouch. You'll shoot out a projectile that you'll automatically be pushing against.
If you have allomantic iron, you can pull those projectiles directly back into your coin pouch or inventory.

B32

Fix feruchemy not working with completely empty metalminds.
Fix bands of mourning, storing when full caused it to overflow and start from 0 again.

B31

Tapping feruchemical bronze will reset time since sleep timer.
Fixing bug that caused powers not to persist through death.
Lerasium spikes now steal attributes, like they are supposed to. Lerasatium spikes steal all powers.
Decreased spiritweb nbt save size by a bunch.
Better spike tooltips. 
Iron/Steel tries to approximate which non-mod items and entities contain metal.
Iron/Steel can push and pull mob and item entities.
Allomancy mode lists off, burning and flaring modes when selected power.

B30

Fixed brass feruchemy not letting player set others on fire at high burn rates.
Buffed electrum feruchemy to be more noticeable. 
Changed copper allomancy effect name from "Burning Copper", to "Copper Cloud".
Fixed and updated patchouli documentation. Hemalurgy now has better descriptions.
Fixed cosmere fortune bonus.
Fix looting event crash when there's a null damage source.
Make Cosmere's ores half as common, because they were ridiculously common.


B29

Fixing deepslate ore, no more pink texture or missing name. Also potentially better ore spawn frequency so it actually appears in deepslate.

B28

First build on 1.18.2. Spike/Metalmind rendering temporarily broken. Potentially other things too

B27

Fixed crash related to bronze allomancy checking if an entity should be glowing

B26

This build contains breaking changes. Players, metalminds and spikes will not load related powers that were saved to them.
Removed accidental character left in village_blacksmith loot table json.
Hemalurgic Tin, Copper, Chromium, Aluminum spikes implemented.
Added Bands Of Mourning creative item. Grants allomancy & feruchemy.
Fixed metal vial amounts. Made nuggets annoying to eat, to encourage vial use.
Prep work for Roshar.
Gem stone item added (not yet available via world gen). Gemstones charge when outside in thunderstorms. Charge faster when not in player inventory.


B25
Fixed some loot chests not being injected into
Implemented ability for partially charged unsealed metalminds to be found in loot chests.
Updated metal trader potential trades, now sets costs, amounts, trades per day and xp per trade based on item rarity.
Updated a lot of loot chest loot. Look for hemalurgically charged spikes, invested metalminds and atium/lerasium beads and raw ore in various chests. Still checking that I've got the loot tables working correctly, but I was able to get things to show up rarely in chests.



B24

Fixed creative mode spikes to only contain their correct powers, not both allomancy and feruchemy.
Added localisation for spike tooltips. No more 'cosmere.trueself'.
Non-scroll wheel based power mode increase/decrease
Wandering trader and several Illager types added to list of potential metalborn mobs.
Boosted ore spawn rate. Was closer to diamond, now is near iron levels of distribution
Fixed some item tagging, so raw ore items from other mods should be more compatible.
Fixed iron spike tool tip so it doesn't list empty power. Now lists attack damage granted.
Updated a lot of the loot chest tables to have a chance of adding items from Cosmere, like ores in the mineshaft or lerasium nuggets in rarer loot chests.

B23

Duralumin feruchemy tapping gives Hero of the Village effect.
Fixed a crash related to bronze feruchemy. Player can now sleep at night anywhere properly.
Cleaned up a bunch of scripts.
Swapped the bendalloy and cadmium effects, so cadmium speeds up things around you, implying you're really slow and bendalloy slows things around you, implying that you're faster.

B22

Remove curse of binding from spikes. 
Fix bracelet curio render location. 
Consume minecraft:iron/gold nuggets. 
Lerasium & Lerasatium can be consumed to gain powers. 
Added missing localisation for hemalurgic spike creative mode tab. 
Fixed bronze allomancy glow effect.


B21

Fixed Mixin related crash

B20

Localisation should now use "manifestation type - metal type" instead of the name of the manifestation for allomancy and feruchemy. Eg "Feruchemical Steel" instead of Steelrunner.
Fixed some balancing of the metalminds, so that it's 1000 capacity per ingot used to make it.

B19

The creative mode menu now has a more comprehensive list of hemalurgic spikes with the abilities that they can steal
Remove our own Gold/Iron Nuggets from being created.
Added metalmind descriptions to JEI/NEI
Stop the game crashing when patchouli isn't installed, oops
Shortened allomancy guide book listings, as it doesn't create extra pages for you if you throw too much at it.
Use pewter nugget for allomancy icon in guide book for now. Was metal vial which has no icon yet.
Made iron pulling quite a bit stronger (0.064, was 0.008)

B18

Bumped forge version to 36.2.0
Changed Gold feruchemy to have lower hearts when storing and then regen-like effect when tapping.
Prep for cosmere related loot to start showing up in chests
Making allomancy last longer than a few seconds per bead. now lasts between 60-180 seconds. Easier for testing till we adjust burn rates by metal.
Fixed a bug in logic for hemalurgy spikes being used as metalminds when equipped. No more constant damage every time the item nbt updates!
Fixed a bug where allomancy users would gain more metals when burning instead of less. oops.
Fixed a bug in feruchemy where the game would stop trying to store attributes in the next metalmind when the first one was full.
Fixing a bug with chromium allomancy causing a full crash of the game oops.


B17

Added a creative mode only shard blade to test a model that was created using blockbench. 
Fixing some bugs related to soothing and rioting. May have to look into updating the entity goals, since it doesn't seem to affect passive mobs
Initial framework for patchouli datagen provider. Wont have to make a couple hundred json completely by hand anymore
Initial framework for advancement datagen provider. 
Visual updates to ring/bracelet png.

B16

Auto select a power if none are selected. Only really relevant for first time joining world.
Fixed crash from mousing over creative debug hemalurgy spike that gives all powers in inventory.
Spikes now show up in more varied locations based on curio slot.
Remove key bind check on most allomancy


B15

Adding nuggets to bottles/vials that can then be consumed.
Iron/steel sight. Steel pushing set up. Iron pushing not yet implemented.
Starting to use allomantic/feruchemical strength that the user either starts with or steals with feruchemy.
Duralumin/Nicrosil initial framework set up, though untested.
Fixed a bug where player powers were not persisting across death.
Big spiritweb menu refactor, now I actually understand what's going on there.

B14

Adding illagers and witches to possible powered individuals.
Adding electrum feruchemy. Not sure how happy I am with it. Reduces/increases damage taken a small amount. Any suggestions on what storing/tapping determination could do would be appreciated.
Have also removed annoying mode change message and updated the debug text so it says selected power and what mode the selected power is in.
Big refactor to convert to using attributes instead of capability to track what powers you currently have access to.

B13

All feruchemy basically done except for nicrosil and electrum.
Players are twinborn at a minimum, or they are granted either full allomancy or full feruchemy.
Fine tuned feruchemical bronze storing, so it doesn’t spam chat with sleep messages.
Fine tuned brass feruchemy, so that tapping brass actually does something. Sets others on fire if tapping enough when attacking. Sets self on fire if tapping too much.

B12

Following 1.17 style of ore blocks dropping raw ore, rather than the block itself.
Raw ore is affected by fortune.
Alloys are made out of raw ore for the most part. Recipes are visible in the JEI mod.
Fixed up tin feruchemy so it provides zoom instead of night vision.
Fixed night vision in tin allomancy so it doesn’t spam flash the screen on low night vision timer left.

B11

World generation done! You can find the new ores out in the world now.
Zinc feruchemy gives more experience when tapping, less experience when storing.
Gold feruchemy acts more like poison and regeneration, except the reduced health is dependent on how much health you are storing.
Storing identity with duralumin will hide your nameplate if you’re storing enough. Visibility to mobs is also dependent on how much you’re storing

B9

Ore blocks exist but not generating in the world

B8 and below.

Initial commit, never kept track of what the build had then really.
Some feruchemy,
basic gui,
swallowing metal for allomancy etc.
Hemalurgy initial pass too.
Compounding also is in there.
