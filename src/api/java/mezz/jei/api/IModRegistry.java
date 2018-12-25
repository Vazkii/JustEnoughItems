package mezz.jei.api;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.gui.IGhostIngredientHandler;
import mezz.jei.api.gui.IGlobalGuiHandler;
import mezz.jei.api.gui.IGuiScreenHandler;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.IRecipeRegistryPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

/**
 * Entry point for the JEI API, functions for registering recipes are available from here.
 * The IModRegistry instance is passed to your mod plugin in {@link IModPlugin#register(IModRegistry)}.
 */
public interface IModRegistry {

	/**
	 * Get helpers and tools for implementing JEI plugins.
	 *
	 * @since JEI 2.27.0
	 */
	IJeiHelpers getJeiHelpers();

	/**
	 * Get useful functions relating to recipe ingredients.
	 *
	 * @since JEI 3.11.0
	 */
	IIngredientRegistry getIngredientRegistry();

	/**
	 * Add the recipes provided by your plugin.
	 * Handle them with {@link #handleRecipes(Class, IRecipeWrapperFactory, ResourceLocation)}.
	 * Recipes added here that already implement {@link IRecipeWrapper} do not need to add a handler.
	 *
	 * @since JEI 4.3.0
	 */
	void addRecipes(Collection<?> recipes, ResourceLocation recipeCategoryUid);

	/**
	 * Add a handler for recipes provided by your plugin.
	 * Recipes that already implement {@link IRecipeWrapper} do not need to add a handler here.
	 *
	 * @param recipeClass          the recipe class being handled.
	 * @param recipeWrapperFactory turns recipes into recipe wrappers.
	 * @param recipeCategoryUid    a unique category id. For vanilla category IDs, see {@link VanillaRecipeCategoryUid}.
	 * @since JEI 4.3.0
	 */
	<T> void handleRecipes(Class<T> recipeClass, IRecipeWrapperFactory<T> recipeWrapperFactory, ResourceLocation recipeCategoryUid);

	/**
	 * Add a clickable area on a gui to jump to specific categories of recipes in JEI.
	 *
	 * @param guiContainerClass  the gui class for JEI to detect.
	 * @param xPos               left x position of the clickable area, relative to the left edge of the gui.
	 * @param yPos               top y position of the clickable area, relative to the top edge of the gui.
	 * @param width              the width of the clickable area.
	 * @param height             the height of the clickable area.
	 * @param recipeCategoryUids the recipe categories that JEI should display.
	 */
	void addRecipeClickArea(Class<? extends GuiContainer> guiContainerClass, int xPos, int yPos, int width, int height, ResourceLocation... recipeCategoryUids);

	/**
	 * Add an association between an ingredient and what it can craft. (i.e. Furnace ItemStack -> Smelting and Fuel Recipes)
	 * Allows players to see what ingredient they need to craft in order to make recipes from a recipe category.
	 *
	 * @param catalystIngredient the ingredient that can craft recipes (like a furnace or crafting table)
	 * @param recipeCategoryUids the recipe categories handled by the ingredient
	 * @since JEI 4.5.0
	 */
	void addRecipeCatalyst(Object catalystIngredient, ResourceLocation... recipeCategoryUids);

	/**
	 * Add a handler to give JEI extra information about how to layout the item list next to a specific type of GuiContainer.
	 * Used for guis with tabs on the side that would normally intersect with JEI's item list.
	 */
	void addAdvancedGuiHandlers(IAdvancedGuiHandler<?>... advancedGuiHandlers);

	/**
	 * Add a handler to give JEI extra information about how to layout the item list.
	 * Used for guis that display next to GUIs and would normally intersect with JEI.
	 *
	 * @since JEI 4.14.0
	 */
	void addGlobalGuiHandlers(IGlobalGuiHandler... globalGuiHandlers);

	/**
	 * Add a handler to let JEI draw next to a specific class (or subclass) of {@link GuiScreen}.
	 * By default, JEI can only draw next to {@link GuiContainer}.
	 *
	 * @since JEI 4.8.4
	 */
	<T extends GuiScreen> void addGuiScreenHandler(Class<T> guiClass, IGuiScreenHandler<T> handler);

	/**
	 * Lets mods accept ghost ingredients from JEI.
	 * These ingredients are dragged from the ingredient list on to your gui, and are useful
	 * for setting recipes or anything else that does not need the real ingredient to exist.
	 *
	 * @since JEI 4.8.4
	 */
	<T extends GuiScreen> void addGhostIngredientHandler(Class<T> guiClass, IGhostIngredientHandler<T> handler);

	/**
	 * Add an info page for an ingredient.
	 * Description pages show in the recipes for an ingredient and tell the player a little bit about it.
	 *
	 * @param ingredient      the ingredient to describe
	 * @param ingredientType  the type of the ingredient
	 * @param descriptionKeys Localization keys for info text.
	 *                        New lines can be added with "\n" or by giving multiple descriptionKeys.
	 *                        Long lines are wrapped automatically.
	 *                        Very long entries will span multiple pages automatically.
	 * @since JEI 4.12.0
	 */
	<T> void addIngredientInfo(T ingredient, IIngredientType<T> ingredientType, String... descriptionKeys);

	/**
	 * Add an info page for multiple ingredients together.
	 * Description pages show in the recipes for an ingredient and tell the player a little bit about it.
	 *
	 * @param ingredients     the ingredients to describe
	 * @param ingredientType  the type of the ingredients
	 * @param descriptionKeys Localization keys for info text.
	 *                        New lines can be added with "\n" or by giving multiple descriptionKeys.
	 *                        Long lines are wrapped automatically.
	 *                        Very long entries will span multiple pages automatically.
	 * @since JEI 4.12.0
	 */
	<T> void addIngredientInfo(List<T> ingredients, IIngredientType<T> ingredientType, String... descriptionKeys);

	/**
	 * Get the registry for setting up recipe transfer.
	 */
	IRecipeTransferRegistry getRecipeTransferRegistry();

	/**
	 * Register your own Recipe Registry Plugin here.
	 *
	 * @see IRecipeRegistryPlugin
	 * @since JEI 3.12.0
	 */
	void addRecipeRegistryPlugin(IRecipeRegistryPlugin recipeRegistryPlugin);
}
