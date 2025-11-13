package net.redstone233.tam.core.brewing;

import dev.latvian.mods.rhino.*;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RhinoBrewingRecipeParser {

    /* 临时队列，脚本解析阶段往里扔数据 */
    private static final List<RecipeTriple> QUEUE = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger("RhinoBrewingRecipeParser");

    /* 供 TestAnnMod 调用的入口 */
    public static void reloadAll(ResourceManager manager) {
        QUEUE.clear();                       // 热重载时先清空
        /* 注意：findResources 第二个参数是 Predicate<Identifier> */
        manager.findResources("scripts/brewing_recipes", id -> id.getPath().endsWith(".js"))
                .forEach((id, res) -> {
                    try (InputStreamReader reader = new InputStreamReader(res.getInputStream())) {
                        parseScript(reader);
                    } catch (Exception ex) {
                        TestAnnMod.LOGGER.error("Failed to load script {}", id, ex);
                    }
                });
        TestAnnMod.LOGGER.info("Brewing scripts reloaded. Queued {} recipes.", QUEUE.size());
    }

    public static void reloadAll() {
        QUEUE.clear();

        // 使用FabricLoader直接获取游戏目录，构建目标路径
        Path scriptPath = Path.of(FabricLoader.getInstance().getGameDir() + "/data/" + TestAnnMod.MOD_ID + "/scripts/brewing_recipes");
        File scriptDir = scriptPath.toFile();

        if (!scriptDir.exists() || !scriptDir.isDirectory()) {
            TestAnnMod.LOGGER.warn("Brewing script directory not found: {}", scriptPath);
            return;
        }

        File[] scriptFiles = scriptDir.listFiles((dir, name) -> name.endsWith(".js"));
        if (scriptFiles == null) {
            TestAnnMod.LOGGER.warn("No brewing script files found in: {}", scriptPath);
            return;
        }

        for (File scriptFile : scriptFiles) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(scriptFile))) {
                parseScript(reader);
                TestAnnMod.LOGGER.info("Successfully loaded brewing script: {}", scriptFile.getName());
            } catch (Exception ex) {
                TestAnnMod.LOGGER.error("Failed to load brewing script {}", scriptFile.getAbsolutePath(), ex);
            }
        }
        TestAnnMod.LOGGER.info("Brewing scripts reloaded from file system. Queued {} recipes.", QUEUE.size());
    }

    /* 注册到 Fabric 的入口，由 TestAnnMod.onInitialize 调用 */
    public static void registerWithFabric() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            for (RecipeTriple tr : QUEUE) {
                builder.registerPotionRecipe(tr.input, tr.ingredient, tr.output);
            }
            TestAnnMod.LOGGER.info("Registered {} potion recipes to Fabric.", QUEUE.size());
        });
    }

    /* 1. 外部入口：由 TestAnnMod.onInitialize 调用一次即可 */
    public static void registerWithFabricToDataPack() {
        /* 1.1 注册资源重载监听器 */
        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(TestAnnMod.MOD_ID, "brewing_script_loader");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        reloadAllDataPack(manager);        // 把 RM 传进来
                    }
                });

        /* 1.2 把队列注册到酿造配方 */
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            for (RecipeTriple tr : QUEUE) {
                builder.registerPotionRecipe(tr.input, tr.ingredient, tr.output);
            }
            LOGGER.info("Registered {} potion recipes to Fabric.", QUEUE.size());
        });
    }

    /* 2. 真正扫描：使用 ResourceManager */
    public static void reloadAllDataPack(ResourceManager manager) {
        QUEUE.clear();
        /* 扫描 data/tam/script 下所有 .js */
        manager.findResources("script", id -> id.getPath().endsWith(".js"))
                .forEach((id, resource) -> {
                    try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                        parseScript(reader);
                    } catch (Exception ex) {
                        TestAnnMod.LOGGER.error("Failed to load brewing script {}", id, ex);
                    }
                });
        LOGGER.info("Brewing scripts reloaded. Queued {} recipes.", QUEUE.size());
    }


    /* 解析脚本 */
    private static void parseScript(InputStreamReader reader) throws Exception {
        ContextFactory cf = new ContextFactory();
        Context cx = null;
        try {
            cx = cf.enter();
            Scriptable scope = cx.initStandardObjects();
            ScriptableObject.defineConstProperty(scope, String.valueOf(BrewingRecipeEventClass.class), cx);

            // 在ScriptableObject实例上调用defineFunctionProperties
            if (scope instanceof ScriptableObject scriptableScope) {
                scriptableScope.defineFunctionProperties(cx, new String[]{"brew"}, BrewingRecipeEventClass.class, ScriptableObject.DONTENUM);
            }

            cx.evaluateReader(scope, reader, "brewScript", 1, null);

        } catch (Exception ex) {
            TestAnnMod.LOGGER.error("Failed to parse brewing script", ex);
            throw ex;
        } finally {
            // 如果没有Context.exit()，尝试其他清理方式
            if (cx != null) {
                try {
                    // 尝试调用Context的退出方法（如果存在）
                    // 或者让ContextFactory处理清理
                    // 或者简单地设置为null让GC处理
                    cx = null;
                } catch (Exception e) {
                    TestAnnMod.LOGGER.warn("Error during context cleanup", e);
                }
            }
        }
    }

    /* 队列里的三元组 */
    private record RecipeTriple(RegistryEntry<Potion> input, Ingredient ingredient, RegistryEntry<Potion> output) {}

    /* Rhino 宿主类，仅负责把数据压入 QUEUE */
    public static class BrewingRecipeEventClass extends ScriptableObject {
        @Override
        public String getClassName() {
            return "BrewingRecipeEvent";
        }

        /* JS: BrewingRecipeEvent.create(e => {...}) */
        public static void create(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
            Scriptable event = cx.newObject(thisObj);
            Scriptable input = cx.newObject(event);
            Scriptable material = cx.newObject(event);
            Scriptable output = cx.newObject(event);

            ScriptableObject.putProperty(event, "input", input, cx);
            ScriptableObject.putProperty(event, "material", material, cx);
            ScriptableObject.putProperty(event, "output", output, cx);

            Function lambda = (Function) args[0];
            lambda.call(cx, thisObj, event, new Object[]{event});

            /* 收集结果 - 使用安全的类型检查 */
            RegistryEntry<Potion> inP = safeGetPotionEntry(input, "potion", cx);
            RegistryEntry<Potion> outP = safeGetPotionEntry(output, "potion", cx);
            Ingredient ing = parseIngredient(cx, material);
            if (inP != null && ing != null && outP != null) {
                QUEUE.add(new RecipeTriple(inP, ing, outP));
            }
        }

        /* JS: brew({input:{...}, material:..., output:{...}}) */
        public static void brew(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
            Scriptable obj = (Scriptable) args[0];
            RegistryEntry<Potion> inP = safeGetPotionEntry((Scriptable) ScriptableObject.getProperty(obj, "input", cx), "potion", cx);
            Ingredient ing = parseIngredient(cx, (Scriptable) ScriptableObject.getProperty(obj, "material", cx));
            RegistryEntry<Potion> outP = safeGetPotionEntry((Scriptable) ScriptableObject.getProperty(obj, "output", cx), "potion", cx);
            if (inP != null && ing != null && outP != null) {
                QUEUE.add(new RecipeTriple(inP, ing, outP));
            }
        }

        /* ---------- 安全类型检查方法 ---------- */
        private static RegistryEntry<Potion> safeGetPotionEntry(Scriptable obj, String propertyName, Context cx) {
            if (obj == null) return null;

            Object propertyValue = ScriptableObject.getProperty(obj, propertyName, cx);
            if (propertyValue == null || propertyValue == Undefined.INSTANCE) {
                return null;
            }

            // 如果是字符串，使用原有的解析逻辑
            if (propertyValue instanceof String potionId) {
                return parsePotionFromString(potionId);
            }

            // 如果是RegistryEntry，进行安全的类型转换
            if (propertyValue instanceof RegistryEntry) {
                @SuppressWarnings("unchecked")
                RegistryEntry<Potion> tempEntry = (RegistryEntry<Potion>) propertyValue;

                // 验证值是否为Potion类型
                if (tempEntry.value() instanceof Potion) {
                    return tempEntry;
                } else {
                    TestAnnMod.LOGGER.warn("RegistryEntry value is not a Potion: {}", tempEntry.value());
                    return null;
                }
            }

            TestAnnMod.LOGGER.warn("Expected String or RegistryEntry for potion, but got: {}",
                    propertyValue.getClass().getSimpleName());
            return null;
        }

        /* ---------- 从字符串解析药水的辅助方法 ---------- */
        private static RegistryEntry<Potion> parsePotionFromString(String potionId) {
            try {
                Potion potion = Registries.POTION.get(Identifier.of(potionId));
                if (potion != null) {
                    return Registries.POTION.getEntry(potion);
                } else {
                    TestAnnMod.LOGGER.warn("Unknown potion ID: {}", potionId);
                    return null;
                }
            } catch (Exception e) {
                TestAnnMod.LOGGER.warn("Failed to parse potion from ID: {}", potionId, e);
                return null;
            }
        }

        /* ---------- 原有的parsePotion方法（现在使用安全方法） ---------- */
        private static RegistryEntry<Potion> parsePotion(Context cx, Scriptable o) {
            return safeGetPotionEntry(o, "potion", cx);
        }

        private static Ingredient parseIngredient(Context cx, Scriptable o) {
            if (o == null) return null;
            Object itemRaw = ScriptableObject.getProperty(o, "item", cx);
            Object tagRaw  = ScriptableObject.getProperty(o, "tag", cx);
            if (itemRaw instanceof String itemId) {
                Item item = Registries.ITEM.get(Identifier.of(itemId));
                return item == Items.AIR ? Ingredient.empty() : Ingredient.ofItems(item);
            }
            if (tagRaw instanceof String tagId) {
                TagKey<Item> tag = TagKey.of(Registries.ITEM.getKey(), Identifier.of(tagId));
                return Ingredient.fromTag(tag);
            }
            return Ingredient.empty();
        }

        /* 链式空实现 */
        public static Scriptable toCreateBrewing(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
            return thisObj;
        }
    }
}