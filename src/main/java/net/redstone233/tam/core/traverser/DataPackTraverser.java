package net.redstone233.tam.core.traverser;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.redstone233.tam.core.validator.TamVersionValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DataPackTraverser {
    private static final Logger LOGGER = LoggerFactory.getLogger("DataPackTraverser");

    /**
     * 使用 DataPackTraverser 遍历和验证所有数据包
     * 返回验证结果列表
     */
    public static List<TamVersionValidator.ValidationResult> traverseAndValidateDatapacks(
            ResourceManager resourceManager) {
        return traverseAndValidateDatapacks(resourceManager, JsonOps.INSTANCE);
    }

    /**
     * 使用指定 DynamicOps 遍历和验证所有数据包
     */
    public static <T> List<TamVersionValidator.ValidationResult> traverseAndValidateDatapacks(
            ResourceManager resourceManager, DynamicOps<T> ops) {
        List<TamVersionValidator.ValidationResult> results = new ArrayList<>();

        try {
            // 使用 findAllResources 方法查找所有 pack.mcmeta 文件
            // 第一个参数是起始路径，第二个参数是过滤器
            Map<Identifier, List<Resource>> allPackMeta = resourceManager.findAllResources(
                    "", // 从根目录开始搜索
                    identifier -> identifier.getPath().endsWith("pack.mcmeta") // 只查找 pack.mcmeta 文件
            );

            LOGGER.info("开始遍历数据包，找到 {} 个 pack.mcmeta 位置", allPackMeta.size());

            // 处理每个找到的 pack.mcmeta 位置
            for (Map.Entry<Identifier, List<Resource>> entry : allPackMeta.entrySet()) {
                Identifier id = entry.getKey();
                List<Resource> resources = entry.getValue();

                // 每个位置可能有多个资源（由于资源包叠加），但我们只取第一个
                if (!resources.isEmpty() && isRootPackMcmeta(id)) {
                    processDatapackWithValidation(ops, resources.getFirst(), id, results);
                }
            }

        } catch (Exception e) {
            LOGGER.error("遍历数据包时发生错误", e);
            results.add(TamVersionValidator.ValidationResult.error(e.getMessage()));
        }

        return results;
    }

    /**
     * 处理单个数据包并进行验证
     */
    @SuppressWarnings("unchecked")
    private static <T> void processDatapackWithValidation(DynamicOps<T> ops, Resource resource,
                                                          Identifier id,
                                                          List<TamVersionValidator.ValidationResult> results) {
        try {
            String datapackName = extractDatapackName(id);
            LOGGER.debug("正在验证数据包: {}", datapackName);

            String jsonContent = readResourceContent(resource);
            JsonElement jsonElement = JsonParser.parseString(jsonContent);

            T input;
            if (ops == JsonOps.INSTANCE) {
                input = (T) jsonElement;
            } else {
                input = (T) jsonContent;
            }

            TamVersionValidator.ValidationResult result =
                    TamVersionValidator.validateDatapackConfig(ops, input, datapackName);
            results.add(result);

            if (result.valid()) {
                LOGGER.info("✓ 数据包 {} 验证通过", datapackName);
                if (result.message() != null && !result.message().getString().isEmpty()) {
                    LOGGER.info("  描述: {}", result.message().getString());
                }
            } else {
                LOGGER.warn("✗ 数据包 {} 验证失败: {}", datapackName, result.error());
            }

        } catch (Exception e) {
            LOGGER.error("处理数据包 {} 时发生错误", id, e);
            results.add(TamVersionValidator.ValidationResult.error(e.getMessage()));
        }
    }

    /**
     * 读取资源内容为字符串
     */
    private static String readResourceContent(Resource resource) {
        try {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }
            return content.toString();
        } catch (Exception e) {
            throw new RuntimeException("读取资源内容失败", e);
        }
    }

    /**
     * 检查是否是数据包根目录的 pack.mcmeta
     */
    private static boolean isRootPackMcmeta(Identifier id) {
        String path = id.getPath();
        // 数据包的 pack.mcmeta 应该直接在数据包根目录下
        // 路径格式可能是:
        // - "pack.mcmeta" (主数据包)
        // - "datapacks/some_pack/pack.mcmeta" (世界数据包)
        // - "fabricLoaderSpecific/pack.mcmeta" (Fabric 加载的数据包)
        return path.equals("pack.mcmeta") ||
                (path.contains("datapacks") && path.endsWith("/pack.mcmeta")) ||
                (path.endsWith("/pack.mcmeta") && !path.contains("/data/"));
    }

    /**
     * 从 Identifier 中提取数据包名称
     */
    private static String extractDatapackName(Identifier id) {
        String path = id.getPath();
        if (path.equals("pack.mcmeta")) {
            return "main_datapack";
        } else if (path.contains("/")) {
            // 提取路径中的最后一个目录名作为数据包名称
            String[] parts = path.split("/");
            if (parts.length >= 2) {
                // 如果是 datapacks/xxx/pack.mcmeta 格式，取 xxx
                for (int i = 0; i < parts.length - 1; i++) {
                    if (parts[i].equals("datapacks") && i + 1 < parts.length) {
                        return parts[i + 1];
                    }
                }
                // 否则取倒数第二个部分
                return parts[parts.length - 2];
            }
        }
        return "unknown_datapack_" + path.hashCode();
    }

    /**
     * 获取验证结果统计信息
     */
    public static ValidationStats getValidationStats(List<TamVersionValidator.ValidationResult> results) {
        long validCount = results.stream().filter(TamVersionValidator.ValidationResult::valid).count();
        long errorCount = results.size() - validCount;

        return new ValidationStats(results.size(), (int) validCount, (int) errorCount);
    }

    /**
     * 验证统计信息
     */
    public record ValidationStats(int total, int valid, int errors) {
        public double getSuccessRate() {
            return total > 0 ? (double) valid / total * 100 : 0;
        }

        @Override
        public @NotNull String toString() {
            return String.format("总计: %d, 通过: %d, 失败: %d, 成功率: %.1f%%",
                    total, valid, errors, getSuccessRate());
        }
    }

    /**
     * 替代方法：使用 getAllResources 查找特定位置的 pack.mcmeta
     */
    public static List<TamVersionValidator.ValidationResult> traverseWithGetAllResources(
            ResourceManager resourceManager) {
        List<TamVersionValidator.ValidationResult> results = new ArrayList<>();

        try {
            // 创建标识符来查找 pack.mcmeta
            Identifier packMcmetaId = Identifier.of("pack.mcmeta");

            // 使用 getAllResources 获取所有位置的 pack.mcmeta
            List<Resource> allResources = resourceManager.getAllResources(packMcmetaId);

            LOGGER.info("使用 getAllResources 找到 {} 个 pack.mcmeta 资源", allResources.size());

            for (Resource resource : allResources) {
                try {
                    String datapackName = "datapack_from_resource";
                    String jsonContent = readResourceContent(resource);

                    TamVersionValidator.ValidationResult result =
                            TamVersionValidator.validateDatapackFromString(jsonContent, datapackName);
                    results.add(result);

                } catch (Exception e) {
                    LOGGER.error("处理资源时发生错误", e);
                    results.add(TamVersionValidator.ValidationResult.error(e.getMessage()));
                }
            }

        } catch (Exception e) {
            LOGGER.error("使用 getAllResources 遍历数据包时发生错误", e);
            results.add(TamVersionValidator.ValidationResult.error(e.getMessage()));
        }

        return results;
    }
}