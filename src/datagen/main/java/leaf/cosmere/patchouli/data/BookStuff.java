/*
 * File updated ~ 2 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.patchouli.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import leaf.cosmere.api.text.StringHelper;

import java.util.Map;

//https://vazkiimods.github.io/Patchouli/docs/patchouli-basics/page-types
public class BookStuff
{
	public static class Category
	{
		public String name;
		public String description;
		public String icon;
		public String parent = "";
		public String flag = "";
		public Integer sortnum = 0;
		public boolean secret = false;

		public Category(String name, String description, String icon)
		{
			this.name = name.toLowerCase();
			this.description = description;
			this.icon = icon;
		}

		public JsonElement serialize()
		{
			JsonObject jsonobject = new JsonObject();

			jsonobject.addProperty("name", StringHelper.fixCapitalisation(this.name));//Convert to people readable text
			jsonobject.addProperty("description", this.description);
			jsonobject.addProperty("icon", this.icon);

			if (!this.parent.isEmpty())
			{
				jsonobject.addProperty("parent", this.parent);
			}
			if (!this.flag.isEmpty())
			{
				jsonobject.addProperty("flag", this.flag);
			}
			jsonobject.addProperty("sortnum", this.sortnum);
			jsonobject.addProperty("secret", this.secret);

			return jsonobject;
		}
	}

	public static class Entry
	{
		public String name;
		public Category category;
		public String icon;

		public Page[] pages;

		public String displayTitle = "";
		public String advancement = "";
		public String flag = "";

		public boolean priority = false;
		public boolean secret = false;
		public boolean read_by_default = false;
		public Integer sortnum = 0;

		public String turnin = ""; // advancement needed to mark entry complete

		//itemstack strings -> page 0-index num
		public Map<String, Integer> extra_recipe_mappings;

		public Entry()
		{
		}

		public Entry(String name, Category category)
		{
			this(name, category, category.icon);
		}

		public Entry(String name, Category category, String icon)
		{
			this.name = name.toLowerCase();
			this.category = category;
			this.icon = icon;
		}

		public JsonElement serialize(String modid)
		{
			JsonObject jsonobject = new JsonObject();

			//enforced
			final String displayTitleName = this.displayTitle.isEmpty() ? this.name : this.displayTitle;
			jsonobject.addProperty("name", StringHelper.fixCapitalisation(displayTitleName));//ensure people readable text
			jsonobject.addProperty("category", modid + ":" + this.category.name);
			jsonobject.addProperty("icon", this.icon);

			JsonArray jsonarray = new JsonArray();
			for (Page page : this.pages)
			{
				jsonarray.add(page.serialize());
			}
			jsonobject.add("pages", jsonarray);

			if (advancement != null && !this.advancement.isEmpty())
			{
				jsonobject.addProperty("advancement", this.advancement);
			}

			if (!this.flag.isEmpty())
			{
				jsonobject.addProperty("advancement", this.flag);
			}

			jsonobject.addProperty("priority", this.priority);
			jsonobject.addProperty("secret", this.secret);
			jsonobject.addProperty("read_by_default", this.read_by_default);

			jsonobject.addProperty("sortnum", this.sortnum);

			if (!this.turnin.isEmpty())
			{
				jsonobject.addProperty("turnin", this.turnin);
			}

			if (extra_recipe_mappings != null && !extra_recipe_mappings.isEmpty())
			{
				JsonObject extraRecipeMappingsArray = new JsonObject();
				for (Map.Entry<String, Integer> entry : this.extra_recipe_mappings.entrySet())
				{
					extraRecipeMappingsArray.addProperty(entry.getKey(), entry.getValue());
				}
				jsonobject.add("extra_recipe_mappings", extraRecipeMappingsArray);
			}


			return jsonobject;
		}

		public Entry setDisplayTitle(String s)
		{
			displayTitle = s;
			return this;
		}
	}

	public abstract static class Page
	{
		public String type;
		public String text;
		public String title = "";
		public String advancement = "";
		public String flag = "";
		public String anchor = "";
		public String[] recipes = null;

		public Page(String type)
		{
			this.type = type;
			this.text = "";
		}

		public Page(String type, String text)
		{
			this.type = type;
			this.text = text;
		}

		public Page(String type, String text, String title)
		{
			this.type = type;
			this.text = text;
			this.title = title;
		}

		public Page(String type, String text, String[] recipes)
		{
			this.type = type;
			this.text = text;
			this.recipes = recipes;
		}

		public JsonElement serialize()
		{
			JsonObject jsonObject = new JsonObject();

			addElement(jsonObject, "type", this.type);
			addElement(jsonObject, "text", this.text);
			addElement(jsonObject, "advancement", this.advancement);
			addElement(jsonObject, "title", this.title);

			if (recipes != null && recipes.length > 0)
			{
				JsonArray jsonArray = new JsonArray();
				for (String recipe : this.recipes)
				{
					jsonArray.add(recipe);
				}
				jsonObject.add("pages", jsonArray);
			}

			return jsonObject;
		}

		protected void addElement(JsonObject jsonObject, String key, String value)
		{
			if (!value.isEmpty())
			{
				jsonObject.addProperty(key, value);
			}
		}

		public Page setTitle(String title)
		{
			this.title = title;
			return this;
		}

		public Page setText(String s)
		{
			text = s;
			return this;
		}
	}

	public static class CraftingPage extends Page
	{
		public String recipe;
		public String recipe2 = "";

		public CraftingPage(String recipe)
		{
			super("patchouli:crafting", "");
			this.recipe = recipe;
		}

		public CraftingPage(String text, String recipe)
		{
			super("patchouli:crafting", text);
			this.recipe = recipe;
		}

		public CraftingPage(String text, String recipe, String recipe2)
		{
			super("patchouli:crafting", text);
			this.recipe = recipe;
			this.recipe2 = recipe2;
		}


		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			jsonObject.addProperty("recipe", this.recipe);
			if (!recipe2.equals(""))
			{
				//stop the logger complaining about invalid recipes oops
				jsonObject.addProperty("recipe2", this.recipe2);
			}


			return jsonObject;
		}
	}

	public static class EmptyPage extends Page
	{
		public boolean draw_filler = true;

		public EmptyPage()
		{
			super("empty", "");
		}

		public EmptyPage(boolean draw_filler)
		{
			super("empty", "");
			this.draw_filler = draw_filler;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			jsonObject.addProperty("draw_filler", draw_filler);

			return jsonObject;
		}
	}

	public static class EntityPage extends Page
	{
		public String name = "";
		public String entity = "";
		public float scale = 1;
		public float offset = 0;
		public boolean rotate = false;
		public float default_rotation = -45;

		public EntityPage(String entity)
		{
			super("entity", "");
			this.entity = entity;
		}

		public EntityPage(String text, String entity)
		{
			super("entity", text);
			this.entity = entity;
		}

		public EntityPage(String text, String name, String entity)
		{
			super("entity", text);
			this.name = name;
			this.entity = entity;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			addElement(jsonObject, "name", this.name);
			addElement(jsonObject, "entity", this.entity);
			jsonObject.addProperty("scale", this.scale);
			jsonObject.addProperty("offset", this.offset);
			jsonObject.addProperty("rotate", this.rotate);
			jsonObject.addProperty("default_rotation", this.default_rotation);

			return jsonObject;
		}
	}

	public static class ImagePage extends Page
	{
		public String[] images;
		public boolean border = false;

		public ImagePage(String text, String[] images)
		{
			super("image", text);
			this.images = images;
		}

		public ImagePage(String text, String title, String[] images)
		{
			super("image", text, title);
			this.images = images;
		}

		public ImagePage(String text, String title, boolean border, String[] images)
		{
			super("image", text, title);
			this.images = images;
			this.border = border;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			jsonObject.addProperty("border", this.border);

			if (images != null && images.length > 0)
			{
				JsonArray jsonarray = new JsonArray();
				for (String image : this.images)
				{
					jsonarray.add(image);
				}
				jsonObject.add("images", jsonarray);
			}

			return jsonObject;
		}
	}

	public static class QuestPage extends Page
	{
		public String trigger;

		public QuestPage(String text, String trigger)
		{
			super("quest", text);
			this.trigger = trigger;
		}

		public QuestPage(String text, String title, String trigger)
		{
			super("quest", text, title);
			this.trigger = trigger;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			addElement(jsonObject, "trigger", this.trigger);

			return jsonObject;
		}
	}

	public static class RelationsPage extends Page
	{
		public String[] entries;

		public RelationsPage(String text, String[] entries)
		{
			super("relations", text);
			this.entries = entries;
		}

		public RelationsPage(String text, String title, String[] entries)
		{
			super("relations", text, title);
			this.entries = entries;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			if (entries != null && entries.length > 0)
			{
				JsonArray jsonarray = new JsonArray();
				for (String entry : this.entries)
				{
					jsonarray.add(entry);
				}
				jsonObject.add("entries", jsonarray);
			}

			return jsonObject;
		}


	}

	public static class SmeltingPage extends Page
	{
		public String recipe;
		public String recipe2 = "";

		public SmeltingPage(String recipe)
		{
			super("smelting", "");
			this.recipe = recipe;
		}

		public SmeltingPage(String text, String recipe)
		{
			super("smelting", text);
			this.recipe = recipe;
		}

		public SmeltingPage(String text, String recipe, String recipe2)
		{
			super("smelting", text);
			this.recipe = recipe;
			this.recipe2 = recipe2;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			jsonObject.addProperty("recipe", this.recipe);
			jsonObject.addProperty("recipe2", this.recipe2);


			return jsonObject;
		}
	}

	public static class SpotlightPage extends Page
	{
		public String item;
		public boolean link_recipe = false;

		public SpotlightPage(String text, String item)
		{
			super("spotlight", text);
			this.item = item;
		}

		public SpotlightPage(String text, String title, String item)
		{
			super("spotlight", text, title);
			this.item = item;
		}

		@Override
		public JsonElement serialize()
		{
			JsonObject jsonObject = (JsonObject) super.serialize();

			jsonObject.addProperty("item", this.item);
			jsonObject.addProperty("link_recipe", this.link_recipe);

			return jsonObject;
		}
	}

	public static class TextPage extends Page
	{
		public TextPage()
		{
			super("text");
		}

		public TextPage(String text)
		{
			super("text", text);
		}

		public TextPage(String text, String title)
		{
			super("text", text, title);
		}
	}
}


