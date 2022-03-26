The Cosmere Project - For 1.16 Minecraft

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
