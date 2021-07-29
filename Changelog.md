The Cosmere Project - For 1.16 Minecraft

B18

Changed Gold feruchemy to have lower hearts when storing and then regen-like effect when tapping.
Fixed a bug in logic for hemalurgy spikes being used as metalminds when equipped. No more constant damage every time the item nbt updates!
Fixed a bug where allomancy users would gain more metals when burning instead of less. oops.
fixed a bug in feruchemy where the game would stop trying to store attributes in more metalminds when the first one was full.

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
