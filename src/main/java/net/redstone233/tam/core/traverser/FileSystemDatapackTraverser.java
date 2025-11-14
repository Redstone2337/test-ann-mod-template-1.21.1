package net.redstone233.tam.core.traverser;

import net.minecraft.server.MinecraftServer;
import net.redstone233.tam.core.validator.TamVersionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileSystemDatapackTraverser {
    private static final Logger LOGGER = LoggerFactory.getLogger("FileSystemDatapackTraverser");

    /**
     * 通过文件系统直接遍历数据包文件夹
     */
    public static List<TamVersionValidator.ValidationResult> traverseDatapacksFromFilesystem(MinecraftServer server) {
        List<TamVersionValidator.ValidationResult> results = new ArrayList<>();

        Path datapacksDir = server.getRunDirectory().getParent()
                .resolve("world")
                .resolve("datapacks");

        if (!Files.exists(datapacksDir)) {
            LOGGER.warn("数据包目录不存在: {}", datapacksDir);
            return results;
        }

        // 使用 try-with-resources 确保 Stream 被正确关闭
        try (var stream = Files.list(datapacksDir)) {
            // 在 try-with-resources 块内处理流
            stream.forEach(datapackPath -> {
                if (Files.isDirectory(datapackPath) || isZipFile(datapackPath)) {
                    validateDatapackFromPath(datapackPath, results);
                }
            });

        } catch (IOException e) {
            LOGGER.error("遍历数据包目录时发生错误", e);
        }

        return results;
    }

    /**
     * 验证单个数据包
     */
    private static void validateDatapackFromPath(Path datapackPath,
                                                 List<TamVersionValidator.ValidationResult> results) {
        try {
            Path packMcmetaPath;

            if (Files.isDirectory(datapackPath)) {
                packMcmetaPath = datapackPath.resolve("pack.mcmeta");
            } else {
                packMcmetaPath = getPackMcmetaFromZip(datapackPath);
            }

            if (packMcmetaPath != null && Files.exists(packMcmetaPath)) {
                TamVersionValidator.ValidationResult result =
                        TamVersionValidator.validateDatapackFromPath(datapackPath);
                results.add(result);
            } else {
                LOGGER.warn("数据包 {} 缺少 pack.mcmeta 文件", datapackPath.getFileName());
                results.add(TamVersionValidator.ValidationResult.missingConfig());
            }

        } catch (Exception e) {
            LOGGER.error("验证数据包 {} 时发生错误", datapackPath.getFileName(), e);
            results.add(TamVersionValidator.ValidationResult.error(e.getMessage()));
        }
    }

    /**
     * 检查是否为 ZIP 文件
     */
    private static boolean isZipFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        return fileName.endsWith(".zip") || fileName.endsWith(".jar");
    }

    /**
     * 从 ZIP 文件中提取 pack.mcmeta
     */
    private static Path getPackMcmetaFromZip(Path zipPath) {
        try (FileSystem zipFs = FileSystems.newFileSystem(zipPath, (ClassLoader) null)) {
            Path packMcmeta = zipFs.getPath("pack.mcmeta");
            if (Files.exists(packMcmeta)) {
                Path tempFile = Files.createTempFile("datapack_", ".mcmeta");
                Files.copy(packMcmeta, tempFile, StandardCopyOption.REPLACE_EXISTING);
                return tempFile;
            }
        } catch (Exception e) {
            LOGGER.error("读取 ZIP 数据包 {} 时发生错误", zipPath.getFileName(), e);
        }
        return null;
    }
}