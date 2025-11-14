package net.redstone233.tam.core.validator;

import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.text.Text;
import net.redstone233.tam.core.tool.PackMcmeta;
import net.redstone233.tam.core.tool.TamConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

public class TamVersionValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger("TAMVersionValidator");
    private static final int CURRENT_MOD_VERSION = 1;

    /**
     * 使用 DynamicOps 验证数据包配置
     */
    public static <T> ValidationResult validateDatapackConfig(DynamicOps<T> ops, T input, String datapackName) {
        try {
            DataResult<PackMcmeta> parseResult = PackMcmeta.parse(ops, input);

            if (parseResult.error().isPresent()) {
                String errorMsg = parseResult.error().get().message();
                LOGGER.warn("无法解析数据包 {} 的配置: {}", datapackName, errorMsg);
                return ValidationResult.parseError(errorMsg);
            }

            PackMcmeta config = parseResult.result().orElseThrow();
            TamConfig tamConfig = config.tam();

            // 检查配置版本兼容性
            if (CURRENT_MOD_VERSION >= 7 && tamConfig.getConfigVersion() == TamConfig.ConfigVersion.V1) {
                LOGGER.warn("数据包 {} 使用已弃用的 1.0 格式配置，建议升级到 2.0 格式", datapackName);
            }

            // 验证数据包格式
            int packFormat = config.pack().packFormat();
            if (!tamConfig.isValidPackFormat(packFormat)) {
                return ValidationResult.incompatibleFormat(packFormat, tamConfig);
            }

            // 显示重载消息
            if (!tamConfig.description().getString().isEmpty()) {
                LOGGER.info("数据包重载: {} - {}", datapackName, tamConfig.description().getString());
            }

            return ValidationResult.success(tamConfig.description());

        } catch (Exception e) {
            LOGGER.error("验证数据包 {} 时发生错误", datapackName, e);
            return ValidationResult.error(e.getMessage());
        }
    }

    /**
     * 专门处理字符串输入的验证方法（便捷方法）
     */
    public static ValidationResult validateDatapackFromString(String jsonContent, String datapackName) {
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonContent);
            return validateDatapackConfig(JsonOps.INSTANCE, jsonElement, datapackName);
        } catch (Exception e) {
            LOGGER.error("验证数据包 {} 时发生解析错误", datapackName, e);
            return ValidationResult.parseError("JSON 解析错误: " + e.getMessage());
        }
    }

    /**
     * 从文件路径验证数据包的便捷方法
     */
    public static ValidationResult validateDatapackFromPath(Path datapackPath) {
        try {
            Path mcmetaPath = datapackPath.resolve("pack.mcmeta");
            if (!Files.exists(mcmetaPath)) {
                return ValidationResult.missingConfig();
            }

            String jsonContent = Files.readString(mcmetaPath);
            String datapackName = datapackPath.getFileName().toString();

            return validateDatapackFromString(jsonContent, datapackName);

        } catch (Exception e) {
            LOGGER.error("验证数据包时发生错误: {}", datapackPath, e);
            return ValidationResult.error(e.getMessage());
        }
    }

    /**
     * 专门用于 DataPackTraverser 的验证方法
     */
    public static <T> boolean isValidDatapack(DynamicOps<T> ops, T input, String datapackName) {
        ValidationResult result = validateDatapackConfig(ops, input, datapackName);
        if (!result.valid()) {
            LOGGER.warn("数据包 {} 验证失败: {}", datapackName, result.error());
        }
        return result.valid();
    }

    public record ValidationResult(boolean valid, Text message, String error) {

        public static ValidationResult success(Text message) {
                return new ValidationResult(true, message, null);
            }

            public static ValidationResult missingConfig() {
                return new ValidationResult(false, null, "缺少 pack.mcmeta 文件");
            }

            public static ValidationResult parseError(String error) {
                return new ValidationResult(false, null, "解析错误: " + error);
            }

            public static ValidationResult incompatibleFormat(int format, TamConfig config) {
                return new ValidationResult(false, null,
                        String.format("数据包格式 %d 不兼容，支持的格式范围: %s",
                                format, getSupportedFormatsString(config)));
            }

            private static String getSupportedFormatsString(TamConfig config) {
                if (config.maxFormat().isPresent() && config.minFormat().isPresent()) {
                    return config.minFormat().get() + " - " + config.maxFormat().get();
                } else if (config.tamFormat().isPresent()) {
                    return "固定格式: " + config.tamFormat().get();
                } else if (config.supportedFormats().isPresent()) {
                    return "自定义格式范围";
                }
                return "未知";
            }

            public static ValidationResult error(String error) {
                return new ValidationResult(false, null, error);
            }
        }
}