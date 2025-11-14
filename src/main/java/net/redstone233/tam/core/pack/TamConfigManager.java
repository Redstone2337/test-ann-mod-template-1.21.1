package net.redstone233.tam.core.pack;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.redstone233.tam.TestAnnMod;
import net.redstone233.tam.core.traverser.DataPackTraverser;
import net.redstone233.tam.core.traverser.FileSystemDatapackTraverser;
import net.redstone233.tam.core.validator.TamVersionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TamConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("TAMConfigManager");
    private static final List<TamVersionValidator.ValidationResult> VALIDATION_RESULTS = new ArrayList<>();
    private static MinecraftServer currentServer;

    public static void initialize() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return Identifier.of(TestAnnMod.MOD_ID, "datapack_validator");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        VALIDATION_RESULTS.clear();
                        LOGGER.info("开始使用 DataPackTraverser 验证 TAM 数据包配置...");

                        // 方法1: 使用 findAllResources 进行遍历（主要方法）
                        List<TamVersionValidator.ValidationResult> resourceManagerResults =
                                DataPackTraverser.traverseAndValidateDatapacks(manager);
                        VALIDATION_RESULTS.addAll(resourceManagerResults);

                        // 方法2: 使用 getAllResources 进行遍历（备用方法）
                        if (VALIDATION_RESULTS.isEmpty()) {
                            LOGGER.info("尝试使用 getAllResources 方法进行遍历...");
                            List<TamVersionValidator.ValidationResult> getAllResourcesResults =
                                    DataPackTraverser.traverseWithGetAllResources(manager);
                            VALIDATION_RESULTS.addAll(getAllResourcesResults);
                        }

                        // 方法3: 使用文件系统遍历（如果可用）
                        if (currentServer != null) {
                            LOGGER.info("使用文件系统遍历进行补充验证...");
                            List<TamVersionValidator.ValidationResult> fileSystemResults =
                                    FileSystemDatapackTraverser.traverseDatapacksFromFilesystem(currentServer);
                            VALIDATION_RESULTS.addAll(fileSystemResults);
                        }

                        DataPackTraverser.ValidationStats stats =
                                DataPackTraverser.getValidationStats(VALIDATION_RESULTS);
                        LOGGER.info("TAM 数据包验证完成: {}", stats);

                        printDetailedValidationResults();
                    }
                }
        );
    }

    public static void setServer(MinecraftServer server) {
        currentServer = server;
    }

    /**
     * 输出详细的验证结果
     */
    private static void printDetailedValidationResults() {
        DataPackTraverser.ValidationStats stats =
                DataPackTraverser.getValidationStats(VALIDATION_RESULTS);

        LOGGER.info("=== TAM 数据包验证详细报告 ===");
        LOGGER.info("{}", stats);

        if (stats.errors() > 0) {
            LOGGER.warn("失败的验证:");
            VALIDATION_RESULTS.stream()
                    .filter(result -> !result.valid())
                    .forEach(result -> LOGGER.warn("  - {}", result.error()));
        }

        // 显示成功的数据包信息
        long successfulWithDescription = VALIDATION_RESULTS.stream()
                .filter(result -> result.valid() && result.message() != null &&
                        !result.message().getString().isEmpty())
                .count();

        if (successfulWithDescription > 0) {
            LOGGER.info("成功验证的数据包描述:");
            VALIDATION_RESULTS.stream()
                    .filter(result -> result.valid() && result.message() != null &&
                            !result.message().getString().isEmpty())
                    .forEach(result -> LOGGER.info("  - {}", result.message().getString()));
        }

        LOGGER.info("=== 报告结束 ===");
    }

    /**
     * 获取所有验证结果
     */
    public static List<TamVersionValidator.ValidationResult> getValidationResults() {
        return new ArrayList<>(VALIDATION_RESULTS);
    }

    /**
     * 检查是否有验证失败的数据包
     */
    public static boolean hasValidationErrors() {
        return VALIDATION_RESULTS.stream().anyMatch(result -> !result.valid());
    }

    /**
     * 获取成功的验证结果
     */
    public static List<TamVersionValidator.ValidationResult> getSuccessfulValidations() {
        return VALIDATION_RESULTS.stream()
                .filter(TamVersionValidator.ValidationResult::valid)
                .toList();
    }

    /**
     * 获取失败的验证结果
     */
    public static List<TamVersionValidator.ValidationResult> getFailedValidations() {
        return VALIDATION_RESULTS.stream()
                .filter(result -> !result.valid())
                .toList();
    }

    /**
     * 手动触发验证（用于测试或其他用途）
     */
    public static void triggerManualValidation(ResourceManager resourceManager) {
        LOGGER.info("手动触发 TAM 数据包验证...");
        reload(resourceManager);
    }

    // 这个私有方法是为了在手动触发时重用重载逻辑
    private static void reload(ResourceManager manager) {
        // 这里可以调用重载逻辑，但需要重构现有的 reload 方法
        // 或者我们可以直接调用注册的监听器的 reload 方法
    }
}